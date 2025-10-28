package org.sopt.domain.member.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static final AtomicLong sequence = new AtomicLong(1);

    private IdGenerator() {}

    public static long nextId() {
        return sequence.getAndIncrement();
    }

    public static void init(long startValue){
        sequence.set(startValue);
    }
}
