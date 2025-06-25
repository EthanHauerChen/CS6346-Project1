package locks;

import common.ILock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneralizedPetersonLock implements ILock {
    public AtomicBoolean[] flag;
    public AtomicInteger victim = new AtomicInteger(-1);

    public GeneralizedPetersonLock(int numProcesses) {
        flag = new AtomicBoolean[numProcesses];
        for (int i = 0; i < numProcesses; i++) flag[i] = new AtomicBoolean(false);
    }

    @Override
    public void acquireLock(int me) {
        flag[me].set(true);
        victim.set(me);

        //busy wait
        while (hasConflict(me) && victim.get() == me);
    }

    @Override
    public void releaseLock(int me) {
        flag[me].set(false);
    }

    private boolean hasConflict(int me) {
        for (int k = 0; k < flag.length; k++) {
            if (k != me && flag[k].get()) {
                return true;
            }
        }
        return false;
    }
}
