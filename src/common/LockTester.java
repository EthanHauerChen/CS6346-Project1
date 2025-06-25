package common;

public class LockTester {

    public static void testLock(ILock lock, int numProcesses, int iterations) throws InterruptedException {
        Counter counter = new Counter();
        Thread[] threads = new Thread[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            int process = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) {
                    lock.acquireLock(process);
                    counter.incrementValue();
                    lock.releaseLock(process);
                }
            });
        }

        for (int i = 0; i < numProcesses; i++) {
            threads[i].start();
        }

        for (int i = 0; i < numProcesses; i++) {
            threads[i].join();
        }

        System.out.println("Expected counter value: " + (numProcesses * iterations));
        System.out.println("Actual counter value: " + counter.getValue());
    }
}
