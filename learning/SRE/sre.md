# SRE Interview Questions and Answers
## For a Senior Java Backend Developer with 13 Years of Experience

For a candidate with 13 years of Java backend development experience, an SRE interview typically assesses more than Java coding. Interviewers want to know whether you can keep production systems reliable, diagnose complex incidents, automate operational work, and design services that scale and recover from failures.

Your Spring Boot microservices experience gives you a strong foundation. The key is to connect it to SRE principles: availability, latency, monitoring, incident management, automation, capacity planning, and production ownership.

## 1. SRE Fundamentals

### Q1. What is Site Reliability Engineering (SRE)?
**Answer:** SRE is an engineering discipline that applies software engineering principles to operations to make systems reliable, scalable, and efficient.

Key responsibilities include:
- Defining and measuring reliability targets.
- Monitoring services and responding to production incidents.
- Automating repetitive operational work.
- Reducing failure rates and recovery time.
- Improving capacity, performance, and resilience.
- Learning from incidents through blameless postmortems.

**Senior-level answer:** Focus not just on restoring a service after an incident, but also on eliminating recurring causes through automation, architecture improvements, and measurable reliability objectives.

### Q2. What is the difference between SLI, SLO, and SLA?
| Term | Meaning | Example |
|---|---|---|
| SLI | Service Level Indicator: a measured reliability metric | Percentage of successful HTTP requests |
| SLO | Service Level Objective: the internal reliability target | 99.9% successful requests over 30 days |
| SLA | Service Level Agreement: a commitment to customers, often with consequences for breaches | Contractual availability commitment |

**Remember:** The SLI measures, the SLO targets, and the SLA commits.

### Q3. What is an error budget?
An error budget is the amount of unreliability permitted by an SLO.

For a 99.9% availability SLO over a 30-day month:

`Error budget = 30 × 24 × 60 × 0.001 = 43.2 minutes`

This is equivalent downtime if availability is measured by downtime. Request-based error budgets may be more meaningful for distributed services.

If the error budget is being consumed too quickly, prioritize reliability improvements and reconsider the risk of new releases.

### Q4. What is toil in SRE?
Toil is repetitive, manual, automatable operational work that scales with service growth.

Examples include manually restarting services, repeatedly clearing queues, or running the same deployment commands by hand.

Ways to reduce toil:
- Automate routine actions with scripts or pipelines.
- Introduce self-healing mechanisms where safe.
- Create actionable runbooks.
- Fix recurring root causes instead of repeatedly applying workarounds.

### Q5. What are the four golden signals of monitoring?
1. **Latency:** How long requests take.
2. **Traffic:** How much demand the service receives.
3. **Errors:** How many requests fail.
4. **Saturation:** How close a resource is to its capacity.

For a Spring Boot service, these might include p95 response time, requests per second, HTTP 5xx rate, and CPU or connection-pool utilization.

### Q6. What is the difference between monitoring and observability?
**Monitoring** tells you when known conditions occur—for example, CPU utilization exceeds a threshold.

**Observability** helps you investigate why a system is behaving unexpectedly using metrics, logs, traces, and other diagnostic evidence.

For microservices, distributed tracing is especially valuable because one user request can pass through multiple services, databases, and message brokers.

### Q7. What is the difference between availability and reliability?
Availability measures whether a service is usable when required. Reliability is broader: it includes consistent correct behavior, fault tolerance, and recovery.

A service can be technically available but unreliable if it frequently returns incorrect results, times out, or loses data.

## 2. Incident Management and Production Troubleshooting

### Q8. A production service's error rate suddenly increases. What would you do?
**Answer:** Follow a structured incident-response process rather than immediately restarting the service.

1. **Assess impact:** Identify affected endpoints, customers, regions, and dependencies.
2. **Check recent changes:** Review deployments, configuration changes, infrastructure events, and traffic changes.
3. **Inspect telemetry:** Examine error rate, latency, CPU, memory, thread pools, database pools, and dependency health.
4. **Correlate failures:** Use logs and distributed traces to identify the failing component.
5. **Mitigate:** Roll back a release, disable a problematic feature, shift traffic, or apply another safe mitigation.
6. **Verify recovery:** Confirm that the error rate and latency return to acceptable levels.
7. **Prevent recurrence:** Conduct a blameless postmortem and track corrective actions.

