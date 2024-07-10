package xueluoanping.ecliptic.mixin.client;


import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
public class MixinClientLevelData {
    @Inject(at = {@At("HEAD")}, method = {"isRaining"}, cancellable = true)
    private void mixin_getRenderLayers(CallbackInfoReturnable<Boolean> cir) {
        // if ((Object) this instanceof ClientLevel.ClientLevelData clientLevel)
        // {
        //     cir.setReturnValue(WeatherChecker.isRain(Minecraft.getInstance().level));
        // }
    }
}
