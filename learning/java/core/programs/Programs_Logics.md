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