**Senior-level point:** During a major incident, restoration and communication come first. Root-cause analysis can continue after the system is stable.

### Q9. What is an incident, and how do you manage a major incident?
An incident is an unplanned event that degrades service reliability or threatens an agreed service objective.

For a major incident:
- Assign an incident commander.
- Delegate investigation to appropriate technical responders.
- Maintain a clear communication channel and status updates.
- Record the timeline, evidence, and decisions.
- Prioritize safe mitigation over speculative fixes.
- Confirm recovery and communicate closure.
- Conduct a blameless postmortem.

The incident commander coordinates the response; technical responders investigate and mitigate the failure.

### Q10. What is a blameless postmortem?
A blameless postmortem examines how a failure occurred and why existing safeguards did not prevent or contain it, without focusing on individual blame.

It should contain:
- Impact and duration.
- Detection method and response timeline.
- Technical and contributing factors.
- What worked and what did not.
- Corrective actions with owners and deadlines.

For example, if an application exhausts its database connections, the fix might involve pool tuning, connection leak detection, and query optimization—not simply instructing someone to be more careful.

### Q11. What is MTTR, and how can you reduce it?
MTTR commonly means *Mean Time to Repair* or *Mean Time to Recovery*, depending on the organization's definition.

Ways to reduce it:
- Improve alert quality and detection speed.
- Maintain actionable runbooks.
- Add request correlation IDs and distributed tracing.
- Automate safe rollback and recovery procedures.
- Practise incident response through game days.
- Make deployment and configuration changes easier to reverse.

Clarify how your organization defines MTTR because the measured start and end points can differ.

### Q12. How would you troubleshoot a service with high latency but a low error rate?
Investigate latency distributions, especially p95 and p99, instead of looking only at averages.

1. Determine whether latency is isolated to one endpoint or affects the whole service.
2. Compare application, database, and dependency latency.
3. Inspect traces for slow spans.
4. Check CPU, GC pauses, thread-pool queues, connection pools, locks, and network delays.
5. Compare current behavior with traffic levels and recent changes.
6. Mitigate the bottleneck and verify the improvement.

A slow downstream API can cause request threads to remain blocked even while CPU utilization stays low.

### Q13. What is a runbook, and what makes one useful?
A runbook is a documented procedure for diagnosing or responding to a known operational condition.

A good runbook includes:
- Symptoms and alert links.
- Diagnostic commands and dashboards.
- Expected output or interpretation.
- Safe mitigation steps and rollback instructions.
- Escalation contacts and verification criteria.

Runbooks should be tested and updated after incidents. A list of commands without interpretation or safety guidance is not sufficient.

## 3. Java, JVM, and Spring Boot SRE Questions

### Q14. A Java application has high CPU usage. How would you investigate it?
First, identify which process and threads are consuming CPU. Collect evidence before making changes.

```bash
top -H -p <PID>
jcmd <PID> Thread.print
jstat -gcutil <PID> 1000 10
```

- `top -H` displays thread-level CPU usage.
- `jcmd Thread.print` captures a thread dump.
- `jstat -gcutil` helps inspect garbage-collection activity.

Correlate high-CPU thread IDs with thread-dump stacks and inspect hot code paths, tight loops, excessive serialization, regex processing, lock contention, and GC activity. A profiler or Java Flight Recorder can provide deeper evidence.

**Senior-level point:** High CPU does not necessarily mean a memory leak. CPU saturation and memory pressure are different problems, although they can occur together.

### Q15. How would you investigate an OutOfMemoryError?
First, identify the specific error:
- `Java heap space`
- `GC overhead limit exceeded`
- `Metaspace`
- `Direct buffer memory`
- `unable to create native thread`

