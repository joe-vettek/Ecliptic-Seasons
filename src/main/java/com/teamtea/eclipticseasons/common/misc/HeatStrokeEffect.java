package com.teamtea.eclipticseasons.common.misc;


import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class HeatStrokeEffect extends Effect {


    public HeatStrokeEffect(EffectType neutral, int i) {
        super(neutral, i);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // return duration % 60 == 0;
        return false;
    }


    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        // if (entityLivingBaseIn.getHealth() > 0.1F)
        {
            // need a damage tag is bypasses_armor
            // entityLivingBaseIn.hurt(entityLivingBaseIn.damageSources().generic(), Math.min(entityLivingBaseIn.getHealth() * 0.025f,0.1f) + 0.001f);
        }
    }




}
