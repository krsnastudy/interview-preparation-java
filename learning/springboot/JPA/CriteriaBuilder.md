Here’s a **complete breakdown** of how to implement a **Dynamic Query Builder using JPA Criteria API** for **data sorting** in Spring Boot.

---

## 🧠 Why Use Criteria API?

* Build **type-safe**, **dynamic queries** at runtime.
* Useful for **advanced filtering**, **sorting**, and **pagination**.
* Alternative to JPQL or native SQL when conditions vary.

---

## 🛠️ Use Case Example

Assume you have an entity `Employee`:

```java
@Entity
public class Employee {
    @Id
    private Long id;
    private String name;
    private Double salary;
    private String department;
}
```

You want to:

* Sort by any field dynamically (e.g., `name`, `salary`)
* Sort direction: `ASC` or `DESC`

---

## ✅ Step-by-Step Implementation

### 1. **Method in Repository (Custom)**

Create a **custom repository interface**:

```java
public interface EmployeeRepositoryCustom {
    List<Employee> getEmployeesSorted(String sortBy, String sortDirection);
}
```

### 2. **Implementation Class**

```java
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> getEmployeesSorted(String sortBy, String sortDirection) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        // Sort dynamically
        if ("desc".equalsIgnoreCase(sortDirection)) {
            query.orderBy(cb.desc(root.get(sortBy)));
        } else {
            query.orderBy(cb.asc(root.get(sortBy)));
        }

        return entityManager.createQuery(query).getResultList();
    }
}
```

### 3. **Integrate with Repository**

Extend in your main repository:

```java
public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {
}
```

### 4. **Use in Service**

```java
@Autowired
private EmployeeRepository employeeRepository;

public List<Employee> getSortedEmployees(String sortBy, String direction) {
    return employeeRepository.getEmployeesSorted(sortBy, direction);
}
```

---

## ⚙️ Advanced: Add Filtering & Pagination (Optional)

You can extend this pattern to include:

* Dynamic filters using `Predicate`
* Pagination with `setFirstResult()` and `setMaxResults()`

```java
CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
Root<Employee> root = query.from(Employee.class);

// Example filter: where salary > 5000
Predicate predicate = cb.gt(root.get("salary"), 5000);
query.where(predicate);

// Pagination
TypedQuery<Employee> typedQuery = entityManager.createQuery(query);
typedQuery.setFirstResult(0); // offset
typedQuery.setMaxResults(10); // limit
```

---

## 🧪 Test Sample

URL: `GET /employees?sortBy=name&sortDirection=asc`
→ Returns sorted employee list by name ascending.

---

## 📚 References

* [Baeldung – Criteria API Guide](https://www.baeldung.com/hibernate-criteria-queries)
* [Spring Docs – JPA Criteria API](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.extensions)

---

## ✅ TL;DR

| Feature         | Benefit                               |
| --------------- | ------------------------------------- |
| CriteriaBuilder | Type-safe, dynamic queries            |
| Dynamic Sorting | `cb.asc()` / `cb.desc()`              |
| Runtime Fields  | Pass field & direction as method args |
| Extendability   | Easy to add filters, pagination       |

---
