# CompletableFuture Deep Dive (Java)

---

## 1. Why CompletableFuture Exists

Before Java 8, async work meant `Future`. A `Future` could tell you a task was running, but it had big gaps:

- No way to **combine** two async results.
- No way to **chain** the next step once one finished.
- No way to **react automatically** on completion (you had to call `.get()` and block).
- No built-in way to **handle exceptions** in async pipelines.

`CompletableFuture` (Java 8, `java.util.concurrent`) fixes all of this. It implements both `Future` and `CompletionStage`, so it can be:
- Manually completed (`complete()`)
- Chained (`thenApply`, `thenCompose`...)
- Combined with other futures (`thenCombine`, `allOf`, `anyOf`)
- Non-blocking, with callback-style reactions (`thenAccept`, `thenRun`)

Think of it as **Java's Promise** (similar to JavaScript Promises).

---

## 2. Core Method Groups

### A. Creating a CompletableFuture

| Method | Purpose |
|---|---|
| `CompletableFuture.completedFuture(value)` | Already-completed future with a value |
| `CompletableFuture.supplyAsync(Supplier)` | Runs a task **returning a value**, async |
| `CompletableFuture.runAsync(Runnable)` | Runs a task **returning nothing**, async |
| `new CompletableFuture<>()` + `complete(value)` | Manual completion (you control when it finishes) |

```java
CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> fetchUser());
CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> sendLog());
```

By default, these run on `ForkJoinPool.commonPool()`. You can pass a custom `Executor` as a second argument — **important in production**, because the common pool is shared JVM-wide and can starve other tasks.

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
CompletableFuture.supplyAsync(() -> fetchUser(), executor);
```

---

### B. Transforming Results (Chaining)

| Method | Input → Output | Use case |
|---|---|---|
| `thenApply(Function)` | value → new value | Transform result (like `.map()`) |
| `thenApplyAsync(Function)` | same, but runs on a (possibly different) thread | Transform, but off the calling thread |
| `thenCompose(Function)` | value → **another CompletableFuture** | Chain dependent async calls (like `.flatMap()`) |
| `thenCombine(other, BiFunction)` | two independent futures → combined value | Merge results of two parallel calls |

```java
CompletableFuture<Integer> length = CompletableFuture
    .supplyAsync(() -> "hello")
    .thenApply(String::toUpperCase)   // "HELLO"
    .thenApply(String::length);       // 5
```

**`thenApply` vs `thenCompose`** — this is the #1 confusion point:
- `thenApply`: use when the function returns a **plain value**.
- `thenCompose`: use when the function itself returns a **CompletableFuture** (avoids nested `CompletableFuture<CompletableFuture<T>>`).

```java
// thenCompose example — dependent async calls
CompletableFuture<Order> orderFuture = getUserAsync(userId)
    .thenCompose(user -> getOrderForUserAsync(user)); // returns CF<Order>, flattened
```

---

### C. Consuming Results (No Return Value)

| Method | Purpose |
|---|---|
| `thenAccept(Consumer)` | Use the result, return nothing |
| `thenRun(Runnable)` | Ignore result entirely, just run something after |

```java
CompletableFuture.supplyAsync(() -> "data")
    .thenAccept(result -> System.out.println("Got: " + result));

CompletableFuture.supplyAsync(() -> "data")
    .thenRun(() -> System.out.println("Task done"));
```

---

### D. Combining Multiple Futures

| Method | Behavior |
|---|---|
| `thenCombine(other, BiFunction)` | Wait for **both**, combine results |
| `CompletableFuture.allOf(cf1, cf2, ...)` | Wait for **all** to finish (returns `Void`) |
| `CompletableFuture.anyOf(cf1, cf2, ...)` | Completes when **any one** finishes first |

```java
CompletableFuture<Double> priceCF = CompletableFuture.supplyAsync(() -> getPrice());
CompletableFuture<Integer> stockCF = CompletableFuture.supplyAsync(() -> getStock());

CompletableFuture<String> combined = priceCF.thenCombine(stockCF,
    (price, stock) -> "Price: " + price + ", Stock: " + stock);
