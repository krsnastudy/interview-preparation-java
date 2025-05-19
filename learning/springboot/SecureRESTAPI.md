## ✅ 1. **HTTP Basic Authentication**
**🔹 Use Case:** Simple internal APIs or test environments.

### 🛡️ How it works:
- Client sends Base64-encoded username:password in the `Authorization` header.

```http
Authorization: Basic dXNlcjpwYXNz
```

### 💡 Spring Boot Example:
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
      .httpBasic(Customizer.withDefaults());

    return http.build();
}
```

### 📚 Resource:
- [Spring Security HTTP Basic Docs](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html)

---

## ✅ 2. **JWT (JSON Web Token) Authentication**
**🔹 Use Case:** Stateless APIs with mobile/web clients or microservices.

### 🛡️ How it works:
- Issue JWT on login.
- Client sends JWT in `Authorization: Bearer <token>` header.
- Server validates signature + claims.

### 💡 Key Components:
- `JwtService` – to generate/validate token
- `JwtAuthFilter` – to intercept and validate each request

[✅ Click here for full Spring Boot JWT example](#scrollTo=cb9e493f-43cb-4097-9aa3-b6a7b44292f1)

---

## ✅ 3. **OAuth2 / OpenID Connect (OIDC)**
**🔹 Use Case:** Integrate with identity providers like Google, Okta, Auth0, Azure AD.

### 🛡️ How it works:
- Uses **authorization server** to handle login.
- Access Token + (optional) Refresh Token issued.
- Spring validates using **JWT or opaque token introspection**.

### 💡 Spring Boot Configuration (resource server):
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://accounts.google.com
```

```java
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }
}
```

### 📚 Resource:
- [Spring OAuth2 Resource Server Guide](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)

---

## ✅ 4. **API Key Authentication**
**🔹 Use Case:** Public APIs or third-party integrations.

### 🛡️ How it works:
- Client sends a predefined API key in headers.
- Server validates against known list.

```http
GET /api/data
x-api-key: my-secret-key
```

### 💡 Filter Example:
```java
@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private static final String API_KEY = "my-secret-key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String apiKey = request.getHeader("x-api-key");
        if (!API_KEY.equals(apiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        chain.doFilter(request, response);
    }
}
```

---

## ✅ 5. **Role-based Access Control (RBAC)**
**🔹 Use Case:** Control access based on user roles (e.g., ADMIN, USER).

### 🛡️ Example:
```java
@GetMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> adminEndpoint() {
    return ResponseEntity.ok("Hello Admin");
}
```

### 💡 Add to Security Config:
```java
@EnableMethodSecurity  // enables @PreAuthorize, @Secured
```

---

## ✅ 6. **Mutual TLS (mTLS)**
**🔹 Use Case:** Internal services, high-security environments (e.g., banking, finance).

### 🛡️ How it works:
- Both server and client present certificates.
- Encrypted + authenticated communication.

### 💡 Setup:
- Configure keystore/truststore in `application.yml`
```yaml
server:
  ssl:
    client-auth: need
    key-store: classpath:server.jks
    key-store-password: secret
    trust-store: classpath:truststore.jks
    trust-store-password: trustpass
```

---

## ✅ 7. **CORS Protection**
**🔹 Use Case:** Secure browser-based access for cross-origin requests.

### 💡 Configuration:
```java
@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                .allowedOrigins("https://myfrontend.com")
                .allowedMethods("*");
        }
    };
}
```

---

## ✅ 8. **Throttling / Rate Limiting**
**🔹 Use Case:** Prevent abuse and DoS attacks.

### 🛡️ Tools:
- Bucket4j (Java lib)
- API Gateways (e.g., Spring Cloud Gateway, Kong, NGINX)

```java
@Bean
public Filter rateLimitFilter() {
   // implement in-memory or Redis-backed rate limit logic
}
```

---

## ✅ 9. **Spring Cloud Gateway with Token Relay**
**🔹 Use Case:** Centralized authentication in microservices.

- API Gateway authenticates user.
- Forwards valid JWT to downstream services.

### 📚 Resource:
- [Spring Cloud Gateway Security](https://spring.io/projects/spring-cloud-gateway)

---

## 📊 Comparison Table

| Security Mechanism    | Use Case                        | Stateless | Setup Complexity | Scalable |
|------------------------|----------------------------------|-----------|------------------|----------|
| Basic Auth             | Simple internal APIs             | ✅        | 🟢 Easy           | 🟠 Medium |
| JWT                    | Mobile/web login                 | ✅        | 🟡 Medium         | ✅ High  |
| OAuth2 / OIDC          | SSO, Federated Identity          | ✅        | 🔴 High           | ✅ High  |
| API Key                | Third-party, public APIs         | ✅        | 🟢 Easy           | 🟠 Medium |
| Role-based Access      | Admin/User level permissions     | ✅        | 🟡 Medium         | ✅ High  |
| Mutual TLS             | Service-to-service high trust    | ✅        | 🔴 Complex         | 🟠 Medium |
| Rate Limiting          | DDoS protection                  | ✅        | 🟡 Medium         | ✅ High  |

---

## 📚 Resources

- 🔐 [Spring Security Official Docs](https://spring.io/projects/spring-security)
- 🧪 [JWT + Spring Boot Full Example](https://github.com/bezkoder/spring-boot-security-jwt)
- 🛡️ [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2.html)
- 📦 [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)

---

## 🧠 TL;DR – 9 Ways to Secure REST APIs in Spring Boot

| # | Security Method       | Best For                     |
|---|------------------------|------------------------------|
| 1 | Basic Auth             | Simple/internal APIs         |
| 2 | JWT                    | Stateless Auth (mobile/web)  |
| 3 | OAuth2 / OIDC          | SSO, federated login         |
| 4 | API Key                | Third-party/public APIs      |
| 5 | RBAC (@PreAuthorize)   | Role-based endpoints         |
| 6 | Mutual TLS             | Internal service security    |
| 7 | CORS                   | Secure cross-origin access   |
| 8 | Rate Limiting          | Protect from abuse           |
| 9 | Gateway Security       | Microservices boundary guard |
