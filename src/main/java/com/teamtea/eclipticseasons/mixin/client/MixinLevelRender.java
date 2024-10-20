package com.teamtea.eclipticseasons.mixin.client;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(WorldRenderer.class)
public abstract class MixinLevelRender {


    @Shadow
    @Nullable
    private ClientWorld level;


    @WrapOperation(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getTemperature(Lnet/minecraft/util/math/BlockPos;)F")
    )
    private float ecliptic$renderSnowAndRain_getPrecipitationAt(Biome instance, BlockPos blockPos, Operation<Float> original) {
        return WeatherManager.getPrecipitationAt(level,instance,BlockPos.ZERO)== Biome.RainType.SNOW?
                0f:1f;
    }

    @WrapOperation(
            method = "tickRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation()Lnet/minecraft/world/biome/Biome$RainType;")
    )
    private Biome.RainType ecliptic$tickRain_getPrecipitationAt(Biome instance, Operation<Biome.RainType> original) {
        return WeatherManager.getPrecipitationAt(level,instance,BlockPos.ZERO);
    }

    //
    // @Redirect(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I"))
    // private int ecliptic$getAdjustedLightColorForSnow(BlockAndTintGetter level, BlockPos pos)
    // {
    //     final int packedLight = LevelRenderer.getLightColor(level, pos);
    //     // if (Config.INSTANCE.weatherRenderChanges.getAsBoolean())
    //     {
    //         // Adjusts the light color via a heuristic that mojang uses to make snow appear more white
    //         // This targets both paths, but since we always use the rain rendering, it's fine.
    //         final int lightU = packedLight & 0xffff;
    //         final int lightV = (packedLight >> 16) & 0xffff;
    //         final int brightLightU = (lightU * 3 + 240) / 4;
    //         final int brightLightV = (lightV * 3 + 240) / 4;
    //         return brightLightU | (brightLightV << 16);
    //     }
    //     // return packedLight;
    // }


    // @ModifyConstant(method = "renderSnowAndRain", constant = {@Constant(intValue = 5), @Constant(intValue = 10)})
    // private int ecliptic$ModifySnowAmount(int constant)
    // {
    //     // This constant is used to control how much snow is rendered - 5 with default, 10 with fancy graphics. By default, we bump this all the way to 15.
    //     return ClientWeatherChecker.ModifySnowAmount(constant);
    // }
}
