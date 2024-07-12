package com.teamtea.ecliptic.mixin.common.bee;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.BeePollinateGoal.class)
public class MixinBee_BeePollinateGoal {

    // @Shadow @Final private Bee this$0;

    @Shadow @Final private Bee this$0;

    @ModifyExpressionValue(
            method = "canBeeUse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")
    )
    private boolean canBeeUseCheckRain(boolean original) {
        return WeatherManager.wantsToEnterHiveCheckRain(this$0);
    }


    @ModifyExpressionValue(
            method = "canBeeContinueToUse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")
    )
    private boolean canBeeContinueToUseCheckRain(boolean original) {
        return WeatherManager.wantsToEnterHiveCheckRain(this$0);
    }

}
