# 21. Strategy Pattern (Behavioral)

> **One-liner:** Define a family of interchangeable algorithms behind an interface and select one at runtime.

## 1. The problem it solves

Interest may be simple or compound (and tomorrow, reducing-balance). `if (type == ...)` chains violate Open/Closed.

## 2. Structure (participants)

- **Strategy** - `InterestStrategy` (functional interface).
- **ConcreteStrategies** - `SIMPLE`, `COMPOUND` lambdas.
- **Context** - `InterestCalculator` with `setStrategy`.

## 3. Real-life analogy

Choosing a route in Google Maps - same goal, different algorithm (fastest, shortest, avoid tolls).

## 4. Code walkthrough

File: `src/behavioral/StrategyDemo.java` (self-contained, runnable)

100000 @ 8% for 3 years: simple = 24000.00, compound = 25971.20, using `BigDecimal` with explicit rounding (never `double` for money).

## 5. Where you've already seen it (JDK / Spring)

- `Comparator`
- `ThreadPoolExecutor.RejectedExecutionHandler`
- Spring Security `PasswordEncoder`, `AuthenticationProvider`
- `Collectors` as strategies

## 6. Interview questions & model answers

**Q1. How do lambdas change Strategy?**  
A functional-interface strategy needs no class - pass a lambda or method reference.

**Q2. Strategy vs State?**  
Strategy: client picks the algorithm, strategies are independent. State: transitions are internal and driven by context state.

**Q3. Strategy vs Template Method?**  
Strategy uses composition (swap whole algorithm at runtime). Template Method uses inheritance (fixed skeleton, vary steps).

**Q4. Wire strategies with Spring?**  
Inject `Map<String, InterestStrategy>` keyed by bean name, pick by request value.

**Q5. Why BigDecimal?**  
Binary floating point can't represent decimal fractions exactly; money needs exact scale and `RoundingMode`.

## 7. Pitfalls / when NOT to use

- Clients must know the strategies to choose well.
- Too many tiny strategies - group related ones.

## 8. Compare with similar patterns

**vs Command:** algorithm vs request. **vs Bridge:** Bridge splits abstraction/implementation hierarchies.

## 9. 30-second answer for the interview

"Strategy is a behavioral pattern: define a family of interchangeable algorithms behind an interface and select one at runtime. A typical case: Interest may be simple or compound (and tomorrow, reducing-balance). `if (type == ...)` chains violate Open/Closed. The trade-off I'd mention: clients must know the strategies to choose well."

## 10. Learning resources

- [Refactoring.Guru - Strategy](https://refactoring.guru/design-patterns/strategy)
- [Baeldung - Strategy pattern](https://www.baeldung.com/java-strategy-pattern)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
