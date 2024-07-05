// package xueluoanping.ecliptic.mixin;
//
//
// import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
// import me.jellysquid.mods.sodium.client.model.light.LightMode;
// import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
// import me.jellysquid.mods.sodium.client.model.light.LightPipelineProvider;
// import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
// import me.jellysquid.mods.sodium.client.model.quad.BakedQuadView;
// import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
// import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
// import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
// import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
// import net.minecraft.client.renderer.block.model.BakedQuad;
// import net.minecraft.client.renderer.texture.TextureAtlasSprite;
// import net.minecraft.core.Direction;
// import net.minecraft.world.level.block.state.BlockState;
// import net.minecraft.world.phys.Vec3;
// import org.spongepowered.asm.mixin.Final;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import xueluoanping.ecliptic.client.BakedModelEncoderFixer;
//
// import java.util.List;
//
// @Mixin({BlockRenderer.class})
// public abstract class MixinBlockRender {
//
//     @Shadow(remap = false) private boolean useReorienting;
//
//     @Shadow(remap = false) protected abstract QuadLightData getVertexLight(BlockRenderContext ctx, LightPipeline lighter, Direction cullFace, BakedQuadView quad);
//
//     @Shadow(remap = false) protected abstract int[] getVertexColors(BlockRenderContext ctx, ColorProvider<BlockState> colorProvider, BakedQuadView quad);
//
//     @Inject(at = {@At("HEAD")}, method = {"renderQuadList"}, cancellable = true, remap = false)
//     private void mixin_renderQuadList(BlockRenderContext ctx, Material material, LightPipeline lighter, ColorProvider<BlockState> colorizer, Vec3 offset, ChunkModelBuilder builder, List<BakedQuad> quads, Direction cullFace, CallbackInfo ci) {
//         // BakedModelEncoderFixer.renderQuadList(((BlockRenderer)(Object)this),ctx, material, lighter, colorizer, offset, builder,quads,cullFace);
//         this.useReorienting = true;
//         int i = 0;
//
//         int quadsSize;
//         for(quadsSize = quads.size(); i < quadsSize; ++i) {
//             if (!((BakedQuad)quads.get(i)).hasAmbientOcclusion()) {
//                 this.useReorienting = false;
//                 break;
//             }
//         }
//
//         i = 0;
//
//         for(quadsSize = quads.size(); i < quadsSize; ++i) {
//             BakedQuadView quad = (BakedQuadView)quads.get(i);
//             QuadLightData lightData = this.getVertexLight(ctx, quad.hasAmbientOcclusion() ? lighter : this.lighters.getLighter(LightMode.FLAT), cullFace, quad);
//             int[] vertexColors = this.getVertexColors(ctx, colorizer, quad);
//             this.writeGeometry(ctx, builder, offset, material, quad, vertexColors, lightData);
//             TextureAtlasSprite sprite = quad.getSprite();
//
//             try {
//                 if(quad.getSprite().contents().name().toString().contains("grass")){
//                     sprite=BakedModelEncoderFixer.getSnow();
//                 }
//             } catch (Exception e) {
//
//             }
//             if (sprite != null) {
//                 builder.addSprite(sprite);
//             }
//         }
//         ci.cancel();
//     }
//
//     @Shadow(remap = false) protected abstract void writeGeometry(BlockRenderContext ctx, ChunkModelBuilder builder, Vec3 offset, Material material, BakedQuadView quad, int[] colors, QuadLightData light);
//
//     @Shadow(remap = false) @Final private LightPipelineProvider lighters;
// }
