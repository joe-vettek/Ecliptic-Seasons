package com.teamtea.ecliptic.mixin.common.bee;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.BeePollinateGoal.class)
public class MixinBee_BeePollinateGoal {

    // @Shadow @Final private Bee this$0;

    @Shadow @Final private Bee this$0;

    @WrapOperation(
            method = "canBeeUse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")
    )
    private boolean canBeeUseCheckRain(Level instance, Operation<Boolean> original) {
        return WeatherManager.wantsToEnterHiveCheckRain(this$0);
    }


    @WrapOperation(
            method = "canBeeContinueToUse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")
    )
    private boolean canBeeContinueToUseCheckRain(Level instance, Operation<Boolean> original) {
        return WeatherManager.wantsToEnterHiveCheckRain(this$0);
    }

}
