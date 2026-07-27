## Max Repeated Key from a Map
```java
public static void main(String[] args) {
    String[] input = {"Pen", "Eraser", "Note Book", "Pen", "Pencil", "Pen", "Note Book", "Pencil"};

    String maxRepeatedKey = Arrays.stream(input)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
//          .peek(System.out::println)
            .max(Comparator.comparing(Map.Entry::getValue))
            .get()
            .getKey();
    System.out.println("maxRepeatedKey: " + maxRepeatedKey);
}
    
/* **What if two keys have the same frequency?**  */
    String result = Arrays.stream(input)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .max(Comparator.comparing(Map.Entry<String, Long>::getValue)
                           .thenComparing(Map.Entry::getKey, Comparator.reverseOrder()))
            .get()
            .getKey();
    System.out.println(result);
```
```text
Senior **Interview Tip** : This is a favorite follow-up question for experienced Java developers.   

The current implementation is not deterministic when multiple elements have the same frequency because     
Collectors.groupingBy() creates a HashMap, whose iteration order is unspecified.  
To make the result deterministic, I'd add a secondary comparator (for example, by key) or use a LinkedHashMap     
if I need to preserve insertion order.  
        .max(Comparator.comparing(Map.Entry<String, Long>::getValue)
        .thenComparing(Map.Entry::getKey))
```
---
## Sort Elements in a Map by Key, Value
```java
List<Integer> numbers = Arrays.asList(1,18,11,2,3,4,3,2,1,4,5,6,4,2,3,7,6,5,4,8,7,9,8);

/* Print only Non-Repeated Numbers in orders */

// Sort By Key
String collect = numbers.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet().stream()
        .filter(f->f.getValue()==1L)
        .map(Map.Entry::getKey)
        .map(String::valueOf)
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.joining(", "));

        System.out.println("Sort By Map.Entry::getKey : "+collect);
```
Output: Sort By Map.Entry::getKey : 9, 18, 11

---

## Sorting a Map By Key and Values

```java
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("Lohith", "Jammu");
        map.put("Mukesh", "Assam");
        map.put("Vinay", "Delhi");
        map.put("Rakesh", "Andhra");
        map.put("Arun Gowda", "Karnataka");

        System.out.println(map);

        /* sortByValue */
        LinkedHashMap<String, String>
                sortByValue = map.entrySet().stream()
//                .sorted(Comparator.comparing(Map.Entry::getValue))
                .sorted(Map.Entry.comparingByValue())
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) //Reverse Order Sorting
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

        System.out.println("sortByValue: " + sortByValue);

        /* sortByKey */
        Map<String, String> 
                sortByKey = map.entrySet().stream()
//                .sorted(Comparator.comparing(Map.Entry::getKey))
//                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())) //Reverse Order Sorting               
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

        System.out.println("  sortByKey: " + sortByKey);

    }
```

**Sort Elements in a Map by Key, Value**
***sortByValue***  
.sorted(Comparator.comparing(Map.Entry::getValue))  
.sorted(Map.Entry.comparingByValue())
.sorted(Map.Entry.<String, Long>comparingByValue())

**Reverse Order Sorting**  
.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))  
.sorted(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry::getKey, Comparator.reverseOrder())) // thenComparing  

***sortByKey***   
.sorted(Comparator.comparing(Map.Entry::getKey))  
.sorted(Map.Entry.comparingByKey())  
.sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())) //**Reverse Order Sorting**  

**Common aggregation step (shared by all 4 options)**  
**1)** .sorted(Comparator.<Map.Entry<String, Long>, String>comparing(Map.Entry::getKey).thenComparing(Map.Entry::getValue))  
**2)** .sorted(Comparator.<Map.Entry<String, Long>, String>comparing(e -> e.getKey()).thenComparing(e -> e.getValue()))  
**3)** Comparator<Map.Entry<String, Long>> byKey = Comparator.comparing(Map.Entry::getKey);  
.sorted(byKey.thenComparing(Map.Entry::getValue))  
**4)** .sorted(Map.Entry.<String, Long>comparingByKey().thenComparing(Map.Entry.comparingByValue()))

```text
**Output**::  
**GivenValue**: {Lohith=Jammu, Rakesh=Andhra, Arun Gowda=Karnataka, Vinay=Delhi, Mukesh=Assam}\
**sortByValue**: {Rakesh=Andhra, Mukesh=Assam, Vinay=Delhi, Lohith=Jammu, Arun Gowda=Karnataka}\
**sortByKey**: {Arun Gowda=Karnataka, Lohith=Jammu, Mukesh=Assam, Rakesh=Andhra, Vinay=Delhi}
```
---

```java
public class ComparatorFixDemo {

    public static void main(String[] args) {

        String[] sentences = {
                "Java is great and Java is powerful",
                "Spring Boot makes Java development easy"
        };

        // ---------- Common aggregation step (shared by all 4 options) ----------
        Map<String, Long> wordCounts = Arrays.stream(sentences)
                .flatMap(s -> Arrays.stream(s.toLowerCase().split(" ")))
                .map(word -> word.replaceAll("[^a-z0-9]", ""))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println("=== Option A: Explicit type witness ===");
        Map<String, Long> resultA = wordCounts.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>, String>comparing(Map.Entry::getKey)
                        .thenComparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
        resultA.forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println("\n=== Option B: Lambdas instead of method references ===");
        Map<String, Long> resultB = wordCounts.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>, String>comparing(e -> e.getKey())
                        .thenComparing(e -> e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
        resultB.forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println("\n=== Option C: Typed variable anchors inference ===");
        Comparator<Map.Entry<String, Long>> byKey = Comparator.comparing(Map.Entry::getKey);
        Map<String, Long> resultC = wordCounts.entrySet().stream()
                .sorted(byKey.thenComparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
        resultC.forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println("\n=== Option D: Map.Entry's built-in comparators (idiomatic) ===");
        Map<String, Long> resultD = wordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByKey().thenComparing(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
        resultD.forEach((k, v) -> System.out.println(k + " : " + v));
    }
}
```
---

