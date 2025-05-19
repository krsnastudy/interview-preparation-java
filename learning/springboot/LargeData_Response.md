## ✅ Common Approaches to Send Large Data in API Response

### 1. **Pagination (Best Practice ✅)**

Divide large data into pages:
```http
GET /products?page=1&size=100
```

- Reduces payload size
- Fast response time
- Easily cacheable and parallelizable

> **Spring Boot Example:**
```java
@GetMapping("/products")
public Page<Product> getProducts(Pageable pageable) {
    return productRepository.findAll(pageable);
}
```

---

### 2. **Streaming the Response**

Use **streaming output** so the client starts receiving data as it is being written.

#### 🔹 Spring Boot Example:
```java
@GetMapping(value = "/stream", produces = MediaType.APPLICATION_JSON_VALUE)
public void streamData(HttpServletResponse response) throws IOException {
    PrintWriter writer = response.getWriter();
    writer.write("[");
    for (int i = 0; i < 1000000; i++) {
        writer.write("{ \"value\": " + i + " }" + (i < 999999 ? "," : ""));
    }
    writer.write("]");
}
```

- No need to load entire data in memory
- Useful for **huge datasets**
- Can integrate with `@ResponseBodyEmitter` or `StreamingResponseBody`

---

### 3. **Compression (GZIP)**

Enable **GZIP compression** to reduce payload size.
```yaml
# application.yml
server:
  compression:
    enabled: true
    mime-types: application/json
    min-response-size: 1024
```

- Especially helpful for text-heavy payloads (like JSON/XML)

---

### 4. **Asynchronous File Download (Large Exports)**

If data is too big:
- Generate file (CSV, JSON, ZIP)
- Upload to blob store (e.g., S3)
- Send download link to client

```json
{
  "message": "Data export ready",
  "url": "https://bucket.s3.amazonaws.com/export123.csv"
}
```

---

### 5. **Use WebSockets (Real-time Bulk Data)**

For continuous stream of large data — use WebSocket instead of REST:
- Stock feeds
- Large logs or metrics stream

---

## ⚠️ Anti-Patterns to Avoid

- ❌ Sending 1M records in a single JSON — leads to OOM and timeouts
- ❌ Blocking the server while preparing huge payload
- ❌ Ignoring client-side memory limitations

---

## 🧠 TL;DR (Interview Style):

> Use **pagination** or **streaming** to efficiently send large data.  
> For huge or continuous data, prefer **streaming APIs** or **file-based export + download link**.  
> Always **compress** large responses and avoid full payloads in memory.

---
