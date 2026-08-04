# 📘 MCP Server

An MCP Server is the provider-side component of the Model Context Protocol architecture — a standalone process or service that exposes Tools, Resources, and Prompts to any compliant MCP Client over a standardized JSON-RPC 2.0 interface. Building one is conceptually similar to building a REST API, but instead of serving HTTP consumers, it serves LLM-driven hosts that discover and invoke its capabilities dynamically.

---

## 📖 What is an MCP Server?

**Definition:** An MCP Server is a program that implements the server-side of the MCP specification — registering Tools (actions), Resources (read-only data), and Prompts (templates), then listening for JSON-RPC requests over stdio or HTTP+SSE and responding according to the protocol contract.

**Why it exists:**
- AI hosts need a *predictable, self-describing* way to talk to any backend capability without hardcoded integration code.
- Teams owning a system (database, ticketing tool, filesystem) want to expose it once, safely, to any number of AI consumers.

**Problem it solves:**
- Removes the need to write bespoke "AI adapter code" per host application — you build the server once against the spec, and every compliant client can use it.

**Key characteristics:**

| Characteristic | Description |
|---|---|
| Self-describing | Advertises its own capabilities via `tools/list`, `resources/list`, `prompts/list` |
| Stateless or session-based | Can be stateless per request or maintain session context |
| Transport-flexible | Runs over stdio (local subprocess) or HTTP+SSE (remote/networked) |
| Independently deployable | Runs as its own process/service, decoupled from any specific host |
| Security boundary | Owns its own auth/consent enforcement — the protocol doesn't provide this for free |

> **Key Takeaway:** An MCP Server is the "provider" half of the MCP contract — think of it as a purpose-built microservice whose consumers are LLM-driven applications instead of other services.

---

## ❓ Why do we need it?

**Business Problem:** A platform team owning core banking, CRM, or ticketing systems wants to expose safe, well-governed access to AI copilots — without rewriting integration logic every time a new AI product wants access.

**Technical Problem:** Without a standard server contract, each AI host would require its own custom adapter, schema format, and auth handling for the same underlying system.

**Advantages:**
- One server, many consumers (chat copilot, IDE plugin, batch agent) — no duplicated integration logic
- Runtime discoverability means hosts don't need prior knowledge of the server's capabilities
- Clear ownership boundary: the team that owns the backend system also owns the server and its security posture

**Limitations of previous approaches:**
- Custom REST wrappers per AI tool led to duplicated auth/validation logic and inconsistent contracts across consumers
- No standard mechanism for a host to *discover* what a given integration could do at runtime

> **Key Takeaway:** Building an MCP Server shifts integration cost from "per AI consumer" to "per capability, once" — exactly the same value proposition a well-designed internal REST API brings to service-to-service communication.

---

## ⚙️ Internal Working

```
+------------------------+
| Server Startup          |
| - Register Tools        |
| - Register Resources    |
| - Register Prompts      |
+-----------+-------------+
            |
            v
+------------------------+
| Start Transport Listener|
| (stdio or HTTP+SSE)     |
+-----------+-------------+
            |
            v
+------------------------+
| Await incoming          |
| JSON-RPC request         |
+-----------+-------------+
            |
            v
+------------------------+
| Parse method:            |
| tools/list, tools/call,  |
| resources/read, etc.     |
+-----------+-------------+
            |
            v
+------------------------+
| Route to registered      |
| handler                  |
+-----------+-------------+
            |
            v
+------------------------+
| Validate input schema     |
| + Authorization check     |
+-----------+-------------+
            |
            v
+------------------------+
| Execute business logic    |
| (call DB / API / FS)      |
+-----------+-------------+
            |
            v
+------------------------+
| Return JSON-RPC response  |
| (result or error)         |
+------------------------+
```

**Step-by-step:**
1. Server boots and registers its capabilities (Tools, Resources, Prompts) in an internal registry.
2. Server opens its transport channel — stdio (spawned as a subprocess by the host) or HTTP+SSE (a running network service).
3. A connecting client first calls discovery methods (`tools/list`, etc.) — server responds with schemas/metadata.
4. On `tools/call`, the server routes to the matching handler by tool name.
5. Handler validates the incoming arguments against its declared schema and checks authorization.
6. Handler executes business logic against the backend system.
7. Server returns a structured JSON-RPC response — success payload or a typed error.

> **Key Takeaway:** An MCP Server's core job is routing + validation + execution — structurally identical to a REST controller layer, just speaking JSON-RPC instead of HTTP verbs/paths.

---

## 🏗 Architecture / Internal Components

