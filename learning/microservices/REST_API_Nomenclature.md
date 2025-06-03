Here's a detailed and structured explanation of **REST API Nomenclature** — an essential topic for designing clean, intuitive, and scalable APIs in microservices and Spring Boot applications. It's commonly asked in interviews, especially around API design best practices.

---

## 📘 What is REST API Nomenclature?

REST API nomenclature refers to the **naming conventions and structure** used for designing endpoints, HTTP methods, and resources in a RESTful web service.

It ensures:

* **Consistency**
* **Readability**
* **Scalability**
* **Client understanding without docs**

---

## 🔧 Core Concepts

| Concept          | Description                                                          |
| ---------------- | -------------------------------------------------------------------- |
| **Resource**     | A noun that represents a data entity (e.g., users, orders, products) |
| **Endpoint**     | A URL to access or manipulate resources                              |
| **HTTP Method**  | Defines the operation (GET, POST, PUT, DELETE, PATCH)                |
| **URI Naming**   | Follows conventions for hierarchy and operations                     |
| **Plural Nouns** | Used to represent collections (e.g., `/users`, `/orders`)            |

---

## 🧱 Standard REST Naming Structure

```
HTTP METHOD  +  /resource-name  => Meaning
```

### ✅ Examples

| HTTP Method | URL          | Operation               |
| ----------- | ------------ | ----------------------- |
| `GET`       | `/users`     | Get list of users       |
| `GET`       | `/users/123` | Get user by ID          |
| `POST`      | `/users`     | Create a new user       |
| `PUT`       | `/users/123` | Update the entire user  |
| `PATCH`     | `/users/123` | Update part of the user |
| `DELETE`    | `/users/123` | Delete a user           |

---

## 🧭 Resource Best Practices

### 🔸 Use **nouns**, not verbs

❌ `/getUser`, `/deleteUser`

✅ `/users`, `/orders`

---

### 🔸 Use **plural nouns**

❌ `/user`, `/order`

✅ `/users`, `/orders`

---

### 🔸 Use **nested resources** for relationships

E.g., Orders of a User

```
GET /users/123/orders       → All orders of user 123
GET /users/123/orders/987   → Specific order of user 123
```

---

### 🔸 Use **query parameters** for filtering, sorting, pagination

```
GET /users?role=admin&sort=name&page=2&size=10
```

---

### 🔸 Use **HTTP Status Codes** properly

| Code | Meaning               |
| ---- | --------------------- |
| 200  | OK                    |
| 201  | Created               |
| 204  | No Content            |
| 400  | Bad Request           |
| 401  | Unauthorized          |
| 403  | Forbidden             |
| 404  | Not Found             |
| 409  | Conflict              |
| 500  | Internal Server Error |

---

## 🧑‍💻 URI Naming Conventions

| Use Case        | Example                                         |
| --------------- | ----------------------------------------------- |
| Collection      | `/products`                                     |
| Item            | `/products/45`                                  |
| Sub-resource    | `/products/45/reviews`                          |
| Search/filter   | `/products?category=books&sort=price`           |
| Action (custom) | `/orders/123/cancel` (if needed, use sparingly) |

---

## 📋 Real-World Example – Order Service

| Action          | Endpoint                 | Method |
| --------------- | ------------------------ | ------ |
| Create Order    | `/orders`                | `POST` |
| Get All Orders  | `/orders`                | `GET`  |
| Get Order by ID | `/orders/{id}`           | `GET`  |
| Update Order    | `/orders/{id}`           | `PUT`  |
| Cancel Order    | `/orders/{id}/cancel`    | `POST` |
| Get User Orders | `/users/{userId}/orders` | `GET`  |

---

## ✍️ Interview Tips

* Always **prefer nouns over verbs**.
* Avoid using actions in URLs like `/createUser`.
* Use **status codes and error messages** correctly.
* Follow **HATEOAS** (Hypermedia links) if building hypermedia-driven REST APIs.
* Apply **pagination, filtering, sorting** via query params.

---

## 🛠️ Tools & Resources

* 🔗 [RESTful API Design – Microsoft](https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design)
* 🔗 [Best Practices from RestfulAPI.net](https://restfulapi.net/)
* 🔗 [Spring REST API Best Practices – Baeldung](https://www.baeldung.com/rest-api-best-practices)

---

## ✅ TL;DR

* Use **HTTP verbs** to indicate actions, **nouns** for resources.
* Use **plural** resource names.
* Use **path parameters** for resource identity, **query parameters** for filtering/sorting.
* Structure URIs logically for relationships.
* Return **meaningful HTTP status codes**.

---
