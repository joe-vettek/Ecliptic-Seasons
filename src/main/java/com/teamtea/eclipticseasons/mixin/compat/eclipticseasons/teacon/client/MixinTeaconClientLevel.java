package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.client;


import com.teamtea.eclipticseasons.client.core.ClientWeatherChecker;
import com.teamtea.eclipticseasons.compat.teacon.TeaconCheckTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Level.class,priority = 2000)
public class MixinTeaconClientLevel {

    // @Unique
    // public float teacon$rainLevel=0f;
    //
    // @Inject(at = {@At("HEAD")}, method = {"isRaining"}, cancellable = true)
    // private void teacon$isRaining(CallbackInfoReturnable<Boolean> cir) {
    //     if ((Object) this instanceof ClientLevel clientLevel) {
    //         if (Minecraft.getInstance().player != null)
    //             if (TeaconCheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition()))
    //                 cir.setReturnValue(ClientWeatherChecker.isRain(clientLevel));
    //     }
    // }
    //
    // @Inject(at = {@At("HEAD")}, method = {"getRainLevel"}, cancellable = true)
    // private void teacon$getRainLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
    //     if ((Object) this instanceof ClientLevel clientLevel) {
    //         if (Minecraft.getInstance().player != null)
    //             if (teacon$rainLevel>0
    //                     || TeaconCheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition())) {
    //                 teacon$rainLevel=ClientWeatherChecker.getRainLevel(clientLevel, p_46723_);
    //                 cir.setReturnValue(teacon$rainLevel);
    //             }
    //     }
    // }
    //
    // @Inject(at = {@At("HEAD")}, method = {"isRainingAt"}, cancellable = true)
    // private void teacon$isRainingAt(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
    //     if ((Object) this instanceof ClientLevel clientLevel) {
    //         if (TeaconCheckTool.isValidPos(clientLevel, blockPos))
    //             cir.setReturnValue(ClientWeatherChecker.isRainingAt(clientLevel, blockPos));
    //     }
    // }
    //
    // @Inject(at = {@At("HEAD")}, method = {"isThundering"}, cancellable = true)
    // private void teacon$isThundering(CallbackInfoReturnable<Boolean> cir) {
    //     if ((Object) this instanceof ClientLevel clientLevel) {
    //         if (Minecraft.getInstance().player != null)
    //             if (TeaconCheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition()))
    //                 cir.setReturnValue(ClientWeatherChecker.isThundering(clientLevel));
    //     }
    // }
    //
    //
    // @Inject(at = {@At("HEAD")}, method = {"getThunderLevel"}, cancellable = true)
    // private void teacon$getThunderLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
    //     if ((Object) this instanceof ClientLevel clientLevel) {
    //         if (Minecraft.getInstance().player != null)
    //             if (TeaconCheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition()))
    //                 cir.setReturnValue(ClientWeatherChecker.getThunderLevel(clientLevel, p_46723_));
    //     }
    // }
}
