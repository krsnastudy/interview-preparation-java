# 4. Builder Pattern (Creational)

> **One-liner:** Separate the construction of a complex object from its representation, step by step.

## 1. The problem it solves

A `LoanApplication` has 2 mandatory and many optional fields. Telescoping constructors (`new Loan(a,b,null,false,null)`) are unreadable; JavaBean setters leave the object half-built and mutable.

## 2. Structure (participants)

- **Product** - immutable `LoanApplication` with a private constructor.
- **Builder** - static nested class with fluent setters and `build()`.
- `build()` is the single place for **validation** (invariants).

## 3. Real-life analogy

Ordering a custom sandwich: pick bread, filling, sauce in any order; the sandwich is only made when you say 'done'.

## 4. Code walkthrough

File: `src/creational/BuilderDemo.java` (self-contained, runnable)

`LoanApplication.builder("Krishna", amount).tenureMonths(60).insured(true).build()` - mandatory fields in the builder ctor, optional ones fluent, validation at `build()` throws if `amount <= 0`.

## 5. Where you've already seen it (JDK / Spring)

- `StringBuilder`, `Stream.builder()`, `HttpRequest.newBuilder()`, `Locale.Builder`
- Lombok `@Builder`, Spring `UriComponentsBuilder`, `ResponseEntity.ok().header(...).body(...)`

## 6. Interview questions & model answers

**Q1. Builder vs constructor with many params?**  
Readable named steps, safe optional params, immutability, validation in one place.

**Q2. Is `StringBuilder` the Builder pattern?**  
Loosely - it builds a String step by step, but there is no separate Director/Product type; it's usually cited as an example.

**Q3. How to make the built object immutable and thread-safe?**  
Final fields, private constructor, copy mutable inputs (defensive copy) inside `build()`.

**Q4. What is the Director?**  
Optional class that knows common construction sequences (e.g. 'standard home loan'). Rarely needed in Java fluent builders.

**Q5. Records and builders?**  
Records give immutability but no named optional params; combine a record with a nested builder when there are many optional fields.

## 7. Pitfalls / when NOT to use

- Builder must not be reused after `build()` unless it resets state.
- Validate in `build()`, not in setters (cross-field rules).
- Duplicates fields - use Lombok if verbosity hurts.

## 8. Compare with similar patterns

**vs Factory:** Factory returns a product in one call; Builder assembles it over several. **vs Prototype:** build from scratch vs copy an existing one.

## 9. 30-second answer for the interview

"Builder is a creational pattern: separate the construction of a complex object from its representation, step by step. A typical case: A `LoanApplication` has 2 mandatory and many optional fields. Telescoping constructors (`new Loan(a,b,null,false,null)`) are unreadable; JavaBean setters leave the object half-built and mutable. The trade-off I'd mention: builder must not be reused after `build()` unless it resets state."

## 10. Learning resources

- [Refactoring.Guru - Builder](https://refactoring.guru/design-patterns/builder)
- [Baeldung - Builder pattern](https://www.baeldung.com/creational-design-patterns#builder)
- [Effective Java (Joshua Bloch) - Items on builders, singletons, static factories](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
