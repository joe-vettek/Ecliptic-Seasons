package com.teamtea.eclipticseasons.mixin.game;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.eclipticseasons.api.util.WeatherUtil;
import com.teamtea.eclipticseasons.config.ServerConfig;
import com.teamtea.eclipticseasons.game.AnimalHooks;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Bee.BeePollinateGoal.class)
public class MixinBee_BeePollinateGoal {

    // @Shadow @Final private Bee this$0;

    @Shadow
    @Final
    private Bee this$0;


    @Inject(at = {@At("RETURN")}, method = {"canBeeUse","canBeeContinueToUse"}, cancellable = true)
    public void ecliptic$canBeeUse(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if(AnimalHooks.cancelBeePollinate(this$0)){
                cir.setReturnValue(false);
            }
        }
    }


}
