package com.teamtea.ecliptic.mixin.common;


import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class MixinLevel {


    @Inject(at = {@At("HEAD")}, method = {"isRaining"}, cancellable = true)
    private void mixin_isRaining(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.isRainingAnywhere(serverLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"getRainLevel"}, cancellable = true)
    private void mixin_getRainLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.getMaximumRainLevel( serverLevel,p_46723_));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"isRainingAt"}, cancellable = true)
    private void mixin_isRainingAt(BlockPos p_46759_, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.isRainingAt(serverLevel, p_46759_));
        }
    }


    @Inject(at = {@At("HEAD")}, method = {"isThundering"}, cancellable = true)
    private void mixin_isThundering(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.isThunderAnywhere(serverLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"getThunderLevel"}, cancellable = true)
    private void mixin_getThunderLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.getMaximumThunderLevel( serverLevel,p_46723_));
        }
    }
}
