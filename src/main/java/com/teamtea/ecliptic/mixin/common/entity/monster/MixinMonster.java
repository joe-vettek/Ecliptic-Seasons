package com.teamtea.ecliptic.mixin.common.entity.monster;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Monster.class)
public class MixinMonster {


    @WrapOperation(
            method = "isDarkEnoughToSpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")
    )
    private static boolean mixin$isDarkEnoughToSpawn_isThundering(ServerLevel instance, Operation<Boolean> original, @Local(ordinal = 0) BlockPos blockPos) {
        return WeatherManager.isThunderAt(instance,blockPos);
    }
}
