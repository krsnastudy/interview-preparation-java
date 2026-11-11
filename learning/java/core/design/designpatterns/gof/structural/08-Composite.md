# 8. Composite Pattern (Structural)

> **One-liner:** Compose objects into tree structures and let clients treat single objects and groups uniformly.

## 1. The problem it solves

A client portfolio contains stocks, bonds *and* sub-portfolios. Valuing it should be one call regardless of the nesting.

## 2. Structure (participants)

- **Component** - `Holding` with `value()`.
- **Leaf** - `Stock`.
- **Composite** - `Portfolio` that holds children and delegates (`sum` of children).

## 3. Real-life analogy

A file system: a folder can contain files and other folders, and 'size' works on both.

## 4. Code walkthrough

File: `src/structural/CompositeDemo.java` (self-contained, runnable)

`Portfolio all` contains `Equities` (2 stocks), `Bonds` (1 holding) and a `CASH` stock. `all.value()` recursively sums everything; `print()` draws the tree.

## 5. Where you've already seen it (JDK / Spring)

- `java.awt.Container`/`Component` (Swing)
- JSF `UIComponent` trees
- Org-chart / menu / permission hierarchies in apps

## 6. Interview questions & model answers

**Q1. Why does Composite violate Interface Segregation sometimes?**  
Leaves inherit `add/remove` they can't support. Solution: keep child management only on the Composite (type-safe) or throw `UnsupportedOperationException`.

**Q2. Complexity of operations?**  
O(n) over the tree for aggregate operations like `value()`; cache if the tree is large and immutable.

**Q3. How to avoid cycles?**  
Track visited nodes or forbid adding an ancestor as a child.

**Q4. Composite + Visitor?**  
Common pairing - Visitor adds operations over a composite tree without changing node classes.

## 7. Pitfalls / when NOT to use

- Overly general component interface.
- Cycles in the structure cause stack overflow.

## 8. Compare with similar patterns

**vs Decorator:** same recursive shape; Decorator wraps *one* child to add behaviour, Composite aggregates *many* children.

## 9. 30-second answer for the interview

"Composite is a structural pattern: compose objects into tree structures and let clients treat single objects and groups uniformly. A typical case: A client portfolio contains stocks, bonds *and* sub-portfolios. Valuing it should be one call regardless of the nesting. The trade-off I'd mention: overly general component interface."

## 10. Learning resources

- [Refactoring.Guru - Composite](https://refactoring.guru/design-patterns/composite)
- [Baeldung - Composite pattern](https://www.baeldung.com/java-composite-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
