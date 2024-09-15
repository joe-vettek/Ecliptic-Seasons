package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.client;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.teamtea.eclipticseasons.client.core.ClientWeatherChecker;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.compat.teacon.TeaconCheckTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = LevelRenderer.class,priority = 2000)
public abstract class MixinTeaconLevelRender {


    @Shadow
    @Nullable
    public ClientLevel level;


    @Shadow
    @Final
    public Minecraft minecraft;


    @WrapOperation(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")
    )
    private float teacon$renderSnowAndRainCheckRain(ClientLevel clientLevel, float pDelta, Operation<Float> original) {
        if (Minecraft.getInstance().player != null) {
            if (TeaconCheckTool.isValidPos(level, minecraft.player.blockPosition()))
                return ClientWeatherChecker.getRainLevel(level, 1.0f);
        }
        return original.call(clientLevel, pDelta);
    }


    // ModifyExpressionValue may cost much time than it
    @WrapOperation(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation teacon$renderSnowAndRain_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        if (Minecraft.getInstance().player != null) {
            if (TeaconCheckTool.isValidPos(level, minecraft.player.blockPosition()))
                return WeatherManager.getPrecipitationAt(level, biome, pos);
        }
        return original.call(biome, pos);
    }

    @WrapOperation(
            method = "tickRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation teacon$tickRain_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        if (TeaconCheckTool.isValidPos(level, pos))
            return WeatherManager.getPrecipitationAt(level, biome, pos);
        else return original.call(biome, pos);
    }


    @WrapOperation(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I")
    )
    private int teacon$getAdjustedLightColorForSnow(BlockAndTintGetter pLevel, BlockPos pos, Operation<Integer> original) {
        if (TeaconCheckTool.isValidPos(level, pos)) {
            final int packedLight = LevelRenderer.getLightColor(pLevel, pos);
            // if (Config.INSTANCE.weatherRenderChanges.getAsBoolean())
            {
                // Adjusts the light color via a heuristic that mojang uses to make snow appear more white
                // This targets both paths, but since we always use the rain rendering, it's fine.
                final int lightU = packedLight & 0xffff;
                final int lightV = (packedLight >> 16) & 0xffff;
                final int brightLightU = (lightU * 3 + 240) / 4;
                final int brightLightV = (lightV * 3 + 240) / 4;
                return brightLightU | (brightLightV << 16);
            }
            // return packedLight;
        } else return original.call(pLevel, pos);
    }


    @Inject(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V")
    )
    private void teacon$renderSnowAndRain_ModifySnowAmount(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo ci, @Local(ordinal = 3) LocalIntRef integerLocalRef) {
        if (Minecraft.getInstance().player != null)
            if (TeaconCheckTool.isValidPos(level, minecraft.player.blockPosition()))
                integerLocalRef.set(ClientWeatherChecker.ModifySnowAmount(integerLocalRef.get()));
    }
}
