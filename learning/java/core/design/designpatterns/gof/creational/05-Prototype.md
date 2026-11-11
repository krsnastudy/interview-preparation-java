# 5. Prototype Pattern (Creational)

> **One-liner:** Create new objects by copying (cloning) an existing, pre-configured instance.

## 1. The problem it solves

Setting up a `TradeTemplate` (instrument, approvers, limits) is costly or complex. Copy a ready-made one and tweak it instead of rebuilding from scratch.

## 2. Structure (participants)

- **Prototype** - declares `clone()` (here `Cloneable`).
- **ConcretePrototype** - implements the copy, taking care of **deep vs shallow**.
- **Client** - asks the prototype to copy itself.

## 3. Real-life analogy

Photocopying a filled-in form and changing only the name, rather than retyping the whole form.

## 4. Code walkthrough

File: `src/creational/PrototypeDemo.java` (self-contained, runnable)

`TradeTemplate.clone()` calls `super.clone()` and then **re-creates the `approvers` list**. Mutating the clone's list does not affect the original - that's the deep-copy lesson interviewers probe.

## 5. Where you've already seen it (JDK / Spring)

- `Object.clone()`, `ArrayList.clone()`, `HashMap.clone()` (shallow)
- Spring `@Scope("prototype")` - new bean per request (different meaning, related spirit)

## 6. Interview questions & model answers

**Q1. Shallow vs deep copy?**  
Shallow copies field values (references shared). Deep copies nested mutable objects too. `Object.clone()` is shallow by default.

**Q2. Why is `Cloneable` considered broken?**  
It has no `clone()` method, `Object.clone()` is protected and bypasses constructors, and it conflicts with final fields (Bloch Item 13).

**Q3. Better alternatives to `clone()`?**  
Copy constructor or static copy factory (`new TradeTemplate(other)`), or serialization-based deep copy.

**Q4. Spring prototype scope?**  
Each `getBean()`/injection gets a new instance - container does *not* manage its destruction.

## 7. Pitfalls / when NOT to use

- Forgetting to deep-copy mutable fields.
- Circular references make deep copy hard.
- `clone()` skips constructor validation.

## 8. Compare with similar patterns

**vs Factory:** Prototype copies an existing instance; Factory constructs a new one. Often combined with a registry of prototypes.

## 9. 30-second answer for the interview

"Prototype is a creational pattern: create new objects by copying (cloning) an existing, pre-configured instance. A typical case: Setting up a `TradeTemplate` (instrument, approvers, limits) is costly or complex. Copy a ready-made one and tweak it instead of rebuilding from scratch. The trade-off I'd mention: forgetting to deep-copy mutable fields."

## 10. Learning resources

- [Refactoring.Guru - Prototype](https://refactoring.guru/design-patterns/prototype)
- [Baeldung - Clone an object](https://www.baeldung.com/java-deep-copy)
- [Effective Java (Joshua Bloch) - Items on builders, singletons, static factories](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
