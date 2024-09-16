package com.teamtea.eclipticseasons.mixin.compat.sodium;


import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin({AbstractBlockRenderContext.class})
public abstract class MixinAbstractBlockRenderContext {
    @Shadow protected long randomSeed;
    @Shadow @Final protected Supplier<RandomSource> randomSupplier;
    @Shadow protected RandomSource random;
// @Shadow(remap = false) @Final private RandomSource random;
    //
    // @ModifyExpressionValue(
    //         remap = false,
    //         method = "getGeometry",
    //         at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;")
    // )
    // private List<BakedQuad> ecliptic$tesselateWithAO_getQuads(List<BakedQuad> original, @Local(ordinal = 0) BlockRenderContext ctx, @Local(ordinal = 0)Direction face) {
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
