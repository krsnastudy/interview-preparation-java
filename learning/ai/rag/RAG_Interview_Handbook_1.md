# 📘 Retrieval Augmented Generation (RAG)

**Audience:** Senior Java Developer · Technical Lead · Solution Architect
**Focus:** Enterprise / BFSI knowledge assistants built on Java and Spring
**Stack used in examples:** Java 17+, Spring Boot 3.x, Spring AI, PostgreSQL + pgvector

Large language models are fluent but closed-book: they know only what was in their training data. RAG makes them open-book. At question time the system retrieves relevant passages from your own documents and hands them to the model as evidence, so the answer is grounded in current, private, citable sources.

---

## 📖 What is RAG?

**Definition:** Retrieval Augmented Generation is a pattern in which a retriever fetches relevant content from an external knowledge source at query time, and that content is injected into the LLM prompt so the model generates an answer grounded in it. The term comes from the 2020 paper *Retrieval-Augmented Generation for Knowledge-Intensive NLP Tasks* (Lewis et al.).

**Why it exists:** Model weights are a frozen, lossy snapshot. They cannot know yesterday's circular, your internal credit policy, or a customer's account terms.

**Problem it solves:**
- Knowledge cut-off: the model does not know recent facts
- Hallucination: the model invents plausible but wrong answers
- Private data: internal documents were never in the training set
- Traceability: regulated users need to see *which source* supports an answer

**Key characteristics:**

| Characteristic | Meaning |
|---|---|
| Two pipelines | An offline **ingestion** pipeline and an online **query** pipeline |
| Semantic search | Meaning-based matching using embeddings, often combined with keyword search |
| Grounded generation | The prompt instructs the model to answer only from the supplied context |
| Updatable | Change the documents, not the model |
| Citable | Answers can reference document, section, and version |

> **Key Takeaway:** RAG is "search + prompt + generate." The retriever decides *what the model sees*, so retrieval quality caps answer quality.

---

## ❓ Why Do We Need It?

**Business problem:** A bank has thousands of pages of policies, product terms, and regulatory circulars. Staff lose hours searching, answers differ between people, and wrong answers carry compliance risk.

**Technical problem:**
- LLMs hallucinate when they lack the facts
- Context windows are finite and cost grows with every token sent
- Fine-tuning does not reliably inject new facts and goes stale

| Earlier approach | Limitation | RAG advantage |
|---|---|---|
| Keyword search (Elasticsearch only) | Misses synonyms and paraphrases | Semantic retrieval finds meaning |
| Fine-tune the model on documents | Costly, slow to refresh, no citations, can still hallucinate | Update the index in minutes, cite sources |
| Paste all documents into the prompt | Token cost, latency, relevance diluted ("lost in the middle") | Send only the top relevant chunks |
| Rule-based FAQ bots | Brittle, high maintenance | Handles free-form questions |

> **Key Takeaway:** Use RAG when the model needs facts it was never trained on, facts that change, or facts that must be cited.

---

## ⚙️ Internal Working

### Pipeline 1: Ingestion (offline, repeated on change)

```
+-----------------+    +----------------+    +------------------+
| Source docs     |--->| Parse + clean  |--->| Chunk            |
| PDF, Word, wiki |    | (Tika, OCR)    |    | 200-1000 tokens  |
| Confluence, DB  |    | strip headers  |    | with overlap     |
+-----------------+    +----------------+    +--------+---------+
                                                      |
                                                      v
+-----------------+    +----------------+    +------------------+
| Vector store    |<---| Attach         |<---| Embedding model  |
| + keyword index |    | metadata       |    | text -> vector   |
| (pgvector etc.) |    | (ACL, version) |    +------------------+
+-----------------+    +----------------+
```

### Pipeline 2: Query (online, per request)

```
+--------------+
| User question|
+------+-------+
       |
       v
+--------------+   +--------------------+
| Query rewrite|-->| Embed query        |
| (optional)   |   +---------+----------+
+--------------+             |
                             v
                  +----------------------+
                  | Retrieve top-k       |  vector + keyword (hybrid)
                  | + metadata/ACL filter|
                  +----------+-----------+
                             |
                             v
                  +----------------------+
                  | Rerank (cross-encoder|  keep best 3-5
                  +----------+-----------+
                             |
                             v
                  +----------------------+
                  | Build prompt:        |  instructions + context + question
                  | "answer only from    |
                  |  the context"        |
                  +----------+-----------+
                             |
                             v
                  +----------------------+
                  | LLM generates answer |
                  | + citations          |
                  +----------+-----------+
                             |
                             v
                  +----------------------+
                  | Guardrails, logging, |
                  | feedback, evaluation |
                  +----------------------+
```

