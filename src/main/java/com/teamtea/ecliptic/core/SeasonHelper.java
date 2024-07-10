package com.teamtea.ecliptic.core;


import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import com.teamtea.ecliptic.core.SolarDataRunner;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.api.solar.Season;
import net.minecraft.world.level.Level;


public final class SeasonHelper
{
    public static Season getSeason(Level world)
    {
        return world.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(data -> data.getSolarTerm().getSeason()).orElse(Season.NONE);
    }

    public static SolarTerm getSolarTerm(Level world)
    {
        return world.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(SolarDataRunner::getSolarTerm).orElse(SolarTerm.NONE);
    }
}
