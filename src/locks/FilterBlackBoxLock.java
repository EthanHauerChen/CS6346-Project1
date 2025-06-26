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
            gadgets[i] = new GeneralizedPetersonLock(numProcesses);
        }
    }

    @Override
    public String getLockName() {
        return "filter_black_box";
    }

    @Override
    public void acquireLock(int processId) { //subtract 1 from processId for each subsequent GPL so process num 4 doesn't try to acquire flag[4] of a GPL that only has flag of length 2
        for (int i = 0; i <= numProcesses - 1; i++) {
            this.gadgets[i].acquireLock(processId);
        }
    }

    @Override
    public void releaseLock(int processId) {
        for (int i = numProcesses - 1; i >= 0; i--) {
            this.gadgets[i].releaseLock(processId);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 5;
        int COUNTER_ITERATIONS = 1000000;
        FilterBlackBoxLock lock = new FilterBlackBoxLock(NUM_PROCESSES);
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
