# 📘 Interview Handbook: Site Reliability Engineering (SRE)

> **Audience:** Senior Java Developers (10–15+ years), Technical Leads, and Solution Architects  
> **Focus:** Applying software engineering to production operations, with Java service examples  
> **Terminology:** SRE means **Site Reliability Engineering**; an SRE is the engineer practicing it.

Site Reliability Engineering is an engineering discipline for operating services reliably at scale. It connects software development with production operations through measurable service objectives, automation, observability, incident response, and continuous learning. For a Java team, SRE means designing the service and its operating model together: the JVM, application, dependencies, deployment process, and on-call response all affect the customer experience.

---

## 📖 What Is Site Reliability Engineering?

- **Definition:** SRE applies software engineering methods to operational problems so services can meet a defined level of reliability as they grow.
- **Why it exists:** Manual operations and informal reliability goals do not scale with distributed systems, frequent releases, and growing customer expectations.
- **Problem it solves:** It gives product and operations teams shared measures and repeatable engineering practices to manage reliability, change risk, and operational workload.
- **Key characteristics:** Measurable objectives; automation; production ownership; actionable monitoring; blameless learning; capacity and failure planning; explicit trade-offs between reliability and delivery speed.
- **SRE is not simply:** A renamed operations team, a monitoring dashboard, or a promise of 100% uptime. Team responsibilities vary by organization, but the engineering and measurement principles remain useful.

**Key Takeaway:** SRE makes reliability an explicit engineering outcome, measured from the user’s perspective and supported by automation and operational learning.

---

## ❓ Why Do We Need It?

| Aspect | Explanation |
|---|---|
| Business problem | Customers expect services to work during peak demand, deployments, and dependency failures; outages affect revenue, trust, and compliance. |
| Technical problem | Distributed systems fail partially: a Java service can be healthy while its database, queue, DNS, or downstream API is not. |
| Previous approach and limits | “Keep it up” goals, host-only monitoring, and manual runbooks don’t define acceptable customer impact or scale with service complexity. |
| Advantages | Shared reliability targets, earlier detection of user-impacting failures, safer releases, less repetitive toil, and better incident learning. |
| Limitations | SLOs and instrumentation take effort; metrics can be misleading; automation can amplify mistakes; reliability work competes with feature work. |

SRE uses a **service level indicator (SLI)** to measure a customer-relevant outcome, a **service level objective (SLO)** to set the target, and often an **error budget** to make the trade-off between reliability and change explicit. An **SLA** is a formal commitment, often contractual, with consequences; it is not interchangeable with an internal SLO.

**Key Takeaway:** SRE is useful when teams need a shared, evidence-based way to decide what reliability means and what to do when it falls short.

---

## ⚙️ Internal Working

A practical SRE loop connects design, measurement, response, and improvement:

1. **Define the service and user journey.** Identify what the customer needs to succeed, such as submitting a payment, not merely whether a process is running.
2. **Choose SLIs.** Measure successful eligible requests, latency, freshness, or another outcome. Define exclusions and aggregation carefully.
3. **Set SLOs and windows.** Agree on targets with product and engineering stakeholders. A target is a decision, not a universal industry number.
4. **Instrument and observe.** Emit metrics, logs, and traces; alert on urgent symptoms and provide diagnostics for causes.
5. **Manage error budget.** Compare observed reliability with the objective. If the budget is being consumed rapidly, reduce risky changes and prioritize reliability work according to an agreed policy.
6. **Respond and learn.** Mitigate incidents, communicate clearly, and turn blameless postmortem findings into owned engineering actions.
7. **Reduce toil and improve design.** Automate repetitive work and fix recurring causes; verify that automation is safe, observable, and reversible.

```text
Customer journey
      |
      v
SLI measurement ---> SLO target ---> Error-budget policy
      |                                  |
      v                                  v
Dashboards and alerts <--- Incident response / release decisions
      |                                  |
      +------> Learning and engineering improvements <---+
```

