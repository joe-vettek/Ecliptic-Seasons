package com.teamtea.ecliptic.client.core;

import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import com.teamtea.ecliptic.Ecliptic;

import java.util.*;

public class ModelManager {
    public static Map<ResourceLocation, BakedModel> models;
    public static
    LazyOptional<BakedModel> snowOverlayLeaves =
            LazyOptional.of(() -> models.get(new ModelResourceLocation(Ecliptic.ModContents.snowyLeaves.getId(), "")));
    public static
    LazyOptional<BakedModel> snowySlabBottom =
            LazyOptional.of(() -> models.get(new ModelResourceLocation(Ecliptic.ModContents.snowySlab.getId(), "type=bottom,waterlogged=false")));
    public static
    LazyOptional<BakedModel> snowOverlayBlock =
            LazyOptional.of(() -> models.get(new ModelResourceLocation(Ecliptic.ModContents.snowyBlock.getId(), "")));
    public static
    LazyOptional<BakedModel> snowModel =
            LazyOptional.of(() -> models.get(new ModelResourceLocation(new ResourceLocation("minecraft:snow_block"), "")));

    public static ResourceLocation snowy_fern = Ecliptic.rl("block/snowy_fern");
    public static ResourceLocation snowy_grass = Ecliptic.rl("block/snowy_grass");
    public static ResourceLocation snowy_large_fern_bottom = Ecliptic.rl("block/snowy_large_fern_bottom");
    public static ResourceLocation snowy_large_fern_top = Ecliptic.rl("block/snowy_large_fern_top");
    public static ResourceLocation snowy_tall_grass_bottom = Ecliptic.rl("block/snowy_tall_grass_bottom");
    public static ResourceLocation snowy_tall_grass_top = Ecliptic.rl("block/snowy_tall_grass_top");


    public static ResourceLocation mrl(String s) {
        return mrl(s, "");
    }

    public static ResourceLocation mrl(String s, String s2) {
        return new ModelResourceLocation(Ecliptic.rl(s), s2);
    }

    public static final int ChunkSize = 16 * 32;
    public static final int ChunkSizeLoc = ChunkSize - 1;
    public static final int ChunkSizeAxis = 4 * 5;

    public static class ChunkHeightMap {
        private final int[][] matrix = new int[ChunkSize][ChunkSize];
        private final Object[][] lockArray = new Object[ChunkSize][ChunkSize];
        private final int x;
        private final int z;

