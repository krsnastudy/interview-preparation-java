# 📘 Site Reliability Engineering (SRE)

**Audience:** Senior Java Developer · Technical Lead · Solution Architect
**Focus:** Enterprise / BFSI production systems on Kubernetes
**Stack used in examples:** Java 17+, Spring Boot 3.x, Micrometer, Prometheus, Resilience4j

Site Reliability Engineering treats operations as a software problem. Instead of keeping systems alive through manual effort, SREs define reliability in numbers, measure it continuously, and automate everything that repeats. For a senior Java engineer, SRE is the language used to explain why a payment service is allowed to fail 21 minutes a month, and what happens when it fails more.

---

## 📖 What is SRE?

**Definition:** SRE is a discipline, created at Google around 2003, that applies software engineering practices to infrastructure and operations in order to build and run scalable, highly reliable systems.

**Why it exists:** Traditional Dev and Ops teams had opposing incentives. Developers were rewarded for shipping features, operations for stability. Every release became a negotiation. SRE replaces the argument with a shared, numeric contract: the **error budget**.

**Problem it solves:**
- "Reliable enough" was never defined, so teams argued by opinion
- Manual operational work grew linearly with service growth
- Outages were followed by blame instead of learning
- Release speed and stability were treated as a zero-sum trade

**Key characteristics:**

| Characteristic | Meaning |
|---|---|
| Service Level Objectives | Reliability is a target with a number, not a feeling |
| Error budgets | Allowed unreliability that is *spent* on releases and experiments |
| Toil elimination | Repetitive manual work is capped (guideline: under 50% of time) and automated away |
| Blameless culture | Postmortems fix systems, not people |
| Observability first | You cannot defend an SLO you cannot measure |

> **Key Takeaway:** SRE is "what happens when you ask a software engineer to design an operations team." Reliability becomes a measurable feature with a budget.

---

## ❓ Why Do We Need It?

**Business problem:** In banking, every minute of payment downtime is lost revenue, regulatory exposure (RBI/PSD2 reporting), and customer trust. But 100% uptime is impossible and ruinously expensive, so the business needs an *agreed* target.

**Technical problem:**
- Microservices multiply failure modes (network, dependencies, partial failures)
- Distributed systems cannot be reasoned about without telemetry
- Manual runbooks do not scale past a handful of services

| Old approach | Limitation | SRE approach |
|---|---|---|
| "Keep it up at all costs" | Freezes releases, burns out on-call | Error budget balances speed and safety |
| Alert on CPU, memory, disk | Pages for non-issues, misses real user pain | Alert on SLO burn (symptoms) |
| Hero firefighting | Does not scale, knowledge stays in heads | Runbooks, automation, blameless postmortems |
| Uptime SLA only | Contractual, says nothing about internal targets | SLI → SLO → SLA hierarchy |

> **Key Takeaway:** SRE gives engineering and business one number to argue about, and makes that number drive release, staffing, and priority decisions.

---

## ⚙️ Internal Working

### The reliability loop

```
+------------------------+
| 1. Define SLIs         |  What do users experience? (latency, errors)
+-----------+------------+
            |
            v
+------------------------+
| 2. Set SLOs            |  Target, e.g. 99.95% success over 30 days
+-----------+------------+
            |
            v
+------------------------+
| 3. Measure + Alert     |  Metrics, burn-rate alerts
+-----------+------------+
            |
            v
+------------------------+
| 4. Spend / Protect     |  Budget left? ship. Budget gone? freeze, fix.
|    Error Budget        |
+-----------+------------+
            |
            v
+------------------------+
| 5. Respond + Learn     |  Incident -> mitigate -> blameless postmortem
+-----------+------------+
            |
            +--------> back to step 1 (refine SLIs and SLOs)
```

### Step-by-step

