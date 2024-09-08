package com.teamtea.eclipticseasons.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class ButterflyParticle extends FireflyParticle {


    private final SpriteSet spriteSet;
    private boolean isBlink;
    private Vec3 nextPos;

    public ButterflyParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, spriteSet);
        this.lifetime = 800;
        this.gravity = 1E-4f;
        this.spriteSet = spriteSet;

        this.isBlink = false;
        // setSpriteFromAge(this.spriteSet);
        setSprite(spriteSet.get(level.getRandom()));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected void renderRotatedQuad(VertexConsumer pBuffer, Quaternionf pQuaternion, float pX, float pY, float pZ, float pPartialTicks) {
        float f = this.getQuadSize(pPartialTicks);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int i = this.getLightColor(pPartialTicks);
        i = 15728880;
        if (this.age >= this.lifetime * 0.8) {
            i = this.getLightColor(pPartialTicks);
        }

        float ff = System.currentTimeMillis() % 4000;

        ff = (ff - 2000) / 2000f;

        // pQuaternion=pQuaternion.rotateXYZ(0,0,-Mth.DEG_TO_RAD*90);
        // 主要问题在于枢纽点，后续更换纹理需要更新xoffse和yoffeset
        // pQuaternion = pQuaternion.rotateAxis(ff*45*Mth.DEG_TO_RAD,1,0,0);

        float pXOffset1 = .65f;
        float pXOffset0 = -0.35f;
        float pYOffset1 = .65f;
        float pYOffset0 = -0.35f;
        pQuaternion=new Quaternionf();

        pQuaternion = pQuaternion.rotateAxis(ff * 45 * Mth.DEG_TO_RAD, 1, 0, 0);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset1, pYOffset0, f, u1, v1, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset1, pYOffset1, f, u1, v0, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset0, pYOffset1, f, u0, v0, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset0, pYOffset0, f, u0, v1, i, 1f);

        pQuaternion = pQuaternion.rotateAxis(ff * -90 * Mth.DEG_TO_RAD, 1, 0, 0);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset1, pYOffset0, f, u1, v1, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset1, pYOffset1, f, u1, v0, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset0, pYOffset1, f, u0, v0, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset0, pYOffset0, f, u0, v1, i, 1f);
// super.renderRotatedQuad(pBuffer, pQuaternion, pX, pY, pZ, pPartialTicks);
    }

    @Override
    protected void renderVertex(VertexConsumer pBuffer, Quaternionf pQuaternion, float pX, float pY, float pZ, float pXOffset, float pYOffset, float pQuadSize, float pU, float pV, int pPackedLight, float alpha) {
        super.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, pXOffset, pYOffset, pQuadSize, pU, pV, pPackedLight, alpha);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
