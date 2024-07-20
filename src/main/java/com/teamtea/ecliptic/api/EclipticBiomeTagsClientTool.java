package com.teamtea.ecliptic.api;

import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;

public class EclipticBiomeTagsClientTool {


    public static TagKey<Biome> getTag(Biome biome) {
        Level level = WeatherManager.getMainServerLevel();
        level = level != null ? level : Minecraft.getInstance().level;
        if (level != null) {
            var biomes = level.registryAccess().registry(Registries.BIOME);
            if (biomes.isPresent()) {
                for (Map.Entry<ResourceKey<Biome>, Biome> resourceKeyBiomeEntry : biomes.get().entrySet()) {
                    if (resourceKeyBiomeEntry.getValue() == biome) {
                        var holder = biomes.get().getHolder(resourceKeyBiomeEntry.getKey());
                        if (holder.isPresent()) {
                            var tag = holder.get().tags().filter(EclipticBiomeTags.BIOMES::contains).findFirst();
                            if (tag.isPresent()) {
                                return tag.get();
                            }
                        }
                    }
                }
            }
        }
        return EclipticBiomeTags.RAINLESS;

    }
}