### Step-by-step

1. **Parse and clean:** extract text, remove boilerplate, keep structure (headings, tables).
2. **Chunk:** split into passages. Common starting points are 256–1024 tokens with 10–20% overlap, tuned by evaluation, not by guesswork.
3. **Embed:** an embedding model converts each chunk into a vector, so similar meanings sit close together.
4. **Store:** write vectors plus metadata (document ID, version, effective date, access group) to a vector store.
5. **Retrieve:** embed the question, search by similarity, combine with keyword search if needed, apply metadata filters.
6. **Rerank:** a cross-encoder rescores the candidates against the question for better precision.
7. **Augment:** place the best chunks into a prompt template with clear instructions.
8. **Generate and verify:** the LLM answers with citations; checks confirm the answer is supported.

**Similarity metrics:**

| Metric | Idea | Note |
|---|---|---|
| Cosine similarity | Angle between vectors | Most common for text embeddings |
| Dot product | Angle and magnitude | Equals cosine when vectors are normalised |
| Euclidean (L2) | Straight-line distance | Used by some models and indexes |

**Hybrid search with Reciprocal Rank Fusion (RRF):** merge a keyword ranking (BM25) and a vector ranking. Each document scores `sum(1 / (k + rank))` across the lists (commonly `k = 60`). It needs no score calibration between the two systems.

> **Key Takeaway:** Ingestion quality (parsing, chunking, metadata) and retrieval quality (hybrid, filters, rerank) matter more than which LLM you pick.

---

## 🏗 Architecture / Internal Components

| Component | Responsibility |
|---|---|
| **Document loader / parser** | Reads PDF, Word, HTML, wiki, DB rows (Apache Tika, OCR for scans) |
| **Chunker / splitter** | Cuts text into retrievable units; structure-aware or semantic splitting beats fixed size for long policies |
| **Embedding model** | Maps text to a dense vector; the *same* model must embed documents and queries |
| **Vector store** | Persists vectors and runs approximate nearest-neighbour search (pgvector, OpenSearch/Elasticsearch, Milvus, Redis, Pinecone) |
| **ANN index** | HNSW (graph, high recall, more memory) or IVF (clusters, less memory); trades recall for speed |
| **Keyword index** | BM25 for exact terms, product codes, clause numbers |
| **Metadata store** | Tenant, classification, version, effective dates, access groups |
| **Query transformer** | Rewrites vague or multi-turn questions, expands, or decomposes them |
| **Retriever** | Executes search with filters and returns top-k |
| **Reranker** | Cross-encoder that rescores candidates (slower, more accurate than vector search) |
| **Prompt builder** | Template combining instructions, context, and question |
| **LLM** | Generates the final answer |
| **Guardrails** | Input and output checks: PII, injection, groundedness |
| **Evaluator** | Offline test set and online feedback to measure quality |
| **Cache** | Reuses embeddings and answers to cut cost and latency |

> **Key Takeaway:** RAG is a pipeline of small, swappable components. Each one can fail independently, which is why each one needs its own metrics.

---

## 🔄 Visual Flow

### RAG inside a Spring Boot service

```
+-----------+     +------------------+     +---------------------+
| Web / App |---->| RAG API          |---->| Auth + entitlements |
| client    |     | (Spring Boot)    |     | (OAuth2 / JWT)      |
+-----------+     +--------+---------+     +----------+----------+
                           |                          |
                           | filter = user's groups   |
                           v                          |
                  +------------------+                |
                  | Spring AI        |<---------------+
                  | ChatClient +     |
                  | RAG advisor      |
                  +----+--------+----+
                       |        |
          similarity   |        |  prompt + context
          search       v        v
              +-----------+  +--------------+
              | pgvector  |  | LLM provider |
              | (chunks)  |  | (API / local)|
              +-----------+  +--------------+
```

### RAG versus fine-tuning, at a glance

