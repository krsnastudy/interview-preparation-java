To get the **student with the 2nd highest total marks**, we need to:

1. **Group by name**, and sum the marks.
2. **Order the results by total marks** in descending order.
3. **Pick the 2nd highest**.

---

## ✅ **SQL Query**

```sql
SELECT name, SUM(marks) AS total_marks
FROM student
GROUP BY name
ORDER BY total_marks DESC
LIMIT 1 OFFSET 1;
```

---

## 🔍 **Explanation**
- `GROUP BY name`: Aggregates marks for each student name.
- `SUM(marks)`: Computes total marks per student.
- `ORDER BY total_marks DESC`: Highest first.
- `LIMIT 1 OFFSET 1`: Skips the top one (1st highest) and fetches the **2nd highest**.

---

## 🧾 **Result for your data:**

| name  | total_marks |
|-------|-------------|
| **Raj**   | **70**         |

✅ This gives **Raj** with the second-highest total marks (70).

---

In SQL, the clause:

```sql
LIMIT 1 OFFSET 1
```

means:

* **`OFFSET 1`**: Skip the **first row** of the result set.
* **`LIMIT 1`**: Return **only 1 row** after skipping.

---

### 📌 Breakdown

If a query (e.g., `SELECT * FROM users`) normally returns:

| id | name  |
| -- | ----- |
| 1  | Alice |
| 2  | Bob   |
| 3  | Carol |

Then this query:

```sql
SELECT * FROM users LIMIT 1 OFFSET 1;
```

will return:

| id | name |
| -- | ---- |
| 2  | Bob  |

---

### ✅ Use Case

This pattern is commonly used in:

* **Pagination**: Skip rows and fetch the next batch.
* **Getting the 2nd row**, 3rd row, etc. from a result set.

### 🔁 Equivalent Alternative (in MySQL/PostgreSQL):

```sql
SELECT * FROM users ORDER BY id LIMIT 1 OFFSET 1;
```

> Always pair `LIMIT/OFFSET` with `ORDER BY` for deterministic results.

---

### TL;DR:

`LIMIT 1 OFFSET 1` → Skip the first row and return the **second row** in the result.

Would you like a pagination-ready query example with page size and number too?