1. **Choose SLIs.** A Service Level Indicator is a ratio of good events to valid events: `good requests / total requests`.
2. **Set SLOs.** A Service Level Objective is the target for that ratio over a window (usually rolling 28 or 30 days).
3. **Compute the error budget.** `Error budget = 1 − SLO`.
4. **Alert on burn rate,** meaning how fast the budget is being consumed, not on raw thresholds.
5. **Apply the policy.** If the budget is healthy, ship faster. If exhausted, freeze risky releases and prioritise reliability work.
6. **Respond to incidents** with defined roles, then run a blameless postmortem with tracked action items.

### Error budget math (memorise this)

| SLO | Error budget | Allowed downtime / 30 days |
|---|---|---|
| 99% | 1% | 7.2 hours |
| 99.9% | 0.1% | 43.2 minutes |
| 99.95% | 0.05% | 21.6 minutes |
| 99.99% | 0.01% | 4.32 minutes |

> **Key Takeaway:** SLI measures, SLO targets, error budget governs behaviour. SLA is only the externally promised (and usually looser) contract.

---

## 🏗 Architecture / Internal Components

| Component | Responsibility |
|---|---|
| **SLI** | Quantitative measure of user-visible behaviour (success ratio, latency percentile) |
| **SLO** | Internal target for an SLI over a window |
| **SLA** | External, contractual commitment with consequences (credits, penalties) |
| **Error budget** | `1 − SLO`; the quantity that gates release velocity |
| **Error budget policy** | Written agreement on what happens when the budget is spent |
| **Metrics pipeline** | Micrometer → Prometheus → Grafana |
| **Logs / Traces** | ELK/Loki for logs, OpenTelemetry + Jaeger/Tempo for traces |
| **Alert manager** | Routes burn-rate alerts to the right on-call |
| **On-call rotation** | Humans responding, with runbooks and escalation paths |
| **Incident commander** | Single owner who coordinates during an incident |
| **Postmortem process** | Blameless review, root cause, action items with owners |
| **Release controls** | Canary, blue-green, feature flags, automated rollback |

### The Four Golden Signals

| Signal | Question it answers | Java/Spring example |
|---|---|---|
| **Latency** | How long do requests take? (separate success vs failure) | `http_server_requests_seconds` p95/p99 |
| **Traffic** | How much demand is there? | Requests per second |
| **Errors** | How many requests fail? | 5xx ratio, business failures |
| **Saturation** | How full is the system? | Thread pool queue, DB connection pool, heap, GC pause |

**RED** (Rate, Errors, Duration) suits request-driven services. **USE** (Utilization, Saturation, Errors) suits resources like CPU, disk, and connection pools.

> **Key Takeaway:** Every SRE concept maps to a component you can point at: SLIs in metrics, SLOs in alert rules, policy in a document, learning in postmortems.

---

## 🔄 Visual Flow

### Observability to action

```
+----------------+      +------------------+      +----------------+
| Spring Boot    |      | Prometheus       |      | Alertmanager   |
| /actuator/     |----->| scrape + rules   |----->| route + dedupe |
| prometheus     |      | (SLO burn rate)  |      +-------+--------+
+----------------+      +--------+---------+              |
                                 |                        v
                                 v                +----------------+
                        +------------------+      | On-call engineer|
                        | Grafana          |      | (PagerDuty etc) |
                        | SLO dashboards   |      +-------+--------+
                        +------------------+              |
                                                          v
                                                 +----------------+
                                                 | Runbook ->     |
                                                 | Mitigate       |
                                                 +-------+--------+
                                                         |
                                                         v
                                                 +----------------+
                                                 | Postmortem ->  |
                                                 | Action items   |
                                                 +----------------+
```

### Error budget decision flow

```
            +------------------------+
            | Error budget remaining |
            +-----------+------------+
                        |
          +-------------+-------------+
          |                           |
      > 0 (healthy)              <= 0 (exhausted)
          |                           |
          v                           v
  +---------------+          +--------------------+
  | Ship features |          | Freeze risky       |
  | Run experiments|         | releases           |
  | Chaos tests   |          | Fix reliability    |
  +---------------+          | Postmortem actions |
                             +--------------------+
```

---

