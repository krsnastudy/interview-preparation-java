Java 8 introduced a brand new **Date and Time API** under the package `java.time` to overcome the shortcomings of the older `java.util.Date`, `java.util.Calendar`, and `SimpleDateFormat`.

---

## ✅ Why a New API?

### Problems with the old date-time API:

* **Mutable** → `Date` and `Calendar` objects are mutable (not thread-safe).
* **Poor design** → Months start from 0, years offset from 1900, etc.
* **Formatting issues** → `SimpleDateFormat` is not thread-safe.
* **Difficult to understand** and error-prone in time zone manipulation.

---

## ✅ What Java 8 Introduced

### Core Packages:

* `java.time` → Main API for dates, times, durations.
* `java.time.format` → Formatting/parsing.
* `java.time.temporal` → Lower-level access to date/time fields.
* `java.time.zone` → Time zone support.

---

## ✅ Important Classes and Use-Cases

### 1. `LocalDate`

Represents a date (no time or zone).

```java
LocalDate today = LocalDate.now();
LocalDate date = LocalDate.of(2023, 5, 22);
```

### 2. `LocalTime`

Represents a time (no date or zone).

```java
LocalTime now = LocalTime.now();
LocalTime time = LocalTime.of(14, 30);
```

### 3. `LocalDateTime`

Date + Time (no zone).

```java
LocalDateTime dt = LocalDateTime.of(today, time);
```

### 4. `ZonedDateTime`

Date + Time + TimeZone

```java
ZonedDateTime zoned = ZonedDateTime.now(ZoneId.of("America/New_York"));
```

### 5. `Instant`

Machine-readable UTC timestamp.

```java
Instant instant = Instant.now();
```

### 6. `Duration` and `Period`

Used to represent time difference.

```java
// For time
Duration duration = Duration.between(LocalTime.NOON, LocalTime.now());

// For date
Period period = Period.between(LocalDate.of(2020, 1, 1), LocalDate.now());
```

### 7. `DateTimeFormatter`

Thread-safe formatter for parsing and formatting.

```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
String formatted = LocalDate.now().format(formatter);
```

---

## ✅ Backward Compatibility

Convert from old to new:

```java
Date date = new Date();
Instant instant = date.toInstant();
LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
```

Convert from new to old:

```java
Instant i = Instant.now();
Date oldDate = Date.from(i);
```

---

## ✅ TL;DR for Interviews:

| Old API                      | Java 8 Date/Time API                                 |
| ---------------------------- | ---------------------------------------------------- |
| `java.util.Date`, `Calendar` | `LocalDate`, `LocalTime`, `ZonedDateTime`, `Instant` |
| Mutable                      | Immutable and thread-safe                            |
| Time zones tricky            | Built-in timezone support                            |
| Not ISO compliant            | ISO-8601 compliant                                   |
| Formatting not thread-safe   | `DateTimeFormatter` is thread-safe                   |

---

## 🔗 Resources

* [Oracle Java 8 Date and Time](https://docs.oracle.com/javase/tutorial/datetime/index.html)
* [Baeldung Java 8 Date and Time](https://www.baeldung.com/java-8-date-time-intro)
* [Java 8 Date Time Cheat Sheet](https://www.baeldung.com/java-8-date-time-intro#cheatsheet)

---