```
Fine-tuning:  knowledge -> baked INTO weights  (static, hard to cite)
RAG:          knowledge -> fetched AT query time (fresh, citable, access-controlled)
```

---

## 💻 Code Examples

### Basic: the retrieval idea in plain Java (no framework)

```java
import java.util.*;

public class MiniRetriever {

    // A chunk of text with its embedding vector
    record Chunk(String text, double[] vector) {}

    // Cosine similarity = dot(a, b) / (|a| * |b|)
    static double cosine(double[] a, double[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na  += a[i] * a[i];
            nb  += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    // Return the k most similar chunks to the query vector
    static List<Chunk> topK(List<Chunk> store, double[] query, int k) {
        return store.stream()
                .sorted(Comparator.comparingDouble((Chunk c) -> cosine(c.vector(), query)).reversed())
                .limit(k)
                .toList();
    }
    // In reality 'vector' comes from an embedding model, and the store is a vector database.
}
```

### Intermediate: ingestion with Spring AI

```java
@Service
public class IngestionService {

    private final VectorStore vectorStore;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingest(Resource pdf, String department, String version) {
        // 1. Parse the file into Documents
        List<Document> docs = new TikaDocumentReader(pdf).get();

        // 2. Split into chunks (token-based splitter)
        List<Document> chunks = new TokenTextSplitter().apply(docs);

        // 3. Attach metadata used later for filtering, versioning and citations
        chunks.forEach(c -> {
            c.getMetadata().put("department", department);
            c.getMetadata().put("version", version);
            c.getMetadata().put("source", pdf.getFilename());
        });

        // 4. Embed and store (the configured EmbeddingModel is called by the VectorStore)
        vectorStore.add(chunks);
    }
}
```

### Intermediate: naive RAG with QuestionAnswerAdvisor

```java
@Service
public class PolicyAssistant {

    private final ChatClient chatClient;

    public PolicyAssistant(ChatClient.Builder builder, VectorStore vectorStore) {
        // The advisor searches the vector store and appends the results to the prompt
        this.chatClient = builder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder()
                                .similarityThreshold(0.75)   // ignore weak matches
                                .topK(5)                     // number of chunks to retrieve
                                .build())
                        .build())
                .build();
    }

    public String ask(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}
```

### Production: advanced RAG with rewrite, access filter, and "I don't know"

```java
@Service
public class SecurePolicyAssistant {

    private final ChatClient chatClient;

    public SecurePolicyAssistant(ChatClient.Builder builder, VectorStore vectorStore) {

        Advisor rag = RetrievalAugmentationAdvisor.builder()
                // Pre-retrieval: rewrite vague multi-turn questions into standalone queries
                .queryTransformers(RewriteQueryTransformer.builder()
                        .chatClientBuilder(builder.build().mutate())
                        .build())
                // Retrieval: similarity search with a score threshold
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .similarityThreshold(0.60)
                        .topK(8)
                        .build())
                // Augmentation: when nothing relevant is found, do NOT let the model guess
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(false)
                        .build())
                .build();

        this.chatClient = builder.defaultAdvisors(rag).build();
    }

    public String ask(String question, Set<String> userDepartments) {
        // Entitlements come from the validated JWT on the server side, never from the request body.
        String filter = "department in ['" + String.join("','", userDepartments) + "']";

        return chatClient.prompt()
                .advisors(a -> a.param(VectorStoreDocumentRetriever.FILTER_EXPRESSION, filter))
                .user(question)
                .call()
                .content();
    }
}
```

> Module names and builder options have moved between Spring AI releases (for example `spring-ai-advisors-vector-store` for `QuestionAnswerAdvisor`, `spring-ai-rag` for `RetrievalAugmentationAdvisor`, and the modular advisor was flagged experimental in some versions). Check the reference docs for the exact version you use.

### Spring Boot + pgvector: schema and similarity query

```sql
-- Enable the extension and create a table sized to your embedding model
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE doc_chunk (
    id          BIGSERIAL PRIMARY KEY,
    content     TEXT NOT NULL,
    metadata    JSONB,
    embedding   VECTOR(1536)          -- dimension MUST match the embedding model
);

-- Approximate index: HNSW with cosine distance
CREATE INDEX ON doc_chunk USING hnsw (embedding vector_cosine_ops);

-- Query: <=> is cosine distance (smaller = more similar)
SELECT content, metadata
FROM doc_chunk
WHERE metadata->>'department' = 'RISK'
ORDER BY embedding <=> :queryVector
LIMIT 5;
```

