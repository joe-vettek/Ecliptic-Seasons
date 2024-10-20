package com.teamtea.eclipticseasons.mixin.compat.embeddium;


import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.client.core.ModelManager;
import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({ChunkBuilderMeshingTask.class})
public abstract class MixinBlockRenderTask {

    // @Inject(
    //         remap = false,
    //         method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
    //         at = @At(value = "INVOKE",
    //                 // shift = At.Shift.AFTER,
    //                 target = "Lnet/minecraft/client/resources/model/BakedModel;getRenderTypes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;)Lnet/minecraftforge/client/ChunkRenderTypeSet;")
    // )
    // private void mixin_tesselateWithAO_getQuads(
    //         ChunkBuildContext buildContext,
    //         CancellationToken cancellationToken,
    //         CallbackInfoReturnable<ChunkBuildOutput> cir,
    //         @Local BlockRenderContext ctx,
    //         @Local ChunkBuildBuffers buffers,
    //         @Local BlockRenderCache cache,
    //         @Local(ordinal = 0) BlockPos.MutableBlockPos mutableBlockPos,
    //         @Local(ordinal = 1) BlockPos.MutableBlockPos mutableBlockPos2,
    //         @Local(ordinal = 0) BlockState state
    // ) {
    //
    //     if (ModelManager.shouldCutoutMipped(state))
    //     {
    //         BakedModel bakedModel = Minecraft.getInstance().getModelManager().getModel(BlockModelShaper.stateToModelLocation(EclipticSeasons.ModContents.snowyBlock.get().defaultBlockState()));
    //         ctx.update(mutableBlockPos,
    //                 mutableBlockPos2,
    //                 EclipticSeasons.ModContents.snowyBlock.get().defaultBlockState(),
    //                 bakedModel,
    //                 state.getSeed(mutableBlockPos),
    //                 null,
    //                 RenderType.cutoutMipped());
    //         cache.getBlockRenderer().renderModel(ctx, buffers);
    //     }
    // }


}
