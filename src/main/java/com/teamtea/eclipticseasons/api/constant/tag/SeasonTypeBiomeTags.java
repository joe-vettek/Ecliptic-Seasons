package com.teamtea.eclipticseasons.api.constant.tag;

import com.teamtea.eclipticseasons.EclipticSeasons;
import net.minecraft.tags.Tag;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeasonTypeBiomeTags {

    public static final BiomeDictionary.Type SEASONAL = create("seasonal");
    public static final BiomeDictionary.Type MONSOONAL = create("monsoonal");
    public static final BiomeDictionary.Type RAINLESS = create("rainless");
    public static final BiomeDictionary.Type ARID = create("arid");
    public static final BiomeDictionary.Type DROUGHTY = create("droughty");
    public static final BiomeDictionary.Type SOFT = create("soft");
    public static final BiomeDictionary.Type RAINY = create("rainy");
    public static final List<BiomeDictionary.Type> BIOMES = Stream.of(SEASONAL, MONSOONAL, RAINLESS, ARID, DROUGHTY, SOFT, RAINY).collect(Collectors.toList());

    // private static Tag<Biome> create(String s) {
    //     return Tag.create(BuiltinRegistries.BIOME.key(), EclipticSeasons.rl(s));
    // }

    private static BiomeDictionary.Type create(String s) {
        return BiomeDictionary.Type.getType(s);
    }
}
