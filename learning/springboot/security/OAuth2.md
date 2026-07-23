# 📘 Spring Security OAuth2

---
# 📖 What is Spring Security OAuth2?

Spring Security OAuth2 is a framework that enables secure authentication and authorization for applications using the OAuth 2.0 protocol.

It helps applications:

- Authenticate users
- Authorize access to protected resources
- Secure REST APIs
- Support Single Sign-On (SSO)
- Integrate with Identity Providers like Keycloak, Okta, Azure AD, Google, etc.

> **Key Takeaway:** OAuth2 is an authorization framework, while Spring Security provides its implementation in Spring Boot.

---

# ❓ Why do we need OAuth2?

Traditional authentication using username/password for every application has several drawbacks:

- Password sharing across applications
- Poor scalability
- Security risks
- No centralized identity management

OAuth2 solves this by allowing users to grant limited access to applications without sharing passwords.

**Example:**

```
Login with Google

↓

Google authenticates user

↓

Returns Access Token

↓

Application accesses user profile
```

> **Key Takeaway:** OAuth2 delegates authentication to a trusted Identity Provider.

---

# ⚙️ OAuth2 Roles

| Role | Responsibility |
|------|----------------|
| Resource Owner | User |
| Client | Spring Boot Application |
| Authorization Server | Issues Access Token |
| Resource Server | Protected APIs |
| Identity Provider | Google, Keycloak, Okta |

---

# 🔄 OAuth2 Authorization Code Flow

```
+--------+        +-------------------+
| Client |------->| Authorization     |
|        | Login  | Server            |
+--------+        +-------------------+
     |                     |
     | Authorization Code  |
     |<--------------------|
     |                     |
     | Access Token        |
     |-------------------->|
     |<--------------------|
     |                     |
     | Call Protected API  |
     |-------------------->|
     |<--------------------|
```

### Steps

1. User requests login.
2. Redirect to Authorization Server.
3. User authenticates.
4. Authorization Code returned.
5. Client exchanges code for Access Token.
6. Client accesses Resource Server.

> **Key Takeaway:** Password is never shared with the client application.

---

# 🏗 Spring Security Internal Working

```
Client Request

↓

SecurityFilterChain

↓

OAuth2AuthenticationFilter

↓

AuthenticationManager

↓

OAuth2AuthenticationProvider

↓

JWT Validation

↓

SecurityContextHolder

↓

Controller
```

### Important Components

- **SecurityFilterChain** – Entry point for security filters.
- **AuthenticationManager** – Coordinates authentication.
- **AuthenticationProvider** – Validates credentials/token.
- **SecurityContextHolder** – Stores authenticated user details.
- **JwtDecoder** – Validates JWT tokens.

> **Key Takeaway:** Every request passes through the Security Filter Chain before reaching your controller.

---

# 🔑 JWT Token Structure

```
Header

.

Payload

.

Signature
```

Example:

```
xxxxx.yyyyy.zzzzz
```

### Header

- Algorithm
- Token Type

### Payload

- Subject (sub)
- Username
- Roles
- Expiration
- Issuer

### Signature

Verifies token integrity.

> **Key Takeaway:** Never store sensitive information like passwords inside JWT.

---

