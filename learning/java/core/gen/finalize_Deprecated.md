## 🧹 What is `finalize()` in Java?

### 🧾 Definition:
`finalize()` is a method defined in `java.lang.Object` that was meant to be **overridden** to perform **cleanup** before an object is garbage collected.

```java
@Override
protected void finalize() throws Throwable {
    // cleanup logic
}
```

---

### 🔄 When is it called?

- The **GC (Garbage Collector)** calls `finalize()` **once** on an unreachable object **before** reclaiming its memory.

---

### 🧠 Common Use-Cases (historically):
- Releasing non-memory resources (e.g., file handles, sockets)
- Logging object destruction
- Cleanup if you forgot to close resources

---

## ❌ Why is `finalize()` Deprecated (since Java 9 and removed in Java 18)?

### 🚨 Problems with `finalize()`:

| Issue | Explanation |
|-------|-------------|
| ❌ Unpredictable | No guarantee when (or even if) it runs |
| ⏳ Performance hit | GC has to do **finalization queue tracking** |
| 🔄 Resurrection risk | Object can be made reachable again inside `finalize()` |
| 🧪 Hard to test | Non-deterministic behavior makes unit tests unreliable |
| 🧨 Errors ignored | Exceptions inside `finalize()` are swallowed silently |

---

### 📛 Deprecated in Java 9:

> Java 9 introduced `@Deprecated` on `finalize()` and stated:
>
> _"finalization is inherently problematic and unreliable. New code should avoid using it."_

---

## ✅ What to Use Instead?

| Task | Recommended Replacement |
|------|--------------------------|
| Cleaning up resources | Use **`try-with-resources`** and **`AutoCloseable`** |
| Object cleanup on GC | Use **`java.lang.ref.Cleaner`** (Java 9+) |
| Tracking GC activity | Use **profilers or JFR (Java Flight Recorder)** |

---

### 🔁 `Cleaner` Example:

```java
public class MyResource {
    private static final Cleaner cleaner = Cleaner.create();

    private static class State implements Runnable {
        @Override
        public void run() {
            System.out.println("Cleaned up!");
        }
    }

    private final Cleaner.Cleanable cleanable;

    public MyResource() {
        cleanable = cleaner.register(this, new State());
    }
}
```

This is safer, runs off the main thread, and has **no resurrection risk**.

---

## 🧠 TL;DR

- `finalize()` was a **legacy cleanup method** called by GC before object destruction.
- It’s **deprecated** due to **unpredictability, performance cost, and safety concerns**.
- Use **`try-with-resources`**, **`AutoCloseable`**, or **`Cleaner`** (Java 9+) instead.
