## ✅ What `Collections.unmodifiableList(list)` Really Does?

It returns a **wrapper object** that **prevents modification operations** (like `add`, `remove`, `clear`, etc.) by **overriding and throwing `UnsupportedOperationException`**.

---

## 🔍 Internal Working (Simplified Source Code View)

Here’s what happens internally in the JDK:

```java
public static <T> List<T> unmodifiableList(List<? extends T> list) {
    return new UnmodifiableList<>(list);
}

static class UnmodifiableList<E> extends UnmodifiableCollection<E> implements List<E>, RandomAccess {
    final List<? extends E> list;

    UnmodifiableList(List<? extends E> list) {
        super(list);
        this.list = list;
    }

    public E get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }
    
    // Other mutating methods also throw exceptions
}
```

---

## ⚙️ Key Concepts

| Feature | Description |
|--------|-------------|
| **Wrapper class** | The returned list is not a new list. It's a **read-only wrapper** around the original list. |
| **Non-modifiable** | Any attempt to modify (add, remove, set, clear, etc.) will throw `UnsupportedOperationException`. |
| **Still reflects original** | If the original list is modified **directly**, those changes **are visible** in the unmodifiable view. |
| **Shallow protection** | Only the list structure is protected, not the **mutable elements** inside it. |

---

## 🧪 Example

```java
List<String> original = new ArrayList<>();
original.add("Java");

List<String> unmodifiable = Collections.unmodifiableList(original);

// Reading is fine
System.out.println(unmodifiable.get(0)); // Java

// Modifying throws exception
unmodifiable.add("Python"); // 🚫 UnsupportedOperationException

// But modifying the original reflects in the unmodifiable view
original.add("C++");
System.out.println(unmodifiable); // [Java, C++] 😮
```

---

## ⚠️ Common Misunderstanding

> ❗ `unmodifiableList` ≠ truly immutable list  
> It's only **read-only from the reference**, not from the underlying list.

For **true immutability**, use:
```java
List<String> immutableList = List.of("Java", "Python"); // Java 9+
```

---

## 🧠 TL;DR

- `Collections.unmodifiableList()` wraps an existing list and disables mutating operations.
- It's a **read-only view**, not an immutable copy.
- Internal changes to the original list are still reflected.
- It throws `UnsupportedOperationException` for any structural change attempts.
