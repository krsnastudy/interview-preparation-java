### 🧠 What is **Entity Property Validation** in JPA/Spring Boot?

**Entity property validation** ensures that the fields (properties) of a JPA entity meet **certain constraints** (like not null, length, format, etc.) **before persisting them** to the database. This helps catch bad input early — even before hitting the database.

Spring Boot supports this out of the box using **Bean Validation (JSR-380)** with the **Hibernate Validator** implementation.

---

## ✅ Example: Validating a `User` Entity

```java
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Name is mandatory")
    @Size(min = 2, max = 50)
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Min(18)
    private int age;
}
```

### 🧩 Breakdown of Annotations:

| Annotation        | Purpose                            |
| ----------------- | ---------------------------------- |
| `@NotNull`        | Field must not be null             |
| `@Size(min, max)` | String/Collection must be in range |
| `@Email`          | Valid email format                 |
| `@Min`, `@Max`    | Numerical boundaries               |

---

## 🧪 How it Works

When you use `@Valid` in your controller or service layer, Spring Boot triggers validation **automatically**.

### Example: In Controller

```java
@PostMapping("/users")
public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
    userRepository.save(user);
    return ResponseEntity.ok("User created");
}
```

If validation fails, Spring throws a `MethodArgumentNotValidException`, and you can return a custom error response.

---

## 🔄 Validating Nested Objects

```java
public class Order {
    @Valid
    @NotNull
    private Address address;
}
```

`@Valid` tells Spring to recursively validate nested objects like `Address`.

---

## ⚠️ Validation Errors Example (JSON Response)

```json
{
  "timestamp": "2025-05-14T10:10:00",
  "status": 400,
  "errors": [
    "Name is mandatory",
    "Invalid email format"
  ]
}
```

You can handle this using `@ControllerAdvice` for custom error formatting.

---

## 🧩 Commonly Used Validation Annotations

| Annotation                           | Usage                  |
| ------------------------------------ | ---------------------- |
| `@NotNull`, `@NotBlank`, `@NotEmpty` | Required fields        |
| `@Size(min, max)`                    | String, list length    |
| `@Pattern(regexp="")`                | Regex-based validation |
| `@Email`                             | Valid email            |
| `@Min`, `@Max`                       | Numbers                |
| `@Past`, `@Future`                   | Dates                  |

---

## ✅ TL;DR

| Feature                        | Description                                           |
| ------------------------------ | ----------------------------------------------------- |
| Validates entity fields        | Before DB write or in REST requests                   |
| Uses JSR-380 annotations       | Like `@NotNull`, `@Email`, `@Size`, etc.              |
| Auto-integrated in Spring Boot | With Hibernate Validator                              |
| Can be nested and customized   | Supports nested object validation and custom messages |

---

## 📚 Resources

* [Official Bean Validation Spec (JSR-380)](https://beanvalidation.org/2.0/)
* [Baeldung – Bean Validation in Spring Boot](https://www.baeldung.com/spring-boot-bean-validation)
* [Hibernate Validator Docs](https://hibernate.org/validator/)

---
