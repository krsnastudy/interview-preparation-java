Here’s a **simple Java program** to read a file from the **local file system** using `BufferedReader`. This works in **pre-Java 8** and higher.

---

### ✅ Java Code to Read File from Local Path

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadLocalFile {
    public static void main(String[] args) {
        String filePath = "C:/example/sample.txt"; // Change path accordingly

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("Reading file content:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
```

---

### ✅ Steps:

1. Provide **full path** to the file.
2. Use `BufferedReader` with `FileReader`.
3. Handle `IOException` properly.
4. Use **try-with-resources** for automatic closing.

---

### 📝 TL;DR for Interviews

* Use `BufferedReader` for line-by-line reading.
* Prefer `try-with-resources` (Java 7+).
* `FileReader` reads character files (not binary).

Here's the **Java 8 version** of reading a file using `Files.lines()` and Java Streams:

---

### ✅ Java 8 Code to Read File from Local Path

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReadLocalFileJava8 {
    public static void main(String[] args) {
        String filePath = "C:/example/sample.txt"; // Change path accordingly

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
```

---

### ✅ Key Java 8 Features Used:

* `Files.lines(Path)` — reads file as a `Stream<String>`, line-by-line.
* `try-with-resources` — automatically closes stream.
* `forEach(System.out::println)` — prints each line.

---

### 📝 TL;DR for Interviews

* Java 8 introduced `Files.lines(Path)` for efficient, lazy file reading.
* You get a `Stream<String>` for further processing/filtering.
* Better memory usage for large files vs traditional `BufferedReader`.

Let me know if you want to filter or transform the file content using Java 8 Stream APIs.
