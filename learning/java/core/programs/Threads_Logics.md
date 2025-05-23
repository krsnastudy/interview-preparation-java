### Print Odd Even Numbers

```java
public class OddEvenExample implements Runnable {
    @Override
    public void run() {

        for (int i = 1; i <= 10; i++) {
            synchronized (this) {
                if (i % 2 == 0 && Thread.currentThread().getName().equals("t2")) {
                    try {
                        notifyAll();
                        System.out.println(i);
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (i % 2 != 0
                        && Thread.currentThread().getName().equals("t1")) {
                    try {
                    	notify();
                        System.out.println(i);
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static void main(String[] args) {

    	OddEvenExample obj = new OddEvenExample();
        Thread t1 = new Thread(obj, "t1");
        Thread t2 = new Thread(obj, "t2");
        t1.start();
        t2.start();
    }
}
```
---