**Key Takeaway:** SRE is a continuous feedback loop: measure the customer outcome, respond to risk, and engineer away recurring failure and toil.

---

## 🏗 Architecture / Internal Components

| Component | Responsibility | Senior-level consideration |
|---|---|---|
| Service / ownership boundary | Defines the system, dependencies, user journeys, and accountable team. | A service boundary should reflect how it is operated and measured, not only how code is packaged. |
| SLI | Quantifies a service outcome, such as successful requests or latency distribution. | Define eligible events, denominator, labels, and missing-data behavior; avoid ambiguous “uptime.” |
| SLO | States the target and measurement window for one or more SLIs. | Set a target aligned with user needs and cost; more “nines” require disproportionate investment. |
| Error budget | The allowed unreliability implied by the objective over its window. | Pair the number with a policy: who acts, what changes, and how exceptions are handled. |
| Telemetry pipeline | Collects, processes, and routes metrics, logs, and traces. | Control cardinality, sampling, retention, access, and telemetry cost. OpenTelemetry provides vendor-neutral instrumentation and collection, not the storage/visualization backend. |
| Alerting and on-call | Notifies a responder when urgent action can reduce user harm. | Page on actionable symptoms; route non-urgent work to tickets. Avoid paging on every internal threshold. |
| Release / change controls | Deploys changes progressively and detects regressions. | Use health gates, canaries, rollback paths, and change correlation; a successful deployment is not proof of a healthy service. |
| Incident management | Coordinates mitigation, communication, and restoration. | Separate incident command, operations, and communications for larger events. Preserve a blameless learning culture. |
| Capacity and resilience | Plans for demand, saturation, and dependency failure. | Load shedding, timeouts, bounded queues, retries, bulkheads, and graceful degradation must be designed together. |

**Key Takeaway:** SRE depends on clear ownership and measurement as much as on tooling; every component should connect to a reliability decision.

---

## 🔄 Visual Flow

```text
+------------------+     +------------------+     +-------------------+
| Client request   | --> | Java service     | --> | DB / downstream   |
+------------------+     +--------+---------+     +-------------------+
                                  |
                     metrics / logs / traces
                                  |
                                  v
                       +--------------------+
                       | Telemetry backend  |
                       +---------+----------+
                                 |
                   SLI / SLO evaluation and alert
                                 |
                                 v
                    +--------------------------+
                    | On-call mitigation       |
                    | rollback / shed / repair |
                    +------------+-------------+
                                 |
                                 v
                       Postmortem and actions
```

**Key Takeaway:** A production signal must lead to an understandable decision or action; collecting telemetry alone does not create reliability.

---

## 💻 Code Examples

The snippets illustrate patterns, not a complete monitoring stack. Production code also needs configuration, tests, access controls, and backend-specific integration.

### Basic Example: Make a Health Probe Meaningful

```java
// Spring Boot Actuator exposes health endpoints when the dependency is configured.
// Keep liveness independent of remote dependencies to avoid restart cascades.
@Component
class PaymentReadiness implements HealthIndicator {
    private final PaymentRepository repository;

    PaymentReadiness(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        // Readiness may include a bounded dependency check; do not run an unbounded query.
        return repository.isAvailable()
                ? Health.up().withDetail("dependency", "payments-db").build()
                : Health.down().withDetail("dependency", "payments-db").build();
    }
}
```

**Design note:** Liveness answers “should this process be restarted?” Readiness answers “should this instance receive traffic?” Treating every dependency issue as liveness failure can cause restart storms.

### Intermediate Example: Bound a Downstream Call

```java
// Set a finite timeout so a slow dependency cannot hold request threads indefinitely.
// Use a resilience library or HTTP client configuration appropriate to the application.
@Bean
RestClient ledgerClient(RestClient.Builder builder) {
    HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(2)); // Example budget; derive from the end-to-end SLO.

    return builder
            .requestFactory(new ReactorClientHttpRequestFactory(httpClient))
            .baseUrl("${ledger.base-url}")
            .build();
}
```

