package cloud.lemonslice.teastory.environment.solar;

import net.minecraft.client.Minecraft;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public final class BiomeTemperatureManager {
    public final static HashMap<Biome, Float> BIOME_DEFAULT_TEMPERATURE_MAP = new HashMap<>();

    public static void init() {
        ForgeRegistries.BIOMES.forEach(biome ->
        {
            BIOME_DEFAULT_TEMPERATURE_MAP.put(biome, biome.getModifiedClimateSettings().temperature());

            ForgeRegistries.BIOMES.getHolder(biome).ifPresent(biomeHolder -> {
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

    public static float getDefaultTemperature(Biome biome) {
        return BiomeTemperatureManager.BIOME_DEFAULT_TEMPERATURE_MAP.getOrDefault(biome, 0.6F);
    }
}
