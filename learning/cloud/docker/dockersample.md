## 🐳 **1. Sample Dockerfile for Spring Boot App**

Here’s a simple and production-ready Dockerfile for a Spring Boot application built using Maven:

```Dockerfile
# Step 1: Build stage (optional if using multi-stage builds)
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Runtime stage
FROM openjdk:17-alpine
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

✅ **Key Points to Explain in Interviews**:
- Multi-stage build (efficient image size)
- Uses **OpenJDK 17** (align with Java 17+ projects)
- `VOLUME /tmp` optimizes file writes
- `ENTRYPOINT` launches the app

---

## 📦 **2. Sample `docker-compose.yml` for a Microservice + DB**

```yaml
version: '3.8'

services:
  app-service:
    image: my-springboot-app:latest
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db-service:5432/mydb
    depends_on:
      - db-service

  db-service:
    image: postgres:14
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
```

✅ **Explain**:
- `depends_on` ensures DB starts before the app
- Spring Boot uses `application.yml` or `env` for DB connection
- This sets up **Spring Boot + PostgreSQL** stack

---

## 🎯 Bonus: Docker + Kubernetes Interview Questions

| Question | What to Say |
|---------|-------------|
| How do you containerize a Spring Boot microservice? | Use a Dockerfile → build → run with ports and volumes. |
| How do you orchestrate multiple containers? | Use Docker Compose or Kubernetes (Helm for config). |
| How is config managed in Docker vs Kubernetes? | Docker uses `.env` or hardcoded values; Kubernetes uses **ConfigMaps & Secrets**. |
| How do you scale containers? | Use `docker-compose scale` or `kubectl scale deployment`. |
