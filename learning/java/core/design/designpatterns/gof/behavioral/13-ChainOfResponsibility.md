# 13. Chain of Responsibility Pattern (Behavioral)

> **One-liner:** Pass a request along a chain of handlers; each decides to handle it, reject it, or pass it on.

## 1. The problem it solves

A transaction must pass KYC, limit and fraud checks. Hard-coding `if/else` ordering in one method is rigid and unreadable.

## 2. Structure (participants)

- **Handler** - abstract `Validator` with `then(next)` and a final `validate()` template.
- **ConcreteHandlers** - `KycValidator`, `LimitValidator`, `FraudValidator`.
- **Client** - builds the chain and sends the request to the head.

## 3. Real-life analogy

Customer support escalation: L1 -> L2 -> L3; each level resolves it or escalates.

## 4. Code walkthrough

File: `src/behavioral/ChainOfResponsibilityDemo.java` (self-contained, runnable)

`chain.validate(txn)` returns the first failing message or `APPROVED`. Order and membership of the chain are configured at runtime; adding a check = a new class.

## 5. Where you've already seen it (JDK / Spring)

- Servlet `Filter`/`FilterChain`
- Spring Security `SecurityFilterChain`
- Spring MVC `HandlerInterceptor`
- `java.util.logging` handler propagation

## 6. Interview questions & model answers

**Q1. Chain of Responsibility vs Decorator?**  
A handler may *stop* the chain; decorators always delegate. Chain handlers decide *whether*, decorators add *what*.

**Q2. What if no handler handles the request?**  
Define a default (approve/reject) at the chain end - the demo returns `APPROVED`.

**Q3. Pure vs impure chain?**  
Pure: exactly one handler handles it. Impure (servlet filters): several may act, then pass on.

**Q4. Where used in Spring Security?**  
Each filter (CORS, CSRF, JWT/auth, authorization) processes then calls `chain.doFilter`.

## 7. Pitfalls / when NOT to use

- Request may fall off the end unhandled.
- Long chains are hard to debug - log each hop.
- Order bugs (authN must come before authZ).

## 8. Compare with similar patterns

**vs Command:** Command encapsulates a request; chain routes it. **vs Pipeline/Middleware:** same idea, often all handlers run.

## 9. 30-second answer for the interview

"Chain of Responsibility is a behavioral pattern: pass a request along a chain of handlers; each decides to handle it, reject it, or pass it on. A typical case: A transaction must pass KYC, limit and fraud checks. Hard-coding `if/else` ordering in one method is rigid and unreadable. The trade-off I'd mention: request may fall off the end unhandled."

## 10. Learning resources

- [Refactoring.Guru - Chain Of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility)
- [Baeldung - Chain of Responsibility](https://www.baeldung.com/chain-of-responsibility-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
