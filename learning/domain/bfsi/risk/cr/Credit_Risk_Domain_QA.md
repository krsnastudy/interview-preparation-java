# 🏦 Banking & Credit Risk Domain - Interview Q&A for a Senior Java Engineer

**Profile:** 12 years Java, banking / credit-risk domain.
**What this covers:** the *domain* questions interviewers ask engineers on credit-risk platforms, each with a **plain answer**, a **simple worked example** and, where useful, an **interview insight**.
**How to read:** ★ = very likely. Learn the answer + example together; examples make you sound like you have lived it.

> **Accuracy note:** Written from domain knowledge, not from a live regulatory source. Rules differ by jurisdiction (RBI, PRA, EBA/CRR3, Fed/OCC) and change over time. Treat figures as the standard Basel reference points and confirm local specifics for the bank you are interviewing with.

---

## Contents
1. Banking & Credit Basics
2. Core Credit Risk Concepts (PD, LGD, EAD, EL)
3. Credit Lifecycle (Origination -> Recovery)
4. Collateral, Guarantees & Mitigation
5. Regulation: Basel, RWA, Capital
6. Provisioning: IFRS 9 / CECL / NPA
7. Scoring, Rating & Models
8. Counterparty Credit Risk (Derivatives)
9. Portfolio, Concentration & Stress Testing
10. Credit Risk Systems & Data (where Java fits)
11. Scenario Questions
12. Cheat-Sheet: Formulas & Glossary
13. How to Present Your Own Experience + Resources

---

## 1. Banking & Credit Basics

### Q1 ★ What is credit risk?
**Answer:** The risk of loss because a borrower or counterparty fails to meet its contractual obligations (pay interest/principal, deliver, settle). It is the largest risk type for most banks.
**Example:** The bank lends 10 lakh to a customer. If they stop paying after 6 months, the unpaid balance (less recoveries) is the credit loss.
**Insight:** Mention it is more than defaults: **downgrades** (migration) also cost money because they raise provisions and capital.

### Q2 What are the main types of financial risk in a bank?
**Answer:** Credit, market, liquidity, operational, and others (model, compliance, reputational, interest-rate-in-banking-book). Credit risk = counterparty may not pay; market risk = prices move; liquidity = cannot meet cash needs; operational = failures of people/process/systems/external events.
**Example:** A trader's bond falls in price (market), the issuer defaults (credit), the bank cannot fund withdrawals (liquidity), a batch job double-posts payments (operational).

### Q3 ★ Retail vs Corporate (wholesale) credit - how do they differ?
| Aspect | Retail (cards, personal, mortgage, auto) | Corporate / Wholesale |
|---|---|---|
| Volume | Millions of small loans | Few, large loans |
| Decisioning | Automated scorecards, rules | Credit analyst, rating, credit committee |
| Risk modelling | Pooled, statistical | Obligor-by-obligor rating |
| Data | Bureau + behaviour | Financial statements, industry, covenants |
| Monitoring | Delinquency buckets, roll rates | Early-warning signals, covenant tests, reviews |
**Example:** A 5 lakh personal loan is approved in seconds by a scorecard; a 500 crore working-capital facility goes to a committee after a credit memo.

### Q4 Secured vs unsecured lending?
**Answer:** Secured loans are backed by collateral (home, car, securities, receivables) which lowers loss given default; unsecured (credit cards, personal loans) rely only on borrower repayment so carry higher rates and loss rates.
**Example:** Home loan: if the borrower defaults, sell the house. Credit card: no asset to sell, only collections.

### Q5 Fund-based vs non-fund-based facilities?
**Answer:** Fund-based: bank actually lends cash (term loan, overdraft, cash credit). Non-fund-based: bank gives a *commitment* that may become a loan: letters of credit, bank guarantees, undrawn commitments.
**Example:** A bank guarantee for a contractor is off-balance-sheet until the beneficiary invokes it - then it becomes a funded exposure. Hence off-balance items use a **CCF** (credit conversion factor) in exposure calculation.

### Q6 Facility vs limit vs exposure vs outstanding?
**Answer:** *Limit* = maximum the bank is willing to lend to the borrower. *Facility* = specific product under the limit. *Outstanding/drawn* = amount currently used. *Exposure* = amount at risk (drawn + part of undrawn + derivative exposure etc.).
**Example:** Limit 10 cr: term loan 6 cr drawn, undrawn 4 cr -> outstanding 6 cr; exposure may be 6 + CCF x 4.

### Q7 What is KYC / AML and how does it relate to credit?
**Answer:** KYC verifies customer identity and profile; AML monitors for money laundering. They are prerequisites to lending but are *compliance* controls, not credit-risk models. Failing KYC blocks onboarding regardless of credit score.

---

## 2. Core Credit Risk Concepts