## 💻 Code Examples

### Basic: expose health and metrics (Spring Boot 3.x)

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus   # expose only what is needed
  endpoint:
    health:
      probes:
        enabled: true                     # adds /health/liveness and /health/readiness
  metrics:
    tags:
      application: payment-service        # tag every metric with service name
    distribution:
      percentiles-histogram:
        http.server.requests: true        # histogram buckets for accurate p95/p99 in Prometheus
      slo:
        http.server.requests: 100ms,300ms,500ms,1s   # buckets aligned to the latency SLO
```

### Intermediate: custom business SLI with Micrometer

```java
@Service
public class PaymentService {

    private final Counter success;
    private final Counter failure;
    private final Timer latency;

    public PaymentService(MeterRegistry registry) {
        // Good events and bad events share one metric name, split by tag
        this.success = Counter.builder("payments.processed")
                .tag("outcome", "success").register(registry);
        this.failure = Counter.builder("payments.processed")
                .tag("outcome", "failure").register(registry);
        // Timer exposes count, sum and histogram buckets
        this.latency = Timer.builder("payments.latency")
                .publishPercentileHistogram().register(registry);
    }

    public PaymentResult pay(PaymentRequest request) {
        return latency.record(() -> {
            try {
                PaymentResult result = gateway.charge(request);
                success.increment();             // good event
                return result;
            } catch (PaymentException ex) {
                failure.increment();             // bad event feeds the SLI
                throw ex;
            }
        });
    }
}
```

### Production: resilience for a downstream dependency (Resilience4j)

```java
@Service
public class LedgerClient {

    // Retry wraps CircuitBreaker by default: retry only while the circuit is closed
    @CircuitBreaker(name = "ledger", fallbackMethod = "fallback")
    @Retry(name = "ledger")
    public LedgerEntry post(LedgerRequest request) {
        return restClient.post()
                .uri("/ledger")
                .body(request)
                .retrieve()
                .body(LedgerEntry.class);
    }

    // Graceful degradation: queue for later instead of failing the user
    private LedgerEntry fallback(LedgerRequest request, Throwable cause) {
        pendingQueue.enqueue(request);           // reconcile asynchronously
        return LedgerEntry.pending(request.id());
    }
}
```

```yaml
resilience4j:
  circuitbreaker:
    instances:
      ledger:
        sliding-window-size: 50
        failure-rate-threshold: 50              # open circuit at 50% failures
        wait-duration-in-open-state: 30s
        permitted-number-of-calls-in-half-open-state: 5
  retry:
    instances:
      ledger:
        max-attempts: 3
        wait-duration: 200ms
        enable-exponential-backoff: true        # avoid retry storms
        exponential-backoff-multiplier: 2
        enable-randomized-wait: true            # jitter spreads retries out
```

### Production: SLO and burn-rate alerting (Prometheus)

```yaml
# slo-rules.yml  (SLO: 99.9% success, error budget = 0.001)
groups:
  - name: payment-slo
    rules:
      # Error ratio over 5m and 1h windows (recording rules keep alerts cheap)
      - record: payment:error_ratio:rate5m
        expr: |
          sum(rate(http_server_requests_seconds_count{application="payment-service",status=~"5.."}[5m]))
          / sum(rate(http_server_requests_seconds_count{application="payment-service"}[5m]))
      - record: payment:error_ratio:rate1h
        expr: |
          sum(rate(http_server_requests_seconds_count{application="payment-service",status=~"5.."}[1h]))
          / sum(rate(http_server_requests_seconds_count{application="payment-service"}[1h]))

      # Fast burn: 14.4x burn rate consumes ~2% of a 30-day budget in 1 hour -> PAGE
      - alert: PaymentErrorBudgetFastBurn
        expr: |
          payment:error_ratio:rate1h > (14.4 * 0.001)
          and payment:error_ratio:rate5m > (14.4 * 0.001)
        labels:
          severity: page
        annotations:
          summary: "Payment SLO burning 14.4x too fast"
          runbook: "https://wiki.bank.internal/runbooks/payment-slo"
