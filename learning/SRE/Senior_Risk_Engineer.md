# 🎯 Senior Risk Engineer - Interview Question Bank

**Profile assumed:** Senior Java backend engineer (13 yrs), BFSI (Morgan Stanley, Danske, Credit Suisse), interviewing for a *risk technology* role (market / credit / counterparty risk platforms, limits, risk reporting).
**How to use:** Each question has **what they are really testing** and **answer pointers**. Star (★) = very likely. Prepare 2 project stories you can reuse across sections.

> If the role is actually *operational/tech risk* or *SRE-style reliability risk*, tell me and I'll re-target the list.

---

## 1. Resume & Role Fit (always asked first)

| # | Question | Pointers |
|---|---|---|
| 1 ★ | Walk me through your experience on risk/trading platforms. | Pick one system: business purpose, volumes, your ownership, measurable result (latency, cost, run-time). |
| 2 ★ | What was the most complex risk calculation/batch you owned? | Inputs (positions, market data), engine, output (VaR/exposure), SLA, failure handling. |
| 3 | What does "risk" mean to an engineer vs a quant vs a risk manager? | Engineer = correctness, lineage, performance, auditability; quant = model; manager = limits/decisions. |
| 4 ★ | Why risk engineering? Why this firm? | Correctness-critical, regulatory, scale; tie to your Morgan Stanley / Credit Suisse exposure. |
| 5 | What is your biggest production incident and what changed afterwards? | STAR format; root cause, detection, fix, prevention (alerts, runbook, tests). |

---

## 2. Risk Domain Fundamentals (engineer-level, not quant-level)