Then investigate the relevant resource.

For heap exhaustion, review heap usage, GC logs, allocation behavior, and heap dumps. Look for retained collections, caches without eviction, unbounded queues, or objects held by long-lived references.

For native-memory or thread-related failures, inspect thread counts, direct buffers, process memory, container limits, and JVM configuration.

**Important:** Increasing `-Xmx` can provide temporary relief, but it does not fix a memory leak or unbounded growth.

### Q16. What is the difference between a memory leak and high memory usage?
High memory usage is an observation; a memory leak is one possible cause.

- **High memory usage:** May be expected under a large workload or a large but healthy cache.
- **Memory leak:** Objects that are no longer useful remain reachable and continue consuming memory.

Compare heap snapshots over time, inspect retained objects and reference paths, and determine whether memory returns after garbage collection.

In a containerized environment, also compare JVM heap usage with total process and container memory.

### Q17. A Spring Boot service has many threads waiting or blocked. What do you check?
Capture several thread dumps over a short interval and compare them.

Investigate:
- Deadlocks and monitor contention.
- Blocking I/O and slow downstream calls.
- Exhausted Tomcat or application executor pools.
- Database connection-pool exhaustion.
- Locks held for too long.
- Excessive synchronized work or thread starvation.

Correlate thread states with request latency and dependency metrics.

**Example:** If all request threads are blocked waiting for database connections, adding more web threads may worsen resource contention rather than solve the problem.

### Q18. How would you troubleshoot slow garbage collection?
Examine GC logs and Java Flight Recorder data to understand allocation rate, pause duration, heap occupancy, and collection frequency.

Potential causes include:
- Excessive short-lived allocations.
- Large retained object graphs.
- An undersized heap relative to the workload.
- Large caches or unexpectedly large payloads.
- A collector or configuration unsuitable for the latency target.

Understand the allocation and retention pattern first, then tune the JVM if needed. Validate changes using representative workloads and latency percentiles.

### Q19. How do you prevent database connection-pool exhaustion?
- Set a suitable maximum pool size based on database capacity and workload.
- Ensure connections are closed or returned to the pool.
- Configure connection and query timeouts.
- Investigate slow queries and transactions holding connections too long.
- Monitor active, idle, pending, and timed-out connections.
- Avoid increasing the pool blindly.

The combined connection demand from all service instances must remain within the database's safe connection capacity.

### Q20. What Java-specific metrics would you monitor in production?
- Heap and non-heap memory.
- GC frequency and pause duration.
- CPU and process memory.
- Live threads and thread-pool utilization.
- Request rate, error rate, and p95/p99 latency.
- Database connection-pool usage.
- HTTP client connection pools and downstream latency.
- Executor queue depth and task rejections.
- JVM process restarts and container resource throttling.

Use metrics for detection and dashboards, and thread dumps, logs, traces, and profiles for diagnosis.

## 4. Microservices, Resilience, and Distributed Systems

### Q21. What is a circuit breaker, and when would you use it?
A circuit breaker prevents repeated calls to a dependency that is failing or responding too slowly.

Typical states:
- **Closed:** Requests are allowed through.
- **Open:** Calls fail fast or use a fallback.
- **Half-open:** A limited number of trial calls determine whether the dependency has recovered.

In Spring Boot, Resilience4j is a common option.

Configure it based on the dependency's latency and failure characteristics, combine it with sensible timeouts, and ensure that fallbacks do not conceal data corruption or critical business failures.

### Q22. What is the difference between retries and timeouts?
A **timeout** limits how long an operation is allowed to wait. A **retry** attempts an operation again after a failure.

Retries should be:
- Limited in number.
- Restricted to appropriate transient failures.
- Configured with backoff and jitter.
- Applied only when the operation is safe to repeat or protected by idempotency.

Uncontrolled retries can amplify an outage. If a downstream service is overloaded, every caller retrying immediately can create a retry storm.

### Q23. What is idempotency, and why is it important for SRE?
An operation is idempotent if repeating it produces the same intended effect as executing it once.