**Design note:** A timeout bounds waiting but does not guarantee safe retries. Retry only when the operation is idempotent or protected by an idempotency key; cap attempts and total time.

### Production Example: Record a User-Outcome Counter

```java
// Record outcomes at the service boundary using a bounded label set.
// Never put account IDs, request IDs, or arbitrary URLs in metric labels.
@Component
class PaymentMetrics {
    private final Counter accepted;
    private final Counter rejected;

    PaymentMetrics(MeterRegistry registry) {
        this.accepted = Counter.builder("payment.requests")
                .tag("outcome", "accepted")
                .register(registry);
        this.rejected = Counter.builder("payment.requests")
                .tag("outcome", "rejected")
                .register(registry);
    }

    void record(boolean succeeded) {
        // In a real service, define eligible outcomes and expected business rejections explicitly.
        (succeeded ? accepted : rejected).increment();
    }
}
```

**Design note:** A useful SLI needs a trustworthy denominator and consistent semantics. Instrumentation should distinguish service failure from expected business rejection, while avoiding sensitive data and unbounded metric cardinality.

### Spring Boot Example (if applicable)

```yaml
# Example only: expose health and Prometheus metrics on a protected management interface.
management:
  endpoints:
    web:
      exposure:
        include: health,prometheus
  endpoint:
    health:
      probes:
        enabled: true
  server:
    # Use a separate management port/network policy where appropriate.
    port: 8081
```

**Design note:** Secure management endpoints, expose only what operators need, and ensure Kubernetes or another orchestrator uses liveness/readiness correctly. A green health endpoint is not a substitute for customer-facing SLO monitoring.

**Key Takeaway:** Production-grade Java SRE starts with bounded work, reliable outcome instrumentation, and health semantics that prevent cascading failure.

---

## 🏦 Real Production Example

**Scenario: Online banking payment authorization**

1. **Context:** The payment API calls a ledger service and a fraud service. A successful HTTP response from the API is not enough if the payment is not actually authorized or recorded.
2. **SLIs:** Eligible authorization success ratio; end-to-end latency distribution; and ledger event freshness/completion where asynchronous processing is part of the user promise.
3. **SLO:** Product, risk, and engineering agree a target and window based on customer need and architecture. For example, a team might start discussion with 99.9% successful eligible requests over 30 days; this is an illustrative target, not a universal banking standard.
4. **Failure behavior:** Set per-dependency timeouts, avoid unbounded retries, use idempotency keys, and define whether a fraud-service outage fails closed or uses an explicitly approved fallback. Never silently report payment success before the system’s defined commit point.
5. **Operations:** Alert on rapid SLO burn or sustained user-visible failure; correlate traces with sanitized payment references; protect account and card data in logs.
6. **Learning:** A postmortem identifies a pool-exhaustion incident after a slow ledger dependency. Actions include bounded connection pools, queue limits, load shedding, dependency dashboards, and a tested rollback/runbook.

**Key Takeaway:** SRE makes correctness, user impact, and failure policy explicit for the whole payment journey, not just the Java process.

---

## ⚠ Common Mistakes

| Wrong approach | Production problem | Correct approach |
|---|---|---|
| Define reliability as “all hosts are up.” | Hosts can be green while requests fail or exceed latency expectations. | Measure customer-facing outcomes and keep infrastructure signals as diagnostic context. |
| Set an arbitrary 99.999% target. | Cost and architecture may not justify the extra reliability; teams may game the measure. | Derive SLOs from user needs, business impact, and achievable design. |
| Page on CPU, heap, or every exception by default. | Alert fatigue hides real incidents; internal causes may not affect users. | Page for urgent actionable symptoms; use dashboards/tickets for investigation and capacity work. |
| Retry every failed call. | Retries multiply load during overload and duplicate non-idempotent operations. | Use deadlines, bounded retries with backoff/jitter, idempotency, and load shedding. |
| Treat a pod restart as recovery. | It can erase evidence or amplify an outage through restart loops. | Diagnose the failure mode, preserve telemetry, and use probes with distinct semantics. |
| Run a blameless postmortem but never close actions. | The same incident recurs and responders lose trust. | Assign owners and due dates; track systemic fixes and verify their effect. |

