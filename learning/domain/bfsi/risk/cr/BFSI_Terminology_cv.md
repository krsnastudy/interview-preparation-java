# 🏦 BFSI Terminology From Your Resume - Explained for Interview Preparation

**How the terms are grouped :**

| Part | Project / Employer | Domain |
|---|---|---|
| A | BFSI umbrella terms | Whole resume |
| B | Morgan Stanley - Credit Risk, Loan Stress, CECL & CCAR | Credit risk + US regulatory reporting |
| C | Danske Bank - ANS Monitoring & Screening | Anti-Financial Crime (AML, sanctions, fraud, ABC) |
| D | Credit Suisse - CDT / CDS credit risk platform | Investment-banking counterparty credit risk |
| E | Societe Generale - Wealth & online stockbroking support | Retail investing products |
| F | Engineering terms that interviewers attach to BFSI | Cross-cutting |
| G | Resume analysis: what interviewers will probe | Your profile |
| H | Quick glossary + resources | Revision |

---

# PART A - BFSI Umbrella Terms

### 1. BFSI
**Meaning:** **B**anking, **F**inancial **S**ervices and **I**nsurance. Banks (lending, deposits), financial services (investment banking, brokerage, asset/wealth management, payments) and insurers.  
**Real-life example:** Your salary account (bank), your mutual fund/stock account (financial services), your health policy (insurance) are all BFSI customers' touchpoints.  
**In your resume:** Summary line, plus Morgan Stanley, Danske, Credit Suisse, Societe Generale.  
**Interview angle:** *"What makes BFSI engineering different?"* -> correctness of money, regulation, auditability, security, high availability, and data lineage. Say you treat "an incorrect number" as worse than "a slow response".  

### 2. Investment Banking (IB) vs Retail Banking vs Wholesale Banking
**Meaning:** *Retail* = individuals (accounts, cards, loans). *Wholesale/Corporate* = companies and institutions (credit lines, trade finance). *Investment banking* = capital markets: underwriting, M&A advice, trading, derivatives, prime brokerage.  
**Real-life example:** A salaried person's home loan is retail. A company raising 500 crore through a bond issue, and hedging currency risk with a forward, is investment/corporate banking.  
**In your resume:** Credit Suisse **Investment Banking** credit-risk platform, Morgan Stanley (IB + wealth).  
**Interview angle:** Explain that IB credit risk is dominated by **counterparty exposure on trades** (uncertain, market-driven), unlike retail credit risk which is loan balances and delinquency.  

### 3. Wealth Management / Stockbroking
**Meaning:** Services to invest and manage customers' money. **Stockbroking** = executing buy/sell orders in securities; **wealth management** adds advice, portfolio management, pensions and tax wrappers.  
**Real-life example:** You place an order to buy 100 shares via a broker app; a wealth manager builds your retirement portfolio.  
**In your resume:** Societe Generale wealth management & online stockbroking (ISA, SIPP, CFD, spread betting).  

### 4. Regulator and Regulatory Compliance
**Meaning:** Regulators set and police rules (US: Federal Reserve, OCC, SEC, FinCEN, OFAC; UK: FCA, PRA; EU: ECB/EBA; India: RBI, SEBI). **Compliance** = proving the bank follows them.  
**Real-life example:** The Federal Reserve requires large US banks to show they can survive a severe recession (CCAR). A bank that cannot produce accurate numbers on time is breaching its obligation even if it is actually healthy.  
**In your resume:** "compliance-critical microservices", "regulatory reporting", "translated complex compliance rules into service logic".  
**Interview angle:** *"What does compliance-critical mean for your code?"* -> traceability, versioned rules/models, reproducible runs, evidence for audits, tests for every rule, strict change control.  

### 5. Risk Management (the "Three Lines of Defence")
**Meaning:** 1st line = business/IT owning risk day to day; 2nd line = risk & compliance oversight; 3rd line = internal audit.  
**Real-life example:** A developer builds a limit check (1st line), the risk team validates it (2nd), audit samples it (3rd).  
**Interview angle:** Position yourself as a 1st-line engineer who builds controls that 2nd/3rd lines can verify.  

---

# PART B - Morgan Stanley: Credit Risk, Loan Stress, CECL & CCAR

### 6. Credit Risk
**Meaning:** The risk that a borrower/counterparty won't pay as agreed, causing loss.  
**Real-life example:** Bank lends 10,00,000; borrower stops paying after 6 months; the unpaid balance net of recovery is the loss.  
**In your resume:** "Credit Risk - Loan Stress", Credit Suisse "Credit Risk Management Platform".  
**Interview angle:** Know **PD, LGD, EAD, EL** (see terms below) - they underpin CECL, CCAR and exposure calculations.  

### 7. PD, LGD, EAD, Expected Loss (EL)
**Meaning:**
- **PD** Probability of Default; **LGD** Loss Given Default (% lost after recoveries); **EAD** Exposure At Default (amount owed at default).  
- **EL = PD x LGD x EAD.**  

