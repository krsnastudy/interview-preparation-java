To **show the availability and latency of a microservice over a day**, you typically use a **monitoring and observability toolchain** that collects, stores, and visualizes metrics. These are the **two key metrics**:

---

### 🔍 1. **Availability**

**Definition**: % of time the service is up and healthy over a period (e.g., 24h).
**Formula**:

```text
Availability (%) = (Uptime / Total Time) * 100
```

---

### 🔍 2. **Latency**

**Definition**: Time taken to respond to requests (p50, p90, p99).
Measured in **milliseconds** or **seconds**.

---

## ✅ How to Capture and Show This in Microservices

### A. **Expose Metrics via Actuator**

If you're using Spring Boot:

```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
# application.yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
```

This exposes:

* `/actuator/health` → for availability
* `/actuator/metrics/http.server.requests` → for latency

---

### B. **Use Prometheus + Grafana for Visualization**

#### 🔸 Prometheus

* Scrapes metrics from your microservice (`/actuator/prometheus`).
* Stores time-series data.

#### 🔸 Grafana

* Connects to Prometheus.
* Builds dashboards with visual graphs.

#### Example PromQL queries:

* **Availability**:

  ```promql
  up{job="my-service"} == 1
  ```
* **Latency**:

  ```promql
  histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))
  ```

---

### C. **Visualization Dashboard Example in Grafana**

Show charts like:

| Time (HH\:MM) | Availability (%) | p50 Latency | p95 Latency | p99 Latency |
| ------------- | ---------------- | ----------- | ----------- | ----------- |
| 01:00         | 100%             | 120ms       | 300ms       | 500ms       |
| 06:00         | 100%             | 130ms       | 320ms       | 510ms       |
| 12:00         | 99.9%            | 140ms       | 350ms       | 580ms       |
| ...           | ...              | ...         | ...         | ...         |

You can also create **SLI/SLO dashboards** based on this.

---

### D. **Optional: Use Distributed Tracing**

For deeper latency analysis:

* Use **OpenTelemetry**, **Jaeger**, or **Zipkin**
* Track per-request latency across services

---

## 💡 Interview Angle

| Question                                   | Sample Points                             |
| ------------------------------------------ | ----------------------------------------- |
| How do you monitor service health?         | Actuator, Prometheus, Grafana             |
| How do you track latency and availability? | Metrics endpoint, PromQL, p95/p99 latency |
| How do you alert on downtime?              | Alert rules in Prometheus or Grafana      |

---

### 🧠 TL;DR:

To show **availability & latency of a microservice over a day**:

* Use Spring Boot Actuator to expose metrics
* Use **Prometheus** to collect them
* Use **Grafana** to visualize with daily time-range
* Track **uptime %** and **latency percentiles (p95/p99)**

---
