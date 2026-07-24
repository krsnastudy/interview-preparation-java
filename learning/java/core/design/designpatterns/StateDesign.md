A **Traffic Signal System** typically follows the **State Design Pattern** in Java.

---

### 🟢 **Why State Pattern?**

The **State Pattern** allows an object to change its behavior when its internal state changes. It’s ideal when an object can be in **multiple states**, and its behavior varies based on that state — **just like a traffic light**.

---

### 🚦 **Traffic Signal States:**
- **Red** → Stop
- **Green** → Go
- **Yellow** → Prepare to stop

Each of these is a state, and the traffic signal **transitions** between them in a **cycle**.

---

### ✅ Real-world Analogy in Code (Simplified):

```java
// State interface
interface TrafficLightState {
    void next(TrafficLight light);
    String getColor();
}

// Concrete states
class RedLight implements TrafficLightState {
    public void next(TrafficLight light) {
        light.setState(new GreenLight());
    }
    public String getColor() {
        return "Red";
    }
}

class GreenLight implements TrafficLightState {
    public void next(TrafficLight light) {
        light.setState(new YellowLight());
    }
    public String getColor() {
        return "Green";
    }
}

class YellowLight implements TrafficLightState {
    public void next(TrafficLight light) {
        light.setState(new RedLight());
    }
    public String getColor() {
        return "Yellow";
    }
}

// Context
class TrafficLight {
    private TrafficLightState state;

    public TrafficLight() {
        state = new RedLight(); // initial state
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public void change() {
        state.next(this);
    }

    public void show() {
        System.out.println("Current Light: " + state.getColor());
    }
}
```

### 🧪 Usage:
```java
public class Main {
    public static void main(String[] args) {
        TrafficLight signal = new TrafficLight();
        for (int i = 0; i < 6; i++) {
            signal.show();
            signal.change();
        }
    }
}
```

---

### 🎯 Interview Tip:
> "Traffic signals are a classic example of the **State Design Pattern**, where each light (Red, Green, Yellow) is a state, and the signal object transitions between these states in a controlled way."

---

Want a UML diagram or more complex version with timers and concurrency?