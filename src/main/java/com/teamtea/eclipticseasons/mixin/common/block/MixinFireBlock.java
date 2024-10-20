package com.teamtea.eclipticseasons.mixin.common.block;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireBlock.class)
public class MixinFireBlock {

    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/server/ServerWorld;isRaining()Z", ordinal = 0)
    )
    private boolean mixin$Tick_isRaining(ServerWorld level, Operation<Boolean> original, @Local( argsOnly = true) BlockPos blockPos) {
        return WeatherManager.isRainingAt(level, blockPos);
    }

    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/server/ServerWorld;isRaining()Z", ordinal = 1)
    )
    private boolean mixin$Tick_isRaining2(ServerWorld level, Operation<Boolean> original, @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        return WeatherManager.isRainingAt(level, blockPos);
    }

}
