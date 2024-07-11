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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import com.teamtea.ecliptic.Ecliptic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    // if (state.getBlock() instanceof GrassBlock&& blockAndTintGetter.getBlockState(pos.above()).isAir()
    public static BakedModel checkAndUpdate(BlockAndTintGetter blockAndTintGetter, BlockState state, BlockPos pos, Direction direction, BakedModel bakedModel) {
        var out = bakedModel;
        // &&blockAndTintGetter.canSeeSky(pos)
        // if (ClientSetup.snowModel.resolve().isPresent()
        //         && blockAndTintGetter.getBlockState(pos.above()).isAir()&& (state.isSolidRender(blockAndTintGetter, pos)||state.is(BlockTags.LEAVES)))
        // {
        //     out = ClientSetup.snowModel.resolve().get();
        //     // out=new WarpBakedModel(bakedModel,ClientSetup.snowModel.resolve().get());
        // }
        return out;
    }


    public static BakedModel checkDirectionAndUpdate(BlockAndTintGetter blockAndTintGetter, BlockState state, BlockPos pos, Direction direction, BakedModel bakedModel) {
        // return direction == Direction.UP ? checkAndUpdate(blockAndTintGetter, state, pos, direction, bakedModel) : bakedModel;
        return bakedModel;
    }

    public static boolean ShouldReplaceQuads(BlockState state, Direction direction, RandomSource randomSource) {
        // return direction == Direction.UP;
        return false;
    }

    public static List<BakedQuad> getReplacedQuads(Map<Direction, List<BakedQuad>> culledFaces, BlockState state, Direction direction, RandomSource randomSource) {
        var BakedQuads = culledFaces.get(direction);
        var outBakedQuads = new ArrayList<BakedQuad>();
        // java.util.ConcurrentModificationException: null
        // for (BakedQuad bakedQuad : BakedQuads) {
        //    var wrapperBakedQuad = new WrapBakedQuad(bakedQuad.getVertices(),
        //             bakedQuad.getTintIndex(),
        //             bakedQuad.getDirection(),
        //             bakedQuad.getSprite(),
        //             bakedQuad.isShade(),
        //             bakedQuad.hasAmbientOcclusion(), bakedQuad);
        //     outBakedQuads.add(wrapperBakedQuad);
        // }

        return outBakedQuads;
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
            for (int i = 0; i < ChunkSize; i++) {
                for (int j = 0; j < ChunkSize; j++) {
                    matrix[i][j] = Integer.MIN_VALUE;
                    lockArray[i][j] = new Object();
                }
            }
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
    public static Map<Long, Boolean> blockMap = new ConcurrentHashMap<>();
    public static Map<ChunkPos, ChunkHeightMap> ChunkMap = new ConcurrentHashMap<>(16);
    public static final ArrayList<ChunkHeightMap> RegionList = new ArrayList<>(4);
    public static Map<List<BakedQuad>, List<BakedQuad>> quadMap = new HashMap<>(1024, 0.5f);
    public static List<BakedQuad>[] quadKeyList = (List<BakedQuad>[]) new List<?>[1024];
    public static List<BakedQuad>[] quadValueList = (List<BakedQuad>[]) new List<?>[1024];
    public static int emptyQuadPos = 0;

    public static List<BakedQuad> getQuad(List<BakedQuad> input, List<BakedQuad> nullValue) {
        for (int i = 0; i < quadKeyList.length; i++) {
            var k = quadKeyList[i];
            if (k == null)
                break;
            if (k == input)
                return quadValueList[i];

        }
        return nullValue;
    }

    public static int setQuad(List<BakedQuad> keyList, List<BakedQuad> newList) {
        if (emptyQuadPos < quadKeyList.length) {
            quadKeyList[emptyQuadPos] = keyList;
            quadValueList[emptyQuadPos] = newList;
            emptyQuadPos++;
            return emptyQuadPos;
        }
        Ecliptic.logger("Please update the size of quadList");
        return -1;
    }


    private static final int PACKED_X_LENGTH = 1 + Mth.log2(Mth.smallestEncompassingPowerOfTwo(30000000));
    private static final int PACKED_Z_LENGTH = PACKED_X_LENGTH;
    private static final long PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
    private static final long PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
    private static final int Z_OFFSET = PACKED_X_LENGTH;

    public static long asLongPos(BlockPos pos) {
        long i = 0L;
        i |= ((long) pos.getX() & PACKED_X_MASK);
        return i | ((long) pos.getZ() & PACKED_Z_MASK) << Z_OFFSET;
    }

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
            map = new ChunkHeightMap(x, z);
            // ChunkMap.put(chunkPos, map);
            synchronized (RegionList) {
                RegionList.add(map);
            }

            h = Minecraft.getInstance().level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() - 1;
            map.updateHeight(pos, h);
            // return h;
        }
        return h;
    }

    public static final int FLAG_BLOCK = 1;
    public static final int FLAG_SLAB = 2;
    public static final int FLAG_STAIRS = 3;
    public static final int FLAG_LEAVES = 4;

    // 实际上这里之所以太慢还有个问题就是会一个方块访问七次
    public static List<BakedQuad> appendOverlay(BlockAndTintGetter blockAndTintGetter, BlockState state, BlockPos pos, Direction direction, RandomSource random, long seed, List<BakedQuad> list) {
        // Minecraft.getInstance().level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING,pos);
        // 不处理空列表，这代表着不处理这个方向
        // if (state.is(BlockTags.LEAVES) && !list.isEmpty()) {
        //     return List.of(new BakedQuadRetextured(list.get(0),
        //             ClientSetup.snowOverlayBlock.resolve().get().getQuads(null, Direction.UP, null).get(0).getSprite()));
        // }
        // if (true)return list;

        if ( direction != Direction.DOWN
                && !list.isEmpty()) {

            int flag = 0;
            if (state.getBlock() instanceof LeavesBlock) {
                flag = FLAG_LEAVES;
            } else if ((state.isSolidRender(blockAndTintGetter, pos)
                    // state.isSolid()
                    || state.getBlock() instanceof LeavesBlock
                    || (state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP)
                    || (state.getBlock() instanceof StairBlock && state.getValue(StairBlock.HALF) == Half.TOP))) {
                flag = FLAG_BLOCK;
            } else if (state.getBlock() instanceof SlabBlock) {
                flag = FLAG_SLAB;
            } else if (state.getBlock() instanceof StairBlock) {
                flag = FLAG_STAIRS;
            } else return list;

            boolean isLight = getHeightOrUpdate(pos, false) == pos.getY();
            // boolean isLight=getLight(blockAndTintGetter, pos, state, random)>=blockAndTintGetter.getMaxLightLevel();
            // long blockLong = asLongPos(pos);

            // isLight = (getHeightOrUpdate(pos, false) == pos.getY());
            // long time = System.currentTimeMillis();
            // var cpos = new ChunkPos(-1,0);
            // for (int i = 0; i < 100000 * 100; i++) {
            //     getHeightOrUpdate(pos, false) ;
            // // ChunkMap.get(cpos);
            // //     cpos.hashCode();
            // }
            // Ecliptic.logger(System.currentTimeMillis() - time);
            if (isLight
                    && state.getBlock() != Blocks.SNOW_BLOCK
                    && shouldSnowAt(blockAndTintGetter, pos, state, random, seed)) {
                // DynamicLeavesBlock
                var cc = quadMap.getOrDefault(list, null);
                // long time = System.currentTimeMillis();
                // for (int i = 0; i < 100000 * 100; i++) {
                //      // cc = quadMap.getOrDefault(list, null);
                //      //              getQuad(list, null);
                //     Minecraft.getInstance().level.getBiome(pos).is(Tags.Biomes.IS_DESERT);
                // }
                // var t1 = System.currentTimeMillis() - time;
                // Ecliptic.logger(t1,state);
                // var cc = getQuad(list, null);

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
                        // ClientSetup.models.get(new ModelResourceLocation(Ecliptic.ModContents.snowyStairs.getId(),"facing=north,half=bottom,shape=outer_left,waterlogged=true"))
                        // 楼梯的方向是无
                        snowModel = models.get(BlockModelShaper.stateToModelLocation(snowState));
                    }

                    if (snowModel != null) {
                        int size = list.size();
                        var snowList = snowModel.getQuads(snowState, direction, null);
                        ArrayList<BakedQuad> newList;
                        if (direction == Direction.UP) {
                            newList = (ArrayList<BakedQuad>) snowList;
                        } else {
                            newList = new ArrayList<BakedQuad>(size + snowList.size());
                            newList.addAll(list);
                            newList.addAll(snowList);
                        }
                        quadMap.put(list, newList);
                        setQuad(list, newList);
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
