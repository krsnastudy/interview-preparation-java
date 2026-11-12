# 📘 Microservices Design Patterns

**Audience:** Senior Java Developer · Technical Lead · Solution Architect
**Focus:** The most-used microservices patterns in enterprise / BFSI systems, with Spring Boot 3 / Spring Cloud / Resilience4j / Kafka
**Java level:** Java 17+

A monolith fails in one place; a distributed system fails in a hundred new ways. Microservices patterns are the proven answers to the problems that appear the moment you split a system over a network: *how do clients find and call services, how do we keep data consistent without one big transaction, and how do we stop one slow service from taking everything down?*

---

## 📖 What are Microservices Design Patterns?

**Definition:** Reusable solutions to recurring problems in distributed, independently deployable services. Most are catalogued at microservices.io (Chris Richardson); resilience patterns come from *Release It!* (Nygard).

**Why they exist:** Splitting a monolith moves complexity from code into the network, the data layer and operations.

**Problems they solve:**
- Clients must call dozens of services (routing, auth, aggregation)
- No single ACID transaction across services
- Partial failure and cascading failure
- Service location changes constantly (containers, autoscaling)
- Configuration, tracing and security repeated in every service

**The pattern map (the ones interviewers actually ask)**

| Concern | Patterns |
|---|---|
| **Decomposition** | Decompose by business capability / bounded context, **Strangler Fig**, Database per Service |
| **Edge & integration** | **API Gateway**, **Backend for Frontend (BFF)**, API Composition |
| **Communication** | Sync REST/gRPC, Async messaging/events, **Service Discovery** |
| **Data consistency** | **Saga**, **Transactional Outbox**, **CQRS**, **Event Sourcing**, **Idempotent Consumer** |
| **Resilience** | **Circuit Breaker**, Retry + backoff + jitter, Timeout, **Bulkhead**, Rate Limiter |
| **Cross-cutting infra** | Externalized Config, **Sidecar / Service Mesh**, Observability (tracing, metrics, logs), Health checks |

> **Key Takeaway:** Patterns are grouped by the problem they solve. In an interview, start from the *problem* ("we have no distributed transaction") and arrive at the pattern ("so a Saga with an Outbox"), not the other way round.

---

## ❓ Why Do We Need Them?

**Business problem:** Banks want independent release cycles per product (payments, cards, loans) and regulators demand auditability and resilience. Teams must ship without coordinating one giant release.

**Technical problem:** A call that was a method invocation is now a network call that can be slow, fail, duplicate or arrive out of order.

**Fallacies of distributed computing (quote these):** the network is reliable; latency is zero; bandwidth is infinite; the network is secure; topology doesn't change; there is one administrator; transport cost is zero; the network is homogeneous.

| Monolith habit | What breaks in microservices | Pattern that fixes it |
|---|---|---|
| One `@Transactional` over many tables | No cross-service ACID | Saga + Outbox |
| Shared database | Tight coupling, schema lock-in | Database per Service |
| In-process call | Slow/failed dependency cascades | Circuit Breaker, Timeout, Bulkhead |
| Hard-coded host/port | Instances come and go | Service Discovery |
| One UI, one backend | Chatty clients, many protocols | API Gateway / BFF |
| Single log file | Request spans 8 services | Distributed tracing, correlation ID |
| Big-bang rewrite | Too risky | Strangler Fig |

> **Key Takeaway:** Every pattern exists because a *guarantee the monolith gave you for free* (ACID, in-process calls, one deployment) is gone.

---

## ⚙️ Internal Working

How a typical request flows through the patterns:

```
 Client (mobile/web)
        |
        v
+---------------------+   authN/Z (JWT), rate limit, routing, TLS
|     API Gateway     |   -> BFF / aggregation
+----------+----------+
           | lookup (Service Discovery)
           v
+---------------------+   Circuit Breaker + Retry + Timeout + Bulkhead
|  Payment Service    |-----------------------------------+
+----------+----------+                                   |
           | local ACID tx                                v
           v                                    +---------------------+
+---------------------+                         |  FX Rate Service    |
| Payment DB          |                         +---------------------+
|  + OUTBOX table     |
+----------+----------+
           | relay (poller / CDC)
           v
+---------------------+     events      +----------------------+
|  Kafka topic        |---------------->| Ledger / Notification|
+---------------------+                 |  (Idempotent Consumer)|
                                        +----------------------+
```

**Step by step**
1. Gateway authenticates, rate-limits and routes the call.
2. Service instance is resolved via discovery (client-side LB or Kubernetes Service).
3. Downstream sync calls are wrapped by resilience patterns.
4. The service commits its **business row and an outbox row in one local transaction**.
5. A relay publishes the outbox row to Kafka (at-least-once).
6. Consumers de-duplicate (idempotency) and perform their own local transaction; failures trigger **compensation** (Saga).

> **Key Takeaway:** The reliable core is *local transaction + outbox + at-least-once delivery + idempotent consumer*. Saga and CQRS are built on top of it.

---

## 🏗 Architecture / Internal Components

