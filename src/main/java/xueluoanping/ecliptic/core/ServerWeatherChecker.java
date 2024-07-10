package xueluoanping.ecliptic.core;

import cloud.lemonslice.environment.solar.SolarTerm;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;

public class ServerWeatherChecker {

    public enum SnowRenderStatus {
        SNOW,
        SNOW_MELT,
        // RAIN,
        // CLOUD,
        NONE
    }

    public static boolean isInTerms(SolarTerm start, SolarTerm end, SolarTerm check) {
        if (start.ordinal() <= end.ordinal()) {
            return start.ordinal() <= check.ordinal() && check.ordinal() <= end.ordinal();
        } else
            return start.ordinal() <= check.ordinal() || check.ordinal() <= end.ordinal();
    }

    public static SnowRenderStatus getSnowStatus(ServerLevel level, Biome biome, BlockPos pos) {
        var provider = SolarUtil.getProvider(level);
        var status = SnowRenderStatus.NONE;
        if (provider != null) {
            if (level.isRaining()) {
                if (isInTerms(SolarTerm.LIGHT_SNOW, SolarTerm.GREATER_COLD, provider.getSolarTerm())) {
                    status = SnowRenderStatus.SNOW;
                }
            }
            if (isInTerms(SolarTerm.RAIN_WATER, SolarTerm.AUTUMNAL_EQUINOX, provider.getSolarTerm())) {
                status = SnowRenderStatus.SNOW_MELT;
            }
        }
        return status;
    }
}
