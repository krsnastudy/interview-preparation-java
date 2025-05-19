To perform **inter-service communication asynchronously using `RestTemplate`** in Spring Boot, you can wrap the blocking `RestTemplate` calls inside **`@Async` methods** or use **`CompletableFuture`** to run them in a separate thread.

---

## ✅ What You’ll Learn
- How to use `RestTemplate` in async mode
- How to use `@Async` and `CompletableFuture`
- How to enable async support in Spring Boot
- Example code and best practices

---

## 🔹 Step 1: Enable Async Support

Add `@EnableAsync` to your configuration class or Spring Boot main class.

```java
@SpringBootApplication
@EnableAsync
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

---

## 🔹 Step 2: Create an Async Service for Inter-Service Call

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class MyAsyncRestService {

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<String> callOtherServiceAsync(String url) {
        String response = restTemplate.getForObject(url, String.class);
        return CompletableFuture.completedFuture(response);
    }
}
```

🧠 **Note**: `RestTemplate` itself is **blocking**, so `@Async` offloads that blocking call to a different thread to avoid blocking the main thread.

---

## 🔹 Step 3: Configure the RestTemplate Bean

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

## 🔹 Step 4: Call the Async Method from Controller or Another Service

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class MyController {

    @Autowired
    private MyAsyncRestService asyncService;

    @GetMapping("/trigger")
    public String triggerAsyncCall() throws Exception {
        CompletableFuture<String> future = asyncService.callOtherServiceAsync("http://other-service/api/data");

        // Do other work here...

        String result = future.get();  // Optional: wait if needed
        return "Response from other service: " + result;
    }
}
```

---

## 🔹 Optional: Configure Custom Async Executor

For better thread control, define a custom executor:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncRest-");
        executor.initialize();
        return executor;
    }
}
```

And update the service to use this executor:

```java
@Async("taskExecutor")
public CompletableFuture<String> callOtherServiceAsync(String url) {
    ...
}
```

---

## 🔹 Best Practices
| ✅ Do | ❌ Avoid |
|------|---------|
| Use `@Async` for parallel execution | Blocking in the main thread |
| Use `CompletableFuture` for chaining | Blocking on `future.get()` unless needed |
| Define a custom `ThreadPoolTaskExecutor` | Using the default executor in production |
| Handle timeouts and exceptions gracefully | Silent failures on async methods |

---

## 🎯 Summary

- `RestTemplate` is **blocking**, but you can make it async using `@Async` + `CompletableFuture`.
- Always enable `@EnableAsync` and configure a **thread pool** for real async performance.
- Use async when calling **slow or external services** to improve responsiveness.

---

Would you like a version using **WebClient (non-blocking)** for fully reactive async? It's more modern and recommended for new applications.