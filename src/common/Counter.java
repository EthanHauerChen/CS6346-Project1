package common;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private int value = 0;

    public int getValue() {
        return value;
    }

    public void incrementValue() {
        value++;
    }
}
