package com.teamtea.eclipticseasons.misc.teacon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;


public class CheckTool {
    public static boolean isOnTeaconServer() {
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
            return System.getenv("TEACON_DEDICATED_SERVER") != null;
        return true;
    }

    public static boolean isValidLevel(Level level) {
        return level != null && isOnTeaconServer() && level.dimension().equals(Level.OVERWORLD);
    }

    public static boolean isValidPos(Level level, BlockPos blockPos) {
        if (isValidLevel(level)) {
            return blockPos.getX() > 100 && blockPos.getX() < 1000
                    && blockPos.getZ() > 100 && blockPos.getZ() < 1000;
            // return true;
        }
        return false;
    }

    public static boolean isValidPos(Level level, LevelChunk levelChunk) {
        var chunkpos = levelChunk.getPos();
        int i = chunkpos.getMiddleBlockX();
        int j = chunkpos.getMiddleBlockZ();
        BlockPos blockpos1 = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(i, 0, j));
        return isValidPos(level, blockpos1);
    }
}
