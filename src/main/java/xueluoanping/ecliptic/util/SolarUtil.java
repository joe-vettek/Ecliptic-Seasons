package xueluoanping.ecliptic.util;

import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.capability.SolarData;
import net.minecraft.world.level.Level;


public class SolarUtil {


    public static SolarData getProvider(Level level) {
        return level==null?null:level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).resolve().orElse(null);
    }
}
