# 9. Decorator Pattern (Structural)

> **One-liner:** Attach additional responsibilities to an object dynamically by wrapping it - same interface in and out.

## 1. The problem it solves

You need Email, Email+SMS, Email+Audit, Email+SMS+Audit... subclassing explodes. You want to combine behaviours at runtime.

## 2. Structure (participants)

- **Component** - `Notifier`.
- **ConcreteComponent** - `EmailNotifier`.
- **Decorator** - abstract class holding a `Notifier`.
- **ConcreteDecorator** - `SmsDecorator`, `AuditDecorator`.

## 3. Real-life analogy

Putting toppings on a pizza or wrapping a gift: each layer adds something, the core stays the same.

## 4. Code walkthrough

File: `src/structural/DecoratorDemo.java` (self-contained, runnable)

`new AuditDecorator(new SmsDecorator(new EmailNotifier()))` - order of wrapping decides output order (audit first, then email, then SMS).

## 5. Where you've already seen it (JDK / Spring)

- `BufferedInputStream(new FileInputStream(..))`
- `Collections.unmodifiableList()`, `synchronizedList()`
- `HttpServletRequestWrapper`

## 6. Interview questions & model answers

**Q1. Decorator vs inheritance?**  
Inheritance is static and per-class; decorators are runtime and composable.

**Q2. Decorator vs Proxy?**  
Both wrap with the same interface. Decorator *adds behaviour* chosen by the client; Proxy *controls access* to a subject (lazy load, security, remote).

**Q3. Drawbacks?**  
Many small classes, hard-to-debug wrapping order, identity checks (`==`) break.

**Q4. Which Java I/O class is the textbook example?**  
`BufferedReader(new InputStreamReader(System.in))`.

## 7. Pitfalls / when NOT to use

- Order-sensitive stacking (encrypt-then-compress vs compress-then-encrypt).
- Decorators must delegate *all* methods of the interface.

## 8. Compare with similar patterns

**vs Composite:** one child vs many. **vs Chain of Responsibility:** chain may stop propagation; decorators always delegate.

## 9. 30-second answer for the interview

"Decorator is a structural pattern: attach additional responsibilities to an object dynamically by wrapping it - same interface in and out. A typical case: You need Email, Email+SMS, Email+Audit, Email+SMS+Audit... subclassing explodes. You want to combine behaviours at runtime. The trade-off I'd mention: order-sensitive stacking (encrypt-then-compress vs compress-then-encrypt)."

## 10. Learning resources

- [Refactoring.Guru - Decorator](https://refactoring.guru/design-patterns/decorator)
- [Baeldung - Decorator pattern](https://www.baeldung.com/java-decorator-pattern)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