| Component | Responsibility | Typical tech |
|---|---|---|
| **API Gateway** | Single entry: routing, authN, rate limit, TLS termination, request aggregation | Spring Cloud Gateway, Kong, AWS API Gateway, NGINX |
| **Service Registry** | Who is where (instances + health) | Eureka, Consul, Kubernetes DNS/Endpoints |
| **Config Server** | Externalised, versioned, per-environment config | Spring Cloud Config, K8s ConfigMap/Secret, Vault |
| **Message Broker** | Async events/commands, durability, ordering per key | Kafka, RabbitMQ, ActiveMQ/IBM MQ |
| **Resilience library** | Circuit breaker, retry, bulkhead, rate limiter, time limiter | Resilience4j |
| **Outbox relay** | Moves outbox rows to the broker | Polling job, Debezium CDC |
| **Saga orchestrator** | Drives a long-running workflow and compensations | Camunda, Temporal, Axon, custom state machine |
| **Service mesh / sidecar** | mTLS, retries, traffic shifting outside app code | Istio/Envoy, Linkerd |
| **Observability stack** | Metrics, logs, traces | Micrometer + Prometheus/Grafana, OpenTelemetry, ELK/Loki, Jaeger/Tempo |
| **Schema registry** | Versioned event contracts | Confluent Schema Registry (Avro/Protobuf/JSON Schema) |

> **Key Takeaway:** Know *who owns each responsibility*: gateway = edge concerns, mesh = service-to-service networking, library = business-aware resilience, broker = async decoupling.

---

## 🔄 Visual Flow

### 1. Circuit Breaker state machine

```
          failure rate >= threshold
   +--------+ ---------------------> +--------+
   | CLOSED |                        |  OPEN  |  calls rejected fast
   +---+----+ <--------------------- +---+----+  (fallback)
       ^        probes succeed          |
       |                                | waitDuration elapsed
       |        +-----------+           v
       +--------| HALF-OPEN |<----------+
   probes OK    +-----+-----+
                      | probes fail -> back to OPEN
```

### 2. Saga - orchestration with compensation (fund transfer)

```
 Orchestrator
   |-- 1 ReserveFunds ---------> Account Svc   OK
   |-- 2 CreditBeneficiary ----> Ledger Svc    FAIL (frozen)
   |
   |<-- compensate in reverse order
   |-- undo ReserveFunds ------> Account Svc   (release hold)
   '-- mark transfer FAILED, notify customer
```

### 3. CQRS + Event Sourcing

```
 Command --> [Command Model] --append--> [Event Store] --publish--> Projector
                                                                       |
 Query   <-- [Read Model / denormalised view] <------------------------'
```

### 4. Strangler Fig

```
 Clients --> [ Facade / Gateway ] --/loans/new--> New Loan Service
                      |
                      '--everything else--------> Legacy Monolith
 (routes move to new services one by one until the monolith is empty)
```

> **Key Takeaway:** If you can draw the state machine (circuit breaker) and the compensation order (saga) on a whiteboard, you have passed most follow-ups.

---

## 💻 Code Examples

### Basic - API Gateway routing (Spring Cloud Gateway)

```yaml
# application.yml - route + filters at the edge
# NOTE: property prefix changed in newer Spring Cloud releases
# (spring.cloud.gateway.server.webflux.routes) - check your version.
spring:
  cloud:
    gateway:
      routes:
        - id: payments
          uri: lb://payment-service          # lb:// = resolve via service discovery + client-side load balancing
          predicates:
            - Path=/api/payments/**          # route when the path matches
          filters:
            - StripPrefix=1                  # /api/payments/x -> /payments/x
            - name: RequestRateLimiter       # throttle per client (needs Redis)
              args:
                redis-rate-limiter.replenishRate: 20
                redis-rate-limiter.burstCapacity: 40
```

### Basic - Circuit Breaker + Retry + Bulkhead (Resilience4j)

```java
@Service
public class FxRateClient {

    private final RestClient rest;                 // Spring 6.1+; timeouts configured on the request factory

    public FxRateClient(RestClient.Builder builder) {
        this.rest = builder.baseUrl("http://fx-service").build();
    }

    // Default Resilience4j aspect order: Retry( CircuitBreaker( RateLimiter( TimeLimiter( Bulkhead( call )))))
    @Retry(name = "fxRates")                                           // 3 attempts with exponential backoff
    @CircuitBreaker(name = "fxRates", fallbackMethod = "cachedRate")   // open after 50% failures
    @Bulkhead(name = "fxRates")                                        // cap concurrent calls (protect threads)
    public BigDecimal rate(String pair) {
        return rest.get().uri("/rates/{pair}", pair)
                   .retrieve().body(BigDecimal.class);
    }

    // Fallback signature = same args + Throwable. Return stale/cached data, never silently invent money values.
    BigDecimal cachedRate(String pair, Throwable t) {
        log.warn("FX fallback for {} due to {}", pair, t.toString());
        return RateCache.lastKnown(pair);
    }
}
```

```yaml
resilience4j:
  circuitbreaker:
    instances:
      fxRates:
        slidingWindowType: COUNT_BASED
        slidingWindowSize: 20                    # evaluate the last 20 calls
        minimumNumberOfCalls: 10                 # don't judge on tiny samples
        failureRateThreshold: 50                 # % failures -> OPEN
        slowCallDurationThreshold: 2s            # slow = failure-ish
        slowCallRateThreshold: 80
        waitDurationInOpenState: 30s
        permittedNumberOfCallsInHalfOpenState: 5 # probes
        automaticTransitionFromOpenToHalfOpenEnabled: true
  retry:
    instances:
      fxRates:
        maxAttempts: 3
        waitDuration: 200ms
        enableExponentialBackoff: true
        exponentialBackoffMultiplier: 2
        enableRandomizedWait: true               # jitter
        retryExceptions: [java.io.IOException, java.util.concurrent.TimeoutException]
  bulkhead:
    instances:
      fxRates:
        maxConcurrentCalls: 20
        maxWaitDuration: 0                       # fail fast when full
```

