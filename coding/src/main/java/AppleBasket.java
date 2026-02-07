import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * taowifi@163.com
 *
 * Apple Orchard Concurrency Problem
 * You are implementing a simulation for an apple orchard. Multiple workers interact concurrently with a
 * shared basket of apples.
 *
 * Worker Types
 * 1. Producers: Each producer places one apple at a time into the basket.
 * 2. Consumers: Each consumer removes one apple at a time, but only when the basket has more than
 * 10 apples.
 *
 * Basket Constraints
 * - Maximum capacity: 100 apples
 * - Producers must wait if the basket is full.
 * - Consumers must wait if the basket has 10 or fewer apples.
 *
 * Your Task
 * Design and implement a thread-safe Java class named AppleBasket with the following API:
 * void putApple(); — called by producer threads
 * void takeApple(); — called by consumer threads
 * Your implementation must:
 * - Enforce all basket rules
 * - Prevent race conditions
 * - Avoid deadlocks
 *
 * Then write a short driver program that starts multiple producer and consumer threads and
 * demonstrates them interacting concurrently.
 * */
public class AppleBasket {
    // Since I’m using intrinsic locking with synchronized, I am choosing a non-concurrent queue.
    private final Queue<Integer> basket = new LinkedList<>();


    private static final int MAX_CAPACITY = 100;
    private static final int MIN_CAPACITY = 10;


    public synchronized void putApple(int appleId) throws InterruptedException {
        while (basket.size() >= MAX_CAPACITY) {
            System.out.println(Thread.currentThread().getName() + "Basket full");
            wait();
        }
        basket.add(appleId);
        System.out.println(Thread.currentThread().getName() + " produced apple " + appleId + " | size=" + basket.size());
        notifyAll();
    }


    public synchronized void takeApple() throws InterruptedException {
        while (basket.size() <= MIN_CAPACITY) {
            System.out.println(Thread.currentThread().getName() + " waiting: basket has <= " + MIN_CAPACITY);
            wait();
        }
        int apple = basket.remove();
        System.out.println(Thread.currentThread().getName() + " consumed apple " + apple + " | size=" + basket.size());
        notifyAll();
    }

    public static void main(String[] args) {

        AppleBasket basket = new AppleBasket();

        // Producer Runnable
        Runnable producer = () -> {
            int appleId = 0;
            try {
                while (true) {
                    basket.putApple(appleId++);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Consumer Runnable
        Runnable consumer = () -> {
            try {
                while (true) {
                    basket.takeApple();
                    Thread.sleep(100); // slow down consumption
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Start multiple producers
        for (int i = 1; i <= 3; i++) {
            new Thread(producer, "Producer-" + i).start();
        }

        // Start multiple consumers
        for (int i = 1; i <= 2; i++) {
            new Thread(consumer, "Consumer-" + i).start();
        }
    }
}
