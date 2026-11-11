# 14. Command Pattern (Behavioral)

> **One-liner:** Turn a request into a stand-alone object so it can be queued, logged, retried or undone.

## 1. The problem it solves

You need undo for account operations and an audit trail of what was executed.

## 2. Structure (participants)

- **Command** - `execute()` / `undo()`.
- **ConcreteCommand** - `DepositCommand`, `WithdrawCommand` (hold receiver + args).
- **Receiver** - `Account`.
- **Invoker** - keeps a history stack and runs commands.

## 3. Real-life analogy

A restaurant order slip: the waiter (invoker) doesn't cook; the slip carries the request to the kitchen and can be cancelled.

## 4. Code walkthrough

File: `src/behavioral/CommandDemo.java` (self-contained, runnable)

Deposit 1000, withdraw 300 -> balance 700; `undoLast()` reverses the withdrawal -> 1000. The same shape supports queues, retry and macro commands.

## 5. Where you've already seen it (JDK / Spring)

- `Runnable`, `Callable`, tasks submitted to `ExecutorService`
- `javax.swing.Action`
- Spring Batch `Tasklet`; CQRS 'command' messages

## 6. Interview questions & model answers

**Q1. How do you implement undo?**  
Each command stores enough state to reverse itself (or a memento); the invoker keeps a stack.

**Q2. Command vs Strategy?**  
Strategy = interchangeable *algorithm*. Command = a *request* as an object with receiver, args and optional undo.

**Q3. Why is `Runnable` a Command?**  
It encapsulates an action as an object passed to an invoker (thread/executor) that decides when to run it.

**Q4. Command in microservices?**  
CQRS commands, outbox/event messages, saga steps with compensating (undo) actions.

## 7. Pitfalls / when NOT to use

- Command class explosion - lambdas help.
- Undo must be idempotent/consistent with concurrent changes.

## 8. Compare with similar patterns

**vs Memento:** commands often use mementos for undo. **vs Chain:** chain routes; command encapsulates.

## 9. 30-second answer for the interview

"Command is a behavioral pattern: turn a request into a stand-alone object so it can be queued, logged, retried or undone. A typical case: You need undo for account operations and an audit trail of what was executed. The trade-off I'd mention: command class explosion - lambdas help."

## 10. Learning resources

- [Refactoring.Guru - Command](https://refactoring.guru/design-patterns/command)
- [Baeldung - Command pattern](https://www.baeldung.com/java-command-pattern)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [Head First Design Patterns (O'Reilly) - best beginner-friendly book](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
