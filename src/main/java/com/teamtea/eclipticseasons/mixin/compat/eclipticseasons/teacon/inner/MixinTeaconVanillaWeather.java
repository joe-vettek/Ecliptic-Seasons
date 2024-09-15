package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.inner;


import com.teamtea.eclipticseasons.compat.vanilla.VanillaWeather;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VanillaWeather.class)
public class MixinTeaconVanillaWeather {

    // @Inject(at = {@At("HEAD")}, method = {"canRunSpecialWeather"}, cancellable = true)
    // private static void teacon$getSnowDepthAtBiome(CallbackInfoReturnable<Boolean> cir) {
    //     if (TeaconCheckTool.isOnTeaconServer())
    //         cir.setReturnValue(true);
    // }

}
