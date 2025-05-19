### **1. Enabling Spring Boot Actuator**
Spring Boot Actuator provides built-in endpoints for monitoring and managing applications.

#### **Step-by-Step Configuration:**
1. **Add Dependencies**  
   In your `pom.xml` (Maven) or `build.gradle` (Gradle):

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   ```

2. **Configure Actuator in `application.properties` or `application.yml`**
    - Enable all endpoints (optional):

      ```properties
      management.endpoints.web.exposure.include=*
      ```
    - Restrict specific endpoints:

      ```properties
      management.endpoints.web.exposure.include=health,metrics,info
      ```

3. **Access Actuator Endpoints**  
   Once enabled, common endpoints include:
    - `/actuator/health` → Check application health.
    - `/actuator/metrics` → View runtime metrics.
    - `/actuator/info` → Application metadata.

---

### **2. Enabling Swagger UI (SpringDoc OpenAPI)**
Swagger provides an interactive API documentation interface.

#### **Step-by-Step Configuration:**
1. **Add Dependencies**  
   For **Spring Boot 3+** (SpringDoc OpenAPI):

   ```xml
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
       <version>2.0.2</version>
   </dependency>
   ```

   For **Spring Boot 2.x**:

   ```xml
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-ui</artifactId>
       <version>1.6.9</version>
   </dependency>
   ```

2. **Access Swagger UI**  
   After running the application, access Swagger UI at:

   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Customize API Documentation in `application.properties`**

   ```properties
   springdoc.api-docs.path=/api-docs
   springdoc.swagger-ui.path=/swagger-ui
   ```

    - This lets you access docs via:
      ```
      http://localhost:8080/api-docs
      http://localhost:8080/swagger-ui
      ```

---

### **Interview Tips:**
1. **Explain Use Cases**
    - Actuator: Helps in **monitoring**, tracking **app health**, and exposing **metrics**.
    - Swagger UI: Simplifies **API documentation** and improves **developer experience**.

2. **Security Considerations**
    - **Disable sensitive endpoints in production:**
      ```properties
      management.endpoints.web.exposure.exclude=env,beans
      ```
    - **Secure Swagger UI:** Allow only authorized access.

3. **Real-World Scenario**
    - Actuator helps **DevOps teams monitor microservices** for health status.
    - Swagger UI reduces **API integration time** for frontend developers.

---

Spring Boot Actuator provides a set of built-in endpoints that help monitor and manage your application. Here are the default endpoints available:

### **Common Actuator Endpoints**
| Endpoint          | Description |
|------------------|-------------|
| `/actuator/health` | Shows the application health status. |
| `/actuator/info` | Displays custom application metadata. |
| `/actuator/metrics` | Provides various performance metrics. |
| `/actuator/loggers` | Shows and modifies logging levels. |
| `/actuator/env` | Displays environment properties. |
| `/actuator/mappings` | Shows all registered URL mappings. |
| `/actuator/beans` | Lists all beans registered in the application context. |
| `/actuator/threaddump` | Provides a thread dump (useful for debugging). |
| `/actuator/heapdump` | Generates a heap dump of the application. |
| `/actuator/configprops` | Shows all configured properties. |
| `/actuator/scheduledtasks` | Displays scheduled tasks running in the application. |
| `/actuator/httptrace` | Provides HTTP request traces (need explicit enablement). |
| `/actuator/auditevents` | Displays application audit events. |

### **Important Notes:**
1. **Security Considerations**
   - By default, only `/health` and `/info` are exposed publicly.
   - To expose all endpoints, modify `application.properties`:
     ```properties
     management.endpoints.web.exposure.include=*
     ```
   - Restrict specific endpoints using:
     ```properties
     management.endpoints.web.exposure.exclude=beans,env,mappings
     ```

2. **Enabling Additional Features**
   - Some endpoints (like `/heapdump`, `/threaddump`, `/httptrace`) need extra dependencies or explicit configurations.
   - `/httptrace` requires enabling tracing:
     ```properties
     management.endpoint.httptrace.enabled=true
     ```

### **Interview Tips**
✅ Explain why Actuator is important for monitoring & troubleshooting microservices.  
✅ Highlight security concerns—exposing endpoints in production can reveal sensitive data.  
✅ Real-world scenario: DevOps teams use `/metrics` and `/health` to track service performance and trigger auto-scaling in Kubernetes.

Would you like a demo on **how to customize Actuator responses** with a code example? 🚀