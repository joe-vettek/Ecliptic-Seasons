package com.teamtea.ecliptic.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamtea.ecliptic.Ecliptic;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FireflyParticle extends TextureSheetParticle {


    private final SpriteSet spriteSet;

    public FireflyParticle(ClientLevel level, double p277219, double p277220, double p277221, SpriteSet p277215) {
        super(level, p277219, p277220, p277221);
        this.lifetime = 800;
        this.gravity = 1E-4f;
        this.spriteSet = p277215;
        setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public void render(VertexConsumer vertexConsumer, Camera camera, float patialTicks) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) patialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp((double) patialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp((double) patialTicks, this.zo, this.z) - vec3.z());
        Quaternionf quaternionf;
        if (this.roll == 0.0F) {
            quaternionf = camera.rotation();
        } else {
            quaternionf = new Quaternionf(camera.rotation());
            quaternionf.rotateZ(Mth.lerp(patialTicks, this.oRoll, this.roll));
        }

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(patialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternionf);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(patialTicks);
        j = 15728880;
        int combinedLightIn = 15728880;
        int light1 = combinedLightIn & '\uffff';
        int light2 = combinedLightIn >> 16 & '\uffff';

        vertexConsumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            // Ecliptic.logger(spriteSet.get(this.age, this.lifetime));
            int ii = this.age % 8 > 4 ? 0 : 1;
            setSprite(spriteSet.get(ii, 1));

            // TODO：后期改进为寻找位置，当
            if (random.nextInt(4) == 0) {
                this.xd = (random.nextBoolean() ? 0.05f : -0.05f) * random.nextInt(5);
                this.yd = (random.nextBoolean() ? 0.015f : -0.015f) * random.nextInt(5);
                this.zd = (random.nextBoolean() ? 0.05f : -0.05f) * random.nextInt(5);
            }

            this.move(xd, yd, zd);

            if (this.onGround) {
                this.yd = 0.2f;
                this.move(xd,
                        yd,
                        zd);
            }

        }
    }
}
