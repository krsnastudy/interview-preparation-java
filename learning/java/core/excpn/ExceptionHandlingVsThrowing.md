## ⚙️ Exception Handling vs. Throwing

### ✅ Declaring and using `try-catch` blocks (without throwing):
- **Negligible performance overhead**.
- JVM optimizes normal `try-catch` flow very efficiently.
- You **can and should use** `try-catch` for expected error paths.

```java
try {
    int a = 10 / 2;  // Fast path, no real cost
} catch (ArithmeticException e) {
    e.printStackTrace();
}
```

### ❌ Actually throwing an exception:
- **Expensive** due to:
    - Creating the exception object
    - Capturing the full **stack trace**
    - Walking the call stack to find a handler
- Slower than typical control flow (by **orders of magnitude**)

```java
throw new RuntimeException("Boom!");  // 🔥 Very costly
```

⏱️ Benchmarks show exception throwing is **10x–100x slower** than if-statements for error checks.

---

## 🧠 Why Is It Expensive?

1. **Object creation** (heap allocation)
2. **Stack trace capture** (walk the thread’s stack)
3. **Search for handler** (unwinding frames)

---

## 🧪 Microbenchmark Example

```java
public class ExceptionPerf {
    public static void main(String[] args) {
        long t1 = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            try {
                int x = 10 / 2;  // fast
            } catch (ArithmeticException e) {}
        }
        long t2 = System.nanoTime();
        System.out.println("No exception: " + (t2 - t1));

        t1 = System.nanoTime();
        for (int i = 0; i < 1_000; i++) {
            try {
                throw new Exception();  // expensive
            } catch (Exception e) {}
        }
        t2 = System.nanoTime();
        System.out.println("With exception: " + (t2 - t1));
    }
}
```

---

## ✅ Best Practices

| Do | Don’t |
|----|-------|
| Use `try-catch` for **unexpected failures** | ❌ Don’t use exceptions for regular control flow |
| Validate inputs before risky operations | ❌ Avoid throwing exceptions in performance-critical loops |
| Use tools like `Optional` or return codes for expected errors | ❌ Don’t swallow exceptions without logging |

---

## 🔥 TL;DR

- ❌ **Throwing exceptions is slow** (due to stack trace and call stack walking).
- ✅ **Using `try-catch` without throwing is cheap**.
- 🔁 Don’t use exceptions for regular control flow.
- 🧠 Use **exceptions only for exceptional cases**.

--- `Either`, or precondition checks instead of exceptions?

