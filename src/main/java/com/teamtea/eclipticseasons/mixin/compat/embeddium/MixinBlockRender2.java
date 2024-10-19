package com.teamtea.eclipticseasons.mixin.compat.embeddium;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.teamtea.eclipticseasons.client.core.ModelManager;

import java.util.List;
import java.util.Random;

@Mixin({BlockRenderer.class})
public abstract class MixinBlockRender2 {


    @Shadow(remap = false) @Final private Random random;

    @ModifyExpressionValue(
            remap = false,
            method = "renderModel",
            at = @At(value = "INVOKE", ordinal = 0,
                    target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Ljava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Ljava/util/List;")
    )
    private List<BakedQuad> mixin_tesselateWithAO_getQuads(List<BakedQuad> original,
                                                           @Local(argsOnly = true) BlockAndTintGetter world,
                                                           @Local(argsOnly = true) BlockState blockState,
                                                           @Local(ordinal = 0, argsOnly = true) BlockPos blockPos,
                                                           @Local Direction direction,
                                                           @Local(argsOnly = true) long seed) {
        return ModelManager.appendOverlay(world, blockState, blockPos, direction, random, seed, original);
    }

    @ModifyExpressionValue(
            remap = false,
            method = "renderModel",
            at = @At(value = "INVOKE", ordinal = 1,
                    target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Ljava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Ljava/util/List;")
    )
    private List<BakedQuad> mixin_tesselateWithAO_getQuads2(List<BakedQuad> original,
                                                            @Local(argsOnly = true) BlockAndTintGetter world,
                                                            @Local(argsOnly = true) BlockState blockState,
                                                            @Local(ordinal = 0, argsOnly = true) BlockPos blockPos,
                                                            @Local(argsOnly = true) long seed) {
        return ModelManager.appendOverlay(world, blockState, blockPos, null, random, seed, original);
    }

    // @WrapOperation(
    //         remap = false,
    //         method = "renderModel",
    //         at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;isFaceVisible(Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderContext;Lnet/minecraft/core/Direction;)Z")
    // )
    // private boolean mixin$renderModel_isFaceVisible(BlockRenderer blockRenderer, BlockRenderContext ctx, Direction face, Operation<Boolean> original) {
    //     return ModelManager.shouldisFaceVisible(blockRenderer,ctx,face,original);
    // }
}
