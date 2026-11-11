# 📘 Core Java Design Patterns

**Audience:** Senior Java Developer · Technical Lead · Solution Architect
**Focus:** GoF patterns as used in enterprise / BFSI Java systems, the JDK, and Spring
**Java level:** Java 17+ (records, switch expressions); notes where older versions differ

Design patterns are named, proven solutions to recurring design problems. At senior level, interviewers do not want definitions. They want to hear *which pattern you chose, which alternatives you rejected, and what it cost*.

---

## 📖 What are Design Patterns?

**Definition:** A design pattern is a reusable, language-independent template for solving a common design problem in a given context. The classic catalogue is the 23 patterns from *Design Patterns: Elements of Reusable Object-Oriented Software* (Gamma, Helm, Johnson, Vlissides, the "Gang of Four").

**Why they exist:** Experienced engineers kept reinventing the same structures: how to create objects flexibly, how to compose them, how to vary behaviour. Patterns give those structures names and known trade-offs.

**Problem they solve:**
- Tight coupling between classes that change at different rates
- Large `if/else` and `switch` blocks that grow with every new requirement
- Object creation logic scattered across the codebase
- A shared vocabulary gap in code reviews ("use a Strategy here")

**Key characteristics:**

| Characteristic | Meaning |
|---|---|
| Intent | The problem the pattern solves, more important than the class diagram |
| Participants | The roles (e.g. Context, Strategy, ConcreteStrategy) |
| Consequences | Benefits *and* costs; no pattern is free |
| Context-dependent | Often a language feature (lambda, enum, record) replaces the heavy form |

**The three categories**

| Category | Concern | Patterns covered in this handbook |
|---|---|---|
| **Creational** | How objects are created | Singleton, Factory Method, Abstract Factory, Builder, Prototype |
| **Structural** | How objects are composed | Adapter, Decorator, Proxy, Facade, Composite |
| **Behavioral** | How objects interact and share responsibility | Strategy, Observer, Template Method, Chain of Responsibility, Command, State |

> **Key Takeaway:** A pattern is a *named trade-off*, not a template to copy. Knowing when **not** to use one is the senior-level signal.

---

## ❓ Why Do We Need Them?

**Business problem:** Banking requirements change constantly: a new payment channel, a new interest rule, a new regulatory check. Code that needs edits in ten places for each change is slow and risky.

**Technical problem:** Without structure, systems become rigid (hard to change), fragile (changes break unrelated code), and immobile (hard to reuse).

| Smell in code | Pattern that helps |
|---|---|
| `switch (type)` creating objects in many places | Factory Method / Abstract Factory |
| Constructor with 9 parameters, half optional | Builder |
| `if (type == X) ... else if (type == Y)` for behaviour | Strategy / State |
| Third-party API with the wrong interface | Adapter |
| Cross-cutting logging, security, transactions | Proxy / Decorator |
| Many subsystems called in a fixed order by every caller | Facade |
| Many components must react to one event | Observer |
| Same algorithm skeleton, different steps | Template Method |
| Sequential validations or approvals | Chain of Responsibility |

**Link to SOLID:**

| Principle | Patterns that support it |
|---|---|
| Open/Closed | Strategy, Factory, Decorator, Observer |
| Single Responsibility | Facade, Chain of Responsibility, Command |
| Liskov Substitution | Template Method, Decorator (must honour contracts) |
| Interface Segregation | Adapter, Facade |
| Dependency Inversion | Factory, Strategy, Abstract Factory |

> **Key Takeaway:** Patterns are most valuable when they remove a *recurring pain* (rigid conditionals, scattered creation), not when applied up front "just in case."

---

## ⚙️ Internal Working

### How to choose and apply a pattern

```
+------------------------+
| 1. Identify the pain   |  rigid if/else, scattered new, tight coupling
+-----------+------------+
            |
            v
+------------------------+
| 2. Find what varies    |  algorithm? object type? interface? event consumers?
+-----------+------------+
            |
            v
+------------------------+
| 3. Encapsulate it      |  behind an interface / abstract class
+-----------+------------+
            |
            v
+------------------------+
| 4. Program to the      |  callers depend on the abstraction
|    abstraction         |
+-----------+------------+
            |
            v
+------------------------+
| 5. Check the cost      |  extra classes? indirection? thread-safety?
+------------------------+
```

### Core principle behind most patterns

> **"Identify what varies and separate it from what stays the same."** Favour **composition over inheritance**. Program to **interfaces**, not implementations.

### Quick selection guide

| You need to... | Reach for |
|---|---|
| Control the number of instances | Singleton (or just a Spring singleton bean) |
| Let subclasses/config decide which class to create | Factory Method |
| Create families of related objects | Abstract Factory |
| Build a complex immutable object step by step | Builder |
| Copy an expensive-to-create object | Prototype |
| Make an incompatible class fit your interface | Adapter |
| Add behaviour dynamically without subclassing | Decorator |
| Control access / add cross-cutting concerns | Proxy |
| Simplify a complex subsystem | Facade |
| Swap algorithms at runtime | Strategy |
| Notify many dependents of a change | Observer |
| Fix an algorithm skeleton, vary steps | Template Method |
| Pass a request along a series of handlers | Chain of Responsibility |

> **Key Takeaway:** Start from the *variation point*, not from the pattern catalogue.

---

## 🏗 Architecture / Internal Components

