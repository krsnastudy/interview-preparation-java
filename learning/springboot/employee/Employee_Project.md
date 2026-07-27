# Employee Service — Spring Boot Project (Senior/12 YOE Interview-Ready)

I'll build this as a **layered, production-style architecture** — the kind an interviewer would expect from someone with 12 years of experience: proper separation of concerns, DTO pattern, global exception handling, validation, pagination, and full REST semantics (GET/POST/PUT/PATCH/DELETE).

**Assumptions I'm making** (standard for this level of interview prep): H2 in-memory DB (easy to run, no setup), Lombok for boilerplate, springdoc-openapi for Swagger docs, manual DTO mapping (interviewers often ask you to *explain* MapStruct vs manual vs ModelMapper — I'll cover that in the Q&A section).

---

## 📁 Folder Structure

```
employee-service/
├── pom.xml
└── src
    ├── main
    │   ├── java/com/example/employeeservice
    │   │   ├── EmployeeServiceApplication.java
    │   │   ├── config/
    │   │   │   └── OpenApiConfig.java
    │   │   ├── controller/
    │   │   │   └── EmployeeController.java
    │   │   ├── service/
    │   │   │   ├── EmployeeService.java
    │   │   │   └── impl/EmployeeServiceImpl.java
    │   │   ├── repository/
    │   │   │   └── EmployeeRepository.java
    │   │   ├── entity/
    │   │   │   └── Employee.java
    │   │   ├── dto/
    │   │   │   ├── EmployeeRequestDTO.java
    │   │   │   ├── EmployeePatchDTO.java
    │   │   │   └── EmployeeResponseDTO.java
    │   │   ├── mapper/
    │   │   │   └── EmployeeMapper.java
    │   │   ├── enums/
    │   │   │   └── Department.java
    │   │   └── exception/
    │   │       ├── EmployeeNotFoundException.java
    │   │       ├── DuplicateEmployeeException.java
    │   │       ├── ErrorResponse.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       └── application.yml
    └── test
        └── java/com/example/employeeservice
            ├── controller/EmployeeControllerTest.java
            └── service/EmployeeServiceImplTest.java
```

---

## 1️⃣ `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>employee-service</artifactId>
    <version>1.0.0</version>
    <name>employee-service</name>
    <description>Employee Service - Interview Hands-on Project</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.5.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 2️⃣ Main Application

```java
// EmployeeServiceApplication.java
package com.example.employeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}
```

---

## 3️⃣ `application.yml`

```yaml
server:
  port: 8080

spring:
  application:
    name: employee-service
  datasource:
    url: jdbc:h2:mem:employeedb
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

---

## 4️⃣ Enum

```java
// enums/Department.java
package com.example.employeeservice.enums;

public enum Department {
    ENGINEERING, HR, FINANCE, SALES, MARKETING, OPERATIONS
}
```

---

## 5️⃣ Entity

```java
// entity/Employee.java
package com.example.employeeservice.entity;

import com.example.employeeservice.enums.Department;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Department department;

    private Double salary;

    private LocalDate dateOfJoining;

    @Builder.Default
    private Boolean active = true;
}
```

> **Interview note:** `@Enumerated(EnumType.STRING)` vs `EnumType.ORDINAL` is a classic gotcha question — STRING is safer for schema evolution since ORDINAL breaks if you reorder/insert enum values.

---

## 6️⃣ DTOs

```java
// dto/EmployeeRequestDTO.java
package com.example.employeeservice.dto;

import com.example.employeeservice.enums.Department;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Department is required")
    private Department department;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    private Double salary;

    @NotNull(message = "Date of joining is required")
    @PastOrPresent(message = "Date of joining cannot be in the future")
    private LocalDate dateOfJoining;
}
```

```java
// dto/EmployeePatchDTO.java
package com.example.employeeservice.dto;

import com.example.employeeservice.enums.Department;
import lombok.*;

// All fields optional - used for PATCH (partial update)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePatchDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Department department;
    private Double salary;
    private Boolean active;
}
```

```java
// dto/EmployeeResponseDTO.java
package com.example.employeeservice.dto;

import com.example.employeeservice.enums.Department;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Department department;
    private Double salary;
    private LocalDate dateOfJoining;
    private Boolean active;
}
```

> **Why DTOs instead of exposing the entity directly?** Decouples your API contract from your DB schema, prevents over-posting attacks, and lets you version the API independently. This is a **very common senior-level interview question.**

---

## 7️⃣ Mapper

```java
// mapper/EmployeeMapper.java
package com.example.employeeservice.mapper;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.dto.EmployeeResponseDTO;
import com.example.employeeservice.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDTO dto) {
        return Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .salary(dto.getSalary())
                .dateOfJoining(dto.getDateOfJoining())
                .active(true)
                .build();
    }

    public EmployeeResponseDTO toResponseDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .dateOfJoining(employee.getDateOfJoining())
                .active(employee.getActive())
                .build();
    }
}
```

---

## 8️⃣ Repository

```java
// repository/EmployeeRepository.java
package com.example.employeeservice.repository;

