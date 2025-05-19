To configure a **ThreadPool in a server environment (like a Spring Boot application)**, you typically configure a **ThreadPoolTaskExecutor** for handling concurrent tasks efficiently (like @Async tasks, scheduled tasks, or custom business logic processing).

---

## ✅ 1. **Basic ThreadPool Configuration in Spring Boot**

You can configure a custom `ThreadPoolTaskExecutor` as a Spring bean.

### 🔧 Java Config:
```java
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean(name = "customExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);         // Min threads to keep
        executor.setMaxPoolSize(50);          // Max threads allowed
        executor.setQueueCapacity(100);       // Task queue before max threads
        executor.setThreadNamePrefix("MyApp-Executor-");
        executor.initialize();
        return executor;
    }
}
```

### 🧠 Key Parameters:
| Parameter         | Purpose |
|------------------|---------|
| `corePoolSize`   | Minimum number of threads always kept alive |
| `maxPoolSize`    | Max number of threads |
| `queueCapacity`  | Tasks waiting in queue before spawning more threads |
| `threadNamePrefix` | Useful for debugging |

---

## ✅ 2. **Using It with `@Async`**

```java
@Async("customExecutor")
public void processInBackground() {
    // Some background task
}
```

---

## ✅ 3. **Configure via `application.properties` (Optional)**

Spring Boot doesn’t auto-create thread pool via properties alone, but you can externalize values:

```properties
executor.corePoolSize=10
executor.maxPoolSize=50
executor.queueCapacity=100
```

Then read these into the config class via `@Value`.

---

## ✅ 4. **For Embedded Tomcat Thread Pool (Web Request Handling)**

To configure the thread pool that handles **HTTP requests** in embedded Tomcat:

```yaml
server:
  tomcat:
    threads:
      min-spare: 10
      max: 200
```

Or programmatically:
```java
@Bean
public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
    return factory -> factory.addConnectorCustomizers(connector -> {
        ProtocolHandler handler = connector.getProtocolHandler();
        if (handler instanceof AbstractProtocol) {
            AbstractProtocol<?> protocol = (AbstractProtocol<?>) handler;
            protocol.setMaxThreads(200);
            protocol.setMinSpareThreads(10);
        }
    });
}
```

---

## ⚙️ TL;DR

| Use Case                  | ThreadPool Config |
|---------------------------|------------------|
| `@Async` task execution   | `ThreadPoolTaskExecutor` |
| Embedded Tomcat HTTP threads | `server.tomcat.threads.max` |
| Scheduled tasks           | `TaskScheduler` with pool config |

---

Would you like me to show how to profile or monitor your thread pool usage at runtime?