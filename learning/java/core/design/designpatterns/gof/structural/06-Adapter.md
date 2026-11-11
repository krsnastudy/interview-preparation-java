# 6. Adapter Pattern (Structural)

> **One-liner:** Convert the interface of an existing class into the interface clients expect.

## 1. The problem it solves

Your app speaks `PaymentGateway.pay(account, amount)`, but the vendor only exposes a legacy SOAP `sendXml(String)`. You cannot (or should not) change either side.

## 2. Structure (participants)

- **Target** - `PaymentGateway`.
- **Adaptee** - `LegacySoapGateway`.
- **Adapter** - `SoapGatewayAdapter` implements Target and *wraps* the Adaptee (object adapter).

## 3. Real-life analogy

A travel plug adapter: your Indian charger works in a UK socket without modifying either.

## 4. Code walkthrough

File: `src/structural/AdapterDemo.java` (self-contained, runnable)

`SoapGatewayAdapter.pay()` builds the XML and delegates to `legacy.sendXml`. Clients depend only on `PaymentGateway`, so replacing SOAP with REST later means writing one new adapter.

## 5. Where you've already seen it (JDK / Spring)

- `Arrays.asList()` (array -> List)
- `InputStreamReader` (bytes -> chars)
- `Collections.enumeration()`
- Spring MVC `HandlerAdapter`

## 6. Interview questions & model answers

**Q1. Object adapter vs class adapter?**  
Object adapter uses composition (preferred in Java). Class adapter uses inheritance - needs multiple inheritance, so not possible for two classes in Java.

**Q2. Adapter vs Decorator?**  
Adapter *changes* the interface; Decorator keeps the interface and adds behaviour.

**Q3. Adapter vs Facade?**  
Adapter makes one interface fit another; Facade simplifies a whole subsystem behind a new interface.

**Q4. Where have you used it in a bank project?**  
Wrapping legacy mainframe/SOAP/MQ services behind clean domain interfaces (anti-corruption layer in DDD terms).

## 7. Pitfalls / when NOT to use

- Adapters pile up if the target interface is unstable.
- Don't leak adaptee exceptions - translate them.

## 8. Compare with similar patterns

**vs Proxy:** Proxy keeps the same interface and controls access. **vs Bridge:** Bridge is designed up-front; Adapter fixes incompatibility after the fact.

## 9. 30-second answer for the interview

"Adapter is a structural pattern: convert the interface of an existing class into the interface clients expect. A typical case: Your app speaks `PaymentGateway.pay(account, amount)`, but the vendor only exposes a legacy SOAP `sendXml(String)`. You cannot (or should not) change either side. The trade-off I'd mention: adapters pile up if the target interface is unstable."

## 10. Learning resources

- [Refactoring.Guru - Adapter](https://refactoring.guru/design-patterns/adapter)
- [Baeldung - Adapter pattern](https://www.baeldung.com/java-adapter-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
