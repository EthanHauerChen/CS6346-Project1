package locks;

import common.Counter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class PetersonLock {
    public AtomicBoolean[] flag = {new AtomicBoolean(false), new AtomicBoolean(false)};
    public AtomicInteger victim = new AtomicInteger(-1);

    public void acquireLeft(Runnable callback) {
        this.acquireLock(0, callback);
    }

    public void acquireRight(Runnable callback) {
        this.acquireLock(1, callback);
    }

    private int getNotMe(int me) {
        return (me == 0) ? 1 : 0;
    }

    private void acquireLock(int me, Runnable callback) {
        int notMe = getNotMe(me);
        flag[me].set(true);
        victim.set(me);

        while (flag[notMe].get() && victim.get() == me) { /* busy-wait */ }
        callback.run();
        flag[me].set(false);
    }

    public static void main(String[] args) throws InterruptedException {
        int COUNTER_ITERATIONS = 100000;
        Counter counter = new Counter();
        PetersonLock lock = new PetersonLock();

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < COUNTER_ITERATIONS; i++) {
                lock.acquireLeft(counter::incrementValue);
            }
        });

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < COUNTER_ITERATIONS; i++) {
                lock.acquireRight(counter::incrementValue);
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
