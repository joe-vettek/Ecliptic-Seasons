package com.teamtea.ecliptic.mixin.client;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamtea.ecliptic.client.core.ClientWeatherChecker;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRender {


    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    private int ticks;

    @Shadow
    @Final
    public Minecraft minecraft;

    @Shadow
    @Final
    private float[] rainSizeX;

    @Shadow
    @Final
    private float[] rainSizeZ;

    @Shadow
    @Final
    private static ResourceLocation RAIN_LOCATION;

    @Shadow
    public static int getLightColor(BlockAndTintGetter p_109542_, BlockPos p_109543_) {
        return 0;
    }

    @Shadow
    @Final
    private static ResourceLocation SNOW_LOCATION;

    // 我们注释这个写法，因为它会让光影模组无法正常渲染
    // @Inject(at = {@At("HEAD")}, method = {"renderSnowAndRain"}, cancellable = true)
    // private void mixin_renderSnowAndRain(LightTexture p_109704_, float p_109705_, double p_109706_, double p_109707_, double p_109708_, CallbackInfo ci) {
    //     if (minecraft == null || minecraft.level == null) ci.cancel();
    //     if (level.effects().renderSnowAndRain(level, ticks, p_109705_, p_109704_, p_109706_, p_109707_, p_109708_))
    //         return;
    //     boolean re = ClientWeatherChecker.renderSnowAndRain((LevelRenderer) (Object) this, ticks, rainSizeX, rainSizeZ, RAIN_LOCATION, SNOW_LOCATION, p_109704_, p_109705_, p_109706_, p_109707_, p_109708_);
    //     if (re)
    //         ci.cancel();
    // }


    @WrapOperation(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")
    )
    private float mixin$renderSnowAndRainCheckRain(ClientLevel clientLevel,float p_109705_,Operation<Float> original) {
        // var anyRain = WeatherManager.getBiomeList(Minecraft.getInstance().level).stream().anyMatch(WeatherManager.BiomeWeather::shouldRain);
        // return WeatherManager.getMaximumRainLevel(clientLevel,p_109705_);
        return ClientWeatherChecker.getRainLevel(1.0f,level);
    }


    // ModifyExpressionValue may cost much time than it
    @WrapOperation(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation mixin$renderSnowAndRain_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        return WeatherManager.getPrecipitationAt(level,biome,pos);
    }

    @WrapOperation(
            method = "tickRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;")
    )
    private Biome.Precipitation mixin$tickRain_getPrecipitationAt(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        return WeatherManager.getPrecipitationAt(level,biome,pos);
    }
}
