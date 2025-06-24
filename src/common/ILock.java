package common;

import locks.FilterBlackBoxLock;

public interface ILock {

    public void acquireLock(int processId);

    public void releaseLock(int processId);
}
