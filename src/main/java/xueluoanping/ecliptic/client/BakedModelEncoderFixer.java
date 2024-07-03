package xueluoanping.ecliptic.client;

// import com.mojang.blaze3d.vertex.PoseStack;
// import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
// import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
// import net.caffeinemc.mods.sodium.api.math.MatrixHelper;
// import net.caffeinemc.mods.sodium.api.util.ColorABGR;
// import net.caffeinemc.mods.sodium.api.util.ColorARGB;
// import net.caffeinemc.mods.sodium.api.util.ColorU8;
// import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
// import net.caffeinemc.mods.sodium.api.vertex.format.common.ModelVertex;
// import org.joml.Matrix3f;
// import org.joml.Matrix4f;
// import org.lwjgl.system.MemoryStack;

// import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
// import me.jellysquid.mods.sodium.client.model.light.LightMode;
// import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
// import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
// import me.jellysquid.mods.sodium.client.model.quad.BakedQuadView;
// import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
// import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
// import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
// import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BakedModelEncoderFixer {
    // public static void renderQuadList(BlockRenderer blockRenderer, BlockRenderContext ctx, Material material, LightPipeline lighter, ColorProvider<BlockState> colorizer, Vec3 offset, ChunkModelBuilder builder, List<BakedQuad> quads, Direction cullFace) {
        // blockRenderer.useReorienting = true;
        // int i = 0;
        //
        // int quadsSize;
        // for(quadsSize = quads.size(); i < quadsSize; ++i) {
        //     if (!((BakedQuad)quads.get(i)).hasAmbientOcclusion()) {
        //         blockRenderer.useReorienting = false;
        //         break;
        //     }
        // }
        //
        // i = 0;
        //
        // for(quadsSize = quads.size(); i < quadsSize; ++i) {
        //     BakedQuadView quad = (BakedQuadView)quads.get(i);
        //     QuadLightData lightData = blockRenderer.getVertexLight(ctx, quad.hasAmbientOcclusion() ? lighter : blockRenderer.lighters.getLighter(LightMode.FLAT), cullFace, quad);
        //     int[] vertexColors = blockRenderer.getVertexColors(ctx, colorizer, quad);
        //     blockRenderer.writeGeometry(ctx, builder, offset, material, quad, vertexColors, lightData);
        //     TextureAtlasSprite sprite = quad.getSprite();
        //     if (sprite != null) {
        //         builder.addSprite(sprite);
        //     }
        // }

    // }

    public static TextureAtlasSprite getSnow() {
        try {
            return Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(new ResourceLocation("block/snow"));
        } catch (Exception e) {
            return null;
        }
    }
    // public static void writeQuadVertices(VertexBufferWriter writer, PoseStack.Pose matrices, ModelQuadView quad, int color, int light, int overlay) {
    //     Matrix3f matNormal = matrices.normal();
    //     Matrix4f matPosition = matrices.pose();
    //     MemoryStack stack = MemoryStack.stackPush();
    //
    //     try {
    //         long buffer = stack.nmalloc(144);
    //         long ptr = buffer;
    //         int normal = MatrixHelper.transformNormal(matNormal, quad.getNormal());
    //         int i = 0;
    //
    //         while (true) {
    //             if (i >= 4) {
    //                 writer.push(stack, buffer, 4, ModelVertex.FORMAT);
    //                 break;
    //             }
    //
    //             float x = quad.getX(i);
    //             float y = quad.getY(i);
    //             float z = quad.getZ(i);
    //             float xt = MatrixHelper.transformPositionX(matPosition, x, y, z);
    //             float yt = MatrixHelper.transformPositionY(matPosition, x, y, z);
    //             float zt = MatrixHelper.transformPositionZ(matPosition, x, y, z);
    //             ModelVertex.write(ptr, xt, yt, zt, multARGBInts(quad.getColor(i), color, 1), quad.getTexU(i), quad.getTexV(i), overlay, ModelQuadUtil.mergeBakedLight(quad.getLight(i), light), mergeNormalAndMult(quad.getForgeNormal(i), normal, matNormal));
    //             ptr += 36L;
    //             ++i;
    //         }
    //     } catch (Throwable var22) {
    //         if (stack != null) {
    //             try {
    //                 stack.close();
    //             } catch (Throwable var21) {
    //                 var22.addSuppressed(var21);
    //             }
    //         }
    //
    //         throw var22;
    //     }
    //
    //     if (stack != null) {
    //         stack.close();
    //     }
    //
    // }
    //
    //
    // public static void writeQuadVertices2(VertexBufferWriter writer, PoseStack.Pose matrices, ModelQuadView quad, float r, float g, float b, float[] brightnessTable, boolean colorize, int[] light, int overlay) {
    //     Matrix3f matNormal = matrices.normal();
    //     Matrix4f matPosition = matrices.pose();
    //     MemoryStack stack = MemoryStack.stackPush();
    //
    //     try {
    //         long buffer = stack.nmalloc(144);
    //         long ptr = buffer;
    //         int normal = MatrixHelper.transformNormal(matNormal, quad.getNormal());
    //
    //         for(int i = 0; i < 4; ++i) {
    //             float x = quad.getX(i);
    //             float y = quad.getY(i);
    //             float z = quad.getZ(i);
    //             float xt = MatrixHelper.transformPositionX(matPosition, x, y, z);
    //             float yt = MatrixHelper.transformPositionY(matPosition, x, y, z);
    //             float zt = MatrixHelper.transformPositionZ(matPosition, x, y, z);
    //             float brightness = brightnessTable[i];
    //             float fR;
    //             float fG;
    //             float fB;
    //             int color;
    //             if (colorize) {
    //                 color = quad.getColor(i);
    //                 float oR = ColorU8.byteToNormalizedFloat(ColorABGR.unpackRed(color));
    //                 float oG = ColorU8.byteToNormalizedFloat(ColorABGR.unpackGreen(color));
    //                 float oB = ColorU8.byteToNormalizedFloat(ColorABGR.unpackBlue(color));
    //                 fR = oR * brightness * r;
    //                 fG = oG * brightness * g;
    //                 fB = oB * brightness * b;
    //             } else {
    //                 fR = brightness * r;
    //                 fG = brightness * g;
    //                 fB = brightness * b;
    //             }
    //
    //             color = ColorABGR.pack(fR, fG, fB, 1.0F);
    //             ModelVertex.write(ptr, xt, yt, zt, color, quad.getTexU(i), quad.getTexV(i), overlay, ModelQuadUtil.mergeBakedLight(quad.getLight(i), light[i]), mergeNormalAndMult(quad.getForgeNormal(i), normal, matNormal));
    //             ptr += 36L;
    //         }
    //
    //         writer.push(stack, buffer, 4, ModelVertex.FORMAT);
    //     } catch (Throwable var34) {
    //         if (stack != null) {
    //             try {
    //                 stack.close();
    //             } catch (Throwable var33) {
    //                 var34.addSuppressed(var33);
    //             }
    //         }
    //
    //         throw var34;
    //     }
    //
    //     if (stack != null) {
    //         stack.close();
    //     }
    // }
    //
    // private static int multARGBInts(int colorA, int colorB, float per) {
    //     if (colorA == -1) {
    //         return colorB;
    //     } else if (colorB == -1) {
    //         return colorA;
    //     } else {
    //         int a = (int) ((float) ColorARGB.unpackAlpha(colorA) / 255.0F * per + (1 - per) * ((float) ColorARGB.unpackAlpha(colorB) / 255.0F) * 255.0F);
    //         int b = (int) ((float) ColorARGB.unpackBlue(colorA) / 255.0F * per + (1 - per) * ((float) ColorARGB.unpackBlue(colorB) / 255.0F) * 255.0F);
    //         int g = (int) ((float) ColorARGB.unpackGreen(colorA) / 255.0F * per + (1 - per) * ((float) ColorARGB.unpackGreen(colorB) / 255.0F) * 255.0F);
    //         int r = (int) ((float) ColorARGB.unpackRed(colorA) / 255.0F * per + (1 - per) * ((float) ColorARGB.unpackRed(colorB) / 255.0F) * 255.0F);
    //         return ColorARGB.pack(r, g, b, a);
    //     }
    // }
    //
    // private static int mergeNormalAndMult(int packed, int calc, Matrix3f matNormal) {
    //     return (packed & 16777215) == 0 ? calc : MatrixHelper.transformNormal(matNormal, packed);
    // }


}
