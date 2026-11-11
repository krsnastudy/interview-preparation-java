# 16. Iterator Pattern (Behavioral)

> **One-liner:** Provide a way to traverse a collection sequentially without exposing its internal representation.

## 1. The problem it solves

Clients need to walk a statement's transactions (oldest-first or newest-first) without knowing it's an `ArrayList`.

## 2. Structure (participants)

- **Iterator** - `hasNext()`, `next()`.
- **ConcreteIterator** - anonymous classes in the demo (forward, reverse).
- **Aggregate** - `Iterable<Txn>` returning iterators.

## 3. Real-life analogy

A TV remote's 'next channel' button - you don't care how channels are stored.

## 4. Code walkthrough

File: `src/behavioral/IteratorDemo.java` (self-contained, runnable)

`Statement implements Iterable<Txn>` so it works in the for-each loop; `newestFirst()` offers a second traversal strategy.

## 5. Where you've already seen it (JDK / Spring)

- `java.util.Iterator`, `Iterable`, `ListIterator`
- Fail-fast iterators throwing `ConcurrentModificationException`
- `Stream` as an internal iterator

## 6. Interview questions & model answers

**Q1. External vs internal iterator?**  
External: client drives (`hasNext/next`). Internal: collection drives (`forEach`, streams).

**Q2. Fail-fast vs fail-safe?**  
Fail-fast (`ArrayList`, `HashMap`) throws CME if modified during iteration via `modCount`. Fail-safe (`CopyOnWriteArrayList`, `ConcurrentHashMap`) iterate over a snapshot/weakly consistent view.

**Q3. How to remove while iterating?**  
`iterator.remove()` or `removeIf`, never `list.remove` inside for-each.

**Q4. Iterator vs ListIterator?**  
ListIterator is bidirectional, can `add/set` and gives indices.

## 7. Pitfalls / when NOT to use

- Modifying during iteration.
- Iterators are not thread-safe.

## 8. Compare with similar patterns

**vs Visitor:** iterator traverses; visitor applies operations per element type.

## 9. 30-second answer for the interview

"Iterator is a behavioral pattern: provide a way to traverse a collection sequentially without exposing its internal representation. A typical case: Clients need to walk a statement's transactions (oldest-first or newest-first) without knowing it's an `ArrayList`. The trade-off I'd mention: modifying during iteration."

## 10. Learning resources

- [Refactoring.Guru - Iterator](https://refactoring.guru/design-patterns/iterator)
- [Baeldung - Iterator pattern](https://www.baeldung.com/java-iterator)
- [Effective Java (Joshua Bloch) - Items on builders, singletons, static factories](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
