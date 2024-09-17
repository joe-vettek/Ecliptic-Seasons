package com.teamtea.eclipticseasons.mixin.client;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.teamtea.eclipticseasons.client.core.ClientWeatherChecker;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.config.ServerConfig;
import com.teamtea.eclipticseasons.compat.vanilla.VanillaWeather;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRender {

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Final
    public Minecraft minecraft;


    // 我不确定目前是否还需要mixin，但是由于我们已经代理了Level的判断，因此这可能是不需要的
    // @WrapOperation(
    //         method = "renderSnowAndRain",
    //         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")
    // )
    // private float ecliptic$renderSnowAndRainCheckRain(ClientLevel clientLevel, float pDelta, Operation<Float> original) {
    //     // var anyRain = WeatherManager.getBiomeList(Minecraft.getInstance().level).stream().anyMatch(WeatherManager.BiomeWeather::shouldRain);
    //     // return WeatherManager.getMaximumRainLevel(clientLevel,pDelta);
    //     if (ServerConfig.Debug.useSolarWeather.get())
    //         return ClientWeatherChecker.getRainLevel(level, 1.0f);
    //     else return original.call(clientLevel, pDelta);
    // }


    // ModifyExpressionValue may cost much time than it
    @WrapOperation(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation ecliptic$renderSnowAndRain_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        if (ServerConfig.Debug.useSolarWeather.get())
            return WeatherManager.getPrecipitationAt(level, biome, pos);
        else return VanillaWeather.replacePrecipitationIfNeed(level, biome, original.call(biome, pos));
    }

    @WrapOperation(
            method = "tickRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation ecliptic$tickRain_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        if (ServerConfig.Debug.useSolarWeather.get())
            return WeatherManager.getPrecipitationAt(level, biome, pos);
        else return VanillaWeather.replacePrecipitationIfNeed(level, biome, original.call(biome, pos));
    }


    // @WrapOperation(
    //         method = "renderSnowAndRain",
    //         at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I")
    // )
    // private int ecliptic$getAdjustedLightColorForSnow(BlockAndTintGetter pLevel, BlockPos pos, Operation<Integer> original) {
    //     // if (ServerConfig.Debug.useSolarWeather.get()) {
    //     //     final int packedLight = LevelRenderer.getLightColor(pLevel, pos);
    //     //     // if (Config.INSTANCE.weatherRenderChanges.getAsBoolean())
    //     //     {
    //     //         // Adjusts the light color via a heuristic that mojang uses to make snow appear more white
    //     //         // This targets both paths, but since we always use the rain rendering, it's fine.
    //     //         final int lightU = packedLight & 0xffff;
    //     //         final int lightV = (packedLight >> 16) & 0xffff;
    //     //         final int brightLightU = (lightU * 3 + 240) / 4;
    //     //         final int brightLightV = (lightV * 3 + 240) / 4;
    //     //         return brightLightU | (brightLightV << 16);
    //     //     }
    //     //     // return packedLight;
    //     // } else
    //     return original.call(pLevel, pos);
    // }

    @Inject(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V")
    )
    private void ecliptic$renderSnowAndRain_ModifySnowAmount(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo ci, @Local(ordinal = 3) LocalIntRef integerLocalRef) {
        if (ServerConfig.Debug.useSolarWeather.get())
            integerLocalRef.set(ClientWeatherChecker.ModifySnowAmount(integerLocalRef.get(),pPartialTick));
    }

    @WrapOperation(
            method = "tickRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V")
    )
    private void ecliptic$tickRain_modifySound(ClientLevel instance, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource, float pVolume, float pPitch, boolean pDistanceDelay, Operation<Void> original) {
        if (ServerConfig.Debug.useSolarWeather.get()) {
            original.call(instance, blockPos, soundEvent, soundSource, ClientWeatherChecker.modifyVolume(soundEvent, pVolume), ClientWeatherChecker.modifyPitch(soundEvent, pPitch), pDistanceDelay);
        } else {
            original.call(instance, blockPos, soundEvent, soundSource, pVolume, pPitch, pDistanceDelay);
        }
    }

    @ModifyVariable(
            method = {"tickRain"},
            at = @At("STORE"),
            ordinal = 0
    )
    private int ecliptic$tickRain_modifyAmount(int originalNum) {
        if (ServerConfig.Debug.useSolarWeather.get()) {
            return  ClientWeatherChecker.modifyRainAmount(originalNum);
        } else return originalNum;
    }
}
