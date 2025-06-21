package locks;

public class TournamentTreeLock {
    PetersonLock[] nodes;

    public TournamentTreeLock(int numProcesses) {
        nodes = new PetersonLock[numProcesses-1];
    }

    public void acquireLock(Thread[] processes) {
        if (processes.length != nodes.length+1) throw new IllegalArgumentException("array size does not match");
        
        if (processes.length == 1) {
            acquire(0, 1, 0);
            processes[0].start();
            release(0, 1, 0);
            return;
        }
        for (int i = 0; i < processes.length; i++) {
            //todo
        }
    }

    private void acquire(int id, int total, int offset) {
        if (total == 1) return;
        int instance = id / 2;
        nodes[offset+instance].acquireLock(id % 2);
        acquire(instance, total/2, offset + total/2);
    }

    private void release(int id, int total, int offset) {
        if (total == 1) return;
        int instance = id / 2;
        release(instance, total/2, offset + total/2);
        nodes[offset+instance].releaseLock(id % 2);
    }
}