| Component | Responsibility |
|---|---|
| **Capability Registry** | In-memory catalog of registered Tools/Resources/Prompts and their schemas |
| **Transport Listener** | Accepts connections over stdio or HTTP+SSE |
| **JSON-RPC Dispatcher** | Parses incoming requests, routes by method name to the right handler |
| **Schema Validator** | Ensures tool/resource arguments conform to declared JSON Schema before execution |
| **Handler / Business Logic Layer** | Executes the actual tool logic (equivalent to a service layer) |
| **Auth/Consent Layer** | Enforces who can call what — must be explicitly implemented by the server author |
| **Session Manager** | (Optional) Tracks per-connection state across multiple calls |
| **Backend Adapters** | Connectors to actual systems — DB repositories, REST clients, file I/O |

> **Key Takeaway:** A well-architected MCP Server mirrors a layered Spring Boot application: Controller (dispatcher) → Validation → Service (handler) → Repository (backend adapter).

---

## 🔄 Visual Flow

```
+--------------------+
| MCP Client (Host)   |
+---------+----------+
          |  tools/call
          v
+--------------------+
| Transport Layer      |
| (stdio / HTTP+SSE)    |
+---------+----------+
          |
          v
+--------------------+
| JSON-RPC Dispatcher   |
+---------+----------+
          |
          v
+--------------------+
| Tool Handler          |
| (validate + execute)  |
+---------+----------+
          |
          v
+--------------------+
| Backend System         |
| (DB / REST / FS)       |
+--------------------+
```

---

## 💻 Code Examples

### Basic Example — Minimal MCP Server (Java, conceptual SDK usage)

```java
public class SimpleMcpServer {

    public static void main(String[] args) {
        McpServer server = McpServer.builder()
                .name("ticketing-service")
                .version("1.0.0")
                .build();

        server.addTool(
            Tool.builder()
                .name("create_ticket")
                .description("Creates a support ticket")
                .inputSchema("{\"title\": \"string\", \"priority\": \"string\"}")
                .handler(req -> {
                    String title = req.getArgument("title");
                    String priority = req.getArgument("priority");
                    String ticketId = TicketRepository.create(title, priority); // business logic
                    return ToolResult.success(Map.of("ticketId", ticketId));
                })
                .build()
        );

        server.start(Transport.STDIO);
    }
}
```

### Intermediate Example — Registering Multiple Tools + a Resource

```java
McpServer server = McpServer.builder().name("hr-service").version("1.0.0").build();

server.addTool(
    Tool.builder()
        .name("apply_leave")
        .description("Submits a leave application")
        .inputSchema(LeaveRequestSchema.SCHEMA)
        .handler(req -> {
            LeaveRequest leave = req.parseAs(LeaveRequest.class);
            String appId = LeaveService.apply(leave);
            return ToolResult.success(Map.of("applicationId", appId));
        })
        .build()
);

server.addResource(
    Resource.builder()
        .uri("hr://policy/leave-policy")
        .name("Leave Policy Document")
        .mimeType("text/markdown")
        .handler(uri -> ResourceContent.text(PolicyRepository.getLeavePolicy()))
        .build()
);
```

### Production Example — With Schema Validation, Auth, Rate Limiting, and Audit

```java
server.addTool(
    Tool.builder()
        .name("update_customer_record")
        .description("Updates a customer's profile — restricted, audited operation")
        .inputSchema(CustomerUpdateSchema.SCHEMA)
        .handler(req -> {
            // 1. Rate limiting per caller
            if (!RateLimiter.allow(req.getPrincipal())) {
                return ToolResult.error("RATE_LIMITED", "Too many requests");
            }

            // 2. Authorization
            if (!AuthService.hasRole(req.getPrincipal(), "CRM_WRITE")) {
                return ToolResult.error("UNAUTHORIZED", "Missing CRM_WRITE role");
            }

            // 3. Schema-validated parse (framework auto-validates against inputSchema)
            CustomerUpdateRequest update = req.parseAs(CustomerUpdateRequest.class);

            // 4. Business logic
            CustomerService.update(update);

            // 5. Audit trail
            AuditLogger.log("CUSTOMER_UPDATE", req.getPrincipal(), update);

            return ToolResult.success(Map.of("status", "UPDATED"));
        })
        .build()
);
```

### Spring Boot Integration Example — Running an MCP Server Alongside a REST API

