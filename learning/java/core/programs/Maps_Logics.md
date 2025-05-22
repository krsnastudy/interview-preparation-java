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