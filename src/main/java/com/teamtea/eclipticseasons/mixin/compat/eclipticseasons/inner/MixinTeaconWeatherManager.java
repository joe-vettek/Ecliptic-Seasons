package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.inner;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.misc.teacon.TeaconCheckTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherManager.class)
public class MixinTeaconWeatherManager {

    @Inject(at = {@At("HEAD")}, method = {"getSnowDepthAtBiome"}, cancellable = true)
    private static void teacon$getSnowDepthAtBiome(Level serverLevel, Biome biome, CallbackInfoReturnable<Integer> cir) {
        if (TeaconCheckTool.isValidLevel(serverLevel)) {
            if (EclipticSeasonsApi.getInstance().getSolarTerm(serverLevel).isInTerms(SolarTerm.BEGINNING_OF_WINTER, SolarTerm.GREATER_COLD)) {
                cir.setReturnValue(100);
            } else cir.setReturnValue(0);
        } else cir.setReturnValue(0);
    }

    @Inject(at = {@At("HEAD")}, method = {"getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/biome/Biome;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"}, cancellable = true)
    private static void teacon$getPrecipitationAt(Level levelNull, Biome biome, BlockPos pos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (TeaconCheckTool.isValidPos(levelNull, pos)) {
            if (teacon$isRainingAt(levelNull, pos)) {
                if (EclipticSeasonsApi.getInstance().getSolarTerm(levelNull).isInTerms(SolarTerm.BEGINNING_OF_WINTER, SolarTerm.GREATER_COLD)) {
                    cir.setReturnValue(Biome.Precipitation.SNOW);
                } else cir.setReturnValue(Biome.Precipitation.RAIN);
            }
        } else cir.setReturnValue(Biome.Precipitation.NONE);
    }

    @Unique
    private static Boolean teacon$isRainingAt(Level serverLevel, BlockPos pos) {
        if (!serverLevel.dimensionType().natural()) {
            return false;
        }
        if (!serverLevel.canSeeSky(pos)) {
            return false;
        } else if (serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return false;
        }
        var biome = MapChecker.getSurfaceBiome(serverLevel, pos);
        return teacon$isRainingAtBiome(serverLevel, biome);
    }

    @Unique
    private static Boolean teacon$isRainingAtBiome(Level serverLevel, Holder<Biome> biome) {
        var ws = WeatherManager.getBiomeList(serverLevel);
        if (ws != null)
            for (WeatherManager.BiomeWeather biomeWeather : ws) {
                if (biome.value() == biomeWeather.biomeHolder.value()) {
                    return biomeWeather.shouldRain();
                }
            }
        return false;
    }

}
