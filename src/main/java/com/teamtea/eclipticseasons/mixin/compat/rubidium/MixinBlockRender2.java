package com.teamtea.eclipticseasons.mixin.compat.rubidium;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.client.core.ModelManager;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Random;

@Mixin({BlockRenderer.class})
public abstract class MixinBlockRender2 {


    @Shadow(remap = false)
    @Final
    private Random random;

    @ModifyExpressionValue(
            remap = false,
            method = "renderModel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/model/IBakedModel;getQuads(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/Direction;Ljava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Ljava/util/List;",
            ordinal = 0))
    private List<BakedQuad> mixin_tesselateWithAO_getQuads(List<BakedQuad> original,
                                                @Local(argsOnly = true) IBlockDisplayReader world,
                                                @Local(argsOnly = true) BlockState blockState,
                                                @Local(ordinal = 0, argsOnly = true) BlockPos blockPos,
                                                @Local Direction direction,
                                                @Local(argsOnly = true) long seed) {
        return ModelManager.appendOverlay(world, blockState, blockPos, direction, random, seed, original);
    }

    @ModifyExpressionValue(
            remap = false,
            method = "renderModel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/model/IBakedModel;getQuads(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/Direction;Ljava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Ljava/util/List;",
            ordinal = 1))
    private List<BakedQuad> mixin_tesselateWithAO_getQuads2(List<BakedQuad> original,
                                                           @Local(argsOnly = true) IBlockDisplayReader world,
                                                           @Local(argsOnly = true) BlockState blockState,
                                                           @Local(ordinal = 0, argsOnly = true) BlockPos blockPos,
                                                           @Local(argsOnly = true) long seed) {
        return ModelManager.appendOverlay(world, blockState, blockPos, null, random, seed, original);
    }
}
