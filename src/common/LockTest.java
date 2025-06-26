package common;

import java.util.ArrayList;

public class LockTest {
    private final ILock lock;
    private final int numRuns;
    private final int numProcesses;
    private final int numIterations;
    private final LockTestResults results;

    public LockTest(ILock lock, int numRuns, int numProcesses, int numIterations) {
        this.lock = lock;
        this.numRuns = numRuns;
        this.numProcesses = numProcesses;
        this.numIterations = numIterations;
        this.results = new LockTestResults();
    }

    public LockTestResults runAndGetResults() throws InterruptedException {
        for (int i = 0; i < this.numRuns; i++) this.runTest();
        return this.results;
    }

    private void runTest() throws InterruptedException {
        Counter counter = new Counter();
        Thread[] threads = new Thread[this.numProcesses];

        for (int i = 0; i < this.numProcesses; i++) {
            int process = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < this.numIterations; j++) {
                    lock.acquireLock(process);
                    counter.incrementValue();
                    lock.releaseLock(process);
                }
            });
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < this.numProcesses; i++) threads[i].start();
        for (int i = 0; i < this.numProcesses; i++) threads[i].join();
        long executionTime = System.currentTimeMillis() - startTime;
        this.results.addRunEntry(counter.getValue(), executionTime);
    }

    public class LockTestResults {
        private static final String CSV_SEPARATOR = ", ";
        private final ArrayList<Long> actualCounterValues = new ArrayList<>();
        private final ArrayList<Long> executionTimes = new ArrayList<>();

        public ArrayList<Long> getActualCounterValues() {
            return new ArrayList<>(this.actualCounterValues);
        }

        public ArrayList<Long> getExecutionTimes() {
            return new ArrayList<>(this.executionTimes);
        }

        public Long getAverageExecutionTime() {
            return this.executionTimes.stream().mapToLong(Long::longValue).sum() / numRuns;
        }

        public void addRunEntry(long actualCounterValue, long executionTime) {
            this.actualCounterValues.add(actualCounterValue);
            this.executionTimes.add(executionTime);
        }

        public static String getCsvTitle() {
            return new StringBuilder()
                .append("lock_name").append(CSV_SEPARATOR)
                .append("num_runs").append(CSV_SEPARATOR)
                .append("num_processes").append(CSV_SEPARATOR)
                .append("num_iterations").append(CSV_SEPARATOR)
                .append("avg_time_per_run_(ms)")
                .toString();
        }

        public String getCsvEntry() {
            return new StringBuilder()
                .append(lock.getLockName()).append(CSV_SEPARATOR)
                .append(numRuns).append(CSV_SEPARATOR)
                .append(numProcesses).append(CSV_SEPARATOR)
                .append(numIterations).append(CSV_SEPARATOR)
                .append(this.getAverageExecutionTime())
                .toString();
        }
    }
}
