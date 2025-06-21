package locks;

public class TournamentTreeLock {
    PetersonLock[] nodes;

    public TournamentTreeLock(int numProcesses) {
        nodes = new PetersonLock[numProcesses];
    }
}
