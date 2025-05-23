Excellent question — one that often appears in experienced Java interviews!

---

### ✅ **Interview Answer: What is the performance impact of not overriding `equals()` and `hashCode()` in a custom class?**

If you don't override `equals()` and `hashCode()` in a custom object class (like `Employee`, `Customer`, etc.), **you'll face functional and performance issues** — especially when using **collections like `HashMap`, `HashSet`, or `Hashtable`**.

---

### 🔍 **Impact if NOT Overridden**

#### 1. **Incorrect Behavior in Hash-based Collections**

* `HashMap` and `HashSet` use **hashCode** to locate buckets and **equals** to resolve collisions.
* Without overriding, **Object's default `hashCode()` uses memory address**, and `equals()` compares references (not content).
* **Result:** Two logically equal objects (same fields) are treated as **different** ⇒ duplicate entries allowed in `Set`, unexpected behavior in `Map`.

#### 2. **Performance Degradation**

* All entries may **fall into the same hash bucket** if the default `hashCode()` produces poor distribution.
* Then every lookup in `HashMap` or `HashSet` becomes **O(n)** (linear search using `equals`), instead of **O(1)** expected performance.
* This defeats the **main advantage of hash-based collections**.

#### 3. **Issues with Collections.contains() / remove()**

* Methods like `.contains()`, `.remove()` rely on `equals()` and `hashCode()`.
* Without proper override, these operations **fail to locate objects**, causing bugs.

---

### ✅ **Real-world Example**

```java
Set<Employee> set = new HashSet<>();
set.add(new Employee(101, "Krishna"));
set.add(new Employee(101, "Krishna")); // duplicate? Not without overriding equals/hashCode!

System.out.println(set.size()); // Will print 2 instead of 1
```

---

### 🧠 Interview Tip:

> "In any domain-driven or enterprise application, overriding `equals()` and `hashCode()` is **critical** when storing custom objects in Sets or Maps. Failure to do so causes both **functional bugs** and **performance bottlenecks**."

---

### TL;DR:

| Without Overriding   | Impact                                      |
| -------------------- | ------------------------------------------- |
| Default `equals()`   | Reference comparison only                   |
| Default `hashCode()` | Based on memory address                     |
| Functional Impact    | Duplicates in `HashSet`, lookup fails       |
| Performance Impact   | Hash collisions → O(n) time instead of O(1) |

