# Real-Life Functional Interface Program — E-Commerce Order Processing System

---

## Scenario

We'll build an **Order Processing Pipeline** for an e-commerce platform — a scenario every Java developer can relate to. It uses all four core functional interfaces **collaboratively**, including their default/static methods.

```
Supplier   → Generates new Order objects (order source)
Function   → Transforms Order → discounted Order → formatted Order
Predicate  → Validates Order (eligibility checks, filters)
Consumer   → Consumes final Order (print, save, notify)
```

---

## Complete Program

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class OrderProcessingSystem {

    // ─────────────────────────────────────────────
    // Domain Model
    // ─────────────────────────────────────────────
    static class Order {
        String customerName;
        double amount;
        String city;
        boolean isPrime;

        Order(String customerName, double amount, String city, boolean isPrime) {
            this.customerName = customerName;
            this.amount = amount;
            this.city = city;
            this.isPrime = isPrime;
        }

        @Override
        public String toString() {
            return String.format("Order[customer=%s, amount=%.2f, city=%s, prime=%b]",
                    customerName, amount, city, isPrime);
        }
    }

    public static void main(String[] args) {

        // ═══════════════════════════════════════════════════
        // 1. SUPPLIER<T> — Generates/produces orders (no input, returns output)
        // ═══════════════════════════════════════════════════
        Supplier<Order> orderSupplier1 = () -> new Order("Ravi Kumar", 4500.00, "Bengaluru", true);
        Supplier<Order> orderSupplier2 = () -> new Order("Alice Sharma", 800.00, "Mumbai", false);
        Supplier<Order> orderSupplier3 = () -> new Order("Zara Khan", 12000.00, "Delhi", true);

        List<Supplier<Order>> orderSuppliers = List.of(orderSupplier1, orderSupplier2, orderSupplier3);

        // Generate all orders by invoking each Supplier's get() method
        List<Order> incomingOrders = orderSuppliers.stream()
                .map(Supplier::get)     // .get() — the single abstract method of Supplier
                .collect(Collectors.toList());

        System.out.println("========== STEP 1: Orders Generated (Supplier) ==========");
        incomingOrders.forEach(System.out::println);

        // ═══════════════════════════════════════════════════
        // 2. PREDICATE<T> — Filters/validates orders (input -> boolean)
        // ═══════════════════════════════════════════════════
        Predicate<Order> isHighValue   = order -> order.amount > 1000;
        Predicate<Order> isPrimeMember = order -> order.isPrime;
        Predicate<Order> isFromMetro   = order -> List.of("Bengaluru", "Mumbai", "Delhi").contains(order.city);

        // Predicate.and() — combine multiple conditions (ALL must be true)
        Predicate<Order> eligibleForDiscount = isHighValue.and(isPrimeMember);

        // Predicate.or() — combine conditions (ANY can be true)
        Predicate<Order> eligibleForFreeShipping = isPrimeMember.or(isHighValue);

        // Predicate.negate() — invert the condition
        Predicate<Order> notEligibleForDiscount = eligibleForDiscount.negate();

        System.out.println("\n========== STEP 2: Predicate Checks ==========");
        for (Order order : incomingOrders) {
            System.out.printf("%-20s | High Value: %-5b | Discount Eligible: %-5b | Free Shipping: %-5b%n",
                    order.customerName,
                    isHighValue.test(order),
                    eligibleForDiscount.test(order),
                    eligibleForFreeShipping.test(order));
        }

        // Filter only discount-eligible orders using the combined Predicate
        List<Order> discountEligibleOrders = incomingOrders.stream()
                .filter(eligibleForDiscount)      // .test() used internally by filter()
                .collect(Collectors.toList());

        System.out.println("\nOrders eligible for discount:");
        discountEligibleOrders.forEach(System.out::println);

        // ═══════════════════════════════════════════════════
        // 3. FUNCTION<T,R> — Transforms orders (input -> output, different type)
        // ═══════════════════════════════════════════════════

        // Function 1: Apply 10% discount to amount, return updated Order
        Function<Order, Order> applyDiscount = order -> {
            double discounted = order.amount * 0.90;
            return new Order(order.customerName, discounted, order.city, order.isPrime);
        };

        // Function 2: Add flat "prime shipping fee waiver" note by adjusting amount
        Function<Order, Order> applyPrimeBenefit = order -> {
            if (order.isPrime) {
                return new Order(order.customerName, order.amount - 50, order.city, order.isPrime);
            }
            return order;
        };

        // Function 3: Convert final Order to a formatted receipt String
        Function<Order, String> generateReceipt = order ->
                String.format("RECEIPT -> %s | Final Amount: ₹%.2f | City: %s",
                        order.customerName, order.amount, order.city);

        // Function.andThen() — chain functions: apply discount THEN prime benefit THEN receipt
        Function<Order, Order> fullPricingPipeline = applyDiscount.andThen(applyPrimeBenefit);

        // Function.compose() — reverse order chaining (runs 'before' first)
        // Here demonstrating: generateReceipt happens AFTER fullPricingPipeline via andThen
        Function<Order, String> completeOrderProcessing = fullPricingPipeline.andThen(generateReceipt);

        // Function.identity() — returns input unchanged (useful in Collectors.toMap etc.)
        Function<Order, Order> noOpFunction = Function.identity();

        System.out.println("\n========== STEP 3: Function Transformations ==========");
        List<String> receipts = discountEligibleOrders.stream()
                .map(completeOrderProcessing)   // .apply() used internally by map()
                .collect(Collectors.toList());

        receipts.forEach(System.out::println);

        // ═══════════════════════════════════════════════════
        // 4. CONSUMER<T> — Consumes final data (input -> void, side-effects: print/save/notify)
        // ═══════════════════════════════════════════════════

        Consumer<String> printReceipt = receipt -> System.out.println("[PRINTER] " + receipt);

        Consumer<String> saveToDatabase = receipt -> System.out.println("[DATABASE] Saved: " + receipt);

        Consumer<String> sendSmsNotification = receipt -> System.out.println("[SMS] Notification sent for: " + receipt);

        // Consumer.andThen() — chain multiple consumers to run sequentially on same input
        Consumer<String> fullNotificationPipeline = printReceipt
                .andThen(saveToDatabase)
                .andThen(sendSmsNotification);

        System.out.println("\n========== STEP 4: Consumer Actions (Chained) ==========");
        receipts.forEach(fullNotificationPipeline);   // .accept() used internally by forEach()

        // ═══════════════════════════════════════════════════
        // 5. ALL FOUR WORKING TOGETHER — End-to-End Pipeline
        // ═══════════════════════════════════════════════════
        System.out.println("\n========== STEP 5: Full End-to-End Pipeline ==========");

        Supplier<Order> newOrderSource = () -> new Order("Kiran Rao", 2500.00, "Bengaluru", true);
        Predicate<Order> validationCheck = isHighValue.and(isFromMetro);
        Function<Order, String> processingChain = applyDiscount
                .andThen(applyPrimeBenefit)
                .andThen(generateReceipt);
        Consumer<String> deliveryChain = printReceipt.andThen(saveToDatabase);

        // The full collaborative flow:
        Order newOrder = newOrderSource.get();                  // SUPPLIER produces
        if (validationCheck.test(newOrder)) {                   // PREDICATE validates
            String finalReceipt = processingChain.apply(newOrder); // FUNCTION transforms
            deliveryChain.accept(finalReceipt);                  // CONSUMER consumes
        } else {
            System.out.println("Order rejected: " + newOrder);
        }
    }
}
```

---

## Output

```
========== STEP 1: Orders Generated (Supplier) ==========
Order[customer=Ravi Kumar, amount=4500.00, city=Bengaluru, prime=true]
Order[customer=Alice Sharma, amount=800.00, city=Mumbai, prime=false]
Order[customer=Zara Khan, amount=12000.00, city=Delhi, prime=true]

