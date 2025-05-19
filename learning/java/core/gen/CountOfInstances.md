To **keep a count of instances** of a class in Java, the **common technique** is to use a **static variable**.  
Because a `static` variable belongs to the **class**, not to any specific instance — so all instances can share and update it.

---

# ✅ Example:

```java
public class MyClass {
    
    private static int instanceCount = 0; // Static counter
    
    public MyClass() {
        instanceCount++; // Increment in constructor
    }
    
    public static int getInstanceCount() {
        return instanceCount;
    }
    
    public static void resetCount() {
        instanceCount = 0; // Optional: if you want to reset manually
    }
    
    // Other class members
}
```

### Usage:

```java
public class Test {
    public static void main(String[] args) {
        MyClass a = new MyClass();
        MyClass b = new MyClass();
        MyClass c = new MyClass();

        System.out.println("Instances created: " + MyClass.getInstanceCount());
    }
}
```

### Output:
```
Instances created: 3
```

---

# 🛠 How it Works Internally:
- `instanceCount` is **shared** across all objects.
- Every time you call the constructor `new MyClass()`, the counter increases by 1.
- You can **fetch** it anytime via a static method like `getInstanceCount()`.

---

# ⚡ Important points for Interview:

| Point | Explanation |
|:---|:---|
| `static` variable | Belongs to class, shared across objects |
| Increment in constructor | Each time object is created |
| Thread Safety | If multiple threads create objects, you may need to synchronize (`AtomicInteger`) |
| Reset Option | You can add a manual reset method if needed |

---

# ⚡ Bonus: **Thread Safe** Version (Using `AtomicInteger`)

```java
import java.util.concurrent.atomic.AtomicInteger;

public class SafeMyClass {
    
    private static final AtomicInteger instanceCount = new AtomicInteger(0);
    
    public SafeMyClass() {
        instanceCount.incrementAndGet(); // Thread-safe increment
    }
    
    public static int getInstanceCount() {
        return instanceCount.get();
    }
}
```

✅ This ensures that even in **multi-threaded environments**, instance count is correct.

---

# 📢 **TL;DR**

- Use `static int` counter.
- Increment inside the **constructor**.
- Use `AtomicInteger` for **thread safety** if required.

---