```

`allOf` is commonly used to wait for a batch of parallel calls:

```java
CompletableFuture<Void> all = CompletableFuture.allOf(cf1, cf2, cf3);
all.join(); // blocks until all three are done
```

---

### E. Exception Handling

| Method | Purpose |
|---|---|
| `exceptionally(Function)` | Recover from an exception, return a fallback value |
| `handle(BiFunction)` | Runs **regardless** of success/failure — gets `(result, exception)` |
| `whenComplete(BiConsumer)` | Side-effect on completion (success or failure) — doesn't change the result |

```java
CompletableFuture<String> result = CompletableFuture
    .supplyAsync(() -> {
        if (true) throw new RuntimeException("API down");
        return "success";
    })
    .exceptionally(ex -> "fallback-value"); // catches the exception

CompletableFuture<String> handled = CompletableFuture
    .supplyAsync(() -> riskyCall())
    .handle((res, ex) -> ex != null ? "default" : res);
```

**Rule of thumb:** `exceptionally` = catch block. `handle` = try-catch-finally combined (always runs). `whenComplete` = finally block (doesn't swallow the exception or change the value).

---

### F. Blocking / Retrieving the Result

| Method | Behavior |
|---|---|
| `get()` | Blocks, throws checked exceptions (`ExecutionException`, `InterruptedException`) |
| `join()` | Blocks, throws unchecked `CompletionException` — preferred in stream/lambda chains |
| `getNow(defaultValue)` | Returns immediately: result if done, else the default |

```java
String result = cf.join(); // no checked exception to handle
```

---

## 3. Real-Life Example: E-Commerce Order Summary Service

**Scenario:** When a user opens an order details page, you need to fetch data from **three independent services** — User Service, Product Service, and Payment Service — then combine them into one response. Doing this sequentially would be slow (3x latency). CompletableFuture lets you run them in parallel and combine results.

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OrderSummaryService {

    private final Executor executor = Executors.newFixedThreadPool(4);

    // Simulated remote calls (in real life these are REST/Feign clients)
    private User fetchUser(String userId) {
        sleep(500);
        return new User(userId, "Krishna");
    }

    private Product fetchProduct(String productId) {
        sleep(700);
        return new Product(productId, "Wireless Mouse", 799.0);
    }

    private PaymentStatus fetchPaymentStatus(String orderId) {
        sleep(300);
        return new PaymentStatus(orderId, "SUCCESS");
    }

    public OrderSummary getOrderSummary(String userId, String productId, String orderId) {

        CompletableFuture<User> userCF =
            CompletableFuture.supplyAsync(() -> fetchUser(userId), executor);

        CompletableFuture<Product> productCF =
            CompletableFuture.supplyAsync(() -> fetchProduct(productId), executor)
                .exceptionally(ex -> new Product(productId, "Unavailable", 0.0)); // fallback

        CompletableFuture<PaymentStatus> paymentCF =
            CompletableFuture.supplyAsync(() -> fetchPaymentStatus(orderId), executor);

        // Combine all three once they're done
        CompletableFuture<OrderSummary> summaryCF = userCF
            .thenCombine(productCF, (user, product) -> new Object[]{user, product})
            .thenCombine(paymentCF, (arr, payment) ->
                new OrderSummary((User) arr[0], (Product) arr[1], payment));

        return summaryCF.join(); // total time ≈ 700ms (slowest call), not 1500ms (sum)
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    record User(String id, String name) {}
    record Product(String id, String name, double price) {}
    record PaymentStatus(String orderId, String status) {}
    record OrderSummary(User user, Product product, PaymentStatus payment) {}
}
```

**What's happening:**
- Three independent I/O calls launch **simultaneously** on the executor.
- `thenCombine` chains them pairwise into a single `OrderSummary`.
- `exceptionally` on the product call means if the product service fails, the whole pipeline doesn't crash — it falls back gracefully.
- `.join()` blocks only once, at the very end, and the total wait is roughly the time of the **slowest** call (~700ms), not the sum of all three (~1500ms).

This is the classic real-world use case: **Spring Boot services aggregating data from multiple microservices** in parallel.

---

## 4. Interview Angle

### Likely questions and how to answer them

**Q1: What problem does CompletableFuture solve that Future doesn't?**
> `Future` only lets you check/block for a result — no chaining, no combining, no callback-based reactions, no clean exception handling. `CompletableFuture` adds all of this via the `CompletionStage` API, and can also be completed manually (useful for bridging callback-based APIs into futures).

**Q2: Difference between `thenApply`, `thenAccept`, `thenRun`?**
> All three run after the previous stage completes.
> - `thenApply`: takes input, returns a **new value**.
> - `thenAccept`: takes input, returns **nothing** (`Void`), used for side effects like logging.
> - `thenRun`: takes **no input at all**, just runs something after completion.

