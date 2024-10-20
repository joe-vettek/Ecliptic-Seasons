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

    public static boolean cache = false;
    // private static Tag<Biome> create(String s) {
    //     return Tag.create(BuiltinRegistries.BIOME.key(), EclipticSeasons.rl(s));
    // }

    private static BiomeDictionary.Type create(String s) {
        return BiomeDictionary.Type.getType(s);
    }


    public static void init() {
        if (!cache) {
            BiomeDictionary.getBiomes(BiomeDictionary.Type.OVERWORLD)
                    .forEach(b -> BiomeDictionary.addTypes(b, SEASONAL));

            BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA)
                    .forEach(b -> BiomeDictionary.addTypes(b, SEASONAL));

            BiomeDictionary.getBiomes(BiomeDictionary.Type.END)
                    .forEach(b -> BiomeDictionary.addTypes(b, RAINLESS));
            BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER)
                    .forEach(b -> BiomeDictionary.addTypes(b, RAINLESS));
            BiomeDictionary.getBiomes(BiomeDictionary.Type.VOID)
                    .forEach(b -> BiomeDictionary.addTypes(b, RAINLESS));

            BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY)
                    .forEach(b -> BiomeDictionary.addTypes(b, ARID));
            BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA)
                    .forEach(b -> BiomeDictionary.addTypes(b, ARID));

            BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD)
                    .forEach(b -> {
                        if (BiomeDictionary.getTypes(b).contains(BiomeDictionary.Type.OVERWORLD))
                            BiomeDictionary.addTypes(b, DROUGHTY);
                    });
            BiomeDictionary.getBiomes(BiomeDictionary.Type.MOUNTAIN)
                    .forEach(b -> BiomeDictionary.addTypes(b, ARID));

            BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH)
                    .forEach(b -> BiomeDictionary.addTypes(b, SOFT));
            BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN)
                    .forEach(b -> BiomeDictionary.addTypes(b, SOFT));

            BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE)
                    .forEach(b -> BiomeDictionary.addTypes(b, RAINY));
            
            cache = true;
        }
    }
}
