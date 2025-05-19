## 🔍 What is the class of `Stream` in Java?

When we do something like:
```java
Stream<String> stream = Stream.of("A", "B", "C");
System.out.println(stream.getClass());
```

### ✅ Output:
```
class java.util.stream.ReferencePipeline$Head
```

---

## 📚 Explanation

The `Stream` API is part of the **`java.util.stream`** package and is built using a **pipeline of classes**, most of which are:

### 🔧 Internal Stream Classes:
| Class | Role |
|-------|------|
| `ReferencePipeline` | Handles **object (non-primitive)** streams |
| `IntPipeline`, `LongPipeline`, `DoublePipeline` | For **primitive** specializations |
| `ReferencePipeline$Head` | Start of the stream |
| `ReferencePipeline$StatelessOp` | Stateless intermediate operations (`map`, `filter`) |
| `ReferencePipeline$StatefulOp` | Stateful intermediate operations (`sorted`, `distinct`) |

---

## 📦 Full Class Hierarchy (Simplified)

```text
Stream<T> (interface)
 └── AbstractPipeline<T, R, S>
       └── ReferencePipeline<T, R>
             └── ReferencePipeline$Head
             └── ReferencePipeline$StatelessOp
             └── ReferencePipeline$StatefulOp
```

> These internal classes are not meant for direct use — they are **implementation details** of the Stream pipeline.

---

## ⚠️ Why It Matters in Interviews

Knowing this shows that you understand:
- Stream API is implemented via **pipeline pattern**
- Streams are **lazy** — operations are not evaluated until terminal operation
- The actual implementation is **not just a list wrapper**, but a sequence of transformation nodes

---

## 🧠 TL;DR (Interview Style):

> The actual class of a `Stream` object is usually `ReferencePipeline$Head` or other inner classes of `ReferencePipeline`.  
> These are part of Java's internal stream pipeline design, which supports lazy, chained processing using classes like `StatelessOp` and `StatefulOp`.

---
