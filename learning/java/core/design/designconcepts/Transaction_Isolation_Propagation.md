# 📘 Transaction Isolation Levels & Propagation - InterviewHandbook
---
# 📖 What are Transaction Isolation Levels and Propagation?

Transactions ensure the **ACID** properties of a database operation.

Spring's `@Transactional` provides two important concepts:

- **Isolation** – Controls how one transaction is isolated from another concurrent transaction.
- **Propagation** – Defines how nested or multiple transactional methods interact.

> **Key Takeaway:** Isolation solves **concurrency problems**, while Propagation controls **transaction boundaries** between method calls.

---

# ❓ Why do we need them?

In enterprise applications, multiple users may update the same data simultaneously.

Example:

```
Customer A

↓

Updates Account Balance

↑

Customer B

↓

Reads Same Account
```

Without proper isolation:

- Dirty Reads
- Non-repeatable Reads
- Phantom Reads
- Lost Updates

Without proper propagation:

- Nested service calls may commit or rollback unexpectedly.

> **Key Takeaway:** Isolation ensures data consistency; Propagation ensures correct transaction flow.

---

# ⚙️ ACID Properties

| Property | Meaning |
|----------|---------|
| Atomicity | Complete or Rollback |
| Consistency | Data remains valid |
| Isolation | Transactions don't interfere |
| Durability | Committed data survives failures |

---

# ⚙️ Transaction Lifecycle

```
Method Invoked

↓

Spring AOP Proxy

↓

Begin Transaction

↓

Execute Business Logic

↓

Commit

OR

Rollback

↓

Release Connection
```

> **Key Takeaway:** Spring creates transactions through AOP proxies.

---

# 🏗 Isolation Levels

Spring supports five isolation levels.

---

## 1️⃣ DEFAULT

Uses the database's default isolation level.

Example:

MySQL → Repeatable Read

Oracle → Read Committed

Use when:

Most applications.

---

## 2️⃣ READ_UNCOMMITTED

Lowest isolation.

Allows reading uncommitted data.

Possible Issue

```
Dirty Read
```

Example

```
Transaction A

Updates Salary

↓

Not Committed

↓

Transaction B Reads Salary

↓

Transaction A Rollback
```

Transaction B read invalid data.

Rarely used.

---

## 3️⃣ READ_COMMITTED

Only committed data can be read.

Prevents

✅ Dirty Read

Still allows

❌ Non-repeatable Read

Example

```
Transaction A

Reads Balance = 100

↓

Transaction B

Updates Balance = 150

↓

Transaction A

Reads Again = 150
```

Two reads returned different values.

Default in Oracle.

---

## 4️⃣ REPEATABLE_READ

Same row always returns same value.

Prevents

✅ Dirty Read

✅ Non-repeatable Read

Still allows

❌ Phantom Read

Default in MySQL.

---

## 5️⃣ SERIALIZABLE

Highest isolation.

Transactions execute sequentially.

Prevents

✅ Dirty Read

✅ Non-repeatable Read

✅ Phantom Read

Drawback

- Slow
- Lock contention
- Reduced concurrency

Use only for critical operations.

---

# 📊 Isolation Comparison

| Isolation | Dirty Read | Non-repeatable | Phantom | Performance |
|------------|------------|----------------|----------|-------------|
| Read Uncommitted | ❌ Possible | ❌ Possible | ❌ Possible | ⭐⭐⭐⭐⭐ |
| Read Committed | ✅ Prevented | ❌ Possible | ❌ Possible | ⭐⭐⭐⭐ |
| Repeatable Read | ✅ Prevented | ✅ Prevented | ❌ Possible | ⭐⭐⭐ |
| Serializable | ✅ Prevented | ✅ Prevented | ✅ Prevented | ⭐ |

---

# ⚙️ Propagation Types

Propagation defines what happens when one transactional method calls another.

---

## REQUIRED (Default)

```
A()

↓

B()

↓

Same Transaction
```

If transaction exists

↓

Join it.

Else

↓

Create new transaction.

Most commonly used.

---

## REQUIRES_NEW

```
A()

↓

Suspend A

↓

Create New Transaction

↓

Commit

↓

Resume A
```

Independent transaction.

Useful for

- Audit Logging
- Notifications
- Email

---

## SUPPORTS

If transaction exists

↓

Join.

Else

↓

Execute without transaction.

Good for read-only methods.

---

## NOT_SUPPORTED

Suspend existing transaction.

Execute without transaction.

Useful for

- Report generation
- Bulk reads

---

## NEVER

Fails if transaction exists.

Throws exception.

Rarely used.

---

## MANDATORY

Transaction must already exist.

Else

↓

Exception.

Useful for enforcing transactional boundaries.

---

## NESTED

Creates Savepoint.

```
Main Transaction

↓

Savepoint

↓

Nested Transaction

↓

Rollback

↓

Main Continues
```

Requires database support.

---

# 📊 Propagation Comparison

| Propagation | Existing Transaction | New Transaction |
|--------------|----------------------|-----------------|
| REQUIRED | Join | Create |
| REQUIRES_NEW | Suspend | Create |
| SUPPORTS | Join | None |
| NOT_SUPPORTED | Suspend | None |
| NEVER | Exception | None |
| MANDATORY | Join | Exception |
| NESTED | Savepoint | Nested |

---

# 💻 Example

```java
@Service
public class PaymentService {

    @Transactional
    public void transfer() {

        debit();

        credit();

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void audit() {

        saveAudit();

    }

}
```

### Flow

