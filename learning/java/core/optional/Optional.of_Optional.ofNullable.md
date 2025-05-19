`Optional.of()` and `Optional.ofNullable()` are both used to create `Optional` instances, but they behave **differently with `null` values**. This is a **commonly asked Java 8 interview question**.

---

### ✅ `Optional.of(T value)`

* **Throws `NullPointerException`** if the value is `null`.
* Use this **when you're sure** the value is **not null**.

```java
String name = "Krishna";
Optional<String> optionalName = Optional.of(name); // ✅ OK

String nullName = null;
Optional<String> opt = Optional.of(nullName); // ❌ Throws NullPointerException
```

---

### ✅ `Optional.ofNullable(T value)`

* **Safe to use** — handles `null` values.
* Returns:

    * `Optional.empty` if value is `null`
    * `Optional.of(value)` otherwise

```java
String name = null;
Optional<String> optionalName = Optional.ofNullable(name); // ✅ No Exception, returns Optional.empty
```

---

### 🧠 **Interview Short Answer:**

> * `Optional.of(value)` throws `NullPointerException` if value is null.
> * `Optional.ofNullable(value)` handles nulls safely and returns `Optional.empty` if null.
> * Use `of()` when nulls are not allowed, and `ofNullable()` when nulls are possible.

---
