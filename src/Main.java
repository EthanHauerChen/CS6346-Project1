//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import common.Counter;
import locks.FilterBlackBoxLock;
import locks.TournamentTreeLock;

public class Main {
    public static void main(String[] args) {
        // do nothing
    }

    public static void testTournamentLock(int NUM_PROCESSES, int COUNTER_ITERATIONS) {
        //check if NUM_PROCESSES IS POWER OF 2
        int logValue = (int)(Math.log(NUM_PROCESSES) / Math.log(2));
        if (Math.pow(2, logValue) != NUM_PROCESSES) throw new IllegalArgumentException("num processes must be a power of 2");

        Counter counter = new Counter();
        Thread[] threads = new Thread[NUM_PROCESSES];
        TournamentTreeLock lock = new TournamentTreeLock(NUM_PROCESSES);

        //todo
    }
}