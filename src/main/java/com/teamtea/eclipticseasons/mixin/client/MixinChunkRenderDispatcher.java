package com.teamtea.eclipticseasons.mixin.client;


import com.llamalad7.mixinextras.sugar.Local;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.client.core.ModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin({ChunkRenderDispatcher.RenderChunk.RebuildTask.class})
public class MixinChunkRenderDispatcher {
    //
    // @Dynamic
    // @Shadow @Final ChunkRenderDispatcher.RenderChunk this$0;

    // @Inject(
    //         remap = false,
    //         method = "compile",
    //         at = @At(value = "INVOKE",
    //                 // shift = At.Shift.AFTER,
    //                 target = "Lnet/minecraft/client/resources/model/BakedModel;getRenderTypes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;)Lnet/minecraftforge/client/ChunkRenderTypeSet;")
    // )
    // private void mixin_compile_extraSnowyModel(
    //         float pX,
    //         float pY,
    //         float pZ,
    //         ChunkBufferBuilderPack pChunkBufferBuilderPack,
    //         CallbackInfoReturnable<ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults> cir,
    //         @Local(ordinal = 2) BlockPos blockpos2,
    //         @Local(ordinal = 1) BlockState blockstate,
    //         @Local PoseStack posestack,
    //         @Local RenderChunkRegion renderchunkregion,
    //         @Local RandomSource randomsource,
    //         @Local Set<RenderType> renderTypeSet
    //         ) {
    //
    //     if (ModelManager.shouldCutoutMipped(blockstate)) {
    //         BakedModel bakedModel = Minecraft.getInstance().getModelManager().getModel(BlockModelShaper.stateToModelLocation(EclipticSeasons.ModContents.snowyBlock.get().defaultBlockState()));
    //         RenderType renderType = RenderType.cutoutMipped();
    //         BufferBuilder bufferbuilder2 = pChunkBufferBuilderPack.builder(RenderType.cutoutMipped());
    //         // this$0.beginLayer(bufferbuilder2);
    //         if(renderTypeSet.add(renderType))
    //             bufferbuilder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
    //         long seed=blockstate.getSeed(blockpos2);
    //         posestack.pushPose();
    //         posestack.translate((float)(blockpos2.getX() & 15), (float)(blockpos2.getY() & 15), (float)(blockpos2.getZ() & 15));
    //         Minecraft.getInstance().getBlockRenderer().getModelRenderer()
    //                 .tesselateBlock(renderchunkregion,
    //                         bakedModel,
    //                         EclipticSeasons.ModContents.snowyBlock.get().defaultBlockState(),
    //                         blockpos2,
    //                         posestack,
    //                         bufferbuilder2,
    //                         true,
    //                         randomsource,
    //                         blockstate.getSeed(blockpos2),
    //                         OverlayTexture.NO_OVERLAY,
    //                         ModelData.EMPTY,
    //                         renderType);
    //         posestack.popPose();
    //     }
    // }

}