### Q8 ★ Explain PD, LGD, EAD and Expected Loss.
| Term | Meaning |
|---|---|
| **PD** | Probability of Default over a horizon (usually 1 year) |
| **LGD** | Loss Given Default: % of exposure lost after recoveries |
| **EAD** | Exposure At Default: amount outstanding when default happens |
| **EL** | Expected Loss = PD x LGD x EAD |

**Example:** PD 2%, LGD 45%, EAD 10,00,000 -> EL = 0.02 x 0.45 x 10,00,000 = **9,000**.
**Insight:** EL is a *cost of doing business* (priced and provisioned). Capital covers **unexpected** loss.

### Q9 ★ Expected loss vs unexpected loss - and who pays for each?
**Answer:** EL is the average loss you expect; it is covered by **pricing** (credit spread) and **provisions**. UL is the volatility around the average (bad years); it is covered by **capital**. Economic capital ~ loss at a high confidence level (e.g., 99.9%) minus EL.
**Example:** A portfolio loses 1% on average (EL), but in a recession could lose 4%. The extra 3% is what capital protects against.

### Q10 How do you calculate EAD for undrawn commitments?
**Answer:** EAD = drawn + CCF x undrawn. CCF reflects the chance the borrower draws before defaulting (borrowers in trouble draw down).
**Example:** Limit 10,00,000, drawn 6,00,000, CCF 50% -> EAD = 6,00,000 + 0.5 x 4,00,000 = **8,00,000**.

### Q11 ★ What is the definition of default?
**Answer:** Basel: obligor is **90+ days past due** on a material amount, **or** the bank considers it **unlikely to pay** without recourse to collateral (UTP: bankruptcy, distressed restructuring, etc.). Many regimes use the same 90-day idea (e.g., RBI NPA).
**Example:** EMI due 1 Jan unpaid; on 1 Apr (90 days) the loan is classified as defaulted/NPA, even if collateral exists.
**Insight:** Default definition must be consistent across PD models, provisioning and reporting - a classic source of data bugs.

### Q12 What are DPD buckets and SMA categories?
**Answer:** Days Past Due buckets track delinquency: 0, 1-30, 31-60, 61-90, 90+. In India, RBI "Special Mention Accounts": SMA-0 (1-30 days), SMA-1 (31-60), SMA-2 (61-90); 90+ = NPA.
**Example:** A customer 45 DPD is SMA-1: still performing but flagged for early collections.

### Q13 What are roll rates and vintage analysis?
**Answer:** *Roll rate* = % of accounts moving from one delinquency bucket to the next. *Vintage* = performance of loans originated in the same period tracked over their age.
**Example:** 100 accounts at 30 DPD; 12 roll to 60 DPD -> roll rate 12%. A 2023-Q1 vintage with higher 12-month default than 2022-Q1 signals weaker underwriting.