**Key Takeaway:** SRE failures often come from poor measurement and unsafe operational behavior, not a lack of dashboards.

---

## ✅ Best Practices

- **Coding standards:** Design for explicit timeouts, idempotency, bounded queues, clear failure types, and graceful shutdown.
- **Performance:** Profile representative traffic; watch tail latency, allocation, GC pauses, thread pools, connection pools, and queue depth.
- **Thread safety:** Keep shared state minimal; bound executor queues and concurrency; do not hide blocking calls on event-loop threads.
- **Scalability:** Load test realistic dependencies and failure modes; define capacity and overload behavior before peak traffic.
- **Logging:** Use structured logs and correlation/trace IDs; redact credentials, tokens, personal data, and payment details.
- **Monitoring:** Track latency, traffic, errors, and saturation. Alert on symptoms that are urgent and actionable; link alerts to runbooks and dashboards.
- **Change safety:** Canary risky changes, evaluate health against SLO signals, and maintain a tested rollback or mitigation path.
- **Incident learning:** Write blameless postmortems focused on contributing conditions and system improvements; verify action items.
- **Toil:** Measure repetitive operational work and automate it safely; reserve engineering capacity to eliminate recurring sources.
- **Security and resilience:** Include security failure, dependency degradation, regional failure, backup restoration, and access control in reliability planning.

**Key Takeaway:** Reliable services are built by safe defaults, controlled change, useful signals, and sustained investment in eliminating repeat incidents.

---

## ⚡ Performance Considerations

| Dimension | What to assess | Typical SRE response |
|---|---|---|
| CPU | Request cost, serialization, compression, contention, GC CPU | Profile; optimize measured hot paths; scale only after understanding efficiency. |
| Memory | Heap, allocation rate, direct buffers, caches, leak patterns | Use heap/JFR evidence; cap caches and buffers; distinguish heap from container limits. |
| Time complexity | Cost growth with input, fan-out, and concurrency | Bound input and fan-out; use appropriate algorithms; protect expensive endpoints. |
| Scalability | Throughput and tail latency as demand grows | Load test; partition where justified; use admission control and backpressure. |
| Network | Round trips, DNS/TLS, retries, payload size | Set end-to-end deadlines; reuse connections; avoid retry amplification. |
| Caching | Hit rate, freshness, invalidation, stampedes | Define consistency and TTL; control stampedes; measure cache contribution. |
| Threads | JVM platform/virtual threads, executor queues, blocking, pool saturation | Choose execution model deliberately; bound concurrency; inspect thread dumps and queue wait. |
| Telemetry | Cardinality, sampling, retention, export overhead | Keep labels bounded; sample traces thoughtfully; monitor the telemetry pipeline itself. |

**Key Takeaway:** SRE uses measurement to find the limiting resource and improve the customer outcome; averages alone can hide tail and saturation problems.

---

## ⚖ Trade-offs

- **Advantages:** Reliability becomes measurable; release decisions become evidence-based; incidents improve system design; toil can be reduced through engineering.
- **Disadvantages:** Requires time for instrumentation, operations, and reliability work; poorly chosen SLOs create incentives to game metrics; complex observability can be costly.
- **Use it when:** A service has meaningful customer impact, dependencies, on-call load, growth, or delivery risk that benefits from explicit reliability practices.
- **Avoid over-engineering when:** A low-impact internal tool has no meaningful availability target; apply proportional practices rather than importing a large-company operating model wholesale.
- **Real-life analogy:** An SLO is like a reliability budget for a journey: a small amount of delay or disruption may be acceptable, but repeated disruption uses up the agreed margin. The analogy does not replace precise event definitions or risk controls.