| Pattern | Participants | Responsibility |
|---|---|---|
| **Singleton** | Single class with private constructor | Guarantee one instance and a global access point |
| **Factory Method** | Product, Creator | Creator defers *which* Product to instantiate |
| **Abstract Factory** | AbstractFactory, ConcreteFactory, Products | Create related object families consistently |
| **Builder** | Builder, Product (often Director) | Separate construction from representation; validate on `build()` |
| **Prototype** | Prototype with copy operation | Create new objects by copying a template |
| **Adapter** | Target, Adaptee, Adapter | Translate one interface into another |
| **Decorator** | Component, ConcreteComponent, Decorator | Wrap an object to add behaviour, same interface |
| **Proxy** | Subject, RealSubject, Proxy | Stand-in that controls access (lazy, remote, security, logging) |
| **Facade** | Facade, subsystem classes | One simple entry point to a complex subsystem |
| **Composite** | Component, Leaf, Composite | Treat single objects and trees uniformly |
| **Strategy** | Context, Strategy, ConcreteStrategy | Interchangeable algorithms behind one interface |
| **Observer** | Subject, Observer | One-to-many notification of state changes |
| **Template Method** | AbstractClass, ConcreteClass | Skeleton in a `final` method, steps overridden |
| **Chain of Responsibility** | Handler, ConcreteHandlers | Each handler processes or passes along |
| **Command** | Command, Invoker, Receiver | Encapsulate a request as an object (queue, undo, audit) |
| **State** | Context, State | Behaviour changes with internal state |

### Where you already use them (JDK and Spring)

| Pattern | JDK example | Spring example |
|---|---|---|
| Singleton | `Runtime.getRuntime()` | Default bean scope (one per container) |
| Factory Method | `List.of()`, `Calendar.getInstance()`, `Optional.of()` | `BeanFactory`, `FactoryBean` |
| Builder | `StringBuilder`, `Stream.builder()`, `HttpRequest.newBuilder()` | `UriComponentsBuilder`, `ResponseEntity.ok().body()` |
| Prototype | `Object.clone()` | `@Scope("prototype")` beans |
| Adapter | `Arrays.asList()`, `InputStreamReader` | `HandlerAdapter`, `HttpMessageConverter` |
| Decorator | `BufferedReader`, `Collections.unmodifiableList()` | `HttpServletRequestWrapper`, `BeanPostProcessor` wrappers |
| Proxy | `java.lang.reflect.Proxy` | AOP: `@Transactional`, `@Cacheable`, `@Async` |
| Facade | `javax.faces` / `java.net.URL` | `JdbcTemplate`, `RestClient` |
| Strategy | `Comparator`, `ThreadPoolExecutor` rejection policies | `PasswordEncoder`, `ResourceLoader` |
| Observer | `PropertyChangeListener`, `Flow` | `ApplicationEventPublisher` + `@EventListener` |
| Template Method | `AbstractList`, `InputStream.read()` | `JdbcTemplate`, `RestTemplate` callbacks |
| Chain of Responsibility | Servlet `FilterChain` | Spring Security `SecurityFilterChain`, `HandlerInterceptor` |

> **Key Takeaway:** Every Java developer already uses a dozen patterns daily. Interviewers love hearing JDK and Spring examples.

---

## 🔄 Visual Flow

### Strategy (varies the algorithm)

```
+----------------------+         +-------------------------+
| InterestCalculator   |-------->| <<interface>>           |
| (Context)            |  has-a  | InterestStrategy        |
| - strategy           |         | + interest(p, months)   |
+----------------------+         +------------+------------+
                                              ^
                              +---------------+---------------+
                              |                               |
                  +-----------+-----------+       +-----------+-----------+
                  | SimpleInterest        |       | CompoundInterest      |
                  +-----------------------+       +-----------------------+
```

### Decorator (stacks behaviour, same interface)

```
 caller --> [ AuditDecorator ] --> [ SmsDecorator ] --> [ EmailNotifier ]
                 |                      |                     |
          logs the call          sends SMS also        sends email (core)
          then delegates         then delegates
```

### Proxy (controls access to the real object)

```
 +--------+      +------------------+      +----------------+
 | Client |----->| Proxy            |----->| Real service   |
 +--------+      | - security check |      | (account svc)  |
                 | - timing / log   |      +----------------+
                 | - transaction    |
                 +------------------+
```

### Chain of Responsibility (validation pipeline)

```
 request --> [ AmountValidator ] --> [ KycValidator ] --> [ FraudValidator ] --> accepted
                  |                       |                      |
               reject                  reject                 reject
```

### Observer (one event, many reactions)

```
                     +-------------------+
                     | TransferService   |  publishes TransferCompleted
                     +---------+---------+
                               |
              +----------------+-----------------+
              v                v                 v
      +-------------+  +--------------+  +---------------+
      | SMS alert   |  | Audit logger |  | Rewards engine|
      +-------------+  +--------------+  +---------------+
```

---

## 💻 Code Examples

### 1. Singleton: three safe implementations

