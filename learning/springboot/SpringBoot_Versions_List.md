Confirmed — big update since my training cutoff: **Spring Boot has moved to major version 4.x** (Spring Boot 3.5 was the final 3.x release).

# 📘 Spring Boot Release Versions List (Verified — September 2026)

## 📋 Full Version Timeline

Spring Boot 4.1 was released 30 Jun 2026, with OSS support ending 31 Jul 2027 and commercial support ending 31 Jul 2028; the latest patch is 4.1.1, released 20 Aug 2026. Spring Boot 4.0 was released 30 Nov 2025, with OSS support ending 31 Dec 2026 and commercial support ending 31 Dec 2027; latest patch 4.0.8 (20 Aug 2026).

Spring Boot 3.5 was released 31 May 2025, with OSS support having ended 30 Jun 2026 and commercial support running until 30 Jun 2032; latest patch 3.5.16 (25 Jun 2026). 3.4 was released 30 Nov 2024, OSS support ended 31 Dec 2025, commercial support ends 31 Dec 2026. 3.3 was released 31 May 2024, both OSS (ended 30 Jun 2025) and commercial support (ended 30 Jun 2026) have now expired. 3.2 released 30 Nov 2023, fully EOL including commercial as of 31 Dec 2025. 3.1 released 31 May 2023, fully EOL as of 30 Jun 2025. 3.0 released 24 Nov 2022, fully EOL as of 31 Dec 2024.

2.7 was released 31 May 2022 — OSS support ended 30 Jun 2023, but commercial support runs unusually long, until 30 Jun 2029, reflecting its role as the last Java-8-compatible bridge release. Versions 2.6 and earlier are fully EOL, including commercial support.

| Release | GA Date | OSS Support Ends | Commercial Support Ends | Status (Sep 2026) |
|---|---|---|---|---|
| **4.1** | 30 Jun 2026 | 31 Jul 2027 | 31 Jul 2028 | ✅ **Current/Latest** |
| **4.0** | 30 Nov 2025 | 31 Dec 2026 | 31 Dec 2027 | ✅ Supported |
| 3.5 | 31 May 2025 | 30 Jun 2026 | 30 Jun 2032 | ⚠️ OSS EOL, commercial only |
| 3.4 | 30 Nov 2024 | 31 Dec 2025 | 31 Dec 2026 | ⚠️ OSS EOL, commercial only |
| 3.3 | 31 May 2024 | 30 Jun 2025 | 30 Jun 2026 | ❌ Fully EOL |
| 3.2 | 30 Nov 2023 | 31 Dec 2024 | 31 Dec 2025 | ❌ Fully EOL |
| 3.1 | 31 May 2023 | 30 Jun 2024 | 30 Jun 2025 | ❌ Fully EOL |
| 3.0 | 24 Nov 2022 | 31 Dec 2023 | 31 Dec 2024 | ❌ Fully EOL |
| 2.7 | 31 May 2022 | 30 Jun 2023 | 30 Jun 2029 | ⚠️ Commercial only (long tail) |
| 2.6 and earlier | — | — | — | ❌ Fully EOL |

---

## 🔑 Java Compatibility Matrix

Spring Boot 4.1 supports Java 17 through 26; 3.5 through 4.0 support Java 17 through 25; 3.4 supports Java 17 through 24; 3.3 supports Java 17 through 23; 3.0 through 3.2 support Java 17 through 21; and 2.7 supports Java 8 through 21.

| Spring Boot | Java Range |
|---|---|
| 4.1 | 17 – 26 |
| 3.5 – 4.0 | 17 – 25 |
| 3.4 | 17 – 24 |
| 3.3 | 17 – 23 |
| 3.0 – 3.2 | 17 – 21 |
| 2.7 | 8 – 21 |

---

## 🆕 What's New: The 3.x → 4.0 Jump (This Postdates My Training)

Based on current search results:

Spring Boot 4.0 brings stable API versioning for HTTP endpoints via enhanced @HttpExchange and RestClient support, JSpecify null-safety annotations replacing older @Nullable patterns for more accurate static analysis, and a JakartaEE 11 baseline with Jackson 3 as the new default. It also removes deprecations accumulated across the entire 3.x series, including older Actuator endpoints and legacy configuration properties. Because 4.0 inherits Spring Framework 7, the migration touches Hibernate, Jackson, Tomcat, and any custom auto-configuration libraries that depend on internal Spring APIs.

Spring Boot uses standard MAJOR.MINOR.PATCH versioning, and for compliance purposes like the EU Cyber Resilience Act, the MAJOR and MINOR numbers are what signal the support window and potential for breaking changes — running unsupported versions is flagged as a major compliance red flag.

Spring Boot 4.1.0 requires Java 17 as a minimum and requires Spring Framework 7.0.8 or above.

---

## 🎯 Interview Questions (Updated)

1. **Q: What's the current major version line of Spring Boot?**
   **A:** Spring Boot has moved to 4.x — 3.5 was the final 3.x release (Spring Framework 6.2), and 4.0 introduced Spring Framework 7.
   *Follow-up: What's the headline breaking change in 4.0?*

2. **Q: What changed structurally in the jump to Spring Boot 4.0?**
   **A:** JakartaEE 11 baseline, Jackson 3 as default, stable API versioning for HTTP endpoints, JSpecify null-safety annotations, and removal of long-accumulated 3.x deprecations.
   *Follow-up: Why would upgrading from 3.x to 4.0 also force a Hibernate/Jackson/Tomcat review?*

3. **Q: Which Spring Boot line has an unusually long commercial support tail, and why?**
   **A:** 2.7 — commercial support runs until 2029, far longer than typical, because it was the last release supporting Java 8 and pre-Jakarta namespace, so many enterprises needed a longer bridge before migrating.
   *Follow-up: What compliance risk exists for a company still running 2.7 today without commercial support?*

4. **Q: As of today, which Spring Boot lines are still receiving free OSS security patches?**
   **A:** 4.1 and 4.0 — everything in the 3.x line has exited OSS support (3.5 most recently, ending 30 Jun 2026).
   *Follow-up: What's your migration priority if you're currently running 3.4 or 3.5 in production?*

---

## 💡 Interview Tip

If asked this in an interview, the strongest answer demonstrates you *track* the ecosystem rather than reciting memorized numbers: mentioning that Spring Boot crossed into major version 4 (with the Spring Framework 7 / Jakarta EE 11 baseline) shows active awareness — many candidates who last touched this a year ago will still be anchored to "3.x is current," which is now outdated.

---

## Useful Resources

- **Live Support Matrix (source for this table):** https://endoflife.date/spring-boot
- **Official Spring Boot Support Page:** https://spring.io/projects/spring-boot#support
- **GitHub Release Notes:** https://github.com/spring-projects/spring-boot/wiki
- **4.0 Migration considerations (HeroDevs writeup):** https://www.herodevs.com/blog-posts/spring-boot-versions-eol-dates-and-latest-releases-april-2026

---