**Key Takeaway:** Adopt SRE principles in proportion to service risk; the objective is better customer outcomes, not process for its own sake.

---

## 📊 Comparison Table

| Approach | Advantages | Disadvantages | Performance / reliability profile | Best use case |
|---|---|---|---|---|
| Traditional operations | Clear operational ownership; useful for stable systems | Manual work and handoffs can bottleneck change and learning | Depends heavily on people and runbooks | Small, stable estates or specialized operations |
| DevOps practices | Shared responsibility and faster feedback | Can remain vague without service measures and operating mechanisms | Improves delivery flow when supported by automation | Teams improving collaboration and deployment lifecycle |
| SRE | Explicit reliability targets, error-budget decisions, engineering focus on operations | Requires ownership, telemetry, policy, and sustained investment | Balances reliability and change using observed service behavior | Customer-impacting services with meaningful operational risk |
| Platform engineering | Reusable paved roads and self-service capabilities | A platform can become a bottleneck or hide operational responsibility | Improves consistency and developer productivity when adopted | Organizations with many teams and repeatable service patterns |

**Key Takeaway:** SRE, DevOps, and platform engineering can complement each other; choose practices based on service needs and organizational scale.

---

## 🎯 Interview Questions

### 1. What is SRE?

**Expected answer:** SRE applies software engineering to operations, using measurable reliability targets, automation, observability, and incident learning to operate services at scale.  
**Follow-up:** How does it relate to DevOps in your organization?

### 2. Explain SLI, SLO, SLA, and error budget.

**Expected answer:** An SLI is a measurement; an SLO is an internal target for that measurement over a window; an SLA is an external commitment with consequences; an error budget is the unreliability allowed by the SLO.  
**Follow-up:** Who should approve an SLO and its policy?

### 3. How would you choose an SLI for a payment API?

**Expected answer:** Start from the customer journey and define eligible successful outcomes, denominator, latency requirement, window, and exclusions. Validate that it captures actual user success.  
**Follow-up:** How do you handle expected business declines and fraud rejections?

### 4. What is an error budget used for?

**Expected answer:** It gives teams a shared way to balance change and reliability; policy may shift effort toward reliability when budget is consumed too quickly.  
**Follow-up:** What should happen when teams disagree about attribution?

### 5. What are the four golden signals?

**Expected answer:** Latency, traffic, errors, and saturation. Together they help show user-visible behavior, demand, failures, and resource limits.  
**Follow-up:** Why are percentiles often more informative than averages?

### 6. What should page an on-call engineer?

**Expected answer:** A condition that is urgent, actionable, and likely affecting users now or imminently. Other findings can become tickets or dashboards.  
**Follow-up:** How would you reduce alert fatigue without missing incidents?

### 7. How do you make a Java service resilient to a slow dependency?

**Expected answer:** Use explicit end-to-end deadlines, bounded pools and queues, limited retry with backoff, circuit breaking where suitable, bulkheads, and graceful degradation. Preserve idempotency and monitor saturation.  
**Follow-up:** How can retries worsen an outage?

### 8. What is toil, and how do you reduce it?

**Expected answer:** Toil is repetitive operational work that tends to scale with service growth and provides little enduring value. Measure it, remove root causes, or automate repetitive tasks safely.  
**Follow-up:** When is automation riskier than a manual procedure?

### 9. What is a blameless postmortem?

**Expected answer:** A learning review that examines system conditions and decisions without personal blame, then creates actionable improvements. Blameless does not mean absence of accountability for follow-through.  
**Follow-up:** How do you ensure action items actually reduce recurrence?

### 10. How should liveness and readiness differ?

**Expected answer:** Liveness indicates whether a process should be restarted; readiness indicates whether it should receive traffic. Dependency failures should not automatically trigger liveness restarts.  
**Follow-up:** How can incorrect probes cause an outage?

