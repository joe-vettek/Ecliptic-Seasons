package xueluoanping.ecliptic.mixin.compat.embeddium;


import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.core.ModelManager;

import java.util.List;

@Mixin({BlockRenderer.class})
public abstract class MixinBlockRender2 {

// ctx.world().world.getBlockState(ctx.pos)
    @Inject(at = {@At("HEAD")}, method = {"getGeometry"}, cancellable = true, remap = false)
    private void mixin_renderQuadList(BlockRenderContext ctx, Direction face, CallbackInfoReturnable<List<BakedQuad>> cir) {
        RandomSource random = this.random;
        random.setSeed(ctx.seed());
        var model= ModelManager.checkDirectionAndUpdate(ctx.world(),ctx.state(),ctx.pos(),face,ctx.model());
        var bakes=model.getQuads(ctx.state(), face, random, ctx.modelData(), ctx.renderLayer());
        bakes= ModelManager.appendOverlay(ctx.world(),ctx.state(),ctx.pos(),face,random,ctx.seed(),bakes);
        cir.setReturnValue(bakes);
    }

    @Shadow(remap = false) @Final private RandomSource random;
}
