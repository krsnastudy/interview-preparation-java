To exclude the embedded **Tomcat** server and include **WebLogic Server** in a Spring Boot application, you need to follow these steps:

### 1. **Exclude Tomcat Embedded Server**:
By default, Spring Boot uses an embedded **Tomcat** server. To exclude it, you need to make changes in your `pom.xml` (for Maven) or `build.gradle` (for Gradle).

#### For Maven (`pom.xml`):

In the `dependencies` section of your `pom.xml`, exclude the **spring-boot-starter-tomcat** dependency by adding the following code inside the **spring-boot-starter-web** dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

#### For Gradle (`build.gradle`):

In your `build.gradle` file, you need to exclude Tomcat from `spring-boot-starter-web`:

```groovy
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web') {
        exclude group: 'org.springframework.boot', module: 'spring-boot-starter-tomcat'
    }
}
```

### 2. **Include WebLogic Server as the Servlet Container**:
Spring Boot does not natively support WebLogic as an embedded server, so you will need to configure Spring Boot to deploy to an external WebLogic server.

Here are the steps to deploy your Spring Boot application to WebLogic:

#### 2.1: **Include WebLogic Dependencies**:
You need to include WebLogic's Maven dependencies to integrate it with Spring Boot. However, since WebLogic is not directly supported as an embedded server in Spring Boot, you'll have to package your Spring Boot application as a **WAR** file (Web Application Archive) instead of the default **JAR** file.

Add WebLogic's Maven dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>com.oracle.weblogic</groupId>
    <artifactId>weblogic-servlet-api</artifactId>
    <version>12.2.1.3</version>  <!-- Use the correct version based on your WebLogic setup -->
    <scope>provided</scope>
</dependency>
```

This dependency is provided by WebLogic during runtime, so you should mark it with the `<scope>provided</scope>` tag.

#### 2.2: **Change Packaging to WAR**:
You need to change the packaging of your Spring Boot application to `war` so that it can be deployed to WebLogic.

In your `pom.xml` file, change the `<packaging>` element:

```xml
<packaging>war</packaging>
```

#### 2.3: **Update Spring Boot Application Class**:
Make sure that your Spring Boot application extends `SpringBootServletInitializer` so that it can be deployed as a WAR to WebLogic. Modify your `Application.java` class to look like this:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
```

#### 2.4: **Configure Application Properties (Optional)**:
You may need to configure certain WebLogic-specific properties (like context path or server settings) in your `application.properties` or `application.yml` file.

For example, to set a custom context path in Spring Boot:

```properties
server.servlet.context-path=/myapp
```

#### 2.5: **Build the WAR**:
Run the following Maven command to build the WAR file:

```bash
mvn clean package
```

This will generate a WAR file in the `target` directory.

### 3. **Deploy WAR to WebLogic Server**:
After building the WAR file, you need to deploy it to WebLogic.

1. **Copy the WAR file** to the WebLogic deployment directory (usually `domains/{domain_name}/autodeploy/` or `domains/{domain_name}/servers/{server_name}/tmp`).
2. **Log in to WebLogic Admin Console** and deploy the WAR file via the **Deployments** section.
3. Alternatively, use WebLogic's **WLST** (WebLogic Scripting Tool) to automate deployment.

### 4. **Verify Deployment**:
Once deployed, WebLogic will start serving the Spring Boot application. You can check the WebLogic logs or access the application via the WebLogic server's URL (e.g., `http://localhost:7001/myapp`).

### TL;DR:
1. **Exclude Tomcat** by modifying the `pom.xml` or `build.gradle` to exclude the `spring-boot-starter-tomcat`.
2. **Include WebLogic** by adding WebLogic dependencies (`weblogic-servlet-api`) with the `provided` scope.
3. Change Spring Boot’s packaging to `war` and extend `SpringBootServletInitializer`.
4. Build the WAR file using `mvn clean package` and deploy it to the WebLogic server.

This way, you'll configure Spring Boot to be deployed on WebLogic instead of using the embedded Tomcat server.