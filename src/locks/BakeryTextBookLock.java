package locks;

import common.LockTester;

public class BakeryTextBookLock extends AbstractBakeryLock {

    public BakeryTextBookLock(int numProcesses) {
        super(numProcesses);
    }

    @Override
    public void acquireLock(int me) {
        this.flag[me].set(true);
        this.token[me].set(this.getMaxToken() + 1);
        while (this.hasConflict(me)) ;
    }

    @Override
    public void releaseLock(int me) {
        this.flag[me].set(false);
    }

    private boolean hasConflict(int me) {
        for (int k = 0; k < this.numProcesses; k++) {
            if (k != me && this.flag[k].get() && this.tokenXIsLessThanTokenY(k, me)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 5;
        int COUNTER_ITERATIONS = 1000000;
        BakeryTextBookLock lock = new BakeryTextBookLock(NUM_PROCESSES);
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
