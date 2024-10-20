package com.teamtea.eclipticseasons.mixin.common;



import com.teamtea.eclipticseasons.common.core.solar.SolarAngelHelper;
import net.minecraft.world.IDayTimeReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({IDayTimeReader.class})
public interface MixinLevelTimeAccess extends IDayTimeReader {

    // @Shadow(remap = false) long dayTime();

    // @Inject(at = {@At("HEAD")}, method = {"getTimeOfDay"}, cancellable = true)
    // public default void mixin_getTimeOfDay(float p_46943_, CallbackInfoReturnable<Float> cir) {
    //     // cir.setReturnValue( AsmHandler.getSeasonCelestialAngle(p_63905_,(DimensionType)(Object)this));
    //
    // }

    @Override
    default float getTimeOfDay(float p_46943_) {
        // TeaStory.logger(p_46943_,dayTime());
        return SolarAngelHelper.getSeasonCelestialAngle((IDayTimeReader)(Object)this, dayTime());
    }

    // @Override
    // default int getMoonPhase() {
    //     return this.dimensionType().moonPhase(this.dayTime());
    // }
}
