package com.teamtea.ecliptic.common.core.biome;

import com.teamtea.ecliptic.api.solar.SolarTerm;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;

public class BiomeClimateManager {
    public final static HashMap<Biome, Float> BIOME_DEFAULT_TEMPERATURE_MAP = new HashMap<>();


    public static void init(RegistryAccess tagsUpdatedEvent) {
        BIOME_DEFAULT_TEMPERATURE_MAP.clear();
        var biomes = tagsUpdatedEvent.registry(Registries.BIOME);
        if (biomes.isPresent()) {
            biomes.get().forEach(biome ->
            {
                BIOME_DEFAULT_TEMPERATURE_MAP.put(biome, biome.getModifiedClimateSettings().temperature());
                var loc=biomes.get().getKey(biome);
                var id=biomes.get().getId(biome);
                biomes.get().getHolder(ResourceKey.create(Registries.BIOME, biomes.get().getKey(biome))).ifPresent(biomeHolder -> {

                    WeatherManager.BIOME_WEATHER_LIST.entrySet().stream().forEach(levelArrayListEntry ->
                    {
                        var biomeWeather=new WeatherManager.BiomeWeather(biomeHolder);
                        biomes.get().getId(biome);
                        biomeWeather.location=loc;
                        biomeWeather.id=id;
                        levelArrayListEntry.getValue().add(biomeWeather);
                    });
                });

            });
        }


    }

    public static float getDefaultTemperature(Biome biome) {
        return BiomeClimateManager.BIOME_DEFAULT_TEMPERATURE_MAP.getOrDefault(biome, 0.6F);
    }

    public static final float SNOW_LEVEL=0.15F;
    public static final float FROZEN_OCEAN_MELT_LEVEL=0.1F;

    public static void updateTemperature(Level level, int solarTermIndex) {
        var biomes = level.registryAccess().registry(Registries.BIOME);
        if (biomes.isPresent()) {
            biomes.get().forEach(biome ->
            {
                var temperature = BiomeClimateManager.getDefaultTemperature(biome)>SNOW_LEVEL?
                        Math.max(SNOW_LEVEL+0.001F,BiomeClimateManager.getDefaultTemperature(biome) + SolarTerm.get(solarTermIndex).getTemperatureChange()):
                        Math.min(SNOW_LEVEL,BiomeClimateManager.getDefaultTemperature(biome) + SolarTerm.get(solarTermIndex).getTemperatureChange());

                var oldClimateSettings = biome.climateSettings;
                biome.climateSettings = new Biome.ClimateSettings(
                        oldClimateSettings.hasPrecipitation(),
                        temperature,
                        oldClimateSettings.temperatureModifier(),
                        oldClimateSettings.downfall());
            });
        }
    }
}
