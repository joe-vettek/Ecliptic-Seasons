package com.teamtea.eclipticseasons.mixin.compat.embeddium;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.teamtea.eclipticseasons.client.core.ModelManager;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({BlockRenderer.class})
public abstract class MixinBlockRender2 {


    @Shadow(remap = false) @Final private RandomSource random;


    // @ModifyExpressionValue(
    //         remap = false,
    //         method = "getGeometry",
    //         at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;")
    // )
    // private List<BakedQuad> mixin_tesselateWithAO_getQuads(List<BakedQuad> original, @Local(ordinal = 0) BlockRenderContext ctx,  @Local(ordinal = 0)Direction face) {
    //     return ModelManager.appendOverlay(ctx.world(),ctx.state(),ctx.pos(),face,random,ctx.seed(),original);
    // }



    // @WrapOperation(
    //         remap = false,
    //         method = "renderModel",
    //         at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;isFaceVisible(Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderContext;Lnet/minecraft/core/Direction;)Z")
    // )
    // private boolean mixin$renderModel_isFaceVisible(BlockRenderer blockRenderer, BlockRenderContext ctx, Direction face, Operation<Boolean> original) {
    //     return ModelManager.shouldisFaceVisible(blockRenderer,ctx,face,original);
    // }
}
