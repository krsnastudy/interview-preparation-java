# **Spring Boot Security - Comprehensive Guide for Interviews**

## **1. Introduction to Spring Security**
Spring Security is a powerful framework for **authentication** and **authorization** in Java applications. It provides comprehensive security services for Spring-based applications.

### **Key Features:**
- Authentication (who are you?)
- Authorization (what are you allowed to do?)
- Protection against common attacks (CSRF, XSS, Session Fixation)
- Integration with various authentication providers (LDAP, OAuth2, JWT, Database)

---

## **2. Core Components of Spring Security**

| **Component**          | **Purpose** |
|------------------------|------------|
| **SecurityContext** | Holds authentication and security details |
| **Authentication** | Represents a user's credentials |
| **UserDetails** | Interface for user information |
| **UserDetailsService** | Loads user-specific data |
| **PasswordEncoder** | Encodes & verifies passwords |
| **SecurityFilterChain** | Defines security rules |
| **GrantedAuthority** | Represents permissions/roles |

---

## **3. Authentication vs Authorization**

| **Authentication** | **Authorization** |
|--------------------|------------------|
| Verifies user identity | Checks user permissions |
| Example: Login with username/password | Example: Only ADMIN can access `/admin` |
| Uses `AuthenticationManager` | Uses `AccessDecisionManager` |

---

## **4. Spring Security Implementation in Spring Boot**

### **A. Basic Setup**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder().encode("pass"))
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### **B. Key Annotations**
| **Annotation** | **Purpose** |
|---------------|------------|
| `@EnableWebSecurity` | Enables Spring Security |
| `@PreAuthorize` | Method-level security (e.g., `@PreAuthorize("hasRole('ADMIN')")` |
| `@Secured` | Similar to `@PreAuthorize` but simpler |
| `@RolesAllowed` | JSR-250 annotation for role-based access |

---

## **5. Authentication Methods**

### **A. Form-Based Authentication**
```java
http.formLogin(form -> form
    .loginPage("/login")
    .defaultSuccessUrl("/home")
    .failureUrl("/login?error=true")
);
```

### **B. HTTP Basic Authentication**
```java
http.httpBasic(Customizer.withDefaults());
```

### **C. JWT (JSON Web Token) Authentication**
```java
http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
```

### **D. OAuth2 (Social Login)**
```java
http.oauth2Login(oauth -> oauth
    .loginPage("/login")
    .defaultSuccessUrl("/dashboard")
);
```

---

## **6. Password Encoding**
Always encode passwords before storing them in the database.

### **Using BCryptPasswordEncoder**
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### **How to Use:**
```java
String encodedPassword = passwordEncoder.encode("rawPassword");
boolean matches = passwordEncoder.matches("rawPassword", encodedPassword);
```

---

## **7. CSRF Protection**
Spring Security enables CSRF protection by default.

### **Disabling CSRF (Not Recommended)**
```java
http.csrf(csrf -> csrf.disable());
```

### **When to Disable?**
- For stateless APIs (JWT/OAuth2)
- If using a different CSRF protection mechanism

---

## **8. Method-Level Security**
```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long userId) {
    // Only ADMIN can execute this
}
```

### **SpEL (Spring Expression Language) Examples**
| **Expression** | **Meaning** |
|---------------|------------|
| `hasRole('ADMIN')` | Checks for a role |
| `hasAuthority('READ_PRIVILEGE')` | Checks for a specific permission |
| `isAuthenticated()` | Checks if user is logged in |

---

## **9. Common Interview Questions**

### **Q1: What is Spring Security?**
**Answer:**  
A framework that provides authentication, authorization, and protection against common attacks in Spring applications.

### **Q2: How does JWT work with Spring Security?**
**Answer:**
- User logs in → Server generates a JWT token
- Client sends token in `Authorization: Bearer <token>`
- Spring validates the token and sets up `SecurityContext`

### **Q3: What is the difference between `hasRole()` and `hasAuthority()`?**
**Answer:**
- `hasRole("ADMIN")` → Automatically adds `ROLE_` prefix
- `hasAuthority("ROLE_ADMIN")` → Requires full authority name

### **Q4: How do you disable security for a specific endpoint?**
**Answer:**
```java
.requestMatchers("/public/**").permitAll()
```

### **Q5: What is CSRF, and how does Spring Security prevent it?**
**Answer:**
- **CSRF** = Cross-Site Request Forgery
- Spring Security adds a **CSRF token** to forms and validates it on submission

---

## **10. Best Practices**
✅ **Always encode passwords** (BCrypt, SCrypt, Argon2)  
✅ **Use HTTPS** for secure communication  
✅ **Limit login attempts** to prevent brute force attacks  
✅ **Implement proper session management**  
✅ **Use `@PreAuthorize` for method-level security**

---

## **11. Learning Resources**
1. **Official Docs**: [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
2. **Books**:
    - *Spring Security in Action* by Laurentiu Spilca
    - *Spring Boot: Up and Running* by Mark Heckler
3. **Courses**:
    - [Spring Security on Udemy](https://www.udemy.com/topic/spring-security/)
    - [Spring Boot Security on Pluralsight](https://www.pluralsight.com/courses/spring-boot-security-fundamentals)
4. **GitHub Repos**:
    - [Spring Security Samples](https://github.com/spring-projects/spring-security-samples)

---

## **12. Conclusion (Interview Cheat Sheet)**
📌 **Spring Security = Authentication + Authorization**  
📌 **Key components: `UserDetailsService`, `PasswordEncoder`, `SecurityFilterChain`**  
📌 **Common auth methods: Form, JWT, OAuth2**  
📌 **Always encode passwords & enable CSRF**  
📌 **Use `@PreAuthorize` for fine-grained control**

### **Pro Tip:**
If asked about **real-world usage**, mention:
- **Banking apps** (2FA + OAuth2)
- **E-commerce** (JWT for stateless APIs)
- **Healthcare** (HIPAA-compliant security)
