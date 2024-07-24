package com.teamtea.ecliptic.api;

import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;

public class EclipticBiomeTagsClientTool extends EclipticBiomeTags {

    public static TagKey<Biome> getTag(Biome biome) {
        Level level = WeatherManager.getMainServerLevel();
        level = level != null ? level : Minecraft.getInstance().level;
        return getTag(level, biome);

    }
}