**Real-life example:** PD 2%, LGD 45%, EAD 10,00,000 -> EL = 0.02 x 0.45 x 10,00,000 = **9,000**.  
**In your resume:** The loss-forecasting models behind CECL/CCAR consume these parameters (and your services moved/processed them).  
**Interview angle:** *"Which input would you validate first if the loss number looks wrong?"* -> exposure (EAD) data completeness, then PD/LGD parameter versions and scenario inputs.  

### 8. Loan Stress / Stress Testing
**Meaning:** Projecting how a loan portfolio performs under severe but plausible scenarios (recession, unemployment spike, house-price fall) to estimate losses and capital impact.  
**Real-life example:** Baseline: unemployment 4%, expected portfolio loss 2.4% (24,000 on 10,00,000). Severe scenario: unemployment 10%, loss 5% (**50,000**). The extra 26,000 is what the stress test makes the bank prepare for.  
**In your resume:** "Loan Stress" project title.  
**Interview angle:** *"How do stress tests differ from normal reporting?"* -> forward-looking, scenario-driven, run for many scenarios/time horizons (so compute-heavy and reproducibility-critical). This is where your **multithreading (+40% throughput)** story fits.  

### 9. CECL (Current Expected Credit Losses)
**Meaning:** US GAAP accounting standard (ASC 326) requiring banks to record, **from day one**, an allowance for the **lifetime expected credit losses** of loans, replacing the older "incurred loss" model (loss recognised only when it was probable).  
**Real-life example:** Bank originates a 5-year auto-loan pool of 10,00,000. Lifetime loss rate estimated at 2.4% -> records an allowance of **24,000** immediately. If the economic forecast worsens and the rate rises to 3.0%, allowance becomes 30,000 -> a **6,000 charge ("reserve build")** hits earnings.  
**In your resume:** "microservices supporting CECL ... regulatory reporting".  
**Interview angle:** Difference from IFRS 9 (international): IFRS 9 has **3 stages** (12-month vs lifetime ECL), CECL has **no stages** - lifetime from day one. CECL uses historical experience + current conditions + **reasonable and supportable forecasts** (then reverts to history).  

### 10. Allowance for Credit Losses (ACL) / Provision / Reserve build & release
**Meaning:** The balance-sheet reserve for expected losses; the P&L charge for changing it is the *provision for credit losses*. Increasing it = **build**; decreasing it = **release**.  
**Real-life example:** Allowance goes from 24,000 to 30,000 -> build of 6,000 (expense). If economy improves and it drops to 26,000 -> release of 4,000 (income).  
**Interview angle:** Explain that scenario changes (macro forecasts) can swing earnings without any actual default - so **data and model versioning matter**.  

### 11. CCAR (Comprehensive Capital Analysis and Review)
**Meaning:** The Federal Reserve's annual exercise assessing whether large US banks hold enough capital to keep lending through a severe downturn, and whether their capital planning is sound. Banks submit projections (losses, revenue, capital) under Fed-supplied scenarios.  
**Real-life example:** The Fed says: "Assume GDP falls 6%, unemployment rises to 10%, house prices drop 25%." The bank must show its capital ratios over the next **9 quarters** stay above minimums.  
**In your resume:** "CCAR ... US banking compliance", "directly impacting Federal Reserve stress testing processes".  
**Interview angle:** Be able to say what your system fed into: loan-level data -> loss projections -> capital reporting. Note rules have evolved (stress results now drive the **Stress Capital Buffer**); verify the current format.  

### 12. DFAST (Dodd-Frank Act Stress Tests)
**Meaning:** The stress-test requirement from the Dodd-Frank Act; the Fed runs a *supervisory* stress test and banks also run company-run tests. CCAR and DFAST overlap in scenarios and data; CCAR additionally covers capital planning/actions.  
**Interview angle:** "CCAR = capital plan adequacy; DFAST = stress-loss projections - same data engine feeds both."  

### 13. Macroeconomic Scenarios (Baseline / Adverse / Severely Adverse)
**Meaning:** Paths of variables (GDP, unemployment, interest rates, house prices, equity prices) over the projection horizon. The Fed publishes **baseline** and **severely adverse** (earlier cycles also had "adverse").  
**Real-life example:** In the severely adverse path, unemployment rises from 4% to 10% over 6 quarters; loss models convert this into higher PD/LGD per loan.  
**Interview angle:** Engineering consequences: scenarios are **versioned inputs**, runs must be reproducible per scenario/version, parallelisable per portfolio.  

### 14. Stress Capital Buffer (SCB), CET1 and Capital Ratios
**Meaning:** **CET1** = highest-quality capital (common equity). The Fed's stress-test results set a bank-specific buffer (**SCB**, minimum 2.5%) above regulatory minimums.  
**Real-life example:** If stress test shows CET1 falling by 3.2 percentage points in the worst quarter, that drawdown (plus planned dividends) is reflected in the buffer requirement.  
**Interview angle:** You don't compute capital rules by hand, but you must know why numbers are sensitive: wrong loss -> wrong buffer -> real money (dividends, buybacks).  

