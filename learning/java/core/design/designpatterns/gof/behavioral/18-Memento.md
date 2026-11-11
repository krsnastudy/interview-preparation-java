# 18. Memento Pattern (Behavioral)

> **One-liner:** Capture and externalise an object's state so it can be restored later without breaking encapsulation.

## 1. The problem it solves

A multi-step loan form needs 'back' / undo to earlier saved states.

## 2. Structure (participants)

- **Originator** - `LoanForm` (creates and restores snapshots).
- **Memento** - immutable `Snapshot` record.
- **Caretaker** - the `Deque` of snapshots; never looks inside.

## 3. Real-life analogy

A video-game save point.

## 4. Code walkthrough

File: `src/behavioral/MementoDemo.java` (self-contained, runnable)

Two snapshots saved, form corrupted with bad data, `restore(history.pop())` returns to the last good state, then the one before. A `record` keeps the memento immutable.

## 5. Where you've already seen it (JDK / Spring)

- Serialization-based snapshots
- Hibernate keeps a loaded-state snapshot to compute dirty checks
- Editor undo stacks

## 6. Interview questions & model answers

**Q1. Memento vs Command for undo?**  
Memento stores *state*; Command stores *the operation* and its inverse. Memory vs complexity trade-off.

**Q2. Why not make state public?**  
Memento preserves encapsulation: only the originator interprets the snapshot.

**Q3. Memory concerns?**  
Large states/many snapshots - cap history, store diffs, or use copy-on-write.

**Q4. Make mementos safe?**  
Immutable (record), defensive copies of mutable fields.

## 7. Pitfalls / when NOT to use

- Memory growth.
- Snapshots of mutable references need deep copy.

## 8. Compare with similar patterns

**vs Prototype:** Prototype copies to create new objects; Memento stores state to restore the same object.

## 9. 30-second answer for the interview

"Memento is a behavioral pattern: capture and externalise an object's state so it can be restored later without breaking encapsulation. A typical case: A multi-step loan form needs 'back' / undo to earlier saved states. The trade-off I'd mention: memory growth."

## 10. Learning resources

- [Refactoring.Guru - Memento](https://refactoring.guru/design-patterns/memento)
- [Baeldung - Memento pattern](https://www.baeldung.com/java-memento-design-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
