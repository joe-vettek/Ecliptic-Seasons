package com.teamtea.ecliptic.mixin.client;


import com.teamtea.ecliptic.client.core.ClientWeatherChecker;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class MixinClientLevel {


    @Inject(at = {@At("HEAD")}, method = {"isRaining"}, cancellable = true)
    private void mixin_isRaining(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(ClientWeatherChecker.isRain(clientLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"getRainLevel"}, cancellable = true)
    private void mixin_getRainLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(ClientWeatherChecker.getRainLevel(p_46723_, clientLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"isRainingAt"}, cancellable = true)
    private void mixin_isRainingAt(BlockPos p_46759_, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(ClientWeatherChecker.isRainingAt(p_46759_, clientLevel));
        }
    }
}