### 15. FR Y-14 (Fed data collection for stress testing)
**Meaning:** Detailed regulatory data submissions: **Y-14M** (monthly loan-level), **Y-14Q** (quarterly), **Y-14A** (annual summary/projections).  
**Real-life example:** Each mortgage's balance, rate, delinquency status, LTV and credit score reported at loan level.  
**Interview angle:** Emphasise **data quality and reconciliation** (loan-level to GL totals), validations before submission, late-file handling.  

### 16. Loan-level Segmentation / Pooling
**Meaning:** Grouping loans with similar risk (product, vintage, score band, region) so loss rates can be estimated for each pool.  
**Real-life example:** "Prime auto 2021 vintage" and "Subprime credit card" are separate pools with different loss curves.  
**Interview angle:** A mis-assigned loan changes the loss rate applied -> design **mapping rules** as versioned, testable reference data.  

### 17. Model Risk Management (SR 11-7) and Model Governance
**Meaning:** US supervisory guidance on developing, validating and controlling models. Models need documentation, independent validation, monitoring and controlled change.  
**Real-life example:** A change to a loss-forecast formula goes through development -> validation -> approval -> production release; you can't silently edit it.  
**Interview angle:** *"How did you ensure model changes didn't break results?"* -> parallel runs, regression packs, tolerance-based reconciliation, version tagging, rollback.  

### 18. Regulatory Reporting / Audit Trail / Data Lineage
**Meaning:** Producing official numbers on time, with proof of how each number was derived (inputs, model/rule versions, who changed what).  
**Real-life example:** Auditor asks: "Why was Q2 allowance 24,000?" -> you replay the run with stored inputs and show the same result.  
**Interview angle:** Say "same input + same version = same output" and describe how you stored snapshots/run metadata.  

---

# PART C - Danske Bank: ANS Monitoring & Screening (Anti-Financial Crime)

### 19. Anti-Financial Crime (AFC / FCC)
**Meaning:** Umbrella for controls against **money laundering, terrorist financing, sanctions breaches, fraud, and bribery & corruption** - exactly the categories in your resume.  
**Interview angle:** Name all four categories and give a one-line system example for each (below).  

### 20. AML (Anti-Money Laundering)
**Meaning:** Preventing criminals from disguising illegal money as legitimate. Classic stages:  **Placement** (get cash into the system), **Layering** (move it around to hide origin), **Integration** (use it as clean money).
**Real-life example:** Drug cash is deposited in small amounts (placement), wired through shell companies in several countries (layering), then used to buy property (integration).  
**Interview angle:** *"Where does your platform detect this?"* -> transaction/behaviour monitoring (placement & layering patterns), screening (known bad parties), case escalation.  

### 21. CFT (Counter-Terrorist Financing)
**Meaning:** Stopping funds from reaching terrorist groups. Amounts are often **small and from legitimate sources**, so volume-based rules alone miss them; screening and network/behaviour signals matter.  
**Real-life example:** Repeated small transfers to a high-risk region by multiple unrelated customers to the same beneficiary.  

### 22. Sanctions & Sanctions Screening
**Meaning:** Legal restrictions on dealing with named people, entities, vessels or countries (lists from **OFAC** US, **EU**, **UN**, **UK HMT/OFSI**). **Screening** compares customers, counterparties and payments against these lists in real time/batch.  
**Real-life example:** A payment to "Mohamad Al-Hassan Trading LLC" is screened; the list contains "Mohammed Alhasan Trading Co." -> a **potential match** raised for review. Paying a true match breaches sanctions and can cost huge fines.  
**In your resume:** "Sanctions Breaches ... screening".  
**Interview angle:** *"Real-time vs batch screening?"* -> payments are screened **before release (real-time, low latency)**; customer base is rescreened in **batch** when lists change.  

### 23. Watchlist / Name Matching / Fuzzy Matching
**Meaning:** Matching names despite spelling, transliteration and ordering differences using algorithms (edit distance, phonetic codes like Soundex/Metaphone, token matching) plus a **score threshold**.  
**Real-life example:** "Ivan Petrov" vs "Ivan Petrof" vs "Petrov, Ivan A." -> score 92% -> above threshold -> alert.  
**Interview angle:** Trade-off: **low threshold = too many false positives (cost), high threshold = missed true matches (regulatory risk)**. Mention secondary identifiers (DOB, country, ID numbers) to reduce noise.  

### 24. Alert, True Positive, False Positive (and tuning)
**Meaning:** An **alert** is a system flag for review. **True positive** = genuinely suspicious; **false positive** = harmless. Commonly, the vast majority of AML alerts are false positives, so **tuning** thresholds and rules is a continuous activity (with documented testing such as above-the-line / below-the-line analysis).  
**Real-life example:** A shop owner's large cash deposits trigger an alert; analysis shows seasonal festival sales -> false positive; rule refined with peer-group context.  
**Interview angle:** *"How do you reduce false positives safely?"* -> segmentation, better data, risk-based thresholds, back-testing, approvals - never "just raise the threshold" without evidence.  

