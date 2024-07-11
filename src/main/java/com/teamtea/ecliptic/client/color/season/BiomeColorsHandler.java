package com.teamtea.ecliptic.client.color.season;

import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.client.core.ColorHelper;
import com.teamtea.ecliptic.common.AllListener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;

public class BiomeColorsHandler {
    public static int[] newFoliageBuffer = new int[65536];
    public static int[] newGrassBuffer = new int[65536];
    public static boolean needRefresh = false;

    public static final ColorResolver GRASS_COLOR = (biome, posX, posZ) ->
    {
        var clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            int originColor = biome.getGrassColor(posX, posZ);
            return AllListener.getSaveDataLazy(clientLevel).map(data ->
            {
                if (needRefresh) {
                    reloadColors();
                }
                double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature(), 0.0F, 1.0F);
                double humidity = Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
                humidity = humidity * temperature;
                int i = (int) ((1.0D - temperature) * 255.0D);
                int j = (int) ((1.0D - humidity) * 255.0D);
                int k = j << 8 | i;

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
                double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature(), 0.0F, 1.0F);
                double humidity = Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
                humidity = humidity * temperature;
                int i = (int) ((1.0D - temperature) * 255.0D);
                int j = (int) ((1.0D - humidity) * 255.0D);
                int k = j << 8 | i;
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
                    int[] foliageBuffer = FoliageColor.pixels;
                    int[] grassBuffer = GrassColor.pixels;

                    for (int i = 0; i < foliageBuffer.length; i++) {
                        int originColor = foliageBuffer[i];
                        SolarTerm solar = SolarTerm.get(data.getSolarTermIndex());
                        if (solar.getColorInfo().getTemperateMix() == 0.0F) {
                            newFoliageBuffer[i] = originColor;
                        } else {
                            newFoliageBuffer[i] = ColorHelper.simplyMixColor(solar.getColorInfo().getBirchColor(), solar.getColorInfo().getTemperateMix(), originColor, 1.0F - solar.getColorInfo().getTemperateMix());
                        }
                    }

                    for (int i = 0; i < grassBuffer.length; i++) {
                        int originColor = grassBuffer[i];
                        SolarTerm solar = SolarTerm.get(data.getSolarTermIndex());
                        if (solar.getColorInfo().getTemperateMix() == 0.0F) {
                            newGrassBuffer[i] = originColor;
                        } else {
                            newGrassBuffer[i] = ColorHelper.simplyMixColor(solar.getColorInfo().getTemperateColor(), solar.getColorInfo().getTemperateMix(), originColor, 1.0F - solar.getColorInfo().getTemperateMix());
                        }
                    }

                    needRefresh = false;
                });
            }
        }
    }
}
