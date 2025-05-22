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

