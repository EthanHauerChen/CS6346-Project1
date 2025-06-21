import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

class PetersonLock {
    public boolean[] flag = {false, false};
    public int victim = -1;
    
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
        flag[me] = true;
        victim = me;
        
        while (flag[notMe] && victim == me) { /** busy-wait **/ }
        callback.run();
        flag[me] = false;
    }
    
    static class Counter {
        private AtomicInteger value = new AtomicInteger(0);
            
        public int getValue() { return value.get(); }
    
        public void incrementValue() { value.incrementAndGet(); }
    }

    public static void main(String[] args) throws InterruptedException {
        int COUNTER_ITERATIONS = 50000;
        Counter counter = new Counter();

        PetersonLock lock = new PetersonLock();
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < COUNTER_ITERATIONS; i++) {
                lock.acquireLeft(() -> counter.incrementValue());
                //System.out.println("Counter is " + counter.getValue());
            }
        });
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < COUNTER_ITERATIONS; i++) {
                lock.acquireRight(() -> counter.incrementValue());
                //System.out.println("Counter is " + counter.getValue());
            }
        });
        threadA.start();
        threadB.start();
        
        threadA.join();
        threadB.join();

        
        // for (int i = 0; i < COUNTER_ITERATIONS; i++) {
        //     PetersonLock lock = new PetersonLock();
            
        //     // simulate multiple processes competing to increment counter using threads
        //     Thread threadA = new Thread(() -> lock.acquireLeft(() -> counter.incrementValue()));
        //     Thread threadB = new Thread(() -> lock.acquireRight(() -> counter.incrementValue()));
            
        //     // allow threads to start
        //     threadA.start();
        //     threadB.start();
            
        //     // wait for threads to finish
        //     threadA.join();
        //     threadB.join();
        // }
        
        System.out.println("Expected counter value: " + (COUNTER_ITERATIONS * 2));
        System.out.println("Actual counter value: " + counter.getValue());
    }
}