package com.teamtea.eclipticseasons.api.util;

import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;


public class EclipticTagClientTool extends EclipticTagTool {

    public static BiomeDictionary.Type getTag(Biome biome) {
        World level = WeatherManager.getMainServerWorld();
        level = level != null ? level : Minecraft.getInstance().level;
        return getTag(level, biome);

    }
}
