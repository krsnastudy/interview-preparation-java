# 📘 JWT in Spring Boot Microservices

Short introduction explaining JWT (JSON Web Token) authentication in Spring Boot Microservices.

---

## 📖 What is JWT?

### Definition

JWT (JSON Web Token) is an open standard (RFC 7519) used for securely transmitting user information between client and server as a digitally signed JSON object.

### Why it exists

To provide a stateless authentication mechanism for distributed applications and microservices.

### Problem it solves

- Stateless authentication
- Secure identity verification
- Elimination of server-side session storage
- Secure communication between microservices

### Key Characteristics

- Stateless
- Self-contained
- Digitally signed
- Compact and URL-safe
- Supports expiration

**Key Takeaway**

JWT enables secure and stateless authentication across Spring Boot microservices.

---

## ❓ Why do we need it?

### Business Problem

Users should authenticate once and access multiple microservices without logging in repeatedly.

### Technical Problem

Maintaining HTTP sessions across distributed microservices becomes difficult and impacts scalability.

### Advantages

- Stateless authentication
- Better scalability
- Reduced server memory
- Easy integration with APIs
- Suitable for cloud-native applications

### Limitations of previous approaches

- Session replication
- Sticky sessions
- Increased server memory usage
- Difficult horizontal scaling

**Key Takeaway**

JWT eliminates server-side session management and enables scalable authentication.

---

## ⚙️ Internal Working

### Step 1

Client sends username and password.

↓

### Step 2

Authentication Server validates credentials.

↓

### Step 3

JWT Token is generated.

↓

### Step 4

Client stores JWT.

↓

### Step 5

Client sends JWT in Authorization header.

↓

### Step 6

API Gateway validates JWT.

↓

### Step 7

Request reaches appropriate microservice.

### Example

```
+----------------------+
| Client               |
+----------+-----------+
           |
           v
+----------------------+
| Authentication Server|
+----------+-----------+
           |
           v
+----------------------+
| JWT Generated        |
+----------+-----------+
           |
           v
+----------------------+
| API Gateway          |
+----------+-----------+
           |
           v
+----------------------+
| Microservice         |
+----------------------+
```

**Key Takeaway**

JWT is generated once and validated for every request without maintaining sessions.

---

## 🏗 Architecture / Internal Components

### Client

Sends login request and stores JWT.

### Authentication Service

Authenticates users and generates JWT.

### JWT Token

Contains user information and claims.

### API Gateway

Validates JWT before forwarding requests.

### User Service

Provides user details.

### Secret Key / Public Key

Used to sign and verify JWT.

### Resource Server

Processes authenticated requests.

---

## 🔄 Visual Flow

```
+------------+
| Client     |
+------+-----+
       |
       v
+----------------------+
| Authentication Server|
+------+---------------+
       |
       | JWT
       v
+----------------------+
| API Gateway          |
+------+---------------+
       |
       v
+----------------------+
| Order Service        |
+----------------------+
       |
       v
+----------------------+
| Payment Service      |
+----------------------+
```

---

## 💻 Code Examples

### Basic Example

```java
String token = jwtService.generateToken(username);
```

### Intermediate Example

```java
String authHeader = request.getHeader("Authorization");

if(authHeader != null && authHeader.startsWith("Bearer ")) {
    String jwt = authHeader.substring(7);
}
```

### Production Example

```java
if(jwtService.validateToken(token)) {
    SecurityContextHolder.getContext()
            .setAuthentication(authentication);
}
```

