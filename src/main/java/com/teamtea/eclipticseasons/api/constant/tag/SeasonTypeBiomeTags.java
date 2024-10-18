package com.teamtea.eclipticseasons.api.constant.tag;

import com.teamtea.eclipticseasons.EclipticSeasons;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class SeasonTypeBiomeTags {
    public static final TagKey<Biome> SEASONAL = create("seasonal");
    public static final TagKey<Biome> MONSOONAL = create("monsoonal");
    public static final TagKey<Biome> RAINLESS = create("rainless");
    public static final TagKey<Biome> ARID = create("arid");
    public static final TagKey<Biome> DROUGHTY = create("droughty");
    public static final TagKey<Biome> SOFT = create("soft");
    public static final TagKey<Biome> RAINY = create("rainy");
    public static final List<TagKey<Biome>> BIOMES = List.of(SEASONAL, MONSOONAL, RAINLESS, ARID, DROUGHTY, SOFT, RAINY);

    private static TagKey<Biome> create(String s) {
        return TagKey.create(BuiltinRegistries.BIOME.key(), EclipticSeasons.rl(s));
    }
}
