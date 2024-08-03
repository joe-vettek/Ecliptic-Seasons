package com.teamtea.eclipticseasons.mixin.client;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.teamtea.eclipticseasons.client.core.ModelManager;

import java.util.BitSet;
import java.util.List;

@Mixin({ModelBlockRenderer.class})
public abstract class MixinBlockRenderVanilla {
    @Shadow
    @Final
    private static Direction[] DIRECTIONS;

    @Shadow
    protected abstract void renderModelFaceAO(BlockAndTintGetter p_111013_, BlockState p_111014_, BlockPos p_111015_, PoseStack p_111016_, VertexConsumer p_111017_, List<BakedQuad> p_111018_, float[] p_111019_, BitSet p_111020_, ModelBlockRenderer.AmbientOcclusionFace p_111021_, int p_111022_);

    // ctx.world().world.getBlockState(ctx.pos)
    // @Inject(at = {@At("HEAD")}, method = {"tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"}, cancellable = true, remap = false)
    // private void mixin_tesselateWithAO(BlockAndTintGetter blockAndTintGetter,
    //                                    BakedModel bakedModel,
    //                                    BlockState state,
    //                                    BlockPos pos,
    //                                    PoseStack poseStack,
    //                                    VertexConsumer vertexConsumer,
    //                                    boolean p_111085_,
    //                                    RandomSource randomSource,
    //                                    long seed,
    //                                    int p_111088_,
    //                                    ModelData modelData,
    //                                    RenderType renderType,
    //                                    CallbackInfo ci) {
    //     float[] afloat = new float[DIRECTIONS.length * 2];
    //     BitSet bitset = new BitSet(3);
    //     ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();
    //     BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
    //
    //     var newmodel = bakedModel;
    //     for (Direction direction : DIRECTIONS) {
    //         randomSource.setSeed(seed);
    //         List<BakedQuad> list = bakedModel.getQuads(state, direction, randomSource, modelData, renderType);
    //         list = ModelManager.appendOverlay(blockAndTintGetter, state, pos, direction, randomSource, seed, list);
    //         if (!list.isEmpty()) {
    //             blockpos$mutableblockpos.setWithOffset(pos, direction);
    //             if (!p_111085_ || Block.shouldRenderFace(state, blockAndTintGetter, pos, direction, blockpos$mutableblockpos)) {
    //                 this.renderModelFaceAO(blockAndTintGetter, state, pos, poseStack, vertexConsumer, list, afloat, bitset, modelblockrenderer$ambientocclusionface, p_111088_);
    //             }
    //         }
    //     }
    //
    //     randomSource.setSeed(seed);
    //     List<BakedQuad> list1 = bakedModel.getQuads(state, (Direction) null, randomSource, modelData, renderType);
    //     list1 = ModelManager.appendOverlay(blockAndTintGetter, state, pos, (Direction) null, randomSource, seed, list1);
    //
    //     if (!list1.isEmpty()) {
    //         this.renderModelFaceAO(blockAndTintGetter, state, pos, poseStack, vertexConsumer, list1, afloat, bitset, modelblockrenderer$ambientocclusionface, p_111088_);
    //     }
    //     ci.cancel();
    //
    // }


    @ModifyExpressionValue(
            remap = false,
            method = "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;")
    )
    private List<BakedQuad> mixin_tesselateWithAO_getQuads(List<BakedQuad> original,@Local(ordinal = 0)BlockAndTintGetter blockAndTintGetter,@Local(ordinal = 0)BlockPos pos,@Local(ordinal = 0)BlockState state, @Local(ordinal = 0)Direction direction, @Local(ordinal = 0)RandomSource randomSource, @Local(ordinal = 0) long seed) {
        return ModelManager.appendOverlay(blockAndTintGetter, state, pos, direction, randomSource, seed, original);
    }

    @ModifyExpressionValue(
            remap = false,
            method = "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;")
    )
    private List<BakedQuad> mixin_tesselateWithAO_getQuads2(List<BakedQuad> original,@Local(ordinal = 0)BlockAndTintGetter blockAndTintGetter,@Local(ordinal = 0)BlockPos pos,@Local(ordinal = 0)BlockState state, @Local(ordinal = 0)RandomSource randomSource, @Local(ordinal = 0) long seed) {
        return ModelManager.appendOverlay(blockAndTintGetter, state, pos, (Direction) null, randomSource, seed, original);
    }

    @ModifyExpressionValue(
            remap = false,
            method = "tesselateWithoutAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;")
    )
    private List<BakedQuad> mixin_tesselateWithoutAO_getQuads(List<BakedQuad> original,@Local(ordinal = 0)BlockAndTintGetter blockAndTintGetter,@Local(ordinal = 0)BlockPos pos,@Local(ordinal = 0)BlockState state, @Local(ordinal = 0)Direction direction, @Local(ordinal = 0)RandomSource randomSource, @Local(ordinal = 0) long seed) {
        return ModelManager.appendOverlay(blockAndTintGetter, state, pos, direction, randomSource, seed, original);
    }

    @ModifyExpressionValue(
            remap = false,
            method = "tesselateWithoutAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;")
    )
    private List<BakedQuad> mixin_tesselateWithoutAO_getQuads2(List<BakedQuad> original,@Local(ordinal = 0)BlockAndTintGetter blockAndTintGetter,@Local(ordinal = 0)BlockPos pos,@Local(ordinal = 0)BlockState state, @Local(ordinal = 0)RandomSource randomSource, @Local(ordinal = 0) long seed) {
        return ModelManager.appendOverlay(blockAndTintGetter, state, pos, (Direction) null, randomSource, seed, original);
    }
}
