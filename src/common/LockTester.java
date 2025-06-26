package common;

import locks.BakeryBlackBoxLock;

public class LockTester {

    public static void testLock(ILock lock, int numProcesses, int numIterations) throws InterruptedException {
        LockTest test =  new LockTest(lock, 1, numProcesses, numIterations);
        LockTest.LockTestResults testResults = test.runAndGetResults();

        System.out.println("Execution time: " + testResults.getAverageExecutionTime());
        System.out.println("Expected counter value: " + (numProcesses * numIterations));
        System.out.println("Actual counter value: " + testResults.getActualCounterValues().get(0));
    }
}
