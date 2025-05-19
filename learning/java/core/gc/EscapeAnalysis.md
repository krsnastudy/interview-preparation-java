## 🚀 What is Escape Analysis?

**Escape Analysis** analyzes the **scope of object references** — basically, whether an object "escapes" the method or thread in which it was created.

### 📌 Goal:
To determine **whether it's safe to allocate an object on the stack** instead of the heap.

---

## 📦 Object Allocation: Heap vs Stack

Normally in Java:
- **Objects** are allocated on the **heap**.
- **Primitives and references** are on the **stack**.

Heap allocation is more expensive (GC, memory pressure). If the JVM knows the object doesn't escape, it can **allocate it on the stack** → faster and no GC needed.

---

## 🔍 Types of Escapes

1. **No Escape**
    - Object does **not leave the method**.
    - 🔁 Safe for stack allocation or even **removal** (scalar replacement).

2. **Method Escape**
    - Object is **passed to another method**.
    - May still be safe depending on method inlining.

3. **Global Escape**
    - Object is **assigned to a field**, returned, or passed to another thread.
    - ❌ Must be allocated on the heap.

---

## ⚡ Optimizations Enabled by Escape Analysis

| Optimization | Description |
|-------------|-------------|
| ✅ **Stack Allocation** | Allocates object on the **stack** instead of the heap — faster, no GC overhead. |
| ✅ **Scalar Replacement** | Replaces object with **individual fields**, avoiding object creation entirely. |
| ✅ **Lock Elision** | Removes **synchronized blocks** if object doesn't escape current thread. |

---

## 📘 Example

```java
public class EscapeDemo {
    public int compute() {
        Point p = new Point(1, 2); // Point is only used locally
        return p.x + p.y;
    }
}

class Point {
    int x, y;
    Point(int x, int y) { this.x = x; this.y = y; }
}
```

👉 Here, `Point` is **eligible for stack allocation** and possibly **scalar replacement** (fields may be inlined as local variables).

---

## ⚙️ JVM Option to Enable Escape Analysis (enabled by default in HotSpot)

```bash
-XX:+DoEscapeAnalysis
```

You can **disable it** for debugging:

```bash
-XX:-DoEscapeAnalysis
```

And **log optimization details**:

```bash
-XX:+PrintEscapeAnalysis -XX:+UnlockDiagnosticVMOptions
```

---

## 🧠 TL;DR

- **Escape Analysis** determines if an object is confined to a method/thread.
- If yes, JVM can:
    - 📍 Allocate on **stack** instead of heap
    - 🧱 Replace object with **scalar fields**
    - 🔓 Remove unnecessary **locks**
- This leads to **faster execution** and **less GC pressure**.