### 11. How would you safely deploy a risky Java change?

**Expected answer:** Use staged rollout/canary, compare relevant SLI signals, define abort and rollback criteria, and ensure schema/data changes are backward-compatible.  
**Follow-up:** What if rollback is unsafe because data has already changed?

### 12. How do you approach a latency incident?

**Expected answer:** Confirm user impact and scope; inspect latency distribution, errors, traffic, and saturation; correlate deploys and dependency traces; mitigate first, then investigate root causes.  
**Follow-up:** What evidence distinguishes JVM GC pressure from a database slowdown?

**Key Takeaway:** Senior answers connect definitions to a concrete service, failure mode, measurement, and operational decision.

---

## 🧠 Scenario-Based Questions

- **What happens if the database latency spikes but the service remains technically healthy?** Check customer-facing latency/error SLI, pool wait, queue depth, traces, and database signals; protect the service with deadlines and bounded concurrency; mitigate before exhausting threads.
- **How would you optimize a service that meets average latency but misses its p99 target?** Segment histograms by route/dependency; inspect tail contributors, fan-out, lock contention, GC pauses, and queueing; test one evidence-backed change at a time.
- **How would you troubleshoot a sudden error-budget burn after deployment?** Confirm SLI correctness and blast radius; correlate the rollout; compare canary and baseline; rollback or disable the feature if user harm is ongoing; preserve traces and follow with a postmortem.
- **How would you design reliability for a payment workflow across microservices?** Identify the end-to-end user outcome and transaction boundaries; define SLOs per critical service and journey; use idempotency, durable events, bounded retries, reconciliation, and clear degraded modes.

**Key Takeaway:** In scenarios, establish user impact, contain harm, use evidence, and then explain the durable fix.

---

## 🛠 Debugging Tips

- **Common production issues:** Thread/connection-pool exhaustion, GC pauses, hot partitions, queue growth, retry storms, DNS/TLS issues, slow dependencies, and bad releases.
- **Logs:** Search structured logs by trace ID, service version, route, and sanitized correlation key; never log credentials or payment secrets.
- **Monitoring:** Check SLO burn, latency histograms, traffic, error rates, saturation, JVM heap/GC, thread and connection pools, queue depth, and dependency health.
- **Troubleshooting:** Scope impact; check recent changes; compare healthy and failing cohorts; identify the bottleneck; mitigate safely; verify customer recovery; then investigate root cause.
- **Debugging techniques:** Java Flight Recorder, thread dumps, heap dumps, GC logs, profilers, distributed traces, and controlled load tests. Capture evidence before restarting when safe.
- **Operational safety:** Avoid running expensive diagnostics on a saturated production node without a plan; protect sensitive data in artifacts and access logs.

**Key Takeaway:** Start with user symptoms and high-signal telemetry; use JVM and dependency evidence to explain the cause.

---

## 📌 When to Use SRE Practices

- A customer-facing service has availability, latency, correctness, freshness, or durability expectations.
- On-call teams repeatedly handle the same incidents or manual procedures.
- Releases create meaningful operational risk, or multiple teams depend on the service.
- Growth, compliance, or business impact makes informal reliability assumptions inadequate.

**Key Takeaway:** Use SRE practices where reliability can be defined, measured, and improved through engineering.

---

## 🚫 When NOT to Use SRE Practices

- Do not impose heavyweight SLO governance on a low-impact prototype with no production users; use lightweight health checks and basic operational ownership.
- Do not promise extreme availability without a customer need, architecture, staffing, and budget to support it.
- Do not use error budgets as a punitive team-ranking metric; that encourages gaming and hides risk.
- Do not automate an unsafe process before understanding its failure modes; automation can make mistakes faster and at larger scale.

**Key Takeaway:** Scale the SRE process to the service’s impact, and avoid reliability theater that produces metrics without useful decisions.

---

## 💡 Interview Tips