### 25. KYC, CDD and EDD
**Meaning:** **KYC** Know Your Customer; **CDD** Customer Due Diligence (identify and verify customer, ultimate beneficial owner, purpose of account, ongoing monitoring); **EDD** Enhanced Due Diligence for high-risk customers (extra documents, senior approval).  
**Real-life example:** A normal salaried customer gets standard CDD; a politically exposed person or a cash-intensive business gets EDD and more frequent reviews.  
**Interview angle:** Customer risk rating drives monitoring intensity - a **risk-based approach**.  

### 26. PEP (Politically Exposed Person) and UBO (Ultimate Beneficial Owner)
**Meaning:** **PEP** = holds/has held a prominent public role (higher corruption risk, plus family/close associates). **UBO** = the real person(s) who own/control a company (often 25%+ ownership, varies by law).  
**Real-life example:** A company's shareholders are three shell entities; tracing through reveals one individual - who is a minister's relative (PEP) -> EDD.  

### 27. Transaction Monitoring and Typologies (Structuring / Smurfing)
**Meaning:** Rules/models scanning transactions for patterns ("typologies") of financial crime. **Structuring/smurfing** = splitting cash into amounts below a reporting threshold (e.g., US $10,000 CTR).  
**Real-life example:** Customer who normally deposits $2,000/month makes 9 deposits of $9,500 across branches in one day (total $85,500) -> structuring alert.  
**Interview angle:** Explain rules (thresholds, velocity, geography) + statistical/behavioural models; mention **data volume, windowed aggregations, idempotent alert generation**.  

### 28. Customer Behaviour Monitoring / Peer-group Analysis / Statistical Models
**Meaning:** Compare a customer's activity with **their own history** and **similar customers (peer group)**; models (anomaly detection, scoring) flag outliers.  
**Real-life example:** A small bakery suddenly receives international wires 50x its peer group average.  
**In your resume:** "customer behaviour monitoring, statistical model integration".  
**Interview angle:** *"How did you integrate statistical models into Java services?"* -> model service behind a stable API / versioned artifacts, feature data contracts, fallbacks, monitoring of drift and latency.  

### 29. Case Management and Escalation Workflow
**Meaning:** Alerts become **cases** assigned to analysts: triage -> investigation -> decision -> escalation -> closure with documented rationale; SLAs and four-eyes approval.  
**Real-life example:** L1 analyst reviews alert, adds notes, escalates to L2; the **MLRO** (Money Laundering Reporting Officer) decides to file a report or close.  
**In your resume:** "case escalation workflows".  
**Interview angle:** Workflow/state-machine design, audit of every state change, SLA timers, reassignments, evidence attachments, **immutable history**.  

### 30. SAR / STR and FIU
**Meaning:** **Suspicious Activity/Transaction Report** filed with the **Financial Intelligence Unit** (e.g., FinCEN in US, NCA in UK, FIU-IND in India) when suspicion arises. **Tipping off** (alerting the customer) is itself an offence.  
**Interview angle:** Systems must restrict who sees SAR status and never leak it to customer-facing channels.  

### 31. Fraud
**Meaning:** Deception for financial gain: card fraud, account takeover, authorised push-payment scams, insider fraud. Typically **real-time detection** and customer impact, unlike AML which is often investigative and regulatory.  
**Real-life example:** Card used in Bengaluru at 2pm and in Berlin at 2:10pm -> blocked/step-up authentication.  
**Interview angle:** Distinguish **fraud (victim loses)** vs **money laundering (criminal hides proceeds)** - different latency, models and reporting.  

### 32. Bribery & Corruption (ABC)
**Meaning:** Preventing improper payments to officials/third parties (laws: US **FCPA**, **UK Bribery Act**). Controls: screening of gifts/payments, third-party due diligence, monitoring unusual payments.  
**Real-life example:** Unusual large "consultancy fees" paid to an intermediary of a government official before a contract award.  

### 33. FATF and Regulatory Expectations
**Meaning:** The **Financial Action Task Force** sets global AML/CFT standards (the 40 Recommendations); countries implement them through national laws.  
**Interview angle:** Mention that AFC systems are judged on **effectiveness** (do they find real crime) not just existence of rules.  

---

# PART D - Credit Suisse: CDT / CDS Credit Risk Platform

> Your longest tenure (2013-2021). Expect the deepest questions here.

