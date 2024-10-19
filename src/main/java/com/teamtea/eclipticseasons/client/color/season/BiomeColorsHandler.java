package com.teamtea.eclipticseasons.client.color.season;

import com.teamtea.eclipticseasons.api.util.EclipticTagClientTool;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.constant.solar.color.SolarTermColor;
import com.teamtea.eclipticseasons.api.constant.tag.SeasonTypeBiomeTags;
import com.teamtea.eclipticseasons.api.util.SimpleUtil;
import com.teamtea.eclipticseasons.client.core.ColorHelper;
import com.teamtea.eclipticseasons.common.AllListener;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class BiomeColorsHandler {
    // public static int[] newFoliageBuffer = new int[65536];
    // public static int[] newGrassBuffer = new int[65536];
    public static Map<TagKey<Biome>, int[]> newFoliageBufferMap = new HashMap<>();
    public static Map<TagKey<Biome>, int[]> newGrassBufferMap = new HashMap<>();

    public static boolean needRefresh = false;

    public static final ColorResolver GRASS_COLOR = (biome, posX, posZ) ->
    {
        // if(true)return 9680335;
        var clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            int originColor = biome.getGrassColor(posX, posZ);
            return AllListener.getSaveDataLazy(clientLevel).map(data ->
            {
                if (needRefresh) {
                    reloadColors();
                }
                // 由于基本温度被更改
                double temperature = Mth.clamp(biome.getBaseTemperature(), 0.0F, 1.0F);
                double humidity = Mth.clamp(biome.getDownfall(), 0.0F, 1.0F);
                humidity = humidity * temperature;
                int i = (int) ((1.0D - temperature) * 255.0D);
                int j = (int) ((1.0D - humidity) * 255.0D);
                int k = j << 8 | i;

                int[] newGrassBuffer = newGrassBufferMap.getOrDefault(EclipticTagClientTool.getTag(biome), GrassColor.pixels);
                return k > newGrassBuffer.length ? -65281 : newGrassBuffer[k];
            }).orElse(originColor);
        } else return -1;
    };

    public static final ColorResolver FOLIAGE_COLOR = (biome, posX, posZ) ->
    {
        var clientLevel = Minecraft.getInstance().level;

        if (clientLevel != null) {
            int originColor = biome.getFoliageColor();
            return AllListener.getSaveDataLazy(clientLevel).map(data ->
            {
                if (needRefresh) {
                    reloadColors();
                }
                double temperature = Mth.clamp(biome.getBaseTemperature(), 0.0F, 1.0F);
                double humidity = Mth.clamp(biome.getDownfall(), 0.0F, 1.0F);
                humidity = humidity * temperature;
                int i = (int) ((1.0D - temperature) * 255.0D);
                int j = (int) ((1.0D - humidity) * 255.0D);
                int k = j << 8 | i;
                
                int[] newFoliageBuffer = newFoliageBufferMap.getOrDefault(EclipticTagClientTool.getTag(biome), FoliageColor.pixels);
                return k > newFoliageBuffer.length ? originColor : newFoliageBuffer[k];
            }).orElse(originColor);
        } else return biome.getFoliageColor();
    };

    public static void reloadColors() {
        {
            var clientLevel = Minecraft.getInstance().level;
            if (clientLevel != null) {
                AllListener.getSaveDataLazy(clientLevel).ifPresent(data ->
                {
                    for (TagKey<Biome> biomeTagKey : SeasonTypeBiomeTags.BIOMES) {
                        int[] newFoliageBuffer = new int[65536];
                        int[] newGrassBuffer = new int[65536];
                        int[] foliageBuffer = FoliageColor.pixels;
                        int[] grassBuffer = GrassColor.pixels;

                        SolarTerm solar = SolarTerm.get(data.getSolarTermIndex());
                        SolarTermColor colorInfo = solar.getSolarTermColor(biomeTagKey);
                        for (int i = 0; i < foliageBuffer.length; i++) {
                            int originColor = foliageBuffer[i];

                            if (colorInfo.getMix() == 0.0F) {
                                newFoliageBuffer[i] = originColor;
                            } else {
                                newFoliageBuffer[i] = ColorHelper.simplyMixColor(colorInfo.getBirchColor(), colorInfo.getMix(), originColor, 1.0F - colorInfo.getMix());
                            }
                        }

                        for (int i = 0; i < grassBuffer.length; i++) {
                            int originColor = grassBuffer[i];
                            if (colorInfo.getMix() == 0.0F) {
                                newGrassBuffer[i] = originColor;
                            } else {
                                newGrassBuffer[i] = ColorHelper.simplyMixColor(colorInfo.getColor(), colorInfo.getMix(), originColor, 1.0F - colorInfo.getMix());
                            }
                        }
                        newFoliageBufferMap.put(biomeTagKey, newFoliageBuffer);
                        newGrassBufferMap.put(biomeTagKey, newGrassBuffer);
                    }

                    needRefresh = false;
                });
            }
        }
    }
}
