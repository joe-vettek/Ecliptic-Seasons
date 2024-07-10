package com.teamtea.ecliptic.mixin.common;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.teamtea.ecliptic.common.core.WeatherHandler;

@Mixin({Biome.class})
public abstract class MixinBiome {
    @Shadow
    @Deprecated
    public abstract float getTemperature(BlockPos p_47506_);

    // 阻止非寒冷群系结冰
    @Inject(at = {@At("HEAD")}, method = {"shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"}, cancellable = true)
    public void mixin_shouldFreeze(LevelReader p_47520_, BlockPos p_47521_, CallbackInfoReturnable<Boolean> cir) {
        if (p_47520_ instanceof ServerLevel level) {
            // 目前设置为不生成雪，根据香草判断一下了
            if ((this.getTemperature(p_47521_) >= 0.15F))
                cir.setReturnValue(false);
        }
    }


    @Inject(at = {@At("HEAD")}, method = {"warmEnoughToRain"}, cancellable = true)
    public void mixin_warmEnoughToRain(BlockPos p_198905_, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(WeatherHandler.onCheckWarm(p_198905_));
    }

    @Inject(at = {@At("HEAD")}, method = {"shouldSnow"}, cancellable = true)
    public void mixin_shouldSnow(LevelReader p_47520_, BlockPos p_47521_, CallbackInfoReturnable<Boolean> cir) {
        if (p_47520_ instanceof ServerLevel level) {
            // cir.setReturnValue(WeatherHandler.onShouldSnow(level,((Biome) (Object) this),p_47521_));
            // cir.setReturnValue(true);
            // 目前设置为不生成雪，根据香草判断一下了
            if ((this.getTemperature(p_47521_) >= 0.15F))
                cir.setReturnValue(false);
        }
    }
}