```java
// A. Enum singleton: simplest, thread-safe, safe against reflection and serialization attacks
public enum AppConfig {
    INSTANCE;

    private final Map<String, String> settings = new ConcurrentHashMap<>();

    public String get(String key) { return settings.get(key); }
    public void put(String key, String value) { settings.put(key, value); }
}
// Usage: AppConfig.INSTANCE.get("region");

// B. Initialization-on-demand holder (Bill Pugh): lazy and thread-safe without synchronization
public final class ConnectionRegistry {
    private ConnectionRegistry() { }                       // block external construction

    private static final class Holder {                    // loaded only on first access
        static final ConnectionRegistry INSTANCE = new ConnectionRegistry();
    }

    public static ConnectionRegistry getInstance() { return Holder.INSTANCE; }
}

// C. Double-checked locking: 'volatile' is mandatory, otherwise another thread may see a
//    partially constructed object (instruction reordering)
public final class RateLimiter {
    private static volatile RateLimiter instance;
    private RateLimiter() { }

    public static RateLimiter getInstance() {
        RateLimiter local = instance;                       // one volatile read in the fast path
        if (local == null) {
            synchronized (RateLimiter.class) {
                local = instance;
                if (local == null) {
                    instance = local = new RateLimiter();
                }
            }
        }
        return local;
    }
}
```

### 2. Factory Method and Spring-style registry

```java
public record PaymentRequest(String id, BigDecimal amount) { }
public record PaymentResult(String id, boolean success) { }

public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}

class UpiProcessor implements PaymentProcessor {
    public PaymentResult process(PaymentRequest r) { return new PaymentResult(r.id(), true); }
}
class CardProcessor implements PaymentProcessor {
    public PaymentResult process(PaymentRequest r) { return new PaymentResult(r.id(), true); }
}

public enum PaymentType { UPI, CARD }

public final class PaymentProcessorFactory {
    private PaymentProcessorFactory() { }

    // Switch expression over an enum is exhaustive: adding a type forces a compile error here
    public static PaymentProcessor create(PaymentType type) {
        return switch (type) {
            case UPI  -> new UpiProcessor();
            case CARD -> new CardProcessor();
        };
    }
}
```

```java
// Spring way: no switch at all. New processor = new @Component, zero changes to the router.
@Component("UPI")
class SpringUpiProcessor implements PaymentProcessor { /* ... */ }

@Component("CARD")
class SpringCardProcessor implements PaymentProcessor { /* ... */ }

@Service
class PaymentRouter {
    private final Map<String, PaymentProcessor> processors;   // Spring injects all beans, keyed by bean name

    PaymentRouter(Map<String, PaymentProcessor> processors) { this.processors = processors; }

    PaymentResult pay(String type, PaymentRequest request) {
        PaymentProcessor p = processors.get(type);
        if (p == null) throw new IllegalArgumentException("Unsupported payment type: " + type);
        return p.process(request);
    }
}
```

### 3. Builder: immutable object with validation

```java
public final class LoanApplication {
    private final String applicantId;     // required
    private final BigDecimal amount;      // required
    private final int tenureMonths;       // required
    private final String purpose;         // optional
    private final boolean insured;        // optional

    private LoanApplication(Builder b) {
        this.applicantId = b.applicantId;
        this.amount = b.amount;
        this.tenureMonths = b.tenureMonths;
        this.purpose = b.purpose;
        this.insured = b.insured;
    }

    // Required fields go in the factory method so they cannot be forgotten
    public static Builder builder(String applicantId, BigDecimal amount, int tenureMonths) {
        return new Builder(applicantId, amount, tenureMonths);
    }

    public static final class Builder {
        private final String applicantId;
        private final BigDecimal amount;
        private final int tenureMonths;
        private String purpose = "GENERAL";   // sensible defaults for optional fields
        private boolean insured;

        private Builder(String applicantId, BigDecimal amount, int tenureMonths) {
            this.applicantId = applicantId;
            this.amount = amount;
            this.tenureMonths = tenureMonths;
        }

        public Builder purpose(String purpose) { this.purpose = purpose; return this; }
        public Builder insured(boolean insured) { this.insured = insured; return this; }

        public LoanApplication build() {
            // Validate once, at the single point where the object is created
            if (amount == null || amount.signum() <= 0)
                throw new IllegalStateException("amount must be positive");
            if (tenureMonths < 6 || tenureMonths > 360)
                throw new IllegalStateException("tenure must be 6-360 months");
            return new LoanApplication(this);
        }
    }
    // getters omitted
}

// Usage: readable and impossible to mix up arguments
LoanApplication app = LoanApplication.builder("CUST-1", new BigDecimal("500000"), 60)
        .purpose("HOME")
        .insured(true)
        .build();
```

### 4. Strategy with lambdas

```java
@FunctionalInterface
public interface InterestStrategy {
    BigDecimal interest(BigDecimal principal, int months);
}

public final class InterestStrategies {
    private static final BigDecimal TWELVE_HUNDRED = BigDecimal.valueOf(1200);

    // Simple interest: P * rate * months / 1200 (rate is annual percent)
    public static InterestStrategy simple(BigDecimal annualRatePercent) {
        return (p, m) -> p.multiply(annualRatePercent)
                .multiply(BigDecimal.valueOf(m))
                .divide(TWELVE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    // Monthly compounding: P * ((1 + r/1200)^m - 1)
    public static InterestStrategy compound(BigDecimal annualRatePercent) {
        return (p, m) -> {
            BigDecimal monthly = annualRatePercent.divide(TWELVE_HUNDRED, 10, RoundingMode.HALF_UP);
            BigDecimal factor = BigDecimal.ONE.add(monthly).pow(m);
            return p.multiply(factor.subtract(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP);
        };
    }
}

public class InterestCalculator {
    private final InterestStrategy strategy;                       // chosen at runtime

    public InterestCalculator(InterestStrategy strategy) { this.strategy = strategy; }

    public BigDecimal calculate(BigDecimal principal, int months) {
        return strategy.interest(principal, months);
    }
}

// Usage: no if/else, new rule = new strategy
var simple = new InterestCalculator(InterestStrategies.simple(new BigDecimal("9.5")));
var compound = new InterestCalculator(InterestStrategies.compound(new BigDecimal("9.5")));
```

