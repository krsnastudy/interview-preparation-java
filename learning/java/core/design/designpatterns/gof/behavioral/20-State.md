# 20. State Pattern (Behavioral)

> **One-liner:** Let an object alter its behaviour when its internal state changes - it appears to change class.

## 1. The problem it solves

An account behaves differently when Active vs Frozen. Huge `switch(state)` blocks scatter across every method.

## 2. Structure (participants)

- **Context** - `Account`.
- **State** - `AccountState` interface.
- **ConcreteStates** - `Active`, `Frozen`; each method returns the *next* state.

## 3. Real-life analogy

A traffic light or a vending machine: same button press, different outcome depending on current state.

## 4. Code walkthrough

File: `src/behavioral/StateDemo.java` (self-contained, runnable)

withdraw (ok) -> freeze -> withdraw (denied) -> unfreeze -> withdraw (ok). Transition logic lives in the state classes, not in `Account`.

## 5. Where you've already seen it (JDK / Spring)

- Spring State Machine
- TCP connection states
- Workflow/BPM engines, payment/order lifecycles

## 6. Interview questions & model answers

**Q1. State vs Strategy?**  
Same structure. States know about each other and *transition* themselves; strategies are independent and chosen by the client.

**Q2. Who triggers transitions?**  
Either the context or the states themselves (demo: states return the next state).

**Q3. Stateless state objects?**  
If states hold no data they can be singletons/enums (flyweight).

**Q4. When prefer an enum + switch?**  
Few states and simple transitions - the pattern is overkill.

## 7. Pitfalls / when NOT to use

- Many small classes.
- Transitions scattered across states are hard to see - document with a diagram.

## 8. Compare with similar patterns

**vs Strategy:** self-transitioning vs client-chosen. **vs Flyweight:** stateless states can be shared.

## 9. 30-second answer for the interview

"State is a behavioral pattern: let an object alter its behaviour when its internal state changes - it appears to change class. A typical case: An account behaves differently when Active vs Frozen. Huge `switch(state)` blocks scatter across every method. The trade-off I'd mention: many small classes."

## 10. Learning resources

- [Refactoring.Guru - State](https://refactoring.guru/design-patterns/state)
- [Baeldung - State pattern](https://www.baeldung.com/java-state-design-pattern)
- [Spring Statemachine](https://spring.io/projects/spring-statemachine)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
