package common;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private final AtomicInteger value = new AtomicInteger(0);

    public int getValue() {
        return value.get();
    }

    public void incrementValue() {
        value.incrementAndGet();
    }
}