```java
@Configuration
public class McpServerConfig {

    @Bean
    public McpServer mcpServer(CustomerService customerService) {
        McpServer server = McpServer.builder()
                .name("crm-mcp-server")
                .version("1.0.0")
                .build();

        server.addTool(
            Tool.builder()
                .name("get_customer")
                .description("Fetches customer profile by ID")
                .handler(req -> {
                    String custId = req.getArgument("custId");
                    return ToolResult.success(customerService.getProfile(custId));
                })
                .build()
        );

        return server;
    }

    // Runs the MCP server on its own HTTP+SSE port, independent of the main Spring MVC port
    @Bean
    public ApplicationRunner startMcpServer(McpServer mcpServer) {
        return args -> mcpServer.start(Transport.HTTP_SSE);
    }
}
```

> ⚠️ **Note:** Exact MCP Java SDK class/method names are evolving — verify against the current official SDK release before using verbatim in production code or citing exact API names in an interview.

---

## 🏦 Real Production Example

**Insurance — Claims Processing MCP Server:**
An insurance company builds a single **Claims MCP Server** exposing:
- Tool `submit_claim` — validates and files a new claim
- Tool `check_claim_status` — read-only status lookup
- Resource `claims://policy-terms/{policyId}` — policy document text for context
- Prompt `claim-summary-template` — standardized prompt for generating claim summaries

This one server is reused by: an internal adjuster copilot, a customer-facing chatbot (with stricter tool permissions), and a nightly batch-reconciliation AI agent — each host connects independently, but the claims team maintains and secures **one** codebase instead of three separate integrations.

> **Key Takeaway:** The server owner defines the contract once; different hosts can be granted different subsets of tool access (e.g., customer-facing bots get read-only tools, internal copilots get write access) — the same server, different trust levels per consumer.

---

## ⚠ Common Mistakes

| Wrong Approach | Problem | Correct Approach |
|---|---|---|
| No schema validation on tool inputs | LLM-generated or malformed arguments crash handlers or cause unsafe execution | Always validate against a strict JSON Schema before executing business logic |
| Blocking I/O in tool handlers | Long DB/API calls block the server thread, degrading throughput | Use async/non-blocking execution for slow backend calls |
| Exposing every internal operation as a Tool | Increases attack surface, gives the LLM excessive capability | Expose only the minimum necessary operations (principle of least privilege) |
| No distinction between read (Resource) and write (Tool) operations | Blurs safety boundaries — LLM can't reason about what's "safe" to call freely | Model read-only data as Resources, side-effecting actions as Tools |
| Running the MCP server with no authentication on HTTP+SSE transport | Any network client could invoke tools | Enforce API keys/OAuth/mTLS at the transport layer for remote servers |

> **Production impact:** A MCP Server without authorization checks on a `create_ticket` or `update_customer_record` tool is equivalent to publishing an unauthenticated write API — a serious production and compliance risk in regulated industries.

---

## ✅ Best Practices

- **Coding Standards:** Treat each Tool's `inputSchema` as a versioned API contract; document clearly like OpenAPI
- **Performance:** Use async handlers for slow I/O; avoid heavy computation on the request thread
- **Thread Safety:** Design handlers to be stateless, or use proper concurrency controls for shared state
- **Scalability:** Deploy HTTP+SSE-based servers behind a load balancer like any stateless microservice
- **Security:** Enforce auth/authorization per tool; require explicit consent flows for side-effecting operations
- **Logging:** Structured, correlation-ID-based logs for every `tools/call`/`resources/read` invocation
- **Monitoring:** Expose metrics (latency, error rate, call volume) per tool — treat each tool like an API endpoint in your dashboards

---

## ⚡ Performance Considerations

| Aspect | Consideration |
|---|---|
| CPU | Overhead is in JSON-RPC parsing/schema validation — usually negligible vs. backend call cost |
| Memory | Avoid loading large Resource payloads fully into memory — stream where possible |
| Time Complexity | Bound by handler implementation, not the protocol itself |
| Scalability | HTTP+SSE servers scale horizontally; stdio servers are inherently single-process/local |
| Network | SSE connections are long-lived — plan for connection limits and timeouts |
| Caching | Cache frequently-read Resources with low volatility |
| Thread Usage | Use a bounded thread pool or async/reactive execution for concurrent tool calls |

---

## ⚖ Trade-offs

**Advantages:** Reusable across hosts, self-describing, clear ownership boundary, independently deployable and scalable.

**Disadvantages:** Requires building and operating a new service; security/consent logic must be designed explicitly; adds an extra hop compared to in-process function calling.

**When to use:** You're exposing a capability that multiple AI applications will need, now or in the future, and want centralized governance over how it's accessed.

**When NOT to use:** A single AI app needs a narrow, one-off capability with no reuse potential — direct function calling inside that app is simpler.

