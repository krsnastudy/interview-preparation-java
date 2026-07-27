# List vs Collection — `remove(1)` Behaves Differently! (Classic Interview Trap)

```java
public class TestPractice {
    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>(List.of(1,2,3));
        System.out.println("list B4 --> "+list);
        list.remove(1);
        System.out.println("list A4 --> "+list);

        Collection<Integer> collection = new ArrayList<>(List.of(1,2,3));
        System.out.println("collection B4 --> "+collection);
        collection.remove(1);
        System.out.println("collection A4 --> "+collection);
    }
}
```
**most popular "gotcha" interview questions** in core Java — it tests whether you understand that 
**method overload resolution happens at compile time, based on the declared (static) type**, not the runtime type.

---

## Output

```
list B4 --> [1, 2, 3]
list A4 --> [1, 3]

collection B4 --> [1, 2, 3]
collection A4 --> [2, 3]
```

**Same underlying `ArrayList`, same value `1` passed to `remove` — completely different results.**

---

## Why This Happens

### `List<Integer>` — two overloaded `remove` methods available

```java
list.remove(1);
```

The **`List`** interface declares **two** overloads:

```java
E remove(int index)        // removes element AT this index
boolean remove(Object o)   // removes the first occurrence of this VALUE
```

When you write `list.remove(1)`, the compiler sees an `int` literal `1`. Java's overload resolution rule: **the most specific applicable method wins**, and a primitive `int` match beats autoboxing to `Integer` (Object). So it resolves to `remove(int index)` — **removes the element at index 1**, which is the value `2`.

```
[1, 2, 3]
     ↑ index 1 removed → 2
Result: [1, 3]
```

### `Collection<Integer>` — only ONE `remove` method available

```java
collection.remove(1);
```

The **`Collection`** interface declares only:

```java
boolean remove(Object o)
```

There's **no `remove(int)` overload on `Collection`** — `List` adds that as an interface-specific extra method, but `Collection` doesn't have it. So `1` gets **autoboxed to `Integer.valueOf(1)`**, and it resolves to `remove(Object o)` — **removes the value `1`**.

```
[1, 2, 3]
 ↑ value 1 removed
Result: [2, 3]
```

---

## Visual Summary

```
Static type: List<Integer>          Static type: Collection<Integer>
     │                                       │
     ▼                                       ▼
remove(int) exists?  YES              remove(int) exists?  NO
     │                                       │
     ▼                                       ▼
resolves to remove(index)             resolves to remove(Object)
     │                                       │
     ▼                                       ▼
removes element AT position 1         removes the VALUE 1 (autoboxed)
     │                                       │
     ▼                                       ▼
[1, 3]                                 [2, 3]
```

> **Key insight:** Both variables point to the exact same `ArrayList` object at runtime. This isn't about polymorphism or runtime dispatch at all — it's purely about **which overload the compiler picks based on the compile-time (declared) type of the reference variable**. This is why it's such a good interview question: it separates people who understand overload resolution from people who just assume "it's the same object, so it should behave the same."

---

## 🎯 Interview-Oriented Insights

| Question | Key Answer |
|---|---|
| Why doesn't Java just pick `remove(Object)` for both, treating `1` as `Integer`? | Overload resolution always prefers a match **without boxing/unboxing** over one that requires it, when both are applicable. `int` matches `remove(int)` exactly — no conversion needed — so it wins over `remove(Object)`, which would require autoboxing |
| How do you force `List.remove` to remove by **value** instead of index? | Explicitly box it: `list.remove(Integer.valueOf(1))` or `list.remove((Integer) 1)` — this makes the compiler pick `remove(Object)` |
| Is this resolved at compile time or runtime? | **Compile time** — this is static (early) binding for overload resolution, as opposed to dynamic (late) binding used for overridden methods. The bytecode generated for `list.remove(1)` literally calls a different method signature than `collection.remove(1)` |
| Why does `Collection` not have `remove(int)` at all? | `Collection` is a general contract — many implementations (`Set`, `Queue`) have **no concept of index/position**, so an index-based `remove` wouldn't make sense there. `List` adds it because lists are inherently ordered/indexed |
| What's the real-world lesson here? | Be very deliberate about which type you declare a variable as (`List` vs `Collection`) — it's not just style, it can **change actual runtime behavior** for overloaded methods. This is also why some teams have lint rules discouraging `remove(int)` on `List<Integer>` without an explicit cast, to avoid silent bugs |
| Would this trap occur with `List<String>`? | No — `String` isn't autoboxed from `int`, so `list.remove("1")` unambiguously resolves to `remove(Object)`. This bug is **specific to boxed numeric types** like `Integer`, `Long`, etc. where autoboxing creates the ambiguity |

---

## 📚 Learning Resources

- **JLS §15.12.2** (Compile-Time Step 2: Determine Method Signature) — the formal overload resolution rules, if you want to go deep
- Search **"Java autoboxing overload resolution pitfall List remove"** — this exact example is widely covered on Baeldung and Stack Overflow
- Practice variant: try the same trap with `Set<Integer>` (no `remove(int)` exists there either — same behavior as `Collection`)

Want to explore a related trap — **`ArrayList` vs `LinkedList` performance characteristics for `remove`**, or move to a different core Java topic like **`ConcurrentModificationException` during iteration + removal**?