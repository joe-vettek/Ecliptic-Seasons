package com.teamtea.eclipticseasons.api.constant.crop;


import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.api.constant.biome.Humidity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public enum CropHumidityType
{
    ARID(new CropHumidityInfo(Humidity.ARID), EclipticSeasonsMod.rl("crops/arid_arid")),
    ARID_DRY(new CropHumidityInfo(Humidity.ARID, Humidity.DRY), EclipticSeasonsMod.rl("crops/arid_dry")),
    ARID_AVERAGE(new CropHumidityInfo(Humidity.ARID, Humidity.AVERAGE), EclipticSeasonsMod.rl("crops/arid_average")),
    ARID_MOIST(new CropHumidityInfo(Humidity.ARID, Humidity.MOIST), EclipticSeasonsMod.rl("crops/arid_moist")),
    ARID_HUMID(new CropHumidityInfo(Humidity.ARID, Humidity.HUMID), EclipticSeasonsMod.rl("crops/arid_humid")),
    DRY(new CropHumidityInfo(Humidity.DRY), EclipticSeasonsMod.rl("crops/dry_dry")),
    DRY_AVERAGE(new CropHumidityInfo(Humidity.DRY, Humidity.AVERAGE), EclipticSeasonsMod.rl("crops/dry_average")),
    DRY_MOIST(new CropHumidityInfo(Humidity.DRY, Humidity.MOIST), EclipticSeasonsMod.rl("crops/dry_moist")),
    DRY_HUMID(new CropHumidityInfo(Humidity.DRY, Humidity.HUMID), EclipticSeasonsMod.rl("crops/dry_humid")),
    AVERAGE(new CropHumidityInfo(Humidity.AVERAGE), EclipticSeasonsMod.rl("crops/average_average")),
    AVERAGE_MOIST(new CropHumidityInfo(Humidity.AVERAGE, Humidity.MOIST), EclipticSeasonsMod.rl("crops/average_moist")),
    AVERAGE_HUMID(new CropHumidityInfo(Humidity.AVERAGE, Humidity.HUMID), EclipticSeasonsMod.rl("crops/average_humid")),
    MOIST(new CropHumidityInfo(Humidity.MOIST), EclipticSeasonsMod.rl("crops/moist_moist")),
    MOIST_HUMID(new CropHumidityInfo(Humidity.MOIST, Humidity.HUMID), EclipticSeasonsMod.rl("crops/moist_humid")),
    HUMID(new CropHumidityInfo(Humidity.HUMID), EclipticSeasonsMod.rl("crops/humid_humid"));

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

    public TagKey<Item> getTag() {
        return TagKey.create(Registries.ITEM, res);
    }

    public TagKey<Block> getBlockTag() {
        return TagKey.create(Registries.BLOCK, res);
    }
}
