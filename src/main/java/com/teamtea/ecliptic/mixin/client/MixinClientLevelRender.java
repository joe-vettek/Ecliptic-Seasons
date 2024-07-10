package com.teamtea.ecliptic.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.teamtea.ecliptic.client.core.WeatherChecker;

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

    @Inject(at = {@At("HEAD")}, method = {"renderSnowAndRain"}, cancellable = true)
    private void mixin_renderSnowAndRain(LightTexture p_109704_, float p_109705_, double p_109706_, double p_109707_, double p_109708_, CallbackInfo ci) {
        if (minecraft==null||minecraft.level==null)ci.cancel();
        if (level.effects().renderSnowAndRain(level, ticks, p_109705_, p_109704_, p_109706_, p_109707_, p_109708_))
            return;
        WeatherChecker.renderSnowAndRain((LevelRenderer)(Object)this,ticks,rainSizeX,rainSizeZ,RAIN_LOCATION,SNOW_LOCATION,p_109704_,  p_109705_,  p_109706_,  p_109707_,  p_109708_);

        ci.cancel();
    }

}
