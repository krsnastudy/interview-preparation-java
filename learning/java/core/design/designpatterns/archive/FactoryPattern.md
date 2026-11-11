### 🏭 Factory Method Design Pattern – Explained

The **Factory Method Pattern** is a **creational design pattern** that provides an interface for creating objects in a superclass but allows **subclasses to alter the type of objects that will be created**.

---

### ✅ When to Use:

* When the exact type of object is determined by subclasses.
* When you want to delegate object creation to child classes.
* To adhere to **Open/Closed Principle** by avoiding direct instantiation using `new`.

---

### 📦 Real-World Analogy:

Think of a **Document Editor** – it has a method to create documents. Whether it creates a Word, PDF, or Excel document depends on the concrete subclass (e.g., WordApp, PDFApp).

---

### 👨‍💻 Java Example:

#### Step 1: Product Interface

```java
interface Notification {
    void notifyUser();
}
```

#### Step 2: Concrete Products

```java
class EmailNotification implements Notification {
    public void notifyUser() {
        System.out.println("Sending Email Notification");
    }
}

class SMSNotification implements Notification {
    public void notifyUser() {
        System.out.println("Sending SMS Notification");
    }
}
```

#### Step 3: Creator Class with Factory Method

```java
abstract class NotificationFactory {
    public abstract Notification createNotification();
}
```

#### Step 4: Concrete Creators

```java
class EmailNotificationFactory extends NotificationFactory {
    public Notification createNotification() {
        return new EmailNotification();
    }
}

class SMSNotificationFactory extends NotificationFactory {
    public Notification createNotification() {
        return new SMSNotification();
    }
}
```

#### Step 5: Client Code

```java
public class FactoryMethodDemo {
    public static void main(String[] args) {
        NotificationFactory factory = new EmailNotificationFactory();
        Notification notification = factory.createNotification();
        notification.notifyUser();
    }
}
```

---

### 🧠 Interview Short Answer:

> The Factory Method Pattern defines an interface for object creation but allows subclasses to alter the type of objects that will be created. It promotes loose coupling and adheres to the Open/Closed Principle.

---

### 🔄 Benefits:

* Encourages **loose coupling**.
* Promotes **reusability** and **extensibility**.
* Helps in managing and maintaining **object creation logic**.

---

### 🆚 Factory Method vs Abstract Factory:

| Feature          | Factory Method | Abstract Factory          |
| ---------------- | -------------- | ------------------------- |
| Product Type     | Single Product | Multiple Related Products |
| Focus            | One object     | Family of objects         |
| Uses Inheritance | Yes            | Yes + composition         |

---

