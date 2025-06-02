The **Primary Key** and **Unique Key** in a database are both used to enforce **uniqueness** of values in a column or a group of columns. However, there are **important differences** between them in behavior and constraints:

---

### 🔑 **Primary Key vs Unique Key: Key Differences**

| Feature              | **Primary Key**                               | **Unique Key**                                       |
| -------------------- | --------------------------------------------- | ---------------------------------------------------- |
| **Uniqueness**       | Ensures all values are unique                 | Ensures all values are unique                        |
| **NULLs Allowed**    | ❌ Not allowed (implicitly NOT NULL)           | ✅ Allowed (can contain one or more NULLs)            |
| **Number per Table** | Only **one** primary key per table            | Can have **multiple** unique keys                    |
| **Default Index**    | Creates a **clustered index** (in most RDBMS) | Creates a **non-clustered index**                    |
| **Purpose**          | Main identifier for a row (entity identity)   | Used to enforce uniqueness, not necessarily identity |
| **Constraint Name**  | Default name is `PK_<table_name>`             | Default name is `UQ_<table_name>_<column_name>`      |

---

### ✅ Example

```sql
-- Primary Key
CREATE TABLE employee (
    emp_id INT PRIMARY KEY,        -- only one primary key
    email VARCHAR(100) UNIQUE      -- unique constraint allowed
);
```

In this example:

* `emp_id` is the **primary key** – cannot be null, must be unique.
* `email` is a **unique key** – must be unique, but **can be null**.

---

### 🧠 Summary

* **Primary Key**: Used as the **main identifier**; must be unique and **NOT NULL**.
* **Unique Key**: Used to **enforce uniqueness**; **NULL values are allowed**.

---

### 🔧 SQL Example

```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY,              -- Primary Key: uniquely identifies each user, NOT NULL
    username VARCHAR(50) UNIQUE,          -- Unique Key: must be unique, NULL allowed
    email VARCHAR(100) UNIQUE,            -- Unique Key: must be unique, NULL allowed
    phone_number VARCHAR(20)              -- No constraint
);
```

### 🔍 Explanation

* `user_id`:

    * **Primary Key**
    * Cannot be `NULL`
    * Must be **unique**
    * Only **one** per table

* `username` and `email`:

    * **Unique Keys**
    * Allow **NULL** values
    * Must be unique **if not NULL**
    * You can have **multiple** unique keys in one table

---

### 📌 Insert Examples

```sql
-- ✅ Valid: unique user_id, username, and email
INSERT INTO users VALUES (1, 'krishna123', 'krishna@example.com', '9999999999');

-- ✅ Valid: second row with NULL username and NULL email
INSERT INTO users VALUES (2, NULL, NULL, '8888888888');

-- ❌ Invalid: duplicate user_id (primary key violation)
-- INSERT INTO users VALUES (1, 'john_doe', 'john@example.com', '7777777777');

-- ❌ Invalid: duplicate username (unique constraint violation)
-- INSERT INTO users VALUES (3, 'krishna123', 'krish2@example.com', '6666666666');

-- ✅ Valid: same NULL in username/email is allowed (NULL ≠ NULL in SQL)
INSERT INTO users VALUES (4, NULL, NULL, '5555555555');
```

---

### ✅ TL;DR

* **Primary Key**: Unique + Not Null (only one per table)
* **Unique Key**: Unique + allows Null (can have multiple)
