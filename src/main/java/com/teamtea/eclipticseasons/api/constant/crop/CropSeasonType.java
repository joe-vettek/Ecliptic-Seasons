package com.teamtea.eclipticseasons.api.constant.crop;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public enum CropSeasonType {
    SPRING(new CropSeasonInfo(1), EclipticSeasonsMod.rl("crops/spring")),
    SUMMER(new CropSeasonInfo(2), EclipticSeasonsMod.rl("crops/summer")),
    AUTUMN(new CropSeasonInfo(4), EclipticSeasonsMod.rl("crops/autumn")),
    WINTER(new CropSeasonInfo(8), EclipticSeasonsMod.rl("crops/winter")),
    SP_SU(new CropSeasonInfo(3), EclipticSeasonsMod.rl("crops/spring_summer")),
    SP_AU(new CropSeasonInfo(5), EclipticSeasonsMod.rl("crops/spring_autumn")),
    SP_WI(new CropSeasonInfo(9), EclipticSeasonsMod.rl("crops/spring_winter")),
    SU_AU(new CropSeasonInfo(6), EclipticSeasonsMod.rl("crops/summer_autumn")),
    SU_WI(new CropSeasonInfo(10), EclipticSeasonsMod.rl("crops/summer_winter")),
    AU_WI(new CropSeasonInfo(12), EclipticSeasonsMod.rl("crops/autumn_winter")),
    SP_SU_AU(new CropSeasonInfo(7), EclipticSeasonsMod.rl("crops/spring_summer_autumn")),
    SP_SU_WI(new CropSeasonInfo(11), EclipticSeasonsMod.rl("crops/spring_summer_winter")),
    SP_AU_WI(new CropSeasonInfo(13), EclipticSeasonsMod.rl("crops/spring_autumn_winter")),
    SU_AU_WI(new CropSeasonInfo(14), EclipticSeasonsMod.rl("crops/summer_autumn_winter")),
    ALL(new CropSeasonInfo(15), EclipticSeasonsMod.rl("crops/all_seasons"));

    private final CropSeasonInfo info;
    private final ResourceLocation res;

    CropSeasonType(CropSeasonInfo info, ResourceLocation res) {
        this.info = info;
        this.res = res;
    }

    public CropSeasonInfo getInfo() {
        return info;
    }

    public ResourceLocation getRes() {
        return res;
    }

    public TagKey<Item> getTag() {
        return TagKey.create(Registries.ITEM, res);
    }

    public TagKey<Block> getBlockTag() {
        return TagKey.create(Registries.BLOCK, res);
    }
}
