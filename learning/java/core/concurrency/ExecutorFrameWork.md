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


# Java Executor Framework: interview notes

The companion `ExecutorFrameworkDemo.java` is a single-file Java 21+ program. It demonstrates the common executor factories, a directly configured bounded `ThreadPoolExecutor`, task results with `Future`, and orderly shutdown.

## The core idea

The Executor framework separates **what work should run** (`Runnable` or `Callable`) from **how threads are created, scheduled, and managed** (`Executor` / `ExecutorService`). Instead of creating a raw `Thread` for every operation, submit tasks to an executor. `ExecutorService` adds lifecycle management, task submission, `Future` results, bulk operations, and termination waiting.

## Pool comparison

| Executor | Worker behavior | Good fit | Interview caveat |
|---|---|---|---|
| `newFixedThreadPool(n)` | At most `n` active workers; shared unbounded queue | Bounded concurrency for a known workload | The worker count is bounded, but the queue is not. A sustained producer overload can consume memory and increase latency. |
| `newCachedThreadPool()` | Reuses idle workers and can create more as demand rises; idle workers time out | Many short-lived, intermittent tasks | No practical worker cap; bursts of blocked or slow tasks can create many platform threads. |
| `newSingleThreadExecutor()` | One worker; tasks execute sequentially | Serialized updates or ordered background work | One slow task blocks all later tasks; its work queue is unbounded. |
| `newScheduledThreadPool(n)` | Schedules delayed and periodic actions using `n` core workers | Timers, retries, periodic maintenance | A long-running scheduled task occupies a worker. Periodic tasks do not overlap with themselves. Handle exceptions so recurring work is not unintentionally suppressed. |
| `newWorkStealingPool(p)` | Fork/join workers with work-stealing; order is not guaranteed | Many independent or recursively split CPU tasks | Not a drop-in ordered executor; common-pool-style parallelism is not a resource-isolation strategy. |
| `ThreadPoolExecutor` configured directly | You choose core/max sizes, queue, keep-alive, factory, and rejection handler | Production workloads needing explicit capacity and overload behavior | Explain the queue and rejection policy; `maximumPoolSize` only comes into play after the queue fills. |
| `newVirtualThreadPerTaskExecutor()` | Starts a virtual thread per task; no fixed worker pool | Large numbers of mostly-blocking I/O tasks | Java 21+. Virtual threads reduce the cost of waiting; they do not make CPU-bound work faster or eliminate the need to limit downstream resources. |

The standard factory methods for fixed, cached, single, scheduled, and work-stealing executors are documented in the [Java `Executors` API](https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/util/concurrent/Executors.html). Virtual threads are documented in Oracle's [Java 21 virtual threads guide](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html).

## `ThreadPoolExecutor` admission sequence

When a task arrives, a `ThreadPoolExecutor` generally:

1. Starts workers until `corePoolSize` is reached.
2. Queues new tasks while the queue accepts them.
3. If the queue is full, creates workers up to `maximumPoolSize`.
4. If it cannot queue or create another worker, invokes the `RejectedExecutionHandler`.

The demo uses a bounded `ArrayBlockingQueue` and `CallerRunsPolicy`. Under saturation, the submitting thread runs the task, which slows further submissions and provides a simple form of backpressure. Other standard policies include `AbortPolicy` (throw), `DiscardPolicy` (drop), and `DiscardOldestPolicy` (drop the queue head). Choose a policy based on whether loss, caller slowdown, or explicit failure is acceptable.

## Task and result methods

- `execute(Runnable)` is fire-and-forget; it has no returned result handle.
- `submit(Runnable/Callable)` returns a `Future`. `get()` waits; for a `Callable`, it returns the value. Task failures are surfaced from `get()` as `ExecutionException`.
- `invokeAll` submits a collection and waits for all results; returned futures correspond to input order, not completion order.
- `invokeAny` returns one successful result and cancels unfinished tasks after success.
- `schedule` runs once after a delay. `scheduleAtFixedRate` targets a cadence; `scheduleWithFixedDelay` waits the delay after one execution finishes.

## Shutdown: a strong interview answer

