## **Creational Patterns (How objects are created):**
1.	**Singleton**: Ensures that a class has only one instance and provides a global point of access to it.
2.	**Factory Method**: Defines an interface for creating an object, but lets subclasses decide which class to instantiate. 3. Abstract Factory: Provides an interface for creating families of related or dependent objects without specifying their concrete classes.
3.	**Builder**: Separates the construction of a complex object from its representation so that the same construction process can create different representations.
4.	**Prototype**: Specifies the kinds of objects to create using a prototypical instance, and creates new objects by copying this prototype.
---

## **Structural Patterns (How classes and objects are composed):**
6.	**Adapter (Wrapper)**: Converts the interface of a class into another interface clients expect. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.
7.	**Bridge**: Decouples an abstraction from its implementation so that the two can vary independently.
8.	**Composite**: Composes objects into tree structures to represent part-whole hierarchies. Composite lets clients treat individual objects and compositions of objects uniformly.
9.	**Decorator**: Dynamically adds responsibilities to an object. Decorators provide a flexible alternative to subclassing for extending functionality.
10.	**Facade**: Provides a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level interface that makes the subsystem easier to use.
11.	**Flyweight**: Uses sharing to support large numbers of fine-grained objects efficiently.
12.	**Proxy**: Provides a surrogate or placeholder for another object to control access to it.
---

## **Behavioral Patterns (How objects interact and distribute responsibility):**
13.	**Chain of Responsibility**: Avoids coupling the sender of a request to its receiver by giving more than one object a chance to handle the request. Chain the receiving objects and pass the request along the chain until an object handles it.
14.	**Command**: Encapsulates a request as an object, thereby letting you parameterize clients with queues, and request logging, and support undoable operations.
15.	**Interpreter**: Given a language, define a representation for its grammar along with an interpreter that uses the representation to interpret sentences in the language.
16.	**Iterator**: Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
17.	**Mediator**: Defines an object that encapsulates how a set of objects interact. Mediator promotes loose coupling by keeping objects from referring to each other explicitly, and it lets you vary their interaction independently.
18.	**Memento**: Without violating encapsulation, capture and externalize an object's internal state so that the object can be restored to this state later.
19.	**Observer**: Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
20.	**State**: Allows an object to alter its behavior when its internal state changes. The object will appear to change its class.
21.	**Strategy**: Defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from the clients that use it.
22.	**Template Method**: Defines the skeleton of an algorithm in an operation, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.
23.	**Visitor**: Represents an operation to be performed on the elements of an object structure. Visitor lets you define a new operation without changing the classes of the elements on which it operates.   
---
