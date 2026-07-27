# 📘 Saga Design Pattern in Spring Boot Microservices

Short introduction explaining the Saga Design Pattern.

---

## 📖 What is Saga Design Pattern?

### Definition

Saga is a design pattern used to manage distributed transactions across multiple microservices.

### Why it exists

To maintain data consistency without using a distributed two-phase commit (2PC).

### Problem it solves

- Distributed transaction management
- Service independence
- Data consistency across services

### Key Characteristics

- Sequence of local transactions
- Each service updates its own database
- Uses compensating transactions for rollback
- Event-driven or orchestration-based

**Key Takeaway**

Saga manages distributed transactions by executing local transactions and compensating failed operations.

---

## ❓ Why do we need it?

### Business Problem

In microservices, multiple services participate in one business transaction.

Example:

- Order Service
- Payment Service
- Inventory Service
- Shipping Service

If one service fails after another succeeds, data becomes inconsistent.

### Technical Problem

Traditional database transactions cannot span multiple databases.

### Advantages

- Loose coupling
- Independent services
- Better scalability
- No distributed locking

### Limitations of previous approaches

- Two-Phase Commit reduces availability
- Tight coupling
- Poor scalability

**Key Takeaway**

Saga provides eventual consistency across multiple microservices.

---

## ⚙️ Internal Working

Step 1

Client sends request.

↓

Step 2

Order Service creates order.

↓

Step 3

Payment Service processes payment.

↓

Step 4

Inventory Service reserves stock.

↓

Step 5

Shipping Service schedules delivery.

If any step fails,

Compensating transactions execute.

Example
