# 23. Visitor Pattern (Behavioral)

> **One-liner:** Add new operations to a set of classes without modifying them, using double dispatch.

## 1. The problem it solves

You have `Bond` and `Equity` and need valuation, risk labels, and later reporting, without bloating the instrument classes.

## 2. Structure (participants)

- **Visitor<R>** - `visit(Bond)`, `visit(Equity)`.
- **Element** - `Instrument.accept(visitor)`.
- **ConcreteVisitors** - `ValuationVisitor`, `RiskVisitor`.

## 3. Real-life analogy

A tax inspector visiting different business types: same visit, different checks per type.

## 4. Code walkthrough

File: `src/behavioral/VisitorDemo.java` (self-contained, runnable)

Each instrument's `accept` calls back `v.visit(this)` - the **second dispatch** chooses the overload by the element's concrete type. A generic `Visitor<R>` returns Double or String.

## 5. Where you've already seen it (JDK / Spring)

- `Files.walkFileTree(FileVisitor)`
- `javax.lang.model.element.ElementVisitor`
- ASM/ByteBuddy class visitors
- Modern alternative: **sealed interfaces + pattern-matching `switch`** (Java 21)

## 6. Interview questions & model answers

**Q1. What is double dispatch?**  
Java overloads resolve at compile time on static type, so `accept` re-dispatches (`v.visit(this)`) to pick the method by the *runtime* type of the element.

**Q2. Main drawback?**  
Adding a new element type forces changes in every visitor (opposite of adding a new operation, which is easy).

**Q3. How does Java 21 reduce the need for Visitor?**  
Sealed hierarchies + `switch` pattern matching give exhaustive, compiler-checked type dispatch without `accept` boilerplate.

**Q4. When use Visitor?**  
Stable element hierarchy, frequently added operations (ASTs, instrument pricing, file trees).

## 7. Pitfalls / when NOT to use

- Breaks encapsulation if visitors need internals.
- Rigid to element changes.

## 8. Compare with similar patterns

**vs Iterator:** iterator traverses; visitor operates per type. **vs Interpreter:** visitor is often used to evaluate ASTs.

## 9. 30-second answer for the interview

"Visitor is a behavioral pattern: add new operations to a set of classes without modifying them, using double dispatch. A typical case: You have `Bond` and `Equity` and need valuation, risk labels, and later reporting, without bloating the instrument classes. The trade-off I'd mention: breaks encapsulation if visitors need internals."

## 10. Learning resources

- [Refactoring.Guru - Visitor](https://refactoring.guru/design-patterns/visitor)
- [Baeldung - Visitor pattern](https://www.baeldung.com/java-visitor-pattern)
- [JEP 441 - Pattern Matching for switch](https://openjdk.org/jeps/441)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
