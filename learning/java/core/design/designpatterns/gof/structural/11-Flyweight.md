# 11. Flyweight Pattern (Structural)

> **One-liner:** Share fine-grained immutable objects to save memory instead of creating many identical ones.

## 1. The problem it solves

A million orders each referencing `Instrument(isin, name, exchange)` - storing a copy per order wastes memory.

## 2. Structure (participants)

- **Flyweight** - immutable shared state (*intrinsic*): `Instrument`.
- **FlyweightFactory** - cache keyed by identity (`InstrumentFactory`).
- **Context** - holds *extrinsic* state (quantity, side) per use: `Order`.

## 3. Real-life analogy

Fonts in a text editor: one glyph object for 'e' referenced by thousands of characters; position/size is stored outside it.

## 4. Code walkthrough

File: `src/structural/FlyweightDemo.java` (self-contained, runnable)

1000 orders -> **1** `Instrument` object. The demo prints `distinct Instrument objects = 1` and `same reference? true`.

## 5. Where you've already seen it (JDK / Spring)

- `Integer.valueOf()` caches -128..127
- String constant pool / `String.intern()`
- `Boolean.valueOf()`, `Character.valueOf()`

## 6. Interview questions & model answers

**Q1. Intrinsic vs extrinsic state?**  
Intrinsic = shared, immutable, stored in the flyweight. Extrinsic = varies by context, passed in or stored by the client.

**Q2. Why must flyweights be immutable?**  
Many clients share them; a mutation would leak to all.

**Q3. Flyweight vs object pool?**  
Pool objects are checked out/in and exclusively used; flyweights are shared concurrently.

**Q4. Gotcha with `Integer` caching?**  
`Integer a=127,b=127; a==b` is true, `128==128` is false - compare with `equals`.

## 7. Pitfalls / when NOT to use

- Cache growth without eviction -> memory leak (use `WeakHashMap`/size limits).
- Complexity not worth it for small data sets.

## 8. Compare with similar patterns

**vs Singleton:** one instance vs a keyed pool of shared instances. **vs Prototype:** copy vs share.

## 9. 30-second answer for the interview

"Flyweight is a structural pattern: share fine-grained immutable objects to save memory instead of creating many identical ones. A typical case: A million orders each referencing `Instrument(isin, name, exchange)` - storing a copy per order wastes memory. The trade-off I'd mention: cache growth without eviction -> memory leak (use `WeakHashMap`/size limits)."

## 10. Learning resources

- [Refactoring.Guru - Flyweight](https://refactoring.guru/design-patterns/flyweight)
- [Baeldung - Flyweight pattern](https://www.baeldung.com/java-flyweight)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
