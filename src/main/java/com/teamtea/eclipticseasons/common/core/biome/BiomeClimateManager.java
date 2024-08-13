package com.teamtea.eclipticseasons.common.core.biome;

import com.mojang.serialization.MapCodec;
import com.teamtea.eclipticseasons.api.util.EclipticTagTool;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.constant.tag.SeasonTypeBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

public class BiomeClimateManager {
    public final static Map<Biome, Float> BIOME_DEFAULT_TEMPERATURE_MAP = new IdentityHashMap<>();
    public final static Map<Biome, Float> CLIENT_BIOME_DEFAULT_TEMPERATURE_MAP = new IdentityHashMap<>();

    public static void resetBiomeTemps(RegistryAccess registryAccess, boolean isServer) {
        resetBiomeTempsMap(registryAccess, isServer ? BIOME_DEFAULT_TEMPERATURE_MAP : CLIENT_BIOME_DEFAULT_TEMPERATURE_MAP);
    }

    public static void resetBiomeTempsMap(RegistryAccess registryAccess, Map<Biome, Float> useMap) {
        useMap.clear();
        var biomes = registryAccess.registry(Registries.BIOME);
        biomes.ifPresent(biomeRegistry -> biomeRegistry.forEach(biome ->
        {
            useMap.put(biome, biome.getModifiedClimateSettings().temperature());
        }));
    }

    public static final float DEFAULT_TEMPERATURE = 0.598F;

    public static float getDefaultTemperature(Biome biome, boolean isServer) {
        return isServer ?
                BIOME_DEFAULT_TEMPERATURE_MAP.getOrDefault(biome, DEFAULT_TEMPERATURE) :
                CLIENT_BIOME_DEFAULT_TEMPERATURE_MAP.getOrDefault(biome, DEFAULT_TEMPERATURE);
    }

    public static final float SNOW_LEVEL = 0.15F;
    public static final float FROZEN_OCEAN_MELT_LEVEL = 0.1F;

    public static void updateTemperature(Level level, SolarTerm solarTermIndex) {
        boolean isServer = level instanceof ServerLevel;
        level.registryAccess().registry(Registries.BIOME).ifPresent(biomeRegistry -> biomeRegistry.forEach(biome ->
        {
            var temperature = BiomeClimateManager.getDefaultTemperature(biome, isServer) > SNOW_LEVEL ?
                    Math.max(SNOW_LEVEL + 0.001F, BiomeClimateManager.getDefaultTemperature(biome, isServer) + solarTermIndex.getTemperatureChange()) :
                    Math.min(SNOW_LEVEL, BiomeClimateManager.getDefaultTemperature(biome, isServer) + solarTermIndex.getTemperatureChange());

            // clean temperature change
            // var oldClimateSettings = biome.climateSettings;
            // biome.climateSettings = new Biome.ClimateSettings(
            //         oldClimateSettings.hasPrecipitation(),
            //         temperature,
            //         oldClimateSettings.temperatureModifier(),
            //         oldClimateSettings.downfall());
        }));
    }

    // it's hard to check the
    public static Float agent$GetBaseTemperature(Biome biome) {
        float f = getDefaultTemperature(biome, true);
        if (f == DEFAULT_TEMPERATURE) {
            float f2 = getDefaultTemperature(biome, false);
            f = f2 != f ? f2 : f;
        }
        return f;
    }

    public static Boolean agent$hasPrecipitation(Biome biome) {
        return !EclipticTagTool.getTag(biome).equals(SeasonTypeBiomeTags.RAINLESS);
    }


    public static Holder<Biome> getHolder(RegistryAccess registryAccess, Biome biome) {
        return registryAccess.registry(Registries.BIOME)
                .get()
                .holders()
                .filter(biomeReference -> biomeReference.value() == biome)
                .findFirst().get();
    }
}