For example, a payment API may accept an idempotency key so that a client retry does not create a duplicate charge.

This matters because networks fail, responses can be lost, and clients cannot always determine whether the server completed an operation.

Design idempotency around durable request tracking, unique constraints, and clearly defined transaction boundaries rather than relying only on in-memory checks.

### Q24. How do you handle failures in asynchronous messaging systems?
Consider:
- Retry policies for transient failures.
- Dead-letter queues for messages that repeatedly fail.
- Idempotent consumers to handle redelivery.
- Message ordering and partitioning requirements.
- Consumer lag and processing latency.
- Schema compatibility and poison messages.
- Backpressure and safe replay procedures.

For Kafka, monitor consumer lag and processing throughput. Determine whether the bottleneck is a slow consumer, partition imbalance, a downstream dependency, or insufficient processing capacity.

### Q25. How would you design a highly available microservice?
1. Run multiple instances across suitable failure domains.
2. Use health checks and load balancing.
3. Remove unhealthy instances from traffic.
4. Make dependencies resilient through timeouts, bounded retries, and circuit breakers.
5. Ensure database and messaging layers have appropriate availability and recovery mechanisms.
6. Automate deployments and rollback.
7. Monitor SLIs and test failure scenarios.

High availability is an end-to-end property. Multiple application instances do not guarantee availability if every instance depends on a single database or shared service that can fail.

### Q26. How would you handle a cascading failure across microservices?
A cascading failure occurs when one component's failure causes additional components to become overloaded or fail.

Approach:
- Identify the original failing dependency and affected request paths.
- Reduce load through traffic control, rate limiting, or feature degradation.
- Apply appropriate timeouts and circuit breakers.
- Limit retries and concurrent requests.
- Preserve capacity for critical functions.
- Recover dependencies gradually and monitor system-wide behavior.

Longer-term improvements include bulkheads, load shedding, dependency isolation, capacity testing, and resilience exercises.

## 5. Linux, Networking, and Kubernetes

### Q27. A Linux server is slow. How would you investigate it?
Start with system-level evidence:

```bash
uptime
top
free -m
vmstat 1 5
iostat -xz 1 5
df -h
ss -s
```

These help assess load, CPU and memory pressure, paging, disk I/O, filesystem usage, and network socket statistics.

Also check process-level resource usage, system logs, application logs, and relevant network connections. Identify the constrained resource before changing configuration or restarting processes.

### Q28. What is the difference between CPU utilization and load average?
CPU utilization measures how busy CPUs are over a measurement interval.

Load average on Linux represents the average number of tasks that are runnable or waiting in uninterruptible sleep, over intervals such as 1, 5, and 15 minutes.

A high load average does not automatically mean high CPU utilization. Tasks waiting for disk I/O can contribute to load while CPU usage remains relatively low.

### Q29. A Kubernetes pod is repeatedly restarting. How do you investigate it?
Inspect the pod state, events, logs, resource limits, and health probes.

```bash
kubectl get pods
kubectl describe pod <pod-name>
kubectl logs <pod-name> --previous
kubectl get events --sort-by=.metadata.creationTimestamp
```

Common causes include:
- Application crashes.
- `OOMKilled` due to memory limits.
- Failed liveness probes.
- Incorrect configuration or secrets.
- Dependency startup failures.
- CPU throttling or startup timeouts.

Determine the actual cause before increasing resource limits or changing probes.

### Q30. What is the difference between liveness, readiness, and startup probes?
| Probe | Purpose |
|---|---|
| Liveness | Determines whether a container should be restarted |
| Readiness | Determines whether a pod should receive traffic |
| Startup | Allows a slow-starting application time to initialize before other probes take effect |

For a Spring Boot application, probe configuration should reflect actual application lifecycle behavior. A temporary downstream dependency issue should not automatically cause every instance to restart.

