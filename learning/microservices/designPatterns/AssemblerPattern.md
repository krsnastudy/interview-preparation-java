## 🎯 Purpose of Assembler Pattern

> To **separate transformation logic** between the **domain layer** and the **presentation layer** (e.g., controllers, REST responses).

---

## 🔄 Typical Flow

```text
Controller → DTO → Assembler → Domain Model
                            ↓
               Domain Model → Assembler → DTO → Response
```

It helps decouple your internal domain logic from external-facing DTOs.

---

## 🧱 Real-world Example

### 👨‍💻 Entity Class

```java
public class User {
    private Long id;
    private String username;
    private LocalDate dob;
}
```

### 📦 DTO Class

```java
public class UserDTO {
    private Long id;
    private String username;
    private String dob; // in yyyy-MM-dd format
}
```

### 🛠️ Assembler Class

```java
public class UserAssembler {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setDob(user.getDob().toString());
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setDob(LocalDate.parse(dto.getDob()));
        return user;
    }
}
```

---

## 💡 Where it's used

- In **Spring MVC/REST Controllers**
- In **Clean Architecture** (to protect domain layer)
- With **MapStruct** (a tool that auto-generates assembler logic)

---

## ⚖️ Benefits

| Benefit                         | Why It Matters                          |
|----------------------------------|------------------------------------------|
| Separation of concerns          | Keeps domain logic clean                 |
| Easier testing                  | You can test transformations separately |
| Flexibility in API contracts    | Change DTOs without touching domain     |
| Supports API versioning         | Different DTOs for v1/v2 APIs           |

---

## 🔁 TL;DR (Interview Style):

> The **Assembler Pattern** helps convert between domain models and DTOs. It's essential for separating API layer from business logic and improving maintainability and testability of the code.  
> Commonly seen in Spring Boot apps, especially in layered and clean architectures.

---
