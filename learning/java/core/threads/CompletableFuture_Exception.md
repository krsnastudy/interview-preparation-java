When an exception is raised in a `CompletableFuture`, it is handled differently based on how the exception is thrown and what actions have been defined to handle it. The exception can affect the result of the `CompletableFuture` and its behavior, but Java provides mechanisms to handle these exceptions.

### Here's what happens when an exception is raised:

1. **Exception Handling and `completeExceptionally()`**:
    - When an exception occurs during the execution of a `CompletableFuture`, it calls the `completeExceptionally(Throwable ex)` method.
    - This marks the `CompletableFuture` as **completed exceptionally**. It transitions to the **failed** state with the exception stored inside it.
    - Any subsequent calls to `get()`, `join()`, or similar methods will throw the stored exception.

2. **Exceptions in Asynchronous Tasks**:
    - If an exception is thrown in an asynchronous task (e.g., inside `thenApplyAsync()`, `thenAcceptAsync()`, etc.), the exception will be passed to the `CompletableFuture`'s internal state and can be handled by the consumers that are chained using methods like `exceptionally()`, `handle()`, or `whenComplete()`.

3. **Using `exceptionally()`**:
    - The `exceptionally()` method allows you to handle exceptions and provide a fallback value. If an exception occurs in the preceding stage of the `CompletableFuture`, this handler will be invoked with the exception, and you can return a default value to continue the chain.

   ```java
   CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
       if (true) throw new RuntimeException("Error occurred");
       return 42;
   });

   future.exceptionally(ex -> {
       System.out.println("Handled exception: " + ex.getMessage());
       return 0;  // Fallback value
   }).thenAccept(result -> System.out.println("Result: " + result));
   ```

4. **Using `handle()`**:
    - The `handle()` method is similar to `exceptionally()`, but it provides access to both the result and the exception (if any) of the preceding stage. You can handle both normal results and exceptions in a unified manner.

   ```java
   CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
       if (true) throw new RuntimeException("Error occurred");
       return 42;
   });

   future.handle((result, ex) -> {
       if (ex != null) {
           System.out.println("Handled exception: " + ex.getMessage());
           return 0;  // Fallback value
       }
       return result;
   }).thenAccept(result -> System.out.println("Result: " + result));
   ```

5. **Using `whenComplete()`**:
    - The `whenComplete()` method allows you to register a callback that will be executed after the `CompletableFuture` is completed, regardless of whether it is successful or failed. This is useful for logging or performing additional cleanup tasks.

   ```java
   CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
       if (true) throw new RuntimeException("Error occurred");
       return 42;
   });

   future.whenComplete((result, ex) -> {
       if (ex != null) {
           System.out.println("Exception occurred: " + ex.getMessage());
       } else {
           System.out.println("Result: " + result);
       }
   });
   ```

6. **Using `join()` vs `get()`**:
    - If you call `join()` on a `CompletableFuture` and it completes exceptionally, it will throw a `CompletionException`, which wraps the original exception.
    - If you call `get()`, it will throw an exception based on the underlying exception in the `CompletableFuture`. It can throw a `ExecutionException` with the original exception wrapped inside it.

   ```java
   try {
       Integer result = future.join();  // Will throw CompletionException if there's an exception
   } catch (CompletionException e) {
       System.out.println("Exception: " + e.getCause());
   }
   ```

### Key Points:

1. **CompletableFuture completes exceptionally** when an exception is thrown in any of the asynchronous tasks.
2. You can handle exceptions using methods like `exceptionally()`, `handle()`, and `whenComplete()`.
3. Exceptions in `CompletableFuture` can be retrieved and handled via `get()`, `join()`, and exception-handling methods.
4. If the exception is unhandled and you try to get the result, it will throw `CompletionException` or `ExecutionException`.

### TL;DR:
When an exception is thrown in a `CompletableFuture`, it transitions to a failed state and can be handled using `exceptionally()`, `handle()`, or `whenComplete()`. Unhandled exceptions will cause methods like `get()` or `join()` to throw `CompletionException` or `ExecutionException`.