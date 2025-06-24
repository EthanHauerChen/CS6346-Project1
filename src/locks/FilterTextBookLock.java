package locks;

import common.ILock;
import common.LockTester;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class FilterTextBookLock implements ILock {
    public int numProcesses;
    public volatile AtomicIntegerArray level;
    public volatile AtomicIntegerArray victim;

    public FilterTextBookLock(int numProcesses) {
        this.numProcesses = numProcesses;
        this.level = new AtomicIntegerArray(numProcesses);
        this.victim = new AtomicIntegerArray(numProcesses);
    }

    @Override
    public void acquireLock(int me) {
        for (int l = 1; l < this.numProcesses; l++) {
            level.set(me, l);
            victim.set(l, me);
            while (this.hasConflict(me, l)) { /* busy-wait */ }
        }
    }

    @Override
    public void releaseLock(int me) {
        this.level.set(me, 0);
    }

    private boolean hasConflict(int me, int level) {
        for (int k = 0; k < this.numProcesses; k++) {
            if (k != me && this.level.get(k) >= level && victim.get(level) == me) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 5;
        int COUNTER_ITERATIONS = 1000000;
        FilterTextBookLock lock = new FilterTextBookLock(NUM_PROCESSES);
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
