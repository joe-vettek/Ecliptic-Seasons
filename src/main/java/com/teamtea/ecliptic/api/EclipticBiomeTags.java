package com.teamtea.ecliptic.api;

import com.teamtea.ecliptic.Ecliptic;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class EclipticBiomeTags {

    public static final TagKey<Biome> SEASONAL = create("seasonal");
    public static final TagKey<Biome> MONSOONAL = create("monsoonal");

    public static final TagKey<Biome> RAINLESS = create("rainless");
    public static final TagKey<Biome> ARID = create("arid");
    public static final TagKey<Biome> DROUGHTY = create("droughty");
    public static final TagKey<Biome> SOFT = create("soft");
    public static final TagKey<Biome> RAINY = create("rainy");

    private static TagKey<Biome> create(String s) {
        return TagKey.create(Registries.BIOME, Ecliptic.rl(s));
    }
}
