In databases, **views** are virtual tables representing the result of a stored query. They simplify complex queries, enforce security, and abstract business logic. There are several **types of database views**, each with specific use cases:

---

### 🔹 1. **Simple View**

* Based on a single table.
* Does **not** use functions, joins, or group by.
* Can be **updatable**.

✅ Example:

```sql
CREATE VIEW employee_names AS
SELECT id, name FROM employees;
```

---

### 🔹 2. **Complex View**

* Based on **multiple tables**, includes joins, group by, aggregate functions, etc.
* Typically **not updatable**.

✅ Example:

```sql
CREATE VIEW department_salary AS
SELECT d.name, AVG(e.salary)
FROM employees e JOIN departments d ON e.dept_id = d.id
GROUP BY d.name;
```

---

### 🔹 3. **Materialized View**

* Stores **physically materialized results** of the query.
* Supports **faster read performance**, often used in reporting.
* Needs manual or scheduled **refresh**.

✅ Example (Oracle):

```sql
CREATE MATERIALIZED VIEW sales_summary
AS SELECT region, SUM(sales) FROM orders GROUP BY region;
```

---

### 🔹 4. **Dynamic View (Runtime View)**

* Created on the fly in application logic, not permanently stored in the DB.
* Useful for temporary, user-specific reports.

---

### 🔹 5. **Inline View**

* A subquery used in the `FROM` clause of another query.
* Temporary and unnamed.

✅ Example:

```sql
SELECT * FROM (
  SELECT name, salary FROM employees
) AS temp WHERE salary > 50000;
```

---

### 🔹 6. **Partitioned View (SQL Server)**

* Joins data from multiple **tables (partitions)** into a single virtual table.
* Supports **horizontal partitioning**.

✅ Example:

```sql
CREATE VIEW all_orders AS
SELECT * FROM orders_2022
UNION ALL
SELECT * FROM orders_2023;
```

---

### 🔹 7. **Updatable View**

* Allows `INSERT`, `UPDATE`, `DELETE`.
* Must meet certain conditions (e.g., based on a single table, no aggregation).

---

### 🔹 8. **Read-only View**

* Used only for **data retrieval**, not for updates.
* Common for views with joins or complex expressions.

---

### 🔸 Use Cases

| View Type         | Use Case                            |
| ----------------- | ----------------------------------- |
| Simple View       | Hide certain columns                |
| Complex View      | Abstract complex logic              |
| Materialized View | Performance boost for heavy queries |
| Inline View       | Temporary complex filter logic      |
| Partitioned View  | Logical union of similar tables     |
| Updatable View    | Modify data via abstraction         |
| Read-only View    | Report generation                   |

---

### 📝 TL;DR for Interviews:

* **Simple vs Complex**: Based on complexity and table count.
* **Materialized**: Stored, faster but needs refresh.
* **Inline**: Temporary subquery view.
* **Partitioned**: Merges similar tables.
* **Updatable vs Read-only**: Based on mutability of data.
