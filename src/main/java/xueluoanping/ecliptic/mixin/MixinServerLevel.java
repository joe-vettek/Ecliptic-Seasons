package xueluoanping.ecliptic.mixin;


import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerLevel.class})
public abstract class MixinServerLevel {
    @Inject(at = {@At("HEAD")}, method = {"resetWeatherCycle"}, cancellable = true)
    public void mixin_getTimeOfDay(CallbackInfo ci) {
        ci.cancel();

    }
}