### 34. Counterparty & Counterparty Credit Risk (CCR)
**Meaning:** A counterparty is the other party to a trade/contract. **CCR** = risk it defaults **before the trade settles/matures**, when the bank is owed money.
**Real-life example:** The bank has an interest-rate swap with a corporate. Today the swap is worth +5 to the bank (corporate owes it). If the corporate defaults now, bank loses up to 5 (less collateral/recoveries).
**In your resume:** "trade, counterparty, collateral, and credit rating data", "exposure calculation".
**Interview angle:** Key difference from loans: exposure is **uncertain and moves with markets** and can be zero or large.

### 35. Trade Data and Trade Lifecycle (Front/Middle/Back Office)
**Meaning:** Lifecycle: **front office** (trader executes/books), **middle office** (risk, P&L control, confirmations), **back office** (settlement, payments, reconciliations). Upstream systems feed your platform with trades.
**Real-life example:** Trader books an FX forward -> confirmation sent -> credit system receives the trade -> exposure updated -> settlement at maturity.
**Interview angle:** Know that data arrives from **multiple upstream systems with different formats and timing**; reconciliation and late/amended trades are the real work.

### 36. Mark-to-Market (MtM) and Exposure
**Meaning:** **MtM** = current market value of a trade. **Current exposure** = max(MtM, 0) after netting/collateral. **Potential Future Exposure (PFE)** = high-percentile estimate of how large it could become.
**Real-life example:** Swap MtM +5 -> exposure 5. If MtM -3, current exposure 0 (bank owes them). PFE might be 12 at 95% confidence over the life.
**In your resume:** "accurate exposure calculation and limit monitoring".

### 37. Netting and ISDA Master Agreement
**Meaning:** **Netting** offsets positive and negative trades with the same counterparty under a legally enforceable agreement (**ISDA Master Agreement**), so only the net amount is at risk (a "netting set").
**Real-life example:** Trade A +10, Trade B -7 -> net exposure **3** instead of 10. If the agreement isn't enforceable in that jurisdiction, no netting benefit.
**In your resume:** "interfaces for ... netting".
**Interview angle:** Netting sets are **reference data + legal flags**; a wrong mapping can overstate or understate exposure by multiples.

### 38. Collateral, Margin (VM/IM), CSA, Haircut
**Meaning:** **Collateral** = assets posted to cover exposure. **CSA** (Credit Support Annex) defines what/how often. **Variation Margin (VM)** = daily top-up for MtM changes; **Initial Margin (IM)** = extra buffer. **Haircut** = discount to the collateral's value for price volatility.
**Real-life example:** Counterparty posts a government bond worth 10,00,000 with a 5% haircut -> counts as **9,50,000**. Exposure 12,00,000 -> uncovered 2,50,000.
**In your resume:** "collateral management".
**Interview angle:** Collateral reduces **LGD/exposure**, not the counterparty's PD. Eligibility, haircuts, currency mismatch and thresholds are rule-heavy - versioned configuration.

### 39. Credit Limit, Limit Monitoring, Limit Breach
**Meaning:** **Limit** = maximum exposure the bank accepts to a counterparty (by product, tenor, group). **Limit monitoring** compares exposure with limit continuously/EOD; **breach/excess** triggers escalation or approval.
**Real-life example:** Limit for ABC Corp = 50m; current exposure 47m; new trade adds 6m -> would breach -> requires approval or is rejected.
**In your resume:** "limit monitoring".
**Interview angle:** Limits are hierarchical (group -> legal entity -> product) and must be **checked atomically** to avoid two concurrent trades both using the same headroom.

### 40. Pre-deal Enquiry (Pre-deal Check)
**Meaning:** Before a trade is done, the front office asks the credit system "if I do this trade, will it fit within the limits?" and gets approve/refer/decline with headroom.
**Real-life example:** Trader asks: "10m 5-year swap with ABC Corp - ok?" The system calculates incremental exposure and replies "Approved, headroom 4m" or "Referred to credit officer".
**In your resume:** "pre-deal enquiries".
**Interview angle:** Low-latency, **what-if exposure calculation** (adds hypothetical trade to the netting set), clear reason codes, audit of the answer given.

### 41. Credit Approval
**Meaning:** Formal sign-off by credit officers/committees for limits and exceptions, with delegated authority levels.
**Real-life example:** Increase of ABC Corp's limit from 50m to 80m requires senior credit officer approval with documented rationale.
**In your resume:** "interfaces for credit approvals".
**Interview angle:** Workflow with **authority matrix**, four-eyes, audit trail, expiry/renewal dates.

### 42. Credit Rating (Internal and External)
**Meaning:** Grade of creditworthiness. External: S&P, Moody's, Fitch (AAA ... D). **Internal rating** from the bank's model/analyst. Investment grade = BBB-/Baa3 or above.
**Real-life example:** Counterparty downgraded from BBB to BB+ -> falls below investment grade -> higher risk weight, tighter limits, possible collateral calls.
**In your resume:** "credit rating data".

