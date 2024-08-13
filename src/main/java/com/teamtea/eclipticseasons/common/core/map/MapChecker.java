package com.teamtea.eclipticseasons.common.core.map;

import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

public class MapChecker {
    public static final int ChunkSize = 16 * 32;
    public static final int ChunkSizeLoc = ChunkSize - 1;
    public static final int ChunkSizeAxis = 4 + 5;
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
        updateLock = true;
        synchronized (RegionList) {
            RegionList.clear();
        }
        updateLock = false;
    }

    // 获取chunk位置
    public static int blockToSectionCoord(int i) {
        return i >> ChunkSizeAxis;
    }

    public static int getHeightOrUpdate(Level levelNull, BlockPos pos) {
        return getHeightOrUpdate(levelNull, pos, false);
    }

    public static int getHeightOrUpdate(Level levelNull, BlockPos pos, boolean forceUpdate) {
        return getHeightOrUpdate(levelNull, pos, forceUpdate, ChunkHeightMap.TYPE_HEIGHT);
    }

    public static int getHeightOrUpdate(Level levelNull, BlockPos pos, boolean forceUpdate, int type) {
        // if (ClientConfig.Renderer.useVanillaCheck.get())
        //     return Integer.MIN_VALUE;
        // EclipticSeasonsMod.logger(levelNull);
        int x = blockToSectionCoord(pos.getX());
        int z = blockToSectionCoord(pos.getZ());
        // ChunkPos chunkPos = new ChunkPos(x, z);
        // var map = ChunkMap.getOrDefault(chunkPos, null);

        ChunkHeightMap map = null;
        // try{
        while (updateLock) {
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
            var chunkHeightMap = RegionList.get(i);
            if (chunkHeightMap.x == x && chunkHeightMap.z == z) {
                map = chunkHeightMap;
                break;
            }
        }

        int value = 0;
        if (map != null) {
            if (type == ChunkHeightMap.TYPE_HEIGHT) {
                value = map.getHeight(pos);
                if (value <= map.minY || forceUpdate) {
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
                    value = rh;
                }
            } else if (type == ChunkHeightMap.TYPE_BIOME) {
                value = map.getBiome(pos);
                if (value == -1 || forceUpdate) {
                    var rh = levelNull.registryAccess().registryOrThrow(Registries.BIOME).getId(levelNull.getBiome(pos).value());
                    map.updateBiome(pos, rh);
                    value = rh;
                }
            }
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
                    map = new ChunkHeightMap(x, z, levelNull.getMinBuildHeight() - 1);
                    RegionList.add(map);
                }
            }
            updateLock = false;

            if (type == ChunkHeightMap.TYPE_HEIGHT) {
                value = levelNull.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
                map.updateHeight(pos, value);
            } else if (type == ChunkHeightMap.TYPE_BIOME) {
                value = levelNull.registryAccess().registryOrThrow(Registries.BIOME).getId(levelNull.getBiome(pos).value());
                map.updateBiome(pos, value);
            }

            // h = getHeightOrUpdate(pos, false);
            // return h;

        }
        return value;
    }

    // TODO:感觉用随机表性能更高
    public static boolean shouldSnowAt(Level level, BlockPos pos, BlockState state, RandomSource random, long seed) {
        // biome =   level.getBiome(pos);
        // var biome = ServerConfig.Season.biomeDetectNoise.get() ?
        //         level.getBiome(pos) :
        //         level.getNoiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2);

        var biomeList = WeatherManager
                .getBiomeList(level);

        if (biomeList != null) {
            int id = getHeightOrUpdate(level, pos, false, ChunkHeightMap.TYPE_BIOME);
            if (id < biomeList.size()) {
                var biomeHolder = biomeList.get(id).biomeHolder;
                if (WeatherManager.getSnowDepthAtBiome(level, biomeHolder.value()) > Math.abs(seed % 100)) {
                    return true;
                }
            }
        }

        return false;
        // >= random.nextInt(100));
    }

    public static Holder<Biome> getSurfaceBiome(Level level, BlockPos pos) {
        return WeatherManager
                .getBiomeList(level)
                .get(getHeightOrUpdate(level, pos, false, ChunkHeightMap.TYPE_BIOME))
                .biomeHolder;
    }

    // 获取chunk内部位置
    public static int getChunkValue(int i) {
        return i & (ChunkSizeLoc);
    }


    public static int getBlockType(BlockState state, Level level, BlockPos pos) {
        int flag = 0;
        var onBlock = state.getBlock();
        if (onBlock instanceof LeavesBlock) {
            flag = MapChecker.FLAG_LEAVES;
        } else if ((state.isSolidRender(level, pos)
                // state.isSolid()
                || onBlock instanceof LeavesBlock
                || (onBlock instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP)
                || (onBlock instanceof StairBlock && state.getValue(StairBlock.HALF) == Half.TOP))) {
            flag = MapChecker.FLAG_BLOCK;
        } else if (onBlock instanceof SlabBlock) {
            flag = MapChecker.FLAG_SLAB;
        } else if (onBlock instanceof StairBlock) {
            flag = MapChecker.FLAG_STAIRS;
        } else if (MapChecker.LowerPlant.contains(onBlock)) {
            flag = MapChecker.FLAG_GRASS;
        } else if (MapChecker.LARGE_GRASS.contains(onBlock)) {
            flag = MapChecker.FLAG_GRASS_LARGE;
        } else if ((onBlock instanceof FarmBlock || onBlock instanceof DirtPathBlock)) {
            flag = MapChecker.FLAG_FARMLAND;
        }
        return flag;
    }

    public static int getSnowOffset(BlockState state, int flag) {
        int offset = 0;
        if (flag == MapChecker.FLAG_GRASS || flag == MapChecker.FLAG_GRASS_LARGE) {
            if (flag == MapChecker.FLAG_GRASS) {
                offset = 1;
            }
            // 这里不忽略这个警告，因为后续会有优化
            else if (flag == MapChecker.FLAG_GRASS_LARGE) {
                if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                    offset = 1;
                } else {
                    offset = 2;
                }
            }
        }
        return offset;
    }

    public static List<Holder<Biome>> getBiomes(Level level, BlockPos pos) {
        var mPos = new BlockPos.MutableBlockPos(pos.getX(),
                level.getMaxBuildHeight(),
                // level.getHeight(Heightmap.Types.MOTION_BLOCKING,pos.getX(),pos.getZ()),
                pos.getZ());

        var list = new ArrayList<Holder<Biome>>();
        while (mPos.getY() >= level.getMinBuildHeight()) {
            list.add(level.getBiome(mPos));
            mPos = mPos.move(Direction.DOWN);
        }
        return list;
    }
}
