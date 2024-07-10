package com.teamtea.ecliptic.common.core.weather;

import com.teamtea.ecliptic.api.solar.Season;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.handler.SolarUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;

public class WeatherManager {

    public static Boolean onCheckWarm(BlockPos p198905) {
      return   SolarTerm.get(AllListener.provider.resolve().get().worldSolarTime.getSolarTermIndex()).getSeason() != Season.WINTER;
    }

    public static boolean onShouldSnow(ServerLevel level, Biome biome, BlockPos pos) {
        return   SolarTerm.get(AllListener.provider.resolve().get().worldSolarTime.getSolarTermIndex()).getSeason() == Season.WINTER;
    }

    public static boolean agentAdvanceWeatherCycle(ServerLevel serverLevel, ServerLevelData serverLevelData, WritableLevelData levelData, RandomSource random) {

        return true;
    }

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
