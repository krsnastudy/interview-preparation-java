### Print Non-Repeated First Char

```java
    str.chars()
    .mapToObj(i -> Character.valueOf((char) i))
    .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
    .entrySet().stream()
    .filter(entry -> (entry.getValue() == 1L))
    .findFirst()
    .map(k -> k.getKey())
    .get();
```

---

### Swap Two Strings

```java
public static void main(String[] args) {
    String str1 = "Radha", str2 = "Krishna";
    System.out.println("B4 Swap: " + str1 + " " + str2);

    // Step 1: Concatenate both strings
    str1 = str1 + str2;  // Now str1 = "HelloWorld"

    // Step 2: Extract str2 from the combined string
    str2 = str1.substring(0, str1.length() - str2.length());  // str2 = "Hello"

    // Step 3: Extract str1 from the combined string
    str1 = str1.substring(str2.length());  // str1 = "World"
    
    System.out.println("A4 Swap: " + str1 + " " + str2);
}
```
---

### Reverse the String

```java
// Reverse String
String str = "RadhaKrishna";
String reduce = Stream.of(str.split("")).reduce("",(a,b)->b+a);

// Reverse the Words in a scentence
String str = "This is an Apple";
String revStr = 
        Arrays.asList(str.split(" "))
              .stream()
              .map(s->new StringBuffer(s).reverse())
              .collect(Collectors.joining(" "));

String revWords = 
        Arrays.stream(str.split("\\s+"))
              .reduce((a, b) -> b + " " + a)
              .orElse(" ");
```

---