import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.enums.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<Employee> findByEmailIgnoreCase(String email);

    Page<Employee> findByDepartment(Department department, Pageable pageable);
}
```

---

## 9️⃣ Exceptions

```java
// exception/EmployeeNotFoundException.java
package com.example.employeeservice.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }
}
```

```java
// exception/DuplicateEmployeeException.java
package com.example.employeeservice.exception;

public class DuplicateEmployeeException extends RuntimeException {
    public DuplicateEmployeeException(String email) {
        super("Employee already exists with email: " + email);
    }
}
```

```java
// exception/ErrorResponse.java
package com.example.employeeservice.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;
}
```

```java
// exception/GlobalExceptionHandler.java
package com.example.employeeservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EmployeeNotFoundException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler(DuplicateEmployeeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateEmployeeException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", req, details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req, null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message,
                                                          HttpServletRequest req, List<String> details) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .details(details)
                .build();
        return new ResponseEntity<>(error, status);
    }
}
```

---

## 🔟 Service Layer

```java
// service/EmployeeService.java
package com.example.employeeservice.service;

import com.example.employeeservice.dto.*;
import com.example.employeeservice.enums.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);
    EmployeeResponseDTO getEmployeeById(Long id);
    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable);
    Page<EmployeeResponseDTO> getEmployeesByDepartment(Department department, Pageable pageable);
    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);      // PUT - full replace
    EmployeeResponseDTO patchEmployee(Long id, EmployeePatchDTO dto);        // PATCH - partial update
    void deleteEmployee(Long id);
}
```

```java
// service/impl/EmployeeServiceImpl.java
package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.*;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.enums.Department;
import com.example.employeeservice.exception.DuplicateEmployeeException;
import com.example.employeeservice.exception.EmployeeNotFoundException;
import com.example.employeeservice.mapper.EmployeeMapper;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new DuplicateEmployeeException(dto.getEmail());
        }
        Employee saved = employeeRepository.save(employeeMapper.toEntity(dto));
        return employeeMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeMapper.toResponseDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employeeMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployeesByDepartment(Department department, Pageable pageable) {
        return employeeRepository.findByDepartment(department, pageable)
                .map(employeeMapper::toResponseDTO);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        // Full replace - PUT semantics
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setDepartment(dto.getDepartment());
        existing.setSalary(dto.getSalary());
        existing.setDateOfJoining(dto.getDateOfJoining());

        return employeeMapper.toResponseDTO(employeeRepository.save(existing));
    }

    @Override
    public EmployeeResponseDTO patchEmployee(Long id, EmployeePatchDTO dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        // Partial update - only apply non-null fields (PATCH semantics)
        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getDepartment() != null) existing.setDepartment(dto.getDepartment());
        if (dto.getSalary() != null) existing.setSalary(dto.getSalary());
        if (dto.getActive() != null) existing.setActive(dto.getActive());

        return employeeMapper.toResponseDTO(employeeRepository.save(existing));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }
}
```

---

## 1️⃣1️⃣ Controller (All HTTP Methods)

```java
// controller/EmployeeController.java
package com.example.employeeservice.controller;

