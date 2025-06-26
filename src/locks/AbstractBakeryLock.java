package locks;

import common.ILock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractBakeryLock implements ILock {
    protected int numProcesses;
    protected volatile AtomicBoolean[] flag;
    protected volatile AtomicInteger[] token;

    public AbstractBakeryLock(int numProcesses) {
        this.numProcesses = numProcesses;
        this.flag = new AtomicBoolean[numProcesses];
        this.token = new AtomicInteger[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            this.flag[i] = new AtomicBoolean(false);
            this.token[i] = new AtomicInteger(0);
        }
    }

    protected int getMaxToken() {
        int max = 0;
        for (int i = 0; i < this.numProcesses; i++) {
            max = Math.max(max, this.token[i].get());
        }
        return max;
    }

    protected boolean tokenXIsLessThanTokenY(int processX, int processY) {
        int labelX = this.token[processX].get();
        int labelY = this.token[processY].get();
        if (labelX < labelY) return true;
        return labelX == labelY && processX < processY;
    }
}
