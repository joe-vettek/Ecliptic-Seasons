package com.teamtea.eclipticseasons.client.render.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamtea.eclipticseasons.common.block.blockentity.WindChimesBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class WindChimesBlockEntityRenderer implements BlockEntityRenderer<WindChimesBlockEntity> {
    public WindChimesBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(WindChimesBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLight, int combinedOverlay) {

    }
}