- **What interviewers expect:** Clear distinctions between SLI/SLO/SLA, evidence-based trade-offs, practical incident handling, and an engineering mindset toward operations.
- **Common mistakes:** Claiming SRE means “100% uptime”; discussing tools before outcomes; treating every alert as a page; ignoring error-budget policy; suggesting unlimited retries.
- **Answer confidently:** Define the user outcome, describe measurement and target, explain failure behavior, discuss trade-offs, then give a production example and how you would verify it.
- **Likely follow-ups:** SLO adoption, alert design, Java/JVM diagnosis, capacity planning, incident command, canary strategy, security, and postmortem action tracking.

**Key Takeaway:** Show that you can own the service after deployment, not only design and code it.

---

## 📝 Short Conclusion Summary

Site Reliability Engineering applies software engineering to production operations. Teams define customer-focused SLIs and SLOs, use error budgets to make change decisions, instrument services for actionable observability, automate repetitive work, and learn from incidents. For a Java service, this includes JVM and dependency behavior, safe timeouts and concurrency limits, progressive delivery, and clear ownership. The goal is a service whose reliability is measurable and continuously improved at a cost appropriate to its users.

**Key Takeaway:** SRE treats reliability as a measurable product and engineering responsibility.

---

## ⚡ TL;DR

| Topic | Summary |
|---|---|
| Purpose | Apply engineering to reliable production operations. |
| Internal working | Define SLIs/SLOs, observe service health, manage error budgets, respond, learn, and improve. |
| Best for | Customer-impacting systems with operational risk, scale, or on-call demands. |
| Avoid | Heavy process for low-impact services; targets without a decision policy. |
| Advantages | Shared reliability goals, safer change, lower toil, better incident learning. |
| Disadvantages | Requires instrumentation, ownership, time, and operational investment. |
| Performance | Measure latency, traffic, errors, saturation, and JVM/dependency bottlenecks. |
| Interview keyword | SLI, SLO, SLA, error budget, toil, golden signals, blameless postmortem. |

---

## 🚀 30-Second Interview Answer

> “Site Reliability Engineering applies software engineering to the operation of production services. We start by defining customer-focused SLIs and SLOs, then use observability and error-budget policy to balance reliability with delivery. SRE also emphasizes automation, bounded failure behavior, effective incident response, and blameless learning that removes recurring causes and toil. For a Java payment service, for example, I’d measure successful end-to-end authorizations and latency, set explicit dependency deadlines and idempotency rules, alert on user-impacting SLO burn, and use canaries and postmortems to improve the system.”

---

## 🔗 Useful Resources

- **Google SRE books and resources:** [SRE resources library](https://sre.google/resources/) — Official books, articles, and learning material.
- **Practical SRE workbook:** [The Site Reliability Workbook](https://sre.google/workbook/table-of-contents/) — SLOs, monitoring, toil, on-call, incident response, postmortems, and canarying.
- **SLO and error-budget policy:** [Example error-budget policy](https://sre.google/workbook/error-budget-policy/) — A concrete policy example; adapt it to local business and operational context.
- **Monitoring:** [Monitoring Distributed Systems](https://sre.google/sre-book/monitoring-distributed-systems/) — Golden signals, symptoms versus causes, and alerting principles.
- **OpenTelemetry:** [Official documentation](https://opentelemetry.io/docs/) — Instrumentation and collection of metrics, traces, and logs; not an observability backend.
- **Java operations:** [Java Flight Recorder documentation](https://docs.oracle.com/en/java/javase/21/jfapi/) — JVM profiling and diagnostic APIs; check the documentation for the deployed JDK version.
- **English video:** [OpenTelemetry Demystified: An Observability Tutorial](https://www.youtube.com/watch?v=dfpGU9al_i4) — Introductory walkthrough of traces, metrics, and logs.

---

*Prepared as an interview revision guide. Targets and policies in examples are illustrative; each organization should define them from user needs, risk, and service architecture.*
