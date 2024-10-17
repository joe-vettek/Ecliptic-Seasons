package com.teamtea.eclipticseasons.api.constant.game;


import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public enum BreedSeasonType {
    SPRING(new BreedSeasonInfo(1), EclipticSeasonsMod.rl("breed/spring")),
    SUMMER(new BreedSeasonInfo(2), EclipticSeasonsMod.rl("breed/summer")),
    AUTUMN(new BreedSeasonInfo(4), EclipticSeasonsMod.rl("breed/autumn")),
    WINTER(new BreedSeasonInfo(8), EclipticSeasonsMod.rl("breed/winter")),
    SP_SU(new BreedSeasonInfo(3), EclipticSeasonsMod.rl("breed/spring_summer")),
    SP_AU(new BreedSeasonInfo(5), EclipticSeasonsMod.rl("breed/spring_autumn")),
    SP_WI(new BreedSeasonInfo(9), EclipticSeasonsMod.rl("breed/spring_winter")),
    SU_AU(new BreedSeasonInfo(6), EclipticSeasonsMod.rl("breed/summer_autumn")),
    SU_WI(new BreedSeasonInfo(10), EclipticSeasonsMod.rl("breed/summer_winter")),
    AU_WI(new BreedSeasonInfo(12), EclipticSeasonsMod.rl("breed/autumn_winter")),
    SP_SU_AU(new BreedSeasonInfo(7), EclipticSeasonsMod.rl("breed/spring_summer_autumn")),
    SP_SU_WI(new BreedSeasonInfo(11), EclipticSeasonsMod.rl("breed/spring_summer_winter")),
    SP_AU_WI(new BreedSeasonInfo(13), EclipticSeasonsMod.rl("breed/spring_autumn_winter")),
    SU_AU_WI(new BreedSeasonInfo(14), EclipticSeasonsMod.rl("breed/summer_autumn_winter")),
    ALL(new BreedSeasonInfo(15), EclipticSeasonsMod.rl("breed/all_seasons"));

    private final BreedSeasonInfo info;
    private final ResourceLocation res;

    BreedSeasonType(BreedSeasonInfo info, ResourceLocation res) {
        this.info = info;
        this.res = res;
    }

    public BreedSeasonInfo getInfo() {
        return info;
    }

    public ResourceLocation getRes() {
        return res;
    }

    public TagKey<EntityType<?>> getTag() {
        return TagKey.create(Registries.ENTITY_TYPE, res);
    }
}
