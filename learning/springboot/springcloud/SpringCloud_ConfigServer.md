A **Spring Cloud Config Server** is a centralized configuration management solution for **Spring Boot microservices**. It allows your services to fetch configuration values from a **central repository** (like Git, Vault, or a filesystem) instead of hardcoding or packaging them with the app.

---

## ✅ Why Use Config Server?

| Problem                                               | Solution                             |
| ----------------------------------------------------- | ------------------------------------ |
| Duplicated config across services                     | Centralize config in one place       |
| Need to change config without redeploying services    | Dynamic refresh via Spring Cloud Bus |
| Store different profiles (dev, test, prod) separately | Profile-based config files           |
| Secure sensitive config values                        | Store encrypted values in Git/Vault  |

---

## 🏗️ How It Works

```
         +-------------+
         | Git/Vault   |  ← Central config repository
         +------+------+
                |
                ↓
        +---------------+
        | Config Server |  ← Spring Boot App with @EnableConfigServer
        +---------------+
                ↑
                |
      +---------+---------+
      |   Microservice A   |
      |   Microservice B   | ← Fetch config from Config Server
      +--------------------+
```

---

## 🛠️ Step-by-Step Implementation

### 1. **Create Spring Cloud Config Server**

**Add Dependencies (`pom.xml`):**

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

**Main Class:**

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

**`application.yml`:**

```yaml
server:
  port: 8888

spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/your-org/config-repo
          default-label: main
```

---

### 2. **Set Up Git Repo Structure (Central Config)**

Your config repository can have:

```
config-repo/
├── application.yml               # Shared config
├── service-a.yml                 # Service-specific config
├── service-b-dev.yml            # Profile-specific config
```

Example: `service-a.yml`

```yaml
server:
  port: 8081

message: "Hello from config server"
```

---

### 3. **Configure Client Microservices**

**Add Dependency:**

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

**`bootstrap.yml`:**

```yaml
spring:
  application:
    name: service-a
  cloud:
    config:
      uri: http://localhost:8888
      profile: dev
```

> `spring.application.name` = File name in config repo

---

### 4. **Enable Refresh (Optional)**

To allow dynamic config updates without restarting the service:

**Add:**

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-bus-amqp</artifactId> <!-- For RabbitMQ -->
</dependency>
```

**Add `@RefreshScope` on beans:**

```java
@RefreshScope
@RestController
public class GreetingController {
    @Value("${message}")
    private String message;

    @GetMapping("/msg")
    public String getMessage() {
        return message;
    }
}
```

Call `/actuator/refresh` endpoint to reload config.

---

## 📚 Resources

* [Official Spring Cloud Config Docs](https://cloud.spring.io/spring-cloud-config/reference/html/)
* [Spring Cloud GitHub Example](https://github.com/spring-cloud/spring-cloud-config)
* [Baeldung – Spring Cloud Config](https://www.baeldung.com/spring-cloud-configuration)

---

## 💡 Interview Highlights

| Question                            | Sample Answer                                           |
| ----------------------------------- | ------------------------------------------------------- |
| What is Spring Cloud Config Server? | A centralized config service for distributed apps.      |
| How do services fetch config?       | Using `spring.cloud.config.uri` and `application.name`. |
| Where can you store configs?        | Git, Vault, or local filesystem.                        |
| Can config be updated at runtime?   | Yes, using Spring Cloud Bus with `@RefreshScope`.       |

---

## ✅ TL;DR

* Spring Config Server = Centralized config provider
* Backed by Git, Vault, etc.
* Clients fetch config at startup
* Use `@RefreshScope` for dynamic changes
* Avoids duplication and hardcoding across services

---
