package com.teamtea.ecliptic.api.util;

import com.teamtea.ecliptic.Ecliptic;

import java.util.function.Supplier;

public class SimpleUtil {
    public static void testTime(Runnable runnable) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100000 * 100; i++) {
            runnable.run();
        }
        Ecliptic.logger(System.currentTimeMillis() - time);
    }
}
