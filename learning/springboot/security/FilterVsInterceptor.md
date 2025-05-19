Here’s the **difference between Interceptor and Filter in Spring Boot**, important for interviews:

| Aspect              | Filter (javax.servlet.Filter)                           | Interceptor (HandlerInterceptor)                     |
| ------------------- | ------------------------------------------------------- | ---------------------------------------------------- |
| **Belongs to**      | Servlet Specification                                   | Spring Framework                                     |
| **Execution Level** | Works at the **Servlet container** level                | Works at the **Spring MVC** level                    |
| **Use Case**        | Logging, authentication, request wrapping, etc.         | Pre/post-processing of controller execution          |
| **Access**          | Accesses only **HTTPServletRequest/Response**           | Accesses **handler method, model, view, etc.**       |
| **Execution Order** | Runs **before** Spring MVC is invoked                   | Runs **after** filter, but before controller method  |
| **Configuration**   | Defined in `@WebFilter` or via `FilterRegistrationBean` | Implemented via `WebMvcConfigurer#addInterceptors()` |
| **Return Value**    | Doesn’t return view or model                            | Can interrupt request processing & return response   |

---

### Short Answer for Interviews:

* **Filter** is from **Servlet API**, executes before Spring context, used for low-level concerns.
* **Interceptor** is Spring-specific, used for **pre/post-controller logic**, like authorization or logging.

Here’s a **working example** of both a **Filter** and an **Interceptor** in a Spring Boot application.

---

### ✅ 1. **Servlet Filter Example**

**Use case:** Log every request URI before it reaches Spring.

```java
@Component
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("Filter :: Request URI: " + req.getRequestURI());
        chain.doFilter(request, response);
    }
}
```

No need to manually register if annotated with `@Component`.

---

### ✅ 2. **Spring MVC Interceptor Example**

**Use case:** Log before and after a controller method is executed.

```java
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("Interceptor :: PreHandle: " + request.getRequestURI());
        return true; // If false, request won’t proceed to controller
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("Interceptor :: AfterCompletion: " + request.getRequestURI());
    }
}
```

Now register it:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }
}
```

---

### 💡 Summary:

* Use **Filter** for general request logging, encoding, CORS handling.
* Use **Interceptor** for business-related logic before or after controllers.

---

![](D:\Study\Prac\GIT_Repo\CoreJava\src\com\study\resources\FilterVsInterceptor.png)
