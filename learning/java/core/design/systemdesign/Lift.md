Designing a **Lift (Elevator) System** is a classic **object-oriented and system design interview question**, testing both **real-time system modeling** and **algorithm design**.

Let’s walk through a complete **interview-ready Lift System Design**:

---

## 🧭 **Problem Statement**

Design a system to manage multiple elevators in a building that:

* Moves elevators efficiently.
* Handles pickup and destination requests.
* Supports multiple users requesting elevators at the same time.

---

## ✅ Functional Requirements

1. Multiple elevators in a building (say 10 floors, 3 lifts).
2. Each elevator responds to:

    * **External calls**: floor button (Up/Down).
    * **Internal calls**: floor selection inside the elevator.
3. Real-time state tracking (floor, direction, requests).
4. Efficient scheduling (minimize wait/travel time).

---

## ❌ Non-Functional Requirements

* **Low latency** in response time.
* **Fault tolerance** (if a lift fails).
* Scalable to N elevators.

---

## 🔧 High-Level Components

```
+------------------------+
|     Elevator System    |
+-----------+------------+
            |
    +-------v--------+
    | ElevatorManager | <--- Controls all elevators
    +-------+--------+
            |
   +--------v--------+
   |    Elevators     |
   +--------+--------+
            |
   +--------v--------+
   | Control Panel    |
   +------------------+
```

---

## 🧱 Classes & Responsibilities

### 🔹 `ElevatorSystem`

* Initializes elevators
* Accepts external requests (floor, direction)
* Routes to appropriate elevator

### 🔹 `Elevator`

* Maintains:

    * Current floor
    * Direction
    * State (idle/moving/stopped)
    * Internal queue
* Executes move logic

### 🔹 `Request`

```java
class Request {
    int floor;
    Direction direction; // UP or DOWN
}
```

### 🔹 `ElevatorManager`

* Assigns requests to elevators
* Uses scheduling algorithm (e.g., Nearest-Car, FCFS, SCAN)

---

## 🧠 Scheduling Algorithms

1. **FCFS** (First-Come-First-Served)
2. **Nearest Car Algorithm**:

    * Assign request to elevator closest and going in that direction.
3. **SCAN (Elevator Algorithm)**:

    * Elevator moves in one direction servicing requests, then reverses.

---

## ⚙️ Elevator States

* **IDLE**
* **MOVING**
* **STOPPED**
* **MAINTENANCE**

---

## 💬 Sample OOP Design (Java-like)

```java
enum Direction { UP, DOWN, IDLE }

class Elevator {
    int id;
    int currentFloor;
    Direction direction;
    TreeSet<Integer> requests;

    void addRequest(int floor) {
        requests.add(floor);
    }

    void step() {
        // Move one floor towards next request
    }
}
```

```java
class ElevatorSystem {
    List<Elevator> elevators;

    void pickupRequest(int floor, Direction direction) {
        Elevator e = assignElevator(floor, direction);
        e.addRequest(floor);
    }

    Elevator assignElevator(int floor, Direction dir) {
        // Nearest-Car algorithm
    }

    void stepAll() {
        for (Elevator e : elevators)
            e.step();
    }
}
```

---

## ⚠️ Edge Cases

* Two users request the same floor in opposite directions.
* Elevator is full — must skip requests.
* Faulty elevator (state = MAINTENANCE).
* Prioritize emergency overrides (e.g., fire, VIP).

---

## 🛠️ Enhancements

* Predictive analytics (peak hour optimization).
* Sensor input integration.
* Energy-saving modes.
* Display system per floor.

---

## ✅ TL;DR

* Model elevators as **stateful objects** with floor queues.
* Use a **scheduler** to assign external requests.
* Choose appropriate algorithm (**SCAN**, **Nearest-Car**) for fairness + efficiency.
* Support **step-wise movement**, and allow **concurrent elevator simulation**.

---
Here’s a **complete Java-like OOP implementation** of the **core classes** for a Lift (Elevator) System, suitable for interviews. This implementation is designed for simulation purposes and follows clean architecture principles.

---

## 🧱 Class Diagram (Text-based)

```
ElevatorSystem
 ├── List<Elevator>
 └── assignElevator(), pickupRequest(), stepAll()

Elevator
 ├── int id, currentFloor
 ├── Direction direction
 ├── TreeSet<Integer> requests
 └── step(), addRequest()

Direction (enum): UP, DOWN, IDLE

Request
 ├── int floor
 └── Direction direction
```

---

## ✅ Java-Like Code Implementation

### 🔹 `Direction` Enum

```java
enum Direction {
    UP, DOWN, IDLE;
}
```

---

### 🔹 `Request` Class

```java
class Request {
    int floor;
    Direction direction;

    Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }
}
```

---

### 🔹 `Elevator` Class

```java
class Elevator {
    int id;
    int currentFloor;
    Direction direction;
    TreeSet<Integer> requests; // Sorted set for ordered floor stops

    Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.requests = new TreeSet<>();
    }

    void addRequest(int floor) {
        requests.add(floor);
        updateDirection();
    }

    void step() {
        if (requests.isEmpty()) {
            direction = Direction.IDLE;
            return;
        }

        int destination = (direction == Direction.UP || direction == Direction.IDLE) ? 
                            requests.ceiling(currentFloor) : requests.floor(currentFloor);

        if (destination == currentFloor) {
            System.out.println("Elevator " + id + " stopped at floor " + currentFloor);
            requests.remove(currentFloor);
        } else {
            currentFloor += (destination > currentFloor) ? 1 : -1;
        }

        updateDirection();
    }

    void updateDirection() {
        if (requests.isEmpty()) {
            direction = Direction.IDLE;
        } else if (requests.first() > currentFloor) {
            direction = Direction.UP;
        } else if (requests.last() < currentFloor) {
            direction = Direction.DOWN;
        }
    }
}
```

---

### 🔹 `ElevatorSystem` Class

```java
class ElevatorSystem {
    List<Elevator> elevators;

    ElevatorSystem(int n) {
        elevators = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            elevators.add(new Elevator(i));
        }
    }

    void pickupRequest(int floor, Direction direction) {
        Elevator e = assignElevator(floor, direction);
        if (e != null) {
            e.addRequest(floor);
        }
    }

    Elevator assignElevator(int floor, Direction dir) {
        Elevator best = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            if (e.direction == Direction.IDLE || e.direction == dir) {
                int distance = Math.abs(e.currentFloor - floor);
                if (distance < minDistance) {
                    minDistance = distance;
                    best = e;
                }
            }
        }

        return best != null ? best : elevators.get(0); // Fallback
    }

    void stepAll() {
        for (Elevator e : elevators) {
            e.step();
        }
    }
}
```

---

## 🧪 Sample Usage

```java
public class Main {
    public static void main(String[] args) {
        ElevatorSystem system = new ElevatorSystem(3);

        system.pickupRequest(5, Direction.UP);
        system.pickupRequest(2, Direction.UP);
        system.pickupRequest(7, Direction.DOWN);

        for (int i = 0; i < 10; i++) {
            system.stepAll();
            Thread.sleep(1000); // simulate real time
        }
    }
}
```

---

## ✅ TL;DR

* **Elevator** holds its state and floor queue.
* **ElevatorSystem** assigns requests using a greedy nearest-elevator strategy.
* Simple **step-based simulation** mimics real-time behavior.
* Extensible for **priority**, **maintenance**, or **full-system scheduling**.

---
