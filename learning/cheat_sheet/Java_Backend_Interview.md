# Java Backend Interview Cheat Sheet

One-line memory hooks for Core Java, Spring Boot, Microservices and SQL, with the trade-off an interviewer will ask next.

## 1. Core Java

### OOP in one line each

| Concept | Memory hook |
| --- | --- |
| Encapsulation | Private fields, public getters and setters; hide the data |
| Inheritance | IS-A reuse. No multiple class inheritance; private members and constructors are not inherited |
| Polymorphism | Overloading is decided at compile time; overriding at runtime |
| Abstraction | Abstract class holds state and partial code; interface is a contract (default methods since Java 8) |
| Aggregation vs composition | Weak has-a (parts survive the owner) vs strong has-a (parts die with the owner) |
| Coupling and cohesion | Aim for low coupling (depend on interfaces) and high cohesion (one job per class) |
| Immutable class recipe | Final class, private final fields, no setters, defensive copies of mutable fields |

### Collections

| Type | Key facts to quote |
| --- | --- |
| HashMap | Bucket array; default capacity 16, load factor 0.75, doubles on resize. Bucket turns into a tree at 8 entries (table size 64 or more). One null key. Not thread-safe. O(1) average |
| LinkedHashMap | HashMap plus insertion or access order. Good base for an LRU cache |
| TreeMap / TreeSet | Red-black tree, sorted, O(log n), no null key |
| HashSet | Backed by a HashMap; elements are the keys, value is a dummy constant |
| ArrayList vs LinkedList | ArrayList: O(1) index, grows by 1.5x. LinkedList: O(1) at the ends, O(n) by index. ArrayList wins in almost every case |
| ConcurrentHashMap | Lock-free reads, CAS plus a lock on one bucket for writes, no null keys or values, weakly consistent iterator |
| Hashtable / synchronizedMap | Whole-map lock, so one thread at a time |
| CopyOnWriteArrayList | Copy on every write; best for read-heavy lists, fail-safe iterator |
| Fail-fast vs fail-safe | Fail-fast throws ConcurrentModificationException (modCount check). Fail-safe iterates a snapshot or tolerates changes |
| equals and hashCode | Equal objects must have equal hash codes; the reverse is not required. Override both or neither |

### Strings

- Strings are immutable and pooled. A literal reuses the pool; `new String("a")` always makes a new heap object. `==` compares references, `equals()` compares content.
- `intern()` returns the pooled instance.
- StringBuilder is not synchronized and is fast. StringBuffer is synchronized and slower. Use StringBuilder by default.

### Concurrency

| Topic | Memory hook |
| --- | --- |
| Thread states | NEW, RUNNABLE, BLOCKED, WAITING, TIMED\_WAITING, TERMINATED |
| start() vs run() | start() creates a new thread; run() executes on the current thread. Starting twice throws IllegalThreadStateException |
| wait() vs sleep() | wait() needs a synchronized block and releases the monitor; sleep() keeps it |
| synchronized vs ReentrantLock | ReentrantLock adds tryLock, timeouts, fairness and interruptible waits. Always unlock in finally |
| volatile | Guarantees visibility and ordering, not atomicity. Use Atomic classes for counters |
| Deadlock | Needs mutual exclusion, hold and wait, no preemption, circular wait. Break one: use a global lock order or tryLock with timeout |
| execute() vs submit() | execute() returns nothing; submit() returns a Future that carries the result or the exception |
| Thread pool sizing | CPU-bound: about the number of cores. I/O-bound: more, using Little's Law (concurrency = rate x latency) |
| CompletableFuture | Chain async steps with thenApply, thenCompose, thenCombine; handle errors with exceptionally or handle; allOf waits for all |
| Virtual threads (Java 21) | Cheap threads for blocking I/O; they do not speed up CPU work, so still cap concurrency to protect dependencies |
| Daemon thread | Does not stop the JVM from exiting; set before start() |

### JVM and memory

| Topic | Memory hook |
| --- | --- |
| Heap | Young (Eden plus two survivor spaces) and old generation. Shared by all threads |
| Metaspace | Class metadata, native memory, replaced PermGen in Java 8 |
| Stack | One per thread; one frame per method call; holds locals and references |
| Class loading | Loading, linking (verify, prepare, resolve), initialization. Loaders: Bootstrap, Platform, Application; parent delegation |
| GC | G1 is the default since Java 9. ZGC and Shenandoah for low pause. Mark and sweep: mark live objects from GC roots, then reclaim the rest |
| StackOverflowError vs OutOfMemoryError | Stack full from runaway recursion vs heap full from too many live objects or a leak |
| ClassNotFoundException vs NoClassDefFoundError | Exception on dynamic load (Class.forName) vs error when a class present at compile time is missing at runtime |
| Memory leak causes | Static collections, unbounded caches, ThreadLocal not removed, unclosed resources |

