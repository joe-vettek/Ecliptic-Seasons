package com.teamtea.eclipticseasons.api.constant.crop;


import com.teamtea.eclipticseasons.api.constant.biome.Humidity;
import com.teamtea.eclipticseasons.EclipticSeasons;
import net.minecraft.util.ResourceLocation;

public enum CropHumidityType
{
    ARID(new CropHumidityInfo(Humidity.ARID), EclipticSeasons.rl("crops/arid_arid")),
    ARID_DRY(new CropHumidityInfo(Humidity.ARID, Humidity.DRY), EclipticSeasons.rl("crops/arid_dry")),
    ARID_AVERAGE(new CropHumidityInfo(Humidity.ARID, Humidity.AVERAGE), EclipticSeasons.rl("crops/arid_average")),
    ARID_MOIST(new CropHumidityInfo(Humidity.ARID, Humidity.MOIST), EclipticSeasons.rl("crops/arid_moist")),
    ARID_HUMID(new CropHumidityInfo(Humidity.ARID, Humidity.HUMID), EclipticSeasons.rl("crops/arid_humid")),
    DRY(new CropHumidityInfo(Humidity.DRY), EclipticSeasons.rl("crops/dry_dry")),
    DRY_AVERAGE(new CropHumidityInfo(Humidity.DRY, Humidity.AVERAGE), EclipticSeasons.rl("crops/dry_average")),
    DRY_MOIST(new CropHumidityInfo(Humidity.DRY, Humidity.MOIST), EclipticSeasons.rl("crops/dry_moist")),
    DRY_HUMID(new CropHumidityInfo(Humidity.DRY, Humidity.HUMID), EclipticSeasons.rl("crops/dry_humid")),
    AVERAGE(new CropHumidityInfo(Humidity.AVERAGE), EclipticSeasons.rl("crops/average_average")),
    AVERAGE_MOIST(new CropHumidityInfo(Humidity.AVERAGE, Humidity.MOIST), EclipticSeasons.rl("crops/average_moist")),
    AVERAGE_HUMID(new CropHumidityInfo(Humidity.AVERAGE, Humidity.HUMID), EclipticSeasons.rl("crops/average_humid")),
    MOIST(new CropHumidityInfo(Humidity.MOIST), EclipticSeasons.rl("crops/moist_moist")),
    MOIST_HUMID(new CropHumidityInfo(Humidity.MOIST, Humidity.HUMID), EclipticSeasons.rl("crops/moist_humid")),
    HUMID(new CropHumidityInfo(Humidity.HUMID), EclipticSeasons.rl("crops/humid_humid"));

    private final CropHumidityInfo info;
    private final ResourceLocation res;

    CropHumidityType(CropHumidityInfo info, ResourceLocation res)
    {
        this.info = info;
        this.res = res;
    }

    public CropHumidityInfo getInfo()
    {
        return info;
    }

    public ResourceLocation getRes()
    {
        return res;
    }
}
