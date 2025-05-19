### **🔹 Implementing Interceptor in Spring Boot**
Spring Interceptors are used to **pre-process and post-process HTTP requests**. They allow you to execute logic before and after the request reaches the controller.

---

## **1️⃣ Steps to Implement an Interceptor**
✅ Create an interceptor by implementing `HandlerInterceptor`.  
✅ Override `preHandle()`, `postHandle()`, and `afterCompletion()`.  
✅ Register the interceptor using `WebMvcConfigurer`.

---

### **2️⃣ Add Dependencies**
If you're using Spring Boot, you only need the following dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

### **3️⃣ Create an Interceptor**
The `LoggingInterceptor` logs request details before processing and after response generation.

```java
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    // Executed BEFORE the request reaches the controller
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("[PRE HANDLE] Request URL: " + request.getRequestURL());
        System.out.println("[PRE HANDLE] Method: " + request.getMethod());
        System.out.println("[PRE HANDLE] Request Time: " + LocalDateTime.now());
        return true;  // Returning false will stop the request from proceeding
    }

    // Executed AFTER the controller processes the request but BEFORE sending the response
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        System.out.println("[POST HANDLE] Response Status: " + response.getStatus());
    }

    // Executed AFTER the request is completed
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("[AFTER COMPLETION] Request Completed at: " + LocalDateTime.now());
        if (ex != null) {
            System.out.println("[AFTER COMPLETION] Exception: " + ex.getMessage());
        }
    }
}
```

---

### **4️⃣ Register the Interceptor in Spring Boot**
To activate the interceptor, register it using `WebMvcConfigurer`.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**")  // Apply to specific API paths
                .excludePathPatterns("/api/auth/**"); // Exclude authentication endpoints
    }
}
```

---

### **5️⃣ Test the Interceptor**
#### **Create a Sample REST Controller**
```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Interceptor Example!";
    }
}
```

#### **Test with Curl or Postman**
```bash
curl http://localhost:8080/api/hello
```
**Expected Logs in Console:**
```bash
[PRE HANDLE] Request URL: http://localhost:8080/api/hello
[PRE HANDLE] Method: GET
[PRE HANDLE] Request Time: 2025-04-01T10:00:00
[POST HANDLE] Response Status: 200
[AFTER COMPLETION] Request Completed at: 2025-04-01T10:00:01
```

---

## **🔹 Use Cases for Interceptors**
1️⃣ **Logging**: Track incoming requests and outgoing responses.  
2️⃣ **Authentication & Authorization**: Validate tokens before processing the request.  
3️⃣ **Rate Limiting**: Restrict excessive API calls per user.  
4️⃣ **Global Exception Handling**: Catch and log errors at a central level.  
5️⃣ **Header Manipulation**: Modify request/response headers dynamically.

---

## **🔥 Key Takeaways**
✅ `preHandle()` → Runs **before** the request reaches the controller.  
✅ `postHandle()` → Runs **after** the controller processes the request.  
✅ `afterCompletion()` → Runs **after** the request is fully completed.  
✅ You can **include/exclude** specific endpoints using `addPathPatterns() & excludePathPatterns()`.
