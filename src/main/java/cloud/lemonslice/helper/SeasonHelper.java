package cloud.lemonslice.helper;


import cloud.lemonslice.capability.CapabilitySolarTermTime;
import cloud.lemonslice.capability.SolarDataRunner;
import cloud.lemonslice.environment.solar.SolarTerm;
import cloud.lemonslice.environment.solar.Season;
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
