package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.inner;


import com.teamtea.eclipticseasons.common.core.solar.SolarDataManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SolarDataManager.class)
public class MixinTeaconSolarDataManager {

    // @Shadow
    // protected int solarTermsDay;
    //
    // @Inject(at = {@At("HEAD")}, method = {"setSolarTermsDay"}, cancellable = true)
    // private void teacon$getSnowDepthAtBiome(int day, CallbackInfo ci) {
    //     if(TeaconCheckTool.isOnTeaconServer()) {
    //         this.solarTermsDay = TeaconSolarTermCalculator.getNowTerm().ordinal() * 7 + 0;
    //         ci.cancel();
    //     }
    // }

}
