## 🧹 Garbage Collector Evolution by Java Version

| Java Version | Default GC     | New/Improved GCs                         | Notes |
|--------------|----------------|------------------------------------------|-------|
| **Java 8**   | **Parallel GC** (Throughput Collector) | G1 GC (optional) | G1 was introduced but not default. |
| **Java 11**  | **G1 GC**      | ✅ ZGC (Experimental)<br>✅ Epsilon GC | Focus on low-pause times; ZGC for ultra-low latency. |
| **Java 17**  | **G1 GC**      | ✅ ZGC (Stable)<br>✅ Shenandoah (Experimental)<br>✅ Epsilon | More options for ultra-low pause collectors. |
| **Java 21**  | **G1 GC** (Still default) | ✅ ZGC, ✅ Shenandoah (Stable)<br>✅ Generational ZGC (Preview) | Focus on throughput + low latency.<br>JEP 439 for Gen ZGC. |

---

## 🔍 GC Types Explained

### 1. **Parallel GC** (Throughput Collector)
- Old default (Java 8)
- Focus: **High throughput**
- **Stop-the-world**, **multi-threaded** for young & old generations
- Not ideal for latency-sensitive apps

### 2. **G1 GC** (Garbage First)
- Default from **Java 9 onwards**
- Region-based memory
- Predictable **low-pause times**
- Incremental compaction
- A good default for most workloads

### 3. **ZGC** (Z Garbage Collector)
- Introduced: Java 11 (Experimental)
- Stable: Java 15+
- Extremely **low pause times (<10ms)** even for **large heaps (TBs)**
- Concurrent, scalable
- Suitable for real-time, low-latency systems

```bash
-XX:+UseZGC
```

### 4. **Shenandoah GC**
- Introduced by Red Hat
- Low-pause collector with **concurrent compaction**
- Ideal for large heaps
- Stable in OpenJDK from Java 17+

```bash
-XX:+UseShenandoahGC
```

### 5. **Epsilon GC**
- **No-op GC** — does not collect garbage
- Useful for **performance testing or debugging**
- Added in Java 11

```bash
-XX:+UseEpsilonGC
```

### 6. **Generational ZGC** (Preview in Java 21)
- ZGC with **generational support**
- Better performance for young-gen-heavy workloads
- Combines the best of **ZGC + generational collection**

```bash
-XX:+UseZGC -XX:+ZGenerational
```

---

## 🔁 GC Evolution Timeline

```
Java 8     --> Parallel (Default), G1 (Optional)
Java 9     --> G1 becomes default
Java 11    --> ZGC & Epsilon introduced
Java 15    --> ZGC becomes production-ready
Java 17    --> Shenandoah & Epsilon production-ready
Java 21    --> Generational ZGC (Preview), ZGC/Shenandoah mature
```

---

## ✅ GC Selection Tips

| Use Case | Recommended GC |
|----------|----------------|
| High throughput batch jobs | **Parallel GC** |
| Balanced, general-purpose apps | **G1 GC** |
| Low-pause-time apps (real-time, financial) | **ZGC / Shenandoah** |
| Testing GC overhead | **Epsilon GC** |

---

## 📌 How to Select GC in JVM

```bash
java -XX:+UseG1GC           # Use G1 GC
java -XX:+UseParallelGC     # Use Parallel GC
java -XX:+UseZGC            # Use ZGC
java -XX:+UseShenandoahGC   # Use Shenandoah
```

Here's a **visual comparison chart** and **heap layout diagrams** to help you better understand how different Garbage Collectors (GCs) work in Java 8, 11, 17, and 21.

---

## 🔍 Visual Comparison of Major GCs

| Feature                     | **Parallel GC** | **G1 GC**         | **ZGC**                 | **Shenandoah**         |
|----------------------------|------------------|-------------------|--------------------------|------------------------|
| **Pause Time**             | High             | Medium–Low        | **<10 ms**               | **<10 ms**             |
| **Throughput**             | ⭐⭐⭐⭐⭐           | ⭐⭐⭐⭐              | ⭐⭐⭐                    | ⭐⭐⭐                   |
| **Latency Sensitivity**    | ❌               | ✅                 | ✅✅✅                   | ✅✅✅                 |
| **Concurrent Compaction**  | ❌               | ✅ (incremental)   | ✅                       | ✅                    |
| **Heap Scalability (TBs)** | ❌               | Limited            | ✅ (multi-TB)            | ✅ (large heaps)       |
| **Production Ready**       | ✅               | ✅                 | ✅ (Java 15+)            | ✅ (Java 17+)          |
| **GC Threads**             | Parallel         | Region-based      | Mostly concurrent        | Mostly concurrent      |
| **Java Version**           | Default in 8     | Default 9–21      | Java 11+ (stable in 15+) | Java 12+ (stable in 17+) |

---

## 🧠 Heap Layout Diagrams

---

### 🔸 1. **Parallel GC (Java 8 Default)**

```
+------------------------------+
|           Heap               |
+------------------------------+
| Young Gen |     Old Gen      |
| (Eden + S0 + S1)             |
+------------------------------+
```

- Uses **stop-the-world** pauses
- Focuses on **throughput**, not latency
- Simpler layout

---

### 🔸 2. **G1 GC (Default from Java 9+)**

```
+------------------------------+
|           Heap               |
+------------------------------+
|       Regions (1–32 MB)      |
|  Mixed Young/Old/Free       |
+------------------------------+
```

- Breaks heap into **regions**
- Performs **incremental compaction**
- Collects young and old gen in **mixed mode**

---

### 🔸 3. **ZGC (Java 11+, Stable in 15)**

```
+------------------------------+
|           Heap               |
+------------------------------+
|   Colored Pointers & Views   |
|  Remapped behind the scenes  |
+------------------------------+
```

- Uses **colored pointers** for metadata
- Heap is not divided into Gen (no Young/Old)
- Ultra-low pause times, mostly concurrent

---

### 🔸 4. **Shenandoah (Java 17+)**

```
+------------------------------+
|           Heap               |
+------------------------------+
|   Regions + Concurrent GC    |
|   Compact while running      |
+------------------------------+
```

- Similar region-based model
- Performs **concurrent compaction**
- Maintains low latency even with big heaps

---

## 🧪 TL;DR: Which GC Should You Choose?

| Requirement                      | Best GC            |
|----------------------------------|--------------------|
| High throughput (batch jobs)     | Parallel GC        |
| Balanced performance & latency   | G1 GC (default)    |
| Real-time or low-latency systems | ZGC or Shenandoah  |
| Performance benchmarking         | Epsilon GC         |

---