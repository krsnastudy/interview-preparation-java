When **all tasks** in a `CompletableFuture` raise exceptions, the behavior depends on how the exceptions are handled within the `CompletableFuture` and how the exception handling mechanisms are defined in the code. Here's a breakdown of what happens:

### 1. **Completion Exceptionally**:
If all the tasks in a `CompletableFuture` (such as those in a chain of `thenApply()`, `thenAccept()`, `thenCompose()`, etc.) throw exceptions, the `CompletableFuture` will **complete exceptionally** and hold the last exception thrown.

Internally, `CompletableFuture` uses the `completeExceptionally(Throwable ex)` method to mark it as completed exceptionally.

### 2. **Handling with `exceptionally()`**:
If you have chained the tasks and used methods like `exceptionally()` to handle the exception, those exceptions will be passed to `exceptionally()`, and you can return a fallback value.

For example:
```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error in task 1");
})
.thenApplyAsync(result -> {
    // This will not be executed because the previous task threw an exception
    throw new RuntimeException("Error in task 2");
})
.exceptionally(ex -> {
    System.out.println("Handled exception: " + ex.getMessage());
    return 0;  // Return fallback value
});

future.thenAccept(result -> System.out.println("Result: " + result));
```

Output:
```
Handled exception: Error in task 2
Result: 0
```

In this case, `exceptionally()` handles the last exception and provides a fallback value (0).

### 3. **Using `handle()`**:
The `handle()` method allows handling both normal results and exceptions, and it gives you the opportunity to handle each exception as they propagate.

Example:
```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error in task 1");
})
.thenApplyAsync(result -> {
    // This will not be executed
    throw new RuntimeException("Error in task 2");
})
.handle((result, ex) -> {
    if (ex != null) {
        System.out.println("Handled exception: " + ex.getMessage());
        return 0;  // Provide a fallback value
    }
    return result;
});

future.thenAccept(result -> System.out.println("Result: " + result));
```

Output:
```
Handled exception: Error in task 2
Result: 0
```

In this example, `handle()` provides a mechanism to deal with the exception and return a fallback value (0).

### 4. **Using `whenComplete()`**:
If no specific exception handler is provided in the chain, but you use `whenComplete()`, it will be called when the `CompletableFuture` is completed, either exceptionally or normally.

Example:
```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error in task 1");
})
.thenApplyAsync(result -> {
    throw new RuntimeException("Error in task 2");
})
.whenComplete((result, ex) -> {
    if (ex != null) {
        System.out.println("Exception occurred: " + ex.getMessage());
    } else {
        System.out.println("Result: " + result);
    }
});

future.join();  // Wait for completion and print the result (if any)
```

Output:
```
Exception occurred: Error in task 2
```

`whenComplete()` will let you observe both the result and the exception, and you can take actions like logging or cleanup.

### 5. **Uncaught Exception (Unhandled Exception)**:
If you don't handle the exception explicitly using `exceptionally()`, `handle()`, or `whenComplete()`, the `CompletableFuture` will complete exceptionally and store the exception. If you try to retrieve the result using `get()`, `join()`, or similar methods, it will throw an exception.

- `get()` throws an `ExecutionException` that wraps the original exception.
- `join()` throws a `CompletionException` that wraps the original exception.

Example:
```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error in task");
});

try {
    Integer result = future.get();  // This will throw ExecutionException
} catch (ExecutionException e) {
    System.out.println("Caught exception: " + e.getCause().getMessage());
}
```

Output:
```
Caught exception: Error in task
```

### Summary of Behavior:

1. **Completion Exceptionally**: If all tasks throw exceptions, the `CompletableFuture` will complete exceptionally and the last exception will be stored.
2. **Handling Exceptions**:
    - **`exceptionally()`**: Handles the exception and provides a fallback value.
    - **`handle()`**: Handles both the result and exception, providing an opportunity to handle exceptions and return a fallback value.
    - **`whenComplete()`**: Allows you to observe both the result and exception, but doesn't modify the result (useful for logging, cleanup, etc.).
3. **Uncaught Exception**: If no exception handler is provided, methods like `get()` and `join()` will throw a wrapped exception (`ExecutionException` or `CompletionException`).

### TL;DR:
When all tasks in a `CompletableFuture` raise exceptions, the `CompletableFuture` completes exceptionally, and the exception can be handled using methods like `exceptionally()`, `handle()`, or `whenComplete()`. If unhandled, methods like `get()` or `join()` will throw a wrapped exception.