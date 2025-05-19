Excellent and **very important Core Java interview question**!  
This is a favorite for **Java concurrency and multithreading rounds**. Let's break it down clearly.

---

# 🚀 Question:
**Why is `wait()` method in `Object` class, and not in `Thread` class?**

---

# 🧠 Understanding the Concepts:

| Concept | Explanation |
|:---|:---|
| `wait()` | Makes a **thread** wait and **releases the lock** it holds on the **object**. |
| Locking | In Java, **every object** can have a lock (monitor), not just threads. |
| Synchronization | Threads synchronize **on objects**, not on other threads. |

---

# ✅ Real Reason

> **Because `wait()` operates on the lock of an OBJECT, not on the Thread itself.**

🔵 **Details:**

- In Java, **every object** has an **intrinsic lock** (also called a monitor lock).
- When you `synchronize` on an object:
  ```java
  synchronized (someObject) {
     // critical section
  }
  ```
  you are locking **someObject's** monitor, not the Thread.

- The `wait()` method is used when a **Thread wants to release the lock on that OBJECT** and move into **waiting state** until another thread notifies on the same object.

🔴 **If `wait()` were in Thread class**, it would imply that you are waiting on a thread's lock, but Java's concurrency model is based on **object monitors** — not on threads.

Hence:
- `wait()`, `notify()`, and `notifyAll()` are methods of **Object**, not Thread.

---

# 🧩 How it flows:

```java
synchronized (object) {
    object.wait(); // Waits on the object's monitor
}
```
- **Thread** waits, but **monitor belongs to the Object**.

---

# 🔥 Key Points for Interview

- Java uses **object-level locks**, not thread-level locks.
- **wait()**, **notify()**, and **notifyAll()** must be called on objects.
- Thread holds a lock on an **object**, and `wait()` releases that object's lock.
- If `wait()` was in Thread class, it would break Java’s synchronization model (which revolves around object monitors).

---

# 📜 Short Interview Answer

> "`wait()` is in Object class because in Java, every object has an intrinsic lock (monitor), and threads acquire locks on objects, not on other threads. Since `wait()` is intended to release the object's lock and make the thread wait, it naturally belongs to Object class, not Thread class."

---

# ⚡ TL;DR:

| Aspect | Why |
|:---|:---|
| Lock Ownership | Objects own locks (monitors), not threads. |
| Synchronization | Synchronization happens on objects. |
| API Design | `wait()` acts on an object’s monitor, so it belongs in Object class. |

---