```yaml
# application.yml (illustrative)
spring:
  ai:
    vectorstore:
      pgvector:
        index-type: HNSW
        distance-type: COSINE_DISTANCE
        dimensions: 1536
```

> **Key Takeaway:** Start with naive RAG, measure it, and add rewrite, hybrid search, and reranking only where evaluation shows a gap.

---

## 🏦 Real Production Example

**Scenario: internal credit-policy and regulatory assistant at a bank**

Relationship managers and ops staff ask, "What is the maximum LTV for a salaried applicant under the current home-loan policy, and what exceptions apply?"

```
+---------------------+    +-------------------+    +-----------------+
| Policy PDFs, circ-  |--->| Ingestion job     |--->| pgvector +      |
| ulars, product T&Cs |    | (nightly + on     |    | keyword index   |
| (versioned)         |    |  document change) |    | metadata: ver,  |
+---------------------+    +-------------------+    | effective date, |
                                                    | classification  |
                                                    +--------+--------+
                                                             |
+--------+   JWT   +-------------+  filter by role  +--------v--------+
| Staff  |-------->| RAG API     |----------------->| Retrieve+rerank |
| portal |         | (Spring)    |                  +--------+--------+
+---+----+         +------+------+                           |
    ^                     |        prompt + sources          v
    |                     +------------------------>  +------------+
    |   answer + citations (doc, section, version)    | LLM        |
    +-------------------------------------------------+------------+
                          |
                          v
                 +-------------------+
                 | Audit log: user,  |
                 | question, sources,|
                 | model, answer     |
                 +-------------------+
```

| Concern | Design decision |
|---|---|
| Correctness | Only retrieve documents effective on the query date; cite doc, section, version |
| Access control | Filter at retrieval time by the user's entitlements; never rely on the prompt to hide data |
| Freshness | Re-ingest on document change; delete superseded versions from the index |
| Compliance | Audit log of question, retrieved chunks, model, and answer; retention per policy |
| Safety | The assistant informs staff; credit decisions stay with the decision engine and humans |
| Fallback | If no chunk passes the threshold, answer "not found in policy" and route to a human |

> **Key Takeaway:** In BFSI, RAG is an *advisory* layer with strict access control, versioned sources, and an audit trail, not an autonomous decision-maker.

---

## ⚠ Common Mistakes

| Wrong approach | Problem | Correct approach |
|---|---|---|
| Fixed 2,000-token chunks across all docs | Chunks mix topics; retrieval is noisy | Structure-aware chunking (by heading/clause), tune size with evaluation |
| Very tiny chunks | Lose context; answers fragmentary | Moderate size, overlap, optionally attach parent section |
| Ignoring metadata and access control | A user retrieves documents they must not see | Filter by entitlement inside the retrieval query |
| Trusting top-k blindly | Irrelevant chunks make the model confident and wrong | Similarity threshold, reranker, and a "no answer" path |
| Vector-only search | Misses exact identifiers (clause 4.2(b), product codes) | Hybrid search (BM25 + vector) |
| Changing the embedding model without re-indexing | Query and document vectors are incompatible | Re-embed everything; version the index |
| No evaluation set | Quality regressions go unnoticed | Maintain a golden question set and track retrieval and answer metrics |
| Stuffing 30 chunks into the prompt | Cost, latency, and distraction | Retrieve wide, rerank, send the best few |
| Treating retrieved text as trusted | Prompt injection hidden inside documents | Treat context as data; restrict tools; filter outputs |
| Stale index | Answers cite withdrawn policies | Automate re-ingestion and deletion of superseded versions |

```
Wrong:   embed whole PDFs in 2,000-token chunks, vector search only, no filter
   |
   v
Impact:  wrong clause retrieved, confident hallucination, data leakage across teams
   |
   v
Right:   structure-aware chunks + metadata + hybrid search + rerank + ACL filter + eval set
```

> **Key Takeaway:** Most RAG failures are retrieval failures. Debug what was retrieved before blaming the model.

---

## ✅ Best Practices

