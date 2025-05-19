A **`WeakHashMap`** is a special kind of **Map** in Java, where the keys are stored as **weak references**. This means that if there are no strong references to a key object, it becomes eligible for garbage collection. In other words, the entries in a `WeakHashMap` can be automatically removed by the garbage collector when the key is no longer in use.

### **How Garbage Collection Works with `WeakHashMap`**

1. **Weak References**:
    - In a `WeakHashMap`, the keys are stored as **weak references** (i.e., `java.lang.ref.WeakReference`).
    - A weak reference allows an object to be garbage collected as soon as it is no longer strongly referenced anywhere in the application. This contrasts with normal references (strong references), which prevent an object from being garbage collected as long as the reference exists.

2. **Garbage Collection Mechanism**:
    - When an object is used as a key in a `WeakHashMap`, the map holds a weak reference to that object.
    - If there are no other **strong references** to the key, the garbage collector will reclaim the memory used by that key object, and the corresponding entry in the `WeakHashMap` will be removed.
    - If the key is still being used elsewhere in the application (i.e., there are strong references to it), the entry in the `WeakHashMap` will remain intact.

3. **Behavior on GC**:
    - The **`WeakHashMap`** will automatically clean up entries when their keys are garbage collected. This means that **keys** without strong references will be removed from the map during a garbage collection cycle.

4. **Use Case**:
    - `WeakHashMap` is particularly useful when you want to store objects in a map but you don't want the presence of those objects in the map to prevent them from being garbage collected.
    - This can be handy when you want to implement caches or store temporary data where objects should be allowed to be reclaimed once they are no longer used.

### **Internal Working**

- The **`WeakHashMap`** internally uses **`WeakReference`** for its keys. A `WeakReference` is a special type of reference that does not prevent an object from being garbage collected.
- When a key in the map becomes eligible for garbage collection (i.e., no strong references to the key exist), the entry (key-value pair) is automatically removed from the map.
- The garbage collector runs periodically, and when it identifies that a weakly referenced key is no longer in use, it clears that entry from the map.

### **Example of `WeakHashMap` with Garbage Collection:**

```java
import java.util.WeakHashMap;

public class WeakHashMapExample {
    public static void main(String[] args) {
        // Create a WeakHashMap
        WeakHashMap<String, String> weakMap = new WeakHashMap<>();

        // Create a strong reference to a key
        String key1 = new String("key1");
        
        // Insert key-value pair into WeakHashMap
        weakMap.put(key1, "value1");

        System.out.println("Before GC: " + weakMap); // Output: {key1=value1}

        // Remove strong reference to the key
        key1 = null;

        // Suggest garbage collection
        System.gc();

        // Wait for GC to run and remove the key from the WeakHashMap
        try {
            Thread.sleep(1000); // Sleep for a while to let GC run
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("After GC: " + weakMap); // Output: {}
    }
}
```

### **Output**:
```
Before GC: {key1=value1}
After GC: {}
```

### **Explanation**:
1. We create a `WeakHashMap` and insert a key-value pair.
2. We then nullify the strong reference to the key (`key1 = null`), which means it has no strong references anymore.
3. When `System.gc()` is called, the garbage collector runs, and since `key1` is no longer strongly referenced, it becomes eligible for garbage collection.
4. The `WeakHashMap` removes the entry with the key `key1`, as the key has been garbage collected.

### **Summary of Key Points**:

1. **Weak References**: The keys in `WeakHashMap` are stored as weak references. This means that the map does not prevent the garbage collection of the keys when there are no strong references to them.
2. **Automatic Cleanup**: Entries in the map are automatically removed during garbage collection when their keys are no longer reachable via strong references.
3. **Use Case**: `WeakHashMap` is useful for caching scenarios or when you want the map entries to be automatically cleared once the keys are no longer in use.
4. **Not for Permanent Storage**: `WeakHashMap` is not ideal for storing data you want to keep around permanently; it’s used for temporary mappings where the existence of the key in the map should not prevent it from being garbage collected.