### Java 8 and later

- Functional interfaces: Predicate (test), Function (apply), Consumer (accept), Supplier (get), BiFunction, UnaryOperator.
- Streams: intermediate operations (map, filter, sorted, distinct) are lazy; a terminal operation (collect, forEach, reduce, count) triggers the pipeline. A stream is used once.
- Optional: return type for maybe-absent values; avoid it for fields and parameters.
- Newer features: var (10), switch expressions (14), text blocks (15), records (16), Stream.toList (16), sealed classes (17), virtual threads (21).

### Exceptions and patterns

- Checked exceptions must be handled or declared (external failures). Unchecked extend RuntimeException (programming mistakes). `throw` raises one; `throws` declares.
- try-with-resources closes anything that implements AutoCloseable.
- Singleton can be broken by reflection, serialization and clone(). Safest form is an enum singleton.
- Method hiding: a static method redeclared in a child is hidden, not overridden; the reference type decides which runs.

## 2. Spring Boot

### Core ideas

| Topic | Memory hook |
| --- | --- |
| IoC and DI | The container creates and wires beans; you declare dependencies. Prefer constructor injection: immutable, testable, fails fast |
| Bean scopes | singleton (default), prototype, request, session. A singleton is one per container, not one per JVM |
| Bean lifecycle | Instantiate, inject, @PostConstruct, ready, @PreDestroy |
| @Component, @Service, @Repository, @Controller | Same stereotype idea; @Repository adds exception translation, @Controller adds web mapping |
| @Bean vs @Component | @Bean is a method in a @Configuration class (you control creation, good for third-party classes). @Component is class-level scanning |
| @Primary vs @Qualifier | @Primary marks the default bean among several of one type. @Qualifier picks one at the injection point and wins over @Primary |
| Circular dependency | Avoid by redesign or events; constructor cycles fail at startup, and Boot 2.6+ forbids cycles by default |
| @Autowired on fields | Works but hides dependencies and hampers testing; use constructors |

### Spring Boot essentials

| Topic | Memory hook |
| --- | --- |
| @SpringBootApplication | @Configuration plus @EnableAutoConfiguration plus @ComponentScan |
| Auto-configuration | Boot reads the auto-configuration imports file and applies configs guarded by @ConditionalOnClass, @ConditionalOnMissingBean and @ConditionalOnProperty. Your own bean backs it off |
| Starters | Curated dependency bundles, for example spring-boot-starter-web |
| Config precedence | Command-line args, then env variables, then application-{profile}.yml, then application.yml |
| Profiles | @Profile and spring.profiles.active select environment-specific beans and config |
| @ConfigurationProperties vs @Value | Typed, validated binding of a property group vs single-value injection |
| Actuator | /health, /metrics, /info, /prometheus. Split liveness and readiness for Kubernetes |
| Embedded server | Tomcat by default; can swap to Jetty or Undertow |

### REST and web

| Topic | Memory hook |
| --- | --- |
| @RestController | @Controller plus @ResponseBody |
| HTTP verbs | GET read, POST create, PUT replace or create, PATCH partial update, DELETE remove. GET and PUT and DELETE are idempotent; POST is not |
| Status codes | 200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict, 429 Too Many Requests, 500 Server Error, 503 Unavailable |
| @PathVariable vs @RequestParam vs @RequestBody | URL segment vs query parameter vs JSON body |
| Validation | @Valid with Bean Validation annotations (@NotNull, @Size, @Email); errors surface as MethodArgumentNotValidException |
| Global error handling | @RestControllerAdvice with @ExceptionHandler returns one consistent error shape |
| Filter vs Interceptor vs AOP | Filter is servlet level (before DispatcherServlet), interceptor is Spring MVC level (pre and post handler), AOP wraps bean methods |
| Pagination | Pageable and Page; prefer keyset pagination for very large tables |
| Why POST for a read | To keep sensitive values out of URLs and access logs |

### Data, JPA and transactions

