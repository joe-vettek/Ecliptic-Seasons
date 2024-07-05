package xueluoanping.ecliptic.client.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import xueluoanping.ecliptic.client.ClientSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelReplacer {
    // if (state.getBlock() instanceof GrassBlock&& blockAndTintGetter.getBlockState(pos.above()).isAir()
    public static BakedModel checkAndUpdate(BlockAndTintGetter blockAndTintGetter, BlockState state, BlockPos pos, Direction direction, BakedModel bakedModel) {
        var out = bakedModel;
        // &&blockAndTintGetter.canSeeSky(pos)
        if (ClientSetup.snowModel.resolve().isPresent()
                && blockAndTintGetter.getBlockState(pos.above()).isAir()&& state.isSolidRender(blockAndTintGetter, pos))
        {
            out = ClientSetup.snowModel.resolve().get();
            // out=new WarpBakedModel(bakedModel,ClientSetup.snowModel.resolve().get());
        }
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
        // if ( direction != Direction.DOWN)
        {
            if (ClientSetup.snowOverlayBlock.resolve().isPresent() && state.isSolidRender(blockAndTintGetter, pos)
                    && blockAndTintGetter.getBlockState(pos.above()).isAir()) {
                list = new ArrayList<>(list);
                list.addAll(ClientSetup.snowOverlayBlock.resolve().get().getQuads(null, direction, null));
            } else if (ClientSetup.snowySlabBottom.resolve().isPresent() && state.is(BlockTags.SLABS)
                    && blockAndTintGetter.getBlockState(pos.above()).isAir()) {
                list = new ArrayList<>(list);
                list.addAll(ClientSetup.snowySlabBottom.resolve().get().getQuads(null, direction, null));
            }
        }
        return list;
    }
}