```
Transfer

↓

Transaction A

↓

Audit

↓

Transaction B

↓

Commit

↓

Resume Transaction A
```

---

# 🏦 Real Production Example

### Online Banking

Customer transfers ₹10,000.

Steps

```
Debit Account

↓

Credit Account

↓

Audit Log

↓

SMS Notification
```

Debit & Credit

↓

Same Transaction.

Audit

↓

REQUIRES_NEW

SMS

↓

Async.

Even if SMS fails,

Money transfer should succeed.

---

# ⚠ Common Mistakes

❌ Using SERIALIZABLE everywhere.

❌ Calling `@Transactional` methods internally (self-invocation bypasses Spring proxies).

❌ Catching exceptions without rethrowing or marking rollback, causing unintended commits.

❌ Using transactions for long-running operations.

❌ Opening transactions before external API calls.

---

# ✅ Best Practices

- Keep transactions short.
- Perform only database work inside transactions.
- Use `READ_COMMITTED` for most applications.
- Use `REPEATABLE_READ` when consistency is important.
- Use `REQUIRES_NEW` for audit logging.
- Prefer `readOnly = true` for query methods.
- Roll back on business exceptions when appropriate.

---

# ⚡ Performance Considerations

Avoid

```
Transaction

↓

REST Call

↓

Kafka

↓

Sleep()

↓

Database
```

Better

```
REST

↓

Kafka

↓

Transaction

↓

Database

↓

Commit
```

Short transactions improve throughput and reduce lock contention.

---

# ⚖ Trade-offs

| Choice | Advantage | Disadvantage |
|---------|-----------|--------------|
| Read Committed | Better concurrency | Non-repeatable reads |
| Repeatable Read | Consistent reads | More locking |
| Serializable | Maximum consistency | Lowest performance |
| REQUIRED | Simple transaction flow | Shared rollback scope |
| REQUIRES_NEW | Independent commit/rollback | Additional overhead |

### Real-Life Analogy

Imagine a family shopping together.

**REQUIRED**

Everyone uses the same shopping cart.

If the cart is abandoned, everyone's purchases are lost.

**REQUIRES_NEW**

One person goes to another counter and pays separately.

Even if the main family's purchase is cancelled, that receipt is already completed.

---

# 🎯 Interview Questions

### 1. Difference between Isolation and Propagation?

**Answer**

Isolation controls visibility between concurrent transactions.

Propagation controls behavior when one transactional method invokes another.

---

### 2. Default Isolation Level?

**Answer**

`DEFAULT`, which delegates to the database configuration.

Examples:

- Oracle → READ_COMMITTED
- MySQL → REPEATABLE_READ

---

### 3. Most commonly used Propagation?

**Answer**

`REQUIRED`.

Joins an existing transaction or creates one if none exists.

---

### 4. Why use REQUIRES_NEW?

**Answer**

To execute an independent transaction, commonly used for audit logs, notifications, or history records.

---

### 5. Difference between REQUIRED and REQUIRES_NEW?

| REQUIRED | REQUIRES_NEW |
|-----------|--------------|
| Joins existing transaction | Creates a separate transaction |
| Rolls back with parent | Commits/rolls back independently |
| Default behavior | Used selectively |

---

### 6. Which isolation level prevents all read anomalies?

**Answer**

`SERIALIZABLE`, but it reduces concurrency and should be reserved for highly critical operations.

---

# 📌 When to Use

✅ Banking

✅ Financial Transactions

✅ Order Processing

✅ Inventory Updates

✅ Payment Systems

✅ Stock Trading

---

# 🚫 When NOT to Use

- Long-running batch jobs without careful design.
- External HTTP/API calls inside transactions.
- Report generation and analytics queries (prefer read-only, non-transactional approaches where appropriate).

---

# 💡 Interview Tips

Interviewers expect you to explain:

- ACID properties.
- Dirty Read, Non-repeatable Read, Phantom Read.
- Isolation vs Propagation.
- Why `REQUIRES_NEW` is used for audit logging.
- Why `READ_COMMITTED` is a common default.
- Why long-running transactions are harmful.
- The role of Spring AOP proxies in `@Transactional`.

Always support your explanation with a production example.

---

# 📝 Short Conclusion Summary

Transaction Isolation Levels control how concurrent transactions interact to maintain data consistency, while Propagation defines how transactions behave across nested service calls. Choosing the right combination balances consistency, performance, and scalability. In production systems, `READ_COMMITTED` (or the database default) with `REQUIRED` is common, while `REQUIRES_NEW` is typically used for independent operations such as audit logging.

---

# ⚡ TL;DR

| Topic | Summary |
|--------|---------|
| Isolation | Controls concurrency between transactions |
| Propagation | Controls transaction behavior across method calls |
| Default Isolation | Database default (`DEFAULT`) |
| Default Propagation | `REQUIRED` |
| Best For | Banking, Payments, Order Processing |
| Common Choice | READ_COMMITTED + REQUIRED |
| Independent Transaction | REQUIRES_NEW |
| Highest Isolation | SERIALIZABLE |
| Interview Keyword | Consistency vs Transaction Boundary |

---

# 🚀 30-Second Interview Answer

> "Isolation and Propagation solve two different problems in transaction management. Isolation defines how concurrent transactions interact and prevents anomalies like dirty reads or phantom reads, while Propagation determines how transactional methods behave when they call one another. In Spring, `REQUIRED` is the default propagation and joins an existing transaction, whereas `REQUIRES_NEW` starts an independent transaction. In production, I typically use the database's default isolation level with `REQUIRED`, and `REQUIRES_NEW` for audit logging or other operations that must commit independently."