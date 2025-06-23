package locks;

import common.Counter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class PetersonLock {
    public AtomicBoolean[] flag = {new AtomicBoolean(false), new AtomicBoolean(false)};
    public AtomicInteger victim = new AtomicInteger(-1);

    private int getNotMe(int me) {
        return (me == 0) ? 1 : 0;
    }

    public void acquireLock(int me) {
        int notMe = getNotMe(me);
        flag[me].set(true);
        victim.set(me);
        while (flag[notMe].get() && victim.get() == me) { /* busy-wait */ }
    }

    public void releaseLock(int me) {
        flag[me].set(false);
    }

    public static void main(String[] args) throws InterruptedException {
        int COUNTER_ITERATIONS = 10000000;
        Counter counter = new Counter();
        PetersonLock lock = new PetersonLock();

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < COUNTER_ITERATIONS; i++) {
                lock.acquireLock(0);
                counter.incrementValue();
                lock.releaseLock(0);
            }
        });

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < COUNTER_ITERATIONS; i++) {
                lock.acquireLock(1);
                counter.incrementValue();
                lock.releaseLock(1);
            }
        });

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        System.out.println("Expected counter value: " + (COUNTER_ITERATIONS * 2));
        System.out.println("Actual counter value: " + counter.getValue());
    }
}