========== STEP 2: Predicate Checks ==========
Ravi Kumar           | High Value: true  | Discount Eligible: true  | Free Shipping: true 
Alice Sharma         | High Value: false | Discount Eligible: false | Free Shipping: false
Zara Khan            | High Value: true  | Discount Eligible: true  | Free Shipping: true 

Orders eligible for discount:
Order[customer=Ravi Kumar, amount=4500.00, city=Bengaluru, prime=true]
Order[customer=Zara Khan, amount=12000.00, city=Delhi, prime=true]

========== STEP 3: Function Transformations ==========
RECEIPT -> Ravi Kumar | Final Amount: ₹4000.00 | City: Bengaluru
RECEIPT -> Zara Khan | Final Amount: ₹10750.00 | City: Delhi

========== STEP 4: Consumer Actions (Chained) ==========
[PRINTER] RECEIPT -> Ravi Kumar | Final Amount: ₹4000.00 | City: Bengaluru
[DATABASE] Saved: RECEIPT -> Ravi Kumar | Final Amount: ₹4000.00 | City: Bengaluru
[SMS] Notification sent for: RECEIPT -> Ravi Kumar | Final Amount: ₹4000.00 | City: Bengaluru
[PRINTER] RECEIPT -> Zara Khan | Final Amount: ₹10750.00 | City: Delhi
[DATABASE] Saved: RECEIPT -> Zara Khan | Final Amount: ₹10750.00 | City: Delhi
[SMS] Notification sent for: RECEIPT -> Zara Khan | Final Amount: ₹10750.00 | City: Delhi

