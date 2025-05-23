Here's a **Java 8** example for writing to a file using `Files.write()`:

---

### ✅ Java 8 Code to Write to a File

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class WriteToFileJava8 {
    public static void main(String[] args) {
        String filePath = "C:/example/output.txt"; // Change path accordingly

        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");

        try {
            Files.write(Paths.get(filePath), 
                        lines, 
                        StandardOpenOption.CREATE, 
                        StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("File written successfully!");
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
```

---

### ✅ Explanation:

* `Files.write(Path, List<String>, OpenOption...)`: Writes list of strings to file.
* `StandardOpenOption.CREATE`: Creates file if it doesn’t exist.
* `StandardOpenOption.TRUNCATE_EXISTING`: Replaces file contents if file exists.

---

### 📝 TL;DR for Interviews

* Use `Files.write(...)` for easy file writing in Java 8.
* Combine with `List<String>` and `Stream` for dynamic content.
* For appending use `StandardOpenOption.APPEND`.