### 5. Decorator

```java
public interface Notifier { void send(String message); }

public class EmailNotifier implements Notifier {
    public void send(String message) { System.out.println("EMAIL: " + message); }
}

// Base decorator holds the wrapped object and delegates by default
public abstract class NotifierDecorator implements Notifier {
    protected final Notifier wrapped;
    protected NotifierDecorator(Notifier wrapped) { this.wrapped = wrapped; }
}

public class SmsDecorator extends NotifierDecorator {
    public SmsDecorator(Notifier wrapped) { super(wrapped); }
    public void send(String message) {
        wrapped.send(message);                                   // keep existing behaviour
        System.out.println("SMS: " + message);                   // add new behaviour
    }
}

public class AuditDecorator extends NotifierDecorator {
    public AuditDecorator(Notifier wrapped) { super(wrapped); }
    public void send(String message) {
        System.out.println("AUDIT: sending notification");
        wrapped.send(message);
    }
}

// Behaviour is composed at runtime, in any combination or order
Notifier notifier = new AuditDecorator(new SmsDecorator(new EmailNotifier()));
notifier.send("Debit of INR 5,000 on your account");
```

### 6. Proxy: JDK dynamic proxy (the engine behind Spring AOP)

```java
public interface AccountService { BigDecimal balance(String accountId); }

public class TimingProxy implements InvocationHandler {
    private final Object target;
    private TimingProxy(Object target) { this.target = target; }

    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target, Class<T> iface) {
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(), new Class<?>[] { iface }, new TimingProxy(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.nanoTime();
        try {
            return method.invoke(target, args);                  // delegate to the real object
        } catch (InvocationTargetException e) {
            throw e.getTargetException();                        // unwrap so callers see the real exception
        } finally {
            long micros = (System.nanoTime() - start) / 1_000;
            System.out.println(method.getName() + " took " + micros + " us");
        }
    }
}

AccountService service = TimingProxy.wrap(new RealAccountService(), AccountService.class);
```

### 7. Adapter and Facade

```java
// Adapter: legacy SOAP client wrapped to fit our modern interface
public interface PaymentGateway { boolean charge(String account, BigDecimal amount); }

public class LegacySoapClient {                                  // cannot be changed
    public String submitXml(String xml) { return "<status>OK</status>"; }
}

public class SoapGatewayAdapter implements PaymentGateway {
    private final LegacySoapClient client;
    public SoapGatewayAdapter(LegacySoapClient client) { this.client = client; }

    public boolean charge(String account, BigDecimal amount) {
        // Translate our call into the legacy format (escape values properly in real code)
        String xml = "<charge account=\"" + account + "\" amount=\"" + amount + "\"/>";
        return client.submitXml(xml).contains("OK");
    }
}

// Facade: one call hides four collaborating services
@Service
public class LoanFacade {
    private final KycService kyc;
    private final CreditBureauService bureau;
    private final EligibilityService eligibility;
    private final DisbursementService disbursement;

    // constructor injection omitted for brevity

    public LoanDecision apply(LoanApplication app) {
        kyc.verify(app.applicantId());
        int score = bureau.score(app.applicantId());
        if (!eligibility.isEligible(app, score)) return LoanDecision.rejected("Low score");
        return disbursement.schedule(app);
    }
}
```

### 8. Template Method and Chain of Responsibility

```java
// Template Method: skeleton is final, steps vary
public abstract class ReportGenerator {

    public final String generate() {                             // 'final' protects the algorithm order
        List<String> raw = fetchData();
        List<String> cleaned = clean(raw);
        return format(cleaned);
    }

    protected abstract List<String> fetchData();                 // must override
    protected abstract String format(List<String> rows);         // must override
    protected List<String> clean(List<String> rows) { return rows; }   // optional hook
}

// Chain of Responsibility: each validator checks and passes the request on
public abstract class TxnValidator {
    private TxnValidator next;

    public TxnValidator linkWith(TxnValidator next) { this.next = next; return next; }

    public final void validate(Txn txn) {
        check(txn);                                              // throws on failure
        if (next != null) next.validate(txn);
    }
    protected abstract void check(Txn txn);
}

TxnValidator chain = new AmountValidator();
chain.linkWith(new KycValidator()).linkWith(new FraudValidator());
chain.validate(txn);                                             // new rule = new link, no edits elsewhere
```

### 9. Observer in Spring Boot

```java
public record TransferCompleted(String txnId, String account, BigDecimal amount) { }

@Service
public class TransferService {
    private final ApplicationEventPublisher publisher;
    public TransferService(ApplicationEventPublisher publisher) { this.publisher = publisher; }

    @Transactional
    public void transfer(String from, String to, BigDecimal amount) {
        // ... debit and credit ...
        publisher.publishEvent(new TransferCompleted("TXN-1", from, amount));   // publisher knows no listeners
    }
}

@Component
class SmsAlertListener {
    // Runs only after the DB transaction commits, so we never alert on a rolled-back transfer
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void on(TransferCompleted event) { /* send SMS */ }
}

@Component
class AuditListener {
    @EventListener
    void on(TransferCompleted event) { /* write audit record */ }
}
```

> **Note:** `java.util.Observable` and `Observer` are deprecated since Java 9. Use `PropertyChangeListener`, `java.util.concurrent.Flow`, or Spring events.

