# 15. Interpreter Pattern (Behavioral)

> **One-liner:** Define a grammar as a class hierarchy and interpret sentences by evaluating the syntax tree.

## 1. The problem it solves

Business users want to configure formulas such as `principal rate * years * 100 /` without redeploying code.

## 2. Structure (participants)

- **AbstractExpression** - `Expr.eval(ctx)`.
- **TerminalExpression** - `Num`, `Var`.
- **NonTerminalExpression** - `Add`, `Sub`, `Mul`, `Div`.
- **Context** - variable map; **Parser** builds the tree from postfix tokens.

## 3. Real-life analogy

A calculator or SQL engine: text is parsed into a tree and the tree is evaluated.

## 4. Code walkthrough

File: `src/behavioral/InterpreterDemo.java` (self-contained, runnable)

Simple interest = `principal rate * years * 100 /` evaluates to 25500.0 for 100000, 8.5%, 3 years. Records + a stack-based postfix parser keep it tiny.

## 5. Where you've already seen it (JDK / Spring)

- `java.util.regex.Pattern`
- Spring Expression Language (SpEL)
- `java.text.Format` family

## 6. Interview questions & model answers

**Q1. When is Interpreter appropriate?**  
Small, stable grammars (rules, formulas, filters). For big grammars use ANTLR/parser generators.

**Q2. Why is it the least used pattern?**  
Each grammar rule = a class; complex grammars become unmaintainable and slow.

**Q3. Interpreter + Composite?**  
The syntax tree *is* a Composite; terminals are leaves.

**Q4. Interpreter + Visitor?**  
Visitor can add operations (print, optimise) on the AST without changing node classes.

## 7. Pitfalls / when NOT to use

- Performance: tree walking per evaluation.
- Error handling for malformed input (stack underflow) must be explicit.

## 8. Compare with similar patterns

**vs Strategy:** Interpreter evaluates a language; Strategy swaps an algorithm.

## 9. 30-second answer for the interview

"Interpreter is a behavioral pattern: define a grammar as a class hierarchy and interpret sentences by evaluating the syntax tree. A typical case: Business users want to configure formulas such as `principal rate * years * 100 /` without redeploying code. The trade-off I'd mention: performance: tree walking per evaluation."

## 10. Learning resources

- [Wikipedia - Interpreter pattern](https://en.wikipedia.org/wiki/Interpreter_pattern)
- [Baeldung - Spring Expression Language](https://www.baeldung.com/spring-expression-language)
- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF book)](https://en.wikipedia.org/wiki/Design_Patterns)
- [ANTLR](https://www.antlr.org/)
