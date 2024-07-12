package com.teamtea.ecliptic.mixin.common.bee;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.class)
public class MixinBee {

    @ModifyExpressionValue(
            method = "wantsToEnterHive",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isRaining()Z")
    )
    private boolean wantsToEnterHiveCheckRain(boolean original) {
        return WeatherManager.wantsToEnterHiveCheckRain((Bee)(Object)this);
    }

}
