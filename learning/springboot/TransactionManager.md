# ✅ How to Implement Transaction Manager in Spring Boot

In **Spring Boot**, a **Transaction Manager** is responsible for:
- Starting a transaction
- Committing it if everything is fine
- Rolling it back if an exception occurs

Spring Boot **auto-configures** the `PlatformTransactionManager` based on your database (like HikariCP + JPA + Hibernate).

You mainly need:
- **Proper configuration** (or sometimes custom TransactionManager)
- **Using `@Transactional`** to manage transactions declaratively.

---

# 🛠 Basic Setup for Transaction Management

✅ Step 1: **Add dependencies**

In your `pom.xml` (for JPA + Transaction Management):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId> <!-- Or any database like MySQL -->
    <artifactId>h2</artifactId>
</dependency>
```

---

✅ Step 2: **Enable Transaction Management**

Spring Boot automatically enables it with JPA Starter.

**Optional** if you want to be explicit:
```java
@EnableTransactionManagement
@SpringBootApplication
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

---

✅ Step 3: **Use @Transactional in your Service Layer**

```java
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void createEmployee(Employee emp) {
        employeeRepository.save(emp);

        // simulate some exception
        if(emp.getName().equals("error")) {
            throw new RuntimeException("Simulated error");
        }
    }
}
```

- If an exception occurs, **transaction will rollback automatically**.
- If no error, **transaction will commit**.

---

✅ Step 4: **How does Spring Boot pick the Transaction Manager?**

Spring Boot auto-configures one based on available libraries:

| Library Available | Transaction Manager Picked |
|--------------------|-----------------------------|
| Spring Data JPA     | `JpaTransactionManager` |
| JDBC Template       | `DataSourceTransactionManager` |
| MongoDB             | `MongoTransactionManager` |
| Neo4j               | `Neo4jTransactionManager` |

If multiple managers exist, you can manually specify which one to use using `@Transactional(transactionManager = "name")`.

---

# 🎯 Example: Custom TransactionManager (Advanced Case)

If you want a **custom transaction manager**:

```java
@Configuration
@EnableTransactionManagement
public class TransactionManagerConfig {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        return tm;
    }
}
```

➡️ Spring will now use your **custom transaction manager**.

---

# ✅ Important Points in Interviews:

- Only **unchecked exceptions** (RuntimeException) cause automatic rollback by default.
- For **checked exceptions**, you have to specify manually like this:
  ```java
  @Transactional(rollbackFor = Exception.class)
  ```
- Transaction is **started before method runs** and **committed/rolled back after method finishes**.
- Transactions should **always be in the service layer**, **NOT** in the controller or repository layer.

---

# ✅ TL;DR:

> In Spring Boot, **TransactionManager** is auto-configured.  
> You simply use `@Transactional` at the service layer to control commit/rollback behavior.

---
