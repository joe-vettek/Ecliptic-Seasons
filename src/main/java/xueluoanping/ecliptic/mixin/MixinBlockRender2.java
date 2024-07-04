package xueluoanping.ecliptic.mixin;


import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.light.LightMode;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.LightPipelineProvider;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.BakedQuadView;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.BakedModelEncoderFixer;
import xueluoanping.ecliptic.client.ClientSetup;

import java.util.List;

@Mixin({BlockRenderer.class})
public abstract class MixinBlockRender2 {

// ctx.world().world.getBlockState(ctx.pos)
    @Inject(at = {@At("HEAD")}, method = {"getGeometry"}, cancellable = true, remap = false)
    private void mixin_renderQuadList(BlockRenderContext ctx, Direction face, CallbackInfoReturnable<List<BakedQuad>> cir) {
        RandomSource random = this.random;
        random.setSeed(ctx.seed());
        var bakes=ctx.model().getQuads(ctx.state(), face, random, ctx.modelData(), ctx.renderLayer());
        if (ctx.state().getBlock() instanceof GrassBlock&& ctx.world().world.isEmptyBlock(ctx.pos().above())){
            bakes= ClientSetup.models.get(new ModelResourceLocation(new ResourceLocation("minecraft:snow_block"), "")).getQuads(ctx.state(), face, random, ctx.modelData(), ctx.renderLayer());
        }
        cir.setReturnValue(bakes);
    }

    @Shadow(remap = false) @Final private RandomSource random;
}
