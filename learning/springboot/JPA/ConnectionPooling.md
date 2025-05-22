Handling **connection pooling** in microservices is crucial for managing database connections efficiently, especially under high concurrency. In Spring Boot-based microservices, **HikariCP** is the default and recommended connection pool.

---

## 🔹 What is Connection Pooling?

Connection pooling is a mechanism to **reuse** database connections instead of opening and closing them frequently. It:

* Reduces latency
* Saves database resources
* Improves throughput

---

## 🔹 Default Pool: HikariCP in Spring Boot

Spring Boot (2.x and above) uses **HikariCP** as its default connection pool because it’s:

* Fast
* Lightweight
* Reliable

---

## 🔹 🔧 How to Configure HikariCP in Spring Boot

### ✅ Step 1: Add database dependency (if not already)

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.postgresql</groupId> <!-- or mysql -->
  <artifactId>postgresql</artifactId>
</dependency>
```

### ✅ Step 2: `application.yml` Configuration Example

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: myuser
    password: mypass
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 30000
      max-lifetime: 600000
      connection-timeout: 30000
      pool-name: HikariPool-Microservice
```

---

## 🔹 HikariCP Parameters Explained

| Property             | Meaning                                   |
| -------------------- | ----------------------------------------- |
| `maximum-pool-size`  | Max number of connections in the pool     |
| `minimum-idle`       | Minimum number of idle connections        |
| `idle-timeout`       | Max idle time before releasing connection |
| `max-lifetime`       | Max age of a connection before retiring   |
| `connection-timeout` | Max wait time for getting a connection    |
| `pool-name`          | Name of the connection pool               |

---

## 🔎 Common Issues & Resolutions

### ❌ **Connection Leak / Pool Exhausted**

* **Cause**: Connections not closed properly or long-held transactions.
* **Fix**:

    * Ensure all DB connections are released.
    * Use Spring's `@Transactional` properly.
    * Enable leak detection:

      ```yaml
      spring.datasource.hikari.leak-detection-threshold: 2000
      ```

### ❌ **Connection Timeout**

* **Cause**: Not enough connections or slow queries.
* **Fix**:

    * Increase `maximum-pool-size`
    * Optimize SQL queries
    * Investigate DB performance

### ❌ **Frequent Disconnections**

* **Cause**: Firewall or DB killing idle connections.
* **Fix**:

    * Reduce `idle-timeout`
    * Implement DB keep-alive query (`validationQuery`)

      ```yaml
      spring.datasource.hikari.validation-timeout: 5000
      ```

---

## 🧪 Tips for Observability

Use **Actuator Metrics**:

```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Then monitor:

```
/actuator/metrics/hikaricp.connections.active
/actuator/metrics/hikaricp.connections.max
```

Use **Grafana + Prometheus** for visual dashboards.

---

## 🛠️ Runtime Debug

Enable Hikari logs for debugging connection issues:

```yaml
logging:
  level:
    com.zaxxer.hikari: DEBUG
```

---

## ✅ TL;DR

| Topic                      | Summary                                                     |
| -------------------------- | ----------------------------------------------------------- |
| **Connection Pooling**     | Reuses DB connections, improves performance                 |
| **Default in Spring Boot** | HikariCP                                                    |
| **Key Configs**            | `max-pool-size`, `idle-timeout`, `leak-detection-threshold` |
| **Common Issues**          | Pool exhaustion, leaks, timeouts                            |
| **Tools**                  | Spring Actuator, Prometheus, Grafana for monitoring         |

---