**Real-life analogy:** An MCP Server is like a **bank branch counter** — it exposes a fixed, well-defined set of services (deposit, withdraw, check balance) to any customer (host) who walks up, rather than every customer needing a private backdoor into the vault.

---

## 📊 Comparison Table

| Feature | MCP Server | REST API Service | In-Process Function Calling |
|---|---|---|---|
| Consumer type | AI hosts (LLM-driven) | Any HTTP client | Single AI app only |
| Discovery | Runtime (`tools/list`) | Static (OpenAPI/Swagger) | None — hardcoded |
| Reusability | High, across multiple hosts | High, across services | None |
| Deployment | Independent process (stdio or networked) | Independent service | Embedded in the app |
| Best Use Case | Multi-host AI tool/data exposure | General service-to-service integration | Rapid single-app prototyping |

---

## 🎯 Interview Questions

1. **Q: What is the core responsibility of an MCP Server?**
   **A:** Registering and exposing Tools, Resources, and Prompts, then routing and executing JSON-RPC requests from clients.
   *Follow-up: How does this compare structurally to a REST controller layer?*

2. **Q: What transports can an MCP Server run over, and how do they differ operationally?**
   **A:** stdio (spawned as a local subprocess by the host, no networking) vs. HTTP+SSE (a standalone networked service, requires its own auth/security).
   *Follow-up: Which would you choose for a server accessed by multiple remote teams, and why?*

3. **Q: How does an MCP Server enforce security, given the protocol doesn't provide it automatically?**
   **A:** The server author must explicitly implement authentication, authorization, and consent gating within tool handlers.
   *Follow-up: How would you design role-based tool access for different host consumers?*

4. **Q: What's the difference between modeling something as a Tool vs. a Resource on the server?**
   **A:** Tools represent actions with potential side effects; Resources represent read-only, passive data.
   *Follow-up: Why does this distinction matter for LLM safety reasoning?*

5. **Q: How would you validate tool input arguments safely?**
   **A:** Enforce a strict JSON Schema on every tool's `inputSchema`, and reject/parse-validate before executing business logic — never trust LLM-generated arguments blindly.
   *Follow-up: What happens if you skip this step and the LLM hallucinates a malformed argument?*

6. **Q: How do you scale an MCP Server under high concurrent load?**
   **A:** Use async/non-blocking handlers, deploy multiple instances behind a load balancer (for HTTP+SSE), and avoid blocking I/O on the request thread.
   *Follow-up: What changes if the server must maintain session state across calls?*

7. **Q: What's the risk of exposing too many tools from a single MCP Server?**
   **A:** Increases the attack surface and gives the connected LLM excessive capability — violates least-privilege principles.
   *Follow-up: How would you split one large server into multiple bounded-context servers?*

8. **Q: How would you version a Tool's schema without breaking existing hosts?**
   **A:** Treat it like API versioning — additive, backward-compatible schema changes; deprecate old fields gracefully rather than removing them abruptly.
   *Follow-up: What's your rollback strategy if a schema change breaks a production host?*

9. **Q: How do you test an MCP Server independent of any AI host?**
   **A:** Use a test MCP client (or manually craft JSON-RPC requests) to call `tools/list` and `tools/call` directly, verifying schema and handler behavior without needing an LLM in the loop.
   *Follow-up: How would you integrate this into a CI pipeline?*

10. **Q: What auditing considerations are critical for an MCP Server in a regulated industry (e.g., banking)?**
    **A:** Every tool invocation should be logged with caller identity, arguments, timestamp, and result — enabling full traceability for compliance.
    *Follow-up: How would you ensure logs don't leak sensitive data (e.g., account numbers) while remaining useful for audits?*

---

## 🧠 Scenario-Based Questions

- **What happens if** two different hosts connect to the same MCP Server simultaneously and both call a stateful tool — how do you isolate their sessions?
- **How would you optimize** an MCP Server whose `search_records` tool handler is causing high database load under concurrent LLM-driven calls?
- **How would you troubleshoot** a scenario where `tools/list` returns correctly, but every `tools/call` fails with a schema validation error?
- **How would you design** an MCP Server for a loan-processing system where some tools (read status) are safe for a customer-facing bot, but others (approve loan) must be restricted to internal staff-only hosts?
- **How would you secure** an HTTP+SSE MCP Server exposed across multiple internal teams with different trust levels?

---

## 🛠 Debugging Tips

