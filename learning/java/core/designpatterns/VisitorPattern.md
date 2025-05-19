### 🧭 Visitor Design Pattern in Java

The **Visitor Design Pattern** is a **behavioral design pattern** that lets you **separate an algorithm** from the objects on which it operates. This allows you to add new operations to existing object structures **without modifying their classes**.

---

### ✅ When to Use

* When you want to perform **operations on a group of similar objects**.
* When you need to **add new functionality** without altering the structure of the objects.
* When your object structure is **stable**, but you need to frequently **add behaviors**.

---

### 📦 Real-world Analogy

Think of a **tax inspector** visiting different types of properties (residential, commercial) and calculating tax. You don’t change the properties themselves but let the visitor apply a new operation.

---

### 👨‍💻 Java Code Example

#### Step 1: Define an interface for the element

```java
interface Item {
    void accept(Visitor visitor);
}
```

#### Step 2: Concrete classes implementing Item

```java
class Book implements Item {
    int price = 50;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class Pen implements Item {
    int price = 10;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
```

#### Step 3: Visitor Interface

```java
interface Visitor {
    void visit(Book book);
    void visit(Pen pen);
}
```

#### Step 4: Concrete Visitor Implementation

```java
class ShoppingCartVisitor implements Visitor {
    int total = 0;

    @Override
    public void visit(Book book) {
        total += book.price;
    }

    @Override
    public void visit(Pen pen) {
        total += pen.price;
    }

    public int getTotal() {
        return total;
    }
}
```

#### Step 5: Client Code

```java
public class VisitorPatternDemo {
    public static void main(String[] args) {
        Item[] items = new Item[] { new Book(), new Pen() };

        ShoppingCartVisitor visitor = new ShoppingCartVisitor();
        for (Item item : items) {
            item.accept(visitor);
        }

        System.out.println("Total Cost = " + visitor.getTotal());
    }
}
```

---

### 🧠 Interview Short Answer

> The Visitor pattern allows adding new behaviors to classes without modifying them. It involves creating a `Visitor` interface with `visit()` methods for each type, and element classes (`accept()` method) that delegate behavior to the visitor.

---