**Data and ingestion**
- Clean and structure documents first; poor parsing of tables and headers hurts everything downstream
- Store rich metadata: source, version, effective date, owner, classification, access group
- Make ingestion idempotent and incremental; track a content hash per document
- Remove or flag superseded versions

**Retrieval**
- Use hybrid search for enterprise text full of identifiers
- Apply a similarity threshold and a reranker; retrieve wide (top 20–50), keep narrow (top 3–5)
- Apply access filters *in the query*, not after the model responds

**Generation**
- Instruct the model to answer only from context and to say when the context is insufficient
- Require citations and display them to the user
- Keep temperature low for factual answers

**Quality and operations**
- Build a golden dataset of real questions with expected sources; run it on every change
- Log question, retrieved chunk IDs, scores, prompt size, latency, and cost (redact PII)
- Monitor thumbs-down feedback and "no answer" rates

**Security**
- Validate the user's identity server-side; derive entitlements from the token
- Never concatenate raw user input into filter expressions or SQL
- Treat retrieved documents as untrusted input; guard against prompt injection
- Control data residency and what is sent to external model providers

**Thread safety and scalability**
- `ChatClient` and `VectorStore` beans are shared across requests; keep request data out of fields
- Batch embedding calls during ingestion; apply rate limits and retries with backoff

> **Key Takeaway:** Measure, filter, cite, and log. Those four habits separate a demo from a production RAG system.

---

## ⚡ Performance Considerations

| Area | Consideration | Recommendation |
|---|---|---|
| Latency | Embedding + search + rerank + LLM generation all add up; the LLM dominates | Stream the response; cache embeddings; set per-stage timeouts |
| Cost | Tokens in the prompt and the output are billed | Send fewer, better chunks; cache frequent answers |
| Vector search | Exact search is O(n); ANN indexes trade recall for speed | HNSW or IVF; tune index parameters (for example `ef_search`) against a recall target |
| Memory | HNSW keeps a graph in memory; embeddings use `dimensions × 4` bytes per float | Choose smaller embedding dimensions where quality allows; consider quantisation |
| Ingestion throughput | Embedding millions of chunks is slow | Batch requests, parallelise with bounded concurrency, resume on failure |
| Reranking | Cross-encoder cost grows with candidates | Rerank only the top 20–50 |
| Network | Calls to external model APIs | Connection pooling, timeouts, circuit breaker, regional endpoints |
| Thread usage | Blocking calls to embedding and LLM APIs | Bulkhead pools or virtual threads (Java 21) for I/O-bound work |
| Caching | Many users ask similar questions | Semantic or exact-match cache with TTL, invalidated on re-ingestion |

> **Key Takeaway:** Retrieval is cheap compared to generation. Spend effort on relevance so the model needs fewer tokens to get it right.

---

## ⚖ Trade-offs

| Advantages | Disadvantages |
|---|---|
| Fresh, private, citable knowledge | Quality depends on retrieval and data hygiene |
| No model retraining for new documents | Extra moving parts (index, embeddings, reranker) to operate |
| Access control can be enforced at retrieval | Added latency compared to a bare LLM call |
| Reduces hallucination when context is good | Can still hallucinate or ignore context |
| Works with any LLM | Multi-hop and aggregate questions are weak with plain top-k |

**When to use:** knowledge-heavy Q&A over changing or private documents.
**When NOT to use:** pure reasoning or transformation tasks, tiny static knowledge that fits in the prompt, or decisions that need deterministic, auditable rules.

**Real-life analogy:** An open-book exam. The student (LLM) is capable but cannot memorise everything, so a librarian (retriever) brings the right pages. If the librarian fetches the wrong pages, even a brilliant student writes a confident wrong answer.

---

## 📊 Comparison Table

| Approach | Advantages | Disadvantages | Performance | Best use case |
|---|---|---|---|---|
| **Naive RAG** | Simple, fast to build | Noisy retrieval, no rewriting or reranking | Lowest latency | Prototypes, small clean corpora |
| **Advanced RAG** (rewrite, hybrid, rerank) | Much better relevance | More components and latency | Moderate | Most enterprise Q&A |
| **Agentic RAG** | Model plans searches and tools, handles multi-step questions | Harder to control, cost and latency variable | Highest latency | Research-style, multi-source questions |
| **GraphRAG** | Captures relationships across entities | Costly to build and maintain graph | Heavy ingestion | Questions about connections (ownership, dependencies) |
| **Fine-tuning** | Teaches style, format, domain behaviour | Stale facts, no citations, retraining cost | Fast inference | Consistent tone or task format |
| **Long-context stuffing** | No retrieval system needed | Token cost, latency, attention dilution | Slow and expensive at scale | One-off analysis of a few documents |

