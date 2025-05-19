## Microservices Design Patterns – Interview Cheat Sheet

### 1. Decomposition Patterns

#### 1.1 Decompose by Business Capability

* Split services based on business functions (e.g., OrderService, PaymentService).
* Aligned with DDD (Domain-Driven Design).

#### 1.2 Decompose by Subdomain

* Based on bounded contexts (Core, Supporting, Generic).
* Encourages team autonomy and modularity.

---

### 2. Integration Patterns

#### 2.1 API Gateway

* Single entry point for clients.
* Manages routing, rate limiting, authentication.
* Tools: Spring Cloud Gateway, Zuul, Kong.

#### 2.2 Backend for Frontend (BFF)

* Separate backend for each frontend (web, mobile).
* Tailors responses to frontend needs.

#### 2.3 Aggregator Pattern

* Combines results from multiple microservices.
* Reduces round-trips for the client.

#### 2.4 Chained/Orchestration Pattern

* Services call each other in a workflow.
* Central control with orchestrator or service-led chaining.

---

### 3. Database Patterns

#### 3.1 Database per Service

* Each service owns its own DB.
* Ensures service autonomy.

#### 3.2 Shared Database (Anti-Pattern)

* Multiple services share a DB.
* Leads to tight coupling and should be avoided.

#### 3.3 CQRS (Command Query Responsibility Segregation)

* Separate read and write models.
* Optimizes performance and scalability.

---

### 4. Resilience Patterns

#### 4.1 Circuit Breaker

* Prevents repeated calls to a failing service.
* Tools: Resilience4j, Hystrix (deprecated).

#### 4.2 Retry

* Automatically retries failed calls.
* Useful for transient errors.

#### 4.3 Timeout

* Aborts calls that take too long.
* Avoids resource hogging.

#### 4.4 Bulkhead

* Isolates service resources (thread pools).
* Prevents one service from affecting others.

#### 4.5 Rate Limiting

* Restricts request rate.
* Prevents overload or abuse.

---

### 5. Observability & Operations

#### 5.1 Log Aggregation

* Centralized logging using ELK, Fluentd.

#### 5.2 Distributed Tracing

* Trace requests across services.
* Tools: OpenTelemetry, Jaeger, Zipkin.

#### 5.3 Health Check

* Endpoint to report service health (e.g., /health).

---

### 6. Infrastructure Patterns

#### 6.1 Service Discovery

* Dynamic registration and lookup.
* Tools: Eureka, Consul.

#### 6.2 Sidecar

* Deploy helper services alongside primary service.
* Common in service mesh (e.g., Istio).

#### 6.3 Strangler Fig

* Gradually replace monolith parts with microservices.
* Ensures safe migration.

---

### Useful Resources

* Book: "Microservices Patterns" by Chris Richardson
* Site: [https://microservices.io/](https://microservices.io/)
* Docs: [https://spring.io/projects/spring-cloud](https://spring.io/projects/spring-cloud)
* Blog: [https://www.baeldung.com/tag/microservices/](https://www.baeldung.com/tag/microservices/)
* YouTube: Tech Primers, Java Brains

---

### TL;DR Summary

Use microservices patterns to:

* **Split services** clearly (Decomposition)
* **Enable efficient communication** (Integration)
* **Ensure autonomy & performance** (Database)
* **Improve fault tolerance** (Resilience)
* **Monitor & manage systems** (Observability)
* **Support scalability & evolution** (Infrastructure)