### Q31. How would you investigate a Kubernetes pod with high memory usage?
Compare:
- Container memory usage and configured limits.
- JVM heap and non-heap memory.
- Direct buffers and thread stacks.
- GC activity and allocation rate.
- Recent deployments and workload changes.

Java heap is only part of the process's memory footprint. A container can exceed its memory limit even if the heap appears healthy.

Use appropriate JVM diagnostics and container metrics to establish where the memory is being consumed.

### Q32. How would you troubleshoot a service that is reachable internally but not externally?
Trace the connection path layer by layer:
1. DNS resolution.
2. Client connectivity and routing.
3. Firewall or network policy.
4. Load balancer or ingress configuration.
5. Service and endpoint configuration.
6. Pod readiness and application port binding.
7. TLS certificates and protocol configuration.

Compare a successful internal request with the failing external request to isolate the first point of failure.

## 6. Monitoring, Alerting, and SRE Tooling

### Q33. What is the difference between Prometheus and Grafana?
**Prometheus** collects and stores time-series metrics and supports querying them with PromQL.

**Grafana** visualizes metrics and other telemetry through dashboards and can help present alerting information.

For example, Prometheus might collect request duration and error counters from a Spring Boot application, while Grafana displays latency trends and error rates.

### Q34. How would you design effective alerts?
Prefer actionable alerts tied to user impact or a meaningful reliability objective.

Good alerting principles:
- Alert on sustained error-rate or latency degradation.
- Use SLO-based burn-rate alerts when appropriate.
- Set thresholds based on baselines and service requirements.
- Avoid alerting on every transient spike.
- Include dashboards, context, and runbook links.
- Distinguish immediate paging from lower-priority tickets.

An alert should indicate a condition requiring investigation or action—not merely report that a metric changed.

### Q35. What is distributed tracing, and how does it help?
Distributed tracing follows a request as it travels across multiple services. Each operation contributes a span, and related spans form a trace.

For a request passing through an API gateway, a Spring Boot service, and a database, tracing helps identify which operation contributes most to total latency.

OpenTelemetry is a common instrumentation standard, while a compatible backend can store and visualize traces.

### Q36. What is the difference between logs, metrics, and traces?
| Telemetry | Best suited for |
|---|---|
| Logs | Detailed events and diagnostic messages |
| Metrics | Trends, rates, resource utilization, and alerting |
| Traces | Request paths and latency across distributed components |

They work best together. A trace can identify a slow database operation, metrics can reveal whether the problem affects many requests, and logs can explain what happened during the operation.

### Q37. How would you reduce alert fatigue?
Review alerts that are frequently acknowledged without action, duplicate the same symptom, or provide insufficient context.

Then:
- Remove or consolidate redundant alerts.
- Use symptom-based alerts and appropriate severity levels.
- Add deduplication and grouping.
- Tune thresholds and evaluation windows.
- Create clear runbooks.
- Review alert quality after incidents.

The objective is not simply fewer alerts; it is ensuring that important alerts receive attention quickly.

## 7. CI/CD, Automation, and Capacity Planning

### Q38. How do you ensure a deployment does not impact production reliability?
Use a controlled deployment process:
- Automated unit, integration, and security tests.
- Health checks and pre-deployment validation.
- Canary or rolling deployments where appropriate.
- Monitoring of error rates and latency during rollout.
- Automated or well-defined rollback criteria.
- Backward-compatible database and API changes.

For high-risk changes, release gradually and verify the service against its SLO before expanding traffic.

### Q39. What is the difference between blue-green and canary deployment?
| Blue-green | Canary |
|---|---|
| Two deployment environments are maintained | A small portion of traffic is sent to the new version |
| Traffic switches between environments | Traffic increases gradually as confidence grows |
| Rollback can be a traffic switch | Rollback involves routing traffic away from the new version |
| Requires managing two environments | Requires traffic splitting and suitable telemetry |

Both approaches require compatible data changes, health checks, and validation.

### Q40. How would you automate repetitive operational tasks?
Identify a frequent, well-understood task and automate it with appropriate safeguards.