Call `shutdown()` when the executor is no longer needed. It rejects new tasks but lets accepted tasks finish. Wait with `awaitTermination`. If the deadline expires, call `shutdownNow()` to request interruption of active tasks and retrieve queued tasks that never started. Interruption is cooperative: task code should respond to `InterruptedException` or restore the interrupt flag, as `shortTask` does.

Avoid relying on daemon threads or process exit to clean up pools. Unclosed executors can keep an application alive and leak resources. In Java 21+, `ExecutorService` is also `AutoCloseable`, so try-with-resources can be used when its close/wait behavior fits the application lifecycle.

## Common interview questions

**Why use an executor instead of creating threads directly?**
It centralizes thread management, limits concurrency when configured to do so, supports queuing and lifecycle control, and provides result/cancellation abstractions.

**Does a fixed thread pool bound memory?**
No. The factory uses a shared unbounded queue. Bound the queue with a directly configured `ThreadPoolExecutor` and define what happens when saturated.

**Difference between `execute` and `submit`?**
`execute` accepts a `Runnable` and returns no result. `submit` returns a `Future`, enabling waiting, cancellation, result retrieval, and observing task exceptions.

**What is the difference between `shutdown` and `shutdownNow`?**
`shutdown` is graceful and accepts no new tasks while allowing submitted tasks to finish. `shutdownNow` makes a best-effort interruption request and returns tasks that never commenced; it cannot forcibly stop code that ignores interruption.

**When choose virtual threads over a pool?**
For high-concurrency workloads with many blocking operations, virtual threads make a thread-per-task programming style much cheaper. Keep explicit limits around scarce resources (database connections, remote-service quotas). Use a bounded platform-thread executor for CPU-bound concurrency and isolation.

**What should you measure before picking pool size?**
Task type and service time, CPU count/utilization, blocking fraction, queue depth and wait time, rejection count, throughput, tail latency, and downstream capacity. `availableProcessors()` is a starting signal, not a universal sizing formula.

**Yes.** Executors queue submitted tasks while the worker threads are busy, but the queue type and capacity depend on the executor. In Java 17’s standard implementations, the queues are:

| Executor in the demo | Queue used | Maximum queued tasks |
|---|---|---:|
| `newFixedThreadPool(n)` | `LinkedBlockingQueue` | Effectively unbounded; its default capacity is `Integer.MAX_VALUE` |
| `newSingleThreadExecutor()` | `LinkedBlockingQueue` | Effectively unbounded; its default capacity is `Integer.MAX_VALUE` |
| `newCachedThreadPool()` | `SynchronousQueue` | **0 stored tasks**; each task must be handed directly to a worker, or a new worker is created |
| `newScheduledThreadPool(n)` | `DelayedWorkQueue` | Unbounded |
| `newWorkStealingPool(n)` | Fork/join work-stealing queues | No single shared queue or simple configured capacity; the pool uses multiple queues |
| Custom `ThreadPoolExecutor` in the demo | `ArrayBlockingQueue<>(2)` | **2 tasks** |

The fixed and single-thread factory methods use an unbounded queue, so tasks can accumulate if they arrive faster than workers process them. That can lead to high memory use. The cached pool’s `SynchronousQueue` doesn’t store tasks: it hands a task to a worker immediately, or the pool creates another worker. The [Java 17 `Executors` API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Executors.html) documents the standard factory behavior; the exact queue implementations come from the [OpenJDK implementation](https://code.googlesource.com/edge/openjdk/%2B/refs/heads/master/jdk/src/share/classes/java/util/concurrent/Executors.java).

In the custom pool, the configuration is **2 core workers, up to 4 workers, and a queue of 2**. When all workers are busy and the queue is full, `CallerRunsPolicy` makes the thread submitting the task run it. This slows down submissions and provides basic backpressure. The [Java `ThreadPoolExecutor` docs](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ThreadPoolExecutor.html) describe how queueing, pool sizing, and rejection work together.

One detail for interviews: `Integer.MAX_VALUE` is the queue’s theoretical capacity, not a practical guarantee that it can hold that many tasks—the process can run out of memory first.

**Sample Code**
https://github.com/krsnastudy/CoreJava/blob/master/src/com/prac/core/jdks/jdk5/threads/executor/ExecutorFrameworkDemo.java