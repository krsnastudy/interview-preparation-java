## ✅ 🔥 **Top Java 11 Features – Interview Point of View**

### 1. ✅ **Local-Variable Syntax for Lambda Parameters**
- **Feature**: Use `var` in lambda expressions.
- **Why**: Improves code readability and helps with annotations.
```java
list.forEach((var item) -> System.out.println(item));
```

---

### 2. ✅ **New String Methods**
Java 11 introduced several helpful `String` utility methods:

| Method | Description |
|--------|-------------|
| `isBlank()` | Returns true if the string is empty or only whitespace |
| `lines()` | Converts string to a Stream of lines |
| `strip()`, `stripLeading()`, `stripTrailing()` | Unicode-aware trimming |
| `repeat(int)` | Repeats the string n times |

🔸 **Example:**
```java
String text = " Java 11 ";
System.out.println(text.strip());      // "Java 11"
System.out.println("Hello\nWorld".lines().count()); // 2
```

---

### 3. ✅ **Files.readString() and writeString()**
- Simplifies reading and writing text files.
```java
Path path = Path.of("sample.txt");
Files.writeString(path, "Hello Java 11");
String content = Files.readString(path);
```

---

### 4. ✅ **HttpClient API (Standardized)**
- Introduced in Java 9 as incubating, now stable.
- Supports HTTP/1.1 and HTTP/2, with sync and async calls.

🔸 **Example:**
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.github.com"))
    .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.body());
```

---

### 5. ✅ **Optional.isEmpty()**
- Cleaner syntax than `!isPresent()`.
```java
Optional<String> name = Optional.empty();
if (name.isEmpty()) {
    System.out.println("No value");
}
```

---

### 6. ✅ **Running Java Files without Compilation**
- Java 11 allows running `.java` files directly using:
```bash
java HelloWorld.java
```
👉 Useful for scripting or quick testing.

---

### 7. ✅ **Deprecations and Removals**
- Java EE and CORBA modules removed.
- Encourage modular and clean applications.

---

## 📌 Real-Time Use Case in Projects:
> In my recent microservices project, I used Java 11's `HttpClient` to make non-blocking external API calls, and `Files.readString()` to read config data in a clean way. The `var` keyword with lambdas also made the code more concise and readable.

---

## 📌 Summary (TL;DR for Interview):

| Feature | Benefit |
|--------|---------|
| `var` in lambdas | Improves code readability |
| String enhancements | Easier text handling |
| New File IO methods | Cleaner file reading/writing |
| Standardized HttpClient | Async/sync HTTP support |
| `Optional.isEmpty()` | Better null safety |
| Run Java without compile | Quick prototyping |

---

Let me know if you want a side-by-side comparison with Java 8 or 17 for a more impressive interview pitch!