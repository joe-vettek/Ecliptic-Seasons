package com.teamtea.eclipticseasons.client.particle;


import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.spawner.WorldEntitySpawner;


import java.util.List;
import java.util.stream.Stream;

public class FireflyParticle extends SpriteTexturedParticle {


    private final IAnimatedSprite spriteSet;
    private boolean isBlink;
    private Vector3d nextPos;

    public FireflyParticle(ClientWorld level, double x, double y, double z, IAnimatedSprite spriteSet) {
        super(level, x, y, z);
        this.lifetime = 800;
        this.gravity = 1E-4f;
        this.spriteSet = spriteSet;

        this.isBlink = false;
        setSpriteFromAge(this.spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void render(IVertexBuilder vertexConsumer, ActiveRenderInfo camera, float patialTicks) {
        Vector3d vec3 = camera.getPosition();
        float f = (float) (MathHelper.lerp(patialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float) (MathHelper.lerp(patialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float) (MathHelper.lerp(patialTicks, this.zo, this.z) - vec3.z());
        Quaternion quaternionf;
        if (this.roll == 0.0F) {
            quaternionf = camera.rotation();
        } else {
            quaternionf = new Quaternion(camera.rotation());
            quaternionf.mul(Vector3f.ZP.rotation(MathHelper.lerp(patialTicks, this.oRoll, this.roll)));
        }

        Vector3f[] avector3f = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(patialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternionf);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(patialTicks);
        j = 15728880;
        if (this.age >= this.lifetime * 0.8) {
            j = this.getLightColor(patialTicks);
        }
        ;
        int combinedLightIn = 15728880;
        int light1 = combinedLightIn & '\uffff';
        int light2 = combinedLightIn >> 16 & '\uffff';

        vertexConsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexConsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

        if (isBlink) {
            f6 = spriteSet.get(1, 1).getU0();
            f7 = spriteSet.get(1, 1).getU1();
            f4 = spriteSet.get(1, 1).getV0();
            f5 = spriteSet.get(1, 1).getV1();
            vertexConsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, 0.5f).uv2(j).endVertex();
            vertexConsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, 0.5f).uv2(j).endVertex();
            vertexConsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, 0.5f).uv2(j).endVertex();
            vertexConsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, 0.5f).uv2(j).endVertex();
        }


    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime || onGround) {
            this.remove();
        } else {
            // Ecliptic.logger(spriteSet.get(this.age, this.lifetime));
            isBlink = this.age % 8 > 4 && this.age < this.lifetime * 0.8;
            // setSprite(spriteSet.get(isBlink ? 0 : 1, 1));
            Vector3d nowPos = new Vector3d(x, y, z);
            BlockPos targetPosition = new BlockPos(x + xd, y + yd, z + zd);

            Vector3d vec3 = Entity.collideBoundingBoxHeuristically((Entity) null, new Vector3d(xd, yd, zd), this.getBoundingBox(), this.level, ISelectionContext.empty(), new ReuseableStream<>(Stream.empty()));
            if (this.nextPos != null &&
                    (!WorldEntitySpawner.isValidEmptySpawnBlock(level, targetPosition, level.getBlockState(targetPosition), level.getFluidState(targetPosition), EntityType.BAT)
                            || targetPosition.getY() <= 0
                            || Math.abs(vec3.y) < (double) 1.0E-5F
                            || this.onGround
                            || level.getNearestPlayer(x + xd, y + yd, z + zd, 1f, false) != null
                    )) {
                this.nextPos = null;
                // this.stoppedByCollision=false;
            }
            if (nextPos == null || nextPos.closerThan(nowPos, 1f) || nextPos.distanceTo(nowPos) > 100) {
                this.nextPos = Vector3d.atCenterOf(findNextPosition());
                Vector3d re = nextPos.subtract(nowPos).multiply(0.02d, 0.02d, 0.02d);
                this.xd = re.x;
                this.yd = re.y;
                this.zd = re.z;
            } else {
                Vector3d re = nextPos.subtract(nowPos).multiply(0.02d, 0.02d, 0.02d);
                this.xd = 0.78 * this.xd + 0.3 * Math.abs(random.nextGaussian()) * re.x;
                this.yd = 0.8 * this.yd + 0.25 * Math.abs(random.nextGaussian()) * re.y;
                this.zd = 0.78 * this.zd + 0.3 * Math.abs(random.nextGaussian()) * re.z;
            }

            BlockPos pos = new BlockPos(x, y - 0.1f, z);
            if (!WorldEntitySpawner.isValidEmptySpawnBlock(level, pos, level.getBlockState(pos), level.getFluidState(pos), EntityType.BAT)) {
                this.yd = 0.05f;
            }

            this.move(xd, yd, zd);

        }
    }

    protected BlockPos findNextPosition() {

        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(x, y, z);

        do {
            // int b = random.nextGaussian()*7;
            double i = x + random.nextGaussian() * 4;
            double j = y + random.nextGaussian() * 2;
            double k = z + random.nextGaussian() * 4;
            blockpos$mutableblockpos.set(i, j, k);
            // Ecliptic.logger(blockpos$mutableblockpos);
        }
        while (!WorldEntitySpawner.isValidEmptySpawnBlock(level, blockpos$mutableblockpos, level.getBlockState(blockpos$mutableblockpos), level.getFluidState(blockpos$mutableblockpos), EntityType.BAT));


        return blockpos$mutableblockpos;
    }
}
