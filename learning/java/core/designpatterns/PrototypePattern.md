### 🧬 Prototype Design Pattern – Explained

The **Prototype Pattern** is a **creational design pattern** that is used to create **clones (copies)** of existing objects without depending on their concrete classes.

---

### ✅ When to Use:

* When object creation is **costly** (e.g., network, database operations).
* When objects are **mostly similar** but with slight variations.
* To **avoid using `new` keyword** repeatedly.

---

### 🧠 Interview Short Answer:

> The Prototype Pattern lets you clone existing objects without coupling to their specific classes, using a common interface with a `clone()` method.

---

### 📦 Real-World Analogy:

Think of creating **multiple resumes** for job applications. Instead of rewriting the whole resume, you copy the original and change a few details.

---

### 👨‍💻 Java Example:

#### Step 1: Prototype Interface

```java
public interface Prototype extends Cloneable {
    Prototype clone();
}
```

#### Step 2: Concrete Class

```java
public class Employee implements Prototype {
    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Prototype clone() {
        return new Employee(id, name); // deep copy
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "'}";
    }
}
```

#### Step 3: Client Code

```java
public class PrototypePatternDemo {
    public static void main(String[] args) {
        Employee emp1 = new Employee(101, "Krishna");
        Employee emp2 = (Employee) emp1.clone();

        System.out.println(emp1);
        System.out.println(emp2);
    }
}
```

---

### 🔁 Deep Copy vs Shallow Copy

* **Shallow Copy**: Copies only the reference for mutable fields.
* **Deep Copy**: Creates **new copies** of all referenced objects (recommended for mutable fields like `List`, `Map`, etc.)

---

### ✅ Advantages:

* **Improves performance** by avoiding repeated object creation.
* **Reduces boilerplate code**.
* Allows for **dynamic object creation at runtime**.

---

### 📌 Use Case in Java:

Java's `Object.clone()` is a built-in example of the prototype pattern.

```java
MyObject copy = (MyObject) original.clone();
```

---

### 🔄 Comparison with Other Creational Patterns:

| Pattern          | Use When                                 |
| ---------------- | ---------------------------------------- |
| Singleton        | Only one instance needed                 |
| Factory Method   | Subclasses decide object creation        |
| Abstract Factory | Create families of related objects       |
| Builder          | Step-by-step object construction         |
| **Prototype**    | Clone object instead of building new one |

---

