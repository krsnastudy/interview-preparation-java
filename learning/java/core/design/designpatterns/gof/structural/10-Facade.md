# 10. Facade Pattern (Structural)

> **One-liner:** Provide a simple, unified interface to a complex subsystem.

## 1. The problem it solves

Applying for a loan involves a credit bureau, KYC service and disbursement service. Callers should not orchestrate three APIs.

## 2. Structure (participants)

- **Facade** - `LoanFacade.applyForLoan()`.
- **Subsystem classes** - `CreditBureau`, `KycService`, `DisbursementService` (unaware of the facade).

## 3. Real-life analogy

A hotel concierge: one request ('arrange my airport pickup') instead of dealing with cab, desk and staff yourself.

## 4. Code walkthrough

File: `src/structural/FacadeDemo.java` (self-contained, runnable)

`LoanFacade` checks KYC, then credit score, then disburses. Controllers call one method; subsystem internals can change freely.

## 5. Where you've already seen it (JDK / Spring)

- `java.net.URL.openStream()`
- Spring `JdbcTemplate`/`RestTemplate` simplify many low-level calls
- Typical `@Service` application-service layer

## 6. Interview questions & model answers

**Q1. Facade vs Mediator?**  
Facade is one-way (client -> subsystem) and subsystems don't know it. Mediator is two-way: colleagues talk *through* it.

**Q2. Does a Facade hide the subsystem?**  
No - it's a convenience; advanced users can still use subsystem classes directly.

**Q3. Facade in microservices?**  
API Gateway / Backend-for-Frontend aggregating several services.

**Q4. Risk?**  
Becoming a 'god class' that holds all business logic.

## 7. Pitfalls / when NOT to use

- Facade bloat.
- Coupling every client to the facade makes it a bottleneck for change.

## 8. Compare with similar patterns

**vs Adapter:** Adapter changes an interface; Facade simplifies many. **vs Proxy:** Proxy has the same interface as the real subject.

## 9. 30-second answer for the interview

"Facade is a structural pattern: provide a simple, unified interface to a complex subsystem. A typical case: Applying for a loan involves a credit bureau, KYC service and disbursement service. Callers should not orchestrate three APIs. The trade-off I'd mention: facade bloat."

## 10. Learning resources

- [Refactoring.Guru - Facade](https://refactoring.guru/design-patterns/facade)
- [Baeldung - Facade pattern](https://www.baeldung.com/java-facade-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
