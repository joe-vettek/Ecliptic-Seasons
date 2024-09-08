package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.common;


import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.misc.teacon.TeaconCheckTool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class MixinTeaconLevel {

    @Inject(at = {@At("HEAD")}, method = {"isRainingAt"}, cancellable = true)
    private void teacon$isRainingAt(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerLevel serverLevel) {
            if (TeaconCheckTool.isValidPos((Level) (Object) this, blockPos))
                cir.setReturnValue(WeatherManager.isRainingAt(serverLevel, blockPos));
        }
    }

}