        public ChunkHeightMap(int x, int z) {
            this.x = x;
            this.z = z;
            Ecliptic.logger(String.format("Create new Height Map with [%s, %s]", x, z));
            for (int i = 0; i < ChunkSize; i++) {
                for (int j = 0; j < ChunkSize; j++) {
                    matrix[i][j] = Integer.MIN_VALUE;
                    lockArray[i][j] = new Object();
                }
            }
            Ecliptic.logger(String.format("End create [%s, %s]", x, z));
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

    // 获取chunk内部位置
    public static int getChunkValue(int i) {
        return i & (ChunkSizeLoc);
    }

    // 获取chunk位置
    public static int blockToSectionCoord(int i) {
        return i >> ChunkSizeAxis;
    }

    // TODO:内存更新，双链表+Hash，用LRU
    public static final ArrayList<ChunkHeightMap> RegionList = new ArrayList<>(4);
    public static Map<List<BakedQuad>, List<BakedQuad>> quadMap = new HashMap<>(1024, 0.5f);


    public static int getHeightOrUpdate(BlockPos pos, boolean shouldUpdate) {
        int x = blockToSectionCoord(pos.getX());
        int z = blockToSectionCoord(pos.getZ());
        ChunkPos chunkPos = new ChunkPos(x, z);
        // var map = ChunkMap.getOrDefault(chunkPos, null);
        ChunkHeightMap map = null;
        for (ChunkHeightMap chunkHeightMap : RegionList) {
            if (chunkHeightMap.x == x && chunkHeightMap.z == z) {
                map = chunkHeightMap;
                break;
            }
        }
        int h = 0;
        if (map != null) {
            h = map.getHeight(pos);
            if (h == Integer.MIN_VALUE || shouldUpdate) {
                map.updateHeight(pos, Minecraft.getInstance().level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1);
                h = map.getHeight(pos);
            }
            // return h;
        } else {

            // ChunkMap.put(chunkPos, map);
            synchronized (RegionList) {
                boolean hasBuild = false;
                for (ChunkHeightMap chunkHeightMap : RegionList) {
                    if (chunkHeightMap.x == x && chunkHeightMap.z == z) {
                        hasBuild = true;
                        break;
                    }
                }
                if (!hasBuild) {
                    map = new ChunkHeightMap(x, z);
                    RegionList.add(map);
                }
            }
            // h = Minecraft.getInstance().level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
            // map.updateHeight(pos, h);
            h = getHeightOrUpdate(pos, false);
            // return h;
        }
        return h;
    }

    public static final int FLAG_BLOCK = 1;
    public static final int FLAG_SLAB = 2;
    public static final int FLAG_STAIRS = 3;
    public static final int FLAG_LEAVES = 4;
    public static final int FLAG_GRASS = 5;
    public static final int FLAG_GRASS_LARGE = 501;
    public static final List<Block> GRASS = List.of(Blocks.GRASS, Blocks.FERN);
    public static final List<Block> LARGE_GRASS = List.of(Blocks.TALL_GRASS, Blocks.LARGE_FERN);


    // 实际上这里之所以太慢还有个问题就是会一个方块访问七次
    public static List<BakedQuad> appendOverlay(BlockAndTintGetter blockAndTintGetter, BlockState state, BlockPos pos, Direction direction, RandomSource random, long seed, List<BakedQuad> list) {
        // Minecraft.getInstance().level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING,pos);
        // 不处理空列表，这代表着不处理这个方向
        // if (state.is(BlockTags.LEAVES) && !list.isEmpty()) {
        //     return List.of(new BakedQuadRetextured(list.get(0),
        //             ClientSetup.snowOverlayBlock.resolve().get().getQuads(null, Direction.UP, null).get(0).getSprite()));
        // }
        // if (true)return list;

        if (direction != Direction.DOWN
                && !list.isEmpty()) {

            var onBlock = state.getBlock();
            int flag = 0;
            if (onBlock instanceof LeavesBlock) {
                flag = FLAG_LEAVES;
            } else if ((state.isSolidRender(blockAndTintGetter, pos)
                    // state.isSolid()
                    || onBlock instanceof LeavesBlock
                    || (onBlock instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP)
                    || (onBlock instanceof StairBlock && state.getValue(StairBlock.HALF) == Half.TOP))) {
                flag = FLAG_BLOCK;
            } else if (onBlock instanceof SlabBlock) {
                flag = FLAG_SLAB;
            } else if (onBlock instanceof StairBlock) {
                flag = FLAG_STAIRS;
            } else if (GRASS.contains(onBlock)) {
                flag = FLAG_GRASS;
            } else if (LARGE_GRASS.contains(onBlock)) {
                flag = FLAG_GRASS_LARGE;
            } else return list;

            var onPos = getHeightOrUpdate(pos, false);
            boolean isLight = false;
            int offset = 0;
            if (flag == FLAG_GRASS || flag == FLAG_GRASS_LARGE) {
                if (flag == FLAG_GRASS) {
                    isLight = onPos == pos.getY() - 1;
                    offset = 1;
                }
                // 这里不忽略这个警告，因为后续会有优化
                else if (flag == FLAG_GRASS_LARGE) {
                    if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                        isLight = onPos == pos.getY() - 1;
                        offset = 1;
                    } else {
                        isLight = onPos == pos.getY() - 2;
                        offset = 2;
                    }
                }
            } else isLight = onPos == pos.getY();

            // SimpleUtil.testTime(()->{getHeightOrUpdate(pos, false);});

            if (isLight
                    && onBlock != Blocks.SNOW_BLOCK
                    && shouldSnowAt(blockAndTintGetter, pos.below(offset), state, random, seed)) {
                // DynamicLeavesBlock
                var cc = quadMap.getOrDefault(list, null);
                if (cc != null) {
                    return cc;
                } else {
                    BakedModel snowModel = null;
                    BlockState snowState = null;
                    if (snowOverlayBlock.resolve().isPresent() && flag == FLAG_BLOCK) {
                        snowModel = snowOverlayBlock.resolve().get();
                    } else if (snowOverlayLeaves.resolve().isPresent() && flag == FLAG_LEAVES) {
                        snowModel = snowOverlayLeaves.resolve().get();
                    } else if (snowySlabBottom.resolve().isPresent() && flag == FLAG_SLAB) {
                        snowModel = snowySlabBottom.resolve().get();
                    } else if (models != null && flag == FLAG_STAIRS) {
                        snowState = Ecliptic.ModContents.snowyStairs.get().defaultBlockState()
                                .setValue(StairBlock.FACING, state.getValue(StairBlock.FACING))
                                .setValue(StairBlock.HALF, state.getValue(StairBlock.HALF))
                                .setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE));
                        // 楼梯的方向是无
                        snowModel = models.get(BlockModelShaper.stateToModelLocation(snowState));
                    } else if (flag == FLAG_GRASS) {
                        if (onBlock == Blocks.GRASS) {
                            snowModel = models.get(snowy_grass);
                        } else if (onBlock == Blocks.FERN) {
                            snowModel = models.get(snowy_fern);
                        } else snowModel = models.get(snowy_grass);
                    } else if (flag == FLAG_GRASS_LARGE) {
                        if (onBlock == Blocks.TALL_GRASS) {
                            snowModel = models.get(offset == 1 ? snowy_tall_grass_bottom : snowy_tall_grass_top);
                        } else if (onBlock == Blocks.LARGE_FERN) {
                            snowModel = models.get(offset == 1 ? snowy_large_fern_bottom : snowy_large_fern_top);
                        } else snowModel = models.get(offset == 1 ? snowy_tall_grass_bottom : snowy_tall_grass_top);
                    }

                    if (snowModel != null) {
                        int size = list.size();
                        var snowList = snowModel.getQuads(snowState, direction, null);
                        ArrayList<BakedQuad> newList;

                        if (flag == FLAG_GRASS) {
                            newList = new ArrayList<>(snowList);
                        } else if (direction == Direction.UP) {
                            newList = new ArrayList<>(snowList);
                        } else {
                            newList = new ArrayList<BakedQuad>(size + snowList.size());
                            newList.addAll(list);
                            newList.addAll(snowList);
                        }
                        quadMap.put(list, newList);
                        list = newList;
                    }
                }
            }

        }
        return list;
    }

    // TODO:感觉用随机表性能更高
    public static boolean shouldSnowAt(BlockAndTintGetter blockAndTintGetter, BlockPos pos, BlockState state, RandomSource random, long seed) {
        // Ecliptic.logger(SolarClientUtil.getSnowLayer() * 100, (seed&99));
        // Minecraft.getInstance().level.getBiome(pos);
        var biome = Minecraft.getInstance().level.getBiome(pos);
        if (WeatherManager.getSnowDepthAtBiome(Minecraft.getInstance().level, biome.get()) > Math.abs(seed % 100)) {
            return true;
        }

        return false;
        // >= random.nextInt(100));
    }

    public static int getLight(BlockAndTintGetter blockAndTintGetter, BlockPos pos, BlockState state, RandomSource random, long seed) {
        // Ecliptic.logger(pos);
        int result = 0;
        if (FMLLoader.getLoadingModList().getModFileById("embeddium") != null) {
            if (blockAndTintGetter instanceof WorldSlice worldSlice) {
                result = Minecraft.getInstance().level.getLightEngine().getRawBrightness(pos.above(), 0);
            }
            // return blockAndTintGetter.getRawBrightness( pos,0);
        } else {
            result = blockAndTintGetter.getLightEngine().getRawBrightness(pos.above(), 0);
        }
        result += blockAndTintGetter.getBlockState(pos.above()).is(BlockTags.SNOW) ? 15 : 0;
        // TODO:这里等会传Blocksate进来
        result += state.is(BlockTags.SNOW) ? -100 : 0;
        // result += SolarUtil.getProvider(Minecraft.getInstance().level).getSolarTerm().getSeason() == Season.WINTER ? 0 : -100;
        if (result >= 15) {
            // TODO:未来设置为降雨
            // Ecliptic.logger(SolarUtil.getProvider(Minecraft.getInstance().level).getSnowLayer());
            result += shouldSnowAt(blockAndTintGetter, pos, state, random, seed) ? 0 : -100;
            // result+=blockAndTintGetter.getBlockState(pos.above()).isAir() ? 0 : -100;
        }
        return result;
    }
}