### 43. Counterparty Hierarchy / Legal Entity / Parent-Subsidiary
**Meaning:** Exposure must often be aggregated at **group level** (parent + subsidiaries) because a parent's failure affects its subsidiaries.
**Real-life example:** Three subsidiaries each have 20m exposure; parent limit is 50m -> group exposure 60m = breach even though each entity is under its own limit.
**Interview angle:** Hierarchy data is time-dependent (acquisitions, mergers) - **effective-dated reference data**.

### 44. Exposure Aggregation & Reference Data (Golden Source)
**Meaning:** Combining trades, collateral, ratings and hierarchy into one view; **golden source** = the authoritative system for a data element (e.g., legal entity master).
**Interview angle:** Describe how you handled conflicting/late data: validation rules, quarantine of bad records, reconciliation reports, alerts for missing feeds.

### 45. Monolith -> Microservices Migration (CDS) in a Risk Context
**Meaning:** Splitting the tightly coupled credit platform into independently deployable services (exposure, limits, approvals, reference data).
**Real-life example:** The "limits" service can now be scaled/released without redeploying the "approvals" service.
**In your resume:** "decomposing a monolith ... cutting time-to-deploy by 60%".
**Interview angle:** Be ready with: how you chose service boundaries (**DDD bounded contexts**), data ownership/ migration, strangler approach, testing parity, resilience (see Hystrix note).

---

# PART E - Societe Generale: Wealth Management & Online Stockbroking (L2 Support)

### 46. ISA (Individual Savings Account) - UK
**Meaning:** A tax-advantaged UK account for savings/investments with an annual contribution allowance (check current allowance).
**Real-life example:** Invest £10,000 in a Stocks & Shares ISA; growth and dividends inside it are free of UK income/capital-gains tax, unlike a normal brokerage account.
**Interview angle:** Systems must enforce **allowance tracking** across tax years and transfers.

### 47. SIPP (Self-Invested Personal Pension) - UK
**Meaning:** A personal pension where the holder chooses investments; contributions get tax relief; access restricted until minimum pension age (55, rising to 57 in 2028 - verify).
**Real-life example:** A person pays £8,000 in, tax relief is added, and they invest in funds/shares inside the SIPP for retirement.

### 48. CFD (Contract for Difference)
**Meaning:** Derivative where you pay/receive the **difference** between opening and closing price without owning the asset; leveraged, so gains and losses are magnified. Retail leverage is capped by regulators (e.g., FCA).
**Real-life example:** Buy 100 CFDs on a share at £10 (value £1,000) with 20% margin = £200. Price rises to £11 -> profit £100 (50% of margin). Falls to £9 -> loss £100 (50% of margin).
**Interview angle:** Margin calls, **real-time P&L and margin calculation** and forced close-out (liquidation) are critical production areas.

### 49. Spread Betting
**Meaning:** Bet a fixed amount per point of price movement (not owning anything); profit/loss = points moved x stake. UK-specific product with distinct tax/regulatory treatment.
**Real-life example:** £5 per point on an index; index rises 40 points -> +£200; falls 40 -> -£200.

### 50. Margin, Leverage and Margin Call
**Meaning:** **Margin** = deposit to open a leveraged position; **leverage** = exposure/margin; **margin call** = demand for more funds when losses erode margin; broker may **close out** positions.
**Real-life example:** Account equity falls below the maintenance level -> alert -> top-up within a deadline or positions are liquidated automatically.

### 51. Stockbroking Order & Settlement (T+1/T+2)
**Meaning:** Order lifecycle: placed -> routed to exchange -> executed -> confirmed -> **settled** (cash and shares exchange) after a standard cycle (US moved to **T+1** in 2024; other markets vary).
**Interview angle:** Reconciliation of **executions vs settlements**, failed trades, corporate actions.

### 52. Client Money Rules (FCA CASS) and Execution-Only vs Advisory
**Meaning:** **CASS** requires firms to segregate client assets/money. *Execution-only* = no advice; *advisory* = suitability obligations.
**Interview angle:** Production issues touching client balances are **high severity** - strict RCA and controls.

### 53. L2 Support Terms: Incident, Severity, SLA, RCA, Forensic Data Analysis
**Meaning:** **Incident** = service disruption; **Severity** (Sev1-4) sets urgency; **SLA** = agreed response/resolution time; **RCA** (root cause analysis) finds why; **forensic data analysis** reconstructs what happened from logs/DB.
**Real-life example:** Customers see wrong balances after a batch; L2 compares pre/post data, finds a duplicate file load, fixes data, raises a defect to prevent recurrence.
**Interview angle:** Use this to show you understand **production reality**, not just development - a strength for principal-level roles.

---

# PART F - Engineering Terms Interviewers Attach to BFSI

### 54. Idempotency
**Meaning:** Repeating the same request has the same effect as doing it once.
**Real-life example:** Customer taps "Pay" twice on a slow network; with an idempotency key, only one payment is made.
**Interview angle:** Essential for retries, message redelivery, batch restarts.

