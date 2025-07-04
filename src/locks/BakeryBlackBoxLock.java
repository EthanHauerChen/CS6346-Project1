package locks;

import common.LockTest;
import common.LockTester;

public class BakeryBlackBoxLock extends AbstractBakeryLock {

    public BakeryBlackBoxLock(int numProcesses) {
        super(numProcesses);
    }

    @Override
    public String getLockName() {
        return "original_bakery";
    }

    @Override
    public void lock(int me) {
        this.flag[me].set(true);
        this.token[me].set(this.getMaxToken() + 1);
        this.flag[me].set(false);

        for (int i = 0; i < this.numProcesses; i++) {
            if (i == me) continue;
            while(this.flag[i].get()) {}
            while(this.token[i].get() != 0 && !this.tokenXIsLessThanTokenY(me, i)) {}
        }
    }

    @Override
    public void unlock(int me) {
        this.token[me].set(0);
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 16;
        int COUNTER_ITERATIONS = 1000000;
        BakeryBlackBoxLock lock = new BakeryBlackBoxLock(NUM_PROCESSES);
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
