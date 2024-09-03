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
        setSpriteFromAge(this.spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected void renderRotatedQuad(VertexConsumer pBuffer, Quaternionf pQuaternion, float pX, float pY, float pZ, float pPartialTicks) {
        float f = this.getQuadSize(pPartialTicks);
        float f1 = this.getU0();
        float f2 = this.getU1();
        float f3 = this.getV0();
        float f4 = this.getV1();
        int i = this.getLightColor(pPartialTicks);
        i = 15728880;
        if (this.age >= this.lifetime * 0.8) {
            i = this.getLightColor(pPartialTicks);
        }

        float ff=System.currentTimeMillis()%4000;

        ff= (ff-2000)/2000f;

        // 主要问题在于枢纽点，后续更换纹理需要更新xoffse和yoffeset
        pQuaternion = pQuaternion.rotateAxis(ff*45*Mth.DEG_TO_RAD,1,0,0);
        // pQuaternion = pQuaternion.add( new Quaternionf(0, 0.2f, 0, 0));
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, .65f, -0.35f, f, f2, f4, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, .65f, .65f, f, f2, f3, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, -.35f, .65f, f, f1, f3, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, -0.35f, -0.35f, f, f1, f4, i, 1f);

        pQuaternion = pQuaternion.rotateAxis(ff*-90*Mth.DEG_TO_RAD,1,0,0);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, .65f, -0.35f, f, f2, f4, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, .65f, .65f, f, f2, f3, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, -.35f, .65f, f, f1, f3, i, 1f);
        this.renderVertex(pBuffer, pQuaternion, pX, pY, pZ, -0.35f, -0.35f, f, f1, f4, i, 1f);

    }


}
