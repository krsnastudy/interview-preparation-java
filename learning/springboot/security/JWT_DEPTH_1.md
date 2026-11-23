# JWT implementation in D-E-P-T-H format

## The spoken answer (about 80 seconds)

> **D (Define):** "JWT is a compact, signed token with three parts: header, payload (claims) and signature. It lets a server verify who the caller is without storing a session.
>
> **E (Explain why):** In microservices, server-side sessions don't scale well. With a JWT, the auth service signs a token once, and every service validates it locally using the signature, expiry, issuer and audience. No database or session lookup on each request.
>
> **P (Practical example):** In Spring Boot, a `/auth/login` endpoint authenticates the user and issues a short-lived access token (about 15 minutes) plus a refresh token. A `OncePerRequestFilter` reads the `Authorization: Bearer` header, validates the token and puts the authentication into the `SecurityContext`. The security config is stateless, with CSRF off for the API and `/auth/**` open.
>
> **T (Trade-offs):** A JWT can't be revoked before it expires, so I keep access tokens short, rotate refresh tokens, and keep a denylist for logout or compromise. The payload is only encoded, not encrypted, so no sensitive data goes in it. For multiple services I'd use RS256 so only the auth server holds the private key.
>
> **H (Hand-off):** I can walk through the filter code or refresh-token rotation if you'd like."

## The flow to remember

1. **Login**: the client sends credentials. The server authenticates them (BCrypt-hashed password) and returns an access token and a refresh token.
2. **Each request**: the client sends `Authorization: Bearer <token>`.
3. **Filter**: validates signature, `exp`, `iss` and `aud`, then loads the user or roles and sets the `SecurityContext`.
4. **Authorization**: `@PreAuthorize("hasRole('ADMIN')")` or URL rules decide access.
5. **Expiry**: the client calls `/auth/refresh` with the refresh token to get a new access token.

Real-life analogy: a **festival wristband**. Staff check the wristband itself (signature and expiry) at each gate without calling the ticket office. The cost is that a lost wristband works until it expires, unless gate staff keep a "blocked" list.

## Code skeleton (Spring Boot 3, Spring Security 6, jjwt 0.12)

```java
// 1. Token service
@Service
public class JwtService {
    private final SecretKey key;   // from env or vault, at least 256 bits
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String generate(UserDetails u) {
        return Jwts.builder()
            .subject(u.getUsername())
            .claim("roles", u.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .issuer("auth-service")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
            .signWith(key).compact();
    }
    public Claims parse(String token) {   // throws if signature or expiry is bad
        return Jwts.parser().verifyWith(key).requireIssuer("auth-service")
                   .build().parseSignedClaims(token).getPayload();
    }
}

// 2. Filter
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserDetailsService uds;
    // constructor omitted
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            try {
                Claims c = jwt.parse(h.substring(7));
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails u = uds.loadUserByUsername(c.getSubject());
                    var auth = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException e) { /* invalid or expired: continue unauthenticated, entry point returns 401 */ }
        }
        chain.doFilter(req, res);
    }
}

// 3. Security config
@Configuration @EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain chain(HttpSecurity http, JwtAuthFilter jwtFilter) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())                       // stateless API using headers, not cookies
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    @Bean PasswordEncoder encoder() { return new BCryptPasswordEncoder(); }
}
```

For production resource servers, mention the standard alternative: `spring-boot-starter-oauth2-resource-server` with `spring.security.oauth2.resourceserver.jwt.issuer-uri` or `jwk-set-uri`. Spring then validates the token and maps claims for you, with no custom filter.

## Highlights to land (the 12-year signals)

| Point | Why it impresses |
|---|---|
| **Signed, not encrypted** | Anyone can decode the payload, so never store passwords or personal data |
| **HS256 vs RS256** | Symmetric (one shared secret, simple) vs asymmetric (private key signs, public key verifies; best for many services) |
| **Short access token plus refresh token** | The standard answer to "how do you handle expiry and revocation" |
| **Validate everything** | Signature, `exp`, `iss`, `aud`, and pin the algorithm to block the `alg: none` attack |
| **Stateless config** | STATELESS session policy, CSRF disabled only because tokens go in a header |
| **Where to store on the client** | HttpOnly Secure SameSite cookie (resists XSS, needs CSRF protection) vs localStorage (easy, but any XSS steals it) |

## Trade-offs and traps

| Question | Strong answer |
|---|---|
| How do you log a user out or revoke a token? | Short expiry, refresh-token rotation, and a Redis denylist keyed by token id (`jti`) with TTL equal to remaining life |
| Where do you keep the secret? | Environment variable or a vault, never in the repo; rotate keys, and use `kid` in the header to support rotation |
| JWT vs session? | JWT scales across services without shared state but is hard to revoke. Sessions are easy to revoke but need shared storage |
| Why not put roles in the token? | You can (it saves a database call), but they go stale until expiry. Keep tokens short or check critical permissions server-side |
| What if the token is stolen? | Short lifetime limits damage; use HTTPS only, bind refresh tokens to a device, rotate them, and detect reuse |
| 401 vs 403? | 401: not authenticated (bad or missing token). 403: authenticated but not allowed |

## Likely follow-ups

1. **"Explain the refresh-token flow."** The access token expires in about 15 minutes. The refresh token (stored hashed in the database, valid for days) gets a new pair, and the old refresh token is invalidated. If an old one is reused, revoke the whole family.
2. **"How do microservices validate it?"** Each service verifies the signature with the public key (JWKS endpoint) and checks claims locally. The gateway can also validate once and pass identity downstream.
3. **"OAuth2 vs JWT?"** OAuth2 is an authorization framework (the protocol). JWT is a token format that OAuth2 and OpenID Connect often use.

