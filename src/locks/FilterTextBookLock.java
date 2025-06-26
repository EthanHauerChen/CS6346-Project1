package locks;

import common.ILock;
import common.LockTester;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class FilterTextBookLock implements ILock {
    public int numProcesses;
    public volatile AtomicInteger[] level;
    public volatile AtomicInteger[] victim;

    public FilterTextBookLock(int numProcesses) {
        this.numProcesses = numProcesses;
        this.level = new AtomicInteger[numProcesses];
        this.victim = new AtomicInteger[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            this.level[i] = new AtomicInteger(0);
            this.victim[i] = new AtomicInteger(-1);
        }
    }

    @Override
    public String getLockName() {
        return "filter_text_book";
    }

    @Override
    public void acquireLock(int me) {
        for (int l = 1; l < this.numProcesses; l++) {
            level[me].set(l);
            victim[l].set(me);
            while (this.hasConflict(me, l)) { /* busy-wait */ }
        }
    }

    @Override
    public void releaseLock(int me) {
        this.level[me].set(0);
    }

    private boolean hasConflict(int me, int level) {
        for (int k = 0; k < this.numProcesses; k++) {
            if (k != me && this.level[k].get() >= level && victim[level].get() == me) {
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