========== STEP 5: Full End-to-End Pipeline ==========
[PRINTER] RECEIPT -> Kiran Rao | Final Amount: ₹2200.00 | City: Bengaluru
[DATABASE] Saved: RECEIPT -> Kiran Rao | Final Amount: ₹2200.00 | City: Bengaluru
```

---

## How Each Functional Interface Collaborates

```
Supplier<Order>                    →  produces a new Order (no input)
        │  .get()
        ▼
Predicate<Order>                   →  validates the Order (Order -> boolean)
        │  .test()  +  .and() / .or() / .negate()
        ▼
Function<Order, Order>             →  transforms Order -> Order (apply discount)
        │  .apply()  +  .andThen() (chains to next function)
        ▼
Function<Order, String>            →  transforms Order -> String (generate receipt)
        │  .apply()
        ▼
Consumer<String>                   →  consumes the receipt (print, save, notify)
        │  .accept()  +  .andThen() (chains to next consumer)
        ▼
      [ Side effects: printed, saved, SMS sent ]
```

---

## Breakdown of Each Interface's Role

| Interface | Signature | Role in This Program | Key Method(s) Used |
|---|---|---|---|
| `Supplier<T>` | `() -> T` | Generates Order objects from nothing | `.get()` |
| `Predicate<T>` | `T -> boolean` | Validates eligibility (high value, prime, metro) | `.test()`, `.and()`, `.or()`, `.negate()` |
| `Function<T,R>` | `T -> R` | Transforms Order (apply discount, generate receipt) | `.apply()`, `.andThen()`, `.compose()`, `Function.identity()` |
| `Consumer<T>` | `T -> void` | Final side-effect actions (print, save, SMS) | `.accept()`, `.andThen()` |

---

## Interview Insights 🎯

**Q1. Why does `Function` support both `andThen()` and `compose()` but `Consumer` only supports `andThen()`?**
> `Consumer` returns `void` — there's nothing to pass forward as input to a "before" function, so `compose()` makes no sense. `Function` returns a value, so you can chain in either direction: `f.andThen(g)` = `g(f(x))`, while `f.compose(g)` = `f(g(x))`.

**Q2. Why doesn't `Predicate` have an `andThen()` method?**
> `Predicate` returns `boolean`, not a value that can be transformed downstream. Instead, it provides boolean-algebra-specific methods: `and()`, `or()`, `negate()` — because combining predicates is a logical operation, not a data transformation.

**Q3. What is the real-world benefit of chaining functions instead of writing one big method?**
> Chaining creates a **pipeline of single-responsibility functions** — each function does ONE thing (apply discount, apply prime benefit, generate receipt). This is more testable, reusable, and readable than one large monolithic method. It mirrors the Decorator/Pipeline design pattern.

**Q4. Can you use `BiFunction` or `BiConsumer` instead in this scenario?**
> Yes — if a transformation needed two inputs (e.g., `Order` + `TaxRate` → discounted `Order`), you'd use `BiFunction<Order, Double, Order>`. This program deliberately uses single-arg interfaces to demonstrate composition clearly.

**Q5. What's the performance implication of chaining many functions with `andThen()`?**
> Each `andThen()` call wraps the previous function in a new lambda — creating a small object overhead per link. For typical business pipelines (a handful of steps) this is negligible. For extremely hot loops (millions of calls/sec), consider writing a single combined lambda instead.

**Q6. How would you unit test the `eligibleForDiscount` Predicate in isolation?**
```java
@Test
void testEligibleForDiscount() {
    Predicate<Order> isHighValue = order -> order.amount > 1000;
    Predicate<Order> isPrimeMember = order -> order.isPrime;
    Predicate<Order> combined = isHighValue.and(isPrimeMember);

    Order testOrder = new Order("Test", 1500, "Bengaluru", true);
    assertTrue(combined.test(testOrder));
}
```
> Because each Predicate is a small, pure function, it's trivially unit-testable in isolation — a major advantage of functional composition over embedded if-else logic.

---

## Resources 📚
- [Function Interface — Oracle Docs](https://docs.oracle.com/en/java/docs/api/java.base/java/util/function/Function.html)
- [Predicate Interface — Oracle Docs](https://docs.oracle.com/en/java/docs/api/java.base/java/util/function/Predicate.html)
- [Functional Interfaces in Java 8 — Baeldung](https://www.baeldung.com/java-8-functional-interfaces)
- Practice: [JDoodle Java Compiler](https://www.jdoodle.com/online-java-compiler)