```

### Spring Boot + Kubernetes: probes that do not cause cascades

```yaml
# deployment.yaml (container section)
livenessProbe:                      # "is the JVM stuck?" -> restart
  httpGet: { path: /actuator/health/liveness, port: 8080 }
  initialDelaySeconds: 30
  periodSeconds: 10
readinessProbe:                     # "can I take traffic?" -> add/remove from Service
  httpGet: { path: /actuator/health/readiness, port: 8080 }
  periodSeconds: 5
resources:
  requests: { cpu: "500m", memory: "1Gi" }
  limits:   { memory: "1Gi" }       # keep -XX:MaxRAMPercentage below this limit
```

> **Key Takeaway:** Instrument the SLI at the code level, alert on budget burn in Prometheus, and protect dependencies with timeouts, backoff with jitter, and circuit breakers.

---

## 🏦 Real Production Example

**Scenario: UPI-style payment gateway**

```
+----------+    +-------------+    +----------------+    +-------------+
| Mobile   |--->| API Gateway |--->| Payment Service|--->| Core Ledger |
| App      |    | (rate limit)|    | (Spring Boot)  |    | (DB2/Oracle)|
+----------+    +-------------+    +-------+--------+    +-------------+
                                           |
                                           v
                                   +----------------+
                                   | Fraud / AML    |
                                   | Service        |
                                   +----------------+
```

| Item | Choice |
|---|---|
| SLI (availability) | Successful payment responses / valid payment requests (exclude 4xx caused by the user) |
| SLI (latency) | Share of payments completing under 500 ms |
| SLO | 99.95% availability, 99% of payments under 500 ms, over 30 days |
| Error budget | 21.6 minutes of full outage per 30 days |
| Page when | Fast burn (14.4x over 1h) |
| Ticket when | Slow burn (1x over 3 days) |

**Incident timeline (typical):**
1. 02:10: burn-rate alert pages on-call. p99 latency doubled after a deploy.
2. 02:15: incident commander assigned. Canary dashboard shows only the new version is affected.
3. 02:18: **mitigate first**, roll back the release. Root cause analysis comes later.
4. 02:30: error rate normal. Budget impact: about 9 minutes spent.
5. Next day: blameless postmortem. Action items: add a latency check to canary analysis, add a load test for the changed query.

> **Key Takeaway:** In production, mitigation beats diagnosis. Roll back first, find root cause once users are safe.

---

## ⚠ Common Mistakes

| Wrong approach | Problem | Correct approach |
|---|---|---|
| Target 100% availability | Impossible, blocks all change, cost explodes | Pick a realistic SLO and use the budget |
| Alert on CPU above 80% | Pages with no user impact, alert fatigue | Alert on symptoms: SLO burn, error rate, latency |
| Track average latency | Hides tail; 1% of users may wait 10x longer | Use p95/p99 histograms |
| Retry immediately, unlimited | Retry storm multiplies load on a struggling dependency | Cap attempts, exponential backoff with jitter, circuit breaker |
| No timeouts on HTTP/DB calls | Threads pile up, pool exhausted, cascading failure | Explicit connect and read timeouts everywhere |
| Dependency checks in liveness probe | A database blip restarts every pod at once | Liveness checks only the JVM; dependencies go in readiness |
| Blame individuals in postmortems | People hide mistakes, same failure repeats | Blameless review focused on system and process gaps |
| SLO set without product input | Target does not match user expectations | Agree SLOs with product and business owners |

```
Wrong:   SLO 100% -> any failure = "we broke the SLO" -> nobody ships
   |
   v
Impact:  Release freeze, shadow IT, hidden risk
   |
   v