### Q14 Types of credit risk?
**Answer:** Default risk, **migration/downgrade** risk, **concentration** risk (single name/sector/country), **country/transfer** risk, **settlement** risk (pay but counterparty doesn't deliver - Herstatt), **counterparty credit risk** on derivatives, **recovery** risk.

### Q15 What is a credit rating and what is investment grade?
**Answer:** A rating is an opinion on creditworthiness. External: S&P/Fitch AAA..D, Moody's Aaa..C. **Investment grade** = BBB-/Baa3 or higher. Banks also build **internal ratings** mapped to a PD on a master scale.
**Example:** BB+ is sub-investment ("junk"); its PD is much higher than BBB.

### Q16 PD: through-the-cycle vs point-in-time?
**Answer:** **TTC** PD is stable across the economic cycle (rating-based, used for Basel capital). **PIT** PD reflects current conditions (used for IFRS 9 / pricing / early warning).
**Example:** In a boom the same borrower's PIT PD falls; TTC PD barely moves.

### Q17 LGD: why "downturn LGD"?
**Answer:** Recoveries are worse in recessions (collateral values fall, workouts take longer). Basel IRB requires **downturn LGD** for capital, while IFRS 9 uses *expected* (probability-weighted) LGD.
**Example:** House value falls 25% in a crisis, so LGD on mortgages rises.

---

## 3. Credit Lifecycle

### Q18 ★ Walk me through the loan lifecycle.
```
Application -> KYC/Fraud checks -> Bureau pull -> Scoring / Rating -> Underwriting rules
 -> Decision (approve / decline / refer) -> Pricing -> Documentation & Collateral
 -> Disbursement -> Servicing -> Monitoring / Early warning
 -> Collections -> Restructuring -> Recovery -> Write-off
```
**Example (retail):** Customer applies via app -> bureau score 740 -> income/DTI check -> auto-approve at 10.5% -> disbursal -> EMI schedule -> 35 DPD triggers SMS reminders.
**Insight:** Be ready to say which stages your Java systems touched.

### Q19 What happens in underwriting?
**Answer:** Assess **capacity** (income vs obligations: DTI, DSCR), **character** (bureau, history), **collateral**, **capital** (borrower's equity), **conditions** (purpose, market) - the "5 Cs of credit". Combine automated rules + score + human review for exceptions.

### Q20 DTI, LTV, DSCR, ICR - what are they?
| Ratio | Formula | Typical use |
|---|---|---|
| **DTI** | Monthly debt payments / monthly income | Retail affordability |
| **LTV** | Loan / collateral value | Mortgage risk |
| **DSCR** | Net operating income / debt service | Corporate/CRE repayment capacity |
| **ICR** | EBIT / interest expense | Corporate coverage |
**Examples:** DTI = 40,000/1,00,000 = 40%. LTV = 80/100 = 80%. DSCR = 150/120 = 1.25 (lenders often require >= 1.2-1.25).

### Q21 What is risk-based pricing?
**Answer:** Price the loan to cover funding cost + operating cost + **expected loss** + capital charge + margin. Riskier borrowers pay more.
**Example:** Same product: score 800 -> 9.5%; score 650 -> 13%.

### Q22 What is a covenant?
**Answer:** A condition in a loan agreement. *Financial covenants* (e.g., Debt/EBITDA < 3x, DSCR > 1.2) and *affirmative/negative* covenants (must insure assets; no extra debt). A breach triggers review, repricing, waiver or default.
**Example:** Company's leverage rises to 3.4x versus a 3.0x covenant -> covenant breach flag -> relationship manager and credit officer review.

### Q23 What are Early Warning Indicators (EWI)?
**Answer:** Signals that credit quality may deteriorate before default: late payments, overdraft overuse, falling account turnover, rating downgrades, covenant breaches, adverse news, sector stress.
**Example:** A corporate's cash-credit account stays at 100% utilisation for 3 months with falling inflows -> EWI alert.

### Q24 ★ Collections, restructuring/forbearance, write-off, recovery - differences?
**Answer:** *Collections*: contact/recover overdue dues. *Forbearance/restructuring*: concessions to a struggling borrower (extend tenor, reduce rate) - must be flagged, often triggers Stage 2/higher risk. *Write-off*: remove a loan deemed uncollectable from the balance sheet (legal claim continues). *Recovery*: money collected after default/write-off.
**Example:** Customer given a 12-month moratorium (forbearance) -> flagged as restructured; loan fully provisioned and written off after 3 years; 20% recovered later via asset sale.

### Q25 Provisioning vs write-off?
**Answer:** A **provision** is an *estimate* of loss held as a reserve (P&L charge, loan stays on balance sheet net of allowance). A **write-off** *removes* the asset (uses the provision). Write-off does not forgive the debt.

---

## 4. Collateral, Guarantees & Mitigation

### Q26 ★ How does collateral reduce credit risk?
**Answer:** It lowers **LGD** (not PD). Eligible collateral (cash, government bonds, listed equity, property, receivables) is valued, **haircut** for price volatility and currency mismatch, and then offset against exposure (with legal enforceability and perfected security required).
**Example:** Exposure 100, collateral 100 with 20% haircut -> recognised 80 -> unsecured part 20.

### Q27 What is LTV and why does it matter?
**Answer:** Loan-to-Value = loan / collateral value. Higher LTV = thinner cushion = higher LGD and risk weight.
**Example:** 80 loan on a 100 house -> 80% LTV; if prices fall 25% (house = 75) the loan is underwater (LTV 107%).

### Q28 Types of credit risk mitigation (CRM)?
**Answer:** Collateral, guarantees/credit insurance, **netting** agreements, **credit derivatives** (CDS), diversification, covenants, syndication/participations, securitisation.

### Q29 What is a CDS?
**Answer:** Credit Default Swap: protection buyer pays periodic premium; if a defined **credit event** (failure to pay, bankruptcy, restructuring) occurs, the seller compensates the loss.
**Example:** Bank buys CDS on a corporate's bond; the corporate defaults -> the CDS seller pays (notional - recovery value).

### Q30 What is netting?
**Answer:** Offsetting positive and negative exposures with the same counterparty under a legally enforceable master agreement (ISDA), so only the net amount is at risk.
**Example:** Trade A +10, Trade B -7 -> net exposure 3 instead of 10.

### Q31 What is a guarantee vs collateral?
**Answer:** Collateral is an *asset* pledged; a guarantee is a *promise by a third party* to pay. Guarantor's creditworthiness substitutes (or supports) the borrower's - risk weight of the guarantor may be applied to the guaranteed part.

---

## 5. Regulation: Basel, RWA, Capital

### Q32 ★ What is Basel and what are its three pillars?
**Answer:** Basel Accords are international banking-supervision standards (BCBS). Evolved through Basel I (1988) -> II (2004) -> III (post-2008) -> final Basel III reforms ("Basel IV").
- **Pillar 1:** Minimum capital for credit, market, operational risk.
- **Pillar 2:** Supervisory review (ICAAP/SREP) - bank-specific extra capital for other risks.
- **Pillar 3:** Market discipline through public disclosures.

### Q33 ★ What is RWA and why does it exist?
**Answer:** Risk-Weighted Assets scale exposures by riskiness so riskier assets need more capital. Capital ratio = Capital / RWA.
**Example:** Corporate loan 100 with 100% risk weight -> RWA 100 -> minimum total capital at 8% = **8**. Government bond at 0% RW -> RWA 0 -> no capital.

### Q34 ★ Capital ratios under Basel III?
| Item | Minimum |
|---|---|
| CET1 (Common Equity Tier 1) | 4.5% of RWA |
| Tier 1 | 6% |
| Total capital | 8% |
| Capital Conservation Buffer | +2.5% (CET1) |
| Countercyclical buffer | 0-2.5% (jurisdiction) |
| G-SIB/D-SIB surcharges | extra for systemic banks |
| Leverage ratio | >= 3% (Tier 1 / total exposure, non-risk-based) |
**Example:** With CCB, CET1 effectively 7% and total 10.5% -> on RWA 100 the bank needs CET1 7 and total 10.5.

### Q35 Standardised Approach vs IRB?
**Answer:** **SA:** regulator-set risk weights (by asset class, external rating, LTV). **IRB:** the bank's *internal* models estimate PD (and LGD/EAD in Advanced IRB) which feed a regulatory formula. **F-IRB:** bank estimates PD only; **A-IRB:** PD, LGD, EAD (and maturity).
**Example (SA):** unrated corporate 100% RW; retail 75%; residential mortgage 35% in Basel II (LTV-based bands in the final reforms).
**Insight:** IRB needs approved models, data history, validation, governance -> big engineering workload.

### Q36 How does the IRB capital formula work (high level)?
**Answer:** It is based on the **Asymptotic Single Risk Factor (Vasicek)** model: a granular portfolio exposed to one systematic factor. Capital K = loss at **99.9% confidence over one year** minus EL, using PD, LGD, an asset correlation, and a maturity adjustment. RWA = K x 12.5 x EAD (12.5 = 1/8%).
**Example:** If K = 8% of EAD -> RWA = 0.08 x 12.5 x EAD = EAD (a 100% risk weight).

### Q37 What is an output floor / Basel III final reforms?
**Answer:** Limits the benefit of internal models: RWA from models cannot fall below a % (phased to **72.5%**) of the standardised RWA. Also revised SA and IRB (e.g., removing A-IRB for some exposures). Timelines differ per jurisdiction.

### Q38 What is the Large Exposure limit?
**Answer:** A bank's exposure to a single counterparty/group is capped at **25% of Tier 1 capital** (lower for G-SIB to G-SIB) to avoid concentration.
**Example:** Tier 1 = 2,000 -> max exposure to one group = 500.

### Q39 What are LCR and NSFR (briefly)?
**Answer:** Liquidity rules: **LCR** >= 100% (enough high-quality liquid assets to survive 30 days of stress) and **NSFR** >= 100% (stable funding over one year). These are liquidity, not credit, but interviewers check you know the difference.

### Q40 What is BCBS 239?
**Answer:** Principles for **effective risk data aggregation and risk reporting**: governance, accurate/complete/timely data, adaptability, and reporting practices (14 principles; 11 for banks).
**Example:** In a crisis, the bank must produce its exposure to a group across all systems *the same day* - needs a single exposure view, lineage and reconciliation.
**Insight:** This is the regulation behind many data-platform projects you may have worked on.

### Q41 What is COREP / FINREP?
**Answer:** EU supervisory reporting templates: **COREP** (capital, RWA, large exposures, leverage) and **FINREP** (financial statements data). Similar reports exist elsewhere (e.g., FR Y-14/FR Y-9C in the US).

---

## 6. Provisioning: IFRS 9 / CECL / NPA

### Q42 ★ Explain IFRS 9 Expected Credit Loss and the three stages.
| Stage | Condition | Provision |
|---|---|---|
| **1** | Performing, no significant increase in credit risk (SICR) | **12-month ECL** |
| **2** | SICR since origination (e.g., 30+ DPD, rating downgrade) | **Lifetime ECL** |
| **3** | Credit-impaired / defaulted (90+ DPD) | **Lifetime ECL** (interest on net carrying amount) |
ECL = sum over periods of PD x LGD x EAD x discount factor, **probability-weighted over scenarios** (base/upside/downside).
**Example:** Exposure 1,00,000, LGD 40%. Stage 1: 12-month PD 1% -> ECL = 0.01 x 0.40 x 1,00,000 = **400**. If it moves to Stage 2 with lifetime PD 6% -> **2,400** (ignoring discounting). The provision jumps six-fold when the loan migrates.

### Q43 ★ What is SICR and how do you detect it?
**Answer:** Significant Increase in Credit Risk since origination. Indicators: relative PD increase, rating downgrade by N notches, 30 DPD (rebuttable presumption), watchlist, forbearance.
**Example:** PD at origination 0.5%, now 2% (4x) and over the bank's threshold -> Stage 2.

### Q44 IFRS 9 vs Basel expected loss - differences?
| | IFRS 9 ECL | Basel IRB EL |
|---|---|---|
| Purpose | Accounting provision | Regulatory capital |
| PD | **Point-in-time**, forward-looking | **Through-the-cycle**, conservative |
| LGD | Expected, unbiased | **Downturn**, floors |
| Horizon | 12-month or lifetime | 1-year |
| Macro scenarios | Required, probability-weighted | Not directly |
**Insight:** Often one team reuses Basel models with adjustments - a "bridge" is a typical engineering project.

### Q45 What is CECL?
**Answer:** US GAAP **Current Expected Credit Loss**: lifetime expected loss recognised **from day one** for all loans (no staging), using historical experience, current conditions and reasonable forecasts.

### Q46 ★ NPA classification (India) / Non-performing definition?
**Answer:** An asset is **NPA** when interest/principal is overdue for **more than 90 days**. Classification: **Standard** -> **Sub-standard** (NPA up to 12 months) -> **Doubtful** (D1/D2/D3 by age) -> **Loss**. Provision percentages increase with the category; secured and unsecured portions differ.
**Example:** A term loan overdue since 1 Jan becomes NPA on 1 Apr; after 12 months as sub-standard it becomes doubtful.

### Q47 Gross NPA vs Net NPA, and provision coverage?
**Answer:** Gross NPA = total non-performing loans; **Net NPA = Gross NPA - provisions**. **Provision Coverage Ratio** = provisions / Gross NPA.
**Example:** Gross NPA 100, provisions 70 -> Net NPA 30, PCR 70%.

### Q48 Cure and probation?
**Answer:** A defaulted loan can return to performing ("cure") only after the borrower clears arrears and meets a **probation** period (e.g., 3-12 months of regular payments), to avoid re-default noise.

---

## 7. Scoring, Rating & Models

### Q49 ★ What is a credit scorecard and how is it built?
**Answer:** A statistical model (typically **logistic regression**) that converts borrower attributes into a score that rank-orders default risk. Steps: define good/bad, collect data, bin variables, compute **WoE** (weight of evidence) and IV, fit the model, scale to points, validate, set cut-offs.
**Example:** Bin "age 25-35": 60% of goods, 20% of bads -> WoE = ln(0.60/0.20) = **1.10** (positive = safer).
**Types:** *Application* (new customers), *behavioural* (existing accounts), *collections* (who to chase).

### Q50 How do you measure model performance?
| Metric | Meaning |
|---|---|
| **AUC / ROC** | Ability to rank defaulters above non-defaulters (0.5 random, 1.0 perfect) |
| **Gini** | 2 x AUC - 1 (AUC 0.80 -> Gini 0.60) |
| **KS** | Max separation between good and bad cumulative distributions |
| **PSI** | Population Stability Index: score distribution drift (<0.10 stable, 0.10-0.25 moderate, >0.25 significant) |
| **Calibration / backtesting** | Predicted PD vs observed default rate |
**Insight:** Discrimination (rank order) and calibration (level) are different - a model can rank well yet mis-state PD levels.

### Q51 What is model risk and model validation?
**Answer:** Risk of wrong decisions because a model is flawed or misused. Governance: independent **validation** (conceptual soundness, data, performance, backtesting), ongoing monitoring, change control, documentation, inventory and approval before use.
**Insight (for engineers):** Model **versioning, reproducibility, audit trail** and "same input -> same output" are engineering responsibilities.

### Q52 What is a master rating scale?
**Answer:** A common set of grades (e.g., 1-10) each mapped to a PD range so ratings from different models are comparable.
**Example:** Grade 3 = PD 0.3%-0.6%; Grade 7 = PD 4%-8%.

### Q53 What is a transition (migration) matrix?
**Answer:** Probabilities of moving between rating grades (and default) over a period; used for lifetime PD, IFRS 9 and credit VaR.
**Example:** 90% of "A" borrowers stay A, 8% fall to B, 1.5% to C, 0.5% default within a year.

### Q54 Explain Gini vs IV vs KS in simple terms.
**Answer:** IV tells how predictive a *single variable* is; KS and Gini describe the *whole model's* separation power.
**Example:** "Number of late payments" has high IV; the final scorecard has Gini 0.55.

### Q55 What is RAROC?
**Answer:** Risk-Adjusted Return on Capital = (Revenue - costs - expected loss) / economic capital. Used to compare business lines and price deals.
**Example:** Revenue 12, costs 4, EL 3, capital 40 -> RAROC = (12 - 4 - 3)/40 = **12.5%**.

---

## 8. Counterparty Credit Risk (Derivatives, Repo)

### Q56 ★ What is counterparty credit risk and how is it different from loan credit risk?
**Answer:** CCR is the risk that a counterparty defaults **before final settlement** of a derivative/repo/securities-financing trade. Unlike a loan, the exposure is **uncertain and two-sided**: it depends on future market prices and may be zero or large.
**Example:** An interest-rate swap has MtM +5 for the bank today -> exposure 5. If rates move so MtM becomes -3, current exposure is 0 (bank owes them).

### Q57 ★ Explain current exposure, EE, PFE and EAD for derivatives.
| Term | Meaning |
|---|---|
| **Current exposure** | max(MtM, 0) today (after netting/collateral) |
| **EE / EPE** | Expected (Positive) Exposure over time |
| **PFE** | Potential Future Exposure: high percentile (e.g., 95-99%) of future exposure |
| **EAD** | Regulatory exposure measure; **SA-CCR**: EAD = alpha x (RC + PFE add-on), alpha = 1.4 |
**Example:** RC 2, PFE add-on 5 -> EAD = 1.4 x (2 + 5) = **9.8**.

### Q58 What is CVA?
**Answer:** **Credit Valuation Adjustment** = the market price of counterparty default risk; reduce the derivative's value by expected loss from counterparty default. Banks hold **CVA capital** and often hedge with CDS.
**Example:** A swap worth 10 on a risk-free basis may be booked at 9.7 after CVA of 0.3.

### Q59 Wrong-way and right-way risk?
**Answer:** **Wrong-way:** exposure rises when the counterparty's credit quality falls (e.g., buying protection on an airline from an airline-related bank; oil-company counterparty on oil derivatives). Dangerous because exposure and default move together.

### Q60 What are CSA, variation margin and initial margin?
**Answer:** A **CSA** (Credit Support Annex to ISDA) defines collateral exchange. **Variation margin (VM)** = daily top-ups to cover current MtM changes. **Initial margin (IM)** = extra buffer for potential change during the close-out period (mandatory for many non-cleared trades under UMR).
**Example:** MtM moves +2 in the bank's favour -> counterparty posts 2 VM next day.

### Q61 CCP clearing - why does it reduce risk?
**Answer:** A **central counterparty** becomes buyer to every seller: multilateral netting, daily margining, default fund and loss waterfall, reducing bilateral exposures but concentrating risk in the CCP.

---

## 9. Portfolio, Concentration & Stress Testing

### Q62 What is credit concentration risk?
**Answer:** Losses concentrated because exposures are correlated: single name, **industry**, **geography**, **product**, collateral type. Managed with limits and diversification.
**Example:** 30% of a bank's book in real estate -> a property downturn hits everything simultaneously.

### Q63 What is correlation / diversification in credit?
**Answer:** Defaults are not independent; shared macro drivers make them cluster. Higher correlation -> fatter loss tail -> more unexpected loss and capital.

### Q64 ★ What is stress testing in credit risk?
**Answer:** Evaluate losses and capital under severe-but-plausible scenarios (recession, unemployment up, house prices down, rates up). **Top-down** by regulators (e.g., Fed CCAR/DFAST, EBA, BoE) and **bank-run** internal stress tests (ICAAP).
**Example:** Scenario: GDP -4%, unemployment +5pts, house prices -25% -> PDs rise, LGDs rise -> projected loss and capital ratio after 3 years.
**Insight:** Engineers deliver scenario engines, data pipelines and reproducible runs.

### Q65 Credit VaR / economic capital?
**Answer:** Loss distribution of the portfolio; **Credit VaR** = loss at a confidence level (99.9%); economic capital = VaR - EL. Models: CreditMetrics (migration), CreditRisk+ (actuarial), KMV/Merton (structural), copula-based simulations.

### Q66 What is securitisation (brief)?
**Answer:** Pool loans into an SPV, issue tranches (senior, mezzanine, equity) with a loss waterfall: first losses hit equity, then mezzanine, then senior. Transfers credit risk to investors; subject to specific capital rules and risk-retention.
**Example:** Pool 100, equity tranche 5: first 5 of losses wipe equity before senior notes lose anything.

---

## 10. Credit Risk Systems & Data (where your Java experience fits)

### Q67 ★ Describe the data/system landscape of a credit risk platform.
```
Source systems (loan origination, core banking, cards, trade/derivatives, collateral mgmt, GL)
        |  extract / events / CDC
        v
 Staging + Data quality + Reference data (customer, hierarchy, ratings, FX)
        v
 Exposure aggregation  ->  Risk engines (PD/LGD/EAD, ECL, RWA, limits, CCR)
        v
 Results store / data mart ---> Reports (COREP, management, limits), dashboards, regulatory submissions
        ^
 Controls: reconciliation to GL, lineage, audit trail, entitlements
```
**Insight:** Use this diagram to place your own projects.

### Q68 What is a decision engine / rules engine and why?
**Answer:** Externalised credit policy rules (eligibility, scoring cut-offs, overrides) executed for each application with a full audit of **which rule fired and why**. Benefits: change policy without redeploy, consistent decisions, explainability (also regulatory requirements like adverse-action reasons).
**Example:** Rule: "Decline if bureau score < 600 OR DTI > 55%; refer if self-employed < 2 years."
**Insight:** Mention Drools/DMN or in-house engines, versioned rule sets, simulation before release.

### Q69 How do you handle limits and utilisation in a system?
**Answer:** A hierarchy (group -> customer -> facility -> sub-limit) with **check-before-use**: new exposure is tested against available limit in real time or batch, with locks/reservations to avoid double-use, expiry/review dates and breach workflows.
**Example:** Customer limit 10 cr; two parallel drawdowns of 6 cr each must not both succeed -> atomic reservation or optimistic locking.

### Q70 ★ How do you ensure reproducibility and auditability of risk numbers?
**Answer:** Versioned inputs (**as-of date snapshots** of positions, market data, ratings), versioned models/config, immutable results, lineage from report back to source, and reruns that give identical outputs. Never overwrite history; store corrections as new versions (bitemporal).
**Example:** Regulator asks "why was RWA 4,210 on 30 June?" -> you replay the run with the same snapshot and model version.

### Q71 Reconciliation between risk and finance?
**Answer:** Exposure/provision numbers in the risk system must tie to the **general ledger** within tolerance; breaks are investigated daily. Differences come from timing, FX rates, accrued interest, definitions of default/exposure.
**Example:** Risk shows loans 1,000.0 cr, GL 999.7 cr -> 0.3 cr break traced to a late-posted repayment file.

### Q72 What are typical data-quality problems?
**Answer:** Missing/duplicate customer IDs, inconsistent hierarchies (parent-child), stale ratings/collateral values, wrong currency or day-count, mismatched default flags across systems, late files.
**Insight:** "Garbage in" leads to wrong capital numbers: describe the controls you built (validation rules, completeness checks, DQ dashboards).

### Q73 Batch vs real-time in credit risk?
**Answer:** **Real-time/near-real-time:** application decisioning, limit checks, pre-deal CCR checks. **Batch (EOD):** full revaluation, ECL, RWA, regulatory reports, portfolio analytics. Hybrid: event-driven incremental updates + EOD official run reconciled.

### Q74 How would you design an exposure aggregation service for a corporate group?
**Answer:** (1) Ingest exposures from each product system with a common canonical model (counterparty, facility, product, currency, exposure type). (2) Resolve **legal-entity hierarchy** (parent/connected parties). (3) Convert currencies using the snapshot rate. (4) Apply netting, collateral and CCFs. (5) Aggregate by hierarchy and compare to limits. (6) Persist with as-of/version, publish events for breaches. (7) Reconcile with sources.
**Example:** Group "XYZ Holdings" has loans in 3 subsidiaries + derivatives -> single consolidated exposure checked against the 25%-of-Tier-1 large-exposure limit.

### Q75 Where do regulations affect your code?
**Answer:** Precision (BigDecimal, rounding rules), day-count conventions, effective-dated reference data, audit trails, retention, entitlements/segregation of duties, change control, test evidence, explainable decisions, data privacy (GDPR / DPDP).

---

## 11. Scenario Questions (domain flavour)

### S1 ★ A customer is 95 days overdue. What should the systems do?
- Flag **default/NPA**, move to **Stage 3**, stop interest accrual per policy, transfer to **collections/recovery**, recompute provision (lifetime ECL / regulatory %), update bureau reporting, restrict further drawdowns, notify relevant teams and keep an audit trail.

### S2 A borrower's rating drops from BBB to BB+. What changes?
- Crosses the **investment-grade** boundary; PD up; internal grade down; may trigger **Stage 2** (SICR), higher provision, higher risk weight/RWA, tighter limits/review, possible repricing, watchlist, and collateral top-up requests.

### S3 Large corporate: the bank has 3 loans to its subsidiaries. How do you check concentration?
- Resolve the **group hierarchy** and sum **connected exposures** against the single-name/large-exposure limit; show headroom and breaches.

### S4 EOD risk report shows exposure 5% higher than yesterday. What do you check?
- Duplicate/missing source feeds, FX rate or date errors, new large trades, rating or collateral changes, model/config release, default flag differences, reconciliation to source and GL, then genuine business movement.

### S5 A model's PSI is 0.31 on the latest scorecard. What does that mean?
- Population shifted significantly (>0.25): investigate data/drift, check performance/backtesting, possibly recalibrate or rebuild; inform model governance.

### S6 Regulator asks for exposure to the real-estate sector as of last quarter-end.
- Need **historical as-of data**, consistent **sector classification** mapping, lineage and reproducibility; run from the stored snapshot, not current data.

### S7 You must cut provisioning batch time from 6h to 3h. Approach?
- Profile, partition by portfolio/product, parallelise pure per-account ECL calc, cache reference/scenario data, pre-aggregate, reduce I/O, incremental processing for unchanged accounts - while keeping results bit-for-bit reconcilable to the previous run.

### S8 Collateral value is stale (last valued 3 years ago). Impact?
- LTV and LGD are understated; may breach policy (revaluation frequency); trigger revaluation workflows, apply conservative haircut, flag a data-quality exception.

---

## 12. Cheat-Sheet

### Key formulas
| Item | Formula | Example |
|---|---|---|
| Expected Loss | PD x LGD x EAD | 2% x 45% x 10,00,000 = 9,000 |
| EAD (undrawn) | Drawn + CCF x Undrawn | 6,00,000 + 50% x 4,00,000 = 8,00,000 |
| Capital requirement | RWA x ratio | 100 x 8% = 8 |
| LTV | Loan / Collateral value | 80/100 = 80% |
| DSCR | NOI / Debt service | 150/120 = 1.25 |
| DTI | Debt payments / Income | 40k/100k = 40% |
| Gini | 2 x AUC - 1 | 2 x 0.80 - 1 = 0.60 |
| WoE | ln(%good / %bad) | ln(60/20) = 1.10 |
| Net NPA | Gross NPA - provisions | 100 - 70 = 30 |
| PCR | Provisions / Gross NPA | 70/100 = 70% |
| RAROC | (Rev - Cost - EL) / Econ. capital | (12-4-3)/40 = 12.5% |
| SA-CCR EAD | 1.4 x (RC + PFE) | 1.4 x (2+5) = 9.8 |
| Large exposure | <= 25% of Tier 1 | 25% x 2,000 = 500 |
| Leverage ratio | Tier 1 / Total exposure | 300/10,000 = 3% |

### Glossary (one-liners)
| Term | Meaning |
|---|---|
| ASRF | Asymptotic Single Risk Factor model behind IRB |
| CCF | Credit Conversion Factor for off-balance items |
| CET1 | Highest-quality capital (common equity) |
| CRM | Credit Risk Mitigation |
| CSA / ISDA | Collateral / master agreements for derivatives |
| DPD | Days Past Due |
| ECL | Expected Credit Loss (IFRS 9) |
| EWI | Early Warning Indicator |
| ICAAP / SREP | Bank's internal capital assessment / Supervisory review |
| IRB (F/A) | Internal Ratings-Based approach (Foundation / Advanced) |
| NPA | Non-Performing Asset |
| PIT / TTC | Point-in-time / Through-the-cycle |
| SICR | Significant Increase in Credit Risk |
| SA / SA-CCR | Standardised Approach / Standardised Approach for CCR |
| UTP | Unlikely to Pay |
| WoE / IV | Weight of Evidence / Information Value |

---

## 13. Presenting Your Own Experience

**Use this structure whenever they say "tell me about your credit risk work":**

1. **Business context:** "We built/maintained the {origination | exposure | provisioning | limits | CCR | reporting} platform for {retail/corporate} credit."
2. **Your ownership:** modules, services, batch, integrations, Java/Spring/Kafka/DB stack.
3. **Domain depth:** "It computed {EAD/ECL/RWA} using {PD, LGD, CCF}..." (use correct terms from this sheet).
4. **Controls:** reconciliation, lineage, audit, tolerances, UAT with business/risk.
5. **Result:** run-time cut from X to Y, fewer breaks, passed audit/regulatory review, zero Sev-1 in a quarter.
6. **Learning:** one thing you now do differently.

**Common traps**
- Using "default" and "NPA" and "Stage 3" interchangeably without noting the differences.
- Saying "capital covers expected loss" (it is UL; EL is pricing/provisions).
- Forgetting that collateral lowers **LGD**, not PD.
- Mixing market-risk VaR with credit-risk concepts.
- Quoting percentages without the jurisdiction/version caveat.

**Questions to ask the interviewer**
- Which part of credit risk does the platform cover: origination, provisioning (IFRS 9), RWA/regulatory, limits, CCR?
- Retail or wholesale? IRB or Standardised? Which models are in-house vs vendor?
- How do engineering and risk/model-validation teams work together and release changes?
- What are the current data-quality or BCBS 239 challenges?

## 📚 Resources
- BCBS standards (Basel framework): https://www.bis.org/basel_framework/
- BCBS 239 principles: https://www.bis.org/publ/bcbs239.htm
- IFRS 9 Financial Instruments (IFRS Foundation): https://www.ifrs.org/issued-standards/list-of-standards/ifrs-9-financial-instruments/
- RBI - Master Circular on Income Recognition, Asset Classification and Provisioning (search the latest on rbi.org.in)
- *Credit Risk Management* - Joetta Colquitt (practical retail/corporate overview)
- *Credit Risk Modeling Using Excel and VBA* - Loeffler & Posch (PD/LGD/portfolio intuition)
- *Credit Risk Scorecards* - Naeem Siddiqi (scorecard development, WoE/IV, cut-offs)
- *Counterparty Credit Risk and CVA* - Jon Gregory (CCR, netting, collateral, CVA)
- *Risk Management and Financial Institutions* - John Hull (overall risk context)
- Moody's / S&P rating methodology pages (public overviews)

> **Final Tip:** For every domain answer give **definition -> tiny numeric example -> why it matters to the system you built.** That three-step pattern turns textbook knowledge into senior-engineer credibility.
