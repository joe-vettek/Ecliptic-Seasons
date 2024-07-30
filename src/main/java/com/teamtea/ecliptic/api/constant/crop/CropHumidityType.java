package com.teamtea.ecliptic.api.constant.crop;


import com.teamtea.ecliptic.api.constant.biome.Humidity;
import net.minecraft.resources.ResourceLocation;
import com.teamtea.ecliptic.Ecliptic;

public enum CropHumidityType
{
    ARID(new CropHumidityInfo(Humidity.ARID), Ecliptic.rl("crops/arid_arid")),
    ARID_DRY(new CropHumidityInfo(Humidity.ARID, Humidity.DRY), Ecliptic.rl("crops/arid_dry")),
    ARID_AVERAGE(new CropHumidityInfo(Humidity.ARID, Humidity.AVERAGE), Ecliptic.rl("crops/arid_average")),
    ARID_MOIST(new CropHumidityInfo(Humidity.ARID, Humidity.MOIST), Ecliptic.rl("crops/arid_moist")),
    ARID_HUMID(new CropHumidityInfo(Humidity.ARID, Humidity.HUMID), Ecliptic.rl("crops/arid_humid")),
    DRY(new CropHumidityInfo(Humidity.DRY), Ecliptic.rl("crops/dry_dry")),
    DRY_AVERAGE(new CropHumidityInfo(Humidity.DRY, Humidity.AVERAGE), Ecliptic.rl("crops/dry_average")),
    DRY_MOIST(new CropHumidityInfo(Humidity.DRY, Humidity.MOIST), Ecliptic.rl("crops/dry_moist")),
    DRY_HUMID(new CropHumidityInfo(Humidity.DRY, Humidity.HUMID), Ecliptic.rl("crops/dry_humid")),
    AVERAGE(new CropHumidityInfo(Humidity.AVERAGE), Ecliptic.rl("crops/average_average")),
    AVERAGE_MOIST(new CropHumidityInfo(Humidity.AVERAGE, Humidity.MOIST), Ecliptic.rl("crops/average_moist")),
    AVERAGE_HUMID(new CropHumidityInfo(Humidity.AVERAGE, Humidity.HUMID), Ecliptic.rl("crops/average_humid")),
    MOIST(new CropHumidityInfo(Humidity.MOIST), Ecliptic.rl("crops/moist_moist")),
    MOIST_HUMID(new CropHumidityInfo(Humidity.MOIST, Humidity.HUMID), Ecliptic.rl("crops/moist_humid")),
    HUMID(new CropHumidityInfo(Humidity.HUMID), Ecliptic.rl("crops/humid_humid"));

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
