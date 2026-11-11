# GoF Design Patterns - Interview Explanations (23)

One markdown note per pattern. Each follows the same layout: **one-liner -> problem -> structure -> real-life analogy -> code walkthrough (linked to the runnable demo) -> JDK/Spring usage -> interview Q&A -> pitfalls -> comparisons -> resources**.

Run the code: `./run-all.sh` or `java src/<category>/<Name>Demo.java` from the project root.


## Creational - How objects get created

| # | Pattern | One-liner | Demo |
|---|---|---|---|
| 1 | [Singleton](creational/01-Singleton.md) | Guarantee exactly one instance of a class and give a global access point to it. | `src/creational/SingletonDemo.java` |
| 2 | [Factory Method](creational/02-FactoryMethod.md) | Define an interface for creating an object but let subclasses decide which class to instantiate. | `src/creational/FactoryMethodDemo.java` |
| 3 | [Abstract Factory](creational/03-AbstractFactory.md) | Provide an interface for creating *families* of related objects without naming their concrete classes. | `src/creational/AbstractFactoryDemo.java` |
| 4 | [Builder](creational/04-Builder.md) | Separate the construction of a complex object from its representation, step by step. | `src/creational/BuilderDemo.java` |
| 5 | [Prototype](creational/05-Prototype.md) | Create new objects by copying (cloning) an existing, pre-configured instance. | `src/creational/PrototypeDemo.java` |

## Structural - How objects/classes are composed

| # | Pattern | One-liner | Demo |
|---|---|---|---|
| 6 | [Adapter](structural/06-Adapter.md) | Convert the interface of an existing class into the interface clients expect. | `src/structural/AdapterDemo.java` |
| 7 | [Bridge](structural/07-Bridge.md) | Decouple an abstraction from its implementation so the two can vary independently. | `src/structural/BridgeDemo.java` |
| 8 | [Composite](structural/08-Composite.md) | Compose objects into tree structures and let clients treat single objects and groups uniformly. | `src/structural/CompositeDemo.java` |
| 9 | [Decorator](structural/09-Decorator.md) | Attach additional responsibilities to an object dynamically by wrapping it - same interface in and out. | `src/structural/DecoratorDemo.java` |
| 10 | [Facade](structural/10-Facade.md) | Provide a simple, unified interface to a complex subsystem. | `src/structural/FacadeDemo.java` |
| 11 | [Flyweight](structural/11-Flyweight.md) | Share fine-grained immutable objects to save memory instead of creating many identical ones. | `src/structural/FlyweightDemo.java` |
| 12 | [Proxy](structural/12-Proxy.md) | Provide a surrogate that controls access to another object (lazy load, cache, security, logging). | `src/structural/ProxyDemo.java` |

## Behavioral - How objects communicate and share responsibility

| # | Pattern | One-liner | Demo |
|---|---|---|---|
| 13 | [Chain of Responsibility](behavioral/13-ChainOfResponsibility.md) | Pass a request along a chain of handlers; each decides to handle it, reject it, or pass it on. | `src/behavioral/ChainOfResponsibilityDemo.java` |
| 14 | [Command](behavioral/14-Command.md) | Turn a request into a stand-alone object so it can be queued, logged, retried or undone. | `src/behavioral/CommandDemo.java` |
| 15 | [Interpreter](behavioral/15-Interpreter.md) | Define a grammar as a class hierarchy and interpret sentences by evaluating the syntax tree. | `src/behavioral/InterpreterDemo.java` |
| 16 | [Iterator](behavioral/16-Iterator.md) | Provide a way to traverse a collection sequentially without exposing its internal representation. | `src/behavioral/IteratorDemo.java` |
| 17 | [Mediator](behavioral/17-Mediator.md) | Centralise complex communication between objects so they don't reference each other directly. | `src/behavioral/MediatorDemo.java` |
| 18 | [Memento](behavioral/18-Memento.md) | Capture and externalise an object's state so it can be restored later without breaking encapsulation. | `src/behavioral/MementoDemo.java` |
| 19 | [Observer](behavioral/19-Observer.md) | Define a one-to-many dependency so that when one object changes state, all dependents are notified. | `src/behavioral/ObserverDemo.java` |
| 20 | [State](behavioral/20-State.md) | Let an object alter its behaviour when its internal state changes - it appears to change class. | `src/behavioral/StateDemo.java` |
| 21 | [Strategy](behavioral/21-Strategy.md) | Define a family of interchangeable algorithms behind an interface and select one at runtime. | `src/behavioral/StrategyDemo.java` |
| 22 | [Template Method](behavioral/22-TemplateMethod.md) | Define the skeleton of an algorithm in a base class and let subclasses override specific steps. | `src/behavioral/TemplateMethodDemo.java` |
| 23 | [Visitor](behavioral/23-Visitor.md) | Add new operations to a set of classes without modifying them, using double dispatch. | `src/behavioral/VisitorDemo.java` |

## Quick selection cheat-sheet

| If the interviewer says... | Think |
|---|---|
| "exactly one instance" | Singleton |
| "create without naming the class" | Factory Method / Abstract Factory |
| "many optional constructor params" | Builder |
| "copy an existing object" | Prototype |
| "incompatible interface / legacy system" | Adapter |
| "two dimensions of variation" | Bridge |
| "tree, part-whole" | Composite |
| "add behaviour at runtime, same interface" | Decorator |
| "simplify a complex subsystem" | Facade |
| "millions of similar objects, memory" | Flyweight |
| "control access, lazy load, AOP" | Proxy |
| "pipeline of checks / filters" | Chain of Responsibility |
| "undo, queue, audit a request" | Command |
| "mini language / formula" | Interpreter |
| "traverse without exposing structure" | Iterator |
| "many-to-many communication" | Mediator |
| "snapshot and restore" | Memento |
| "notify subscribers" | Observer |
| "behaviour depends on state" | State |
| "swap algorithm at runtime" | Strategy |
| "fixed skeleton, vary steps" | Template Method |
| "new operation on a stable type hierarchy" | Visitor |

## Look-alike traps (most-asked comparisons)
- **Strategy vs State** - same shape; Strategy is client-chosen, State self-transitions.
- **Decorator vs Proxy** - Decorator adds behaviour; Proxy controls access.
- **Adapter vs Facade vs Bridge** - fit an interface / simplify many / split two hierarchies.
- **Factory Method vs Abstract Factory** - one product via inheritance vs a family via composition.
- **Observer vs Mediator** - one-to-many broadcast vs many-to-many coordination.
- **Template Method vs Strategy** - inheritance vs composition.

