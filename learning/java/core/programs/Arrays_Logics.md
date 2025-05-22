### Merge Two Arrays
```java
Object[] streamConcat = Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray();

Object[] systemArraycopy = Stream.of(a, b).flatMap(Stream::of).forEach(n::add);

List<Object> n = new ArrayList<>();
Collections.addAll(n, a);
Collections.addAll(n, b);
```
---

### ParallelSort Array Range
```java
//Parallel Sort method for sorting int array
Arrays.parallelSort(numbers);

//Parallel Sort method for sorting int array with in Range
Arrays.parallelSort(anotherNumbers, 1, 5);
```
---

### Print Duplicate Characters from an Array
```java
        
Set<Character> set = new HashSet<>();
Set<Character> duplicateChars =
    str.chars()
       .mapToObj(c -> (char) c)
     //.peek(p->System.out.println("Character: "+p+" ---> SET"+set))
       .filter(f -> !set.add(f))
       .collect(Collectors.toSet());
```
Output: [a, r, t, e, h, i, k, n]

---
### Print Unique Characters from an Array
```java
        input.chars().mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(f->f.getValue()==1L)
                .collect(Collectors.toSet());
```
Output :: Unique    Elements ::  --> [s, c, d, v, l]

---

### Sorting Characters Array Using Comparator

- Convert String to **Character Map** with counts 
```java
LinkedHashMap<Character, Long> charLinkedHashMap = 
        input.chars()
             .mapToObj(c -> (char) c)
             .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
```

- Characters **sortByValue**  [**Map.Entry.comparingByValue()**]
```java
LinkedHashMap<Character, Long> sortByValue = 
        charLinkedHashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
```

- Characters **sortByKey**  [**Map.Entry.comparingByKey()**]
```java
LinkedHashMap<Character, Long> sortByKey =
        charLinkedHashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (old, latest) -> old, LinkedHashMap::new));

```
- Characters **sortByKeyValue**  [**Comparator.comparing.thenComparing()**]
```java
// Create a Comparator         
Comparator<Map.Entry<Character, Long>> comparator = 
        Comparator.comparing(Map.Entry<Character, Long>::getKey)
                .thenComparing(Map.Entry<Character, Long>::getValue);

LinkedHashMap<Character, Long> sortByKeyValue = 
        charLinkedHashMap.entrySet().stream()
                .sorted(comparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
```
