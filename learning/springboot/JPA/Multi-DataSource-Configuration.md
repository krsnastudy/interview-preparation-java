Here’s a **detailed guide on configuring multiple data sources in Spring Boot** with real code examples, step-by-step explanation, and interview relevance.

---

## ✅ When Do You Need Multi-DataSource?

* Connecting to **two different databases** (e.g., one for user data, one for audit logs).
* Integration between legacy and new systems.
* Read-write separation (e.g., primary + replica).

---

## 🔧 Step-by-Step Configuration

### 🎯 Assume:

* **Primary DB**: `users`
* **Secondary DB**: `audit`
* Tech stack: Spring Boot + JPA + HikariCP

---

### 1️⃣ Add Dependencies in `pom.xml`

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

---

### 2️⃣ application.yml or application.properties

```yaml
spring:
  datasource:
    primary:
      url: jdbc:h2:mem:usersdb
      username: sa
      password:
      driver-class-name: org.h2.Driver

    secondary:
      url: jdbc:h2:mem:auditdb
      username: sa
      password:
      driver-class-name: org.h2.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

---

### 3️⃣ Create Configuration Classes

#### 🔹 Primary DataSource Configuration

```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.test.user.repo",
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDataSourceConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.test.user.entity")
                .persistenceUnit("primary")
                .build();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
```

---

#### 🔹 Secondary DataSource Configuration

```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.test.audit.repo",
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDataSourceConfig {

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.test.audit.entity")
                .persistenceUnit("secondary")
                .build();
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
```

---

### 4️⃣ Define Entities and Repositories

#### `User.java` (Primary DB)

```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
}
```

#### `UserRepository.java`

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
```

#### `AuditLog.java` (Secondary DB)

```java
@Entity
public class AuditLog {
    @Id
    @GeneratedValue
    private Long id;

    private String message;
}
```

#### `AuditRepository.java`

```java
@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long> {}
```

---

### 5️⃣ Use the Repositories in Service

```java
@Service
public class DemoService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuditRepository auditRepo;

    public void createUserAndLog() {
        userRepo.save(new User("krishna"));
        auditRepo.save(new AuditLog("User created"));
    }
}
```

---

## 🔍 Interview Angle

| Topic                     | Explanation                                        |
| ------------------------- | -------------------------------------------------- |
| Why multiple datasources? | Multi-tenancy, legacy system integration           |
| Key challenges            | Properly scoping EntityManagers & Transactions     |
| Annotation use            | `@EnableJpaRepositories`, `@Primary`, `@Qualifier` |
| Pitfalls                  | Mixing entities or scanning incorrect packages     |

---

## 📚 Resources

* [Spring Docs - JPA Multiple DataSources](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.data-access.configure-two-data-sources)
* Baeldung Tutorial: [Multi-DB](https://www.baeldung.com/spring-data-jpa-multiple-databases)
* YouTube: *Tech Primers – Multi DB Spring Boot*

---

## ✅ TL;DR

1. Define separate datasource properties.
2. Create two configuration classes for each datasource.
3. Use `@EnableJpaRepositories` with custom `EntityManagerFactory`.
4. Organize entities and repos in separate packages.
5. Inject and use as usual.

---
