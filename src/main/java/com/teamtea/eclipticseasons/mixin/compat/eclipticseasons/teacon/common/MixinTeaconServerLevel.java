package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.common;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.compat.teacon.TeaconCheckTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(value = ServerLevel.class,priority = 2000)
public abstract class MixinTeaconServerLevel extends Level {

    protected MixinTeaconServerLevel(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @WrapOperation(
            method = "tickPrecipitation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z")
    )
    private boolean teacon$tickPrecipitation_isRaining(ServerLevel serverLevel, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) BlockPos pos) {
        if (TeaconCheckTool.isValidPos(serverLevel, pos))
            return WeatherManager.isRainingAt((ServerLevel) (Object) this, pos);
        else return original.call(serverLevel);
    }

    @WrapOperation(
            method = "tickPrecipitation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation teacon$tickPrecipitation_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        if (TeaconCheckTool.isValidPos(this, pos))
            return WeatherManager.getPrecipitationAt((ServerLevel) (Object) this, biome, pos);
        else return original.call(biome, pos);
    }

    @WrapOperation(
            method = "tickChunk",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z")
    )
    private boolean teacon$tickChunk_isRaining(ServerLevel serverLevel, Operation<Boolean> original, @Local(ordinal = 0) LevelChunk levelChunk) {
        if (TeaconCheckTool.isValidPos(this, levelChunk))
            return true;
        else return original.call(serverLevel);
    }

    @WrapOperation(
            method = "tickChunk",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isThundering()Z")
    )
    private boolean teacon$tickChunk_isThundering(ServerLevel serverLevel, Operation<Boolean> original, @Local(ordinal = 0) LevelChunk levelChunk) {
        if (TeaconCheckTool.isValidPos(this, levelChunk))
            return true;
        else return original.call(serverLevel);
    }

    @WrapOperation(
            method = "tickChunk",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isRainingAt(Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean teacon$tickChunk_isRainingAt(ServerLevel serverLevel, BlockPos pos, Operation<Boolean> original, @Local(ordinal = 0) LevelChunk levelChunk) {
        if (TeaconCheckTool.isValidPos(this, levelChunk))
            return WeatherManager.isThunderAt(serverLevel, pos) && WeatherManager.isRainingAt(serverLevel, pos);
        else return original.call(serverLevel, pos);
    }
}
