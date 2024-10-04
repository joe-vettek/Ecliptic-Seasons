package com.teamtea.eclipticseasons.common.core.map;

import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MapChecker {
    public static final int ChunkSize = 16 * 32;
    public static final int ChunkSizeLoc = ChunkSize - 1;
    public static final int ChunkSizeAxis = 4 + 5;
    // TODO:内存更新，双链表+Hash，用LRU
    public static final ArrayList<ChunkInfoMap> RegionList = new ArrayList<>(4);
    public static final int FLAG_NONE_TYPE = 0;
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


    public static boolean isUpdateLock() {
        return updateLock;
    }

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
        return getSurfaceOrUpdate(levelNull, pos, forceUpdate, ChunkInfoMap.TYPE_HEIGHT);
    }

    public static boolean clearChunk(ChunkPos chunkPos) {
        int x0 = chunkPos.getMinBlockX();
        int x1 = chunkPos.getMaxBlockX();
        int z0 = chunkPos.getMinBlockZ();
        int z1 = chunkPos.getMaxBlockZ();

        int x = blockToSectionCoord(x0);
        int z = blockToSectionCoord(z0);
        ChunkInfoMap map = getChunkMap(x, z);

        if (map != null) {
            for (int i = x0; i < x1 + 1; i++) {
                for (int j = z0; j < z1 + 1; j++) {
                    map.updateHeight(i, j, map.minY);
                }
            }
            return true;
        }
        return false;

    }

    public static ChunkInfoMap getChunkMap(int regionX, int regionZ) {
        ChunkInfoMap map = null;
        // try{
        while (updateLock) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        // map = RegionList.stream()
        //         .filter(chunkHeightMap -> chunkHeightMap.regionX == regionX && chunkHeightMap.regionZ == regionZ)
        //         .findFirst()
        //         .orElse(null);
        // size add is dangerous
        for (int i = 0; i < RegionList.size(); i++) {
            var chunkHeightMap = RegionList.get(i);
            if (chunkHeightMap.x == regionX && chunkHeightMap.z == regionZ) {
                map = chunkHeightMap;
                break;
            }
        }
        return map;
    }

    public static int getSurfaceOrUpdate(Level levelNull, BlockPos pos, boolean forceUpdate, int type) {
        int x = blockToSectionCoord(pos.getX());
        int z = blockToSectionCoord(pos.getZ());
        ChunkInfoMap map = getChunkMap(x, z);

        int value = 0;
        if (map != null) {
            if (type == ChunkInfoMap.TYPE_HEIGHT) {
                value = map.getHeight(pos);
                if (value <= map.minY || forceUpdate) {
                    var rh = levelNull.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
                    map.updateHeight(pos, rh);
                    value = rh;
                }
            } else if (type == ChunkInfoMap.TYPE_BIOME) {
                value = map.getBiome(pos);
                if (value == -1 || forceUpdate) {
                    if (levelNull.isLoaded(pos)) {
                        var rh = levelNull.registryAccess().registryOrThrow(Registries.BIOME).getId(levelNull.getBiome(pos).value());
                        map.updateBiome(pos, rh);
                        value = rh;
                    } else {
                        value = levelNull.registryAccess().registry(Registries.BIOME).get().getId(Biomes.THE_VOID);
                    }
                }
            }
        } else {
            updateLock = true;
            synchronized (RegionList) {
                boolean hasBuild = false;
                for (ChunkInfoMap chunkHeightMap : RegionList) {
                    if (chunkHeightMap.x == x && chunkHeightMap.z == z) {
                        hasBuild = true;
                        map = chunkHeightMap;
                        break;
                    }
                }
                if (!hasBuild) {
                    // levelNull.registryAccess().registry(Registries.BIOME).get().getId(Biomes.THE_VOID)
                    map = new ChunkInfoMap(x, z, levelNull.getMinBuildHeight() - 1);
                    RegionList.add(map);
                }
            }
            updateLock = false;

            if (type == ChunkInfoMap.TYPE_HEIGHT) {
                value = levelNull.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
                map.updateHeight(pos, value);
            } else if (type == ChunkInfoMap.TYPE_BIOME) {
                if (levelNull.isLoaded(pos)) {
                    value = levelNull.registryAccess().registryOrThrow(Registries.BIOME).getId(levelNull.getBiome(pos).value());
                    map.updateBiome(pos, value);
                }
            }
        }
        return value;
    }


    public static boolean shouldSnowAt(Level level, BlockPos pos, BlockState state, RandomSource random, long seed) {
        var biomeHolder = getSurfaceBiome(level, pos);
        if (WeatherManager.getSnowDepthAtBiome(level, biomeHolder.value()) > Math.abs(seed % 100)) {
            if (ServerConfig.Debug.notSnowyUnderLight.get()) {
                var abovePos = pos.above();
                if (level.isLoaded(abovePos)) {
                    var stateAbove = level.getBlockState(abovePos);
                    if (stateAbove.getBlock() instanceof LightBlock) {
                        if (stateAbove.getValue(LightBlock.LEVEL) == 0)
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean shouldSnowAtBiome(Level level, Biome biome, BlockState state, RandomSource random, long seed) {
        if (WeatherManager.getSnowDepthAtBiome(level, biome) > Math.abs(seed % 100)) {
            return true;
        }
        return false;
    }

    public static boolean isSmallBiome(Holder<Biome> biomeHolder) {
        // return biomeHolder.is(Tags.Biomes.IS_RIVER)
        //         || biomeHolder.is(Tags.Biomes.IS_BEACH)
        // || biomeHolder.is(Tags.Biomes.IS_OCEAN)
        ;
        return BiomeClimateManager.SMALL_BIOME_MAP.containsKey(biomeHolder.value());
    }

    public static Holder<Biome> idToBiome(Level level, int id) {
        var list = WeatherManager
                .getBiomeList(level);
        return
                list != null ?
                        list.get(id).biomeHolder :
                        level.registryAccess().registry(Registries.BIOME).get().getHolder(id).get();
    }

    public static Holder<Biome> getSurfaceBiome(Level level, BlockPos pos) {
        // fix the pos to surface
        int y = getHeightOrUpdate(level, pos) + 1;
        if (y != pos.getY()) {
            pos = new BlockPos(pos.getX(), y, pos.getZ());
        }

        // var biomeHolder = idToBiome(level, getSurfaceOrUpdate(level, pos, false, ChunkInfoMap.TYPE_BIOME));
        // int i = 0;
        // while (isSmallBiome(biomeHolder)) {
        //     i += 1;
        //     for (Direction direction : Direction.Plane.HORIZONTAL) {
        //         biomeHolder = idToBiome(level, getSurfaceOrUpdate(level, pos.relative(direction, i), false, ChunkInfoMap.TYPE_BIOME));
        //         if (!isSmallBiome(biomeHolder)) {
        //             break;
        //         }
        //     }
        // }
        // return biomeHolder;

        var biome = level.getBiome(pos);
        int i = 0;
        while (isSmallBiome(biome)) {
            i += 1;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                biome = level.getBiome(pos.relative(direction, i));
                if (!isSmallBiome(biome)) {
                    break;
                }
            }
        }
        return biome;
    }

    // 获取chunk内部位置
    public static int getChunkValue(int i) {
        return i & (ChunkSizeLoc);
    }


    public static int getBlockType(BlockState state, Level level, BlockPos pos) {
        int flag = FLAG_NONE_TYPE;
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
        } else if ((
                onBlock instanceof FarmBlock ||
                        onBlock instanceof DirtPathBlock)) {
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


    public static boolean isValidDimension(@Nullable Level level) {
        return level != null
                && level.dimensionType().natural()
                && !level.dimensionType().hasFixedTime();
    }
}