**Evaluation metrics to know**

| Layer | Metric | Meaning |
|---|---|---|
| Retrieval | Recall@k | Did the needed chunk appear in the top k? |
| Retrieval | MRR / nDCG | How high was the right chunk ranked? |
| Generation | Faithfulness (groundedness) | Is every claim supported by the retrieved context? |
| Generation | Answer relevance | Does the answer address the question? |

---

## 🎯 Interview Questions

**Q1. What is RAG and why use it instead of just calling an LLM?**
*Expected:* RAG retrieves relevant external content at query time and puts it in the prompt so the model answers from evidence. It addresses knowledge cut-off, hallucination, private data, and citations, without retraining.
*Follow-up:* What would make you choose fine-tuning instead?

**Q2. Walk me through a RAG architecture end to end.**
*Expected:* Ingestion (parse, chunk, embed, store with metadata) and query (rewrite, embed, retrieve with filters, rerank, build prompt, generate, cite, log). Mention evaluation and guardrails.
*Follow-up:* Which stage do you monitor first when quality drops?

**Q3. What are embeddings and how does similarity search work?**
*Expected:* Embeddings map text to vectors so semantically similar text is close. Search finds nearest vectors by cosine, dot product, or L2, usually via an approximate index.
*Follow-up:* Why must query and documents use the same embedding model?

**Q4. How do you choose chunk size and overlap?**
*Expected:* There is no universal value. Start with a few hundred to about a thousand tokens and 10–20% overlap, prefer structure-aware splitting, then tune using retrieval metrics on a golden set. Too big dilutes relevance, too small loses context.
*Follow-up:* How would you handle tables and numbered clauses?

**Q5. Vector search versus keyword search: when do you need hybrid?**
*Expected:* Vector search handles paraphrase; keyword search handles exact terms, codes, and identifiers. Hybrid with BM25 plus vectors and rank fusion (RRF) covers both.
*Follow-up:* How does Reciprocal Rank Fusion combine two rankings?

**Q6. What is reranking and why add it?**
*Expected:* A cross-encoder scores query and passage together, which is more accurate than comparing precomputed vectors, but slower. Use it on the top 20–50 candidates to select the best few.
*Follow-up:* What is the latency trade-off?

**Q7. What is HNSW and how does it differ from IVF?**
*Expected:* HNSW is a layered proximity graph with high recall and fast queries but higher memory. IVF clusters vectors and searches the nearest clusters, using less memory but needing tuning of probes. Both are approximate.
*Follow-up:* What happens to recall if you lower the search breadth?

**Q8. How do you reduce hallucination in a RAG system?**
*Expected:* Better retrieval, similarity thresholds, reranking, grounded prompts ("only use the context, otherwise say you do not know"), citations, low temperature, groundedness checks, and a human fallback.
*Follow-up:* How do you detect an unsupported claim automatically?

**Q9. How do you evaluate a RAG system?**
*Expected:* Separate retrieval (recall@k, MRR, nDCG) from generation (faithfulness, answer relevance). Maintain a golden question set, use LLM-assisted and human review, and track online feedback. Frameworks such as RAGAS help.
*Follow-up:* How do you stop an LLM judge from being biased?

**Q10. How do you enforce document-level security in RAG?**
*Expected:* Store access metadata with each chunk and apply filters in the retrieval query using entitlements from the authenticated user. Never rely on the prompt to hide content, and never filter after the model has seen the data.
*Follow-up:* How do you keep ACL metadata in sync when permissions change?

**Q11. What is prompt injection in the context of RAG?**
*Expected:* Malicious instructions hidden in retrieved documents or user input try to override the system prompt or exfiltrate data. Mitigate with input and output filtering, least-privilege tools, separating instructions from data, and monitoring.
*Follow-up:* Why is indirect injection harder to catch than direct?

**Q12. How do you keep the index fresh?**
*Expected:* Incremental ingestion triggered by document change events, content hashes, deletion of superseded versions, and re-embedding when the model changes. Version the index.
*Follow-up:* How do you roll out a new embedding model with no downtime?

