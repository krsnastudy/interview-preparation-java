---

## ✅ **Difference Between WAR vs JAR**

| Aspect                  | **JAR (Java ARchive)**                                      | **WAR (Web Application ARchive)**                                |
| ----------------------- | ----------------------------------------------------------- | ---------------------------------------------------------------- |
| **Purpose**             | Package for **Java applications** (standalone or libraries) | Package for **Web applications** (Servlets, JSPs, etc.)          |
| **Used in**             | Desktop apps, Microservices (Spring Boot), libraries        | Traditional web apps (Spring MVC, JSP-based apps)                |
| **Deployment**          | Can be run directly via `java -jar`                         | Deployed to servlet containers (e.g., Tomcat, JBoss, WebLogic)   |
| **Structure**           | `META-INF/`, classes, libraries                             | `WEB-INF/`, `web.xml`, JSPs, classes, libs                       |
| **Main class**          | Defined in `MANIFEST.MF` (`Main-Class`)                     | No main class — relies on servlet container                      |
| **Spring Boot support** | Spring Boot apps are usually **packaged as JARs**           | Can also create WARs if deploying to external servlet containers |
| **Executable**          | Yes (`java -jar app.jar`)                                   | No (needs servlet container like Tomcat)                         |
| **Lightweight**         | ✅ Yes (good for microservices)                              | ❌ Heavier (more suited for traditional enterprise apps)          |

---

## 🔍 **Interview Insight: When to Use What?**

### 🔸 **Use JAR**:

* For **Spring Boot microservices**
* When using **embedded servers** (like embedded Tomcat/Jetty)
* For **cloud-native applications** and containerized deployments

### 🔸 **Use WAR**:

* For **legacy enterprise applications**
* When deploying to an **external application server** (e.g., Tomcat, JBoss)
* When using **Java EE** technologies like JSP/Servlets

---

## ✅ Example Structure

### JAR:

```
/META-INF/MANIFEST.MF
/com/krishna/App.class
/lib/dependency.jar
```

### WAR:

```
/WEB-INF/web.xml
/WEB-INF/classes/com/krishna/Controller.class
/WEB-INF/lib/spring.jar
/index.jsp
```

---

## ✅ TL;DR

| Feature           | JAR                          | WAR                              |
| ----------------- | ---------------------------- | -------------------------------- |
| **For Java Apps** | ✅ Yes                        | ❌ No                             |
| **For Web Apps**  | ⚠️ Only with embedded server | ✅ Yes (Servlet-based)            |
| **Executable**    | ✅ Yes (`java -jar`)          | ❌ No (needs servlet container)   |
| **Spring Boot**   | ✅ Preferred                  | ⚠️ Optional (needs config)       |
| **Deployment**    | Standalone                   | Servlet container (Tomcat, etc.) |

---
