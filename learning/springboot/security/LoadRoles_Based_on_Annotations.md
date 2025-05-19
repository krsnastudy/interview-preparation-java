To **load roles based on annotations** in a **Spring Boot application**, the best practice is to use **Spring Security with annotation-based access control**. Here’s how you can achieve that step-by-step:

---

## ✅ Goal:
You want roles (e.g., `ROLE_ADMIN`, `ROLE_USER`) to be used in annotations like:

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

## 🔧 Step-by-Step Implementation

### 1. **Add Spring Security dependency (Maven)**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

### 2. **Enable method security with annotations**

In your `@SpringBootApplication` class or any `@Configuration` class:

```java
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
}
```

---

### 3. **Use `@PreAuthorize` or `@Secured` for Role-Based Access**

```java
@RestController
public class AdminController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminData() {
        return "Admin Access";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String userData() {
        return "User Access";
    }
}
```

> ⚠️ Note: Spring Security **automatically prefixes roles with `ROLE_`**, so `"hasRole('ADMIN')"` checks for `"ROLE_ADMIN"` internally.

---

### 4. **Configure UserDetailsService or In-Memory Roles**

#### a. **In-Memory Example:**

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("krishna")
        .password("password")
        .roles("USER")
        .build();

    UserDetails admin = User.withDefaultPasswordEncoder()
        .username("admin")
        .password("admin")
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(user, admin);
}
```

---

### 5. **Alternative (Custom UserDetails from DB)**

If loading from DB, make sure your `UserDetails` implementation returns roles like:

```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
        .collect(Collectors.toList());
}
```

---

## 🛡 Common Role-Based Annotations

| Annotation | Purpose |
|-----------|---------|
| `@PreAuthorize("hasRole('ADMIN')")` | Checks before method runs |
| `@Secured("ROLE_ADMIN")` | Similar, but older style |
| `@RolesAllowed("ADMIN")` | JSR-250 style |

---

## 📌 TL;DR

- Use `@EnableMethodSecurity(prePostEnabled = true)`
- Annotate methods with `@PreAuthorize("hasRole('X')")`
- Ensure user roles are returned as `ROLE_X` from `UserDetails`
- Works for both in-memory and DB-backed users

---