**Q13. How would you handle multi-turn conversations in RAG?**
*Expected:* Rewrite the follow-up into a standalone query using chat history before retrieval, and keep a bounded conversation memory.
*Follow-up:* What can go wrong when the rewrite drops a key detail?

**Q14. When is RAG the wrong choice?**
*Expected:* Tasks that need reasoning without external facts, very small static knowledge, deterministic rule-based decisions, or cases requiring exact aggregation (use SQL or tools).
*Follow-up:* How would you answer "total exposure to sector X" with an LLM safely?

**Q15. Explain the Spring AI pieces you would use to build RAG in Java.**
*Expected:* `DocumentReader` and `TokenTextSplitter` for ingestion, `EmbeddingModel` and `VectorStore` for storage, `ChatClient` with `QuestionAnswerAdvisor` for naive RAG or `RetrievalAugmentationAdvisor` for modular RAG, with filter expressions for access control.
*Follow-up:* How would you test this without calling a paid model?

> **Key Takeaway:** Be ready to move between concepts (embeddings, ANN, evaluation) and implementation (filters, advisors, schema).

---

## 🧠 Scenario-Based Questions

**1. Users complain the assistant gives outdated policy answers. What do you check?**
Confirm the retrieved chunks and their version metadata. Likely causes: superseded documents still indexed, ingestion not triggered on update, or no effective-date filter. Fix the ingestion pipeline, delete old versions, and filter by effective date.

**2. The model answers confidently but the citation does not support the claim. How do you troubleshoot?**
Inspect the prompt actually sent: was the right chunk included, truncated, or buried among many chunks? Check chunking (answer split across two chunks), reduce the number of chunks, strengthen the grounding instruction, and add a groundedness check.

**3. A junior analyst can see HR documents through the assistant. What went wrong and how do you fix it?**
Access control was missing or applied after retrieval. Add access-group metadata to every chunk, enforce a filter built from the authenticated user's entitlements inside the retrieval query, add tests with users of different roles, and audit past queries.

**4. Retrieval latency jumped from 80 ms to 900 ms after the corpus grew. How do you investigate?**
Check whether the ANN index is being used (query plan), the index build parameters, memory pressure causing disk reads, and filter selectivity (a restrictive filter can force poor index use). Consider partitioning by tenant or department, tuning the index, or adding pre-filter columns.

**5. Design RAG for 5 million documents across regions with data-residency rules.**
Regional indexes with region-tagged ingestion, regional model endpoints, a routing layer that sends each user to their region, a shared evaluation harness, and per-region audit logs. Avoid copying restricted data across regions.

**6. The question is "list all loans above ₹1 crore approved last quarter." Should you use RAG?**
No. This is a structured aggregate query. Use SQL or a tool call against the system of record, and use the LLM only to interpret the question or explain the result.

---

## 🛠 Debugging Tips

**Always inspect, in this order: question → rewritten query → retrieved chunks and scores → final prompt → answer.**

| Symptom | Likely cause | What to check |
|---|---|---|
| Irrelevant answers | Poor retrieval | Top-k chunks and similarity scores; chunk quality |
| "I don't know" too often | Threshold too strict or no relevant chunk indexed | Lower threshold carefully, verify the document is ingested |
| Hallucinated detail | Context missing or ignored | Prompt contents, chunk boundaries, grounding instruction |
| Right document, wrong section | Chunks too large or split badly | Chunking strategy, add headings to chunk text |
| Exact code or clause not found | Vector-only search | Add keyword search (hybrid) |
| Results change after deploy | Embedding model or index changed | Index and model versions; re-embed consistently |
| Slow responses | Large prompt, slow rerank, cold index | Stage timings, token counts, index usage |
| Cost spike | Too many chunks, no caching | Prompt size per request, cache hit rate |
| Dimension mismatch error | Embedding dimension differs from column | Table `VECTOR(n)` versus the model output size |

**Logging checklist (with PII redacted):** request ID, user role, original and rewritten query, retrieved chunk IDs and scores, filters applied, prompt token count, model and version, latency per stage, answer, user feedback.

**Technique:** keep a replay tool that re-runs a logged question against the current index and prints retrieved chunks side by side, so retrieval regressions are visible immediately.