### 55. Reconciliation
**Meaning:** Matching two data sources and investigating differences (risk system vs general ledger, trades vs positions, loan-level vs summary).
**Real-life example:** Risk shows loans of 1,000.0 cr, GL shows 999.7 cr -> 0.3 cr break traced to a late repayment file.

### 56. Batch vs Real-time (EOD)
**Meaning:** **Real-time** for pre-deal checks, payments screening; **batch/EOD** for official risk numbers, regulatory runs, stress tests. Hybrid is common.
**In your resume:** Concurrent processing improved batch/service throughput (+40%).

### 57. Circuit Breaker / Hystrix (Resilience)
**Meaning:** Stop calling a failing dependency to avoid cascading failure; return fallback/fail fast and retry later.
**Real-life example:** Rating service is down; the limit service returns last-known rating flagged "stale" instead of hanging all requests.
**In your resume:** "Hystrix circuit-breaker" (CDS).
**Interview angle (important):** Hystrix is in **maintenance mode since 2018**; modern equivalent is **Resilience4j** (Spring Cloud Circuit Breaker). Say: "We used Hystrix then; today I'd use Resilience4j with timeouts, bulkheads and fallbacks."

### 58. Service Registry / API Gateway (Eureka)
**Meaning:** **Registry** = directory of running service instances; **gateway** = single entry point (routing, auth, rate limits).
**Interview angle:** On Kubernetes, discovery is often built in (DNS/Services), so Eureka is less needed - know both.

### 59. Zero-Downtime Release / Blue-Green / Rolling
**Meaning:** Deploy without service interruption.
**Real-life example:** New version deployed to a few pods first, traffic shifted gradually; rollback if errors rise.
**In your resume:** "zero-downtime releases" on Kubernetes (Danske).

### 60. Domain-Driven Design (DDD) / Event-Driven Architecture
**Meaning:** DDD models software around business domains ("bounded contexts": limits, exposure, collateral). Event-driven = services communicate through events (e.g., "TradeAmended").
**Interview angle:** Use DDD vocabulary when explaining service boundaries in credit risk.

### 61. Four-Eyes Principle / Segregation of Duties / Entitlements
**Meaning:** Critical actions need two different people; roles restrict access.
**Real-life example:** Analyst proposes a limit increase; a different officer approves.

### 62. UAT / SIT / Change Management
**Meaning:** Testing stages and controlled change processes required in regulated environments (evidence, approvals, release windows).
**In your resume:** "full release cycle ... integration testing, production support".

---

# PART G - Resume Analysis: What Interviewers Will Probe

## Strengths to lean on
| Strength | Evidence in resume | How to use it |
|---|---|---|
| Deep, long-running credit-risk ownership | 7+ years on Credit Suisse CDT, then CDS modernisation | Tell one **end-to-end story** (feature -> release -> production) |
| Regulatory exposure | CECL/CCAR (US), AML/sanctions (EU) | Show you understand *why* controls exist |
| Modernisation | Monolith -> microservices, 60% faster deploy | Prepare the "how and trade-offs" story |
| Performance | +40% throughput with multithreading | Be ready to explain thread-pool design, bottleneck found, measurement |
| Leadership | Squad, estimation, mentoring, workshops | Prepare 2 stories: mentoring, influencing a design decision |

## Likely probing points (prepare crisp answers)
| Topic | Why they'll ask | Preparation tip |
|---|---|---|
| **Employment timeline** (Morgan Stanley engagement ending Jul 2026; Dec 2024 - Jun 2025 between roles; two short stints in 2013 and 2021-22) | Standard screening | Prepare a calm, factual one-line explanation for each (why the engagement ended, why you chose the next role); keep it forward-looking |
| **"Principal" level** | Resume says Principal Engineer / Principal-level target | Prepare evidence of **scope**: architecture decisions, cross-team influence, mentoring, standards, incident leadership |
| **Hystrix** | Deprecated library on the resume | State you know Resilience4j and why Hystrix was retired |
| **Struts / J2EE / WebLogic** | Legacy tech | Frame as "modernised from this to Spring Boot microservices" |
| **40% / 60% metrics** | Numbers invite deep dives | Know baseline, measurement method, test environment, what changed exactly |
| **"Directly impacting Federal Reserve stress testing"** | Strong claim | Be precise: which services, which data flowed, who consumed it; avoid overstating |
| **Marriott / MphasiS (non-BFSI)** | Domain breadth | Keep short; pivot to BFSI contributions |
| **DB mix (DB2, Sybase, SQL Server, Oracle, PostgreSQL)** | Depth check | Choose one (e.g., DB2 or Sybase) and be ready for tuning/indexing/locking questions |

## Convert resume bullets into STAR stories (fill with *your* real details)
**1. Concurrency win (Morgan Stanley)**
- *Situation:* CECL/CCAR runs had a slow stage (volume: [records/loans], runtime [x h]).
- *Task:* Meet the reporting deadline without changing results.
- *Action:* Profiled, found [bottleneck]; introduced [executor/partitioning by portfolio]; ensured thread-safety and deterministic aggregation; verified with reconciliation to the previous results.
- *Result:* Throughput +40%, [runtime from x to y], no break in reconciled totals.