| # | Question | Pointers |
|---|---|---|
| 6 ★ | Types of financial risk? | Market, credit, counterparty (CCR), liquidity, operational, model risk. |
| 7 ★ | What is VaR? Methods? | Loss not exceeded at confidence c over horizon h (e.g. 99%, 1-day / 10-day). **Historical, parametric (variance-covariance), Monte Carlo.** Trade-offs: speed vs tail realism vs compute. |
| 8 ★ | VaR limitations? What is Expected Shortfall? | VaR ignores tail beyond the cut-off and isn't sub-additive; **ES** = average loss beyond VaR; FRTB moves from 99% VaR to **97.5% ES**. |
| 9 | What is backtesting? | Compare VaR with actual P&L; Basel traffic light on 250 days at 99%: green 0-4 exceptions, yellow 5-9, red 10+. |
| 10 | Stress testing vs scenario analysis vs sensitivity? | Stress = extreme shocks (historical/hypothetical); scenario = defined narrative; sensitivity = single-factor bump. |
| 11 ★ | What are the Greeks? | Delta, gamma, vega, theta, rho; **sensitivity-based** risk (bump-and-reval vs analytic/AAD). |
| 12 ★ | Counterparty credit risk: EAD, PD, LGD, PFE, EE, CVA? | EAD exposure at default; PD/LGD probabilities and losses; PFE high-percentile future exposure; EE expected exposure; CVA price of counterparty default risk. |
| 13 | Netting and collateral (CSA/margin)? | Netting sets reduce exposure; variation/initial margin; impact on exposure calculation. |
| 14 | Basel III/IV and FRTB at a high level? | Capital rules; FRTB: Expected Shortfall, liquidity horizons, standardised vs internal model approach. |
| 15 | What is a limit framework? Pre-trade vs post-trade limit checks? | Hard/soft limits, breaches, escalation; pre-trade = low-latency blocking, post-trade = batch/near-real-time monitoring. |
| 16 | Intraday vs end-of-day risk? | EOD = full revaluation, official numbers; intraday = incremental, approximate, lower latency. |
| 17 | What is risk aggregation and why is it hard? | Hierarchies (desk -> book -> legal entity), non-additive measures (VaR isn't additive), consistent market data snapshot. |
| 18 | Data lineage and reconciliation in risk? | Reproducibility (as-of, versioned market data/models), auditors need to explain every number. |

**Insight:** you don't need quant-level math; you must show *you understand what the numbers mean and why they can't be wrong or late*.

---

## 3. Core Java & JVM (depth expected at 13 yrs)

| # | Question | Pointers |
|---|---|---|
| 19 ★ | How do you represent money/risk amounts? | `BigDecimal` with scale and `RoundingMode`; never `double` for money; but `double` for heavy numerical simulation with documented tolerances. |
| 20 ★ | Explain the Java Memory Model and `volatile`/happens-before. | Visibility + ordering; `synchronized`, `final` field semantics; safe publication. |
| 21 ★ | HashMap internals; ConcurrentHashMap vs synchronizedMap. | Buckets, treeification at 8 (min table 64), CAS + bin-level locking; no null keys in CHM. |
| 22 ★ | GC choices for low-latency / large-heap risk engines? | G1 default, **ZGC / Shenandoah** for low pause; sizing, allocation rate, GC logs. |
| 23 | How do you find a memory leak / high CPU in production? | Heap dump (MAT), thread dumps, async-profiler / JFR, GC logs. |
| 24 | Java 8 -> 17 -> 21 features you actually use? | Streams, records, sealed types, pattern matching, virtual threads (Java 21). |
| 25 | Immutability and why it matters for risk? | Thread-safe sharing of market data snapshots, reproducibility. |
| 26 | Streams vs loops for heavy compute? Parallel streams pitfalls? | Shared ForkJoin common pool, uneven splits, avoid blocking/ordering dependencies, measure. |
| 27 | Serialization for caching/messaging? | Avoid Java serialization (security, size); Protobuf/Avro/Kryo; versioning. |
| 28 | Reduce object allocation in hot paths? | Primitive collections, reuse buffers, avoid boxing, escape analysis, off-heap where justified. |

---

## 4. Concurrency & Performance (heavy in risk interviews)

| # | Question | Pointers |
|---|---|---|
| 29 ★ | Design a thread-safe, high-throughput position/exposure aggregator. | Partition by key (book/instrument), single-writer per partition, `LongAdder`/`ConcurrentHashMap.merge`, avoid global locks. |
| 30 ★ | Executor design for a CPU-bound revaluation grid vs I/O-bound services? | CPU pool ~= cores; separate bounded pools for I/O; avoid unbounded queues; backpressure. |
| 31 | `CompletableFuture` fan-out/fan-in with timeouts and failure handling? | `allOf`, `orTimeout`, `exceptionally`; partial results policy. |
| 32 | Deadlock / livelock / starvation: detection and prevention? | Lock ordering, tryLock, thread dumps. |
| 33 | Lock-free vs locking; false sharing; `LongAdder`? | CAS, contention, cache-line padding. |
| 34 | Latency vs throughput - how do you measure and tune? | p50/p99/p99.9, JMH microbenchmarks, load tests, coordinated omission. |
| 35 ★ | A risk batch that took 2h now takes 4h. How do you investigate? | Compare inputs (volume), profile, DB plans, GC, skew, noisy neighbours, recent deploys; bisect. |
| 36 | Virtual threads: where help, where not? | Blocking I/O scale-up; not for CPU-bound; pinning with `synchronized`. |
| 37 | Disruptor / ring buffer / mechanical sympathy? | Single-writer principle, cache-friendly structures, avoiding locks. |

---

## 5. Data, Databases & Caching

| # | Question | Pointers |
|---|---|---|
| 38 ★ | Design storage for positions, trades and risk results (time-series, as-of queries). | Bitemporal modelling (valid time + transaction time), partitioning by date/book, immutable results. |
| 39 | SQL tuning: slow query on a 500M-row table? | Execution plan, indexes (composite/covering), partition pruning, avoid `SELECT *`, stats. |
| 40 | Transactions, isolation levels, phantom/non-repeatable reads? | Read committed vs repeatable read vs serializable; optimistic vs pessimistic locking. |
| 41 | Batch writes with JPA/JDBC? | `saveAll` + batch size, `rewriteBatchedStatements`, avoid N+1, stateless sessions. |
| 42 ★ | Caching market data / reference data? | In-memory (Caffeine/Hazelcast/Coherence/Redis), TTL, invalidation, consistency of **snapshots** (all risk calcs use the same snapshot ID). |
| 43 | SQL vs NoSQL vs columnar/time-series for risk? | OLTP for trades, columnar/OLAP (ClickHouse, Parquet) for aggregation, KV for cache. |
| 44 | Data quality controls on inputs? | Validation, completeness checks, stale price detection, reconciliation with source. |

---

## 6. Architecture: Microservices, Messaging & Reliability

| # | Question | Pointers |
|---|---|---|
| 45 ★ | Kafka in a trading/risk context: ordering, exactly-once, partitioning? | Key by instrument/book for ordering; at-least-once + idempotent consumers; transactions/EOS limits; consumer lag. |
| 46 ★ | How do you keep data consistent across services? | Outbox, Saga, idempotency (see Microservices handbook). |
| 47 | Event-driven vs batch risk architecture? | Lambda/Kappa; incremental risk vs EOD full run; reconciling both. |
| 48 | Circuit breaker, retry, bulkhead for market data feeds? | Fallback to last good snapshot with staleness flag - **never silently invent values**. |
| 49 | Idempotency and exactly-once effect for trade/position updates? | Event IDs, versioning, dedup tables. |
| 50 | Observability for risk systems? | Metrics (batch duration, calc failures, staleness), traces, structured logs, SLOs, alerts on *missing* data. |
| 51 | Resilience: DR, RTO/RPO, active-active vs active-passive? | Risk numbers feed regulatory reports - define RTO/RPO per tier. |
| 52 | Security and entitlements in risk data? | OAuth2/JWT, RBAC/ABAC by desk/legal entity, audit trails, encryption, data masking. |

---

## 7. System Design Questions (pick structure: requirements -> scale -> design -> failure -> trade-offs)

| # | Question | What to cover |
|---|---|---|
| 53 ★ | Design a **real-time pre-trade risk/limit check** system. | Sub-ms/ms latency budget, in-memory exposure state, partition by account/desk, async persistence, fail-open vs fail-closed policy, kill switch. |
| 54 ★ | Design an **end-of-day VaR/exposure calculation platform** (millions of trades). | Market data snapshot, trade ingestion, grid compute (partition by book/risk factor), aggregation tree, results store, re-run/restate, SLA and lineage. |
| 55 | Design a **limit monitoring and breach alerting** service. | Streaming aggregation, hierarchy rollups, hysteresis, escalation workflows, audit. |
| 56 | Design **counterparty exposure** (PFE) calculation at scale. | Monte Carlo paths, netting-set aggregation, GPU/grid, caching of path-dependent valuations. |
| 57 | Design a **market data distribution** and snapshotting service. | Ingest feeds, normalise, golden source, versioned snapshots, replay for reproducibility. |
| 58 | Design a **risk reporting/dashboard** backend. | CQRS read models, pre-aggregation, drill-down, caching, entitlements. |
| 59 | How would you migrate a legacy batch risk system to a modern platform? | Strangler Fig, parallel run + reconciliation tolerances, cut-over plan, rollback. |

**Insight:** always state *fail-open vs fail-closed*, *as-of reproducibility* and *reconciliation* - these are the risk-domain tells.

---

## 8. Testing, Quality & Controls

| # | Question | Pointers |
|---|---|---|
| 60 ★ | How do you test a risk calculation engine? | Golden datasets, property-based tests, tolerance-based comparison, parallel run vs legacy, regression on production snapshots. |
| 61 | Contract testing and backward compatibility of risk APIs/events? | Pact/Spring Cloud Contract, schema registry. |
| 62 | Performance/regression testing in CI? | JMH, load tests with production-like volumes, performance budgets. |
| 63 | How do you prove numbers are correct to auditors? | Lineage, versioning, reproducibility, reconciliations, controls, sign-offs. |
| 64 | Release process for a system that can't be wrong at EOD? | Feature flags, canary, freeze windows, rollback, blue/green. |

---

## 9. Leadership, Delivery & Behavioural (Senior level)

| # | Question | Pointers |
|---|---|---|
| 65 ★ | Tell me about a time you influenced architecture decisions. | Context, options considered, trade-offs, outcome, how you got buy-in. |
| 66 ★ | How do you work with quants, risk managers and traders? | Translate requirements, validate assumptions, short feedback loops, shared test data. |
| 67 | How do you handle a production issue during the EOD run? | Incident roles, comms, mitigation vs root cause, post-mortem (blameless). |
| 68 | How do you mentor juniors / raise code quality? | Code reviews, pairing, standards, tech talks, measurable outcomes. |
| 69 | Disagreement with a stakeholder about scope or risk? | Data-driven, options with cost/risk, escalate appropriately. |
| 70 | How do you balance delivery speed with regulatory controls? | Automate controls, risk-based approach, define "non-negotiables". |
| 71 | How do you estimate and de-risk a large migration? | Slice thin, spikes, parallel runs, milestones, metrics of success. |

---

## 10. Scenario / Troubleshooting (rapid-fire)

1. EOD risk shows a 12% jump in VaR vs yesterday. **What do you check first?** *(market data snapshot, missing/duplicate trades, stale prices, FX, model/config change, then genuine market move)*
2. A consumer processed the same trade event twice. **Impact and fix?** *(double exposure; idempotency key, dedup, reconcile positions)*
3. Market data feed is delayed 20 minutes. **What should the system do?** *(staleness flag, last good snapshot with clear marking, alerting, fail-open/closed policy)*
4. A deployment changed risk numbers by 0.01%. **Acceptable?** *(define tolerance, investigate rounding/ordering of floating-point sums, parallel run)*
5. Latency of limit checks doubled after a release. **How do you isolate?** *(profiling, allocation/GC, lock contention, DB calls, config)*
6. Out-of-order position updates arrive. **How do you handle?** *(versions/sequence numbers, per-key ordering, idempotent upserts)*
7. Database becomes the bottleneck in the batch. **Options?** *(partitioning, batching, read replicas, columnar store, pre-aggregation, caching)*

---

## 11. Questions YOU Should Ask Them

- Which risk types and asset classes does this platform cover? Real-time or batch?
- What are the SLAs (EOD completion time, pre-trade latency) and current pain points?
- How are models validated and released? How do engineers and quants collaborate?
- What does the tech stack look like (Java version, Kafka, grid/compute, DB, cloud)?
- How is on-call/production support handled? How do you measure engineering quality?
- Regulatory drivers right now (FRTB, SA-CCR, CCAR/stress testing)?

---

## 12. 7-Day Preparation Plan

| Day | Focus |
|---|---|
| 1 | Risk basics: VaR, ES, Greeks, CCR (EAD/PFE/CVA), Basel/FRTB overview |
| 2 | Core Java, JMM, GC, concurrency drills |
| 3 | Data: bitemporal design, SQL tuning, caching |
| 4 | Kafka + microservices patterns (use the Microservices handbook) |
| 5 | System design: pre-trade limits, EOD VaR platform (draw each) |
| 6 | Behavioural: 5 STAR stories (incident, migration, conflict, mentoring, influence) |
| 7 | Mock interview + questions to ask |

## 📚 Resources

- *Options, Futures, and Other Derivatives* - John Hull (Greeks, VaR basics)
- *Risk Management and Financial Institutions* - John Hull
- *Counterparty Credit Risk and CVA* - Jon Gregory (CCR/CVA)
- BCBS FRTB standard (BIS): https://www.bis.org/bcbs/publ/d457.htm
- *Java Concurrency in Practice* - Brian Goetz
- *Designing Data-Intensive Applications* - Martin Kleppmann
- *Systems Performance* - Brendan Gregg
- Martin Fowler - Bitemporal History: https://martinfowler.com/articles/bitemporal-history.html
- Your own handbooks: Microservices Design Patterns, SRE, Design Patterns (reuse for sections 6-7)

> **Interview Tip:** For every answer, add one sentence on **correctness, auditability and failure behaviour**. That is what separates a risk-platform engineer from a generic backend engineer.
