package xueluoanping.ecliptic.client.util;

import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.environment.solar.Season;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.loading.FMLLoader;
import xueluoanping.ecliptic.client.ClientSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return direction == Direction.UP ? checkAndUpdate(blockAndTintGetter, state, pos, direction, bakedModel) : bakedModel;
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

    public static List<BakedQuad> appendOverlay(BlockAndTintGetter blockAndTintGetter, BlockState state, BlockPos pos, Direction direction, List<BakedQuad> list) {
        // Minecraft.getInstance().level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING,pos);

        if (direction != Direction.DOWN) {

            if (getLight(blockAndTintGetter, pos) >= blockAndTintGetter.getMaxLightLevel())
                if (ClientSetup.snowOverlayBlock.resolve().isPresent() && (state.isSolidRender(blockAndTintGetter, pos)
                        || state.is(BlockTags.LEAVES)
                        || (state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP))
                ) {
                    list = new ArrayList<>(list);
                    list.addAll(ClientSetup.snowOverlayBlock.resolve().get().getQuads(null, direction, null));
                } else if (ClientSetup.snowySlabBottom.resolve().isPresent() && state.is(BlockTags.SLABS)
                ) {
                    list = new ArrayList<>(list);
                    list.addAll(ClientSetup.snowySlabBottom.resolve().get().getQuads(null, direction, null));
                }
        }
        return list;
    }


    public static int getLight(BlockAndTintGetter blockAndTintGetter, BlockPos pos) {
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
        result += blockAndTintGetter.getBlockState(pos).is(BlockTags.SNOW) ? -100 : 0;
        result+=Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).resolve().get().getSolarTerm().getSeason()== Season.WINTER?0:-100;
        return result;
    }
}
