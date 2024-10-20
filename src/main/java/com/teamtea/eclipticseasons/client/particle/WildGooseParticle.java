package com.teamtea.eclipticseasons.client.particle;

import net.minecraft.client.particle.AshParticle;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.RisingParticle;

import net.minecraft.client.world.ClientWorld;

public class WildGooseParticle extends AshParticle {
    private final IAnimatedSprite spriteSet;

    public WildGooseParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed,1,spriteSet);
        this.spriteSet = spriteSet;
        this.scale(2.8F);
        this.lifetime = 200;
        this.hasPhysics = true;
        this.gravity = 1E-4f;
        this.alpha = 1f;
        this.setSpriteFromAge(spriteSet);
        // this.setParticleSpeed(0, 0, 0);
    }


    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if (level.getNearestPlayer(x + xd, y + yd, z + zd, 16f, false) != null) {
            this.age = this.lifetime + 1;
        }
        super.tick();

        // this.setSpriteFromAge(this.spriteSet);
        // setSprite(sprites.get(this.age % 8, 1));
        try {
            setSprite(spriteSet.get(this.age % 27/3, 8));
        } catch (Exception e) {
            e.printStackTrace();
        }

        move(0.1f, 0.01f, 0.1f);
    }

}