> **Key Takeaway:** Prefer lambdas and Spring wiring over heavy class hierarchies. Strategy becomes a lambda, Factory becomes a bean map, Observer becomes an event.

---

## 🏦 Real Production Example

**Scenario: loan origination platform**

```
                       +----------------------+
 REST request -------->| LoanController       |
                       +----------+-----------+
                                  |
                                  v
                       +----------------------+
                       | LoanFacade           |   FACADE: one entry point
                       +----------+-----------+
                                  |
        +-------------------------+--------------------------+
        v                         v                          v
+----------------+     +--------------------+      +-------------------+
| Validation     |     | InterestStrategy   |      | PaymentProcessor  |
| CHAIN          |     | STRATEGY per       |      | FACTORY (bean map)|
| (amount, KYC,  |     | product type       |      | UPI / NEFT / IMPS |
|  fraud, limits)|     +--------------------+      +-------------------+
+----------------+
                                  |
                                  v
                       +----------------------+
                       | publish LoanApproved |   OBSERVER
                       +----------+-----------+
              +-------------------+-------------------+
              v                   v                   v
        SMS listener        Audit listener      CRM sync listener

 Cross-cutting (PROXY via Spring AOP): @Transactional, @Cacheable, security, metrics
 External bureau API wrapped by an ADAPTER so the domain never sees vendor formats
 LoanApplication created with a BUILDER (validated, immutable)
```

| Requirement change | How the patterns absorb it |
|---|---|
| New repayment product with a new interest rule | Add one `InterestStrategy` |
| New payment rail | Add one `@Component` implementing `PaymentProcessor` |
| New compliance check | Add one validator to the chain |
| Switch credit-bureau vendor | Write a new adapter, domain untouched |
| New downstream system needs approval events | Add one `@EventListener` |

> **Key Takeaway:** In a real system patterns combine. Each one isolates one *axis of change*, so a requirement touches one class instead of many.

---

## ⚠ Common Mistakes

| Wrong approach | Problem | Correct approach |
|---|---|---|
| Hand-written Singleton in a Spring app | Duplicates the container's job, hard to test and mock | Use a normal singleton-scoped bean with constructor injection |
| Double-checked locking without `volatile` | Another thread can see a half-built object | Add `volatile`, or use the holder or enum form |
| Singleton holding mutable shared state | Hidden coupling, race conditions, test pollution | Keep singletons stateless or use thread-safe structures |
| Large `switch` creating objects in many places | Every new type edits many files | Factory or a Spring bean map keyed by type |
| Builder with no validation in `build()` | Invalid objects escape | Validate once in `build()`, make the product immutable |
| Subclassing to add features | Class explosion (`SmsEmailAuditNotifier`...) | Decorator composition |
| Calling a `@Transactional` method from the same class | Self-invocation bypasses the Spring proxy, no transaction applied | Call through another bean, or restructure |
| Observers never unsubscribed | Memory leak: subject keeps listeners alive | Unsubscribe in lifecycle hooks, use weak references, or Spring-managed listeners |
| Throwing exceptions in listeners silently | Other observers or the publisher are affected | Isolate listener failures, log, and make async where appropriate |
| Using `Observable` | Deprecated since Java 9 | Spring events, `PropertyChangeListener`, `Flow` |
| Pattern for its own sake | Needless indirection, harder onboarding | Apply when there is real variation or duplication (rule of three) |

```
Wrong:   switch(type) { case "UPI": new Upi(); case "CARD": new Card(); } in 12 classes
   |
   v
Impact:  adding NEFT touches 12 files, one is missed, production bug
   |
   v
Right:   Map<String, PaymentProcessor> injected by Spring; new type = one new @Component
```

> **Key Takeaway:** Most pattern bugs come from *misusing* them: shared mutable singletons, leaking observers, proxy self-invocation.

---

## ✅ Best Practices

**Design**
- Prefer composition over inheritance; program to interfaces
- Apply the *rule of three*: introduce a pattern when the same variation has appeared repeatedly
- Name classes after the pattern role when it helps (`...Strategy`, `...Factory`, `...Adapter`)

**Coding standards**
- Make products immutable (records, `final` fields); use Builder when there are many optional fields
- Use `final` on Template Method skeletons
- Keep decorators and proxies transparent: honour the interface contract fully
- Prefer enum or holder Singleton over naive lazy versions

**Thread safety**
- A Singleton shared across threads must be stateless or use concurrent structures
- Use `CopyOnWriteArrayList` for listener lists that are read far more than written
- Remember that Spring beans are shared singletons: no per-request state in fields

**Performance and scalability**
- Dynamic proxies add a small call overhead; avoid them in the tightest loops
- Cache expensive Prototype templates, copy on demand
- Use Strategy as a stateless lambda so one instance is shared safely

**Testing and monitoring**
- Strategy, Factory, and Adapter make mocking trivial: inject test doubles
- Log which strategy or processor was selected (with the request ID)
- Alert on listener failures and slow chain steps

> **Key Takeaway:** Good pattern usage looks boring: small interfaces, immutable objects, stateless collaborators.

---

## ⚡ Performance Considerations

