import common.Counter;
import common.LockTest;
import locks.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int NUM_RUNS = 5;
        int NUM_PROCESSES = 5;
        int NUM_ITERATIONS = 1000000;

        LockTest.LockTestResults filterBBResults = new LockTest(
                new FilterBlackBoxLock(NUM_PROCESSES), NUM_RUNS, NUM_PROCESSES, NUM_ITERATIONS
        ).runAndGetResults();

        LockTest.LockTestResults filterTBResults = new LockTest(
                new FilterTextBookLock(NUM_PROCESSES), NUM_RUNS, NUM_PROCESSES, NUM_ITERATIONS
        ).runAndGetResults();

        LockTest.LockTestResults bakeryBBResults = new LockTest(
                new BakeryBlackBoxLock(NUM_PROCESSES), NUM_RUNS, NUM_PROCESSES, NUM_ITERATIONS
        ).runAndGetResults();

        LockTest.LockTestResults bakeryTBResults = new LockTest(
                new BakeryTextBookLock(NUM_PROCESSES), NUM_RUNS, NUM_PROCESSES, NUM_ITERATIONS
        ).runAndGetResults();

        System.out.println(LockTest.LockTestResults.getCsvTitle());
        System.out.println(filterBBResults.getCsvEntry());
        System.out.println(filterTBResults.getCsvEntry());
        System.out.println(bakeryBBResults.getCsvEntry());
        System.out.println(bakeryTBResults.getCsvEntry());
    }
}