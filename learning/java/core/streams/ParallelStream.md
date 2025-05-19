## 🔍 What does `parallelStream()` do?

When you call `parallelStream()` on a collection, it processes elements **concurrently** across multiple threads using a **divide-and-conquer strategy**.

```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
list.parallelStream().forEach(System.out::println);
```

---

## ⚙️ Internal Working of `parallelStream()`

### 1. **ForkJoinPool (Common Pool)**
- Behind the scenes, `parallelStream()` uses a special thread pool called:
  ```java
  ForkJoinPool.commonPool()
  ```
- This pool is a shared thread pool with a number of threads = **CPU cores** (by default).

### 2. **Spliterator Splits the Data**
- A `Spliterator` is used to **recursively split** the collection into smaller chunks.
- It tries to **balance the workload** by dividing the input until it reaches optimal chunk size.

### 3. **Tasks Are Forked and Joined**
- Each chunk is submitted as a **task** to the ForkJoinPool.
- The **Fork/Join framework** handles:
    - **Forking** tasks to separate threads.
    - **Joining** them when all are done.
    - **Merging** the results (if needed, like in `collect()` or `reduce()`).

### 4. **Combining Results**
- If you're doing a reduction (e.g., `sum`, `collect`, etc.), the intermediate results from each thread are **combined using a combiner function**.

---

## 📌 Visual Flow:
```
parallelStream()
      |
      v
Creates Spliterator
      |
      v
Splits data into tasks
      |
      v
ForkJoinPool.commonPool()
      |
      v
Executes tasks in parallel threads
      |
      v
Collects / reduces / processes results
```

---

## ⚠️ Caveats / Best Practices

| ❌ Don't Do This | ✅ Prefer This |
|------------------|----------------|
| Using `parallelStream()` for small datasets | Use `stream()` — parallel overhead is not worth it |
| Mutating shared variables inside `forEach()` | Use stateless, side-effect-free functions |
| Running on performance-critical UI/main thread | Use only for background-heavy tasks |
| Assuming always faster than `stream()` | Measure! Sometimes sequential is better |

---

## ✅ When to Use `parallelStream()`
- Large collections (10,000+ elements)
- CPU-bound operations
- Stateless, independent processing
- Avoiding I/O or synchronized blocks

---

### TL;DR

- `parallelStream()` splits data and processes it using multiple threads from `ForkJoinPool.commonPool()`.
- It uses `Spliterator` and `Fork/Join Framework` for parallelism.
- Great for large, CPU-bound, stateless tasks — but always benchmark before using.