| Area | Consideration | Recommendation |
|---|---|---|
| CPU | Extra method calls through decorators and proxies | Negligible for business code; JIT inlines hot paths. Avoid deep decorator stacks in hot loops |
| Memory | Many small strategy, decorator, or listener objects | Share stateless instances; avoid per-request object graphs |
| Singleton init | Eager creation slows startup | Lazy holder idiom for heavy resources |
| Locking | Synchronized `getInstance()` on every call | Holder or enum form removes the lock from the fast path |
| Reflection | JDK proxies use reflection per call | Fine for service layer; CGLIB or direct code for ultra-hot paths |
| Prototype | Deep copy can cost more than construction | Measure; consider immutable objects instead of copying |
| Observer | Synchronous listeners lengthen the publisher's latency | Use async execution or a message broker for slow consumers |
| Chain | Long chains run sequentially | Order cheap checks first, fail fast |
| Scalability | Global mutable Singleton does not scale across JVMs | Use external state (DB, Redis) for cluster-wide uniqueness |

> **Key Takeaway:** Pattern overhead is rarely the bottleneck. Misplaced locks and synchronous listeners are.

---

## ⚖ Trade-offs

| Advantages | Disadvantages |
|---|---|
| Flexibility: add behaviour without editing existing code | More classes and indirection |
| Shared vocabulary in teams and reviews | Can be over-applied ("pattern fever") |
| Testability through interfaces | Harder debugging through proxies and decorator layers |
| Isolates change to small units | Learning curve for newcomers |

**When to use:** recurring variation, scattered creation logic, cross-cutting concerns, many consumers of one event.
**When NOT to use:** one-off code with no expected variation, simple CRUD, or when a plain method, lambda, or enum is clearer.

**Real-life analogy:** Patterns are like standard building details in architecture: a load-bearing wall or a fire exit. They solve known problems reliably, but you do not add a fire exit to a garden shed.

---

## 📊 Comparison Table

### The GoF patterns at a glance

| Pattern | Category | Intent | Best use case |
|---|---|---|---|
| Singleton | Creational | One instance, global access | Config, registries (prefer DI-managed) |
| Factory Method | Creational | Subclass or config decides the class | Payment channels, parsers |
| Abstract Factory | Creational | Families of related objects | UI toolkits, per-region components |
| Builder | Creational | Stepwise construction of complex objects | Many optional fields, immutable DTOs |
| Prototype | Creational | Copy an existing object | Costly-to-build templates |
| Adapter | Structural | Convert one interface to another | Legacy or vendor integration |
| Decorator | Structural | Add behaviour dynamically | Layered notifications, I/O streams |
| Proxy | Structural | Control access to an object | AOP, lazy loading, security |
| Facade | Structural | Simplify a subsystem | Orchestration entry points |
| Composite | Structural | Tree of uniform objects | Permissions, product bundles, menus |
| Strategy | Behavioral | Swap algorithms | Pricing, interest, routing rules |
| Observer | Behavioral | Notify dependents | Domain events |
| Template Method | Behavioral | Fixed skeleton, variable steps | Batch jobs, report pipelines |
| Chain of Responsibility | Behavioral | Pass along a handler chain | Validation, filters, approvals |
| Command | Behavioral | Request as an object | Queues, undo, audit trails |
| State | Behavioral | Behaviour depends on state | Order, loan, or payment lifecycles |

### Patterns interviewers ask you to tell apart

| | Adapter | Decorator | Proxy | Facade |
|---|---|---|---|---|
| Interface | Changes it | Same | Same | New, simpler |
| Purpose | Make incompatible things work | Add responsibilities | Control access | Hide complexity |
| Wraps | One object | One object (stackable) | One object | Many objects |

| | Strategy | State | Template Method |
|---|---|---|---|
| Varies | Algorithm, chosen by client | Behaviour, changed by internal state | Steps of a fixed algorithm |
| Mechanism | Composition | Composition | Inheritance |
| Who switches | Caller | The object itself | Fixed at class definition |

| | Factory Method | Abstract Factory | Builder |
|---|---|---|---|
| Creates | One product | Family of products | One complex product, step by step |
| Focus | *Which* class | Consistent *set* of classes | *How* it is assembled |

---

## 🎯 Interview Questions

**Q1. Name the ways to implement a Singleton and which you prefer.**
*Expected:* Eager static field, synchronized lazy, double-checked locking with `volatile`, holder idiom, and enum. Prefer enum (or the holder idiom): thread-safe, simple, and enum also resists reflection and serialization attacks.
*Follow-up:* Why is `volatile` required in double-checked locking?

**Q2. How can a Singleton be broken and how do you prevent it?**
*Expected:* Reflection (call the private constructor), serialization (a new instance on deserialization), `clone()`, and multiple class loaders. Prevent with an enum, a constructor guard, `readResolve()`, and by not implementing `Cloneable`.
*Follow-up:* Is a Singleton unique across a cluster?

**Q3. How is a Spring singleton bean different from the GoF Singleton?**
*Expected:* Spring guarantees one instance *per container* per bean definition, not per JVM or per class. It is created by the container and injected, so it is mockable. GoF Singleton enforces uniqueness inside the class itself.
*Follow-up:* What happens to thread safety in a singleton bean?

**Q4. Factory Method versus Abstract Factory?**
*Expected:* Factory Method creates one product and lets a subclass or configuration pick the type. Abstract Factory creates families of related products so they stay consistent.
*Follow-up:* How does Spring's dependency injection replace many factories?

**Q5. Why use Builder instead of telescoping constructors or setters?**
*Expected:* Readable calls, no argument-order mistakes, optional parameters with defaults, validation in one place, and an immutable result (no half-built object visible, unlike setters).
*Follow-up:* How do records and Lombok's `@Builder` relate?

**Q6. Strategy versus State?**
*Expected:* Both delegate to an interface. With Strategy the *client* picks the algorithm; with State the object changes its own behaviour as its internal state transitions.
*Follow-up:* Model a loan lifecycle (applied, approved, disbursed, closed). Which one?

