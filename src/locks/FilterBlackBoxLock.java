package locks;

import common.ILock;
import common.LockTester;

public class FilterBlackBoxLock implements ILock {
    private final int numProcesses;
    private final GeneralizedPetersonLock[] gadgets;

    public FilterBlackBoxLock(int numProcesses) {
        this.numProcesses = numProcesses;
        this.gadgets = new GeneralizedPetersonLock[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            gadgets[i] = new GeneralizedPetersonLock(numProcesses - i);
        }
    }

    public void acquireLock(int processId) {
        for (int i = 0; i <= numProcesses - 1; i++) {
            this.gadgets[i].acquireLock(processId - i > -1 ? processId - i : 0);
        }
    }

    public void releaseLock(int processId) {
        for (int i = numProcesses - 1; i >= 0; i--) {
            this.gadgets[i].releaseLock(processId - i > -1 ? processId - i : 0);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 5;
        int COUNTER_ITERATIONS = 1000000;
        FilterBlackBoxLock lock = new FilterBlackBoxLock(NUM_PROCESSES);
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
