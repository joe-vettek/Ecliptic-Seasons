package com.teamtea.eclipticseasons.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SnowCleaner extends TextureSheetParticle {
    public SnowCleaner(ClientLevel pLevel, double pX, double pY, double pZ, ItemStack itemStack) {
        super(pLevel, pX, pY, pZ);
        this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(itemStack, pLevel, null, 0).getParticleIcon());
        this.gravity = 0.0F;
        this.lifetime = 80;
        this.hasPhysics = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return 0.5F;
    }
}
