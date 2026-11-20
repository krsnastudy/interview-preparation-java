### 🔒 1. **Object-Level Locking (Instance Locking)**

This type of locking is applied **on the instance (object) of the class**.

#### ✅ Characteristics:
- Only one thread can execute a **synchronized instance method/block** on a particular object.
- Other threads can still access the same method on different objects.

#### 🧪 Example:
```java
public class Printer {
    public synchronized void print(String message) {
        System.out.println(Thread.currentThread().getName() + " : " + message);
    }
}
```

Here, the lock is on `this` — the **current object instance**.

---

### 🧪 Test Scenario:
```java
public class TestObjectLock {
    public static void main(String[] args) {
        Printer printer1 = new Printer();
        Printer printer2 = new Printer();

        Runnable task1 = () -> printer1.print("Hello from printer1");
        Runnable task2 = () -> printer2.print("Hello from printer2");

        new Thread(task1).start();
        new Thread(task2).start(); // Can run in parallel as objects are different
    }
}
```

#### ✅ Use Case:
- When you want **each object** to be **independently synchronized**.

---

### 🏛️ 2. **Class-Level Locking (Static Locking)**

This locking is applied on the **Class object** associated with the class (i.e., `ClassName.class`).

#### ✅ Characteristics:
- Used when **synchronizing static methods** or **synchronizing using `ClassName.class`**.
- Only **one thread at a time** can access **any static synchronized method** for the class — even across multiple objects.

#### 🧪 Example:
```java
public class Printer {
    public static synchronized void print(String message) {
        System.out.println(Thread.currentThread().getName() + " : " + message);
    }
}
```

Here, the lock is on the `Printer.class` — **one lock per class, not per object**.

---

### 🧪 Test Scenario:
```java
public class TestClassLock {
    public static void main(String[] args) {
        Runnable task1 = () -> Printer.print("Static print 1");
        Runnable task2 = () -> Printer.print("Static print 2");

        new Thread(task1).start();
        new Thread(task2).start(); // Will execute one after another
    }
}
```

#### ✅ Use Case:
- When you need to **synchronize access to static/shared resources** (like counters, caches, etc.)

---

### 🆚 Key Differences

| Feature                  | Object-Level Locking                     | Class-Level Locking                        |
|--------------------------|-------------------------------------------|---------------------------------------------|
| Lock Scope              | Lock on individual object instance        | Lock on `ClassName.class` object            |
| Applied On              | Non-static synchronized methods/blocks    | Static synchronized methods/blocks          |
| Affects Other Instances | No                                        | Yes — affects all instances of the class    |
| Number of Locks         | One per object                            | One per class                               |
| Use Case                | Instance-specific data                    | Static/shared data                          |

---

### 💡 Interview Tip:
> "Object-level locking is useful when you have independent state in different instances, while class-level locking is used when you need to protect static data shared across threads."

---

Would you like me to show this with a real-world analogy like a **bank account or printer pool**?