package com.teamtea.eclipticseasons.mixin.compat.eclipticseasons.inner;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.misc.teacon.CheckTool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherManager.class)
public class MixinTeaconWeatherManager {

    @Inject(at = {@At("HEAD")}, method = {"getSnowDepthAtBiome"}, cancellable = true)
    private static void teacon$getSnowDepthAtBiome(Level serverLevel, Biome biome, CallbackInfoReturnable<Integer> cir) {
        if (CheckTool.isValidLevel(serverLevel)) {
            if (EclipticSeasonsApi.getInstance().getSolarTerm(serverLevel).isInTerms(SolarTerm.BEGINNING_OF_WINTER, SolarTerm.GREATER_COLD)) {
                cir.setReturnValue(100);
            } else cir.setReturnValue(0);
        } else cir.setReturnValue(0);
    }

    @Inject(at = {@At("HEAD")}, method = {"getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/biome/Biome;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"}, cancellable = true)
    private static void teacon$getPrecipitationAt(Level levelNull, Biome biome, BlockPos pos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (CheckTool.isValidPos(levelNull, pos)) {
            if (EclipticSeasonsApi.getInstance().getSolarTerm(levelNull).isInTerms(SolarTerm.BEGINNING_OF_WINTER, SolarTerm.GREATER_COLD)) {
                cir.setReturnValue(Biome.Precipitation.SNOW);
            } else cir.setReturnValue(Biome.Precipitation.RAIN);
        } else cir.setReturnValue(Biome.Precipitation.NONE);
    }
}
