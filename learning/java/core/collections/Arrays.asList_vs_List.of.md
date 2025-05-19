Here’s a concise comparison of `Arrays.asList()` vs `List.of()` in Java (interview-focused):

---

### ✅ `Arrays.asList(T... a)` (Java 1.2):

* Returns: `java.util.Arrays$ArrayList` (not `java.util.ArrayList`)
* Backed by the original array — changes to the array reflect in the list.
* Fixed-size: can't add/remove elements, only update.
* **Allows** `null` values.
* **Mutable contents**, but fixed-size structure.

```java
String[] arr = {"A", "B"};
List<String> list = Arrays.asList(arr);
list.set(0, "Z");       // OK
list.add("C");          // Throws UnsupportedOperationException
```

---

### ✅ `List.of(T... elements)` (Java 9+):

* Returns: **Immutable List** (`java.util.ImmutableCollections.ListN`)
* **Cannot add, remove, or modify** elements.
* **Does NOT allow** `null` values — throws `NullPointerException`.
* Safer for unmodifiable collections.

```java
List<String> list = List.of("A", "B");
list.get(0);            // OK
list.set(0, "Z");       // Throws UnsupportedOperationException
```

---

### 🔍 Key Differences (Table for Interviews)

| Feature                    | `Arrays.asList()`            | `List.of()`                 |
| -------------------------- | ---------------------------- | --------------------------- |
| Java Version               | Java 1.2                     | Java 9+                     |
| Mutability                 | Fixed-size, mutable elements | Immutable                   |
| Backed by array?           | Yes                          | No                          |
| Allows null?               | Yes                          | No (`NullPointerException`) |
| Suitable for modification? | Yes (set only)               | No                          |

---

### ✅ TL;DR for Interviews:

* Use `Arrays.asList()` when you need a fixed-size but mutable list (backed by array).
* Use `List.of()` when you need an immutable, null-safe, and concise list in Java 9+.

Do you want a code comparison or quiz-style practice question based on this?
