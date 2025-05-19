### 🧠 What is JPA Caching?

**JPA Caching** is a mechanism that improves the performance of data access operations by **avoiding repetitive database hits** for the same data. JPA supports **two levels of caching**:

---

## 🔹 1. First-Level Cache (L1)

* **Scope:** Per `EntityManager` (i.e., per transaction/session)
* **Built-in:** Always enabled
* **Behavior:** When you fetch an entity, it is stored in memory for the duration of the transaction. If you access the same entity again, JPA serves it from memory instead of hitting the DB.

### ✅ Example:

```java
EntityManager em = entityManagerFactory.createEntityManager();

Employee e1 = em.find(Employee.class, 1L);  // DB hit
Employee e2 = em.find(Employee.class, 1L);  // Served from cache

System.out.println(e1 == e2); // true
```

---

## 🔸 2. Second-Level Cache (L2)

* **Scope:** Shared across multiple EntityManagers (global cache)
* **Optional:** Needs explicit setup
* **Used With:** Hibernate, Ehcache, Infinispan, etc.
* **Benefit:** Entities can be cached across sessions, even between different requests or services.

### ✅ Real-Life Scenario

Let’s say you have an e-commerce site:

* Product catalog is read **frequently** but updated **rarely**.
* Thousands of users browse products.
* Without caching, each request hits the DB — bad for performance.

➡️ **With L2 cache**:

* Product entities are stored in a shared cache.
* The DB is hit only once, and the next 1000 reads are served from the cache.

---

## 🔧 How to Enable Second-Level Cache (Hibernate + Ehcache)

### 1. Add Dependencies (Maven)

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-ehcache</artifactId>
</dependency>
```

### 2. Hibernate Configuration

```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
spring.cache.jcache.config=classpath:ehcache.xml
```

### 3. Annotate Entity

```java
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Product {
    @Id
    private Long id;
    private String name;
}
```

---

## 🚫 When Not to Use Caching

| Condition                             | Recommendation                   |
| ------------------------------------- | -------------------------------- |
| High update/delete frequency          | Avoid L2 cache (stale data risk) |
| Sensitive, real-time data             | Use DB hits directly             |
| Transactions with pessimistic locking | May not play well with L2 cache  |

---

## 🧪 Cache Hit vs Miss

| Request                    | L1 Cache | L2 Cache | DB Hit |
| -------------------------- | -------- | -------- | ------ |
| 1st Request                | ❌        | ❌        | ✅      |
| 2nd Request (same session) | ✅        | ❌        | ❌      |
| 3rd Request (new session)  | ❌        | ✅        | ❌      |

---

## ✅ TL;DR

| Level | Scope                  | Enabled By Default | Purpose                                 |
| ----- | ---------------------- | ------------------ | --------------------------------------- |
| L1    | Per EntityManager      | ✅                  | Avoid repeated reads in one transaction |
| L2    | Shared across sessions | ❌                  | Reuse across requests, reduce DB load   |

---

## 📚 Resources

* [Hibernate Caching Docs](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#caching)
* [Baeldung – JPA Caching](https://www.baeldung.com/jpa-entitymanager-caching)

---
