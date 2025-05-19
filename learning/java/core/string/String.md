# 🚀 Your Code

```java
String str1 = "abc";
String str2 = new String("abc");
String str3 = str + "def";  // (assuming you meant str1 + "def")
```

(✅ Small correction: you likely meant `str1 + "def"` for `str3`.)

---

# 🧠 What Happens Internally?

| Line | Internal Behavior |
|:---|:---|
| `String str1 = "abc";` | `"abc"` is created in the **String Constant Pool** if not already present. `str1` points to that pool object. |
| `String str2 = new String("abc");` | A **new separate object** is created in the **Heap memory**, even though `"abc"` already exists in the pool. So now there are **two "abc" strings**: one in pool, one in heap. |
| `String str3 = str1 + "def";` | This is **String concatenation**. Java creates a **new String object** in the heap with value `"abcdef"`. (At runtime.) |

---

# 📜 Detailed Step-by-Step

### 1. `String str1 = "abc";`

- `"abc"` is placed into the **String Constant Pool** (if not already there).
- `str1` now references this **interned** `"abc"` object.

---

### 2. `String str2 = new String("abc");`

- `new String("abc")` creates:
    - Looks for `"abc"` in the pool (finds it ✅).
    - Creates a **new String object in heap memory** — a copy of `"abc"`.
- Now:
    - `str1` → pool `"abc"`
    - `str2` → heap `"abc"`

---

### 3. `String str3 = str1 + "def";`

- Concatenation happens at **runtime**.
- A **new StringBuilder** is created internally.
- Steps Java does:
  ```java
  new StringBuilder(str1)
       .append("def")
       .toString();  // creates a new String object "abcdef"
  ```
- So, a **new String "abcdef"** is created **in the heap** (not in the String Pool unless explicitly interned).

---

# 🏛 Memory Layout After Execution

| Memory Type | Objects |
|:---|:---|
| **String Constant Pool** | "abc" |
| **Heap** | new "abc" (from `new String("abc")`), "abcdef" (from concatenation) |

---

# ⚡ TL;DR

| Aspect | Behavior |
|:---|:---|
| `str1` | Points to "abc" in String Pool |
| `str2` | New "abc" object in Heap |
| `str3` | New "abcdef" object in Heap |
| String Concatenation | Happens at runtime using `StringBuilder` (unless compile-time constants) |

---

# 📚 Interview Important Tip:

If both strings are **compile-time constants**, Java **optimizes** concatenation **at compile time**.

Example:

```java
String s1 = "abc" + "def";  // compiler combines to "abcdef" directly!
```
✅ No `StringBuilder`, no runtime concatenation here.  
✅ "abcdef" directly placed in the constant pool.

---

# 🔥 Bonus Tip:

You can **force** a String to enter the **pool** using `.intern()`:

```java
String s = new String("abc").intern();
```
✅ Now `s` will point to the pooled `"abc"` instead of heap object.

---

# 📋 Final Memory Diagram

```
String Pool:
   "abc" (str1) 
   "abcdef" (only if compile-time)

Heap:
   new String("abc") (str2)
   "abcdef" (created at runtime for str3)
```

---
