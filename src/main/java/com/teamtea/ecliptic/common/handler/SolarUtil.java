package com.teamtea.ecliptic.common.handler;

import com.teamtea.ecliptic.api.solar.Season;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.core.solar.SolarDataManager;
import net.minecraft.world.level.Level;


public class SolarUtil {


    public static SolarDataManager getProvider(Level level) {
        return level == null ? null : AllListener.getSaveDataLazy(level).resolve().orElse(null);
    }

    public static Season getSeason(Level level) {
        return AllListener.getSaveDataLazy(level).map(data -> data.getSolarTerm().getSeason()).orElse(Season.NONE);
    }

    public static SolarTerm getSolarTerm(Level level) {
        return AllListener.getSaveDataLazy(level).map(SolarDataManager::getSolarTerm).orElse(SolarTerm.NONE);
    }
}
