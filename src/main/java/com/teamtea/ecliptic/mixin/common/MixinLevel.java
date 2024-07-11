package com.teamtea.ecliptic.mixin.common;


import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.teamtea.ecliptic.client.core.ClientWeatherChecker;

@Mixin(Level.class)
public class MixinLevel {


    @Inject(at = {@At("HEAD")}, method = {"isRaining"}, cancellable = true)
    private void mixin_isRaining(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.isRaining(serverLevel));
        } else if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(ClientWeatherChecker.isRain(clientLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"getRainLevel"}, cancellable = true)
    private void mixin_getRainLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.getRainLevel(p_46723_, serverLevel));
        } else if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(ClientWeatherChecker.getRainLevel(p_46723_, clientLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"isRainingAt"}, cancellable = true)
    private void mixin_isRainingAt(BlockPos p_46759_, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            cir.setReturnValue(WeatherManager.isRainingAt(p_46759_, serverLevel));
        }else if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(ClientWeatherChecker.isRainingAt(p_46759_, clientLevel));
        }
    }
}
