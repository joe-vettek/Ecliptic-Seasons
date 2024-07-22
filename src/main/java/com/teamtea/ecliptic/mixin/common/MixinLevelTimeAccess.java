package com.teamtea.ecliptic.mixin.common;



import com.teamtea.ecliptic.common.core.solar.SolarAngelHelper;
import net.minecraft.world.level.LevelTimeAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({LevelTimeAccess.class})
public interface MixinLevelTimeAccess extends LevelTimeAccess{

    @Shadow long dayTime();

    // @Inject(at = {@At("HEAD")}, method = {"getTimeOfDay"}, cancellable = true)
    // public default void mixin_getTimeOfDay(float p_46943_, CallbackInfoReturnable<Float> cir) {
    //     // cir.setReturnValue( AsmHandler.getSeasonCelestialAngle(p_63905_,(DimensionType)(Object)this));
    //
    // }

    @Override
    default float getTimeOfDay(float p_46943_) {
        // TeaStory.logger(p_46943_,dayTime());
        return SolarAngelHelper.getSeasonCelestialAngle(dayTime(),(LevelTimeAccess)(Object)this);
    }

    @Override
    default int getMoonPhase() {
        return this.dimensionType().moonPhase(this.dayTime());
    }
}
