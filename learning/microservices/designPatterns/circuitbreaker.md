## 🧠 **What is a Circuit Breaker?**

A **Circuit Breaker** is a **resilience pattern** used to **prevent cascading failures** in distributed systems (like microservices).

### 🔧 Analogy:
> Just like an electrical circuit breaker cuts off power to prevent fire, a software circuit breaker stops calling a failing service to prevent overloading it.

---

## 🚦 Circuit Breaker States

| State         | Description                                                                 |
|---------------|-----------------------------------------------------------------------------|
| ✅ **Closed**     | Everything is fine, calls are passing normally                           |
| ❌ **Open**       | Service has failed too many times, no calls are made                     |
| ⏳ **Half-Open**  | After a timeout, allows limited test calls to see if service recovered    |
| 🔁 **Closed again**| If test calls succeed, circuit closes again                             |

---

## 🛠️ How to Use Circuit Breaker in Spring Boot 3.x with Resilience4j

### ✅ Step 1: Add Dependencies

If you're using **Maven**:

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

> Spring Boot 3+ needs the Spring AOP dependency for annotations like `@CircuitBreaker`.

---

### ✅ Step 2: Enable Resilience4j Support

No extra annotation is required! Once dependencies are added, you can use the annotations directly.

---

### ✅ Step 3: Annotate Your Service with `@CircuitBreaker`

```java
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyExternalService {

    private final RestTemplate restTemplate = new RestTemplate();

    @CircuitBreaker(name = "myService", fallbackMethod = "fallbackMethod")
    public String callExternalService() {
        // Call to another microservice or slow endpoint
        return restTemplate.getForObject("http://external-service/api/data", String.class);
    }

    public String fallbackMethod(Exception e) {
        return "Fallback response: Service is down.";
    }
}
```

---

### ✅ Step 4: Configure the Circuit Breaker in `application.yml`

```yaml
resilience4j:
  circuitbreaker:
    instances:
      myService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 10s
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
```

### 🔍 Config Explanation:
- `slidingWindowSize`: How many calls to track for stats
- `minimumNumberOfCalls`: Minimum before circuit evaluates failure
- `failureRateThreshold`: % of failures to trigger open circuit
- `waitDurationInOpenState`: Time circuit stays open before testing again
- `permittedNumberOfCallsInHalfOpenState`: Number of trial calls
- `automaticTransitionFromOpenToHalfOpenEnabled`: Auto transition logic

---

## 🎯 Real-World Use Case

Imagine you're calling a **Payment Gateway** that goes down. If you don’t use a circuit breaker:
- You keep retrying
- Each retry adds more delay
- More threads get blocked
- Whole system slows or crashes

✅ With a **Circuit Breaker**:
- First few failures are allowed
- When threshold breaches, circuit **opens**
- No more calls are made
- After `X` seconds, circuit **half-opens**
- If trial calls succeed → circuit **closes**
- If not → stays **open**

---

## 🔄 Optional: Retry + CircuitBreaker Together

```java
import io.github.resilience4j.retry.annotation.Retry;

@CircuitBreaker(name = "myService", fallbackMethod = "fallback")
@Retry(name = "myService")
public String callWithRetry() {
    // ...
}
```

And in `application.yml`:

```yaml
resilience4j:
  retry:
    instances:
      myService:
        maxAttempts: 3
        waitDuration: 2s
```

---

## 📈 Monitoring Circuit Breakers

If you're using **Spring Boot Actuator**, add:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Then enable:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: resilience4j.circuitbreakers
```

Now you can hit:  
➡️ `http://localhost:8080/actuator/resilience4j.circuitbreakers`

---

## 📌 Summary

| Key Concept      | Description                                   |
|------------------|-----------------------------------------------|
| Circuit Breaker  | Prevents repeated calls to failing services   |
| Resilience4j     | Lightweight fault-tolerance library           |
| Annotations      | `@CircuitBreaker`, `@Retry`, `@RateLimiter`   |
| States           | Closed → Open → Half-Open → Closed            |
| Configurable     | Via `application.yml`                         |
| Spring Boot 3.x  | Fully supported with `resilience4j-spring-boot3` |

---

Would you like a **working code project on GitHub** or a guide on combining Circuit Breaker with **WebClient** (non-blocking)?