| Topic | Memory hook |
| --- | --- |
| Spring Data JPA | Repository interfaces, derived queries, @Query, specifications |
| Entity relationships | @OneToOne, @OneToMany, @ManyToOne, @ManyToMany. Use mappedBy on the non-owning side; the side with the foreign key owns it |
| Lazy vs eager | LAZY is the default for collections; LazyInitializationException appears outside a session |
| N+1 problem | One query for the parent plus N for children. Fix with JOIN FETCH, @EntityGraph or batch size |
| @Transactional | Works through a proxy, so self-invocation inside the same class bypasses it. Rolls back on unchecked exceptions by default; use rollbackFor for checked ones |
| Propagation | REQUIRED (default), REQUIRES\_NEW (suspend and start new), SUPPORTS, NOT\_SUPPORTED, MANDATORY, NEVER |
| Isolation | READ\_UNCOMMITTED, READ\_COMMITTED, REPEATABLE\_READ, SERIALIZABLE |
| Optimistic vs pessimistic locking | @Version column and retry on conflict vs SELECT FOR UPDATE |
| Open session in view | Convenient but hides lazy loading in the web layer; many teams turn it off |
| Multiple datasources | Mark one @Primary; configure separate EntityManagerFactory and transaction manager for the other |

### Security, testing and ops

- Spring Security runs a filter chain. Authentication answers who you are; authorization answers what you may do. JWT is stateless; sessions are stateful. Hash passwords with BCrypt.
- OAuth2 in one line: the resource server validates tokens issued by an authorization server.
- Testing: @SpringBootTest loads the full context; @WebMvcTest tests only the web layer with MockMvc; @DataJpaTest tests repositories; @MockBean replaces a bean; Testcontainers gives a real database.
- Caching: @EnableCaching with @Cacheable, @CacheEvict and @CachePut; set a TTL and beware of stampedes.
- Async and scheduling: @Async needs @EnableAsync and a configured executor; @Scheduled needs @EnableScheduling.
- Graceful shutdown: set server.shutdown=graceful and a shutdown phase timeout.
- Logging: use SLF4J with structured logs and a trace id in the MDC.

## 3. Microservices

### Core patterns

| Pattern | What it solves | One-line answer |
| --- | --- | --- |
| API Gateway | One entry point for clients | Routing, auth, rate limiting, aggregation (Spring Cloud Gateway) |
| Service discovery | Instances come and go | Registry (Eureka, Consul) or Kubernetes DNS and Services |
| Config server | Central, versioned config | Spring Cloud Config, or Kubernetes ConfigMaps and Secrets |
| Circuit breaker | Failing dependency drags callers down | Closed, then open on failures or slow calls, then half-open trial. Always give a fallback (Resilience4j) |
| Retry with backoff | Transient faults | Exponential backoff plus jitter, a small attempt limit, idempotent calls only |
| Bulkhead | One slow dependency eats all threads | Separate pools or semaphores per dependency |
| Timeout | Hung calls hold threads | Always set connect and read timeouts; propagate a deadline |
| Rate limiting | Overload and fairness | Token bucket per client; return 429 |
| Saga | Transactions across services | Chain of local transactions with compensating actions |
| Outbox | Write to the database and publish an event atomically | Save the event in the same transaction, then a relay publishes it |
| CQRS | Reads and writes need different models | Separate write model and read model |
| Event sourcing | Need a full audit trail | Store events, rebuild state by replay |
| Strangler fig | Migrate a monolith safely | Route features to new services one by one |
| Sidecar and service mesh | Cross-cutting network concerns | Proxy handles mTLS, retries, telemetry (Istio, Linkerd) |

### Communication and data

| Topic | Memory hook |
| --- | --- |
| Sync vs async | REST or gRPC for request and response; Kafka or RabbitMQ for events and decoupling |
| REST vs gRPC | REST is human-friendly JSON over HTTP. gRPC uses Protobuf over HTTP/2: faster, typed contracts, streaming |
| Kafka basics | Topic, partitions, consumer groups. Order is guaranteed only within a partition. Delivery is at-least-once, so consumers must be idempotent |
| Consumer lag | Scale consumers up to the partition count, tune batch size, add a retry topic and a dead-letter queue |
| Database per service | Each service owns its data; share through APIs or events, never a shared table |
| Saga styles | Choreography (events, no coordinator) vs orchestration (a coordinator drives the steps) |
| Idempotency | Idempotency key stored with the result so a retry returns the same outcome |
| CAP theorem | During a network partition choose consistency or availability. Most microservices pick availability with eventual consistency |
| Distributed ID and ordering | UUIDs or snowflake IDs; never rely on clocks across nodes |
| API versioning | URI (/v1), header or media type; keep backward compatibility, deprecate gradually |

### Resilience, observability and delivery

