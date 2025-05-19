## 🌳 What is the Strangler Design Pattern?

> A pattern where you **gradually replace** parts of a legacy system with a new system **piece by piece**, until the old system is fully replaced — just like a **strangler fig tree** grows around and slowly replaces its host tree.

---

## ✅ Use Case

- Migrating a **monolith to microservices**
- Replacing a legacy system with a **new tech stack**
- Introducing **Spring Boot services** into old applications

---

## 🔁 How it works (Step-by-Step)

### 🧱 1. **Old System is Active**
The legacy app is running in production.

### 🔄 2. **Introduce a Proxy Layer**
A **facade** or **API Gateway** routes requests.

```text
Client → API Gateway → Old System (initially)
```

### ✨ 3. **New Modules Replacing Legacy**
You replace one feature at a time.

```text
Client → API Gateway
            ↳ /orders → New Microservice
            ↳ /users → Old Monolith
```

### ❌ 4. **Eventually Decommission Old System**
Once all routes/features are replaced.

---

## 📦 Example in Java / Microservices

Suppose you have a monolith with `/orders`, `/products`, `/users`:

### 🔁 Migration Flow:
```text
Client → API Gateway
   ↳ /orders   → New Spring Boot Microservice
   ↳ /users    → Legacy Monolith
   ↳ /products → Legacy Monolith
```

As each module is rewritten:
- You update the routing logic
- No need for a big-bang rewrite
- Rollback and testing become safer

---

## 📐 Benefits

- ✅ No downtime during migration
- ✅ Can migrate module-by-module
- ✅ Easier testing and rollback
- ✅ Low risk, high control

---

## ⚠️ Challenges

- Gateway/Proxy management
- Data consistency between old/new
- Gradual code duplication/refactoring

---

## 🧠 TL;DR (Interview Style):

> The Strangler Design Pattern is used to **gradually refactor** or replace a legacy system by routing requests to either the old or the new implementation.  
> It enables **safe, incremental migration** and is widely used in **monolith-to-microservice** transitions using an API gateway or façade.

---

