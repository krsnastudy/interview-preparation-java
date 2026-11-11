# 12. Proxy Pattern (Structural)

> **One-liner:** Provide a surrogate that controls access to another object (lazy load, cache, security, logging).

## 1. The problem it solves

Reading balances hits the DB every time; you also want timing around every call without editing service code.

## 2. Structure (participants)

- **Subject** - `AccountService`.
- **RealSubject** - `RealAccountService`.
- **Proxy** - `CachingProxy` (static) and a JDK **dynamic proxy** with an `InvocationHandler`.

## 3. Real-life analogy

A cheque is a proxy for cash; a receptionist screens access to the manager.

## 4. Code walkthrough

File: `src/structural/ProxyDemo.java` (self-contained, runnable)

`CachingProxy` calls the DB once for `A1`; second call is served from cache. `timing(...)` builds a `java.lang.reflect.Proxy` that wraps *any* interface and prints elapsed time.

## 5. Where you've already seen it (JDK / Spring)

- `java.lang.reflect.Proxy`
- Spring AOP: `@Transactional`, `@Cacheable`, `@Async` (JDK proxy or CGLIB)
- Hibernate lazy-loading proxies

## 6. Interview questions & model answers

**Q1. JDK dynamic proxy vs CGLIB?**  
JDK proxy needs an interface and generates a class implementing it. CGLIB (or ByteBuddy) subclasses the target class - can't proxy `final` classes/methods.

**Q2. Why does `@Transactional` not work on self-invocation?**  
The call `this.method()` bypasses the proxy, so no advice runs. Call via another bean or inject self / use AspectJ weaving.

**Q3. Types of proxies?**  
Virtual (lazy), protection (security), remote (RPC stub), caching, smart reference (counting/logging).

**Q4. Proxy vs Decorator?**  
Proxy controls access & lifecycle of the subject, usually creating it itself; Decorator adds behaviour supplied by the client.

## 7. Pitfalls / when NOT to use

- Self-invocation bypass.
- `final` classes/methods can't be CGLIB-proxied.
- `equals`/`hashCode`/`toString` on proxies behave differently.

## 8. Compare with similar patterns

**vs Adapter:** Adapter changes the interface. **vs Facade:** Facade simplifies; Proxy has an identical interface.

## 9. 30-second answer for the interview

"Proxy is a structural pattern: provide a surrogate that controls access to another object (lazy load, cache, security, logging). A typical case: Reading balances hits the DB every time; you also want timing around every call without editing service code. The trade-off I'd mention: self-invocation bypass."

## 10. Learning resources

- [Refactoring.Guru - Proxy](https://refactoring.guru/design-patterns/proxy)
- [Baeldung - Introduction to Spring AOP proxies](https://www.baeldung.com/spring-aop)
- [Oracle - Dynamic Proxy Classes](https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
