import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class TournamentTreeLock {
    PetersonLock[] nodes;

    public TournamentTreeLock(int numProcesses) {
        nodes = new PetersonLock[numProcesses];
    }
}
