package com.teamtea.ecliptic.common.handler;

import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import com.teamtea.ecliptic.api.solar.Season;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.common.core.solar.SolarDataManager;
import net.minecraft.world.level.Level;


public class SolarUtil {


    public static SolarDataManager getProvider(Level level) {
        return level == null ? null : level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).resolve().orElse(null);
    }

    public static Season getSeason(Level level) {
        return level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(data -> data.getSolarTerm().getSeason()).orElse(Season.NONE);
    }

    public static SolarTerm getSolarTerm(Level level) {
        return level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(SolarDataManager::getSolarTerm).orElse(SolarTerm.NONE);
    }
}
