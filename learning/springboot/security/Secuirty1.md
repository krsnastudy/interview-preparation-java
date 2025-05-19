Let’s break down **Authentication using JWT** and **Authorization in Microservices** with:

1. ✅ **Short Interview Answer**
2. 🔍 Real-World Architecture
3. 🛠️ Spring Boot + JWT Example
4. 📘 Bonus: Handling Authorization between services

---

## ✅ **Short Interview Answer (For Interview)**

> In microservices, **JWT (JSON Web Token)** is used for **authentication**. A user logs in, the Auth Service issues a signed token, and each microservice validates this token to **authorize** access without needing to talk to the Auth Service again.

---

## 🧱 Architecture Overview

```text
[Client]
   ↓
[Auth Service] — validates credentials & issues JWT
   ↓
[Microservice A, B, C] — validates JWT from request header (Authorization: Bearer <token>)
```

---

## 🛡️ **JWT Authentication Flow (Step-by-Step)**

1. **Login (Authentication)**

    * User sends credentials (`POST /login`) to **Auth Service**.
    * If valid, Auth Service creates a **JWT token** with user info (username, roles).
    * Token is returned to the client.

2. **Subsequent Requests**

    * Client includes token in header:
      `Authorization: Bearer eyJhbGciOiJIUzI1...`
    * Each microservice **parses and validates** the token using a **shared secret/public key**.
    * Based on claims in JWT (`roles`, `username`), the microservice **authorizes** access.

---

## 🔐 JWT Example with Spring Boot (Simplified)

### 1️⃣ Maven Dependencies

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

### 2️⃣ Generate JWT Token

```java
public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();
}
```

---

### 3️⃣ Validate Token

```java
public Claims validateToken(String token) {
    return Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();
}
```

---

### 4️⃣ Filter to Intercept Requests

```java
public class JwtRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(...) {
        String token = request.getHeader("Authorization").substring(7);
        if (validateToken(token)) {
            UsernamePasswordAuthenticationToken auth =
               new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
```

---

### 5️⃣ Secure Endpoints

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
}
```

---

## 🧾 Token Payload Example

```json
{
  "sub": "krishna",
  "roles": ["ADMIN"],
  "iat": 1714371200,
  "exp": 1714457600
}
```

---

## 🔁 Inter-Service Authorization with JWT

When Service A calls Service B:

* Service A passes the same JWT token in the header.
* Service B verifies the token using shared secret/public key.
* This allows **stateless, secure communication** between services.

---

## ✅ TL;DR for Interviews:

> "In microservices, JWT enables stateless authentication. Auth Service issues a signed token upon login. Microservices validate this token to authenticate and authorize users without needing session storage or a centralized identity server."

---
Here is a complete example showing JWT-based Authentication and Authorization in Microservices using Spring Boot.

---

### 🔐 **JWT Authentication & Authorization Summary (for interviews)**:

* **Authentication**: Validates user credentials and returns JWT.
* **Authorization**: Secures APIs using JWT from Authorization header.
* **Components**:
    * `JwtUtil` – Create & validate JWT.
    * `AuthController` – Login and token issuance.
    * `JwtFilter` – Intercepts requests to validate token.
    * `SecurityConfig` – Sets up Spring Security.
    * `ProductController` – Protected endpoint example.

Would you like an architecture diagram or Postman test collection for this setup?
