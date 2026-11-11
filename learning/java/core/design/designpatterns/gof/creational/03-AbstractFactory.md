# 3. Abstract Factory Pattern (Creational)

> **One-liner:** Provide an interface for creating *families* of related objects without naming their concrete classes.

## 1. The problem it solves

A bank onboarding in India needs PAN/Aadhaar KYC and 30% tax; the US needs SSN and 24%. The objects must be consistent *as a set* - never mix Indian KYC with US tax.

## 2. Structure (participants)

- **AbstractFactory** - `RegionFactory` with `kyc()` and `tax()`.
- **ConcreteFactory** - `IndiaFactory`, `UsFactory`.
- **AbstractProduct** - `KycVerifier`, `TaxCalculator`.
- **Client** - uses only the abstract types.

## 3. Real-life analogy

IKEA furniture lines: choose 'Modern' or 'Classic' once and every chair, table and lamp you get matches.

## 4. Code walkthrough

File: `src/creational/AbstractFactoryDemo.java` (self-contained, runnable)

`onboard(RegionFactory f, ...)` works for any region. Adding a `UkFactory` needs zero changes to `onboard`. Swapping the factory swaps the *whole family* atomically.

## 5. Where you've already seen it (JDK / Spring)

- `javax.xml.parsers.DocumentBuilderFactory`, `TransformerFactory`
- JDBC: a `Connection` creates a matching family of `Statement`/`ResultSet`
- Spring profiles choosing a whole set of beans per environment

## 6. Interview questions & model answers

**Q1. When use Abstract Factory over Factory Method?**  
When products come in families that must be consistent (UI themes, regional compliance, DB-vendor-specific DAOs).

**Q2. Main drawback?**  
Adding a *new product type* (e.g. `fees()`) forces changes in every factory and interface.

**Q3. How does it support Open/Closed?**  
New families (regions) are added by new factory classes; client code never changes.

**Q4. Spring-world equivalent?**  
`@Profile`/`@ConditionalOnProperty` configuration classes that each define a consistent set of beans.

## 7. Pitfalls / when NOT to use

- Heavy boilerplate for small problems.
- Rigid when the product set changes often.

## 8. Compare with similar patterns

**vs Factory Method:** family vs single product. Abstract Factories are often *implemented with* factory methods.

## 9. 30-second answer for the interview

"Abstract Factory is a creational pattern: provide an interface for creating *families* of related objects without naming their concrete classes. A typical case: A bank onboarding in India needs PAN/Aadhaar KYC and 30% tax; the US needs SSN and 24%. The objects must be consistent *as a set* - never mix Indian KYC with US tax. The trade-off I'd mention: heavy boilerplate for small problems."

## 10. Learning resources

- [Refactoring.Guru - Abstract Factory](https://refactoring.guru/design-patterns/abstract-factory)
- [Baeldung - Abstract Factory](https://www.baeldung.com/java-abstract-factory-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