**Q7. Decorator versus Proxy versus Adapter?**
*Expected:* Decorator adds behaviour with the same interface; Proxy controls access with the same interface; Adapter changes the interface. See comparison table.
*Follow-up:* Which does Spring AOP use, and how?

**Q8. How does Spring AOP implement `@Transactional`?**
*Expected:* Creates a proxy (JDK dynamic proxy when the bean implements an interface and proxy-target-class is off; CGLIB subclass otherwise, which is the Spring Boot default). The proxy starts and commits or rolls back the transaction around the call.
*Follow-up:* Why does calling a `@Transactional` method from within the same class not start a transaction?

**Q9. What is the difference between JDK dynamic proxies and CGLIB?**
*Expected:* JDK proxies implement interfaces only; CGLIB generates a subclass so it cannot proxy `final` classes or methods. Both intercept calls to add behaviour.
*Follow-up:* What breaks if a bean method is `final`?

**Q10. Template Method versus Strategy?**
*Expected:* Template Method uses inheritance and fixes the algorithm skeleton in a base class; Strategy uses composition and swaps the whole algorithm at runtime. Prefer Strategy when you need runtime flexibility or multiple axes of variation.
*Follow-up:* Where does `JdbcTemplate` fit?

**Q11. How does Observer work in Java today?**
*Expected:* `Observable` is deprecated (Java 9). Use `PropertyChangeSupport`, `Flow`, Spring `ApplicationEventPublisher`, or a message broker for distributed consumers.
*Follow-up:* What is the lapsed-listener memory leak?

**Q12. Where do you see Chain of Responsibility in Spring and Java EE?**
*Expected:* Servlet `FilterChain`, Spring Security's `SecurityFilterChain`, `HandlerInterceptor`, and logging handler hierarchies.
*Follow-up:* How is it different from the Decorator?

**Q13. How do you implement Prototype in Java and what are the pitfalls of `clone()`?**
*Expected:* `Cloneable` and `clone()` are awkward: shallow copy by default, no constructor call, checked exception. Prefer copy constructors or static copy factories, and immutable objects where possible.
*Follow-up:* Shallow versus deep copy of a list field?

**Q14. How do you make an object immutable, and which patterns help?**
*Expected:* `final` class and fields, no setters, defensive copies, unmodifiable collections or records. Builder constructs it, and Factory methods return it.
*Follow-up:* Is `Collections.unmodifiableList` truly immutable?

**Q15. Which patterns did you use in your last project and why?**
*Expected:* Name concrete ones: Strategy for pricing rules, Chain for validations, Adapter for a vendor API, Facade for orchestration, Observer via domain events. State the problem each solved and a trade-off you accepted.
*Follow-up:* Which pattern did you consider and reject?

**Q16. What are anti-patterns related to design patterns?**
*Expected:* Singleton abuse (global mutable state), God object, premature abstraction, Service Locator hiding dependencies, anemic facades, over-engineered factories.
*Follow-up:* How do you decide a pattern is overkill?

> **Key Takeaway:** For every pattern, be ready with: the problem, the structure, a JDK or Spring example, one drawback.

---

## 🧠 Scenario-Based Questions

**1. A new payment channel arrives every quarter and each one edits a big `switch`. How do you redesign?**
Extract a `PaymentProcessor` interface (Strategy), register each implementation as a Spring bean keyed by type (Factory via bean map). The router never changes; new channels are additive, which satisfies Open/Closed. Add a startup check that every enum type has a processor.

**2. Your Singleton cache returns stale data after you scale to four pods. Why?**
A Singleton is unique per JVM (and per class loader), not per cluster. Each pod has its own copy. Move shared state to Redis or the database, or use a distributed cache with invalidation events.

**3. `@Transactional` on a method does nothing when called from another method in the same service. Why?**
Spring applies the transaction through a proxy, but a call from inside the same object bypasses the proxy (self-invocation). Move the method to another bean, inject the proxy, or restructure so the transactional boundary is entered from outside.

**4. Memory grows steadily and a heap dump shows thousands of listener objects. What pattern problem is this?**
The lapsed-listener leak in Observer: subscribers register but never unsubscribe, and the subject's list keeps them reachable. Fix by deregistering on lifecycle end, using weak references, or letting Spring manage listener beans.

**5. A constructor now takes 11 arguments and callers keep swapping two `BigDecimal` values. What do you do?**
Introduce a Builder (or a record with a compact validating constructor plus a builder), put required fields in the factory method, validate in `build()`, and make the result immutable.

**6. A 20-branch `if/else` computes loan fees by product. How do you refactor safely?**
Characterise the current behaviour with tests, extract a `FeeStrategy` per branch, select it through a map or enum, and remove the conditional. Use a Chain instead if rules apply *cumulatively* rather than exclusively.

**7. You must add audit logging and metrics to 40 service methods without touching them.**
Use a Proxy via Spring AOP (an `@Around` aspect) rather than editing each method. Keep the aspect thin and measure its overhead.

---

## 🛠 Debugging Tips

