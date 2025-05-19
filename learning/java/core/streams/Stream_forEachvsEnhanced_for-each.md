Let's compare **`forEach` in Streams** vs **enhanced `for-each` loop** (also called the "for-each statement") in Java:

---

## ✅ 1. `forEach` in **Java Streams**

### 🔹 Signature:
```java
void forEach(Consumer<? super T> action)
```

- **Input**: A **`Consumer<T>`** functional interface (lambda or method reference)
- **Context**: Called on a `Stream<T>`
- **Introduced in**: Java 8

### ✅ Example:
```java
List<String> names = List.of("Krishna", "Arjun", "Riya");

names.stream().forEach(name -> System.out.println(name));
```

➡️ Here, `name -> System.out.println(name)` is a `Consumer<String>`.

You can also use:
```java
names.stream().forEach(System.out::println);
```

---

## ✅ 2. Enhanced **for-each loop**

### 🔹 Syntax:
```java
for (Type item : collection) {
    // use item
}
```

- **Input**: A collection or array
- **Context**: Language construct (not a method call)
- **Introduced in**: Java 5

### ✅ Example:
```java
for (String name : names) {
    System.out.println(name);
}
```

---

## 🔍 Differences: Stream `forEach` vs Classic `for-each`

| Feature              | Stream `forEach()`                        | Enhanced `for-each` loop           |
|----------------------|-------------------------------------------|------------------------------------|
| Input Type           | `Consumer<T>`                             | Iterable or array                  |
| Syntax Style         | Functional (Lambda)                       | Imperative                         |
| Works with Streams   | ✅ Yes                                     | ❌ No                               |
| Parallel Execution   | ✅ Yes (with `parallelStream()`)          | ❌ No                               |
| Lazy Evaluation      | ❌ Triggers terminal operation             | ❌ Executes directly                |
| Exception Handling   | ❌ Tricky with checked exceptions          | ✅ Easier to handle                 |
| Control Flow (break) | ❌ Not supported in lambda                 | ✅ Supports `break`, `continue`     |

---

## 🧪 Quick Comparison

```java
// Stream forEach
names.stream().forEach(name -> System.out.println(name));

// Classic for-each
for (String name : names) {
    System.out.println(name);
}
```

---

### ✅ TL;DR:

| Feature             | Stream `forEach()`              | Enhanced `for-each`               |
|---------------------|----------------------------------|-----------------------------------|
| Input               | `Consumer<T>` (lambda)          | Collection/array element          |
| Style               | Functional                      | Imperative                        |
| Can Parallelize     | ✅ Yes (with `.parallelStream()`) | ❌ No                             |
| Control Flow        | ❌ No `break/continue`           | ✅ Yes                            |