**Q3: Difference between `thenApply` and `thenCompose`?**
> `thenApply` is for simple transformations (`map`). `thenCompose` is for chaining a function that itself returns a `CompletableFuture` (`flatMap`) — avoiding nested futures. This comes up constantly when one async call depends on the result of another.

**Q4: Difference between sync (`thenApply`) and async (`thenApplyAsync`) variants?**
> The non-async variant runs the callback in the **same thread** that completed the previous stage (which could be the calling thread if already complete, or a worker thread if not). The `Async` variant guarantees the callback runs in a thread from the executor pool — important for keeping heavy work off, say, a request thread.

**Q5: `allOf` vs `anyOf`?**
> `allOf` waits for every future to finish (used for batch/parallel aggregation, returns `Void` — you access individual results separately). `anyOf` completes as soon as the **first** future finishes (useful for "fastest response wins" patterns, e.g., querying two mirrored services).

**Q6: How do you handle exceptions in a CompletableFuture chain?**
> `exceptionally()` for fallback values (like catch), `handle()` to process both success and failure paths in one place, `whenComplete()` for side effects (logging/metrics) without altering the result or swallowing the exception.

**Q7: What thread pool does CompletableFuture use by default, and why does that matter in production?**
> By default it's `ForkJoinPool.commonPool()`, shared across the JVM. In a Spring Boot app, if you don't supply a custom `Executor`, blocking I/O calls inside `supplyAsync` can starve this shared pool and slow down unrelated parallel streams or other async work elsewhere in the app. Best practice: always pass a dedicated `Executor` sized for your workload.

**Q8: `get()` vs `join()`?**
> `get()` throws checked exceptions (`InterruptedException`, `ExecutionException`) — you must handle or declare them. `join()` throws an unchecked `CompletionException`, so it's more convenient inside lambdas/streams where checked exceptions are awkward.

**Q9 (scenario-based, common in interviews):** *"You need to call three microservices in parallel and combine their results with a timeout — how would you do it?"*
> Launch each with `supplyAsync(..., executor)`, combine with `thenCombine`/`allOf`, and apply `.orTimeout(2, TimeUnit.SECONDS)` (Java 9+) or `.completeOnTimeout(fallback, 2, TimeUnit.SECONDS)` to bound the wait — this is a good answer that shows awareness beyond the basic API.

---

## 5. Summary Table

| Category | Methods | One-line takeaway |
|---|---|---|
| Create | `supplyAsync`, `runAsync`, `completedFuture` | Start async work, with or without a return value |
| Transform | `thenApply`, `thenApplyAsync` | `map()` for async results |
| Chain dependent calls | `thenCompose` | `flatMap()` — avoids nested futures |
| Consume | `thenAccept`, `thenRun` | Side effects, no new value |
| Combine | `thenCombine`, `allOf`, `anyOf` | Merge parallel results |
| Handle errors | `exceptionally`, `handle`, `whenComplete` | Catch / catch-finally / finally |
| Get result | `get()`, `join()`, `getNow()` | Blocking retrieval (checked vs unchecked) |
| Timeout (Java 9+) | `orTimeout`, `completeOnTimeout` | Bound how long you wait |

**Core mental model:** CompletableFuture turns async code into a pipeline you build declaratively — create → transform/chain → combine → handle errors → retrieve — instead of manually managing threads and blocking calls. In real systems, its biggest win is running independent I/O-bound calls (DB, REST, microservices) **in parallel** instead of sequentially, cutting response time down to the slowest single call rather than the sum of all calls.

---

## 6. Learning Resources

- **Official Javadoc:** `java.util.concurrent.CompletableFuture` — the authoritative method reference.
- **Baeldung – "Guide to CompletableFuture"** — one of the most practical, example-driven write-ups on this topic.
- **Book:** *Java Concurrency in Practice* (Goetz) — not CompletableFuture-specific (predates Java 8) but essential for understanding the threading model underneath it.
- **Spring Boot docs on `@Async`** — since CompletableFuture is the standard return type for async Spring methods, worth pairing this with `@Async` and `AsyncConfigurer` for a full picture in Spring context.
- **Practice idea:** Rebuild the order-summary example above using real REST calls via `RestTemplate`/`WebClient` and a Spring `@Bean` `Executor`, then experiment with `orTimeout` and `anyOf` for a mirrored-service failover scenario.
