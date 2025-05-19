Here's a clear explanation of **Pessimistic vs Optimistic Locking** in Java (typically JPA/Hibernate):

---

### 🔐 Pessimistic Locking

**Definition:**
It assumes **conflicts are likely**, so it locks the record when it's read. Other transactions must wait or fail.

**Use case:**
Used when you expect **high contention** on data (e.g., booking system, inventory updates).

**How it works:**

* Locks the row in DB (SELECT ... FOR UPDATE)
* Other transactions can't read/write until the lock is released.

**JPA Example:**

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT p FROM Product p WHERE p.id = :id")
Product findWithLock(@Param("id") Long id);
```

---

### 💡 Optimistic Locking

**Definition:**
Assumes **conflicts are rare**, so it checks for changes during the update using a version field.

**Use case:**
Used when you expect **low contention** (e.g., user profile updates).

**How it works:**

* Adds a `@Version` field in entity.
* On update, it checks if version matches DB.
* If not, it throws `OptimisticLockException`.

**JPA Example:**

```java
@Entity
public class Product {
    @Id
    private Long id;

    @Version
    private int version;
}
```

---

### 🆚 Key Differences

| Feature           | Pessimistic Locking       | Optimistic Locking         |
| ----------------- | ------------------------- | -------------------------- |
| Lock Type         | Database row lock         | In-memory version check    |
| Performance       | Slower (due to blocking)  | Faster (no blocking)       |
| Conflict Handling | Prevents conflicts        | Detects & throws exception |
| Suitable For      | High contention use-cases | Low contention use-cases   |

---

### 💬 Interview Short Answer:

> **Pessimistic locking** locks the DB record on read to avoid conflicts, used in high-concurrency cases.
> **Optimistic locking** uses a version field to detect changes at update time, more efficient for low-conflict scenarios.

---
