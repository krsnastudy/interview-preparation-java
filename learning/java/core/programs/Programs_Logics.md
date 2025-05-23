### Generate Fibonacci Series
- Code in **Java Stream API**
```java
int series = 10;
String fibonacci = Stream.iterate(new int[]{0, 1}, n -> new int[]{n[1], n[0] + n[1]})
        .limit(series)
        .map(n->n[0])
        .map(String::valueOf)
        .collect(Collectors.joining(", "));
```
---

### Check String Pallindrome
- Code in **Java 8**
```java
boolean isPallindrome = IntStream.range(0, checkString.length() / 2)
//.peek(j->System.out.println(checkString.charAt(j)))
        .allMatch(i -> checkString.charAt(i) == checkString.charAt(checkString.length() - i - 1));
//      .noneMatch(i -> checkString.charAt(i) != checkString.charAt(checkString.length() - i - 1)); //Another Way
```
---

### Print Prime Numbers

```java
// Print Logic
Stream.iterate(0, n -> n + 1)
        .limit(50)
        .filter(PrimeNumber::isPrimeNumber_Java8)
//      .filter(PrimeNumber::isPrimeNumber_Java5)
        .peek(x -> System.out.format("%s ", x))
        .count();

// Java 8
public static boolean isPrimeNumber_Java8(int number) {
    boolean isPrime = false;
    
    if (number < 2 && number >= 0)
        return isPrime;
    
//	return !IntStream.rangeClosed(2, number / 2).anyMatch(i -> number % i == 0);
    return IntStream.rangeClosed(2, number / 2).noneMatch(i -> number % i == 0);
}

// Java 5
private static boolean isPrimeNumber_Java5(int number) {

    if (number <= 1) return false;    //  1 is not prime and also not composite

    for (int i = 2; i * i <= number; i++) {
        if (number % i == 0) {
            return false;
        }
    }
    return true;
}
```
---

## Sort By SecondName

```java
public static void main(String[] args) {
    String[] sArray = {"Venkata, Saatvik", "Ratan, Tata", "Radha, Krishna", "Kalam, Abdul", "Mishra, Gokul", "Mohit, Bansal", "Kapil, Baarat"};
    System.out.println(Arrays.toString(sArray));

    String collect = Arrays.asList(sArray).stream()
            .sorted((x, y) -> {
                String[] fs = x.split(",");
                String[] ss = y.split(",");
                return fs[1].trim().compareTo(ss[1].trim());
            })
            .map(m -> "[" + m + "]")
            .collect(Collectors.joining(", "));
    System.out.println(collect);
}
```
---
## Print Odd Even Numbers using Collectors.partitioningBy() API

```java
public static void main(String[] args) {

    List<Integer> list =
        IntStream.rangeClosed(1, 20)
                 .mapToObj(i->i)
                 .collect(Collectors.toList());

//Collectors.partitioningBy
    Map<Boolean, List<Integer>> map = 
            list.stream()
                .collect(Collectors.partitioningBy(i -> i % 2 == 0));

//Printing
    map.entrySet().stream().forEach(System.out::println);
}

/** OUTPUT **/
 false=[1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
 true=[2, 4, 6, 8, 10, 12, 14, 16, 18, 20]    

/** Sum Of Odd Number Squares, Cubes **/
Integer reduce = 
        IntStream.rangeClosed(1, 1500)
                 .boxed()
                 .filter(i -> i % 2 != 0)
                 .map(x -> x * x)
                 .reduce(0, Integer::sum);

Integer reduceSquare = IntStream.rangeClosed(1, 1500).boxed().filter(i -> i % 2 != 0).map(square).reduce(0, Integer::sum);
Integer reduceCube = IntStream.rangeClosed(1, 1500).boxed().filter(i -> i % 2 != 0).map(cube).reduce(0, Integer::sum);
```
---

## Group Anagrams

```java
public static Map<String, List<String>> groupAnagrams(List<String> words) {
//	return words.stream().collect(Collectors.groupingBy(GroupAnagram::sortString));
    return words.stream().collect(Collectors.groupingBy(w -> sortString(w)));
}

public static String sortString(String str) {
    char[] charArr = str.toCharArray();
    Arrays.sort(charArr);
    return String.valueOf(charArr);
}

/*** String Anagram if two strings were given ***/
public static boolean isAnagramSort(String string1, String string2) {
    if (string1.length() != string2.length()) {
        return false;
    }
    char[] a1 = string1.toCharArray();
    char[] a2 = string2.toCharArray();
    Arrays.sort(a1);
    Arrays.sort(a2);

//	return String.valueOf(a1).equals(String.valueOf(a2));
    return Arrays.equals(a1, a2);
}
```
---

## Factorial Program

```java
// Java8
public static long factorial(int n) {
    return (n < 2) ? 1 : IntStream.rangeClosed(2, n).reduce(1, (a, b) -> a * b);
}

//Pre-Java 8
public static long factorial(int n) {
    long result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}
```
