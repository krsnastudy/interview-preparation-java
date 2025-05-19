```java
String str = "hello";
str = str.replaceAll("l", "p");
```

### 🔍 What happens internally:

* **`str.replaceAll(...)`** creates a **new String object**.
* It **does not** modify the original `str` because `String` is **immutable**.
* The **new object is created in the heap**, **not** in the **String Constant Pool (SCP)** — unless you explicitly intern it.

### 📌 Key Notes:

| Aspect                     | Explanation                                                          |
| -------------------------- | -------------------------------------------------------------------- |
| `String` immutability      | Any modification returns a new object                                |
| `replaceAll()` behavior    | Returns a new `String` with replacements, original remains unchanged |
| SCP (String Constant Pool) | Only **string literals** or **`intern()`-ed** strings go to SCP      |

### 🔧 Example with SCP check:

```java
String s1 = "hello";
String s2 = s1.replaceAll("l", "p");  // creates "heppo" in heap

System.out.println(s1 == s2);         // false
System.out.println(s2 == "heppo");    // false

String s3 = s2.intern();
System.out.println(s3 == "heppo");    // true (now from SCP)
```

---

### ✅ Interview Short Answer:

> `str.replaceAll()` creates a new String object in the **heap**, not in the **String Constant Pool (SCP)**, unless you explicitly call `.intern()` on it.
