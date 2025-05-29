## 🔍 What is a Regular Expression?

A **Regular Expression (Regex)** is a pattern used to match character combinations in strings. Java provides the `java.util.regex` package to work with regex.

---

## ✅ Core Components of Regex (with Examples)

### 1. **Literals**

* Matches exactly what you type.

```regex
abc
```

✅ Matches: "abc"
❌ Doesn't match: "ab", "abcd"

---

### 2. **Metacharacters**

Special characters with specific meanings:

| Character | Description                 | Example   | Matches             |       |              |
| --------- | --------------------------- | --------- | ------------------- | ----- | ------------ |
| `.`       | Any character (except `\n`) | `a.b`     | "acb", "a1b"        |       |              |
| `^`       | Start of string             | `^Hello`  | "Hello Java"        |       |              |
| `$`       | End of string               | `end$`    | "This is the end"   |       |              |
| `*`       | 0 or more repetitions       | `a*`      | "", "a", "aaaa"     |       |              |
| `+`       | 1 or more repetitions       | `a+`      | "a", "aa", "aaa"    |       |              |
| `?`       | 0 or 1 repetition           | `colou?r` | "color", "colour"   |       |              |
| `[]`      | Character class             | `[abc]`   | "a", "b", "c"       |       |              |
| `[^]`     | Negated character class     | `[^abc]`  | "d", "e"            |       |              |
| `{n}`     | Exactly n times             | `a{3}`    | "aaa"               |       |              |
| `{n,}`    | n or more times             | `a{2,}`   | "aa", "aaaa"        |       |              |
| `{n,m}`   | Between n and m times       | `a{2,4}`  | "aa", "aaa", "aaaa" |       |              |
| `\d`      | Digit (`[0-9]`)             | `\d+`     | "123"               |       |              |
| `\D`      | Non-digit (`[^0-9]`)        | `\D+`     | "abc"               |       |              |
| `\w`      | Word char (`[a-zA-Z0-9_]`)  | `\w+`     | "abc123"            |       |              |
| `\W`      | Non-word char               | `\W+`     | "@\$%"              |       |              |
| `\s`      | Whitespace                  | `\s+`     | " ", "\t", "\n"     |       |              |
| `\S`      | Non-whitespace              | `\S+`     | "abc"               |       |              |
| \`        | \`                          | OR        | \`cat               | dog\` | "cat", "dog" |
| `()`      | Grouping                    | `(abc)+`  | "abc", "abcabc"     |       |              |

---

## 🧪 Java Code Examples

### 📌 Pattern & Matcher Example

```java
import java.util.regex.*;

public class RegexDemo {
    public static void main(String[] args) {
        String input = "abc123";
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(input);
        
        if (matcher.matches()) {
            System.out.println("Input is a valid word string");
        } else {
            System.out.println("Input is invalid");
        }
    }
}
```

### 📌 `String.matches()` Shortcut

```java
public class RegexSimple {
    public static void main(String[] args) {
        String email = "test@example.com";
        boolean valid = email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
        System.out.println("Is valid email? " + valid);
    }
}
```

---

## 💡 Real-Time Interview Use Cases

### 1. ✅ Validate Email

```regex
^[\\w.-]+@[\\w.-]+\\.\\w{2,}$
```

### 2. ✅ Validate Phone Number (10 Digits)

```regex
^\\d{10}$
```

### 3. ✅ Validate Username (Alphanumeric + Underscore)

```regex
^[a-zA-Z0-9_]{5,15}$
```

### 4. ✅ Validate Strong Password

```regex
^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$
```

---

## 🤔 Difference Between `matches()` vs `find()`

| Method      | Description                               |
| ----------- | ----------------------------------------- |
| `matches()` | Matches the **entire** string             |
| `find()`    | Finds **partial matches** within a string |

```java
Matcher matcher = pattern.matcher("abc123");

matcher.matches(); // whole string match
matcher.find();    // partial match anywhere
```

---

## 📌 TL;DR for Interviews

| Topic               | Notes                                               |    |
| ------------------- | --------------------------------------------------- | -- |
| Regex Use           | Pattern matching in strings                         |    |
| Java Classes        | `Pattern`, `Matcher`, `String.matches()`            |    |
| Common Use Cases    | Validation (email, phone, password)                 |    |
| Core Metacharacters | `^`, `$`, `.`, `+`, `*`, `\d`, `\w`, `[]`, `()`, \` | \` |
| Lazy vs Greedy      | Greedy by default (e.g., `.*`); use `?` for lazy    |    |
| Escaping            | Double escape: `\\d`, `\\w`, etc. in Java strings   |    |

---
