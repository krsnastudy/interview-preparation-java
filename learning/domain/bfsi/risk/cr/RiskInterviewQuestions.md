# Banking and Financial Risk Interview Questions

## Question Index

Select a question to jump to its explanation.

1. [What is CECL?](#q1-cecl)
2. [What is CCAR?](#q2-ccar)
3. [What is money laundering?](#q3-money-laundering)
4. [What is netting?](#q4-netting)
5. [What is market risk?](#q5-market-risk)
6. [What is credit risk?](#q6-credit-risk)
7. [What is exposure?](#q7-exposure)
8. [What is collateral management?](#q8-collateral-management)
9. [What is a counterparty?](#q9-counterparty)
10. [What does stress testing mean?](#q10-stress-testing)
11. [How do you prioritize your team, Capgemini, BA, and client?](#q11-priorities)
12. [Is default risk market risk or credit risk?](#q12-default-risk)
13. [What is counterparty credit risk?](#q13-counterparty-credit-risk)
14. [What does yield mean?](#q14-yield)
15. [What is expected loss?](#q15-expected-loss)

<div style="page-break-after: always;"></div>

## Detailed Explanations

<a id="q1-cecl"></a>
### 1. What is CECL?

**CECL** means **Current Expected Credit Losses**. It is a U.S. GAAP accounting model for estimating expected credit losses on certain financial assets, such as loans.

A lender estimates expected losses using relevant historical information, current conditions, and reasonable forecasts, then records an allowance for those losses. CECL is an **accounting estimate**, not a stress test.

**Example:** If a bank expects some borrowers in its loan portfolio to default and not repay in full, it estimates that loss and sets aside a reserve.

**Interview answer:** “CECL is the U.S. GAAP model requiring lenders to estimate expected credit losses over the relevant asset life and recognize an allowance, using historical, current, and forecast information.”

<a id="q2-ccar"></a>
### 2. What is CCAR?

**CCAR** stands for **Comprehensive Capital Analysis and Review**. It is a U.S. Federal Reserve capital-planning and supervisory framework for large banking organizations. It assesses whether banks can maintain sufficient capital under stress and evaluates capital planning. The Federal Reserve’s stress-test program feeds into capital requirements such as the stress capital buffer, so CCAR is best described as part of the broader supervisory capital-planning framework.

**Example:** A bank estimates how a severe recession could affect loan losses, revenue, and its capital ratios.

**Interview answer:** “CCAR evaluates capital adequacy and capital planning at large U.S. banks, including how their capital positions hold up under stressful scenarios.”

<a id="q3-money-laundering"></a>
### 3. What is money laundering?

**Money laundering** is disguising the criminal origin of funds so they appear legitimate. A common model describes three stages:

1. **Placement:** Introduce illicit funds into the financial system.
2. **Layering:** Move funds through transactions to obscure their origin.
3. **Integration:** Make the funds appear to come from legitimate activity.

These stages can overlap. Banks use controls such as customer due diligence, transaction monitoring, and suspicious activity reporting to help detect financial crime.

<a id="q4-netting"></a>
### 4. What is netting?

**Netting** combines eligible amounts owed between parties so they settle a **net amount** instead of each gross amount.

**Example:** If Company A owes Bank B $10 million and Bank B owes Company A $7 million under an enforceable agreement, the net amount due may be $3 million from A to B.

In derivatives, **close-out netting** may determine one net payable amount when a counterparty defaults. Benefits depend on the relevant legal agreements and enforceability; it is not safe to offset arbitrary obligations.

<a id="q5-market-risk"></a>
### 5. What is market risk?

**Market risk** is the risk of loss from changes in market prices or rates—for example, interest rates, foreign exchange rates, equity prices, commodity prices, or credit spreads.

**Example:** A bank holds bonds. If interest rates rise, the market value of those bonds may fall.

<a id="q6-credit-risk"></a>
### 6. What is credit risk?

**Credit risk** is the risk that a borrower or other obligor will not meet payment obligations, causing a lender or investor to incur a loss.

**Example:** A borrower misses loan payments and eventually defaults. A common way to estimate credit loss uses **probability of default (PD)**, **loss given default (LGD)**, and **exposure at default (EAD)**.

<a id="q7-exposure"></a>
### 7. What is exposure?

**Exposure** is the amount a financial institution could lose if a borrower or counterparty fails to meet its obligations. The amount depends on the product and situation.

- For a loan, it may be the outstanding balance.
- For a credit line, it may include the amount likely to be drawn.
- For a derivative, it can change as market values change.

**EAD**, or **exposure at default**, estimates the exposure at the time of default.

<a id="q8-collateral-management"></a>
### 8. What is collateral management?

**Collateral management** is the process of accepting, valuing, monitoring, and exchanging assets that reduce risk between financial institutions.

It commonly includes checking collateral eligibility, valuing collateral and applying haircuts, calculating margin requirements, issuing and resolving margin calls, managing substitutions and returns, and recording and reconciling collateral.

**Example:** If a derivatives portfolio changes in value and leaves one party under-collateralized, it may need to post additional collateral.

Collateral reduces exposure; it does not necessarily eliminate it. Values can move, collateral can be difficult to liquidate, and legal or operational issues can delay recovery.

<a id="q9-counterparty"></a>
### 9. What is a counterparty?

A **counterparty** is the other party in a financial transaction.

**Examples:** In a loan, the borrower is the bank’s counterparty. In a derivatives trade, each institution is the other’s counterparty.

<a id="q10-stress-testing"></a>
### 10. What does stress testing mean?

**Stress testing** estimates how a financial institution or portfolio might perform under severe but plausible adverse scenarios. A scenario might involve a recession, sharply higher interest rates, falling property prices, or increased unemployment.

It helps answer: **“Would the firm have enough capital and liquidity to withstand this scenario?”** A stress scenario is a hypothetical test, not a prediction that the event will occur.

<a id="q11-priorities"></a>
### 11. How do you prioritize your team, Capgemini, BA, and client?

These interests should be aligned rather than treated as competing sides:

1. **Meet regulatory, security, and contractual obligations.**
2. **Understand the client’s business need and agreed priority.**
3. **Work with the BA** to clarify requirements, acceptance criteria, and changes.
4. **Coordinate with the team** to estimate, deliver, and communicate risks.
5. **Follow Capgemini’s policies and commitments** while escalating conflicts early.

Here, **BA** is interpreted as **Business Analyst**. If priorities conflict, make the trade-off visible and escalate instead of bypassing controls.

**Interview answer:** “I would first ensure compliance and contractual commitments, then align with the client on business impact. I’d clarify requirements with the BA, agree a realistic plan with the team, and communicate risks through the appropriate Capgemini channels. If priorities conflict, I’d make the trade-off visible and escalate rather than bypass controls.”

<a id="q12-default-risk"></a>
### 12. Is default risk market risk or credit risk?

**Generally, default risk is a form of credit risk:** it is the possibility that an obligor fails to meet its obligations.

There is a banking nuance: for certain **trading-book positions**, default-risk capital charges are handled within the regulatory market-risk framework. A clear interview response is: **“Conceptually, default risk is credit risk; its regulatory treatment can depend on the exposure and whether it is in the trading book.”**

<a id="q13-counterparty-credit-risk"></a>
### 13. What is counterparty credit risk?

**Counterparty credit risk (CCR)** is the risk that the other party to a transaction defaults before the transaction’s final cash flows are settled. It is especially relevant to derivatives and securities-financing transactions because their market value—and therefore the amount at risk—can change over time.

**Example:** A bank has a swap with a positive value. If its counterparty defaults before settlement, the bank may have to replace the transaction at a higher cost.

<a id="q14-yield"></a>
### 14. What does yield mean?

**Yield** is the return an investment provides, usually expressed as a percentage. For a bond, the **coupon rate** is based on its face value, while **yield to maturity** estimates the annualized return if the bond is held to maturity, accounting for its purchase price and payments. Bond prices and yields generally move in opposite directions.

<a id="q15-expected-loss"></a>
### 15. What is expected loss?

**Expected loss (EL)** is the average loss a lender estimates it may incur from credit exposure over a specified period. A common risk-model formula is:

**Expected Loss = PD × LGD × EAD**

- **PD:** Probability of default
- **LGD:** Percentage of exposure expected to be lost after recoveries
- **EAD:** Estimated exposure at the time of default

**Example:** If PD is 2%, LGD is 40%, and EAD is $1,000,000:

`0.02 × 0.40 × $1,000,000 = $8,000`

The modelled expected loss is **$8,000** for that exposure and time horizon. This formula is a useful risk-model illustration; accounting frameworks such as CECL can require lifetime estimates and more detailed methods.

## Quick Distinctions

| Concept | Short distinction |
|---|---|
| Market risk vs. credit risk | Market risk comes from market-price movements; credit risk comes from failure to pay. |
| Default risk | Usually credit risk; regulatory treatment can vary for trading-book exposures. |
| Exposure vs. expected loss | Exposure is the amount at risk; expected loss estimates the likely loss. |
| CECL vs. CCAR | CECL estimates accounting credit-loss allowances; CCAR concerns bank capital planning and supervisory stress analysis. |
| Counterparty vs. counterparty credit risk | A counterparty is the other party; CCR is the risk that party defaults before settlement. |

## Combined Interview Summary

“These concepts describe how banks measure and manage financial risk. Credit risk concerns non-payment; market risk comes from market movements; exposure measures the amount at risk; and expected loss combines default probability, loss severity, and exposure. Netting and collateral can mitigate exposure when legally and operationally effective. CECL is an accounting loss-estimation framework, while CCAR and stress testing assess capital resilience under adverse scenarios.”

## Useful References

- [FASB CECL overview](https://storage.fasb.org/Day%202_Topic%207_AFI%20-%20Impairment.pdf)
- [Federal Reserve: Stress Tests and Capital Planning](https://www.federalreserve.gov/supervisionreg/stress-tests-capital-planning.htm)
- [FFIEC BSA/AML Manual](https://bsaaml.ffiec.gov/manual/Introduction/01)
- [Basel Framework: Counterparty Credit Risk](https://www.bis.org/committees/bcbs/basel-consolidated-guidelines/module/cri/40)
- [Basel Framework: Market Risk](https://www.bis.org/committees/bcbs/basel-framework/standard/mar/11/inforce/2023-01-01/published/2020-03-27)
- [Investor.gov: Corporate Bonds and Yield](https://www.investor.gov/introduction-investing/general-resources/news-alerts/alerts-bulletins/investor-bulletins/what-are)