> ⚠ `@TimeLimiter` in Resilience4j only wraps **asynchronous** return types (`CompletableFuture`). For blocking calls, set connect/read timeouts on the HTTP client itself.

### Intermediate - Saga orchestrator with compensation (compiled and run)

```java
// Orchestrated saga: execute steps in order; on failure compensate completed steps in reverse.
interface SagaStep {
    String name();
    void execute(Map<String, Object> ctx);      // a LOCAL transaction in one service
    void compensate(Map<String, Object> ctx);   // semantic undo (not a DB rollback)
}

static class SagaOrchestrator {
    private final List<SagaStep> steps;
    SagaOrchestrator(List<SagaStep> steps) { this.steps = steps; }

    boolean run(Map<String, Object> ctx) {
        Deque<SagaStep> completed = new ArrayDeque<>();
        for (SagaStep s : steps) {
            try {
                s.execute(ctx);
                completed.push(s);                         // remember for possible undo
            } catch (RuntimeException e) {
                while (!completed.isEmpty())               // LIFO = reverse order
                    completed.pop().compensate(ctx);
                return false;                              // saga rolled back
            }
        }
        return true;
    }
}
// Steps: ReserveFunds -> CreditBeneficiary -> SendConfirmation
// Output (frozen beneficiary): FAILED at CreditBeneficiary -> undo ReserveFunds -> ROLLED BACK
```

Full runnable file: `SagaDemo.java` (run with `java SagaDemo.java`).

### Production - Transactional Outbox (Spring Boot + JPA + Kafka)

*Illustrative production sketch; not compiled here.*

```java
@Entity @Table(name = "outbox")
class OutboxEvent {
    @Id UUID id;                 // also the idempotency key for consumers
    String aggregateType;        // "Transfer"
    String aggregateId;          // Kafka message key -> per-aggregate ordering
    String type;                 // "TransferCreated"
    @Column(columnDefinition = "jsonb") String payload;
    Instant createdAt;
    boolean published;
}

@Service
class TransferService {
    @Transactional                                   // ONE local ACID transaction
    public UUID create(TransferCommand cmd) {
        Transfer t = transferRepo.save(Transfer.from(cmd));            // 1. business state
        outboxRepo.save(OutboxEvent.of("Transfer", t.getId(),          // 2. event, same commit
                                       "TransferCreated", toJson(t)));
        return t.getId();                            // no broker call inside the DB transaction
    }
}

@Component
class OutboxRelay {
    @Scheduled(fixedDelay = 500)
    @Transactional
    void publish() {
        // Multiple instances: use SELECT ... FOR UPDATE SKIP LOCKED so relays don't fight.
        for (OutboxEvent e : outboxRepo.lockNextBatch(100)) {
            kafka.send("transfer-events", e.aggregateId, e.payload)
                 .join();                            // wait for broker ack
            e.published = true;                      // at-least-once: crash before commit => re-send
        }
    }
}
// At scale prefer CDC (Debezium) tailing the DB log instead of polling.
```

### Production - Idempotent Consumer

```java
@Component
class LedgerListener {
    @KafkaListener(topics = "transfer-events", groupId = "ledger")
    @Transactional
    public void on(ConsumerRecord<String, String> rec) {
        UUID eventId = UUID.fromString(header(rec, "eventId"));
        // INSERT ... ON CONFLICT (event_id) DO NOTHING  -> returns rows affected (0 = duplicate)
        int inserted = processedRepo.insertIfAbsent(eventId);
        if (inserted == 0) return;                   // duplicate delivery: ignore safely
        ledgerService.apply(rec.value());            // business effect in the SAME transaction
    }
}
```

### Production - CQRS projection (read model)

```java
@Component
class AccountSummaryProjector {
    @KafkaListener(topics = "account-events", groupId = "account-summary")
    @Transactional
    void on(AccountEvent ev) {
        // Update a denormalised table purpose-built for the "balance screen" query.
        summaryRepo.upsert(ev.accountId(), ev.newBalance(), ev.occurredAt());
    }
}
// Query side reads account_summary directly: no joins, independently scalable. Eventually consistent.
```

> **Key Takeaway:** The three snippets to memorise are the **resilience config**, the **outbox transaction** and the **idempotent consumer**; they answer 80% of "how do you do this in Spring" follow-ups.

---

## 🏦 Real Production Example

**Scenario: Online fund transfer in a retail bank (Payments, Accounts, Ledger, Fraud, Notification services).**

```
Mobile App
   |  HTTPS + JWT
   v
API Gateway ----(rate limit, auth, idempotency-key header passthrough)
   |
   v
Payment Service  --(sync, circuit breaker + 2s timeout)--> Fraud Service
   |  local tx: transfer(PENDING) + outbox(TransferCreated)
   v
Kafka: transfer-events
   |--> Account Service : reserve funds   --> emits FundsReserved / ReserveFailed
   |--> Ledger Service  : post entries    --> emits LedgerPosted / LedgerFailed   (idempotent)
   '--> Notification    : SMS / email      (idempotent, best-effort)
Payment Service (saga state machine) consumes outcome events:
   all OK        -> COMPLETED
   any failure   -> issue compensations (release hold, reverse ledger) -> FAILED
```

