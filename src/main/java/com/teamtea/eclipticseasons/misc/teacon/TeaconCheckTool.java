package com.teamtea.eclipticseasons.misc.teacon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.io.File;


public class TeaconCheckTool {

    public static boolean teacon = System.getenv("TEACON_DEDICATED_SERVER") != null;

    static {
        if (!teacon)
            teacon = new File("config/eclipticseasons-teacon.toml").exists();
    }

    public static boolean isOnTeaconServer() {
        // Minecraft.getInstance().getCurrentServer().motd
        // if (FMLLoader.getDist() == Dist.DEDICATED_SERVER)
        //     return System.getenv("TEACON_DEDICATED_SERVER") != null;
        return teacon;
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
