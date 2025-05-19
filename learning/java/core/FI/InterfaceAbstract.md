## 🔸 **When to Use an Interface**

> ✅ Use **Interface** when you want to define a **contract** or **capability** that multiple unrelated classes can implement.

### 🔧 Real-World Example:
**You want all machines to be "startable", regardless of type.**

```java
interface Startable {
    void start();
}
```

#### 🔹 Classes implementing it:

```java
class Car implements Startable {
    public void start() {
        System.out.println("Car starting...");
    }
}

class WashingMachine implements Startable {
    public void start() {
        System.out.println("Washing machine starting...");
    }
}
```

💡 `Car` and `WashingMachine` are unrelated, but they both **implement the same capability**: `start()`.

---

## 🔸 **When to Use an Abstract Class**

> ✅ Use **Abstract Class** when you want to define a **base class** with **shared implementation** and allow subclasses to extend it.

### 🔧 Real-World Example:
**You want to model all types of bank accounts.**

```java
abstract class BankAccount {
    protected double balance;

    public void deposit(double amount) {
        balance += amount;
    }

    public abstract void withdraw(double amount);
}
```

#### 🔹 Subclasses extending it:

```java
class SavingsAccount extends BankAccount {
    public void withdraw(double amount) {
        if (balance >= amount) balance -= amount;
    }
}

class CurrentAccount extends BankAccount {
    public void withdraw(double amount) {
        balance -= amount;  // May allow overdraft
    }
}
```

💡 Both accounts **share deposit logic**, but differ in `withdraw()` behavior — perfect case for an abstract class.

---

## 🔄 Key Differences Summary

| Feature                   | Interface                          | Abstract Class                      |
|---------------------------|-------------------------------------|-------------------------------------|
| Inheritance Type          | Multiple inheritance supported      | Single inheritance only             |
| Method Implementation     | Only default/static methods (Java 8+) | Can have full method implementations |
| Use Case                  | Define capability/contract          | Define base class with common logic |
| Fields                    | Constants only (`public static final`) | Can have instance variables         |

---

## TL;DR:

- Use **Interface** when different classes need to follow a common **contract** (e.g., `Comparable`, `Runnable`, `Startable`).
- Use **Abstract Class** when classes share **common code and state**, and you're building a class **hierarchy** (e.g., `BankAccount`, `Shape`, `Animal`).
