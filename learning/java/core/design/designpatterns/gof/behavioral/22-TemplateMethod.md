# 22. Template Method Pattern (Behavioral)

> **One-liner:** Define the skeleton of an algorithm in a base class and let subclasses override specific steps.

## 1. The problem it solves

CSV and HTML reports share the same flow (header, rows, footer) but differ in formatting. Don't duplicate the flow.

## 2. Structure (participants)

- **AbstractClass** - `ReportGenerator` with a `final generate()` template.
- **Abstract steps** - `header()`, `row()`.
- **Hook** - `footer()` with a default.
- **ConcreteClasses** - `CsvReport`, `HtmlReport`.

## 3. Real-life analogy

A recipe outline: 'prepare, cook, serve' is fixed; each dish fills in the details. (Hollywood principle: *don't call us, we'll call you*.)

## 4. Code walkthrough

File: `src/behavioral/TemplateMethodDemo.java` (self-contained, runnable)

`generate(rows)` is final so subclasses can't reorder steps; `HtmlReport` overrides the optional `footer` hook, `CsvReport` uses the default.

## 5. Where you've already seen it (JDK / Spring)

- `HttpServlet.service()` -> `doGet/doPost`
- `AbstractList`/`AbstractMap`
- Spring `JdbcTemplate`, `RestTemplate` (template + callbacks)
- JUnit `@BeforeEach`/test lifecycle

## 6. Interview questions & model answers

**Q1. Why make the template method `final`?**  
To protect the invariant algorithm order from being overridden.

**Q2. Hook vs abstract method?**  
Abstract: subclass *must* implement. Hook: optional override with a sensible default.

**Q3. Template Method vs Strategy?**  
Inheritance + compile-time variation vs composition + runtime swap. Prefer Strategy when flexibility matters.

**Q4. How does it relate to the Hollywood principle?**  
The base class controls flow and calls subclass code - inversion of control.

## 7. Pitfalls / when NOT to use

- Deep inheritance hierarchies.
- Liskov violations when subclasses break the expected flow.

## 8. Compare with similar patterns

**vs Factory Method:** factory methods are often steps within a template method.

## 9. 30-second answer for the interview

"Template Method is a behavioral pattern: define the skeleton of an algorithm in a base class and let subclasses override specific steps. A typical case: CSV and HTML reports share the same flow (header, rows, footer) but differ in formatting. Don't duplicate the flow. The trade-off I'd mention: deep inheritance hierarchies."

## 10. Learning resources

- [Refactoring.Guru - Template Method](https://refactoring.guru/design-patterns/template-method)
- [Baeldung - Template Method pattern](https://www.baeldung.com/java-template-method-pattern)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