| Topic | Memory hook |
| --- | --- |
| Three pillars | Metrics (trends, alerts), logs (events), traces (request path). Link them with a trace id |
| Golden signals | Latency, traffic, errors, saturation |
| Distributed tracing | OpenTelemetry or Micrometer Tracing with Zipkin, Jaeger or Tempo |
| Health checks | Liveness (restart me) kept shallow; readiness (send me traffic) checks dependencies |
| Deployment strategies | Rolling, blue-green (instant rollback), canary (small share of traffic first), feature flags |
| Containers and Kubernetes | Image, pod, deployment, service, ingress, HPA, ConfigMap, Secret. Set requests and limits, and size the JVM with MaxRAMPercentage |
| 12-factor highlights | Config in environment, stateless processes, logs to stdout, disposable processes, dev and prod parity |
| Security | Validate JWT at the gateway and at each service, mTLS between services, least privilege, secrets in a vault |
| Testing | Unit, then integration with Testcontainers, then consumer-driven contract tests (Pact), then a few end-to-end tests |
| Monolith vs microservices | Start with a modular monolith; split only for independent scaling, deployment or team ownership. Microservices trade code complexity for operational complexity |

### Questions to expect

- How do you handle a distributed transaction? Saga plus outbox plus idempotent consumers.
- A downstream service is slow; what protects you? Timeout, circuit breaker, bulkhead, fallback.
- How do services find each other and stay in sync on config? Discovery plus config server or Kubernetes.
- How do you debug one request across ten services? Trace id propagated in headers and logged everywhere.

## 4. SQL

### Query order and basics

The database runs a query in this logical order: FROM and JOIN, WHERE, GROUP BY, HAVING, SELECT, DISTINCT, ORDER BY, LIMIT. That is why a SELECT alias cannot be used in WHERE.

| Topic | Memory hook |
| --- | --- |
| WHERE vs HAVING | WHERE filters rows before grouping; HAVING filters groups after aggregation |
| DELETE vs TRUNCATE vs DROP | DELETE removes chosen rows and can roll back; TRUNCATE empties the table fast; DROP removes the table itself |
| UNION vs UNION ALL | UNION removes duplicates (slower); UNION ALL keeps them |
| Primary vs unique vs foreign key | Primary: unique and not null, one per table. Unique: unique, nulls allowed. Foreign: points to a parent key |
| NULL rules | NULL = NULL is not true; use IS NULL. COUNT(\*) counts rows; COUNT(column) skips NULLs |
| Normalization | 1NF atomic values; 2NF no partial dependency on part of a key; 3NF no transitive dependency. Denormalize deliberately for read speed |
| Stored procedure vs function | Procedure performs actions and may return many results; function returns a value and can be used inside a query |

### Joins

| Join | Returns |
| --- | --- |
| INNER | Only rows that match in both tables |
| LEFT | All left rows, with matching right rows or NULL |
| RIGHT | All right rows, with matching left rows or NULL |
| FULL OUTER | All rows from both sides, NULL where there is no match |
| CROSS | Every combination (Cartesian product) |
| SELF | A table joined to itself, for example employee and manager |

Tip: to find rows with no match, LEFT JOIN and filter where the right key IS NULL.

### Indexes and performance

| Topic | Memory hook |
| --- | --- |
| Clustered vs non-clustered | Clustered decides the physical row order (one per table, usually the primary key). Non-clustered is a separate structure pointing to rows |
| B-tree index | Good for equality, range and ORDER BY. Lookups are O(log n) |
| Composite index | Leftmost-prefix rule: index (a, b, c) serves a, a+b and a+b+c, not b alone |
| Covering index | The index holds every column the query needs, so no table lookup |
| Index killers | Function on the column (WHERE LOWER(name) = ...), leading wildcard (LIKE '%abc'), type mismatch, low-selectivity columns |
| Cost of indexes | Faster reads, slower writes, more storage. Do not index everything |
| EXPLAIN | Read the plan: full table scan vs index scan, row estimates, join order |
| Slow query checklist | Check the plan, add or fix the index, select only needed columns, avoid N+1, paginate with keyset, cache hot reads |
| Pagination | OFFSET gets slower with depth; keyset (WHERE id > last\_id ORDER BY id LIMIT n) stays fast |
| Scaling the database | Read replicas for reads, partitioning for big tables, sharding for write scale (adds complexity) |
| SQL vs NoSQL | SQL for relations and strong consistency; NoSQL for flexible schema and horizontal scale; pick by access pattern |

### Transactions and isolation

ACID: Atomicity (all or nothing), Consistency (rules always hold), Isolation (concurrent transactions do not interfere), Durability (committed data survives a crash).