For example, a script or workflow might collect diagnostics for a failing service, validate the target environment, execute an approved remediation, and verify recovery.

Add logging, access control, timeouts, idempotency where applicable, and a safe failure path. For risky actions, require human approval.

### Q41. How do you perform capacity planning?
Examine historical traffic, peak load, growth trends, resource utilization, dependency limits, and performance-test results.

Estimate capacity under normal and failure conditions, then validate the estimate with load testing.

For a Java microservice, evaluate CPU, heap, GC pauses, request concurrency, connection pools, database throughput, and downstream rate limits.

Capacity planning should also consider headroom and the ability to tolerate losing an instance or availability zone.

### Q42. What is a load test, stress test, and soak test?
- **Load test:** Measures performance under expected or anticipated traffic.
- **Stress test:** Pushes the system beyond expected capacity to find breaking points and failure behavior.
- **Soak test:** Runs sustained load over an extended period to expose leaks, degradation, or resource accumulation.

For senior SRE roles, explain not just how to run these tests, but what decisions the results inform.

## 8. Senior-Level Scenario-Based Questions

### Q43. CPU is at 95%, but request latency and error rates are normal. What do you do?
Determine whether CPU utilization is sustained and whether there is adequate capacity headroom.

Check:
- Request volume and traffic patterns.
- CPU utilization by process and thread.
- GC activity and allocation rate.
- Queue depth and request concurrency.
- Autoscaling status and resource limits.
- Latency and error trends.

If the service is healthy but approaching saturation, evaluate scaling or optimization before it becomes an incident. Avoid making changes based only on a single CPU reading.

### Q44. A deployment causes p99 latency to increase, but average latency remains unchanged. Why?
Average latency can hide a small number of very slow requests. A p99 increase indicates that the slowest portion of requests has degraded.

Possible causes:
- Slow database queries or connection-pool contention.
- Lock contention or GC pauses.
- A slow downstream dependency.
- Resource throttling.
- A regression affecting a particular endpoint or request type.

Compare traces and latency distributions between the old and new versions, then roll back if the impact crosses agreed thresholds and the release is the likely cause.

### Q45. Your database becomes unavailable, and multiple services begin failing. How do you respond?
1. Confirm the database incident and identify affected services.
2. Engage the database and incident-response teams.
3. Reduce load on the failing database and prevent uncontrolled retries.
4. Preserve capacity for critical operations where possible.
5. Apply approved failover or recovery procedures.
6. Check consistency, connection recovery, and application behavior after restoration.
7. Verify end-to-end functionality and monitor for a second wave of failures.

After recovery, review connection management, dependency isolation, retry behavior, database availability, and tested recovery procedures.

### Q46. A service meets its availability target but repeatedly violates its latency target. Is it reliable?
It depends on the service's reliability objectives. If latency is part of its SLO, then it is not meeting the complete reliability target.

Track availability and latency separately, understand their impact on users, and prioritize improvements based on the service's agreed objectives.

### Q47. How would you handle an alert that repeatedly fires but has no clear root cause?
First, establish whether it reflects a genuine user-impacting issue.

Correlate the alert with logs, metrics, traces, deployments, and infrastructure events. If it is noisy or poorly defined, improve the detection logic and alert context rather than simply suppressing it.

For recurring incidents, document hypotheses, evidence, and corrective actions and assign ownership for permanent fixes.

### Q48. How do you balance feature delivery against reliability work?
Use SLO performance, error-budget consumption, incident history, and business impact to inform the decision.

When reliability is healthy, teams can continue planned feature work while maintaining safeguards. When the error budget is being consumed rapidly, recommend prioritizing the highest-impact reliability improvements and reducing risky changes until the service stabilizes.

The decision should be transparent and based on measurable risk, not on an absolute rule that features or reliability always come first.

## 9. Leadership and Behavioral Questions

### Q49. Tell me about a major production incident you handled.
Use the STAR structure:
- **Situation:** Describe the service, business impact, and incident.
- **Task:** Explain your responsibility.
- **Action:** Explain the evidence you collected, how you coordinated the response, and what you did to mitigate the incident.
- **Result:** State the measured outcome and the changes made to prevent recurrence.