Right:   SLO 99.95% -> 21.6 min budget -> ship, measure, spend deliberately
```

> **Key Takeaway:** Most SRE failures are cultural or measurement mistakes, not tooling ones.

---

## ✅ Best Practices

**Reliability and design**
- Set timeouts on every outbound call; make them shorter than the caller's timeout
- Use idempotency keys on payment and transfer APIs so retries are safe
- Degrade gracefully (queue, cache, partial response) instead of failing hard
- Use bulkheads (separate pools) so one slow dependency cannot starve the rest

**Observability**
- Metrics for rates, logs for details, traces for cross-service latency
- Propagate a trace ID through every log line (Micrometer Tracing / OpenTelemetry)
- Use structured JSON logs; never log PAN, tokens, or personal data

**Operations**
- Write a runbook for every paging alert; if no action is possible, it should not page
- Cap pages per on-call shift; review noisy alerts weekly
- Automate rollback in the deployment pipeline
- Use canary releases with automatic analysis against SLIs

**Thread safety and scalability**
- Keep services stateless so they scale horizontally
- Size thread and connection pools from measured saturation, not guesses
- Load test before launch and after any significant change

**Coding standards**
- Distinguish client errors (4xx) from server errors (5xx) so SLIs stay honest
- Expose health endpoints that reflect real readiness

> **Key Takeaway:** Reliability is built in at design time (timeouts, idempotency, degradation) and sustained by operations (alerts, runbooks, learning).

---

## ⚡ Performance Considerations

| Area | Consideration | Recommendation |
|---|---|---|
| CPU | GC and JIT warm-up spike CPU after start | Warm up before taking traffic (readiness delay) |
| Memory | Container limit versus heap size | `-XX:MaxRAMPercentage=75`; leave room for metaspace and native memory |
| Time complexity | Metric label cardinality grows memory in Prometheus | Never label with user ID, order ID, or full URL; use route templates |
| Scalability | Autoscaling reacts slowly to spikes | Scale on saturation or queue depth, keep headroom |
| Network | Cross-zone calls add latency and cost | Keep chatty services in the same zone |
| Caching | Cache stampede after expiry | Staggered TTLs, request coalescing |
| Thread usage | Blocking calls exhaust platform threads | Bulkheads, or virtual threads on Java 21 for I/O-heavy work |
| Alerting cost | Long-range queries are expensive | Use recording rules for 5m and 1h ratios |

> **Key Takeaway:** Observability itself has a cost. Control cardinality and use recording rules.

---

## ⚖ Trade-offs

| Advantages | Disadvantages |
|---|---|
| Shared language between product, dev, and ops | Needs organisational buy-in; error budget policy must be honoured |
| Data-driven release decisions | Good SLIs are hard to define for batch or asynchronous systems |
| Less alert fatigue, less toil | Observability tooling and training cost money |
| Faster, safer delivery | Small teams may not afford a dedicated SRE team |

**When to use:** user-facing, revenue-critical, or regulated services with real availability expectations.
**When NOT to use:** prototypes, internal tools with no uptime expectation, or products with too little traffic to measure meaningfully.

**Real-life analogy:** An error budget is a monthly spending budget. You decide in advance how much you can spend (unreliability). While money is left, you can buy things (ship features). When it runs out, you stop spending and fix your finances (reliability work).

---

## 📊 Comparison Table

| Aspect | SRE | DevOps | Traditional Ops | Platform Engineering |
|---|---|---|---|---|
| Nature | Specific implementation with prescriptive practices | Culture and set of practices | Run and maintain systems | Build internal developer platform |
| Core metric | SLO and error budget | Deployment frequency, lead time (DORA) | Uptime, tickets closed | Developer productivity and adoption |
| Approach to failure | Budget it, learn from it | Fix fast, shorten feedback loop | Avoid at all cost | Provide guardrails and golden paths |
| Work style | Code and automation | Collaboration across teams | Manual procedures | Product mindset for internal users |
| Best use case | Reliability of critical services | Faster, collaborative delivery | Stable, slow-changing systems | Many teams needing consistent tooling |

| Deployment strategy | Advantages | Disadvantages | Best use case |
|---|---|---|---|
| Rolling | Simple, no extra capacity | Mixed versions during rollout, slow rollback | Backward-compatible changes |
| Blue-green | Instant rollback, clean cutover | Double capacity cost | Risky or schema-light releases |
| Canary | Real traffic, small blast radius | Needs good metrics and routing | High-traffic critical services |

---

## 🎯 Interview Questions

**Q1. What is SRE and how is it different from DevOps?**
*Expected:* SRE is a concrete engineering discipline from Google that applies software engineering to operations with SLOs, error budgets, and a toil cap. DevOps is a broader culture of collaboration. A common line: "class SRE implements DevOps."
*Follow-up:* Can a company do DevOps without SRE?

**Q2. Explain SLI, SLO, and SLA with an example.**
*Expected:* SLI is the measurement (ratio of successful payments). SLO is the internal target (99.95% over 30 days). SLA is the contractual promise with consequences, usually looser than the SLO so there is a safety margin.
*Follow-up:* Why should the SLO be stricter than the SLA?

**Q3. What is an error budget and what happens when it is exhausted?**
*Expected:* Error budget is `1 − SLO`, the amount of unreliability you may spend. When it is gone, risky releases pause and the team prioritises reliability work, as defined in the error budget policy.
*Follow-up:* Who has authority to enforce the freeze?

**Q4. What are the four golden signals?**
*Expected:* Latency, traffic, errors, saturation. Mention separating successful and failed request latency.
*Follow-up:* How do RED and USE differ?

**Q5. What is toil and how do you handle it?**
*Expected:* Manual, repetitive, automatable work with no lasting value that scales with service growth. Cap it (about 50%), measure it, and automate the highest-cost items first.
*Follow-up:* Is on-call work always toil?

**Q6. Why use percentiles instead of averages for latency?**
*Expected:* Averages hide tail latency. Users experience the slow requests, and in fan-out architectures tail latency compounds. Use p95/p99 from histograms. Percentiles cannot be averaged across instances, so aggregate histogram buckets instead.
*Follow-up:* Why can you not average p99 across pods?

**Q7. What is burn-rate alerting and why is it better than threshold alerting?**
*Expected:* Burn rate is how fast you consume budget relative to the allowed rate. A burn rate of 1 uses the whole budget exactly at window end. Multi-window, multi-burn-rate alerts (for example 14.4x over 1h paging, 1x over 3 days ticketing) detect both sharp and slow burns with low noise.
*Follow-up:* Why combine a long window with a short window?

**Q8. What makes a good postmortem?**
*Expected:* Blameless, timeline, impact, root cause and contributing factors, what went well and badly, action items with owners and dates, shared widely.
*Follow-up:* How do you stop action items from being forgotten?

**Q9. How do you run an incident?**
*Expected:* Declare, assign an incident commander, separate roles (ops lead, communications), mitigate before diagnosing, communicate on a fixed cadence, record a timeline.
*Follow-up:* When do you escalate?

**Q10. What is a circuit breaker and when does it help?**
*Expected:* It stops calls to a failing dependency after a failure threshold, fails fast, and probes recovery in half-open state. It prevents thread exhaustion and cascading failure.
*Follow-up:* How do retries and circuit breakers interact?

**Q11. What is a retry storm and how do you prevent it?**
*Expected:* Many clients retry at once and overload a recovering service. Prevent with capped retries, exponential backoff with jitter, retry budgets, and circuit breakers.
*Follow-up:* Which operations are unsafe to retry?

**Q12. Liveness versus readiness probe?**
*Expected:* Liveness asks "should this container be restarted?" Readiness asks "should it receive traffic?" Put dependency checks in readiness only, otherwise a shared dependency failure restarts everything.
*Follow-up:* What is a startup probe for?

**Q13. Canary, blue-green, and rolling: when do you use each?**
*Expected:* See comparison table. Canary for high-risk, high-traffic services with automated analysis; blue-green for instant rollback; rolling for simple compatible changes.
*Follow-up:* How do you handle database schema changes in each?

**Q14. How do you choose an SLO for a new service?**
*Expected:* Start from user journeys and business need, measure current performance, pick a target slightly below what users already tolerate, agree with product, and review quarterly.
*Follow-up:* What if the dependency's SLO is lower than yours?

**Q15. What is chaos engineering?**
*Expected:* Deliberately injecting failures (pod kills, latency, dependency outage) in a controlled way to verify resilience assumptions, starting in lower environments with a defined hypothesis and blast radius.
*Follow-up:* How do you run it safely in production?

> **Key Takeaway:** Interviewers test whether you can connect definitions to decisions: budget, alerting, rollback, learning.

---

## 🧠 Scenario-Based Questions

**1. p99 latency doubles right after a deployment. What do you do?**
Check canary or version-split dashboards, compare old and new. If only the new version is affected, roll back immediately. Then investigate (query plans, thread pool saturation, new downstream call) using traces and a profiler. Add a latency gate to the canary analysis.

**2. 80% of the monthly error budget is gone in the first week. What now?**
Find the source (one incident or a slow leak). Pause risky releases per policy, prioritise the top contributors to errors, and review whether the SLO or the dependency is the real problem. Communicate status to product.

**3. Pods are OOMKilled intermittently. How do you troubleshoot?**
Compare container memory limit with heap settings. Heap plus metaspace, thread stacks, and direct buffers must fit in the limit. Enable `-XX:+HeapDumpOnOutOfMemoryError`, check for leaks, and tune `MaxRAMPercentage`. Look for off-heap growth (Netty, direct buffers).

**4. Design the SLOs for a payment API.**
Availability SLI (valid requests succeeded), latency SLI (share under 500 ms), correctness SLI (reconciliation mismatches). Define the 30-day window, exclude user-caused 4xx, set burn-rate alerts, and document the error budget policy.

**5. A slow downstream service takes your whole service down. How did this happen and how do you prevent it?**
Missing timeouts let threads block until the pool was exhausted, so even healthy endpoints stopped responding. Add timeouts, bulkheads, circuit breakers, fallback behaviour, and alert on thread pool saturation.

**6. The on-call engineer got 40 pages overnight. What do you change?**
Audit each alert for actionability. Remove or downgrade non-urgent alerts to tickets, deduplicate related alerts, convert threshold alerts to SLO burn alerts, and add runbooks. Track pages per shift as a team metric.

---

## 🛠 Debugging Tips

| Symptom | First checks | Tools / commands |
|---|---|---|
| High latency | Golden signals, recent deploys, dependency latency | Grafana, traces (Jaeger/Tempo) |
| 5xx spike | Error logs by trace ID, downstream health, config changes | Kibana/Loki, `kubectl logs` |
| Pod restarting | Restart reason, last state | `kubectl describe pod`, `kubectl logs --previous` |
| OOMKilled | Heap versus limit, leak, off-heap | `jcmd <pid> GC.heap_dump`, MAT |
| CPU spike | Hot threads, GC, tight loops | `jcmd <pid> Thread.print`, async-profiler |
| Threads stuck | Blocked or waiting threads, pool saturation | `jstack <pid>`, `/actuator/metrics` |
| Long GC pauses | Allocation rate, heap sizing | `-Xlog:gc*`, GC logs |
| DB slow | Slow query log, connection pool wait | `pg_stat_statements`, HikariCP metrics |

**Useful Java flags and commands**

```bash
# Heap dump on OOM, plus GC logging (JDK 9+ unified logging)
java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/dumps -Xlog:gc*:file=gc.log ...

