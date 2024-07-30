package com.teamtea.ecliptic.api.util;

import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class EclipticTagClientTool extends EclipticTagTool {

    public static TagKey<Biome> getTag(Biome biome) {
        Level level = WeatherManager.getMainServerLevel();
        level = level != null ? level : Minecraft.getInstance().level;
        return getTag(level, biome);

    }
}
