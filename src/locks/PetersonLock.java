package locks;

import common.ILock;
import common.LockTester;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class PetersonLock implements ILock {
    public AtomicBoolean[] flag = {new AtomicBoolean(false), new AtomicBoolean(false)};
    public AtomicInteger victim = new AtomicInteger(-1);

    private int getNotMe(int me) {
        return (me == 0) ? 1 : 0;
    }

    @Override
    public String getLockName() {
        return "peterson";
    }

    @Override
    public void acquireLock(int me) {
        int notMe = getNotMe(me);
        flag[me].set(true);
        victim.set(me);
        while (flag[notMe].get() && victim.get() == me) { /* busy-wait */ }
    }

    @Override
    public void releaseLock(int me) {
        flag[me].set(false);
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 2;
        int COUNTER_ITERATIONS = 10000000;
        PetersonLock lock = new PetersonLock();
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
