package com.teamtea.ecliptic.core;

import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import net.minecraft.world.level.Level;


public class SolarUtil {



    public static SolarDataRunner getProvider(Level level) {

        return level==null?null:level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).resolve().orElse(null);
    }
}
