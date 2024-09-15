package com.teamtea.eclipticseasons.client.color.season;

import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.constant.solar.color.SolarTermColor;
import com.teamtea.eclipticseasons.api.constant.tag.SeasonTypeBiomeTags;
import com.teamtea.eclipticseasons.client.core.ColorHelper;
import com.teamtea.eclipticseasons.common.core.SolarHolders;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BiomeColorsHandler {
    // public static int[] newFoliageBuffer = new int[65536];
    // public static int[] newGrassBuffer = new int[65536];
    public static Map<TagKey<Biome>, int[]> newFoliageBufferMap = new HashMap<>();
    public static Map<TagKey<Biome>, int[]> newGrassBufferMap = new HashMap<>();

    public static boolean needRefresh = false;

    public static final ColorResolver GRASS_COLOR = (biome, posX, posZ) ->
    {

        // if (SimpleUtil.getModsUse(0).getLast().contains("map")){
        //     return MapColor.SNOW.col;
        // }
        int originColor = biome.getGrassColor(posX, posZ);
        if (needRefresh) {
            reloadColors();
        }
        // 由于基本温度被更改
        // double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature() + EclipticUtil.getNowSolarTerm(clientLevel).getTemperatureChange(), 0.0F, 1.0F);

        double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature(), 0.0F, 1.0F);
        double humidity = Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
        humidity = humidity * temperature;
        int i = (int) ((1.0D - temperature) * 255.0D);
        int j = (int) ((1.0D - humidity) * 255.0D);
        int k = j << 8 | i;

        int[] newGrassBuffer = newGrassBufferMap.getOrDefault(BiomeClimateManager.getTag(biome), GrassColor.pixels);
        // 注意大概率会DH
        return k > newGrassBuffer.length ? -65281 : newGrassBuffer[k];
    };

    public static final ColorResolver FOLIAGE_COLOR = (biome, posX, posZ) ->
    {
        int originColor = biome.getFoliageColor();
        if (needRefresh) {
            reloadColors();
        }
        double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature(), 0.0F, 1.0F);
        double humidity = Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
        humidity = humidity * temperature;
        int i = (int) ((1.0D - temperature) * 255.0D);
        int j = (int) ((1.0D - humidity) * 255.0D);
        int k = j << 8 | i;

        int[] newFoliageBuffer = newFoliageBufferMap.getOrDefault(BiomeClimateManager.getTag(biome), FoliageColor.pixels);
        return k > newFoliageBuffer.length ? originColor : newFoliageBuffer[k];
    };

    public static void reloadColors() {
        {

            {
                for (TagKey<Biome> biomeTagKey : SeasonTypeBiomeTags.BIOMES) {
                    int[] newFoliageBuffer = new int[65536];
                    int[] newGrassBuffer = new int[65536];
                    int[] foliageBuffer = FoliageColor.pixels;
                    int[] grassBuffer = GrassColor.pixels;

                    SolarTerm solar = EclipticSeasonsApi.getInstance().getSolarTerm(Minecraft.getInstance().level);
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
            }

        }
    }

}
