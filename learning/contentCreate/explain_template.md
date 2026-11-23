# The universal answer template: **D-E-P-T-H**

Use this for any technical question. Total time: 30-90 seconds, depending on how deep they want to go.

| Step | What to say | Time |
|---|---|---|
| **D**efine | One-line definition in plain words | 5 s |
| **E**xplain the "why" | The problem it solves, or why it exists | 5-10 s |
| **P**ractical example | A real project use, with numbers if you have them | 10-15 s |
| **T**rade-offs | Pros and cons, and when *not* to use it | 5-10 s |
| **H**and-off | Stop and invite a follow-up | 2 s |

# Example: "What is a circuit breaker?"

> **D:** "It's a resilience pattern that stops calls to a failing service. **E:** Without it, slow dependencies exhaust threads and cause cascading failures. **P:** In our payments service we used Resilience4j. It opened at a 50% failure rate and served cached data as the fallback, which cut our incident impact a lot. **T:** The downside is tuning. Wrong thresholds make it flap, so we monitored state changes. **H:** I can go into half-open behaviour if you like."

# Rules that separate a 12-year answer from a 5-year answer

1. **Never start with only a definition.** Start with the *problem*. Seniors explain why, juniors recite what.
2. **Always attach a project story.** Use "In my last project, we…" Interviewers trust experience over theory.
3. **Always mention a trade-off.** Say "it depends on X" and then say what X is. Avoid "always" and "never".
4. **Use numbers.** Examples are p99 latency, pool size, 40% fewer incidents, or 2M requests per day.
5. **Compare with the alternative.** For example, `@Primary` vs `@Qualifier`, or `synchronized` vs `ReentrantLock`.
6. **Stay at the level asked.** Give a short answer first, then go deeper only if they probe. Don't dump everything in one go.

# Templates for other question types

| Question type | Template |
|---|---|
| **"Difference between A and B"** | One-line each → 2-3 key differences → "I use A when…, B when…" |
| **"How would you design X?"** | Requirements → scale numbers → high-level components → bottlenecks and failure handling → trade-offs |
| **"How do you debug / troubleshoot X?"** | Confirm impact → check what changed → narrow down (metrics → logs → traces) → mitigate first → root cause → prevent recurrence |
| **"Tell me about a time…" (behavioural)** | **STAR**: Situation → Task → Action (use "I", not "we") → Result (with numbers) → Learning |
| **"Have you worked with X?" (you haven't)** | "Not in production, but I know the concept: [one line]. It's similar to [thing you know], and I'd pick it up by [plan]." |
| **Coding / problem-solving** | Clarify inputs → brute-force idea → optimise → state time/space complexity → test edge cases aloud |

# Handling the tricky moments

- **You don't know the answer:** Say "I haven't used that directly, but here's how I'd reason about it…" and then think aloud. Never bluff.
- **You need time:** Say "Let me structure my thoughts for a second." This looks senior, not weak.
- **You're running long:** Stop at "Happy to go deeper on any part." Rambling hurts more than brevity.
- **A follow-up goes beyond your knowledge:** Be honest and bridge to what you do know.

# Quick pre-interview prep

For each of your top 15 topics (HashMap, JVM/GC, Spring DI, transactions, microservices, Kafka, and so on), write down:

1. A one-line definition
2. One real project example with a number
3. One trade-off

If you have those three, the D-E-P-T-H template builds the whole answer on its own.

I can turn this into a one-page printable cheat sheet, or run a mock interview where you answer and I give feedback against this template. Which would help more?