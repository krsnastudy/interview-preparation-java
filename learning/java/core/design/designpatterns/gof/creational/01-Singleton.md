# 1. Singleton Pattern (Creational)

> **One-liner:** Guarantee exactly one instance of a class and give a global access point to it.

## 1. The problem it solves

Some things must exist once per JVM/classloader: a config registry, a connection pool manager, a metrics registry. Creating many copies wastes resources or causes inconsistent state.

## 2. Structure (participants)

- Private constructor - nobody else can call `new`.
- A static field holding the single instance.
- A static accessor (`getInstance()`) or an `enum` constant.

## 3. Real-life analogy

The Reserve Bank governor's office: there is one, everyone who needs it calls the same address.

## 4. Code walkthrough

File: `src/creational/SingletonDemo.java` (self-contained, runnable)

`SingletonDemo` shows three flavours: **enum** (`ConfigRegistry.INSTANCE`), **initialization-on-demand holder** (lazy, thread-safe via class loading), and **double-checked locking** with `volatile`.

BFSI angle: a `RateCardRegistry` loaded once at startup that every pricing service reads.

## 5. Where you've already seen it (JDK / Spring)

- `Runtime.getRuntime()`
- Spring beans - default scope is *singleton per ApplicationContext* (not the GoF 'per JVM' guarantee)

## 6. Interview questions & model answers

**Q1. Why is `volatile` needed in double-checked locking?**  
Without it the JIT/CPU may reorder `instance = new X()` so another thread sees a non-null reference to a *partially constructed* object. `volatile` adds the happens-before edge (Java 5+ memory model).

**Q2. Which is the best singleton in Java and why?**  
The `enum` singleton (Bloch). JVM guarantees one instance, it is serialization-safe (no new instance on deserialization) and reflection-safe (cannot `newInstance` an enum).

**Q3. How can a singleton be broken?**  
Reflection (call private ctor via `setAccessible`), serialization (needs `readResolve`), cloning (override `clone` to throw), multiple classloaders (one instance *per classloader*).

**Q4. Is a Spring singleton bean the Singleton pattern?**  
No - it is one instance per container, managed by IoC. The class itself can still be instantiated elsewhere. Advantage: easy to mock/test, no static global state.

**Q5. Why do people call Singleton an anti-pattern?**  
Hidden global state, hard to unit test, tight coupling, concurrency pitfalls. Prefer DI-managed singletons.

## 7. Pitfalls / when NOT to use

- Mutable singleton state needs thread safety (use immutable or concurrent structures).
- Lazy `synchronized getInstance()` on every call is a needless bottleneck.
- Singletons that hold DB connections/etc. complicate testing - inject an interface instead.

## 8. Compare with similar patterns

**vs Static class:** a singleton can implement interfaces, be lazily created and be swapped in tests. **vs Flyweight:** Singleton = one object; Flyweight = a pool of shared objects keyed by state.

## 9. 30-second answer for the interview

"Singleton is a creational pattern: guarantee exactly one instance of a class and give a global access point to it. A typical case: Some things must exist once per JVM/classloader: a config registry, a connection pool manager, a metrics registry. Creating many copies wastes resources or causes inconsistent state. The trade-off I'd mention: mutable singleton state needs thread safety (use immutable or concurrent structures)."

## 10. Learning resources

- [Refactoring.Guru - Singleton](https://refactoring.guru/design-patterns/singleton)
- [Baeldung - Java Singleton](https://www.baeldung.com/java-singleton)
- [Effective Java (Joshua Bloch) - Items on builders, singletons, static factories](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
