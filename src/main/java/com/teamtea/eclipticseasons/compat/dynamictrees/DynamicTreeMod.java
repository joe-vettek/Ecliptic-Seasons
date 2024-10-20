package com.teamtea.eclipticseasons.compat.dynamictrees;

import com.ferreusveritas.dynamictrees.compat.CompatHandler;
import com.ferreusveritas.dynamictrees.compat.seasons.ActiveSeasonGrowthCalculator;
import com.ferreusveritas.dynamictrees.compat.seasons.NormalSeasonManager;
import com.ferreusveritas.dynamictrees.compat.seasons.NullSeasonGrowthCalculator;
import com.ferreusveritas.dynamictrees.compat.seasons.NullSeasonProvider;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.api.util.EclipticTagTool;
import net.minecraft.util.Tuple;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.Tags;

import java.util.Set;

public class DynamicTreeMod {
    public static void init() {
        CompatHandler.registerSeasonManager(EclipticSeasons.MODID, () -> {
            NormalSeasonManager seasonManager = new NormalSeasonManager(
                    world -> world.dimensionType().natural() ?
                            new Tuple<>(new EclipticSeasonProvider(), new ActiveSeasonGrowthCalculator()) :
                            new Tuple<>(new NullSeasonProvider(), new NullSeasonGrowthCalculator())
            );
            seasonManager.setTropicalPredicate((world, pos) -> {
                Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(world.getBiome(pos)).get());
                return types.contains(BiomeDictionary.Type.HOT)
                        && types.contains(BiomeDictionary.Type.OVERWORLD);
            });
            return seasonManager;
        });
    }
}
