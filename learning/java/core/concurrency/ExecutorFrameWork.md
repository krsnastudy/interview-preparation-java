# **Executor Framework in Java - Detailed Guide for Interviews**

The Executor Framework is a powerful **thread management** system introduced in Java 5 (`java.util.concurrent`) to simplify multithreading. It decouples **task submission** from **thread execution**, providing better control over thread lifecycle.

---

## **1. Key Components of Executor Framework**
| Component | Purpose | Key Classes |
|-----------|---------|-------------|
| **Executor** | Base interface for executing tasks | `Executor` |
| **ExecutorService** | Extends `Executor` with lifecycle methods | `ExecutorService` |
| **ThreadPool** | Manages a pool of worker threads | `ThreadPoolExecutor` |
| **Scheduled Executor** | Supports delayed & periodic tasks | `ScheduledThreadPoolExecutor` |
| **Future** | Represents result of async computation | `Future<T>`, `CompletableFuture` |

---

## **2. Types of Thread Pools**
Java provides **4 built-in thread pools** via `Executors` factory:

### **a) FixedThreadPool**
- **Fixed number of threads**.
- **Use Case:** CPU-bound tasks.
```java
ExecutorService executor = Executors.newFixedThreadPool(4); // 4 threads
```

### **b) CachedThreadPool**
- **Dynamically scales threads** (reuses idle threads).
- **Use Case:** Short-lived asynchronous tasks.
```java
ExecutorService executor = Executors.newCachedThreadPool();
```

### **c) SingleThreadExecutor**
- **Single worker thread**.
- **Use Case:** Sequential task execution.
```java
ExecutorService executor = Executors.newSingleThreadExecutor();
```

### **d) ScheduledThreadPool**
- **Supports delayed/periodic tasks**.
- **Use Case:** Timers, heartbeat checks.
```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
executor.schedule(task, 5, TimeUnit.SECONDS); // Run after 5 sec
```

---

## **3. Core Methods of `ExecutorService`**
| Method | Purpose | Example |
|--------|---------|---------|
| `execute(Runnable)` | Submits a task | `executor.execute(() -> System.out.println("Task"));` |
| `submit(Callable)` | Submits a task & returns `Future` | `Future<Integer> future = executor.submit(() -> 42);` |
| `shutdown()` | Graceful shutdown (waits for tasks) | `executor.shutdown();` |
| `shutdownNow()` | Forceful shutdown (returns pending tasks) | `List<Runnable> pending = executor.shutdownNow();` |
| `awaitTermination()` | Blocks until shutdown completes | `executor.awaitTermination(1, TimeUnit.MINUTES);` |

---

## **4. Sample Code: FixedThreadPool Example**
```java
import java.util.concurrent.*;

public class ExecutorDemo {
    public static void main(String[] args) {
        // 1. Create a thread pool with 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 2. Submit tasks
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 3. Shutdown (no new tasks accepted)
        executor.shutdown();
        try {
            // 4. Wait for tasks to finish (max 10 sec)
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force shutdown
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
```

**Output:**
```
Task 1 running on pool-1-thread-1
Task 2 running on pool-1-thread-2
Task 3 running on pool-1-thread-3
Task 4 running on pool-1-thread-1  // Thread reuse
Task 5 running on pool-1-thread-2
```

---

## **5. Handling Results with `Future`**
```java
ExecutorService executor = Executors.newFixedThreadPool(2);

// Submit a Callable task (returns a value)
Future<Integer> future = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;
});

try {
    System.out.println("Result: " + future.get()); // Blocks until result is ready
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
} finally {
    executor.shutdown();
}
```

---

## **6. ScheduledThreadPool Example**
```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

// Run after 2 seconds
scheduler.schedule(() -> System.out.println("Delayed task"), 2, TimeUnit.SECONDS);

// Run every 3 seconds (after initial 1-second delay)
scheduler.scheduleAtFixedRate(() -> System.out.println("Repeating task"), 1, 3, TimeUnit.SECONDS);
```

---

## **7. Best Practices**
✅ **Avoid `Executors.newFixedThreadPool(Integer.MAX_VALUE)`** → Prefer bounded pools.  
✅ **Always shut down `ExecutorService`** to prevent thread leaks.  
✅ **Use `Callable` over `Runnable`** if you need return values.  
✅ **Handle `InterruptedException` properly** to respect thread interruption.

---

## **8. Common Interview Questions**
1. **Q: Difference between `submit()` and `execute()`?**
    - **A:** `submit()` returns a `Future` and can handle `Callable`/`Runnable`, while `execute()` is `void` and only accepts `Runnable`.

2. **Q: How to handle task rejection?**
    - **A:** Use a `RejectedExecutionHandler` (e.g., `ThreadPoolExecutor.CallerRunsPolicy`).

3. **Q: When to use `CachedThreadPool` vs `FixedThreadPool`?**
    - **A:** Use `CachedThreadPool` for short-lived tasks and `FixedThreadPool` for CPU-bound workloads.

---

## **9. Advanced: Custom ThreadPool**
```java
ThreadPoolExecutor customPool = new ThreadPoolExecutor(
    2,                      // Core pool size
    10,                     // Max pool size
    60, TimeUnit.SECONDS,   // Keep-alive time
    new LinkedBlockingQueue<>(100), // Task queue
    Executors.defaultThreadFactory(),
    new ThreadPoolExecutor.AbortPolicy() // Rejection policy
);
```

---

## **Summary**
- **Executor Framework** simplifies thread management.
- **Thread Pools** optimize resource usage.
- **`Future`** allows asynchronous result handling.
- **Always shut down** `ExecutorService` to avoid leaks.
