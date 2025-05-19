In **JPA**, you can define **indexes on entity fields** using annotations such as `@Table(indexes = {...})`. These indexes help optimize database queries and improve performance.

---

## ✅ How to Create Indexes in JPA

You define indexes using the `@Table` annotation on the entity, and `@Index` within it.

### 🧪 Example: Index on `email` and `status` fields

```java
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_status_created_at", columnList = "status, createdAt")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String status;

    private LocalDateTime createdAt;
}
```

---

### ⚙️ Breakdown

| Annotation   | Description                                                |
| ------------ | ---------------------------------------------------------- |
| `@Table`     | Specifies the table’s metadata                             |
| `@Index`     | Creates an index in the DB                                 |
| `columnList` | Comma-separated list of fields to be included in the index |
| `name`       | Optional name for the index (recommended for readability)  |

---

## 📌 Notes

* Indexes are created only when using **DDL auto generation** (i.e., `spring.jpa.hibernate.ddl-auto=create`).
* If the database schema is already managed manually, you should create indexes using SQL.
* For **unique indexes**, use `@Column(unique = true)` or `@Table(uniqueConstraints = ...)`.

---

## 🔐 Unique Index Example

```java
@Table(
    name = "employees",
    uniqueConstraints = @UniqueConstraint(name = "uc_email", columnNames = {"email"})
)
```

---

## 📚 Useful Resources

* [JPA @Table Docs](https://docs.oracle.com/javaee/7/api/javax/persistence/Table.html)
* [Hibernate Schema Generation Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#schema-generation)

---

## ✅ TL;DR

* Use `@Table(indexes = { @Index(...) })` to define DB indexes in JPA entities.
* Helps speed up queries on large tables.
* Useful for performance tuning in frequently queried columns like `email`, `status`, `created_at`.

---
