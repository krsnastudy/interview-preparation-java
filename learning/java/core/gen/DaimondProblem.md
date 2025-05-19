## 💎 What is the Diamond Problem?

Imagine this class hierarchy:

```
    A
   / \
  B   C
   \ /
    D
```

If **class D** inherits methods from **both B and C**, and both B and C override a method from A — then **which method should D inherit**?

This ambiguity is the **diamond problem**.

---

## 👀 Java’s Position Before Java 8

Java **does not allow multiple class inheritance**:

```java
class A {}
class B extends A {}
class C extends A {}
class D extends B, C {} // ❌ Compilation error
```

👉 This avoids ambiguity — **no multiple inheritance of classes.**

---

## 🔄 What Changed in Java 8?

With **default methods** in interfaces, Java allowed interfaces to have concrete methods:

```java
interface A {
    default void hello() { System.out.println("Hello from A"); }
}

interface B extends A {
    default void hello() { System.out.println("Hello from B"); }
}

interface C extends A {
    default void hello() { System.out.println("Hello from C"); }
}

class D implements B, C {
    // Must resolve conflict
    public void hello() {
        B.super.hello(); // or C.super.hello()
    }
}
```

---

## 🔍 How Java Resolves the Diamond Problem

Java follows clear rules:

### ✅ Rule 1: **Class > Interface**
If a method is found in a class (or superclass), it wins over any interface default method.

### ✅ Rule 2: **Most Specific Interface Wins**
If multiple interfaces define the same default method and one interface extends the other, the **more specific (child) interface** wins.

### ✅ Rule 3: **Conflict? You must override**
If multiple interfaces provide the same default method and are unrelated (i.e., no inheritance between them), **the compiler forces you to override it**.

---

## 🧪 Real Example

```java
interface A {
    default void greet() { System.out.println("Hello from A"); }
}

interface B {
    default void greet() { System.out.println("Hello from B"); }
}

class MyClass implements A, B {
    // Conflict! Must override
    public void greet() {
        // Resolve explicitly
        A.super.greet();
        // B.super.greet(); // also valid
    }
}
```

✅ This compiles fine because `MyClass` **resolves the conflict manually**.

---

## 🧠 TL;DR

- 🧩 The **diamond problem** arises with multiple inheritance and method conflicts.
- 🚫 Java avoids it with **no multiple class inheritance**.
- ✅ In **Java 8**, default methods in interfaces reintroduce this issue.
- 🤝 Java resolves it with clear **rules**, and if ambiguity remains, **you must override**.

However, Java **does support multiple inheritance for interfaces**, which can lead to a form of the **diamond problem** in the context of interfaces. Let's break this down clearly and also look at how Java resolves it.

---

# ✅ The Diamond Problem in Java (with Interfaces)

### Scenario:
Imagine you have two interfaces, both defining a method with the same signature, and a class implements both interfaces. If the class doesn't implement the method, the compiler would not know which method to call, leading to ambiguity — the **diamond problem**.

---

### Example Code:

```java
interface A {
    default void print() {
        System.out.println("A");
    }
}

interface B {
    default void print() {
        System.out.println("B");
    }
}

class C implements A, B {
    // Does not override print()
}
```

Here, **C** implements both **A** and **B**, which both have the `print()` method. If **C** does not override the method, Java doesn't know which version of `print()` to use, resulting in a **compilation error**.

---

# ✅ Java's Resolution for the Diamond Problem

Java **solves** this problem with the **default methods** in interfaces introduced in **Java 8**. When you have **multiple default methods** in interfaces, Java provides a way to explicitly resolve the ambiguity.

You can **override** the method in your class, or if you need to explicitly specify which interface method to call, you can use the `super` keyword in the method definition.

### Solution 1: **Override the method in the class**:
If a class implements two interfaces with the same default method, the class should **override the method** to resolve the ambiguity.

```java
class C implements A, B {
    @Override
    public void print() {
        // You can choose which method to call
        A.super.print();  // Calls A's print
        // OR
        B.super.print();  // Calls B's print
    }
}
```

This resolves the **diamond problem** by allowing you to explicitly choose which default method to use, or you can override the method completely.

### Solution 2: **Explicitly use `super` keyword**:
If you need to call a method from a specific interface within the class:

```java
class C implements A, B {
    @Override
    public void print() {
        A.super.print();  // Calls A's version of print()
        B.super.print();  // Calls B's version of print()
    }
}
```

This way, Java gives you the control to explicitly choose which method from the interface to invoke.

---

# ✅ Key Points:
- **Java does not allow multiple inheritance for classes** — this avoids the classic diamond problem.
- **Java allows multiple inheritance for interfaces**, but **with default methods** in Java 8, you may encounter a form of the diamond problem when different interfaces provide the same default method.
- **Java resolves it** by allowing you to override the method in the implementing class and use the `super` keyword to choose the method from a specific interface.

---

# ✅ TL;DR:
> **Diamond Problem in Java** occurs when an interface implements **multiple default methods** with the same signature.  
> Java resolves this by requiring **explicit overrides** in the implementing class and allows the use of `super` to call the method from specific interfaces.

---

