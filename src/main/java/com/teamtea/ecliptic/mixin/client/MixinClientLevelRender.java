package com.teamtea.ecliptic.mixin.client;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.teamtea.ecliptic.client.core.ClientWeatherChecker;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class MixinClientLevelRender {


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


    @ModifyExpressionValue(
            method = "renderSnowAndRain",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F")
    )
    private float wantsToEnterHiveCheckRain(float original) {
        var anyRain = WeatherManager.getBiomeList(Minecraft.getInstance().level).stream().anyMatch(WeatherManager.BiomeWeather::shouldRain);
        return anyRain?1.0f:0.0f;
    }

}
