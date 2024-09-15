package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.teacon.inner;


import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapChecker.class)
public class MixinTeaconMapChecker {

    // @Inject(at = {@At("HEAD")}, method = {"getSurfaceBiome"}, cancellable = true)
    // private static void teacon$getSnowDepthAtBiome(Level level, BlockPos pos, CallbackInfoReturnable<Holder<Biome>> cir) {
    //     if (TeaconCheckTool.isValidPos(level, pos)) {
    //         level.registryAccess().holder(Biomes.PLAINS).ifPresent(cir::setReturnValue);
    //     } else if (TeaconCheckTool.isValidLevel(level)) {
    //         level.registryAccess().holder(Biomes.THE_VOID).ifPresent(cir::setReturnValue);
    //     }
    // }


}
