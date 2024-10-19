package com.teamtea.eclipticseasons.mixin.common;


import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.config.ServerConfig;
import com.teamtea.eclipticseasons.compat.vanilla.VanillaWeather;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Biome.class})
public abstract class MixinBiome {

    @Inject(at = {@At("HEAD")}, method = {"getPrecipitationAt"}, cancellable = true)
    public void ecliptic$getPrecipitationAt(BlockPos pos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (ServerConfig.Debug.useSolarWeather.get())
            cir.setReturnValue(WeatherManager.getPrecipitationAt((Biome) (Object) this, pos));
        else {
            cir.setReturnValue(VanillaWeather.handlePrecipitationat((Biome) (Object) this, pos));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"getBaseTemperature"}, cancellable = true)
    public void ecliptic$getBaseTemperature(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(BiomeClimateManager.agent$GetBaseTemperature((Biome) (Object) this));
    }


    @Inject(at = {@At("HEAD")}, method = {"hasPrecipitation"}, cancellable = true)
    public void ecliptic$hasPrecipitation(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(BiomeClimateManager.agent$hasPrecipitation((Biome) (Object) this));
    }

    //
    // @Inject(at = {@At("HEAD")}, method = {"shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"}, cancellable = true)
    // public void ecliptic$shouldFreeze(LevelReader p_47520_, BlockPos p_47521_, CallbackInfoReturnable<Boolean> cir) {
    // }

    // @Inject(at = {@At("HEAD")}, method = {"warmEnoughToRain"}, cancellable = true)
    // public void ecliptic$warmEnoughToRain(BlockPos p_198905_, CallbackInfoReturnable<Boolean> cir) {
    // }
    //
    // @Inject(at = {@At("HEAD")}, method = {"shouldSnow"}, cancellable = true)
    // public void ecliptic$shouldSnow(LevelReader p_47520_, BlockPos p_47521_, CallbackInfoReturnable<Boolean> cir) {
    // }
}
