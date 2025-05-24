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
