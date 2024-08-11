package com.teamtea.eclipticseasons.common.core.crop;


import com.teamtea.eclipticseasons.api.constant.biome.Humidity;
import com.teamtea.eclipticseasons.api.constant.crop.CropHumidityInfo;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonInfo;
import com.teamtea.eclipticseasons.common.handler.SolarUtil;
import com.teamtea.eclipticseasons.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;


public final class CropGrowthHandler {
    public static void beforeCropGrowUp(CropGrowEvent.Pre event) {
        Block block = event.getState().getBlock();
        var world = event.getLevel();
        BlockPos pos = event.getPos();
        CropSeasonInfo seasonInfo = CropInfoManager.getSeasonInfo(block);
        if (seasonInfo != null && ServerConfig.Season.enableCrop.get()) {
            if (seasonInfo.isSuitable(SolarUtil.getSeason((Level) world))) {
                Humidity env = Humidity.getHumid(world.getBiome(pos).value().getModifiedClimateSettings().downfall(), world.getBiome(pos).value().getTemperature(pos));
                CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
                checkHumidity(event, world, humidityInfo, env);
            } else if (world.getRandom().nextInt(100) < 25) {
                Humidity env = Humidity.getHumid(world.getBiome(pos).value().getModifiedClimateSettings().downfall(), world.getBiome(pos).value().getTemperature(pos));
                CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
                checkHumidity(event, world, humidityInfo, env);
            } else {
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            }
        } else {
            Humidity env = Humidity.getHumid(world.getBiome(pos).value().getModifiedClimateSettings().downfall(), world.getBiome(pos).value().getTemperature(pos));
            CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
            checkHumidity(event, world, humidityInfo, env);
        }
    }

    public static void checkHumidity(CropGrowEvent.Pre event, LevelAccessor world, CropHumidityInfo humidityInfo, Humidity env) {
        if (humidityInfo != null) {
            float f = humidityInfo.getGrowChance(env);
            if (f == 0) {
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            } else if (f > 1.0F) {
                event.setResult(CropGrowEvent.Pre.Result.GROW);
            } else {
                if (world.getRandom().nextInt(1000) < 1000 * f) {
                    event.setResult(CropGrowEvent.Pre.Result.DEFAULT);
                } else {
                    event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
                }
            }
        }
    }
}