# Thread dump of a running JVM (take 3 dumps, 10s apart, to spot stuck threads)
jcmd <pid> Thread.print

# Kubernetes triage
kubectl get pods -n payments
kubectl describe pod <pod> -n payments
kubectl logs <pod> --previous -n payments
kubectl top pod -n payments
```

**Debugging method:** hypothesis, evidence, narrow scope, change one variable. Always capture a heap dump and thread dump *before* restarting a misbehaving instance, because a restart destroys the evidence.

> **Key Takeaway:** Start from user-facing symptoms, narrow with traces, confirm with JVM-level evidence, and preserve evidence before restart.

---

## 📌 When to Use SRE Practices

- Payment, trading, lending, or onboarding systems with strict availability targets
- Many microservices where failure modes and dependencies are hard to reason about
- Teams with frequent releases that need a safe velocity policy
- Regulated environments needing evidence of incident handling and learning
- Services suffering from alert fatigue or recurring manual operations
- Platforms heading into peak events (salary day, festival sales, month-end)

## 🚫 When NOT to Use (or Go Lighter)

- Early prototypes where finding product fit matters more than uptime
- Internal tools with a handful of users and no availability expectation
- Services too low-traffic for statistically meaningful SLIs (use synthetic probes instead)
- Organisations unwilling to honour the error budget policy; the SLO becomes decoration

---

## 💡 Interview Tips

**What the interviewer expects**
- Clear definitions of SLI, SLO, SLA, and error budget with a worked number
- Ability to turn theory into an alerting and release decision
- Hands-on JVM and Kubernetes troubleshooting, not only tool names

**Common mistakes**
- Mixing up SLO and SLA
- Proposing 100% availability
- Saying "we monitor CPU and memory" as the whole answer
- Describing incidents without mentioning mitigation, communication, or learning

**How to answer confidently**
- Use a real incident: symptom, detection, mitigation, root cause, follow-up action
- Quote the numbers (budget minutes, burn rate, latency percentile)
- State trade-offs: "we accepted slower releases for two weeks to rebuild budget"

**Likely follow-ups:** multi-window burn rates, tail latency, retries and idempotency, handling a dependency with a worse SLO than yours, on-call health and toil.

---

## 📝 Short Conclusion Summary

SRE is the practice of running production systems with software engineering discipline. Reliability is defined as an SLO over a user-centred SLI, the remaining error budget decides whether to ship or stabilise, and alerts fire on budget burn rather than raw resource thresholds. Incidents are handled with clear roles and mitigation first, then studied in blameless postmortems that produce tracked fixes. Repetitive work is treated as toil and automated, and resilience patterns such as timeouts, backoff with jitter, circuit breakers, and graceful degradation keep partial failures from cascading.

---

## ⚡ TL;DR

| Topic | Summary |
|---|---|
| Purpose | Run reliable systems at scale by applying software engineering to operations |
| Internal Working | SLI → SLO → error budget → burn-rate alerts → incident response → postmortem |
| Best For | Critical, user-facing, regulated, high-change services |
| Avoid | Prototypes, tiny internal tools, orgs that ignore the error budget policy |
| Advantages | Shared reliability language, safer velocity, less toil and alert fatigue |
| Disadvantages | Needs cultural buy-in, good SLIs are hard, tooling and training cost |
| Performance | Control metric cardinality, use histograms and recording rules, size JVM within container limits |
| Interview Keyword | SLI, SLO, error budget, burn rate, golden signals, toil, blameless postmortem |

---

## 🚀 30-Second Interview Answer

> "SRE applies software engineering to operations so that reliability becomes something we measure and manage. We define SLIs from the user's point of view, for example the share of successful payments under 500 milliseconds, and set an SLO such as 99.95% over 30 days, which gives us an error budget of about 21 minutes. We alert on how fast that budget is burning, using multi-window burn-rate alerts, so pages mean real user impact. If the budget is healthy we ship faster; if it is spent we pause risky releases and fix reliability. During incidents we mitigate first, usually by rolling back, then run a blameless postmortem with tracked action items. On the Java side I back this with timeouts, retries with backoff and jitter, circuit breakers, idempotent APIs, and Micrometer metrics feeding Prometheus and Grafana."

---

## 📚 Further Reading

- Google, *Site Reliability Engineering* (free online at sre.google/books)
- Google, *The Site Reliability Workbook* (SLO alerting chapter covers burn-rate windows)
- Spring Boot reference: Actuator and Micrometer metrics
- Resilience4j documentation: CircuitBreaker, Retry, Bulkhead
