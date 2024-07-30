package com.teamtea.ecliptic.mixin.common.entity.player;


import com.mojang.authlib.GameProfile;
import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.api.constant.solar.SolarTerm;
import com.teamtea.ecliptic.api.util.SimpleUtil;
import com.teamtea.ecliptic.common.AllListener;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerPlayer.class})
public abstract class MixinServerPlayer extends Player {


    public MixinServerPlayer(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(at = {@At("RETURN")}, method = {"tick"})
    private void mixin_init(CallbackInfo ci) {
        if (level().getRandom().nextInt(60) == 0)
            AllListener.getSaveDataLazy(level()).ifPresent(solarDataManager -> {
                if (solarDataManager.getSolarTerm().isInTerms(SolarTerm.BEGINNING_OF_SUMMER, SolarTerm.BEGINNING_OF_AUTUMN)) {
                    var b = this.level().getBiome(this.blockPosition()).value();
                    if (b.getTemperature(this.blockPosition()) > 0.5f) {
                        boolean isDaytime = SimpleUtil.isDay(level());
                        if (!this.isInWaterOrRain()
                                && ((isDaytime && (this.level().canSeeSky(this.blockPosition()))))
                        )
                            if (!this.hasEffect(Ecliptic.EffectRegistry.HEAT_STROKE)) {
                                this.addEffect(new MobEffectInstance(Ecliptic.EffectRegistry.HEAT_STROKE, 1200));
                            }
                    }
                }

            });

    }
}
