package cloud.lemonslice.teastory.client.color.season;

import cloud.lemonslice.silveroak.helper.ColorHelper;
import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;

public class BiomeColorsHandler
{
    public static int[] newFoliageBuffer = new int[65536];
    public static int[] newGrassBuffer = new int[65536];
    public static boolean needRefresh = false;

    public static final ColorResolver GRASS_COLOR = (biome, posX, posZ) ->
    {
        if (Minecraft.getInstance().level != null)
        {
            int originColor = biome.getGrassColor(posX, posZ);
            return Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(data ->
            {
                if (needRefresh)
                {
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
        }
        else return -1;
    };

    public static final ColorResolver FOLIAGE_COLOR = (biome, posX, posZ) ->
    {
        if (Minecraft.getInstance().level != null)
        {
            int originColor = biome.getFoliageColor();
            return Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(data ->
            {
                if (needRefresh)
                {
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
        }
        else return biome.getFoliageColor();
    };

    public static void reloadColors()
    {
        if (Minecraft.getInstance().level != null)
        {
            Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(data ->
            {
                int[] foliageBuffer = FoliageColor.pixels;
                int[] grassBuffer = GrassColor.pixels;

                for (int i = 0; i < foliageBuffer.length; i++)
                {
                    int originColor = foliageBuffer[i];
                    SolarTerm solar = SolarTerm.get(data.getSolarTermIndex());
                    if (solar.getColorInfo().getAlpha() == 0.0F)
                    {
                        newFoliageBuffer[i] = originColor;
                    }
                    else
                    {
                        newFoliageBuffer[i] = ColorHelper.simplyMixColor(solar.getColorInfo().getColor(), solar.getColorInfo().getAlpha(), originColor, 1.0F - solar.getColorInfo().getAlpha());
                    }
                }

                for (int i = 0; i < grassBuffer.length; i++)
                {
                    int originColor = grassBuffer[i];
                    SolarTerm solar = SolarTerm.get(data.getSolarTermIndex());
                    if (solar.getColorInfo().getAlpha() == 0.0F)
                    {
                        newGrassBuffer[i] = originColor;
                    }
                    else
                    {
                        newGrassBuffer[i] = ColorHelper.simplyMixColor(solar.getColorInfo().getColor(), solar.getColorInfo().getAlpha(), originColor, 1.0F - solar.getColorInfo().getAlpha());
                    }
                }

                needRefresh = false;
            });
        }
    }
}