| Decision | Pattern | Why |
|---|---|---|
| Customer retries on a flaky network | **Idempotency key** on `POST /transfers` | Same key returns the original result, never a second debit |
| No distributed transaction | **Saga (orchestrated)** | Each step is a local tx with a compensation |
| Event must not be lost if Kafka is down | **Transactional Outbox** | Business row and event commit together |
| Duplicate event delivery | **Idempotent consumer** | Ledger never posts twice |
| Fraud service slow | **Circuit Breaker + timeout + fallback (manual review queue)** | Protect payment threads and SLA |
| Balance screen has 50x more reads than writes | **CQRS read model** | Scale and shape reads independently |
| Audit: "who changed what, when?" | **Event log / Event Sourcing for the ledger** | Immutable history, replayable |
| Legacy core banking still exists | **Strangler Fig + Adapter (anti-corruption layer)** | Migrate gradually |

> **Key Takeaway:** In BFSI, say **"idempotency, auditability, compensation"** early. Those three words show you understand money, not just Kafka.

---

## ⚠ Common Mistakes

| Wrong approach | Problems | Correct approach |
|---|---|---|
| **Shared database** between services | Hidden coupling, can't change schema, no autonomy | Database per service; share via APIs/events |
| **Distributed monolith**: services call each other synchronously in long chains | Latency multiplies, availability = product of all, lockstep deploys | Async events, fewer hops, merge chatty services |
| **Dual write**: `repo.save()` then `kafka.send()` | Crash between them loses or fakes events | Transactional Outbox / CDC |
| **Retry everything, immediately, forever** | Retry storm turns a blip into an outage | Backoff + jitter, max attempts, retry only idempotent calls, circuit breaker |
| **No timeouts** | Threads pile up, thread pool exhausted | Explicit connect/read timeouts everywhere |
| **Assuming exactly-once delivery** | Duplicates double-charge | At-least-once + idempotent consumers |
| **2PC/XA across services** | Locks, coordinator SPOF, poor scale | Saga |
| **Saga without compensation design** | Stuck half-done transfers | Define compensation and a "stuck saga" alert per step |
| **Gateway full of business logic** | Becomes a new monolith/bottleneck | Gateway = edge concerns only |
| **No correlation ID** | Can't debug a request across services | Propagate trace/correlation ID in all calls and messages |
| **Event Sourcing everywhere** | Huge complexity, replay/versioning pain | Only where audit/temporal queries justify it |

> **Production impact:** the dual-write bug and the retry storm are the two most common root causes in post-mortems of payment systems.

> **Key Takeaway:** Most failures come from ignoring the network: no timeouts, no idempotency, no atomic "state + event".

---

## ✅ Best Practices

