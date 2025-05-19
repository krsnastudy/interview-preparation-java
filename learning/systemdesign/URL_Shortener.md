Designing a **URL Shortener** (like Bitly or TinyURL) is a **common system design interview question**, and it's great for showcasing knowledge of hashing, databases, scalability, and fault tolerance.

---

## 🧭 **Scope**

Build a service that:

* Converts long URLs to short ones (e.g., `tinyurl.com/xYz123`)
* Redirects users from short URLs to the original URLs
* Optionally: track analytics, expiry, or custom aliases

---

## ✅ Functional Requirements

1. **Create Short URL** for a given long URL
2. **Redirect** from short URL to original URL
3. Support **custom aliases** (optional)
4. Set **expiry time** (optional)
5. Track **click analytics** (optional)

---

## 🚦 Non-Functional Requirements

* High availability
* Low latency redirection
* Scalable to billions of URLs
* Prevent collisions and duplicates
* Rate limiting for abuse prevention

---

## 🏛️ High-Level Architecture

```
                 +------------------------+
                 |      Load Balancer     |
                 +----------+-------------+
                            |
                   +--------v--------+
                   |   API Gateway   |
                   +--------+--------+
                            |
           +----------------v----------------+
           |       URL Shortener Service     |
           +----------------+----------------+
                            |
        +-------------------v-------------------+
        |               Storage Layer           |
        | (NoSQL or SQL DB with Cache & Indexes)|
        +-------------------+-------------------+
                            |
           +----------------v----------------+
           |       Hashing/Encoding Service  |
           +---------------------------------+
```

---

## 🔗 Basic Flow

### 1. **URL Shortening**

* User submits long URL `https://example.com/article?id=12345`
* System:

    * Generates a unique **short code** (e.g., `xYz123`)
    * Stores mapping: `xYz123 → https://example.com/...`
    * Returns: `https://short.ly/xYz123`

### 2. **Redirection**

* User visits `https://short.ly/xYz123`
* System:

    * Looks up `xYz123` in DB/cache
    * Redirects to long URL using HTTP 302/301

---

## 📦 Data Model

### Table: `url_mapping`

```sql
short_code   | long_url                        | created_at | expiry_at | click_count
-------------|----------------------------------|------------|-----------|-------------
xYz123       | https://example.com/article...  | timestamp  | timestamp | 256
```

---

## 🔑 Key Design Details

### 🔹 **Short Code Generation**

Use a 6–8 character code, base62 encoding (A–Z, a–z, 0–9) gives:

```
62^6 ≈ 56.8 billion combinations
```

#### Approaches:

1. **Auto-increment ID + Base62**

    * e.g., ID = 100045 → encode to `xYz123`
    * Pros: Simple, unique; Cons: Predictable
2. **Hashing (MD5, SHA-256 + truncate)**

    * Pros: Random-looking; Cons: Risk of collision (need to handle)
3. **Custom Alias (optional)**

---

### 🔹 **Database Choice**

* Use **NoSQL (e.g., DynamoDB, MongoDB)** or **SQL (e.g., MySQL)** depending on scale.
* **Indexes** on `short_code` for fast lookup.
* **Redis Cache** for hot URLs.

---

### 🔹 **Caching Layer**

* Frequently accessed short codes are cached in **Redis/Memcached**.
* Reduces latency & DB load.

---

### 🔹 **Scalability Strategy**

* **Horizontal scaling** of API servers
* **Rate limiting** (e.g., via API Gateway)
* Use **CDN** for static pages & fallback redirects
* Partition DB by hash ranges

---

### 🔹 **Analytics Tracking** (Optional)

* Click counts per short URL
* Geo/IP info, timestamps
* Stored separately to avoid write overhead on URL table

---

## 🔐 Security & Abuse Prevention

* **Rate limiting** & throttling
* Validate URL format and content
* Use CAPTCHA to prevent bot abuse
* Monitor suspicious patterns (e.g., phishing)

---

## ✅ TL;DR Summary

| Component        | Responsibility                       |
| ---------------- | ------------------------------------ |
| URL Shortener    | Accepts long URL, returns short code |
| Redirect Service | Resolves short code to long URL      |
| Hashing Service  | Encodes/decodes short codes (Base62) |
| Storage (DB)     | Stores URL mappings & metadata       |
| Cache (Redis)    | Fast access to hot URLs              |
| Analytics Engine | (Optional) logs clicks and location  |

---

## Code

Here's a **simple URL Shortener project in Java (Spring Boot)** that supports:

* Shortening a long URL
* Redirecting from the short URL to the original
* In-memory storage (for simplicity)

---

## ✅ Tech Stack

* Java 17+
* Spring Boot (Web)
* Lombok (optional)
* No database (uses `ConcurrentHashMap`)

---

## 📁 Project Structure

```
url-shortener/
├── controller/
│   └── UrlController.java
├── service/
│   └── UrlService.java
├── model/
│   └── UrlRequest.java
├── UrlShortenerApplication.java
```

---

## 📦 Code

### 🧠 `UrlService.java`

```java
package service;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlService {

    private final Map<String, String> map = new ConcurrentHashMap<>();

    public String shortenUrl(String longUrl) {
        String shortCode = generateShortCode(longUrl);
        map.put(shortCode, longUrl);
        return shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        return map.get(shortCode);
    }

    private String generateShortCode(String url) {
        return Base64.getUrlEncoder().encodeToString(url.getBytes()).substring(0, 6);
    }
}
```

---

### 📦 `UrlController.java`

```java
package controller;

import model.UrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UrlService;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody UrlRequest request) {
        String shortCode = urlService.shortenUrl(request.getLongUrl());
        return ResponseEntity.ok("http://localhost:8080/" + shortCode);
    }

    @GetMapping("/{shortCode}")
    public void redirectToOriginal(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String longUrl = urlService.getOriginalUrl(shortCode);
        if (longUrl != null) {
            response.sendRedirect(longUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
        }
    }
}
```

---

### 📦 `UrlRequest.java`

```java
package model;

public class UrlRequest {
    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
```

---

### 🏁 `UrlShortenerApplication.java`

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlShortenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }
}
```

---

## ▶️ How to Run

1. Create a Spring Boot project with the above files.
2. Run `UrlShortenerApplication.java`.
3. Test with Postman or cURL:

**POST** to `localhost:8080/shorten`

```json
{
  "longUrl": "https://example.com/my-long-article"
}
```

It returns:

```
http://localhost:8080/aHR0cH
```

**GET** `http://localhost:8080/aHR0cH` redirects to your original URL.

---

## ✅ TL;DR

This is a minimal Spring Boot URL shortener using:

* Base64 for code generation
* In-memory map for storage
* `/shorten` endpoint to create URLs
* `/{shortCode}` for redirection

---
