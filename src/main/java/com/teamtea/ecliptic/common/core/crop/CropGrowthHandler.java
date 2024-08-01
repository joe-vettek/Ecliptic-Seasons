package com.teamtea.ecliptic.common.core.crop;


import com.teamtea.ecliptic.api.constant.biome.Humidity;
import com.teamtea.ecliptic.api.constant.crop.CropHumidityInfo;
import com.teamtea.ecliptic.api.constant.crop.CropSeasonInfo;
import com.teamtea.ecliptic.common.handler.SolarUtil;
import com.teamtea.ecliptic.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;

public final class CropGrowthHandler {
    public static void beforeCropGrowUp(BlockEvent.CropGrowEvent.Pre event) {
        Block block = event.getState().getBlock();
        var world = event.getLevel();
        BlockPos pos = event.getPos();
        CropSeasonInfo seasonInfo = CropInfoManager.getSeasonInfo(block);
        if (seasonInfo != null && ServerConfig.Season.enableCrop.get()) {
            if (seasonInfo.isSuitable(SolarUtil.getSeason((Level) world))) {
                Humidity env = Humidity.getHumid(world.getBiome(pos).get().getModifiedClimateSettings().downfall(), world.getBiome(pos).get().getTemperature(pos));
                CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
                checkHumidity(event, world, humidityInfo, env);
            } else if (world.getRandom().nextInt(100) < 25) {
                Humidity env = Humidity.getHumid(world.getBiome(pos).get().getModifiedClimateSettings().downfall(), world.getBiome(pos).get().getTemperature(pos));
                CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
                checkHumidity(event, world, humidityInfo, env);
            } else {
                event.setResult(Event.Result.DENY);
            }
        } else {
            Humidity env = Humidity.getHumid(world.getBiome(pos).get().getModifiedClimateSettings().downfall(), world.getBiome(pos).get().getTemperature(pos));
            CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
            checkHumidity(event, world, humidityInfo, env);
        }
    }

    public static void checkHumidity(BlockEvent.CropGrowEvent.Pre event, LevelAccessor world, CropHumidityInfo humidityInfo, Humidity env) {
        if (humidityInfo != null) {
            float f = humidityInfo.getGrowChance(env);
            if (f == 0) {
                event.setResult(Event.Result.DENY);
            } else if (f > 1.0F) {
                event.setResult(Event.Result.ALLOW);
            } else {
                if (world.getRandom().nextInt(1000) < 1000 * f) {
                    event.setResult(Event.Result.DEFAULT);
                } else {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
