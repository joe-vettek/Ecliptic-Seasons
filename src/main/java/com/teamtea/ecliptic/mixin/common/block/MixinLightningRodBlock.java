package com.teamtea.ecliptic.mixin.common.block;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningRodBlock.class)
public class MixinLightningRodBlock {

    @WrapOperation(
            method = "onProjectileHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isThundering()Z")
    )
    private boolean mixin$onProjectileHit_isThundering(Level instance, Operation<Boolean> original, @Local(ordinal = 0) BlockHitResult blockHitResult) {
        return WeatherManager.isThunderAtBiome((ServerLevel) instance, instance.getBiome(blockHitResult.getBlockPos()).get());
    }



}
