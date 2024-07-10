package com.teamtea.ecliptic.api.solar;

import com.teamtea.ecliptic.client.core.ColorHelper;
import net.minecraft.world.level.FoliageColor;


public enum SolarTermColors
{
    // Spring Solar Terms
    BEGINNING_OF_SPRING(0xc1a173, 0.32F, 0, 0, 0xc1a173, 0.32F),
    RAIN_WATER(0xc1a173, 0.16F, 0, 0, 0xc1a173, 0.16F),
    INSECTS_AWAKENING(0, 0),
    SPRING_EQUINOX(0, 0),
    FRESH_GREEN(0x7fb80e, 0.16F),
    GRAIN_RAIN(0x7fb80e, 0.32F),

    // Summer Solar Terms
    BEGINNING_OF_SUMMER(0x7fb80e, 0.48F),
    LESSER_FULLNESS(0x7fb80e, 0.64F, 0x7fb80e, 0.1F),
    GRAIN_IN_EAR(0x7fb80e, 0.8F, 0x7fb80e, 0.2F),
    SUMMER_SOLSTICE(0x7fb80e, 0.8F, 0x7fb80e, 0.3F),
    LESSER_HEAT(ColorHelper.simplyMixColor(0x7fb80e, 0.8F, 0xd1923f, 0.2F), 0.8F, 0x7fb80e, 0.4F),
    GREATER_HEAT(ColorHelper.simplyMixColor(0x7fb80e, 0.6F, 0xd1923f, 0.4F), 0.8F, 0x7fb80e, 0.5F, 0xffd400, 0.25F),

    // Autumn Solar Terms
    BEGINNING_OF_AUTUMN(ColorHelper.simplyMixColor(0x7fb80e, 0.4F, 0xd1923f, 0.6F), 0.8F, 0x7fb80e, 0.5F, 0xffd400, 0.5F),
    END_OF_HEAT(ColorHelper.simplyMixColor(0x7fb80e, 0.2F, 0xd1923f, 0.8F), 0.8F, 0x7fb80e, 0.5F, 0xffd400, 0.75F),
    WHITE_DEW(0xd1923f, 0.8F, 0x7fb80e, 0.5F, 0xffd400, 1.0F),
    AUTUMNAL_EQUINOX(0xd1923f, 0.8F, 0x7fb80e, 0.4F, 0xffd400, 1.0F),
    COLD_DEW(ColorHelper.simplyMixColor(0xd1923f, 0.8F, 0xc1a173, 0.2F), 0.8F, 0x7fb80e, 0.3F, 0xffd400, 1.0F),
    FIRST_FROST(ColorHelper.simplyMixColor(0xd1923f, 0.6F, 0xc1a173, 0.4F), 0.8F, 0x7fb80e, 0.2F, ColorHelper.simplyMixColor(0xffd400, 0.8F, 0xc1a173, 0.2F), 1.0F),

    // Winter Solar Terms
    BEGINNING_OF_WINTER(ColorHelper.simplyMixColor(0xd1923f, 0.4F, 0xc1a173, 0.6F), 0.8F, 0x7fb80e, 0.1F, ColorHelper.simplyMixColor(0xffd400, 0.6F, 0xc1a173, 0.4F), 1.0F),
    LIGHT_SNOW(ColorHelper.simplyMixColor(0xd1923f, 0.2F, 0xc1a173, 0.8F), 0.8F, 0, 0, ColorHelper.simplyMixColor(0xffd400, 0.4F, 0xc1a173, 0.6F), 1.0F),
    HEAVY_SNOW(0xc1a173, 0.8F, 0, 0, ColorHelper.simplyMixColor(0xffd400, 0.2F, 0xc1a173, 0.8F), 1.0F),
    WINTER_SOLSTICE(0xc1a173, 0.8F, 0, 0, 0xc1a173, 0.8F),
    LESSER_COLD(0xc1a173, 0.64F, 0, 0, 0xc1a173, 0.64F),
    GREATER_COLD(0xc1a173, 0.48F, 0, 0, 0xc1a173, 0.48F);

    private final int temperateColor;
    private final float temperateMix;
    private final int rainyColor;
    private final float rainyMix;
    private final int birchColor;

    SolarTermColors(int temperateColorIn, float temperateMixIn, int rainyColorIn, float rainyMixIn, int birchColorIn, float birchAlphaIn)
    {
        this.temperateColor = temperateColorIn;
        this.temperateMix = temperateMixIn;
        this.rainyColor = rainyColorIn;
        this.rainyMix = rainyMixIn;
        this.birchColor = ColorHelper.simplyMixColor(birchColorIn, birchAlphaIn, FoliageColor.getBirchColor(), 1.0F - birchAlphaIn);
    }

    SolarTermColors(int temperateColorIn, float temperateMixIn, int rainyColorIn, float rainyMixIn)
    {
        this.temperateColor = temperateColorIn;
        this.temperateMix = temperateMixIn;
        this.rainyColor = rainyColorIn;
        this.rainyMix = rainyMixIn;
        this.birchColor = FoliageColor.getBirchColor();
    }

    SolarTermColors(int temperateColorIn, float temperateMix)
    {
        this.temperateColor = temperateColorIn;
        this.temperateMix = temperateMix;
        this.rainyColor = 0;
        this.rainyMix = 0;
        this.birchColor = FoliageColor.getBirchColor();
    }

    public int getTemperateColor()
    {
        return temperateColor;
    }

    public float getTemperateMix()
    {
        return temperateMix;
    }

    public int getRainyColor()
    {
        return rainyColor;
    }

    public float getRainyMix()
    {
        return rainyMix;
    }

    public static SolarTermColors get(int index)
    {
        return values()[index];
    }

    public int getBirchColor()
    {
        return birchColor;
    }
}
