package locks;

import common.ILock;
import common.LockTester;

public class FilterBlackBoxLock implements ILock {
    private final int numProcesses;
    private final PetersonLock[] gadgets;

    public FilterBlackBoxLock(int numProcesses) {
        this.numProcesses = numProcesses;
        this.gadgets = new PetersonLock[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            gadgets[i] = new PetersonLock();
        }
    }

    public void acquireLock(int processId) {
        for (int i = 0; i <= numProcesses - 1; i++) {
            this.gadgets[i].acquireLock(processId % 2);
        }
    }

    public void releaseLock(int processId) {
        for (int i = numProcesses - 1; i >= 0; i--) {
            this.gadgets[i].releaseLock(processId % 2);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 5;
        int COUNTER_ITERATIONS = 1000000;
        FilterBlackBoxLock lock = new FilterBlackBoxLock(NUM_PROCESSES);
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
