## 🔍 The Four Reference Types in Java

| Reference Type      | GC Behavior               | Can access object? | Use Case                    |
|---------------------|----------------------------|---------------------|-----------------------------|
| **Strong** (default) | Never collected if reachable | ✅ Yes               | Normal object usage         |
| **Soft**             | Collected **only if memory is low** | ✅ Yes         | **Memory-sensitive caching**|
| **Weak**             | Collected **next GC cycle if no strong refs** | ✅ Yes | **Short-lived caches**, maps |
| **Phantom**          | Collected, but cannot access directly | ❌ No         | **Cleanup before GC finalization** |

---

## 🔸 1. **Strong Reference** (Default)

```java
String str = new String("Hello");
```

- As long as `str` is reachable, the object is **never** garbage collected.
- Most references in your code are strong by default.

---

## 🔸 2. **Soft Reference**

```java
SoftReference<String> softRef = new SoftReference<>(new String("Hello"));
```

- Object is **only GC'ed if memory is low**.
- Ideal for **in-memory caches**.

🔁 Example Use:
```java
Map<String, SoftReference<Data>> cache = new HashMap<>();
```

📌 JVM tries to keep soft-referenced objects around as long as possible.

---

## 🔸 3. **Weak Reference**

```java
WeakReference<String> weakRef = new WeakReference<>(new String("Hello"));
```

- Collected **immediately on next GC**, if no strong references.
- Used in `WeakHashMap` keys — auto-cleans entries when key is unreachable.

🧠 Ideal for:
- Plugin registries
- Listeners
- Temporary mappings

---

## 🔸 4. **Phantom Reference**

```java
PhantomReference<String> phantomRef = new PhantomReference<>(str, refQueue);
```

- Object is **already finalized** and **invisible** to code.
- Used for **cleanup after GC** (like finalizers, but safer).
- Requires a `ReferenceQueue` to be useful.

⚠️ You **can’t get()** the object — it's already gone.

---

## 🧠 TL;DR

| Reference Type | When to Use |
|----------------|-------------|
| **Soft**       | For **memory-sensitive caches** — hold onto data unless memory runs low. |
| **Weak**       | For **auto-cleanup mappings**, temporary data, or memory-sensitive listeners. |
| **Phantom**    | For **custom resource cleanup** after object is GC'ed (like file handles, native memory). |

---

