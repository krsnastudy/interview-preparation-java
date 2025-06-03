Here’s a curated list of **frequently asked SQL queries** in **experienced-level interviews**, especially relevant for backend developers like you, Krishna. These cover key areas such as **joins, subqueries, aggregations, window functions, performance tuning, and real-world business logic**.

---

## 🔥 **Top SQL Queries Asked in Experienced Interviews**

---

### 1. **Top N Records**

**Q:** Fetch top 3 highest-paid employees.

```sql
SELECT emp_name, salary
FROM (
  SELECT emp_name, salary,
         RANK() OVER (ORDER BY salary DESC) AS rnk
  FROM employee
) AS ranked
WHERE rnk <= 3;
```

---

### 2. **Second Highest Salary**

```sql
SELECT MAX(salary) AS second_highest
FROM employee
WHERE salary < (SELECT MAX(salary) FROM employee);
```

---

### 3. **Employees in Multiple Departments**

Assuming `employee_department(emp_id, dept_id)`:

```sql
SELECT emp_id
FROM employee_department
GROUP BY emp_id
HAVING COUNT(DISTINCT dept_id) > 1;
```

---

### 4. **Employee with Maximum Salary in Each Department**

```sql
SELECT emp_id, emp_name, dept_id, salary
FROM (
  SELECT *, 
         RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rnk
  FROM employee
) ranked
WHERE rnk = 1;
```

---

### 5. **Duplicate Records Detection**

**Q:** Find duplicate emails.

```sql
SELECT email, COUNT(*)
FROM employee
GROUP BY email
HAVING COUNT(*) > 1;
```

---

### 6. **Fetch Departments with No Employees**

```sql
SELECT d.dept_id, d.dept_name
FROM department d
LEFT JOIN employee e ON e.dept_id = d.dept_id
WHERE e.emp_id IS NULL;
```

---

### 7. **Find Employees Who Never Took a Leave**

Assume `leave(emp_id, leave_date)`:

```sql
SELECT e.emp_name
FROM employee e
WHERE NOT EXISTS (
  SELECT 1 FROM leave l WHERE l.emp_id = e.emp_id
);
```

---

### 8. **Get Employees Who Joined in Last 30 Days**

```sql
SELECT * 
FROM employee
WHERE join_date >= CURRENT_DATE - INTERVAL 30 DAY;
```

---

### 9. **Find Department-wise Average Salary**

```sql
SELECT dept_id, AVG(salary) AS avg_salary
FROM employee
GROUP BY dept_id;
```

---

### 10. **Employees with Same Salary**

```sql
SELECT salary
FROM employee
GROUP BY salary
HAVING COUNT(*) > 1;
```

---

### 11. **Fetch Continuous Date Ranges (Gap Filling)**

Assume a `logins(user_id, login_date)` table:

```sql
SELECT user_id, 
       MIN(login_date) AS start_date,
       MAX(login_date) AS end_date
FROM (
  SELECT user_id, login_date,
         login_date - INTERVAL ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date) DAY AS grp
  FROM logins
) t
GROUP BY user_id, grp;
```

---

### 12. **Employee-Manager Relationship (Self Join)**

```sql
SELECT e.emp_name AS employee, m.emp_name AS manager
FROM employee e
LEFT JOIN employee m ON e.manager_id = m.emp_id;
```

---

### 13. **Query Using CASE Expression**

```sql
SELECT emp_name, salary,
       CASE 
         WHEN salary > 100000 THEN 'High'
         WHEN salary > 50000 THEN 'Medium'
         ELSE 'Low'
       END AS salary_band
FROM employee;
```

---

### 14. **Pivot-like Result (Department-wise Count)**

```sql
SELECT 
  COUNT(CASE WHEN dept_id = 1 THEN 1 END) AS HR,
  COUNT(CASE WHEN dept_id = 2 THEN 1 END) AS IT,
  COUNT(CASE WHEN dept_id = 3 THEN 1 END) AS Finance
FROM employee;
```

---

### 15. **Delete Duplicate Rows (Keep One)**

```sql
DELETE FROM employee
WHERE emp_id NOT IN (
  SELECT MIN(emp_id)
  FROM employee
  GROUP BY email
);
```

---

## ✅ TL;DR – What Interviewers Are Testing

| Area                      | Focus Queries                                                 |
| ------------------------- | ------------------------------------------------------------- |
| Joins & Subqueries        | Manager-employee, max salary, unmatched records               |
| Aggregation & Grouping    | AVG, COUNT, GROUP BY with HAVING                              |
| Window Functions          | `RANK()`, `ROW_NUMBER()`, `DENSE_RANK()`                      |
| Real-World Business Logic | Leaves, top earners, department analysis                      |
| Performance Thinking      | Use of `EXISTS`, avoiding full scans, indexing considerations |

---
