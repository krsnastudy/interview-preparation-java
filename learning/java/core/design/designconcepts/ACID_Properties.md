# 📘 ACID Properties

> **InterviewHandbook**

## 📖 What are ACID Properties?

ACID stands for:
- **Atomicity**
- **Consistency**
- **Isolation**
- **Durability**

ACID properties ensure reliable database transactions.

## ❓ Why do we need it?

- Prevent data corruption
- Maintain data integrity
- Support concurrent users
- Enable transaction rollback on failures

## ⚙️ Internal Working

```
Application
     |
BEGIN TRANSACTION
     |
 Execute SQL
     |
Success? ---- No ----> ROLLBACK
     |
    Yes
     |
   COMMIT
```

## 🏗 Components

- Transaction Manager
- Lock Manager
- Write Ahead Log (WAL)
- Buffer Manager
- Recovery Manager

## 🔄 Visual Flow

```
Client
  |
Service
  |
Transaction
  |
Database
```

## 💻 Spring Boot Example

```java
@Transactional
public void transfer(...) {
    debit();
    credit();
}
```

## 🏦 Production Example

Online banking fund transfer must either debit and credit successfully or rollback both operations.

## ⚠ Common Mistakes

- Long-running transactions
- Missing rollback handling
- Wrong isolation level

## ✅ Best Practices

- Keep transactions short
- Use proper isolation
- Monitor deadlocks
- Log transaction failures

## ⚡ Performance

| Property | Impact |
|---|---|
| Atomicity | Logging overhead |
| Consistency | Constraint validation |
| Isolation | Lock contention |
| Durability | Disk I/O |

## ⚖ Trade-offs

Higher isolation improves correctness but reduces concurrency.

## 📊 Comparison

| Property | Purpose |
|---|---|
| Atomicity | All or nothing |
| Consistency | Valid state |
| Isolation | Concurrent safety |
| Durability | Persistent commit |

## 🎯 Interview Questions

1. What is ACID?
2. Difference between Consistency and Isolation?
3. How does Spring implement transactions?
4. What causes deadlocks?
5. Which isolation level is default?

## 🧠 Scenario

How would you ensure money transfer never loses data during server failure?

## 🛠 Debugging Tips

- Enable SQL logs
- Check deadlocks
- Inspect transaction boundaries

## 📌 When to Use

- Banking
- Payments
- E-commerce
- Loan processing

## 🚫 When NOT to Use

- Eventual consistency systems
- High-throughput analytics

## 💡 Interview Tips

Explain each property with a banking example.

## 📝 Summary

ACID guarantees reliable, consistent and durable database transactions.

## ⚡ TL;DR

| Topic | Summary |
|---|---|
| Purpose | Reliable transactions |
| Best For | OLTP |
| Avoid | Analytics pipelines |

## 🚀 30-Second Interview Answer

ACID is a set of transaction properties—Atomicity, Consistency, Isolation, and Durability—that ensures database operations remain reliable even during failures and concurrent access. It is fundamental for banking and enterprise systems where data integrity is critical.
