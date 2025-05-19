The **CAP Theorem** is a fundamental principle in **distributed systems** that describes the trade-offs between three guarantees:

> **C**onsistency, **A**vailability, and **P**artition Tolerance

---

## 🧠 Definition

**CAP Theorem** states:

> *In a distributed system, you can **only achieve 2 out of the 3 guarantees** (Consistency, Availability, Partition Tolerance) at the same time.*

---

## 🔁 Key Terms

| Term                        | Meaning                                                                                           |
| --------------------------- | ------------------------------------------------------------------------------------------------- |
| **Consistency (C)**         | All nodes see the same data at the same time. Every read returns the most recent write.           |
| **Availability (A)**        | Every request gets a (non-error) response, even if it’s not the latest data.                      |
| **Partition Tolerance (P)** | The system continues to work despite network partitions (communication breakdowns between nodes). |

---

## 📊 CAP Triangle

```
       Consistency
         /\
        /  \
       /    \
      /      \
     /        \
    /__________\
Availability    Partition Tolerance
```

You can only pick **2**:

* **CP**: Consistent + Partition-Tolerant
* **CA**: Consistent + Available (only possible in non-distributed systems)
* **AP**: Available + Partition-Tolerant

---

## 🚦 Real-World Examples

| System                      | Type | Behavior                                                    |
| --------------------------- | ---- | ----------------------------------------------------------- |
| **MongoDB (configurable)**  | AP   | Allows eventual consistency                                 |
| **Cassandra**               | AP   | Prioritizes availability even under partition               |
| **Zookeeper**               | CP   | Strong consistency, may reject requests during partition    |
| **RDBMS (non-distributed)** | CA   | Assumes no partition, supports consistency and availability |
| **Etcd**                    | CP   | Used in Kubernetes for strong consistency                   |

---

## 💬 Interview Answer (Summary)

> CAP Theorem states that in the presence of a network partition, a distributed system must choose between **consistency** and **availability**. Since partition tolerance is essential in distributed systems, we usually pick either **CP** or **AP** based on system needs.

---

## 💡 Design Guidance

| Requirement             | Pick                                   |
| ----------------------- | -------------------------------------- |
| Banking, Payments       | **CP** – Strong consistency required   |
| Social Media, News Feed | **AP** – Availability is more critical |
| Distributed Locks       | **CP** (e.g., Zookeeper)               |
| Real-time Analytics     | **AP** (e.g., Cassandra)               |

---

## 📚 Resources

* [CAP Theorem by Martin Kleppmann](https://martin.kleppmann.com/2015/05/25/cap-theorem.html)
* [Google Cloud - CAP Theorem](https://cloud.google.com/blog/topics/developers-practitioners/cap-theorem-practical-guide)
* Book: **Designing Data-Intensive Applications** by Martin Kleppmann

---

## ✅ TL;DR

* **CAP = Consistency, Availability, Partition Tolerance**
* Can only guarantee **2 out of 3** in a distributed system
* Most real-world systems choose **CP** or **AP**, not CA
* Design trade-offs depend on domain: banking (CP), social media (AP)

---