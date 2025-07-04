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
        long[] turnaroundTime = new long[numProcesses * numIterations];

        for (int i = 0; i < this.numProcesses; i++) {
            int processId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < this.numIterations; j++) {
                    long startLockTime = System.nanoTime();
                    lock.lock(processId);
                    if (counter.getValue() < numIterations) {
                        counter.incrementValue();
                    }
                    else {
                        lock.unlock(processId);
                        break;
                    }
                    lock.unlock(processId);
                    long executionLockTime = System.nanoTime() - startLockTime;
                    turnaroundTime[processId * numIterations + j] = executionLockTime;

                    if (counter.getValue() >= numIterations) break;
                }
            });
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < this.numProcesses; i++) threads[i].start();
        for (int i = 0; i < this.numProcesses; i++) threads[i].join();
        long executionTime = System.currentTimeMillis() - startTime;
        ArrayList<Long> turnaroundTimeList = new ArrayList<>();
        for (long time : turnaroundTime) turnaroundTimeList.add(time);
        this.results.addRunEntry(counter.getValue(), executionTime, turnaroundTimeList);
    }

    public class LockTestResults {
        private static final String CSV_SEPARATOR = ", ";
        private final ArrayList<Long> actualCounterValues = new ArrayList<>();
        private final ArrayList<Long> executionTimes = new ArrayList<>();
        private final ArrayList<ArrayList<Long>> turnaroundTimes = new ArrayList<>();

        public ArrayList<Long> getActualCounterValues() {
            return new ArrayList<>(this.actualCounterValues);
        }

        public ArrayList<Long> getExecutionTimes() {
            return new ArrayList<>(this.executionTimes);
        }

        public ArrayList<Long> getTurnAroundTimes() {
            ArrayList<Long> flattenedList = new ArrayList<>();
            this.turnaroundTimes.forEach(flattenedList::addAll);
            return flattenedList;
        }

        public Long getAverageExecutionTime() {
            return this.executionTimes.stream().mapToLong(Long::longValue).sum() / numRuns;
        }

        public Long getAverageTurnaroundTime() {
            return this.getTurnAroundTimes().stream().mapToLong(Long::longValue).sum() / ((long) numIterations);
        }

        public Long getThroughput() {
            return numIterations / getAverageExecutionTime();
        }

        public void addRunEntry(long actualCounterValue, long executionTime, ArrayList<Long> turnaroundTime) {
            this.actualCounterValues.add(actualCounterValue);
            this.executionTimes.add(executionTime);
            this.turnaroundTimes.add(turnaroundTime);
        }

        public static String getCsvTitle() {
            return new StringBuilder()
                .append("lock_name").append(CSV_SEPARATOR)
                .append("num_runs").append(CSV_SEPARATOR)
                .append("num_processes").append(CSV_SEPARATOR)
                .append("num_iterations").append(CSV_SEPARATOR)
                .append("avg_turnaround_time_(ns)").append(CSV_SEPARATOR)
                .append("avg_time_per_run_(ms)").append(CSV_SEPARATOR)
                .append("avg_throughput(iterations / ms)")
                .toString();
        }

        public String getCsvEntry() {
            return new StringBuilder()
                .append(lock.getLockName()).append(CSV_SEPARATOR)
                .append(numRuns).append(CSV_SEPARATOR)
                .append(numProcesses).append(CSV_SEPARATOR)
                .append(numIterations).append(CSV_SEPARATOR)
                .append(this.getAverageTurnaroundTime()).append(CSV_SEPARATOR)
                .append(this.getAverageExecutionTime()).append(CSV_SEPARATOR)
                .append(this.getThroughput())
                .toString();
        }
    }
}