import com.example.employeeservice.dto.*;
import com.example.employeeservice.enums.Department;
import com.example.employeeservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    // ---------- POST : Create ----------
    @Operation(summary = "Create a new employee")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        EmployeeResponseDTO created = employeeService.createEmployee(dto);
        URI location = URI.create("/api/v1/employees/" + created.getId());
        return ResponseEntity.created(location).body(created);   // 201 Created + Location header
    }

    // ---------- GET : Get by ID ----------
    @Operation(summary = "Get employee by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));   // 200 OK
    }

    // ---------- GET : Get all (paginated + sortable) ----------
    @Operation(summary = "Get all employees with pagination and sorting")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    // ---------- GET : Get by department (query filter) ----------
    @Operation(summary = "Get employees filtered by department")
    @GetMapping("/department/{department}")
    public ResponseEntity<Page<EmployeeResponseDTO>> getByDepartment(
            @PathVariable Department department,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department, pageable));
    }

    // ---------- PUT : Full update ----------
    @Operation(summary = "Full update of an employee (replaces entire resource)")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));   // 200 OK
    }

    // ---------- PATCH : Partial update ----------
    @Operation(summary = "Partial update of an employee (only provided fields)")
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> patchEmployee(
            @PathVariable Long id, @RequestBody EmployeePatchDTO dto) {
        return ResponseEntity.ok(employeeService.patchEmployee(id, dto));    // 200 OK
    }

    // ---------- DELETE ----------
    @Operation(summary = "Delete an employee")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();   // 204 No Content
    }
}
```

---

## 1️⃣2️⃣ OpenAPI Config

```java
// config/OpenApiConfig.java
package com.example.employeeservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI employeeServiceOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Employee Service API")
                .version("1.0")
                .description("CRUD APIs for managing employees"));
    }
}
```

---

## 1️⃣3️⃣ Sample Unit Test (Mockito + MockMvc)

```java
// test/service/EmployeeServiceImplTest.java
package com.example.employeeservice.service;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.enums.Department;
import com.example.employeeservice.exception.EmployeeNotFoundException;
import com.example.employeeservice.mapper.EmployeeMapper;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeMapper employeeMapper;
    @InjectMocks private EmployeeServiceImpl employeeService;

    @Test
    void getEmployeeById_shouldThrow_whenNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(1L));

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void createEmployee_shouldThrow_whenDuplicateEmail() {
        EmployeeRequestDTO dto = EmployeeRequestDTO.builder()
                .email("test@example.com")
                .firstName("Krishna")
                .lastName("K")
                .department(Department.ENGINEERING)
                .salary(50000.0)
                .dateOfJoining(LocalDate.now())
                .build();

        when(employeeRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(true);

        assertThrows(com.example.employeeservice.exception.DuplicateEmployeeException.class,
                () -> employeeService.createEmployee(dto));
    }
}
```

---

## 🧪 Quick Test Commands (once running)

```bash
# Create
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Krishna","lastName":"K","email":"krishna@example.com","department":"ENGINEERING","salary":250000,"dateOfJoining":"2015-06-01"}'

# Get all (paginated)
curl "http://localhost:8080/api/v1/employees?page=0&size=5&sort=salary,desc"

# Get by ID
curl http://localhost:8080/api/v1/employees/1

# Full update
curl -X PUT http://localhost:8080/api/v1/employees/1 -H "Content-Type: application/json" -d '{...}'

# Partial update
curl -X PATCH http://localhost:8080/api/v1/employees/1 -H "Content-Type: application/json" -d '{"salary":300000}'

# Delete
curl -X DELETE http://localhost:8080/api/v1/employees/1
```

Swagger UI: `http://localhost:8080/swagger-ui.html`
H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:employeedb`)

---

## 🎯 Interview-Oriented Insights (12 YOE Level)

| Question | What They're Really Testing |
|---|---|
| Why PUT is idempotent but POST isn't? | Understanding of REST semantics — calling PUT twice with the same body gives the same result; POST creates a new resource each time |
| Difference between PUT and PATCH? | Full replace vs partial update — your code demonstrates both distinctly |
| Why return `201 + Location header` on POST? | REST best practice (RFC 7231) — client should be told where the new resource lives |
| Why DTO layer instead of exposing entity? | Decoupling, security (no over-posting), API versioning independence |
| How do you handle N+1 or pagination at scale? | Mention `Pageable`, `@EntityGraph`, projections, and possibly cursor-based pagination for very large datasets |
| `@Transactional(readOnly = true)` — why bother? | Hibernate skips dirty checking, potential DB-level read optimizations |
| How would you version this API? | URI versioning (`/api/v1/`) vs header versioning — discuss trade-offs |
| MapStruct vs manual mapper vs ModelMapper? | MapStruct = compile-time, fastest, type-safe; ModelMapper = reflection-based, slower; Manual = full control, more boilerplate — this project uses manual for transparency |

---

## 📚 Learning Resources

- **Official Spring Docs:** [spring.io/guides/gs/rest-service](https://spring.io/guides/gs/rest-service)
- **REST API Design Best Practices:** Microsoft REST API Guidelines (search on GitHub)
- **Baeldung** — search "Spring Boot PATCH vs PUT", "Spring Boot Exception Handling"
- **Book:** *Spring in Action* (Craig Walls) — Ch. on REST APIs

> ⚠️ I don't have live search in this conversation — verify links/versions are current if it matters for your interview prep.

---

Want me to extend this with **Spring Security (JWT)**, **Testcontainers integration tests**, or a **Dockerfile + docker-compose** setup — all common asks at the 12 YOE bar?