Prepare a genuine example involving a production outage, latency degradation, database bottleneck, or failed deployment. Include actual impact and recovery times only if you know them.

### Q50. How do you influence developers to improve reliability?
Make reliability part of the development lifecycle through:
- SLOs and measurable service health.
- Production-readiness reviews.
- Automated testing and safe deployment practices.
- Runbooks, dashboards, and clear ownership.
- Postmortems that produce actionable improvements.
- Pairing with developers on complex incidents.

Use evidence and shared goals rather than treating reliability as a separate team's responsibility.

### Q51. How do you prioritize multiple incidents?
Prioritize by user impact, business criticality, scope, safety, and time sensitivity.

Assign clear ownership, separate coordination from technical investigation for major incidents, communicate status, and avoid spreading responders across too many tasks.

Reassess priorities as new evidence emerges.

### Q52. What would you do in your first 90 days as an SRE?
**First 30 days:** Learn the architecture, service ownership, SLIs/SLOs, alerting, deployment process, incident history, and on-call procedures.

**Days 31–60:** Identify recurring incidents, noisy alerts, capacity risks, manual operational work, and gaps in observability. Deliver a small, measurable improvement.

**Days 61–90:** Implement a prioritized reliability improvement, such as automated diagnostics, safer deployment checks, better SLO alerts, or remediation of a recurring failure. Establish a plan for longer-term improvements.

The goal is to understand the environment before introducing broad changes.

## 10. Hands-on Exercises

Practise these tasks in a small Spring Boot application.

- [ ] **Diagnose a slow API:** Generate load, inspect p95/p99 latency, and trace a slow database call.
- [ ] **Investigate a memory problem:** Capture heap or GC evidence and distinguish allocation pressure from retained objects.
- [ ] **Handle a failing dependency:** Implement timeouts, bounded retries, and a circuit breaker; verify recovery behavior.
- [ ] **Create an SLO dashboard:** Measure request success, latency, traffic, and saturation; configure actionable alerts.
- [ ] **Troubleshoot Kubernetes restarts:** Inspect pod events, previous logs, resource limits, and probe behavior.
- [ ] **Simulate a failed deployment:** Deploy a deliberately faulty version in a test environment and practise rollback.

## 11. Quick-Reference Resources

- [Google SRE Book](https://sre.google/sre-book/table-of-contents/) — SRE principles, monitoring, incident response, and reliability engineering.
- [Google SRE Workbook](https://sre.google/workbook/table-of-contents/) — practical implementation of SRE concepts.
- [Prometheus documentation](https://prometheus.io/docs/introduction/overview/) — metrics and monitoring.
- [OpenTelemetry documentation](https://opentelemetry.io/docs/) — observability and distributed tracing.
- [Kubernetes documentation](https://kubernetes.io/docs/home/) — workload operations and troubleshooting.
- [Java 21 troubleshooting guide](https://docs.oracle.com/en/java/javase/21/troubleshoot/) — JVM troubleshooting.
- [Resilience4j documentation](https://resilience4j.readme.io/) — resilience patterns for Java applications.

## Final Preparation Advice

For an SRE role at your experience level, prioritize these five areas:

1. **Production troubleshooting:** Reason systematically from symptoms to evidence and mitigation.
2. **Java/JVM expertise:** Explain GC, memory, thread pools, connection pools, and latency under real workloads.
3. **SRE principles:** Understand SLOs, error budgets, incident response, toil, and postmortems.
4. **Infrastructure and observability:** Practise Linux, networking, Kubernetes, metrics, logs, and traces.
5. **Leadership:** Demonstrate that you can coordinate incidents, influence development teams, and prevent recurring failures.

Some SRE positions are software-engineering-heavy, while others emphasize infrastructure, Kubernetes, Linux, and on-call operations. Prepare for the specific mix in the job description rather than assuming every SRE role has identical expectations.
