package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.inner;


import com.teamtea.eclipticseasons.misc.teacon.TeaconCheckTool;
import com.teamtea.eclipticseasons.misc.vanilla.VanillaWeather;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VanillaWeather.class)
public class MixinTeaconVanillaWeather {

    @Inject(at = {@At("HEAD")}, method = {"canRunSpecialWeather"}, cancellable = true)
    private static void teacon$getSnowDepthAtBiome(CallbackInfoReturnable<Boolean> cir) {
        if (TeaconCheckTool.isOnTeaconServer())
            cir.setReturnValue(true);
    }

}
