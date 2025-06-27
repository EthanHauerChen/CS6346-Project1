package common;

public interface ILock {

    public String getLockName();

    public void lock(int processId);

    public void unlock(int processId);
}