# 💻 Spring Boot Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()
                    .anyRequest().authenticated())
            .oauth2ResourceServer(oauth ->
                    oauth.jwt());

        return http.build();
    }
}
```

---

# 🏦 Real Production Example

**Online Banking**

Customer

↓

Login via Azure AD

↓

Authorization Server issues JWT

↓

Customer calls

```
GET /accounts
```

↓

Spring Security validates JWT

↓

Controller executes

↓

Returns Account Details

---

# ⚠ Common Mistakes

❌ Storing passwords inside JWT.

❌ Long token expiry.

❌ Disabling CSRF without understanding the impact.

❌ Trusting unsigned JWT tokens.

❌ Exposing refresh tokens to browsers.

---

# ✅ Best Practices

- Use HTTPS.
- Use short-lived Access Tokens.
- Use Refresh Tokens securely.
- Validate JWT signature.
- Store secrets securely.
- Implement Role-Based Access Control (RBAC).
- Enable audit logging.
- Rotate signing keys periodically.

---

# ⚡ Performance Considerations

- JWT validation is stateless.
- Avoid database lookup for every request.
- Cache public keys (JWKS).
- Use asymmetric encryption (RSA/ECDSA) for distributed systems.
- Keep JWT payload small.

---

# ⚖ Trade-offs

| Advantages | Disadvantages |
|------------|---------------|
| Stateless authentication | Token revocation is harder |
| Scalable | JWT size increases with claims |
| SSO support | Refresh token management |
| Easy microservice integration | Requires secure key management |

---

# 📊 Authentication vs Authorization

| Authentication | Authorization |
|---------------|---------------|
| Who are you? | What can you access? |
| Login | Permission |
| Username/Password | Roles & Authorities |
| Generates Authentication | Checks Authorization |

---

# 🎯 Interview Questions

### 1. What is OAuth2?

**Answer:**

OAuth2 is an authorization framework that allows third-party applications to access protected resources without exposing user credentials.

---

### 2. Difference between OAuth2 and JWT?

**Answer:**

OAuth2 defines the authorization protocol.

JWT is a token format commonly used within OAuth2.

---

### 3. Difference between Authentication and Authorization?

Authentication verifies identity.

Authorization determines access permissions.

---

### 4. Why is JWT stateless?

Because all required user information is stored inside the token, eliminating server-side session storage.

---

### 5. What is SecurityFilterChain?

It is the entry point in Spring Security that intercepts every HTTP request and applies configured security filters.

---

### 6. What is AuthenticationManager?

Coordinates authentication by delegating to one or more AuthenticationProvider implementations.

---

### 7. What is Refresh Token?

Used to obtain a new Access Token without requiring the user to log in again.

---

# 📌 When to Use OAuth2

✅ REST APIs

✅ Microservices

✅ Mobile Applications

✅ SSO

✅ Enterprise Applications

✅ Cloud Applications

---

# 🚫 When NOT to Use OAuth2

- Small standalone applications
- Internal tools without external authentication requirements
- Simple applications where basic authentication is sufficient

---

# 💡 Interview Tips

Interviewers usually ask:

- Difference between OAuth2 and JWT.
- Explain Authorization Code Flow.
- SecurityFilterChain internals.
- AuthenticationManager vs AuthenticationProvider.
- Stateless authentication.
- Refresh Token vs Access Token.
- How Spring Security validates JWT.
- How to secure Microservices.

Prepare to explain the request flow from **Client → SecurityFilterChain → AuthenticationManager → JWT Validation → Controller**.

---

# 📝 Short Conclusion Summary

Spring Security OAuth2 provides a secure and scalable way to authenticate users and authorize access to protected resources. It integrates seamlessly with Identity Providers, uses JWT for stateless authentication, and secures REST APIs through the Spring Security Filter Chain. It is the preferred choice for modern enterprise applications and microservices.

---

# ⚡ TL;DR

| Topic | Summary |
|--------|---------|
| Purpose | Secure Authentication & Authorization |
| Protocol | OAuth2 |
| Token Format | JWT |
| Entry Point | SecurityFilterChain |
| Authentication | AuthenticationManager |
| Token Validation | JwtDecoder |
| Best For | REST APIs, SSO, Microservices |
| Avoid | Storing sensitive data in JWT |
| Interview Keyword | Stateless Security |

---

# 🚀 30-Second Interview Answer

> "Spring Security OAuth2 is Spring's implementation of the OAuth2 authorization framework, used to secure applications and REST APIs. It authenticates users through an Authorization Server, issues JWT access tokens, and validates those tokens using the Security Filter Chain before allowing access to protected resources. Since JWT is stateless, it eliminates server-side session management, making it highly scalable for enterprise microservices and cloud-native applications."