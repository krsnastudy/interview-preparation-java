### 🦁 What is **ZooKeeper**?

**Apache ZooKeeper** is a **centralized service** used for:

* **Configuration management**
* **Service discovery**
* **Distributed synchronization**
* **Leader election**

It's often used in **distributed systems** to manage and coordinate various nodes.

---

## 📦 Why is ZooKeeper Important?

In distributed systems, managing:

* Shared state
* Coordination
* Failure recovery

...is hard. ZooKeeper solves this by providing **a consistent, fault-tolerant system** for small pieces of critical data (like configuration or service registration).

---

## 🔑 Key Features

| Feature               | Description                                                |
| --------------------- | ---------------------------------------------------------- |
| **Consistency**       | Strong consistency via atomic broadcast protocol (Zab)     |
| **High Availability** | Works with replicated ensemble of nodes                    |
| **Watchers**          | Notify clients when data changes                           |
| **Ephemeral nodes**   | Temporary nodes that auto-delete when a client disconnects |
| **Sequential nodes**  | Auto-incremented nodes used in leader elections            |

---

## 🔧 ZooKeeper Data Model

* **Hierarchical** like a filesystem (`/app/config`, `/app/nodes`)
* Nodes are called **znodes**
* Znodes can store small amounts of data (\~1MB)
* Can be **persistent** or **ephemeral**

---

## 💻 Use Cases in Real Life

| Scenario                | ZooKeeper Usage                                              |
| ----------------------- | ------------------------------------------------------------ |
| **Kafka**               | Broker metadata and controller election                      |
| **Hadoop/HDFS**         | Coordination of NameNode HA                                  |
| **Apache Storm**        | Supervisor coordination                                      |
| **Service Discovery**   | Store available services and notify clients of changes       |
| **Leader Election**     | Elect a leader in a distributed system via sequential znodes |
| **Distributed Locking** | Implement locks using ephemeral nodes                        |

---

## 📘 Example: Leader Election

1. Multiple services create **sequential ephemeral znodes** in `/leaders`.
2. The service with the **smallest node ID** becomes the leader.
3. If it dies, its ephemeral node is deleted, and the next in line becomes the leader.

---

## ⚙️ Basic Commands

```bash
# Start ZooKeeper (standalone)
zkServer.sh start

# Connect to CLI
zkCli.sh

# Create node
create /myapp "Hello"

# Get node data
get /myapp

# List children
ls /

# Delete node
delete /myapp
```

---

## 🔄 ZooKeeper vs etcd vs Consul

| Feature       | ZooKeeper     | etcd            | Consul                     |
| ------------- | ------------- | --------------- | -------------------------- |
| Written in    | Java          | Go              | Go                         |
| Used by       | Kafka, Hadoop | Kubernetes (v3) | HashiCorp tools            |
| Storage model | Hierarchical  | Flat key-value  | Flat with service registry |
| Watch Support | Yes           | Yes             | Yes                        |

---

## 🧠 Interview Tips

* ZooKeeper is **not a database** — it’s for **coordination**, not bulk storage.
* Mention how it’s used in **Kafka** for broker metadata and leader election.
* Understand **ephemeral + sequential znodes** for distributed locking or elections.
* Know the **ZAB protocol** (ZooKeeper Atomic Broadcast) for consensus.

---

## 📚 Resources

* Official: [https://zookeeper.apache.org](https://zookeeper.apache.org)
* [ZooKeeper Internals – DZone](https://dzone.com/articles/zookeeper-internals)
* [YouTube – ZooKeeper Simplified](https://www.youtube.com/watch?v=8krd5qKv9rI)

---

## ✅ TL;DR

| Feature    | Summary                                              |
| ---------- | ---------------------------------------------------- |
| 💬 What    | Centralized coordination system for distributed apps |
| 🎯 Use     | Config mgmt, service discovery, leader election      |
| 🧠 Concept | Hierarchical znodes, ephemeral & sequential nodes    |
| 💪 Power   | High availability + strong consistency               |

