package xueluoanping.ecliptic.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.handler.WeatherHandler;

@Mixin({Biome.class})
public abstract class MixinBiome {

    @Inject(at = {@At("HEAD")}, method = {"warmEnoughToRain"}, cancellable = true)
    public void mixin_warmEnoughToRain(BlockPos p_198905_, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(WeatherHandler.onCheckWarm(p_198905_));
    }

    @Inject(at = {@At("HEAD")}, method = {"shouldSnow"}, cancellable = true)
    public void mixin_shouldSnow(LevelReader p_47520_, BlockPos p_47521_, CallbackInfoReturnable<Boolean> cir) {
        if (p_47520_ instanceof ServerLevel level) {
            // cir.setReturnValue(WeatherHandler.onShouldSnow(level,((Biome) (Object) this),p_47521_));
            // cir.setReturnValue(true);
        }
    }
}
