**Clustered** vs **Non-Clustered Index** is crucial for database performance tuning.

---

### 🔍 **Clustered vs Non-Clustered Index: Key Differences**

| Feature              | **Clustered Index**                                             | **Non-Clustered Index**                                             |
| -------------------- | --------------------------------------------------------------- | ------------------------------------------------------------------- |
| **Data Storage**     | Sorts and stores the actual table data rows in the index order  | Stores a separate structure; points to the data rows                |
| **Number per Table** | Only **one** clustered index (since data can be sorted one way) | **Multiple** non-clustered indexes can exist                        |
| **Access Speed**     | Faster for **range queries**, sorting, and primary key lookups  | Slightly slower, needs an extra lookup (called **bookmark lookup**) |
| **Storage Location** | Data is stored **in the index itself**                          | Index has pointers (row locators) to actual data                    |
| **Usage Scenario**   | Used for **primary key** or heavily queried columns             | Used for **frequently searched columns** not part of primary key    |

---

### 🧠 Analogy

* **Clustered Index**: Like a **phone book** sorted by last name. The data itself is sorted.
* **Non-Clustered Index**: Like an **index in a textbook**. It points to the location of the actual content.

---

### 🧪 Example (SQL Server / MySQL)

```sql
-- Clustered Index (usually created with PRIMARY KEY)
CREATE TABLE employees (
    emp_id INT PRIMARY KEY, -- clustered index by default
    name VARCHAR(100),
    department VARCHAR(50)
);

-- Non-Clustered Index
CREATE NONCLUSTERED INDEX idx_dept ON employees(department);
```

* `emp_id`: Clustered index — table data is physically sorted by this.
* `department`: Non-clustered index — separate index structure with pointers to rows.

---

### ⚙ How They Work Internally

#### Clustered Index:

* B-Tree structure.
* Leaf nodes **contain actual data**.
* Only one allowed — it defines the **physical order** of rows.

#### Non-Clustered Index:

* Also B-Tree.
* Leaf nodes **store row locators** (pointers to actual table data).
* Can be **multiple** per table.

---

### ✅ TL;DR

| Type            | Data Sorted | Points to Data       | Count per Table | Ideal For                          |
| --------------- | ----------- | -------------------- | --------------- | ---------------------------------- |
| Clustered Index | ✅ Yes       | ❌ No (contains data) | 1               | Primary key, range queries         |
| Non-Clustered   | ❌ No        | ✅ Yes                | Many            | Filtering/searching non-PK columns |

---