> **Key Takeaway:** If the right chunk is not in the prompt, no prompt tweak will fix it. Fix retrieval first.

---

## 📌 When to Use RAG

- Internal knowledge assistants over policies, SOPs, runbooks, and product documentation
- Customer-support answers grounded in current terms and FAQs
- Regulatory and compliance lookups that require citations
- Developer assistants over internal code, APIs, and incident history
- Summaries and answers over contracts and reports that change often
- Any scenario where knowledge changes faster than you can retrain a model

## 🚫 When NOT to Use RAG

- Exact numeric aggregation or reporting (use SQL, BI, or tools)
- Deterministic business rules and decisions that must be reproducible (use a rules engine)
- A small, static knowledge set that fits comfortably in the prompt
- Tasks that need no external facts (translation, formatting, reasoning exercises)
- Sources of poor quality that you are not willing to clean and maintain

---

## 💡 Interview Tips

**What the interviewer expects**
- A clear two-pipeline mental model (ingestion and query)
- Understanding of chunking, embeddings, ANN indexes, hybrid search, and reranking
- Awareness of evaluation, security, and operations, not only the "happy path"
- Ability to implement it in Java and Spring, and to talk about trade-offs

**Common mistakes**
- Describing RAG as "put documents in a vector database" and stopping there
- Ignoring access control and data privacy
- Claiming RAG eliminates hallucination
- Not knowing how to measure quality

**How to answer confidently**
- Draw the two pipelines and name one failure mode and one metric for each stage
- Give a concrete number-backed example: chunk size tried, recall@5 before and after adding hybrid search
- Tie it to BFSI constraints: citations, audit logs, entitlements, human fallback

**Likely follow-ups:** choosing the embedding model, HNSW tuning, evaluation methodology, prompt injection, multi-tenant isolation, RAG versus fine-tuning versus long context, cost control.

---

## 📝 Short Conclusion Summary

Retrieval Augmented Generation connects a language model to your own knowledge. An offline pipeline parses, chunks, embeds, and indexes documents with rich metadata; an online pipeline rewrites the question, retrieves candidates using vector and keyword search under the user's access filters, reranks them, and sends the best passages to the model with instructions to answer only from that evidence and cite it. Quality is dominated by retrieval, so success depends on good chunking, hybrid search, reranking, a golden evaluation set, and strict security controls. In enterprise settings RAG is an advisory layer that is fresh, auditable, and updateable without retraining.

---

## ⚡ TL;DR

| Topic | Summary |
|---|---|
| Purpose | Ground LLM answers in current, private, citable documents |
| Internal Working | Ingest: parse → chunk → embed → store. Query: rewrite → retrieve (hybrid + filter) → rerank → prompt → generate → cite |
| Best For | Knowledge Q&A over changing or private content, compliance lookups, support |
| Avoid | Aggregations, deterministic decisions, tiny static knowledge |
| Advantages | Fresh data, citations, access control, no retraining |
| Disadvantages | Retrieval quality dependency, more components, added latency, can still hallucinate |
| Performance | ANN index tuning, retrieve wide then rerank narrow, cache, stream, control tokens |
| Interview Keyword | Embeddings, chunking, HNSW, hybrid search, RRF, reranker, faithfulness, ACL filter |

---

## 🚀 30-Second Interview Answer

> "RAG lets an LLM answer from our own documents instead of relying only on what it memorised. Offline, we parse and chunk documents, embed the chunks, and store them in a vector database such as pgvector with metadata like version and access group. At query time we rewrite the question if needed, run a hybrid vector and keyword search with an entitlement filter, rerank the candidates, and send the top few chunks to the model with an instruction to answer only from that context and cite it. Retrieval quality drives answer quality, so I measure recall@k and faithfulness on a golden question set, and I treat security seriously: access filters in the query, prompt-injection defences, and audit logs. In Java I would build it with Spring AI using a VectorStore, a ChatClient, and the RAG advisors."

---

## 📚 Further Reading

- Lewis et al., *Retrieval-Augmented Generation for Knowledge-Intensive NLP Tasks* (2020)
- Spring AI reference: Retrieval Augmented Generation (docs.spring.io/spring-ai)
- pgvector documentation (GitHub: pgvector/pgvector) for index types and operators
- RAGAS documentation for RAG evaluation metrics
- Anthropic engineering blog: Contextual Retrieval
