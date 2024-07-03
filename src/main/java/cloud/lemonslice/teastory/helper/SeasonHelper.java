package cloud.lemonslice.teastory.helper;


import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.environment.solar.Season;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import net.minecraft.world.level.Level;


public final class SeasonHelper
{
    public static Season getSeason(Level world)
    {
        return world.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(data -> data.getSolarTerm().getSeason()).orElse(Season.NONE);
    }

    public static SolarTerm getSolarTerm(Level world)
    {
        return world.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(CapabilitySolarTermTime.Data::getSolarTerm).orElse(SolarTerm.NONE);
    }
}
