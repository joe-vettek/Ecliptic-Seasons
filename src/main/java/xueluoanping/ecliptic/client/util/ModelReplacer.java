package xueluoanping.ecliptic.client.util;

import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.environment.solar.Season;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.fml.loading.FMLLoader;
import xueluoanping.ecliptic.Ecliptic;
import xueluoanping.ecliptic.client.ClientSetup;
import xueluoanping.ecliptic.util.SolarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModelReplacer {
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

    public static List<BakedQuad> appendOverlay(BlockAndTintGetter blockAndTintGetter, BlockState state, BlockPos pos, Direction direction, RandomSource random, List<BakedQuad> list) {
        // Minecraft.getInstance().level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING,pos);
        if (direction != null && direction != Direction.DOWN) {
            if (getLight(blockAndTintGetter, pos, random) >= blockAndTintGetter.getMaxLightLevel()) {
                BakedModel snowModel = null;
                BlockState snowState = null;
                if (ClientSetup.snowOverlayBlock.resolve().isPresent() && (state.isSolidRender(blockAndTintGetter, pos)
                        || state.is(BlockTags.LEAVES)
                        || (state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP))) {
                    snowModel=ClientSetup.snowOverlayBlock.resolve().get();
                } else if (ClientSetup.snowySlabBottom.resolve().isPresent() && state.getBlock() instanceof SlabBlock) {
                    list = new ArrayList<>(list);
                    snowModel=ClientSetup.snowySlabBottom.resolve().get();
                } else if (ClientSetup.models != null && state.getBlock() instanceof StairBlock) {
                    snowState = Ecliptic.ModContents.snowyStairs.get().defaultBlockState()
                            .setValue(StairBlock.FACING, state.getValue(StairBlock.FACING))
                            .setValue(StairBlock.HALF, state.getValue(StairBlock.HALF))
                            .setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE));
                    // ClientSetup.models.get(new ModelResourceLocation(Ecliptic.ModContents.snowyStairs.getId(),"facing=north,half=bottom,shape=outer_left,waterlogged=true"))
                    snowModel = ClientSetup.models.get(BlockModelShaper.stateToModelLocation(snowState));
                }
                if (snowModel != null) {
                    int size = list.size();
                    var snowList = snowModel.getQuads(snowState, direction, null);
                    var newList = new ArrayList<BakedQuad>(size + snowList.size());
                    newList.addAll(list);
                    newList.addAll(snowList);
                    list = newList;
                }
            }

            // 性能测试
            // if (ClientSetup.snowOverlayBlock.resolve().isPresent() && (state.isSolidRender(blockAndTintGetter, pos)
            //         || state.is(BlockTags.LEAVES)
            //         || (state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP))) {
            //     for (int i = 0; i < 100000; i++) {
            //         var a = getLight(blockAndTintGetter, pos, random) >= blockAndTintGetter.getMaxLightLevel();
            //     }
            //     var t1 = System.currentTimeMillis() - time;
            //     time = System.currentTimeMillis();
            //     for (int i = 0; i < 100000; i++) {
            //         if (ClientSetup.snowOverlayBlock.resolve().isPresent() && (state.isSolidRender(blockAndTintGetter, pos)
            //                 || state.is(BlockTags.LEAVES)
            //                 || (state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP))) {
            //             clist.addAll(ClientSetup.snowOverlayBlock.resolve().get().getQuads(null, direction, null));
            //         }
            //     }
            //     Ecliptic.logger(System.currentTimeMillis() - time, t1);
            // }
        }
        return list;
    }


    public static int getLight(BlockAndTintGetter blockAndTintGetter, BlockPos pos, RandomSource random) {
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
        // result += blockAndTintGetter.getBlockState(pos).is(BlockTags.SNOW) ? -100 : 0;
        // result += SolarUtil.getProvider(Minecraft.getInstance().level).getSolarTerm().getSeason() == Season.WINTER ? 0 : -100;
        if (result >= 15) {
            // TODO:未来设置为降雨
            // Ecliptic.logger(SolarUtil.getProvider(Minecraft.getInstance().level).getSnowLayer());
            result += (SolarUtil.getProvider(Minecraft.getInstance().level).getSnowLayer() >= random.nextFloat()) ? 0 : -100;
            // result+=blockAndTintGetter.getBlockState(pos.above()).isAir() ? 0 : -100;
        }
        return result;
    }
}
