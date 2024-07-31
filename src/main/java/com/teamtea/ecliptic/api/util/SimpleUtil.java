package com.teamtea.ecliptic.api.util;

import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.api.constant.solar.SolarTerm;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.core.solar.SolarAngelHelper;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;


// for other mod use
public class SimpleUtil {
    public static void testTime(Runnable runnable) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 100000 * 100; i++) {
            runnable.run();
        }
        Ecliptic.logger(System.currentTimeMillis() - time);
    }

    public static SolarTerm getNowSolarTerm(Level level) {
        var sd = AllListener.getSaveData(level);
        if (sd != null) return sd.getSolarTerm();
        return SolarTerm.NONE;
    }

    public static boolean isDay(Level level) {
        long dayTime = level.dimensionType().fixedTime().orElse(SolarAngelHelper.getSolarAngelTime(level, level.getDayTime()));
        long termTime = getNowSolarTerm(level).getDayTime();
        long halfTermTime = termTime / 2;
        if (termTime <= 12000) {
            return 6000 - (halfTermTime) < dayTime && dayTime < 6000 + (halfTermTime);
        } else return dayTime >= 24000 + (6000 - (halfTermTime))
                || dayTime <= 6000 + (halfTermTime);
    }

    public static boolean isNight(Level level) {
        return !isDay(level);
    }
}
