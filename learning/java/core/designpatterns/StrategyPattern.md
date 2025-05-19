### 🧩 Strategy Design Pattern – Explained

The **Strategy Pattern** is a **behavioral design pattern** that enables selecting an algorithm at runtime. It defines a **family of algorithms**, encapsulates each one, and makes them interchangeable.

---

### ✅ When to Use:

* When you have multiple ways (algorithms) to perform a specific behavior.
* When you want to **avoid conditional statements** (`if-else` or `switch`) based on behavior.
* When the behavior can vary independently from the context object using it.

---

### 📦 Real-world Analogy:

Think of a **navigation app**. You can select different **route strategies**: fastest, shortest, or eco-friendly. The algorithm (route strategy) is separate from the app logic and can be chosen at runtime.

---

### 👨‍💻 Java Code Example:

#### Step 1: Create a Strategy Interface

```java
interface PaymentStrategy {
    void pay(int amount);
}
```

#### Step 2: Implement Concrete Strategies

```java
class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class PayPalPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}
```

#### Step 3: Context Class

```java
class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void checkout(int amount) {
        paymentStrategy.pay(amount);
    }
}
```

#### Step 4: Client Code

```java
public class StrategyPatternDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();

        cart.setPaymentStrategy(new CreditCardPayment());
        cart.checkout(100);

        cart.setPaymentStrategy(new PayPalPayment());
        cart.checkout(200);
    }
}
```

---

### 🧠 Interview Short Answer:

> The Strategy Pattern defines a family of interchangeable algorithms, encapsulates each one, and allows them to be selected at runtime. It promotes Open/Closed Principle and helps remove conditional logic.

---

### 🔄 Advantages:

* Reduces conditional statements.
* Improves maintainability.
* Promotes the **Open/Closed Principle** (open for extension, closed for modification).

---
