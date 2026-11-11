# 17. Mediator Pattern (Behavioral)

> **One-liner:** Centralise complex communication between objects so they don't reference each other directly.

## 1. The problem it solves

Traders talking directly to every other trader gives N x N dependencies. Colleagues should only know the desk.

## 2. Structure (participants)

- **Mediator** - `Desk` / `TradingDesk`.
- **Colleague** - `Trader` knows only the mediator.

## 3. Real-life analogy

Air-traffic control: planes never talk to each other, only to the tower.

## 4. Code walkthrough

File: `src/behavioral/MediatorDemo.java` (self-contained, runnable)

`Asha.send(...)` calls `desk.broadcast`, which forwards to everyone except the sender. New colleagues just register with the desk.

## 5. Where you've already seen it (JDK / Spring)

- Spring `ApplicationEventPublisher` / event bus
- `DispatcherServlet` (front controller mediating controllers/views)
- Message brokers (Kafka/RabbitMQ) in system design

## 6. Interview questions & model answers

**Q1. Mediator vs Observer?**  
Observer is one-to-many broadcast from a subject. Mediator is many-to-many coordination logic living in one place.

**Q2. Mediator vs Facade?**  
Facade is one-way and subsystems are unaware; Mediator is two-way and colleagues know it.

**Q3. Downside?**  
The mediator can become a God object holding all coordination logic.

**Q4. Microservice analogy?**  
An orchestrator (saga coordinator) vs choreography via events.

## 7. Pitfalls / when NOT to use

- God-class mediator.
- Hidden control flow - hard to trace.

## 8. Compare with similar patterns

**vs Observer:** often implemented with observers; intent differs.

## 9. 30-second answer for the interview

"Mediator is a behavioral pattern: centralise complex communication between objects so they don't reference each other directly. A typical case: Traders talking directly to every other trader gives N x N dependencies. Colleagues should only know the desk. The trade-off I'd mention: god-class mediator."

## 10. Learning resources

- [Refactoring.Guru - Mediator](https://refactoring.guru/design-patterns/mediator)
- [Baeldung - Mediator pattern](https://www.baeldung.com/java-mediator-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
