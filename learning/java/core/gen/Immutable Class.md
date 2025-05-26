# **Immutable Employee Class in Java**

Below is a complete implementation of an **immutable** `Employee` class with fields:  
✅ **Name** (String)  
✅ **City** (String)  
✅ **Date of Joining (DOJ)** (`LocalDate`)  
✅ **Address List** (`List<String>` - Must be defensively copied to ensure immutability)

---

## **1. Immutable Employee Class**
```java
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public final class Employee {
    // Final fields (immutable state)
    private final String name;
    private final String city;
    private final LocalDate doj;
    private final List<String> addressList;

    // Constructor (validates and defensively copies mutable fields)
    public Employee(String name, String city, LocalDate doj, List<String> addressList) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.city = Objects.requireNonNull(city, "City cannot be null");
        this.doj = Objects.requireNonNull(doj, "Date of Joining cannot be null");
        
        // Defensive copy of the mutable List
        this.addressList = new ArrayList<>(Objects.requireNonNull(addressList, "Address list cannot be null"));
    }

    // Getters (no setters!)
    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getDoj() {
        return doj;
    }

    // Returns an unmodifiable view of the list
    public List<String> getAddressList() {
        return Collections.unmodifiableList(addressList);
    }

    // Optional: Override toString() for better logging
    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", doj=" + doj +
                ", addressList=" + addressList +
                '}';
    }

    // Optional: Override equals() and hashCode() for equality checks
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return name.equals(employee.name) &&
                city.equals(employee.city) &&
                doj.equals(employee.doj) &&
                addressList.equals(employee.addressList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, doj, addressList);
    }
}
```

---

## **2. Why is This Class Immutable?**
1. **Final Class**: Prevents subclassing (no mutable subclasses).
2. **Final Fields**: State cannot be modified after construction.
3. **Defensive Copying**:
    - The `addressList` is copied in the constructor and returned as **unmodifiable** via `Collections.unmodifiableList()`.
4. **No Setters**: No methods modify internal state.
5. **Validation**: `Objects.requireNonNull()` ensures no `null` values.

---

## **3. Example Usage**
```java
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> addresses = Arrays.asList("123 Main St", "456 Park Ave");
        Employee emp = new Employee("John Doe", "New York", LocalDate.of(2020, 1, 15), addresses);

        System.out.println(emp);

        // Attempting to modify the list (will throw UnsupportedOperationException)
        List<String> empAddresses = emp.getAddressList();
        try {
            empAddresses.add("789 Broadway"); // Fails!
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify address list!");
        }

        // Original list remains unchanged
        System.out.println("Original addresses: " + addresses); // Still has 2 elements
    }
}
```

**Output:**
```
Employee{name='John Doe', city='New York', doj=2020-01-15, addressList=[123 Main St, 456 Park Ave]}
Cannot modify address list!
Original addresses: [123 Main St, 456 Park Ave]
```

---

## **4. Key Immutability Techniques**
| Technique | Purpose | Example |
|-----------|---------|---------|
| **Final Class** | Prevents subclassing | `public final class Employee` |
| **Final Fields** | State cannot change | `private final String name` |
| **Defensive Copy** | Protects mutable fields | `new ArrayList<>(addressList)` |
| **Unmodifiable View** | Prevents external modifications | `Collections.unmodifiableList()` |
| **Validation** | Ensures invariants | `Objects.requireNonNull()` |

---

## **5. When to Use Immutable Objects?**
✅ **Thread Safety**: No synchronization needed (thread-safe by design).  
✅ **Caching/Hashing**: Safe to use as keys in `HashMap` or `HashSet`.  
✅ **Functional Programming**: Aligns with stateless transformations.

---

## **Interview Questions**
1. **"How do you ensure immutability for a class with a `List` field?"**
    - **Answer**: Defensive copy in constructor + return an **unmodifiable list** in the getter.

2. **"What are the advantages of immutable objects?"**
    - **Answer**: Thread safety, simplicity, cacheability, and predictability.

3. **"Can an immutable class have mutable fields?"**
    - **Answer**: Yes, but they must be **defensively copied** and never exposed directly.

---