| Symptom | Likely pattern-related cause | What to check |
|---|---|---|
| Two instances of a "singleton" | Multiple class loaders, deserialization, or reflection | Class loader hierarchy, `readResolve()`, enum conversion |
| `@Transactional`/`@Cacheable` ignored | Self-invocation or non-public or `final` method | Call path, method visibility, proxy type in the debugger |
| `ClassCastException` on a bean | JDK proxy exposes only interfaces | Inject by interface, or enable class-based proxies |
| Stack traces full of `$$EnhancerBySpringCGLIB` or `Proxy$` frames | Normal proxy layers | Focus on the first frame in your own package |
| Intermittent wrong data in a singleton bean | Mutable instance fields shared across threads | Remove request state from fields; thread dump for contention |
| Memory leak with growing listener lists | Observers never removed | Heap dump: dominator tree of the listener collection |
| Wrong strategy chosen | Bad key or map lookup, missing bean | Log selected strategy and key with the request ID |
| A validator never runs | Chain not linked or link returns wrong node | Print the chain at startup; unit test each link |
| Builder produces invalid objects | Missing validation in `build()` | Add checks; test boundary values |

**Useful techniques**
- In a debugger, inspect `bean.getClass()`: if it shows `$$SpringCGLIB$$` or `jdk.proxy`, you are looking at a proxy
- `jcmd <pid> GC.class_histogram` quickly shows runaway instance counts (listeners, decorators)
- Log pattern decisions at DEBUG with correlation IDs: "strategy=COMPOUND product=HOME"

> **Key Takeaway:** When behaviour is "mysteriously" missing, suspect a proxy boundary. When memory grows, suspect an observer list.

---

## 📌 When to Use Design Patterns

- Behaviour varies by type, region, product, or customer: Strategy, State
- New variants arrive regularly: Factory, bean registries
- Cross-cutting concerns across many classes: Proxy, Decorator, AOP
- Integrating legacy or vendor systems: Adapter, Facade
- Many components react to one event: Observer, domain events
- Ordered, pluggable validations or approvals: Chain of Responsibility
- Complex immutable objects with optional fields: Builder
- Shared algorithm skeleton across jobs: Template Method

## 🚫 When NOT to Use Design Patterns

- One-off logic with no foreseeable variation (a method is enough)
- When a lambda, enum, record, or plain polymorphism solves it more simply
- Prototypes and throw-away code
- To impress reviewers: indirection without a concrete problem hurts readability
- Hand-rolled Singleton or Service Locator inside a DI framework

---

## 💡 Interview Tips

**What the interviewer expects**
- The problem each pattern solves, not just UML boxes
- Concrete JDK and Spring examples
- Trade-offs and thread-safety awareness (especially Singleton)
- Ability to refactor a messy snippet into a pattern live

**Common mistakes**
- Reciting definitions with no example
- Confusing Decorator, Proxy, and Adapter
- Claiming a Spring bean "is the Singleton pattern"
- Forgetting `volatile` in double-checked locking
- Suggesting patterns for everything

**How to answer confidently**
- Use the template: *problem → pattern → small code sketch → JDK/Spring example → trade-off*
- Tie to your project: "We replaced a 15-branch fee calculator with Strategy beans, and new products became one-class changes"
- Mention modern alternatives: lambdas for Strategy, records for value objects, events for Observer

**Likely follow-ups:** SOLID relationships, dependency injection versus Factory, thread-safe Singleton, proxy mechanics, when patterns hurt, how you test them.

---

## 📝 Short Conclusion Summary

Design patterns are named solutions to recurring design problems, grouped into creational, structural, and behavioral families. They work by isolating what varies behind an interface so new requirements add code instead of modifying it. In Java and Spring many patterns are already built in: Strategy as lambdas and `Comparator`, Factory as bean maps, Proxy behind `@Transactional`, Observer as application events, Chain of Responsibility as filter chains. A senior engineer applies a pattern only against a real pain, picks the lightest form the language allows, and can state its cost: extra indirection, shared-state risk, or proxy surprises.

---

## ⚡ TL;DR

| Topic | Summary |
|---|---|
| Purpose | Reusable, named solutions that isolate change and reduce coupling |
| Internal Working | Find what varies → hide it behind an interface → depend on the abstraction |
| Best For | Variable behaviour, scattered creation, cross-cutting concerns, event reactions |
| Avoid | Single-use code, pattern-for-pattern's-sake, hand-rolled Singletons in DI apps |
| Advantages | Open/Closed extension, testability, shared vocabulary |
| Disadvantages | More classes, indirection, debugging through proxies, over-engineering risk |
| Performance | Overhead negligible; watch locks, synchronous observers, deep proxy chains |
| Interview Keyword | Strategy, Factory, Builder, Proxy, Decorator, Observer, Open/Closed, composition over inheritance |

---

## 🚀 30-Second Interview Answer

> "Design patterns are proven, named solutions to recurring design problems, and I use them to isolate what varies so new requirements add code instead of changing it. In our loan platform, interest rules are Strategies, payment channels are Spring beans looked up by type, which is the Factory idea, validations form a Chain of Responsibility, a Facade fronts the orchestration, vendor APIs sit behind Adapters, and approval events go out through Observer using Spring's event publisher. Cross-cutting concerns like transactions and caching come from Spring AOP, which is the Proxy pattern. I prefer lambdas, records, and DI over heavyweight class hierarchies, I keep Singletons stateless, and I only introduce a pattern when there is real variation, because every pattern adds indirection that has to earn its place."

---

## 📚 Further Reading

- Gamma, Helm, Johnson, Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*
- Joshua Bloch, *Effective Java* (Singleton with enum, Builder, static factory methods, immutability)
- Eric Freeman et al., *Head First Design Patterns*
- Spring Framework reference: AOP proxies and application events
- Refactoring.Guru, Design Patterns catalogue (examples in Java)
