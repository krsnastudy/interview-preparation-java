# 2. Factory Method Pattern (Creational)

> **One-liner:** Define an interface for creating an object but let subclasses decide which class to instantiate.

## 1. The problem it solves

Client code should not hard-code `new UpiProcessor()` / `new CardProcessor()`. Adding a new payment type should not require editing existing logic.

## 2. Structure (participants)

- **Product** - interface (`PaymentProcessor`).
- **ConcreteProduct** - `UpiProcessor`, `CardProcessor`.
- **Creator** - abstract class with `createProcessor()` (the factory method) and business logic that uses it.
- **ConcreteCreator** - overrides the factory method.

## 3. Real-life analogy

A hiring pipeline: the 'Recruiter' role is the same, but a campus recruiter vs a lateral recruiter each decides what kind of candidate to bring in.

## 4. Code walkthrough

File: `src/creational/FactoryMethodDemo.java` (self-contained, runnable)

`PaymentService.pay()` is the template that calls the abstract `createProcessor()`. `UpiService` / `CardService` return different processors. Open/Closed: add `NetBankingService` without touching existing code.

Note the *Simple Factory / static factory* (`switch` on a string) is **not** GoF Factory Method - it's a common interview trap.

## 5. Where you've already seen it (JDK / Spring)

- `Calendar.getInstance()`, `NumberFormat.getInstance()`
- `List.of(...)`, `Integer.valueOf()`, `Optional.of()` (static factory methods)
- Spring `FactoryBean<T>`

## 6. Interview questions & model answers

**Q1. Factory Method vs Simple Factory?**  
Simple factory = one class with a big `switch`; not a GoF pattern, violates OCP. Factory Method = inheritance-based: subclasses override the creation step.

**Q2. Factory Method vs Abstract Factory?**  
Factory Method creates *one* product via inheritance. Abstract Factory creates a *family* of related products via composition (an object with several factory methods).

**Q3. How would you do this in Spring?**  
Inject `Map<String, PaymentProcessor>` (bean name -> bean) and look up by key; no `switch`, new beans are auto-discovered.

**Q4. Why prefer static factory methods over constructors (Bloch)?**  
They have names, can return cached instances/subtypes, and need not create a new object each call.

## 7. Pitfalls / when NOT to use

- Class explosion - one creator subclass per product.
- Using it where a simple constructor or lambda `Supplier<T>` suffices.

## 8. Compare with similar patterns

**vs Builder:** Factory Method decides *which type*; Builder decides *how to assemble one complex type*. **vs Strategy:** Strategy swaps behaviour; Factory Method swaps what gets created.

## 9. 30-second answer for the interview

"Factory Method is a creational pattern: define an interface for creating an object but let subclasses decide which class to instantiate. A typical case: Client code should not hard-code `new UpiProcessor()` / `new CardProcessor()`. Adding a new payment type should not require editing existing logic. The trade-off I'd mention: class explosion - one creator subclass per product."

## 10. Learning resources

- [Refactoring.Guru - Factory Method](https://refactoring.guru/design-patterns/factory-method)
- [Baeldung - Factory Method pattern](https://www.baeldung.com/java-factory-pattern)
- [Effective Java (Joshua Bloch) - Items on builders, singletons, static factories](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
