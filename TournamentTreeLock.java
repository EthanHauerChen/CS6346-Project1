import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class TournamentTreeLock {
    PetersonLock[] nodes;

    public TournamentTreeLock(int numProcesses) {
        nodes = new PetersonLock[numProcesses-1];
    }

    public void acquire(Runnable callback) {
        //assign the process to one of the leaf nodes that is available. need to figure out how to find out which node is available
    }
}
