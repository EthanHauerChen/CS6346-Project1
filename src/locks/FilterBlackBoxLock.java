package locks;

import common.Counter;
import locks.GeneralizedPetersonLock;

public class FilterBlackBoxLock {
    private final int numProcesses;
    private final GeneralizedPetersonLock[] gadgets;

    public FilterBlackBoxLock(int numProcesses) {
        this.numProcesses = numProcesses;
        this.gadgets = new GeneralizedPetersonLock[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            gadgets[i] = new GeneralizedPetersonLock(numProcesses-i);
        }
    }

    public void acquireLock(int processId) {
        for (int i = 0; i <= numProcesses - 1; i++) {
            this.gadgets[i].acquireLock(processId-i > -1 ? processId-i : 0);
        }
    }

    public void releaseLock(int processId) {
        for (int i = numProcesses - 1; i >= 0; i--) {
            this.gadgets[i].releaseLock(processId-i > -1 ? processId-i : 0);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 5;
        int COUNTER_ITERATIONS = 10000;

        Counter counter = new Counter();
        Thread[] threads = new Thread[NUM_PROCESSES];
        FilterBlackBoxLock lock = new FilterBlackBoxLock(NUM_PROCESSES);

        for (int i = 0; i < NUM_PROCESSES; i++) {
            int process = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < COUNTER_ITERATIONS; j++) {
                    lock.acquireLock(process);
                    counter.incrementValue();
                    lock.releaseLock(process);
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
}