| Isolation level | Dirty read | Non-repeatable read | Phantom read |
| --- | --- | --- | --- |
| READ\_UNCOMMITTED | Possible | Possible | Possible |
| READ\_COMMITTED | Prevented | Possible | Possible |
| REPEATABLE\_READ | Prevented | Prevented | Possible |
| SERIALIZABLE | Prevented | Prevented | Prevented |

Deadlock: two transactions hold locks the other needs. Reduce it by locking in a consistent order and keeping transactions short.

### Window functions and CTEs

- ROW\_NUMBER gives unique ranks; RANK leaves gaps after ties; DENSE\_RANK leaves no gaps.
- PARTITION BY restarts the calculation per group; LAG and LEAD read the previous or next row; SUM() OVER gives running totals.
- A CTE (WITH clause) names a subquery for readability; a recursive CTE walks hierarchies.

### Classic interview queries

```sql
-- Second highest salary
SELECT MAX(salary) FROM employee
WHERE salary < (SELECT MAX(salary) FROM employee);

-- Nth highest salary (N = 3)
SELECT salary FROM (
  SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS rnk
  FROM employee) t
WHERE rnk = 3;

-- Highest salary per department
SELECT * FROM (
  SELECT e.*, ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rn
  FROM employee e) t
WHERE rn = 1;

-- Find duplicate emails
SELECT email, COUNT(*) FROM users
GROUP BY email HAVING COUNT(*) > 1;

-- Employees earning more than their manager (self join)
SELECT e.name FROM employee e
JOIN employee m ON e.manager_id = m.id
WHERE e.salary > m.salary;

-- Departments with no employees
SELECT d.name FROM department d
LEFT JOIN employee e ON e.dept_id = d.id
WHERE e.id IS NULL;
```

## 5. Answer template and last-minute checklist

### D-E-P-T-H: the template for every answer (30 to 90 seconds)

| Step | Say this | Time |
| --- | --- | --- |
| Define | One plain sentence that defines the term | 5 s |
| Explain why | The problem it solves or why it exists | 5 to 10 s |
| Practical example | A real use from your project, with a number if you have one | 10 to 15 s |
| Trade-offs | Pros, cons, and when not to use it | 5 to 10 s |
| Hand off | Stop and invite a follow-up | 2 s |

Other question types:

- Difference between A and B: one line each, two or three key differences, then when you would pick each.
- Design a system: requirements, scale numbers, components, bottlenecks and failures, trade-offs.
- Debug a problem: confirm impact, check what changed, narrow down with metrics, logs and traces, mitigate first, then root cause and prevention.
- Behavioural: situation, task, action (say what you did), result with numbers, what you learned.
- Not sure: say what you know, reason aloud, and say how you would find out. Do not bluff.

### Traps interviewers set

| Trap | Safe answer |
| --- | --- |
| Check-then-put on a ConcurrentHashMap | Use putIfAbsent, computeIfAbsent or merge; two separate calls are not atomic |
| @Transactional on a method called from the same class | Proxy is bypassed, so no transaction; move it to another bean |
| Overriding equals without hashCode | Equal objects land in different buckets; override both |
| Liveness probe that checks the database | A database blip restarts every pod; keep liveness shallow |
| Retries at every layer | Retry storm multiplies load; retry at one layer with backoff, jitter and a budget |
| Calling run() instead of start() | Runs on the caller's thread; no new thread |
| Using OFFSET for deep pages | Slows down with depth; use keyset pagination |
| Function on an indexed column | The index is skipped; rewrite the predicate |

### Before the interview

- [ ] Write one project story per topic (cache, Kafka, slow query, incident) with a real number
- [ ] Rehearse five answers aloud using D-E-P-T-H, timing each
- [ ] Re-read the trap table above
- [ ] Prepare two questions to ask the interviewer
- [ ] Sleep well; calm and structured beats rushed and complete

### Where to go deeper

| Resource | Best for |
| --- | --- |
| Effective Java (Joshua Bloch) | Core Java design and pitfalls |
| Java Concurrency in Practice (Brian Goetz) | Threads, memory model, locks |
| Spring Framework and Spring Boot reference docs (docs.spring.io) | Authoritative answers on DI, auto-configuration, transactions |
| Baeldung (baeldung.com) | Short, practical Spring and Java tutorials |
| Building Microservices (Sam Newman) | Splitting services, data, deployment |
| Release It! (Michael Nygard) | Circuit breaker, bulkhead, timeouts |
| Designing Data-Intensive Applications (Martin Kleppmann) | Replication, consistency, Kafka, storage |
| Use The Index, Luke (use-the-index-luke.com) | How SQL indexes really work |
| Mode SQL tutorial and LeetCode SQL problems | SQL practice |
