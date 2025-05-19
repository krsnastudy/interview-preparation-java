✅ **Answer:**  
**Multi-catch block** (introduced in **Java 7**) allows you to **catch multiple exceptions** in a **single `catch` block** using a `|` (pipe) symbol.

It **reduces code duplication** when different exceptions can be handled in the same way.

---

### 👉 Basic Syntax:

```java
try {
    // risky code
} catch (IOException | SQLException e) {
    System.out.println("Exception occurred: " + e.getMessage());
}
```

- If the `try` block throws either `IOException` **or** `SQLException`, **this single catch block** will handle it.

---

### 👉 Key Rules:
- Exceptions must be **unrelated** (no parent-child relationship between them).
- The **caught exception variable (`e`) is final** (you **cannot assign** a new value to it inside the catch block).
- **Common handling** logic is applied for all exceptions caught.
- Compiler ensures **no duplicate exception types** inside the `|`.

---

### 👉 Example:

```java
import java.io.*;

public class MultiCatchExample {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
            int data = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Exception handled: " + e.getMessage());
        }
    }
}
```

✅ Here:
- `IOException` (file reading issue)
- `NumberFormatException` (conversion issue)  
  Both are handled by the **same catch block**.

---

### 👉 Wrong Example (Compiler Error):

```java
try {
    // risky code
} catch (IOException | Exception e) { // ❌
    // not allowed
}
```
🔴 **Why error?**
- `IOException` **is a subclass** of `Exception`.
- Multi-catch does **NOT allow parent-child exception types**.

---

### 👉 Internal behavior:

- At runtime, JVM matches the thrown exception **against the multi-catch list**.
- If matched, the first suitable exception in the list will be caught.

---

### ✅ **TL;DR:**
| Feature                | Multi-Catch Block |
|-------------------------|-------------------|
| Introduced in           | Java 7 |
| Syntax                  | `catch (Exception1 | Exception2 e)` |
| Restrictions            | No parent-child relation, `e` is final |
| Benefit                 | Cleaner and less duplicate code |

---