### Spring Boot Example

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated());

    return http.build();
}
```

---

## 🏦 Real Production Example

### Online Banking

- Customer Login
- JWT Generation
- Access Account APIs

### Payment Gateway

- Merchant Authentication
- Secure Payment APIs

### Insurance

- Agent Authentication
- Policy APIs

### E-Commerce

- Customer Login
- Cart Service
- Order Service
- Payment Service

### Loan Processing

- Customer Authentication
- Loan Approval APIs

### Microservices

- Single Sign-On
- API Gateway Authentication

---

## ⚠ Common Mistakes

Wrong Approach

- Storing sensitive information inside JWT
- Long token expiration
- Not validating signature
- Not checking expiration

↓

Problems

- Security vulnerabilities
- Token misuse
- Unauthorized access

↓

Correct Approach

- Store minimal claims
- Short expiration
- Always validate signature
- Use HTTPS

Production Impact

Improves security and reduces authentication risks.

---

## ✅ Best Practices

- Always use HTTPS.
- Keep token expiration short.
- Store minimal claims.
- Validate every request.
- Rotate signing keys.
- Use refresh tokens.
- Implement token revocation if required.
- Log authentication failures.
- Secure secret keys.
- Enable monitoring.

---

## ⚡ Performance Considerations

### CPU

Token verification on every request.

### Memory

No server-side session storage.

### Time Complexity

Fast signature validation.

### Scalability

Highly scalable.

### Network

Small token transferred in headers.

### Caching

Public keys can be cached.

### Thread Usage

No session synchronization required.

---

## ⚖ Trade-offs

### Advantages

- Stateless
- Highly scalable
- Secure
- Easy integration
- Cloud-friendly

### Disadvantages

- Token cannot be modified
- Revocation is difficult
- Token size increases with claims

### When to use

- REST APIs
- Spring Boot Microservices
- Cloud applications

### When NOT to use

- Traditional server-rendered applications using sessions
- Very short-lived internal applications

### Real-life analogy

Airport boarding pass that is verified before every boarding.

---

## 📊 Comparison Table

| Feature | JWT | Session Authentication |
|----------|-----|------------------------|
| Stateful | No | Yes |
| Scalability | High | Medium |
| Server Memory | Low | High |
| Session Storage | Not Required | Required |
| Performance | Better | Lower |
| Suitable for Microservices | Yes | No |

---

## 🎯 Interview Questions

### Question 1

What is JWT?

Expected Answer

A stateless authentication token used for securely transmitting user identity.

Follow-up Question

What are JWT claims?

---

### Question 2

Why is JWT preferred in microservices?

Expected Answer

Because it eliminates server-side session management.

Follow-up Question

What is Stateless Authentication?

---

### Question 3

What are the three parts of JWT?

Expected Answer

- Header
- Payload
- Signature

Follow-up Question

What information should never be stored inside Payload?

---

### Question 4

How is JWT validated?

Expected Answer

By verifying the digital signature and expiration.

Follow-up Question

Which algorithm is commonly used?

---

### Question 5

What is Refresh Token?

Expected Answer

A token used to generate a new access token after expiration.

Follow-up Question

Why shouldn't refresh tokens be sent with every request?

---

## 🧠 Scenario-Based Questions

- How would you implement JWT authentication in Spring Boot?
- How do you invalidate JWT before expiration?
- How do microservices validate JWT?
- How would you secure JWT secret keys?

---

## 🛠 Debugging Tips

### Common production issues

- Invalid signature
- Expired token
- Missing Authorization header
- Incorrect secret key

### Logs

- Authentication logs
- Security logs

### Monitoring

- Failed login attempts
- Invalid token count

### Troubleshooting

- Verify expiration
- Verify signing key
- Decode JWT payload

### Debugging techniques

- Enable Spring Security logs
- Decode JWT using debugging tools
- Verify claims

---

## 📌 When to Use JWT

- Spring Boot Microservices
- REST APIs
- API Gateway
- OAuth2
- Cloud-native applications
- Distributed systems

---

## 🚫 When NOT to Use JWT

- Traditional session-based applications
- Applications requiring immediate session invalidation
- Simple monolithic applications

---

## 💡 Interview Tips

### What interviewer expects

- JWT structure
- Authentication flow
- Stateless authentication
- Spring Security integration

### Common mistakes

- Confusing JWT with OAuth2
- Ignoring refresh tokens
- Storing sensitive data in payload

### How to answer confidently

Explain login flow from Authentication Server to API Gateway and Microservices.

### What follow-up questions may be asked

- JWT vs OAuth2
- JWT vs Session
- Refresh Token
- API Gateway authentication
- Spring Security filter chain

---

## 📝 Short Conclusion Summary

JWT provides stateless authentication by securely transmitting digitally signed user information between clients and Spring Boot microservices. It improves scalability, eliminates server-side sessions, and is widely used in cloud-native and distributed architectures.

---

## ⚡ TL;DR

| Topic | Summary |
|--------|---------|
| Purpose | Stateless Authentication |
| Internal Working | Signed Token Validation |
| Best For | Spring Boot Microservices |
| Avoid | Session-based Applications |
| Advantages | Scalable, Secure |
| Disadvantages | Difficult Revocation |
| Performance | High |
| Interview Keyword | Stateless Authentication |

---

## 🚀 30-Second Interview Answer

JWT (JSON Web Token) is a stateless authentication mechanism used in Spring Boot microservices. After successful authentication, the server generates a signed token containing user claims. The client sends this token in the Authorization header for every request. The API Gateway or Resource Server validates the token signature and expiration before allowing access. Since JWT does not require server-side session storage, it provides better scalability and is widely used in cloud-native microservice architectures.