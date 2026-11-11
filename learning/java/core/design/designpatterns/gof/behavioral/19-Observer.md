# 19. Observer Pattern (Behavioral)

> **One-liner:** Define a one-to-many dependency so that when one object changes state, all dependents are notified.

## 1. The problem it solves

A price ticker must update dashboards, alert services and loggers without knowing who they are.

## 2. Structure (participants)

- **Subject** - `StockTicker` with `subscribe/unsubscribe/publish`.
- **Observer** - `PriceListener` (functional interface).
- **ConcreteObservers** - `AlertService`, lambdas.

## 3. Real-life analogy

YouTube subscriptions: the channel publishes, subscribers get notified.

## 4. Code walkthrough

File: `src/behavioral/ObserverDemo.java` (self-contained, runnable)

Three listeners subscribe; publishing 1625 triggers the dashboard, the log and the >1600 alert. Lambdas make observers one-liners.

## 5. Where you've already seen it (JDK / Spring)

- `PropertyChangeListener`
- `java.util.Observable/Observer` (deprecated since Java 9)
- Spring `ApplicationEvent` + `@EventListener`
- `java.util.concurrent.Flow` / Reactive Streams

## 6. Interview questions & model answers

**Q1. Why was `java.util.Observable` deprecated?**  
It's a class (not interface), not serializable, `setChanged` is protected, thread-safety and event model weaknesses. Use `PropertyChangeSupport` or Flow/Reactive.

**Q2. Push vs pull model?**  
Push sends data in the notification; pull sends a reference and observers query the subject.

**Q3. Memory leak risk?**  
Forgetting to unsubscribe keeps observers alive (the 'lapsed listener' problem). Use weak references or explicit unsubscribe.

**Q4. Spring events sync or async?**  
Synchronous and in the publisher's thread/transaction by default; add `@Async` or `@TransactionalEventListener` for decoupling.

**Q5. Observer vs Pub/Sub?**  
Observer: subject holds observers directly. Pub/Sub: a broker decouples publisher and subscriber (topics, queues).

## 7. Pitfalls / when NOT to use

- Lapsed listeners.
- Notification order is unspecified.
- Exceptions in one observer can block others - isolate.

## 8. Compare with similar patterns

**vs Mediator:** broadcast vs coordination. **vs Event Sourcing:** events as the source of truth.

## 9. 30-second answer for the interview

"Observer is a behavioral pattern: define a one-to-many dependency so that when one object changes state, all dependents are notified. A typical case: A price ticker must update dashboards, alert services and loggers without knowing who they are. The trade-off I'd mention: lapsed listeners."

## 10. Learning resources

- [Refactoring.Guru - Observer](https://refactoring.guru/design-patterns/observer)
- [Baeldung - Observer pattern](https://www.baeldung.com/java-observer-pattern)
- [Spring - Application events](https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
