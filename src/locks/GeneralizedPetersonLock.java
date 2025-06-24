package locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneralizedPetersonLock {
    public AtomicBoolean[] flag;
    public AtomicInteger victim = new AtomicInteger(-1);

    public GeneralizedPetersonLock(int numProcesses) {
        flag = new AtomicBoolean[numProcesses];
        for (int i = 0; i < numProcesses; i++) flag[i] = new AtomicBoolean(false);
    }

    public void acquireLock(int me) {
        flag[me].set(true);
        victim.set(me);

        //busy wait
        boolean anyOtherFlagsTrue = false;
        do {
            for (int i = 0; i < flag.length; i++) {
                if (flag[i].get()) {
                    anyOtherFlagsTrue = true;
                    break;
                }
            }
        } while(anyOtherFlagsTrue && victim.get() == me);
    }

    public void releaseLock(int me) {
        flag[me].set(false);
    }
}
