package locks;

import java.util.concurrent.atomic.AtomicInteger;

public class FilterTextbook {
    AtomicInteger[] level;
    AtomicInteger[] victim;

    public FilterTextbook(int numProcesses) {
        level = new AtomicInteger[numProcesses];
        victim = new AtomicInteger[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            level[i] = new AtomicInteger(0);
            victim[i] = new AtomicInteger(-1);
        }
    }

    public void lock(int processId) {
        for (int i = 1; i < level.length; i++) {
            level[processId].set(i);
            victim[i].set(processId);

            //busy wait
            boolean existsProcessGtrOrEqToMe = false;
            do {
                for (int j = 0; j < level.length; j++) {
                    if (j == processId) continue;
                    if (level[j].get() >= i) {
                        existsProcessGtrOrEqToMe = true;
                        break;
                    }
                }
            } while(existsProcessGtrOrEqToMe && victim[i].get() == processId);
        }
    }
    
    //haofast's function. replace the do-while in lock() with while(hasConflict(processId, i)) and the filter lock will work properly
    private boolean hasConflict(int me, int level) {
        for (int k = 0; k < this.level.length; k++) {
            if (k != me && this.level[k].get() >= level && victim[level].get() == me) {
                return true;
            }
        }
        return false;
    }

    public void unlock(int processId) {
        level[processId].set(0);
    }
}