**Design**
- Split by **business capability / bounded context**, not by technical layer.
- One service owns its data; expose it via API or events.
- Version APIs and events; prefer backward-compatible changes (add fields, don't rename). Use a schema registry.
- Make every write operation **idempotent** (idempotency key or natural key).

**Resilience**
- Timeouts on every remote call; shorter than the caller's own timeout (budget the deadline).
- Retries: only for idempotent/transient errors, with exponential backoff **and jitter**, a cap, and only at one layer (avoid retry amplification).
- Bulkheads: separate pools per dependency so one slow dependency can't starve others.
- Provide sensible **fallbacks** (cached data, degraded mode, queue for later); never fabricate financial values.

**Messaging**
- Key messages by aggregate ID for ordering; design consumers for **duplicates and out-of-order**.
- Dead-letter queue + alerts; keep poison messages from blocking partitions.

**Operations**
- Health endpoints: separate **liveness** and **readiness** (Spring Boot Actuator).
- Observability: structured JSON logs with trace ID, RED metrics (Rate, Errors, Duration), traces via Micrometer Tracing/OpenTelemetry.
- Externalise config and secrets (Config Server / K8s / Vault); never bake them into images.
- Contract tests (Pact / Spring Cloud Contract) to prevent breaking consumers.

**Security**
- Zero-trust: authenticate at the gateway **and** service-to-service (mTLS / JWT propagation); least privilege.

> **Key Takeaway:** Idempotency + timeouts + observability are non-negotiable on day one; everything else can be added incrementally.

---

## ⚡ Performance Considerations

| Area | Consideration |
|---|---|
| **Latency** | Each hop adds network + serialization (1-10 ms typical); fewer, coarser calls; parallelise independent calls (`CompletableFuture`/virtual threads) |
| **Availability math** | Serial chain of N services at 99.9% each ≈ 99.9%^N (10 services ≈ 99.0%) |
| **Threads** | Blocking calls tie up threads; with Java 21 **virtual threads** (`spring.threads.virtual.enabled=true`) blocking I/O scales better, but bulkheads/limits on downstreams are still needed |
| **Serialization** | JSON is simple; Protobuf/Avro are smaller/faster and have schemas (gRPC/Kafka) |
| **Caching** | Cache reference data (FX rates, product catalogue) with TTL; beware stale financial data |
| **DB** | Connection pool per service sized to the DB, not to request threads (HikariCP); avoid N+1 across services (API composition batching) |
| **Messaging** | Kafka throughput scales with partitions; ordering only within a partition; consumer lag is the key metric |
| **CQRS reads** | Pre-computed read models trade write-time work for fast, join-free reads |
| **Outbox polling** | Poll interval vs DB load; move to CDC at high volume |

> **Key Takeaway:** In microservices you optimise **latency budgets and failure behaviour**, not just CPU.

---

## ⚖ Trade-offs

| Aspect | Advantages | Disadvantages |
|---|---|---|
| Microservices overall | Independent deploy/scale, team autonomy, fault isolation, tech freedom | Operational complexity, distributed failures, data consistency effort, higher infra cost |
| Saga | No distributed locks, scales, fits long-running flows | Eventual consistency, compensation logic, harder to reason about/test |
| CQRS | Optimised reads/writes, independent scaling | Two models, eventual consistency, more infrastructure |
| Event Sourcing | Full audit, replay, temporal queries | Steep learning curve, event versioning, GDPR erasure is hard |
| API Gateway | One entry point, centralised cross-cutting concerns | Potential bottleneck/SPOF, can become a monolith |
| Service Mesh | Uniform mTLS/retries/observability, no code changes | Extra latency/resources, operational complexity |

**When to use:** large teams, different scaling profiles, independent release cadence, clear domain boundaries.
**When NOT to use:** small team, early-stage product, unclear domain boundaries, no DevOps maturity. *A modular monolith is often the right first step.*

**Analogy:** A monolith is one big restaurant kitchen; microservices are a food court. Each stall is independent and can scale or fail alone, but you now need signage (gateway), a directory (discovery), a shared payment system (consistency) and health inspections (observability).

> **Key Takeaway:** Microservices trade *code complexity* for *operational and consistency complexity*. Say that trade explicitly.

---

## 📊 Comparison Table

| Pattern | Solves | Key Mechanism | Main Cost | Best Use Case |
|---|---|---|---|---|
| API Gateway | Many clients -> many services | Single edge: route, auth, limit | SPOF/bottleneck risk | Public APIs, mobile/web |
| BFF | One API doesn't fit all clients | One backend per client type | More services | Web vs mobile with different payloads |
| Service Discovery | Dynamic instance locations | Registry / K8s DNS | Registry availability | Autoscaled/containerised services |
| Circuit Breaker | Cascading failure | Fail fast when failing | Tuning thresholds | Any flaky remote call |
| Retry + Backoff | Transient errors | Re-attempt w/ jitter | Retry storms, duplicates | Idempotent reads/writes |
| Bulkhead | Resource exhaustion | Isolated pools/semaphores | Capacity partitioning | Shared thread pools |
| Saga | Cross-service consistency | Local tx + compensation | Eventual consistency | Transfers, orders, onboarding |
| Outbox | Dual-write problem | State + event in one tx | Relay/ops overhead | Reliable event publishing |
| CQRS | Read/write mismatch | Separate models | Two models to keep in sync | Read-heavy dashboards |
| Event Sourcing | Audit/history | Append-only events | Complexity | Ledgers, audit-critical domains |
| Idempotent Consumer | Duplicate delivery | De-dup by event ID | Extra table/lookups | All at-least-once consumers |
| Strangler Fig | Risky rewrite | Incremental routing | Temporary dual-running | Legacy modernisation |
| Sidecar / Mesh | Repeated infra code | Proxy per pod | Resources, complexity | mTLS, polyglot fleets |

**Saga: Choreography vs Orchestration**

| Aspect | Choreography | Orchestration |
|---|---|---|
| Control | Services react to each other's events | Central orchestrator commands steps |
| Coupling | Loose, but implicit flow | Services coupled to orchestrator, flow is explicit |
| Visibility | Hard to see the whole flow | Easy to monitor and debug |
| Best for | 2-4 simple steps | Longer, branching, business-critical flows |

**Sync vs Async communication**

| Aspect | Synchronous (REST/gRPC) | Asynchronous (events/messages) |
|---|---|---|
| Coupling | Temporal coupling (callee must be up) | Decoupled in time |
| Latency | Immediate response | Eventual |
| Failure handling | Timeouts, circuit breakers | Retries, DLQ, idempotency |
| Use | Queries, immediate validation | State changes, workflows, integration |

> **Key Takeaway:** Be ready to compare *any two* patterns on mechanism, cost and use case.

---

## 🎯 Interview Questions

**Q1. What is the API Gateway pattern and why use it?**
*Answer:* A single entry point that routes to services and handles cross-cutting concerns: authentication, rate limiting, TLS, request aggregation, protocol translation. Clients stay simple and internal topology is hidden.
*Follow-up:* How do you avoid it becoming a bottleneck or single point of failure? -> Stateless, horizontally scaled behind a load balancer, no business logic, cache and rate limit wisely.

**Q2. API Gateway vs BFF vs Service Mesh?**
*Answer:* Gateway = north-south edge for all clients. BFF = a gateway tailored per client type (web, mobile). Mesh = east-west service-to-service concerns (mTLS, retries, telemetry) via sidecars.
*Follow-up:* Can they coexist? -> Yes, all three commonly do.

**Q3. Explain the Circuit Breaker pattern and its states.**
*Answer:* Wraps remote calls and tracks failures. CLOSED: calls pass. OPEN: failure rate crossed the threshold, calls fail fast (fallback). HALF-OPEN: after the wait duration, a few probe calls decide whether to close or reopen.
*Follow-up:* Which parameters do you tune? -> sliding window size, minimum calls, failure/slow-call thresholds, wait duration, half-open permits.

**Q4. Retry vs Circuit Breaker - aren't they the same?**
*Answer:* Retry handles *transient* blips by trying again; the breaker protects against *sustained* failure by stopping calls. Together, Resilience4j's default aspect order puts Retry *outside* the CircuitBreaker, so every attempt counts toward breaker statistics and an OPEN breaker short-circuits further retries instead of hammering a dead service.
*Follow-up:* What's a retry storm? -> Many clients retrying simultaneously, amplifying load; fix with backoff + jitter, caps, and breakers.

**Q5. What is the Bulkhead pattern?**
*Answer:* Isolate resources (thread pools/semaphores) per dependency so one slow dependency can't exhaust shared capacity - named after ship compartments.
*Follow-up:* Semaphore vs thread-pool bulkhead? -> Semaphore limits concurrency in the caller thread (cheap); thread-pool gives isolation and timeouts at the cost of context switching.

**Q6. Why not use distributed transactions (2PC) across microservices?**
*Answer:* They need a coordinator and hold locks across services, hurting availability and scalability, and many resources (Kafka, HTTP APIs, NoSQL) don't support XA. Microservices favour availability + eventual consistency via Saga.
*Follow-up:* What do you give up with Saga? -> Isolation; you must handle intermediate states (semantic locks, "PENDING" status).

**Q7. Explain the Saga pattern. Choreography vs Orchestration - which would you choose?**
*Answer:* A saga is a sequence of local transactions, each publishing an event/command; on failure, compensating transactions undo completed steps. Choreography for simple flows; orchestration for complex, auditable, business-critical flows (visibility, central timeouts, retries).
*Follow-up:* What if a compensation fails? -> Retry with backoff (compensations must be idempotent), then alert/manual intervention queue.

**Q8. What is the Transactional Outbox and what problem does it solve?**
*Answer:* The dual-write problem: updating the DB and publishing to a broker can't be atomic. Write the event to an `outbox` table in the same local transaction; a relay (poller or CDC) publishes it later. Delivery is at-least-once.
*Follow-up:* Polling vs CDC? -> Polling is simple but adds DB load and latency; CDC (Debezium) reads the transaction log: low latency, ordered, more infra.

**Q9. How do you achieve idempotency?**
*Answer:* Client sends an **idempotency key**; server stores key -> result (unique constraint) and returns the stored result on replays. Consumers record processed event IDs in the same transaction as the business effect. Natural idempotency (`PUT`, upserts, "set balance version N") helps.
*Follow-up:* What if the same key arrives concurrently? -> Unique constraint / row lock makes one win; the other waits or gets the stored/in-progress response.

**Q10. Explain CQRS. When would you use it?**
*Answer:* Separate the write model (commands, invariants) from the read model (queries, denormalised views), often fed by events. Use when read and write loads/shapes differ greatly (dashboards, statements).
*Follow-up:* How do you handle eventual consistency in the UI? -> Return the write result optimistically, show "processing" states, use version/ETag to poll until the read model catches up.

**Q11. Event Sourcing - what is it, and what are the risks?**
*Answer:* Persist state as an append-only sequence of events; rebuild current state by replay (with snapshots). Gives audit and temporal queries. Risks: event schema evolution (upcasting), replay time, complexity, GDPR erasure (crypto-shredding).
*Follow-up:* Event Sourcing vs just publishing events? -> Publishing events is integration; Event Sourcing makes events the *source of truth*.

**Q12. How does Service Discovery work? Client-side vs server-side?**
*Answer:* Instances register (or are discovered) with a registry with health checks. Client-side (Eureka + Spring Cloud LoadBalancer): client fetches the list and balances. Server-side (Kubernetes Service/DNS, load balancer): client calls a stable name and the platform routes.
*Follow-up:* Do you still need Eureka on Kubernetes? -> Usually no; Kubernetes DNS/Services provide discovery.

**Q13. What is the Strangler Fig pattern?**
*Answer:* Incrementally replace a legacy system by routing specific capabilities through a facade/gateway to new services until the legacy is retired. Lower risk than a big-bang rewrite.
*Follow-up:* How do you handle shared data during migration? -> Anti-corruption layer, data sync/CDC, dual-run with reconciliation, then cut over.

**Q14. How do you do distributed tracing and debugging?**
*Answer:* Propagate a trace context (W3C `traceparent`) across HTTP and message headers; in Spring Boot 3 use Micrometer Tracing with OpenTelemetry, export to Jaeger/Tempo/Zipkin; log the trace ID in every line (MDC); combine with RED metrics.
*Follow-up:* What replaced Spring Cloud Sleuth? -> Micrometer Tracing (Spring Boot 3).

**Q15. How do you handle versioning and compatibility of APIs/events?**
*Answer:* Backward-compatible changes (additive), expand-contract migrations, tolerant readers, schema registry with compatibility checks, consumer-driven contract tests; version via URL/header only for breaking changes.
*Follow-up:* How do you deploy a breaking change safely? -> Expand (support both), migrate consumers, contract (remove old) - with feature flags/canaries.

**Q16. How do you secure microservices?**
*Answer:* OAuth2/OIDC at the edge (JWT access tokens), token propagation or token exchange between services, mTLS (mesh) for service identity, least-privilege scopes, secrets in Vault/K8s Secrets, input validation, and audit logging.
*Follow-up:* Validate JWT in the gateway or every service? -> Both: gateway for coarse checks, services for fine-grained authorisation (defence in depth).

**Q17. What is the Database per Service pattern, and how do you handle reporting/joins?**
*Answer:* Each service owns its schema; no cross-service direct DB access. Cross-service queries use **API composition** or **CQRS read models** built from events; reporting uses a data warehouse/lake fed by CDC.
*Follow-up:* Is a schema-per-service in one DB instance acceptable? -> Often as a pragmatic step if ownership/permissions are strict, but isolation of failure and scaling is weaker.

**Q18. Externalized configuration - how and why?**
*Answer:* Keep config outside the build artifact: Spring Cloud Config/K8s ConfigMaps, secrets in Vault. Enables same image across environments and refresh without redeploy (`@RefreshScope`, config watch).
*Follow-up:* Risks of runtime refresh? -> Inconsistent behaviour across instances mid-refresh; apply carefully, prefer rolling restarts for critical settings.

> **Key Takeaway:** For every pattern be ready with: **problem -> mechanism -> failure mode -> Spring/Kafka tool**.

---

## 🧠 Scenario-Based Questions

**S1. What happens if the Payment Service commits the DB but crashes before sending to Kafka?**
Without Outbox the event is lost and downstream never learns of the transfer (inconsistent state). With Outbox the event row is committed with the data; the relay publishes it after restart (possibly twice) and consumers de-dup.

**S2. The Fraud Service is slow (p99 = 8 s). How do you protect Payments?**
Add a timeout (e.g., 1-2 s), circuit breaker with slow-call threshold, bulkhead to cap concurrency, and a fallback (route to manual review / apply conservative limits). Alert on breaker open, and avoid retries on slow calls.

**S3. Customers are double-charged on retry. Diagnose and fix.**
Check whether `POST /transfers` is idempotent. Fix with an idempotency key (unique constraint, store response), idempotent consumers on the ledger, and reconcile existing duplicates with compensating entries.

**S4. Design a loan-origination workflow across KYC, Credit Bureau, Underwriting, Disbursement.**
Orchestrated saga (long-running, human tasks, SLAs) using a workflow engine (Camunda/Temporal) or state machine in a `LoanApplication` service; each step a local transaction + outbox; compensation (cancel sanction, release limit); timeouts and escalation; idempotent steps; audit trail of state transitions; read model for status tracking.

**S5. Kafka consumer lag keeps growing. How do you troubleshoot?**
Check lag per partition, slow processing (DB, downstream calls), rebalancing storms, poison message retries blocking a partition, too few partitions/consumers. Fixes: scale consumers (<= partitions), batch processing, DLQ for poison messages, tune `max.poll.interval.ms`, remove blocking downstream calls.

**S6. How would you migrate a 15-year-old monolithic core to microservices with no downtime?**
Strangler Fig: gateway/facade in front, extract the most volatile/valuable capability first (bounded context), CDC to sync data, anti-corruption layer, dark launches/canaries, reconcile before cutover, keep rollback routes.

**S7. A downstream call is retried by three layers (client, service, gateway). What goes wrong?**
Retry amplification (3 x 3 x 3 = 27 attempts), overload, duplicates. Retry in one layer only, use budgets, idempotency, and breakers.

> **Key Takeaway:** Structure answers as **failure -> impact -> pattern -> trade-off**.

---

## 🛠 Debugging Tips

| Symptom | Likely cause | What to check |
|---|---|---|
| Intermittent 5xx and slow responses across services | Cascading failure, thread pool exhaustion | Thread dumps, breaker/bulkhead metrics, missing timeouts |
| Duplicate side effects | At-least-once without idempotency | Event IDs, idempotency table, consumer retries/rebalances |
| Missing downstream updates | Dual write / outbox relay stuck | Outbox rows with `published=false`, relay logs, broker health |
| Stuck "PENDING" transfers | Saga step failed or lost event | Saga state table, DLQ, timeouts, compensation errors |
| Growing consumer lag | Slow consumer, rebalances, poison message | Lag per partition, `max.poll.interval.ms`, DLQ |
| Traffic going to dead instance | Stale registry / long TTL | Registry health, client cache refresh, readiness probes |
| Config not applied | Cache/refresh, wrong profile | `/actuator/env`, Config Server logs, `@RefreshScope` |
| Breaker flapping open/closed | Thresholds too tight, tiny sample | `minimumNumberOfCalls`, window size, slow-call settings |

**Toolbox**
- **Logs:** structured JSON with `traceId`, `spanId`, `eventId`, business keys (transferId).
- **Metrics:** Micrometer -> Prometheus; Resilience4j exposes breaker state, failure rate, bulkhead usage; Kafka consumer lag.
- **Traces:** OpenTelemetry/Zipkin/Tempo to find the slow hop.
- **Actuator:** `/actuator/health` (liveness/readiness), `/actuator/metrics`, `/actuator/circuitbreakers`.
- **Chaos testing:** inject latency/failures (Chaos Monkey for Spring Boot, Toxiproxy) to verify patterns work.

> **Key Takeaway:** Start with the **trace ID**, then look at **timeouts, breaker state and consumer lag**.

---

## 📌 When to Use Microservices Patterns

- **Saga + Outbox:** any multi-service business transaction (payments, onboarding, order-to-cash).
- **Circuit Breaker / Timeout / Bulkhead:** every synchronous dependency, especially third-party (credit bureau, payment network, FX).
- **API Gateway / BFF:** public APIs and multiple client types.
- **CQRS:** read-heavy views (statements, dashboards, search).
- **Event Sourcing:** ledgers, audit-critical, temporal "as-of" queries.
- **Strangler Fig:** legacy/mainframe modernisation.
- **Service Mesh:** large polyglot fleets needing uniform mTLS/policy.

## 🚫 When NOT to Use Them

- **Small team / simple domain:** a **modular monolith** is cheaper and safer.
- **Strong consistency needed in one unit of work** (single aggregate): keep it in one service/DB transaction.
- **Event Sourcing/CQRS for plain CRUD:** unnecessary complexity.
- **Service mesh with a handful of services:** overhead outweighs benefit.
- **Retries on non-idempotent calls without keys:** you'll create duplicates.
- **No observability or DevOps maturity:** fix this before splitting services.

---

## 💡 Interview Tips

**What the interviewer expects (10+ years):**
- Problem-first thinking: *why* the pattern, *what it costs*, *what you'd choose instead*.
- Concrete tools: Resilience4j, Spring Cloud Gateway, Kafka, Debezium, Micrometer/OTel, Kubernetes.
- Failure-mode awareness: duplicates, ordering, partial failure, backpressure.
- Real project stories: "In our payments platform we ... and this reduced ... ".

**Common mistakes**
- Reciting definitions without trade-offs.
- Claiming "exactly-once" without caveats (it's effectively at-least-once + idempotency).
- Suggesting 2PC across services, or a shared DB.
- Putting retries everywhere, no timeouts.
- Forgetting security and observability.

**How to answer confidently**
1. State the problem. 2. Name the pattern. 3. Show the mechanism (draw it). 4. State the trade-off. 5. Mention the Spring/Kafka implementation. 6. Give a production example.

**Likely follow-ups:** "What if the compensation fails?", "How do you guarantee ordering?", "How do you test sagas?", "How would you do this without Kafka?", "Orchestration vs choreography - why?", "How do you handle schema evolution?"

> **Key Takeaway:** Seniority shows in **trade-offs and failure handling**, not in the number of patterns you can name.

---

## 📝 Short Conclusion Summary

Microservices design patterns address what you lose when leaving a monolith. API Gateway, BFF and discovery solve edge and routing; Circuit Breaker, Retry, Timeout and Bulkhead contain failure; Saga, Outbox, CQRS, Event Sourcing and idempotent consumers deliver data consistency without distributed transactions; and externalised config, sidecars and observability keep the fleet operable. The core principle is to **design for failure and duplicates**: commit state and event atomically, deliver at-least-once, make every consumer idempotent, and compensate rather than roll back.

---

## ⚡ TL;DR

| Topic | Summary |
|---|---|
| Purpose | Solve distributed-system problems: routing, resilience, consistency, operability |
| Internal Working | Gateway -> discovery -> resilient calls -> local tx + outbox -> broker -> idempotent consumers -> saga compensation |
| Best For | Large domains, independent teams/deploys, differing scale profiles |
| Avoid | Small/simple systems, unclear boundaries, no DevOps maturity |
| Advantages | Autonomy, scalability, fault isolation, incremental modernisation |
| Disadvantages | Operational complexity, eventual consistency, debugging difficulty |
| Performance | Latency budgets, timeouts, partitions, read models, virtual threads |
| Interview Keyword | **Saga, Outbox, Idempotency, Circuit Breaker, CQRS, Strangler Fig, Eventual Consistency** |

---

## 🚀 30-Second Interview Answer

"In microservices we lose ACID transactions and in-process calls, so I rely on a few core patterns. At the edge I use an API Gateway for routing, authentication and rate limiting, with a BFF if web and mobile differ. For service-to-service calls I use timeouts, retries with backoff and jitter, a circuit breaker and bulkheads via Resilience4j, so one slow dependency doesn't cascade. For consistency I don't use distributed transactions; I use a Saga, orchestrated for complex flows like fund transfers, with compensating actions, and the Transactional Outbox so the business state and the event commit atomically. Delivery is at-least-once, so every consumer is idempotent. For read-heavy screens I use CQRS read models, and for legacy modernisation I use the Strangler Fig pattern. Finally, I make it all observable with correlation IDs, Micrometer/OpenTelemetry tracing and consumer-lag and breaker metrics. The trade-off is eventual consistency and operational complexity, which is why I'd start with a modular monolith if the domain or team doesn't justify the split."

---

## 📚 Further Reading

- microservices.io pattern catalogue (Chris Richardson): https://microservices.io/patterns/index.html
- *Microservices Patterns* - Chris Richardson (Manning)
- *Building Microservices* (2nd ed.) - Sam Newman
- *Release It!* (2nd ed.) - Michael Nygard (stability patterns)
- *Designing Data-Intensive Applications* - Martin Kleppmann (consistency, messaging)
- Resilience4j docs: https://resilience4j.readme.io/
- Spring Cloud Gateway reference: https://docs.spring.io/spring-cloud-gateway/reference/
- Debezium (CDC / outbox): https://debezium.io/documentation/
- Micrometer Tracing: https://docs.micrometer.io/tracing/reference/
- Azure Architecture Center - Cloud design patterns: https://learn.microsoft.com/azure/architecture/patterns/
