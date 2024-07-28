package com.teamtea.ecliptic.mixin.common.entity.animal.bee;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.class)
public class MixinBee {

    @WrapOperation(
            method = "wantsToEnterHive",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")
    )
    private boolean wantsToEnterHiveCheckRain(Level instance, Operation<Boolean> original) {
        return WeatherManager.wantsToEnterHiveCheckRain((Bee)(Object)this);
    }

}
