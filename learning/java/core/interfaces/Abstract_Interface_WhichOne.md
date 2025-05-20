
### ✅ **Interview Answer: “If an abstract class has 100% abstract methods, why not just use that instead of an interface?”**

> “While it's technically possible to create a 100% abstract class, **interfaces and abstract classes serve different design purposes** in Java. Even if an abstract class has only abstract methods, interfaces offer key benefits:
>
> 1. **Multiple Inheritance** – A class in Java **can implement multiple interfaces**, but it **can only extend one abstract class**. So interfaces offer better flexibility for designing decoupled, scalable systems.
>
> 2. **Intent and Semantics** – Interfaces are used to define **capabilities or contracts** (e.g., `Comparable`, `Serializable`). Abstract classes are for **partial implementation** or shared behavior. Using an interface clearly signals "this is a contract."
>
> 3. **Design Flexibility** – Interfaces are preferred when we want to **decouple the implementation from the definition**. This makes testing and mocking easier (especially with tools like Mockito).
>
> 4. **Default and Static Methods** (Java 8+) – Interfaces now support **default and static methods**, so they can also carry behavior, reducing the need to fall back to abstract classes for minor logic sharing.
>
> 5. **Legacy Constraints** – In frameworks like Spring or JPA, **proxying and dynamic implementation** often require interfaces (e.g., for AOP or repository patterns).”

---

### ✅ Real-World Example (to impress interviewer):

> *“For instance, in a microservices system, I define common contracts like `PaymentGateway` or `AuditLogger` as interfaces. This lets different services implement their own versions while allowing me to switch implementations easily using dependency injection.”*

---

### 🔄 Summary:

| Aspect               | Interface                      | Abstract Class                  |
| -------------------- | ------------------------------ | ------------------------------- |
| Multiple inheritance | ✅ Supported                    | ❌ Not supported                 |
| Purpose              | Define **contract/capability** | Provide **base class behavior** |
| Fields               | `public static final` only     | Can have instance variables     |
| Methods              | Abstract, default, static      | Abstract and concrete methods   |
| Use case             | When behavior isn't shared     | When some behavior is shared    |

---

### TL;DR:

> Even if an abstract class is 100% abstract, **interfaces provide better design flexibility, multiple inheritance, and clearer intent**, making them ideal for defining **contracts** in Java.
