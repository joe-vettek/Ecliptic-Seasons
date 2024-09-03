package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.client;


import com.teamtea.eclipticseasons.client.core.ClientWeatherChecker;
import com.teamtea.eclipticseasons.misc.teacon.CheckTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class MixinTeaconClientLevel {


    @Inject(at = {@At("HEAD")}, method = {"isRaining"}, cancellable = true)
    private void teacon$isRaining(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            if (Minecraft.getInstance().player != null)
                if (CheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition()))
                    cir.setReturnValue(ClientWeatherChecker.isRain(clientLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"getRainLevel"}, cancellable = true)
    private void teacon$getRainLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            if (Minecraft.getInstance().player != null)
                if (CheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition()))
                    // 这里会发生突变，但是没办法，过渡没法写
                    cir.setReturnValue(ClientWeatherChecker.getRainLevel(clientLevel, p_46723_));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"isRainingAt"}, cancellable = true)
    private void teacon$isRainingAt(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            if (CheckTool.isValidPos(clientLevel, blockPos))
                cir.setReturnValue(ClientWeatherChecker.isRainingAt(clientLevel, blockPos));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"isThundering"}, cancellable = true)
    private void teacon$isThundering(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            if (Minecraft.getInstance().player != null)
                if (CheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition()))
                    cir.setReturnValue(ClientWeatherChecker.isThundering(clientLevel));
        }
    }


    @Inject(at = {@At("HEAD")}, method = {"getThunderLevel"}, cancellable = true)
    private void teacon$getThunderLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            if (Minecraft.getInstance().player != null)
                if (CheckTool.isValidPos(clientLevel, Minecraft.getInstance().player.blockPosition()))
                    cir.setReturnValue(ClientWeatherChecker.getThunderLevel(clientLevel, p_46723_));
        }
    }
}
