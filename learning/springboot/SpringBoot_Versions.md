# Managed Dependency Versions for Prominent Spring Boot Releases

This document outlines the core framework, runtime, and third-party library baselines managed by the [Spring Boot Bill of Materials (BOM)](https://docs.spring.io/spring-boot/appendix/dependency-versions/coordinates.html) for prominent release branches.

## 1. Core Framework & Environment Baselines

The underlying system requirements and native framework baselines for each active release line.

| Dependency / Baseline | Spring Boot 4.1.x (Current Stable) | Spring Boot 4.0.x (Active Patch) | Spring Boot 3.4.x (OSS EOL Reference) |
| :--- | :--- | :--- | :--- |
| **Minimum Java Version** | Java 17 (Supports up to Java 26) | Java 17 | Java 17 |
| **Spring Framework** | 7.0.x | 7.0.x | 6.2.x |
| **Jakarta EE Baseline** | Jakarta EE 11 | Jakarta EE 11 | Jakarta EE 10 |
| **Kotlin** | 2.2.0+ | 2.0.x / 2.1.x | 1.9.x / 2.0.x |

---

## 2. Core Spring Ecosystem Dependencies

These ecosystem dependencies are managed implicitly by the `spring-boot-dependencies` parent artifact.

| Group ID & Artifact ID | Spring Boot 4.1.x | Spring Boot 4.0.x | Spring Boot 3.4.x |
| :--- | :--- | :--- | :--- |
| `org.springframework:spring-core` | 7.0.8+ | 7.0.7 | 6.2.x |
| `org.springframework.security:spring-security-core` | 7.0.x | 7.0.5 | 6.4.x |
| `org.springframework.data:spring-data-bom` | 2026.0.x | 2025.0.x | 2024.1.x |
| `org.springframework.kafka:spring-kafka` | 4.1.x | 4.0.5 | 3.3.x |
| `org.springframework.amqp:spring-rabbit` | 4.1.x | 4.0.x | 3.2.x |
| `org.springframework.integration:spring-integration-core` | 7.1.x | 7.0.0 | 6.4.x |

---

## 3. Prominent Web Runtimes & Third-Party Dependencies

Spring Boot provides auto-configured starter defaults for embedded servers, serialization tools, and persistence engines.

| Dependency / Runtime | Spring Boot 4.1.x | Spring Boot 4.0.x | Spring Boot 3.4.x |
| :--- | :--- | :--- | :--- |
| **Apache Tomcat** | 11.0.x | 11.0.x | 10.1.x |
| **Hibernate ORM** | 7.2.x | 7.2.x | 6.6.x |
| **Jackson BOM** | 3.1.x | 2.17.x / 2.18.x | 2.18.0 |
| **Caffeine Cache** | 3.2.x | 3.1.x | 3.1.x |
| **Logback** | 1.5.x | 1.5.x | 1.5.x |
| **Flyway Core** | 10.x / 11.x | 10.x | 10.20.x |

---

## 4. How to Reference or Override Managed Versions

### Relying on the Managed Version
When defining dependencies in your Maven `pom.xml`, emit the `<version>` tag entirely. The [Spring Boot Starter Parent](https://goregulus.com/cra-basics/spring-boot-versions/) resolves it automatically:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Overriding a Specific Version
To force a different version than the default managed by the BOM, override the explicit release property in your file properties block:

#### Maven (pom.xml)
```xml
<properties>
    <hibernate.version>7.2.19.Final</hibernate.version>
</properties>
```

#### Gradle Version Catalog (`libs.versions.toml`)
If managing dependencies via modern central catalogs, match your declarations inside your [Gradle Central Catalog](https://sabledhiraj.medium.com/streamlining-dependency-management-in-spring-boot-with-libs-versions-toml-7476bbe7ea18) configuration:

```toml
[versions]
springboot = "4.1.0"

[libraries]
springboot-starter = { module = "org.springframework.boot:spring-boot-starter", version.ref = "springboot" }
```


---
## From Claude
## How Spring Boot Manages Dependency Versions (The Mechanism)

**Real-life analogy:** Think of Spring Boot's parent POM / BOM as a **grocery list curated by someone who's already tested every ingredient combination**. You don't pick your own flour brand and yeast brand and hope they work together — you trust the curated list, and it's been verified end-to-end.

### The Core Mechanism: `spring-boot-dependencies` BOM

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.x</version>
</parent>
```

This parent POM imports `spring-boot-dependencies`, a **Bill of Materials (BOM)** — a giant `<dependencyManagement>` block that pins ~200+ library versions (Jackson, Hibernate, Tomcat, Micrometer, etc.) so they're all mutually compatible. You don't specify versions for managed dependencies in your own `pom.xml` — Spring Boot decides.

```
Your pom.xml declares:  spring-boot-starter-web (no version)
                              │
                              ▼
              spring-boot-dependencies BOM resolves it to
              the exact tested version for that Boot release
```

> **Interview gotcha:** If you *do* override a version manually (e.g., force a newer Jackson), you're opting out of Spring's compatibility testing — a classic source of "works on my machine" bugs. Good answer to "when would you override a BOM-managed version?" → only with strong justification (CVE patch) and full regression testing.

---

## Version Outline by Spring Boot Generation

### Spring Boot 3.x line (Jakarta EE 9+, Java 17 baseline)

| Spring Boot | Spring Framework | Java Baseline | Tomcat | Hibernate | Jackson |
|---|---|---|---|---|---|
| 3.0.x | 6.0.x | 17 | 10.1.x | 6.1.x | 2.14.x |
| 3.1.x | 6.0.x | 17 | 10.1.x | 6.2.x | 2.15.x |
| 3.2.x | 6.1.x | 17 (21 supported) | 10.1.x | 6.4.x | 2.15.x |
| 3.3.x | 6.1.x | 17 (21/22 supported) | 10.1.x | 6.5.x | 2.17.x |
| 3.4.x | 6.2.x | 17 (23 supported) | 10.1.x | 6.6.x | 2.18.x |

> ⚠️ These are from memory as of my training cutoff — patch-level numbers especially may be off. **Please verify exact patch versions** via the BOM before quoting them in an interview or a real pom.xml.

### What Changed Structurally at 3.0 (worth knowing cold for interviews)

| Aspect | Spring Boot 2.x | Spring Boot 3.x |
|---|---|---|
| Namespace | `javax.*` | `jakarta.*` |
| Min Java | 8 | 17 |
| Native compilation | Limited/experimental | First-class (GraalVM via Spring AOT) |
| Observability | Micrometer only | Micrometer + Micrometer Tracing (replaces Sleuth) |

---

## How to Actually Check Live Versions (More Reliable Than My Memory)

Since I can't fetch current data, here's how to get ground truth yourself:

**1. Maven — see the fully resolved dependency tree:**
```bash
mvn dependency:tree
```

**2. Check what a specific Boot version manages, without even creating a project:**
```bash
curl https://raw.githubusercontent.com/spring-projects/spring-boot/v3.3.5/spring-boot-project/spring-boot-dependencies/build.gradle
```

**3. Gradle — view the resolved BOM:**
```bash
./gradlew dependencies --configuration compileClasspath
```

**4. IDE approach:** In IntelliJ, `Ctrl+Click` (Cmd+Click on Mac) into `spring-boot-dependencies` in your `pom.xml`'s effective POM view — shows every managed version directly.

---

## 🎯 Interview-Oriented Insights

| Question | Key Answer |
|---|---|
| How does Spring Boot avoid dependency version conflicts? | Centralized BOM (`spring-boot-dependencies`) tested as a compatible matrix, imported via `spring-boot-starter-parent` or `dependencyManagement` |
| What if you're not using `spring-boot-starter-parent` (e.g., your company has its own parent POM)? | Import the BOM manually: `<dependencyManagement><dependencies><dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-dependencies</artifactId><type>pom</type><scope>import</scope></dependency>...` |
| Why did Spring Boot 3.0 jump straight to Java 17? | Alignment with Jakarta EE 9 (`jakarta.*` namespace migration) and to leverage newer JVM features (records, sealed classes, virtual threads groundwork) |
| How do you handle a CVE in a transitive dependency before Spring Boot patches it? | Override the managed version explicitly in your own `<dependencyManagement>`, test thoroughly, and track upstream for the official BOM fix |
| Difference between `spring-boot-starter-parent` and importing the BOM directly? | Starter-parent also gives you plugin management, resource filtering, and default encoding — the BOM import alone only manages dependency versions |

---

## 📚 Resources to Verify Current Numbers

- **Live BOM source of truth:** `https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-dependencies/build.gradle`
- **Release notes:** `https://github.com/spring-projects/spring-boot/wiki`
- **spring.io official page** for currently supported versions and EOL dates

Given how fast this moves, would it help if you enabled web search for this kind of query going forward? I can pull exact current numbers that way instead of relying on my (potentially stale) memory.