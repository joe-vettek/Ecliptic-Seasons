package com.teamtea.eclipticseasons.api.util;

import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class WeatherUtil {
    public static boolean isBlockInRain(Level level, BlockPos blockPos) {
        for (BlockPos pos : List.of(blockPos.above(), blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west())) {
            if (WeatherManager.isRainingAt((ServerLevel) level, pos))
                return true;
        }

        return false;
    }

    public static boolean isEntityInRain(LivingEntity entity) {
        // return WeatherManager.isRainingAt((ServerLevel) entity.level(), entity.blockPosition());
        BlockPos blockpos = entity.blockPosition();
        return entity.level().isRainingAt(blockpos)
                || entity.level().isRainingAt(BlockPos.containing(blockpos.getX(), entity.getBoundingBox().maxY, blockpos.getZ()));
        // return entity.isInWaterOrRain();
    }
}
