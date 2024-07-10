package com.teamtea.ecliptic.core;


import com.teamtea.ecliptic.api.solar.Season;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.handler.event.DataEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public class WeatherHandler {
    @SubscribeEvent
    public static void onSleepFinishedTimeEvent(SleepFinishedTimeEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            // TODO: 根据季节更新概率
            if(!serverLevel.isRaining()&&serverLevel.getRandom().nextFloat()>0.8) {
                serverLevel.setWeatherParameters(0,
                        ServerLevel.RAIN_DURATION.sample(serverLevel.getRandom()),
                        true, false);
            }
        }
    }

    public static Boolean onCheckWarm(BlockPos p198905) {
      return   SolarTerm.get(DataEventHandler.provider.resolve().get().worldSolarTime.getSolarTermIndex()).getSeason() != Season.WINTER;
    }


    public static boolean onShouldSnow(ServerLevel level, Biome biome,BlockPos pos) {
        // AtomicBoolean result= new AtomicBoolean(false);
        // level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(data ->
        // {
        //     double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature(), 0.0F, 1.0F);
        //     double humidity = Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
        //     humidity = humidity * temperature;
        //     int i = (int) ((1.0D - temperature) * 255.0D);
        //     int j = (int) ((1.0D - humidity) * 255.0D);
        //     int k = j << 8 | i;
        //     // return k > newFoliageBuffer.length ? originColor : newFoliageBuffer[k];
        //    result.set(true);
        // });
        // return result.get();

        return   SolarTerm.get(DataEventHandler.provider.resolve().get().worldSolarTime.getSolarTermIndex()).getSeason() == Season.WINTER;

    }
}
