Here's a **system design walkthrough** for a **Twitter-like application**, focusing on core features like **Tweeting** and **Following** — a very common system design interview problem.

---

## 🧠 **Problem Scope (MVP)**

Design a **Twitter-like** microblogging system with:

* **Post Tweet**
* **Follow / Unfollow users**
* **User Timeline** (latest tweets from followed users)
* **Tweet Feed** (global / public tweets – optional)

---

## 🔧 Core Functional Requirements

1. **Post a Tweet** (<=280 characters)
2. **Follow/Unfollow** another user
3. **Get Timeline** (feed of tweets from followed users)
4. **Get User's Tweets** (profile page)

---

## 🔩 Non-Functional Requirements

* **High Read Throughput** (Timeline reads >> Writes)
* **Low Latency** for Timeline
* **Scalable** for millions of users
* **Eventually consistent** timelines are acceptable

---

## 🧱 High-Level Architecture

```
       +---------------+
       |   Load Balancer|
       +-------+-------+
               |
         +-----v-----+
         |  API Tier |
         +-----+-----+
               |
      +--------v--------+
      |  Service Layer   |
      |------------------|
      | - TweetService   |
      | - FollowService  |
      | - TimelineService|
      +--------+---------+
               |
      +--------v--------+
      |   Data Layer     |
      |------------------|
      | - Tweets Store   |
      | - User Graph     |
      | - Timeline Cache |
      +------------------+
```

---

## 🔹 Data Modeling

### 📄 **User Table**

```sql
User(id, username, email, created_at)
```

### 🧑‍🤝‍🧑 **Follow Table** (Adjacency List)

```sql
Follow(follower_id, followee_id, followed_at)
```

### 📝 **Tweet Table**

```sql
Tweet(id, user_id, text, created_at)
```

### 🗂️ **Timeline Table** (pre-computed timeline feed)

```sql
Timeline(user_id, tweet_id, created_at)
```

---

## 📦 **Tweet Posting Flow**

1. User posts a tweet
2. Save tweet in TweetStore (SQL/NoSQL)
3. **Fanout** the tweet to followers:

    * Push tweet IDs into each follower’s **Timeline Store** (Redis/List or DB)
    * Use **Kafka** for async fan-out if followers are large

---

## 👀 **Timeline Generation Strategies**

### 1. **Fan-out on Write (Push Model)** ✅

* On tweet, **push** to followers' timelines (precompute).
* Fast reads, slower writes.
* Used by Twitter for normal users.

### 2. **Fan-out on Read (Pull Model)** ❌

* On read, fetch tweets from followed users.
* Simpler but high latency and expensive joins.

### 3. **Hybrid**

* Fan-out for users with < 10k followers.
* Pull model or batch jobs for celebs (>1M followers).

---

## 🚀 **Optimizations**

* **Redis**: Store most recent timeline (fast reads)
* **Elasticsearch**: Search tweets by keywords
* **Rate limiting**: Tweeting, following to prevent spam
* **Content moderation** pipeline
* **Sharding** tweets by tweet\_id mod n
* **Follower count thresholding** to limit fan-out costs

---

## 🔐 **Security & Reliability**

* Rate limit tweets & follows per minute
* Idempotency key on tweet post to avoid duplicates
* Pagination & cursor-based timeline fetching
* Handle follower cycles

---

## ⚠️ Edge Cases

* Deleting tweets (should remove from timelines)
* User blocks (filter tweets from blocked users)
* Offline delivery guarantees

---

## ✅ TL;DR for Interviews

* Twitter's core design involves **fan-out on write** for timelines.
* Store tweets separately and **duplicate tweet IDs** in followers’ timelines (Redis or DB).
* Use **Kafka** for scalable fan-out, Redis for low-latency read.
* Maintain **Follow graph**, keep Tweet + Timeline stores separate.
* Balance **consistency vs performance**, esp. for high-follower users.

---
Let’s deep dive into the **Timeline Service**, which is **the most critical part** of the Twitter system design and a **favorite interview topic** due to its complexity in scale, performance, and consistency.

---

## 🧠 **Timeline Service – Deep Dive**

---

### ✅ **Goal**

Provide a **personalized timeline** to a user:

* Containing tweets from people they follow.
* Sorted by time (usually descending).
* Fast retrieval with low latency.

---

### 🧩 **Two Main Strategies**

| Strategy                          | Description                                                | Pros             | Cons                                            |
| --------------------------------- | ---------------------------------------------------------- | ---------------- | ----------------------------------------------- |
| **Fan-out on Write** (Push model) | On tweet post, **push** tweet to all followers' timelines. | Very fast reads. | Slow writes for users with many followers.      |
| **Fan-out on Read** (Pull model)  | On timeline read, **pull tweets** from followed users.     | Write is cheap.  | Expensive reads, not scalable for active users. |
| **Hybrid**                        | Push for normal users; pull for celebrities.               | Best of both.    | Complexity in infra logic.                      |

---

## 🏗️ **High-Level Timeline Service Flow (Fan-out on Write)**

```
1. User A tweets
2. Tweet stored in Tweet Store
3. Timeline Service:
   - Gets follower list of A
   - For each follower:
       a. Push tweet ID to their Timeline (Redis/List/DB)
4. User B opens timeline:
   - Read tweet IDs from Redis
   - Fetch tweet content from Tweet Store
```

---

### ⚙️ **Data Model**

#### 🔹 Tweet Table (DB)

```sql
Tweet(id, user_id, content, created_at)
```

#### 🔹 Timeline Table (Redis or DB)

```
Timeline:
  key = user_id
  value = [tweet_id1, tweet_id2, ..., tweet_idN] (sorted)
```

---

### 📈 **Scalability Tricks**

#### 1. **Asynchronous Fan-out (via Kafka)**

* Avoid blocking tweet API.
* Producer: TweetService sends tweet event to Kafka.
* Consumer: TimelineWorker pulls from Kafka and updates timelines.

#### 2. **Sharded Timeline Storage**

* Timeline data is sharded across servers by `user_id % N`.

#### 3. **Timeline Pagination**

* Use **cursor-based** pagination (tweet\_id as cursor).
* Prevents duplicates or missing tweets.

#### 4. **Celebrity Follower Optimization**

* Don’t fan-out tweets for celebs.
* Instead, pull on-demand or use **live queries**.

---

### 🧪 **Read Timeline API – Example**

```java
List<Tweet> getTimeline(userId, lastTweetId, limit) {
  List<Long> tweetIds = redis.getSortedSet(userId, lastTweetId, limit);
  return tweetService.getTweetsByIds(tweetIds);
}
```

---

## 📉 Failure Handling

| Failure Point                        | Mitigation                                |
| ------------------------------------ | ----------------------------------------- |
| Tweet not delivered to all timelines | Retry fan-out jobs from Kafka             |
| Redis down                           | Fallback to DB (slower)                   |
| Kafka lag                            | Show partial timeline with "loading more" |
| DB inconsistency                     | Periodic timeline repair jobs             |

---

## ✅ TL;DR – Timeline Service

* **Fan-out on Write** enables fast reads; use **Kafka + Redis** to scale.
* Store **only tweet IDs** in timeline storage; content fetched separately.
* Use **hybrid fan-out logic** for celeb vs regular users.
* Consider pagination, retry queues, sharding, and failure handling.

---
