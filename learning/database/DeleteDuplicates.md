To **delete duplicate rows** from a table while **retaining only one copy**, you can use **Common Table Expressions (CTE)** or subqueries depending on the database.

---

### ✅ **Generic SQL Approach using CTE (SQL Server, PostgreSQL, etc.):**

Assume the table is `employees(id, name, department)` and you want to keep one row per duplicate combination of name & department.

```sql
WITH CTE AS (
  SELECT *,
         ROW_NUMBER() OVER (PARTITION BY name, department ORDER BY id) AS rn
  FROM employees
)
DELETE FROM employees
WHERE id IN (
  SELECT id FROM CTE WHERE rn > 1
);
```

---

### ✅ **For MySQL (pre-8.0, without CTE):**

```sql
DELETE e1 FROM employees e1
JOIN employees e2
ON e1.name = e2.name AND e1.department = e2.department
AND e1.id > e2.id;
```

---

### 🧠 **Interview Tip:**

* Use `ROW_NUMBER()` in a CTE to find duplicates.
* Always specify **what makes a row duplicate** (columns).
* In MySQL, `JOIN`-based delete is common.
* Be cautious: **backup before delete** in production.

---
