package com.teamtea.ecliptic.common.core;

import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import com.teamtea.ecliptic.api.solar.Season;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import net.minecraft.world.level.Level;


public class SolarUtil {



    public static SolarDataRunner getProvider(Level level) {

        return level==null?null:level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).resolve().orElse(null);
    }

    public static Season getSeason(Level world)
    {
        return world.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(data -> data.getSolarTerm().getSeason()).orElse(Season.NONE);
    }

    public static SolarTerm getSolarTerm(Level world)
    {
        return world.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(SolarDataRunner::getSolarTerm).orElse(SolarTerm.NONE);
    }
}
