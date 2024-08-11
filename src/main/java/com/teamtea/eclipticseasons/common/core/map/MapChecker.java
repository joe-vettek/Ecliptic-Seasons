package com.teamtea.eclipticseasons.common.core.map;

import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

public class MapChecker {
    public static final int ChunkSize = 16 * 32;
    public static final int ChunkSizeLoc = ChunkSize - 1;
    public static final int ChunkSizeAxis = 4 * 5;
    // TODO:内存更新，双链表+Hash，用LRU
    public static final ArrayList<ChunkHeightMap> RegionList = new ArrayList<>(4);
    public static final int FLAG_BLOCK = 1;
    public static final int FLAG_SLAB = 2;
    public static final int FLAG_STAIRS = 3;
    public static final int FLAG_LEAVES = 4;
    public static final int FLAG_GRASS = 5;
    public static final int FLAG_GRASS_LARGE = 501;
    public static final int FLAG_FARMLAND = 6;
    public static final List<Block> LowerPlant = List.of(Blocks.SHORT_GRASS, Blocks.FERN, Blocks.DANDELION);
    public static final List<Block> LARGE_GRASS = List.of(Blocks.TALL_GRASS, Blocks.LARGE_FERN);
    private static boolean updateLock;

    public static void clearHeightMap() {
        updateLock=true;
        synchronized (RegionList) {
            RegionList.clear();
        }
        updateLock=false;
    }

    // 获取chunk位置
    public static int blockToSectionCoord(int i) {
        return i >> ChunkSizeAxis;
    }

    public static int getHeightOrUpdate(Level levelNull, BlockPos pos, boolean shouldUpdate) {
        // if (ClientConfig.Renderer.useVanillaCheck.get())
        //     return Integer.MIN_VALUE;
        int x = blockToSectionCoord(pos.getX());
        int z = blockToSectionCoord(pos.getZ());
        ChunkPos chunkPos = new ChunkPos(x, z);
        // var map = ChunkMap.getOrDefault(chunkPos, null);

        ChunkHeightMap map = null;
        // try{
        while (updateLock){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        // map = RegionList.stream()
        //         .filter(chunkHeightMap -> chunkHeightMap.x == x && chunkHeightMap.z == z)
        //         .findFirst()
        //         .orElse(null);
        // size add is dangerous
        for (int i = 0; i < RegionList.size(); i++) {
            var chunkHeightMap=RegionList.get(i);
                if (chunkHeightMap.x == x && chunkHeightMap.z == z) {
                    map = chunkHeightMap;
                    break;
                }
        }

        int h = 0;
        if (map != null) {
            h = map.getHeight(pos);
            if (h == Integer.MIN_VALUE || shouldUpdate) {
                var rh = levelNull.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
                // if (ClientConfig.Renderer.underSnow.get()) {
                //     var rmPos = new BlockPos.MutableBlockPos(pos.getX(), rh, pos.getZ());
                //     var s = levelNull.getBlockState(rmPos);
                //     while ((!(s.getBlock() instanceof LeavesBlock) && !s.isFaceSturdy(Minecraft.getInstance().level, rmPos, Direction.DOWN))) {
                //         rh--;
                //         s = levelNull.getBlockState(new BlockPos(pos.getX(), rh, pos.getZ()));
                //         rmPos.move(Direction.DOWN, 1);
                //     }
                // }
                map.updateHeight(pos, rh);
                h = rh;
            }
            // return h;
        } else {

            updateLock = true;
            // ChunkMap.put(chunkPos, map);
            synchronized (RegionList) {
                boolean hasBuild = false;
                for (ChunkHeightMap chunkHeightMap : RegionList) {
                    if (chunkHeightMap.x == x && chunkHeightMap.z == z) {
                        hasBuild = true;
                        map = chunkHeightMap;
                        break;
                    }
                }
                if (!hasBuild) {
                    map = new ChunkHeightMap(x, z);
                    RegionList.add(map);
                }
            }
            updateLock = false;

            h = levelNull.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
            map.updateHeight(pos, h);
            // h = getHeightOrUpdate(pos, false);
            // return h;

        }
        return h;
    }

    // TODO:感觉用随机表性能更高
    public static boolean shouldSnowAt(Level level, BlockPos pos, BlockState state, RandomSource random, long seed) {
        // Ecliptic.logger(SolarClientUtil.getSnowLayer() * 100, (seed&99));
        // Minecraft.getInstance().level.getBiome(pos);
        var biome = level.getBiome(pos);
        // SimpleUtil.testTime(()->{ Minecraft.getInstance().level.getBiome(pos);});
        if (WeatherManager.getSnowDepthAtBiome(level, biome.value()) > Math.abs(seed % 100)) {
            return true;
        }

        return false;
        // >= random.nextInt(100));
    }

    // 获取chunk内部位置
    public static int getChunkValue(int i) {
        return i & (ChunkSizeLoc);
    }

    public static class ChunkHeightMap {
        private final int[][] matrix = new int[ChunkSize][ChunkSize];
        private final Object[][] lockArray = new Object[ChunkSize][ChunkSize];
        private final int x;
        private final int z;

        public ChunkHeightMap(int x, int z) {
            this.x = x;
            this.z = z;
            EclipticSeasons.logger(String.format("Create new Height Map with [%s, %s]", x, z));
            for (int i = 0; i < ChunkSize; i++) {
                for (int j = 0; j < ChunkSize; j++) {
                    matrix[i][j] = Integer.MIN_VALUE;
                    lockArray[i][j] = new Object();
                }
            }
            EclipticSeasons.logger(String.format("End create [%s, %s]", x, z));
        }

        public int getHeight(int x, int z) {
            x = getChunkValue(x);
            z = getChunkValue(z);
            return matrix[x][z];
        }

        public int getHeight(BlockPos pos) {
            return getHeight(pos.getX(), pos.getZ());
        }

        public int updateHeight(int x, int z, int y) {
            x = getChunkValue(x);
            z = getChunkValue(z);
            int old;
            synchronized (lockArray[x][z]) {
                old = matrix[x][z];
                matrix[x][z] = y;
            }
            return old;
        }

        public int updateHeight(BlockPos pos, int y) {
            return updateHeight(pos.getX(), pos.getZ(), y);
        }
    }
}
