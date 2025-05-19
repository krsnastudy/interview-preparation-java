**Graceful degradation** is a design principle where a system **continues to operate with reduced functionality** when parts of it fail or are under high stress, rather than crashing completely.

---

### 🎯 Key Idea:

> **"Fail softly, not catastrophically."**

Instead of stopping entirely when something goes wrong, the system provides **limited but useful service**.

---

## 🔧 Real-Life Example

### 1. **E-commerce Website**

* **Normal state**: Product images, recommendations, and reviews are all visible.
* **If recommendation service fails**:

    * Site still loads, products are viewable.
    * A message like "Recommendations are currently unavailable" is shown.

### 2. **Video Streaming App**

* If HD quality is unavailable due to bandwidth limits, it degrades to SD.
* Streaming continues — not perfect, but still functional.

---

## 🧰 In Microservices Context

| Scenario                         | Graceful Degradation                  |
| -------------------------------- | ------------------------------------- |
| Recommendation microservice down | Show products without recommendations |
| Payments slow or failing         | Allow cart saving and notify user     |
| Image CDN unreachable            | Show placeholder image instead        |

Often implemented using:

* Circuit Breakers
* Fallback Methods
* Default Values

---

## ✅ Interview Angle

* Shows resilience and good UX under failure.
* Mention tools like **Resilience4j**, **Hystrix**, or **fallback methods in Spring Cloud**.
* Differentiates from **Fail Fast** (immediately error out).
* Compare with **Fault Tolerance** (system continues working even during partial failures).

---

### 🧠 TL;DR:

**Graceful degradation** means keeping a system **partially functional** when components fail, instead of full breakdown — improving user experience and resilience.
