package com.teamtea.ecliptic.mixin.common.entity.pandas;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Panda.class)
public class MixinPanda {

    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")
    )
    private boolean mixin$tick(Level instance, Operation<Boolean> original) {
        return WeatherManager.isThunderAt((ServerLevel) ((Panda)(Object)this).level(),((Panda)(Object)this).blockPosition());
    }

    @WrapOperation(
            method = "isScared",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")
    )
    private boolean mixin$isScared(Level instance, Operation<Boolean> original) {
        return WeatherManager.isThunderAt((ServerLevel) ((Panda)(Object)this).level(),((Panda)(Object)this).blockPosition());
    }
}