**2. Monolith decomposition (Credit Suisse CDS)**
- *Situation:* Tightly coupled credit platform, slow releases.
- *Action:* Defined service boundaries by domain, introduced Eureka/Gateway/Hystrix, migrated incrementally with parity tests.
- *Result:* Time-to-deploy down 60%; independent scaling/deployment.

**3. AML/Sanctions delivery (Danske)**
- *Situation:* New compliance rules from the Product Owner.
- *Action:* Turned rules into testable service logic, integrated statistical models, built case escalation, deployed on Kubernetes with zero downtime.
- *Result:* Case officers across 4 financial-crime categories served; smooth releases.

## Top 25 questions your resume will trigger
1. Explain CECL and how it differs from IFRS 9.
2. What goes into a CCAR/DFAST submission and what did your service contribute?
3. How do macroeconomic scenarios flow through loss calculations?
4. How did you ensure reproducibility and auditability of runs?
5. What was the bottleneck and how did you prove the 40% improvement?
6. How do you keep multithreaded aggregation deterministic?
7. What is the difference between AML, sanctions and fraud monitoring?
8. How do real-time sanctions screening and batch rescreening differ?
9. How do you reduce false positives without increasing risk?
10. Describe your case-management/escalation design.
11. How did you integrate statistical models into services?
12. Explain exposure, netting and collateral with an example.
13. How would a pre-deal enquiry work technically (latency, what-if exposure)?
14. How do you prevent two trades from consuming the same limit headroom?
15. How do you handle late/amended trades and bad reference data?
16. How did you split the monolith and decide service boundaries?
17. Why Hystrix then, and what would you use today?
18. How do you do zero-downtime releases on Kubernetes?
19. How do you test regulatory/compliance rules?
20. How do you handle production incidents for high-severity issues?
21. How did you estimate and plan in the squad model?
22. How do you mentor engineers and enforce quality (SonarQube, reviews)?
23. How do you design for data quality and reconciliation?
24. What would you do differently in the credit platform if you rebuilt it today?
25. What do you understand about ISA/SIPP/CFD products you supported?

---

# PART H - Quick Glossary & Resources

| Term | One-liner |
|---|---|
| ACL | Allowance for Credit Losses (reserve) |
| AFC/FCC | Anti-Financial Crime / Financial Crime Compliance |
| AML / CFT | Anti-Money Laundering / Counter-Terrorist Financing |
| CCAR / DFAST | Fed capital-planning stress test / Dodd-Frank stress tests |
| CCR | Counterparty Credit Risk |
| CDD / EDD | Customer Due Diligence / Enhanced Due Diligence |
| CECL | Current Expected Credit Losses (US GAAP, lifetime from day one) |
| CET1 / SCB | Core equity capital / Stress Capital Buffer |
| CFD | Contract for Difference (leveraged derivative) |
| CSA / ISDA | Collateral annex / master agreement for derivatives |
| EAD / PD / LGD / EL | Exposure, probability, loss severity, expected loss |
| FIU | Financial Intelligence Unit |
| FR Y-14 | Fed loan-level stress-test data collection |
| IM / VM | Initial / Variation margin |
| ISA / SIPP | UK tax-advantaged savings / personal pension |
| KYC | Know Your Customer |
| MtM / PFE | Mark-to-Market / Potential Future Exposure |
| MLRO | Money Laundering Reporting Officer |
| OFAC | US sanctions authority |
| PEP / UBO | Politically exposed person / Ultimate beneficial owner |
| SAR / STR | Suspicious Activity / Transaction Report |
| SR 11-7 | US model risk management guidance |
| T+1 / T+2 | Settlement cycles |

## 📚 Learning Resources
- Federal Reserve - Stress Tests and Capital Planning (CCAR/DFAST): https://www.federalreserve.gov/supervisionreg/stress-tests-capital-planning.htm
- FASB - ASC 326 (CECL) overview: https://www.fasb.org/
- FATF Recommendations: https://www.fatf-gafi.org/en/topics/fatf-recommendations.html
- OFAC Sanctions Programs and Lists: https://ofac.treasury.gov/
- Basel Framework (BIS): https://www.bis.org/basel_framework/
- UK FCA Handbook (CASS, retail leveraged products): https://www.handbook.fca.org.uk/
- *Counterparty Credit Risk and CVA* - Jon Gregory
- *Anti-Money Laundering Transaction Monitoring* guides from ACAMS (CAMS certification material)
- Your earlier prep files: **Credit Risk Domain Interview Q&A**, **Senior Risk Engineer Question Bank**, **Microservices Design Patterns Handbook**

> **Final Tip:** For every term, use the 3-step pattern: **definition -> one-line example -> "in my project I ..."**. That is how a principal engineer shows domain fluency instead of memorised vocabulary.
