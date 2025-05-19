In Java, a **functional interface** is an interface that has exactly one **abstract method**. The purpose of functional interfaces is to be used primarily with **lambda expressions** and **method references**.

### Can a functional interface extend another functional interface?
Yes, a **functional interface can extend another functional interface**, but with a **key restriction**:

- **The extending interface must still have exactly one abstract method**.

### Why?
- A functional interface can extend another functional interface, and in doing so, it **inherits** the abstract method(s) from the parent interface.
- However, if the child interface inherits **more than one abstract method**, it would violate the contract of being a **functional interface** (which can only have one abstract method). In this case, the child interface would no longer be considered a functional interface.

### Example:

```java
@FunctionalInterface
interface A {
    void methodA(); // Single abstract method
}

@FunctionalInterface
interface B extends A {
    void methodB(); // Single abstract method (inherits methodA from A)
}

public class Example {
    public static void main(String[] args) {
        B obj = (methodB) -> System.out.println("MethodB executed");
        obj.methodB(); // This works as expected.
    }
}
```

In this example:
- Interface `B` extends interface `A` and inherits the method `methodA()` from `A`.
- Since `B` has **exactly one abstract method** (`methodB()`), it remains a **functional interface**.
- If `B` had more than one abstract method, it would **not** be considered a functional interface.

### Why can't a functional interface have more than one abstract method?
- The primary goal of functional interfaces is to represent a **single behavior** that can be implemented with a lambda expression or method reference. Multiple abstract methods would introduce ambiguity in the behavior that the lambda expression is supposed to represent, which defeats the purpose of having a functional interface in the first place.

---

### TL;DR:
Yes, a functional interface **can** extend another functional interface, but the extended interface must still have only **one abstract method**. If it has more than one, it would no longer qualify as a functional interface.