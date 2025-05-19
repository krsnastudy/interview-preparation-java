## 🔍 What does `@Async("asyncExecutor")` do?

It tells Spring:
> “Run this method in a separate thread using the executor bean named `asyncExecutor`.”

---

## ✅ How it works (internally):

### 1. **Proxy Creation (AOP)**

Spring creates a **CGLIB or JDK proxy** around the class containing `@Async` methods.

- When a method is called **from outside the bean**, Spring intercepts it.
- It submits the method to the specified `Executor`.

### 2. **Method Execution Offloaded**

The method doesn't run in the caller's thread. Instead, it’s handed off to a thread from the `asyncExecutor` thread pool.

This happens via:
```java
executor.submit(new CallableTask(...));
```

### 3. **Return Types**

- `void` → fire and forget
- `Future<T>` or `CompletableFuture<T>` → allows async result retrieval

---

## 🧱 Required Setup

### 1. Enable Async

```java
@Configuration
@EnableAsync
public class AsyncConfig {
}
```

### 2. Define `asyncExecutor` Bean

```java
@Bean(name = "asyncExecutor")
public Executor asyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(3);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("AsyncThread-");
    executor.initialize();
    return executor;
}
```

### 3. Use `@Async("asyncExecutor")`

```java
@Service
public class EmailService {

    @Async("asyncExecutor")
    public void sendEmail(String email) {
        // time-consuming logic here
        System.out.println("Sending email to " + email);
    }
}
```

---

## 🔁 Thread Flow (Example)

```java
@RestController
public class MailController {

    @Autowired EmailService emailService;

    @GetMapping("/send")
    public String send() {
        emailService.sendEmail("krishna@example.com");
        return "Email triggered!";
    }
}
```

➡️ When `/send` is hit:
- Controller thread returns `"Email triggered!"` immediately.
- `sendEmail()` runs in a thread like: `AsyncThread-1`.

---

## 🔒 Internal Handling

Under the hood, Spring wraps your async method into:
```java
Callable<Object> task = () -> method.invoke(...);
executor.submit(task);
```

> Uses `AbstractAsyncExecutionInterceptor` and `AsyncExecutionAspectSupport`.

---

## 🧠 TL;DR (Interview Style):

> `@Async("asyncExecutor")` tells Spring to run the method in a separate thread using the named executor.  
> It uses AOP proxies to intercept the method call and submit it to the specified `Executor`.  
> Useful for non-blocking, parallel execution in microservices.

---