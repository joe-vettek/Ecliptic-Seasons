package com.teamtea.ecliptic.common.core;

import com.teamtea.ecliptic.api.solar.SolarTerm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.ecliptic.Ecliptic;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public  class BiomeTemperatureManager {
    public final static HashMap<Biome, Float> BIOME_DEFAULT_TEMPERATURE_MAP = new HashMap<>();

    @SubscribeEvent
    public static void init(TagsUpdatedEvent tagsUpdatedEvent) {
        BIOME_DEFAULT_TEMPERATURE_MAP.clear();
        var biomes=tagsUpdatedEvent.getRegistryAccess().registry(Registries.BIOME);
        if (biomes.isPresent()){
            biomes.get().forEach(biome ->
            {
                BIOME_DEFAULT_TEMPERATURE_MAP.put(biome, biome.getModifiedClimateSettings().temperature());

                biomes.get().getHolder( ResourceKey.create(Registries.BIOME, biomes.get().getKey(biome))).ifPresent(biomeHolder -> {
                    // if (biome.getCategory().equals(Biome.Category.SAVANNA))
                    if(biomeHolder.is(BiomeTags.IS_SAVANNA))
                    {
                        var oldClimateSettings = biome.climateSettings;
                        biome.climateSettings = new Biome.ClimateSettings(
                                oldClimateSettings.hasPrecipitation(),
                                oldClimateSettings.temperature(),
                                oldClimateSettings.temperatureModifier(),
                                0.2F);
                    }
                });

            });
        }

    }

    public static float getDefaultTemperature(Biome biome) {
        return BiomeTemperatureManager.BIOME_DEFAULT_TEMPERATURE_MAP.getOrDefault(biome, 0.6F);
    }

    public static void updateTemperature(Level level, int solarTermIndex) {
        var biomes=level.registryAccess().registry(Registries.BIOME);
        if (biomes.isPresent()){
            biomes.get().forEach(biome ->
            {
                var temperature = BiomeTemperatureManager.getDefaultTemperature(biome) + SolarTerm.get(solarTermIndex).getTemperatureChange();
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
