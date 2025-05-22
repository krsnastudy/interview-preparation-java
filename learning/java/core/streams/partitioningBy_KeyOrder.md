### ✅ `Collectors.partitioningBy` – Default Key Order

The `Collectors.partitioningBy(...)` method **always produces a `Map<Boolean, ...>`**, specifically a `LinkedHashMap<Boolean, ...>` which **preserves insertion order**.

### What is the order?

It will always have:

* Key `false` first
* Key `true` second

**But**, in our code:

```java
result.get(true) + result.get(false);
```

We **explicitly access `true` first**, then `false`, so we don't depend on key order.

### TL;DR for Interview:

> `partitioningBy` returns a `LinkedHashMap<Boolean, List<T>>`, where `false` comes before `true` by default — but access is explicit by key, not by order.

Here's a demonstration using `Collectors.partitioningBy` and verifying the default key order:

### ✅ **Example**

```java
import java.util.*;
import java.util.stream.Collectors;

public class PartitioningByExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(2, 4, 7, 9, 10, 13);

        Map<Boolean, List<Integer>> result = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));

        System.out.println("Result map: " + result);
        System.out.println("Even numbers: " + result.get(true));
        System.out.println("Odd numbers: " + result.get(false));

        // Check the key order
        System.out.println("Key order:");
        result.keySet().forEach(System.out::println);
    }
}
```

### ✅ **Output:**

```
Result map: {false=[7, 9, 13], true=[2, 4, 10]}
Even numbers: [2, 4, 10]
Odd numbers: [7, 9, 13]
Key order:
false
true
```

### 🔍 Explanation:

* `Collectors.partitioningBy()` splits the stream into two lists based on the predicate.
* The result is a `Map<Boolean, List<T>>` implemented as `LinkedHashMap`.
* **Key order** is: `false`, then `true`, by default.

---

### ✅ TL;DR for Interview:

> `Collectors.partitioningBy()` creates a `LinkedHashMap<Boolean, List<T>>`, where keys are `false` then `true` in insertion order. But it's better to use `get(true)` or `get(false)` explicitly than rely on map order.
