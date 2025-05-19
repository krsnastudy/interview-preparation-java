In **JPA (Java Persistence API)**, you can call **stored procedures** using several approaches. Here's a detailed explanation with examples and interview-relevant points.

---

## ✅ Ways to Call Stored Procedures in JPA

### 1. **Using `@NamedStoredProcedureQuery` (Static way)**

Define the stored procedure mapping on the Entity class.

#### 📄 Entity Class Example

```java
@Entity
@NamedStoredProcedureQuery(
    name = "User.getUserByEmail",
    procedureName = "get_user_by_email",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "user_name", type = String.class)
    }
)
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
}
```

#### 📄 Calling from Repository/Service

```java
StoredProcedureQuery query = entityManager
    .createNamedStoredProcedureQuery("User.getUserByEmail")
    .setParameter("email", "test@example.com");

query.execute();
String userName = (String) query.getOutputParameterValue("user_name");
```

---

### 2. **Using `@Procedure` (Spring Data JPA shortcut)**

No need for `EntityManager`.

#### 📄 Repository Example

```java
public interface UserRepository extends JpaRepository<User, Long> {

    @Procedure(name = "get_user_by_email")
    String getUserByEmail(@Param("email") String email);
}
```

> Note: You might need to annotate the entity with `@NamedStoredProcedureQuery` still.

---

### 3. **Using `EntityManager.createStoredProcedureQuery` (Dynamic way)**

This is flexible and doesn't require annotations.

#### 📄 Example

```java
StoredProcedureQuery query = entityManager
    .createStoredProcedureQuery("get_user_by_email")
    .registerStoredProcedureParameter("email", String.class, ParameterMode.IN)
    .registerStoredProcedureParameter("user_name", String.class, ParameterMode.OUT)
    .setParameter("email", "test@example.com");

query.execute();
String userName = (String) query.getOutputParameterValue("user_name");
```

---

## 🧪 Sample Procedure in DB (MySQL Example)

```sql
DELIMITER $$

CREATE PROCEDURE get_user_by_email(IN email VARCHAR(255), OUT user_name VARCHAR(255))
BEGIN
    SELECT name INTO user_name FROM user WHERE user.email = email;
END $$

DELIMITER ;
```

---

## 🧠 Interview Tips

| Question                                                                      | Suggested Answer                                                                                                |
| ----------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Can you call stored procedures in JPA?                                        | Yes, using `@NamedStoredProcedureQuery`, `@Procedure`, or `EntityManager`.                                      |
| What is the difference between `@Procedure` and `@NamedStoredProcedureQuery`? | `@Procedure` is Spring-specific and easier for simple procedures; `@NamedStoredProcedureQuery` is standard JPA. |
| What’s the benefit of using stored procedures?                                | Performance optimization, encapsulated business logic in DB, reduced network traffic.                           |
| What if a procedure returns a result set?                                     | Use `.getResultList()` or map it to an entity/DTO.                                                              |

---

## 📚 Resources

* [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.stored-procedures)
* [Baeldung – Stored Procedures in JPA](https://www.baeldung.com/spring-data-jpa-stored-procedures)
* [Hibernate Docs – Stored Procedures](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql-stored-procedures)

---

## ✅ TL;DR

| Approach                                   | When to Use                        |
| ------------------------------------------ | ---------------------------------- |
| `@NamedStoredProcedureQuery`               | If you prefer JPA standard mapping |
| `@Procedure`                               | For simple, Spring-friendly use    |
| `EntityManager.createStoredProcedureQuery` | For dynamic, flexible use          |

---