- **Common production issues:** schema drift between client expectations and server's declared schema; auth token expiry mid-session; blocking handlers causing SSE connection timeouts.
- **Logs:** Correlate every request/response pair with a request ID; log tool name, caller identity, and outcome (success/error code).
- **Monitoring:** Track per-tool call volume, p95/p99 latency, and error rate — alert on anomalous spikes (potential misuse or bug).
- **Troubleshooting:** Manually replay a failing `tools/call` payload against the server in isolation to separate "LLM picked wrong arguments" from "server-side bug."
- **Debugging techniques:** Enable verbose JSON-RPC-level logging during integration testing; validate all tool schemas with an automated JSON Schema linter in CI.

---

## 📌 When to Use an MCP Server

- You're exposing a capability (data or action) that multiple AI hosts/teams will need over time
- You want centralized governance, auditing, and versioning of an AI-accessible capability
- You're building a platform-level integration meant to outlive any single AI application

## 🚫 When NOT to Use an MCP Server

- A single, short-lived prototype with no reuse requirement across hosts
- Extremely latency-sensitive, tightly-coupled logic where the extra process/network hop isn't justified
- Simple internal scripts where the overhead of building and operating a server outweighs the benefit

---

## 💡 Interview Tips

- **What the interviewer expects:** Understanding that an MCP Server is architecturally similar to a well-designed microservice — not something exotic. Draw explicit parallels to REST API design.
- **Common mistakes:** Assuming the protocol provides security automatically; conflating Tools and Resources; not discussing schema versioning or least-privilege tool design.
- **How to answer confidently:** Frame answers around familiar architecture patterns (layered service design, API contracts, least privilege) rather than treating MCP as a brand-new paradigm.
- **Follow-ups to expect:** Auth/consent design, schema versioning, scaling strategy, testing approach without a live LLM.

---

## 📝 Short Conclusion Summary

An MCP Server is the provider-side implementation of the Model Context Protocol — a standalone, independently deployable process that registers Tools, Resources, and Prompts and serves them to any compliant AI host over JSON-RPC, via stdio or HTTP+SSE. Architecturally, it mirrors a well-layered microservice: a dispatcher routes requests, handlers validate input and enforce authorization, and business logic executes against backend systems. Because the protocol itself doesn't provide security, authentication, authorization, and consent gating for side-effecting tools must be explicitly designed by the server author — a critical consideration in regulated domains. Done well, one MCP Server can be safely and efficiently reused across every current and future AI consumer that needs that capability.

---

## ⚡ TL;DR

| Topic | Summary |
|--------|---------|
| Purpose | Expose Tools/Resources/Prompts to any MCP-compliant AI host |
| Internal Working | Register capabilities → listen on transport → dispatch → validate → execute → respond |
| Best For | Reusable, governed AI-accessible capabilities across multiple hosts |
| Avoid | One-off, single-host, low-reuse integrations |
| Advantages | Reusable, self-describing, independently deployable, clear ownership |
| Disadvantages | Requires explicit security design; adds a service/process to operate |
| Performance | Overhead is protocol-thin; bottleneck is usually the backend call |
| Interview Keyword | "Server owns capability + security; protocol owns discovery + transport" |

---

## 🚀 30-Second Interview Answer

> "An MCP Server is the provider-side component in Model Context Protocol — it registers Tools for actions, Resources for read-only data, and Prompts for templates, then serves them to any compliant AI host over JSON-RPC, either locally via stdio or remotely via HTTP with Server-Sent Events. Structurally, it's very similar to a layered microservice: a dispatcher routes incoming calls by method name, handlers validate arguments against a declared schema and enforce authorization, then execute business logic against the actual backend system — a database, an API, or a file store. The key thing to get right is that MCP itself doesn't provide security — authentication, authorization, and consent for side-effecting tools have to be explicitly built into the server, which matters a lot in regulated domains like banking or insurance. Done properly, you build the server once, and every current and future AI host that needs that capability can reuse it."

---

## Useful Resources

- **Official Specification:** modelcontextprotocol.io
- **Reference SDKs:** Search "Model Context Protocol GitHub" for official Java/Python/TypeScript server SDKs and example implementations
- **Anthropic Engineering Blog:** anthropic.com/news — search "Model Context Protocol"
- **Comparative reading:** JSON-RPC 2.0 specification; OpenAPI/Swagger for schema-contract design patterns you can map directly onto MCP tool schemas

> ⚠️ **Accuracy Note:** MCP is an actively evolving specification, and server SDK APIs may have changed since my January 2026 knowledge cutoff. Verify exact class/method names, transport details, and security recommendations against the official spec before an interview or production implementation — I don't have live web search enabled in this conversation.