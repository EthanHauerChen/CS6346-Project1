//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import common.Counter;
import locks.FilterBlackBoxLock;
import locks.TournamentTreeLock;
import locks.FilterTextbook;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        testFilterTB(10,100000);
    }

    public static void testFilterTB(int NUM_PROCESSES, int COUNTER_ITERATIONS) throws InterruptedException {
        System.out.println("Textbook Filter Lock with " + NUM_PROCESSES + " processes, " + COUNTER_ITERATIONS + " iterations");
        Counter counter = new Counter();
        Thread[] threads = new Thread[NUM_PROCESSES];
        FilterTextbook lock = new FilterTextbook(NUM_PROCESSES);

        for (int i = 0; i < NUM_PROCESSES; i++) {
            int process = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < COUNTER_ITERATIONS; j++) {
                    lock.lock(process);
                    counter.incrementValue();
                    lock.unlock(process);
                }
            });
        }

        for (int i = 0; i < NUM_PROCESSES; i++) {
            threads[i].start();
        }

        for (int i = 0; i < NUM_PROCESSES; i++) {
            threads[i].join();
        }

        System.out.println("Expected counter value: " + (COUNTER_ITERATIONS * NUM_PROCESSES));
        System.out.println("Actual counter value: " + counter.getValue());
    }

    //TODO, replace with the code from filterbb.java from filterbb branch
    public static void testFilterBB(int NUM_PROCESSES, int COUNTER_ITERATIONS) throws InterruptedException {
        System.out.println("Black Box Filter Lock with " + NUM_PROCESSES + " processes, " + COUNTER_ITERATIONS + " iterations");
        Counter counter = new Counter();
        Thread[] threads = new Thread[NUM_PROCESSES];
        FilterBlackBoxLock lock = new FilterBlackBoxLock(NUM_PROCESSES);

        for (int i = 0; i < NUM_PROCESSES; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < COUNTER_ITERATIONS; j++) {
                    lock.acquireLock(0);
                    counter.incrementValue();
                    lock.releaseLock(0);
                }
            });
        }

        for (int i = 0; i < NUM_PROCESSES; i++) {
            threads[i].start();
        }

        for (int i = 0; i < NUM_PROCESSES; i++) {
            threads[i].join();
        }

        System.out.println("Expected counter value: " + (COUNTER_ITERATIONS * NUM_PROCESSES));
        System.out.println("Actual counter value: " + counter.getValue());
    }

    public static void testTournamentLock(int NUM_PROCESSES, int COUNTER_ITERATIONS) throws InterruptedException {
        System.out.println("Tourney lock with " + NUM_PROCESSES + " processes, " + COUNTER_ITERATIONS + " iterations");
        //check if NUM_PROCESSES IS POWER OF 2 or 0
        int logValue = (int)(Math.log(NUM_PROCESSES) / Math.log(2));
        if (Math.pow(2, logValue) != NUM_PROCESSES) throw new IllegalArgumentException("num processes must be a power of 2");
        if (NUM_PROCESSES == 0) throw new IllegalArgumentException("num processes cannot be 0");

        Counter counter = new Counter();
        Thread[] threads = new Thread[NUM_PROCESSES];
        TournamentTreeLock lock = new TournamentTreeLock(NUM_PROCESSES);

        for (int i = 0; i < NUM_PROCESSES; i++) {
            int num = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < COUNTER_ITERATIONS; j++) {
                    lock.acquireLock(num);
                    counter.incrementValue();
                    lock.releaseLock(num);
                }
            });
        }

        for (int i = 0; i < NUM_PROCESSES; i++) threads[i].start();
        for (int i = 0; i < NUM_PROCESSES; i++) threads[i].join();

        System.out.println("Expected counter value: " + (COUNTER_ITERATIONS * NUM_PROCESSES));
        System.out.println("Actual counter value: " + counter.getValue());
    }
}