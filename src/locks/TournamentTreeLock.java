package locks;

import common.ILock;
import common.LockTester;

public class TournamentTreeLock implements ILock {
    PetersonLock[] nodes;

    public TournamentTreeLock(int numProcesses) {
        nodes = new PetersonLock[numProcesses - 1];
        for (int i = 0; i < numProcesses - 1; i++) nodes[i] = new PetersonLock();
    }

    @Override
    public String getLockName() {
        return "tournament";
    }

    @Override
    public void lock(int node) {
        acquire(node, nodes.length + 1, 0);
    }

    @Override
    public void unlock(int node) {
        release(node, nodes.length + 1, 0);
    }

    private void acquire(int id, int total, int offset) {
        if (total == 1) return;
        int instance = id / 2;
        nodes[offset + instance].lock(id % 2);
        acquire(instance, total / 2, offset + total / 2);
    }

    private void release(int id, int total, int offset) {
        if (total == 1) return;
        int instance = id / 2;
        release(instance, total / 2, offset + total / 2);
        nodes[offset + instance].unlock(id % 2);
    }

    public static void main(String[] args) throws InterruptedException {
        int NUM_PROCESSES = 16;
        int COUNTER_ITERATIONS = 1000000;
        TournamentTreeLock lock = new TournamentTreeLock(NUM_PROCESSES);
        LockTester.testLock(lock, NUM_PROCESSES, COUNTER_ITERATIONS);
    }
}
