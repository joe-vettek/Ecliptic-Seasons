package com.teamtea.ecliptic.common.misc;


import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class HeatStrokeEffect extends MobEffect {


    public HeatStrokeEffect(MobEffectCategory neutral, int i) {
        super(neutral, i);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 60 == 0;
    }


    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        boolean isDaytime = entityLivingBaseIn.level().getDayTime() < 14000L;

        BlockPos pos = entityLivingBaseIn.getOnPos();
        if (!entityLivingBaseIn.isInWaterOrRain()
                && ((isDaytime && (entityLivingBaseIn.level().canSeeSky(pos.above()))))
        ) {
            entityLivingBaseIn.hurt(entityLivingBaseIn.damageSources().inFire(),entityLivingBaseIn.getHealth()*0.05f+0.001f);

        }
    }


}
