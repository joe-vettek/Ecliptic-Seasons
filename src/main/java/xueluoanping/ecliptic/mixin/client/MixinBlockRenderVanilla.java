package xueluoanping.ecliptic.mixin.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.ClientSetup;
import xueluoanping.ecliptic.client.util.ModelReplacer;

import java.util.BitSet;
import java.util.List;

@Mixin({ModelBlockRenderer.class})
public abstract class MixinBlockRenderVanilla {
    @Shadow @Final private static Direction[] DIRECTIONS;

    @Shadow protected abstract void renderModelFaceAO(BlockAndTintGetter p_111013_, BlockState p_111014_, BlockPos p_111015_, PoseStack p_111016_, VertexConsumer p_111017_, List<BakedQuad> p_111018_, float[] p_111019_, BitSet p_111020_, ModelBlockRenderer.AmbientOcclusionFace p_111021_, int p_111022_);

    // ctx.world().world.getBlockState(ctx.pos)
    @Inject(at = {@At("HEAD")}, method = {"tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"}, cancellable = true,remap = false)
    private void mixin_tesselateWithAO(BlockAndTintGetter p_111079_, BakedModel p_111080_, BlockState p_111081_, BlockPos p_111082_, PoseStack p_111083_, VertexConsumer p_111084_, boolean p_111085_, RandomSource p_111086_, long p_111087_, int p_111088_, ModelData modelData, RenderType renderType, CallbackInfo ci) {
        float[] afloat = new float[DIRECTIONS.length * 2];
        BitSet bitset = new BitSet(3);
        ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_111082_.mutable();

        var newmodel=ModelReplacer.checkAndUpdate(p_111079_,p_111081_,p_111082_,Direction.UP,p_111080_);

        for(Direction direction : DIRECTIONS) {
            p_111086_.setSeed(p_111087_);
            List<BakedQuad> list = (direction==Direction.UP?newmodel:p_111080_).getQuads(p_111081_, direction, p_111086_, modelData, renderType);
            list=ModelReplacer.appendOverlay(p_111079_,p_111081_,p_111082_,direction,list);
            if (!list.isEmpty()) {
                blockpos$mutableblockpos.setWithOffset(p_111082_, direction);
                if (!p_111085_ || Block.shouldRenderFace(p_111081_, p_111079_, p_111082_, direction, blockpos$mutableblockpos)) {
                    this.renderModelFaceAO(p_111079_, p_111081_, p_111082_, p_111083_, p_111084_, list, afloat, bitset, modelblockrenderer$ambientocclusionface, p_111088_);
                }
            }
        }

        p_111086_.setSeed(p_111087_);
        List<BakedQuad> list1 = p_111080_.getQuads(p_111081_, (Direction)null, p_111086_, modelData, renderType);
        if (!list1.isEmpty()) {
            this.renderModelFaceAO(p_111079_, p_111081_, p_111082_, p_111083_, p_111084_, list1, afloat, bitset, modelblockrenderer$ambientocclusionface, p_111088_);
        }

        ci.cancel();

    }

}
