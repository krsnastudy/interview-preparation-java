## 📘 What is JPA Pagination?

Pagination allows you to **retrieve a subset of records** from a large dataset using:

* **Page number**
* **Page size**
* **Sorting** (optional)

It helps:

* Reduce memory consumption
* Improve performance
* Make API responses scalable

---

## ✅ Step-by-Step JPA Pagination Example

### 1. **Entity Example**

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    // Getters and Setters
}
```

---

### 2. **Repository**

Use `PagingAndSortingRepository` or `JpaRepository` (which extends it):

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

---

### 3. **Service Layer**

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getUsers(int page, int size, String sortField, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") 
                      ? Sort.by(sortField).ascending()
                      : Sort.by(sortField).descending();
                      
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }
}
```

---

### 4. **Controller**

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return userService.getUsers(page, size, sortBy, direction);
    }
}
```

---

## 🔁 Sample API Call

```bash
GET /users?page=1&size=10&sortBy=name&direction=desc
```

Returns:

```json
{
  "content": [...],       // list of users
  "totalPages": 5,
  "totalElements": 48,
  "number": 1,
  "size": 10,
  ...
}
```

---

## 📋 Pagination Object Details

Spring Data JPA returns a `Page<User>` object which includes:

| Field           | Meaning                          |
| --------------- | -------------------------------- |
| `content`       | List of results for the page     |
| `totalPages`    | Total number of pages            |
| `totalElements` | Total records available          |
| `size`          | Number of records per page       |
| `number`        | Current page number (zero-based) |

---

## 📚 Interview Point

| Topic             | Talking Points                         |
| ----------------- | -------------------------------------- |
| Pagination in JPA | Use `Pageable`, `PageRequest`          |
| Sorting           | Use `Sort` object                      |
| Response          | Use `Page<T>` for metadata and content |
| Performance       | Avoid `findAll()` on large datasets    |

---

## 🧠 TL;DR

* Use `PageRequest.of(page, size)` for pagination
* Combine with `Sort.by()` for sorting
* Return `Page<T>` from service/controller for full metadata
* Automatically handled by Spring Data JPA
---