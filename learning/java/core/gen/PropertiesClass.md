Great question! The **`Properties` class** is a key component in Java for handling configuration data, often used in **Java applications** for **storing** and **retrieving** key-value pairs. Let's break it down clearly.

---

# 🚀 What is the `Properties` Class?

- **`Properties`** is a subclass of **`Hashtable`**.
- It is part of **`java.util`** package.
- It is **specifically designed** for handling **configuration data** in the form of key-value pairs (strings).
- Primarily used to **read and write properties files** (commonly `.properties` files), which are simple text files containing key-value pairs.

---

# 🧠 Key Features of `Properties` Class

| Feature | Explanation |
|:---|:---|
| Inherits from | **`Hashtable`**, so it behaves like a map (supports key-value pairs). |
| Data Types | Both **key** and **value** in `Properties` are always **Strings**. |
| File Handling | `Properties` can **load** and **save** properties from/to **.properties** files using methods like `load()` and `store()`. |
| Special Methods | Provides special methods like `getProperty()` and `setProperty()` which are convenient for reading and writing configuration properties. |

---

# 🔧 Common Operations with `Properties` Class

### 1. **Create a Properties object**

```java
Properties properties = new Properties();
```

---

### 2. **Add Key-Value Pairs**

```java
properties.setProperty("username", "admin");
properties.setProperty("password", "admin123");
```

Here, `"username"` and `"password"` are keys, and `"admin"` and `"admin123"` are their corresponding values.

---

### 3. **Get Property Value**

```java
String username = properties.getProperty("username");
System.out.println(username); // prints "admin"
```

- `getProperty()` is used to fetch the value of a given key.

---

### 4. **Load Properties from a File**

Properties files often look like this:

```
# Sample properties file
username=admin
password=admin123
```

To load this into a `Properties` object:

```java
Properties properties = new Properties();
try (FileInputStream input = new FileInputStream("config.properties")) {
    properties.load(input); // Load properties from file
}
```

---

### 5. **Save Properties to a File**

You can **save the properties** to a file with comments as follows:

```java
try (FileOutputStream output = new FileOutputStream("config.properties")) {
    properties.store(output, "Config File");
}
```

The above code will save the properties to a file with a comment `"Config File"`.

---

# 📋 Example Code

```java
import java.io.*;
import java.util.Properties;

public class PropertiesExample {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        
        // Set properties
        properties.setProperty("username", "admin");
        properties.setProperty("password", "admin123");

        // Save properties to a file
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            properties.store(output, "Config File");
        }

        // Load properties from a file
        Properties loadedProperties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            loadedProperties.load(input);
        }

        // Print properties
        System.out.println("Username: " + loadedProperties.getProperty("username"));
        System.out.println("Password: " + loadedProperties.getProperty("password"));
    }
}
```

---

# ⚡ Key Points for Interviews:

1. **Storage**: `Properties` is used to store simple configuration data in the form of key-value pairs.
2. **File Handling**: `Properties` can load and save to and from `.properties` files.
3. **Inheritance**: Since it extends `Hashtable`, `Properties` inherits its basic functionality but is specialized for working with strings (both keys and values).
4. **Common Usage**: Frequently used for reading configuration files, like database configurations, application settings, etc.

---

# 🧩 TL;DR:

The `Properties` class is a specialized **Map** implementation that is used for storing **key-value pairs**, typically in the form of **String** keys and values. It allows you to **load** and **store** data from **properties files** (`.properties`), and it provides helpful methods like `getProperty()` and `setProperty()`.

---
