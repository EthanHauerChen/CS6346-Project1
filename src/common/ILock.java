package common;

import locks.FilterBlackBoxLock;

public interface ILock {

    public String getLockName();

    public void acquireLock(int processId);

    public void releaseLock(int processId);
}
