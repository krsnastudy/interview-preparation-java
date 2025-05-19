Here's the **clear and crisp** answer:

---

## ✅ Does Java use **Call by Value** or **Call by Reference**?

👉 **Java is always Call by Value.**  
**But** — you need to understand **what is passed** by value.

---

## ✅ Detailed Explanation:

In Java:

| Scenario                        | What Happens                             |
|----------------------------------|-----------------------------------------|
| Passing a **primitive type** (int, float) | The **actual value** is copied and passed. Changes inside method **don't affect** original. |
| Passing an **object** (like Employee) | The **reference (address)** to the object is copied and passed **by value**. Changes to object's fields **do affect** the original object, but reassigning the reference **doesn't** affect the original object. |

---

## ✅ Example 1: Primitive Type (int)

```java
void changeValue(int x) {
    x = 10;
}

int a = 5;
changeValue(a);
System.out.println(a);  // Output: 5
```
- `a` remains **5** because primitives are passed **by value** (copy).

---

## ✅ Example 2: Object (Employee)

```java
class Employee {
    String name;
}

void changeEmployee(Employee emp) {
    emp.name = "Krishna";  // Modifying the object's field
}

Employee e = new Employee();
e.name = "John";
changeEmployee(e);
System.out.println(e.name);  // Output: Krishna
```
- `e.name` becomes **"Krishna"** because the **reference value (address)** is copied, and you modified the object it points to.

---

## ✅ Important: Reassigning Object Reference

```java
void reassignEmployee(Employee emp) {
    emp = new Employee();
    emp.name = "New Name";
}

Employee e = new Employee();
e.name = "John";
reassignEmployee(e);
System.out.println(e.name);  // Output: John
```
- The `e.name` is still **"John"** — because reassigning `emp` inside the method **only affects the local copy of the reference**.

---

# ✅ Conclusion:

| Type         | How it behaves         |
|--------------|-------------------------|
| Primitives   | Pass actual **value** (copy) |
| Objects      | Pass **reference value** (copy of address) |
| Reassigning  | Only affects local copy, original untouched |

👉 So **Java is strictly Call by Value**, **always** — even for objects!

---

# ✅ TL;DR:

> Java is **Call by Value** — always.
> - For primitives, you pass the value.
> - For objects, you pass the **value of the reference**, not the object itself.

---

Would you also want a real-world **interview-level tricky question** based on this? (many interviewers love asking a trap question around this!) 🚀

Here’s a **real-world interview tricky question** based on Java's **call by value** — and it often confuses even seniors:

---

# 🚀 Interview Question:
> You are given two `StringBuilder` objects. Write a method that tries to swap their contents.  
> Will the swap work? Why or why not?

---

## ✅ Code they might ask you to write:

```java
void swap(StringBuilder a, StringBuilder b) {
    StringBuilder temp = a;
    a = b;
    b = temp;
}

public static void main(String[] args) {
    StringBuilder sb1 = new StringBuilder("Hello");
    StringBuilder sb2 = new StringBuilder("World");

    new Test().swap(sb1, sb2);

    System.out.println(sb1); // ???
    System.out.println(sb2); // ???
}
```

---

# 🧠 What happens here?

- Inside `swap()`, **the values of the references** `a` and `b` are **swapped locally**.
- BUT — since Java passes **copy of reference**, **the original `sb1` and `sb2` in `main()` remain unchanged**!
- So output will still be:

```
Hello
World
```

➡️ **Swap won't work!**

---

# ✅ How you can modify contents instead:

If you want to swap their **internal contents**, you should **mutate the objects** instead of trying to swap references:

```java
void swapContents(StringBuilder a, StringBuilder b) {
    String temp = a.toString();
    a.setLength(0);
    a.append(b.toString());
    b.setLength(0);
    b.append(temp);
}
```

Now the output will be:

```
World
Hello
```

✅ Because you changed the internal state of the objects directly!

---

# 🔥 Key Interview Takeaway:

| Scenario         | What Happens |
|------------------|--------------|
| Swapping references | Fails |
| Modifying object contents | Works |

➡️ **Java is Call by Value — reference value is passed, not the actual object!**

---

# ✅ TL;DR:

> In Java, swapping two objects' references inside a method **does not work** because Java passes the **copy of the reference** by value, **not** the actual reference itself.

---

Would you also want me to give a **similar trick** with **ArrayLists** (a favorite for coding rounds)? 🚀  
(very short but super sharp!)