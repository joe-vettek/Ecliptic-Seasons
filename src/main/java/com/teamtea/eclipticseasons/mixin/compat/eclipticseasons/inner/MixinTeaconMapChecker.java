package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.inner;


import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.misc.teacon.CheckTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapChecker.class)
public class MixinTeaconMapChecker {

    @Inject(at = {@At("HEAD")}, method = {"getSurfaceBiome"}, cancellable = true)
    private static void teacon$getSnowDepthAtBiome(Level level, BlockPos pos, CallbackInfoReturnable<Holder<Biome>> cir) {
        if (CheckTool.isValidPos(level, pos)) {
            level.registryAccess().holder(Biomes.PLAINS).ifPresent(cir::setReturnValue);
        } else if (CheckTool.isValidLevel(level)) {
            level.registryAccess().holder(Biomes.THE_VOID).ifPresent(cir::setReturnValue);
        }
    }


}
