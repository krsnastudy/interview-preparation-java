# 7. Bridge Pattern (Structural)

> **One-liner:** Decouple an abstraction from its implementation so the two can vary independently.

## 1. The problem it solves

Alerts come in types (transaction, fraud) and channels (email, SMS, push). Inheritance would need `FraudSmsAlert`, `FraudEmailAlert`, ... - an M x N explosion.

## 2. Structure (participants)

- **Abstraction** - `Alert` holding a reference to the implementor.
- **RefinedAbstraction** - `TransactionAlert`, `FraudAlert`.
- **Implementor** - `MessageSender`.
- **ConcreteImplementor** - `EmailSender`, `SmsSender`.

## 3. Real-life analogy

A TV remote (abstraction) and the TV brand (implementation): any remote type works with any TV via the same infrared interface.

## 4. Code walkthrough

File: `src/structural/BridgeDemo.java` (self-contained, runnable)

Two alert classes x two senders = 4 combinations with only 4 classes (not M x N). Add `PushSender` and every alert type gets push for free.

## 5. Where you've already seen it (JDK / Spring)

- JDBC: `DriverManager`/`Connection` (abstraction) over vendor `Driver`s (implementation)
- SLF4J API (abstraction) bridging to Logback/Log4j2 (implementation)

## 6. Interview questions & model answers

**Q1. Bridge vs Adapter?**  
Bridge is planned up-front to let two hierarchies evolve separately; Adapter retrofits unrelated interfaces.

**Q2. Bridge vs Strategy?**  
Structurally similar (composition + interface). Strategy is about swapping *an algorithm*; Bridge is about splitting a *two-dimensional class hierarchy*.

**Q3. Why composition over inheritance here?**  
Inheritance binds the abstraction to one implementation at compile time; composition allows runtime switching and no class explosion.

**Q4. Real example in Java ecosystem?**  
JDBC and SLF4J are the classic ones.

## 7. Pitfalls / when NOT to use

- Over-engineering when there is only one dimension of variation.
- Hard to see on a UML diagram - explain it as 'two hierarchies connected by one reference'.

## 8. Compare with similar patterns

**vs Strategy/State:** same shape, different intent.

## 9. 30-second answer for the interview

"Bridge is a structural pattern: decouple an abstraction from its implementation so the two can vary independently. A typical case: Alerts come in types (transaction, fraud) and channels (email, SMS, push). Inheritance would need `FraudSmsAlert`, `FraudEmailAlert`, ... - an M x N explosion. The trade-off I'd mention: over-engineering when there is only one dimension of variation."

## 10. Learning resources

- [Refactoring.Guru - Bridge](https://refactoring.guru/design-patterns/bridge)
- [Baeldung - Bridge pattern](https://www.baeldung.com/java-bridge-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
