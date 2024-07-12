package com.teamtea.ecliptic.compat;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.compat.CompatHandler;
import com.ferreusveritas.dynamictrees.compat.season.*;
import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.compat.dynamictrees.EclipticSeasonProvider;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;

public class CompatModule {

    public static void register() {

        if (ModList.get().isLoaded(DynamicTrees.MOD_ID)) {
            CompatHandler.registerSeasonManager(Ecliptic.MODID, () -> {
                NormalSeasonManager seasonManager = new NormalSeasonManager(
                        world -> world.dimensionType().natural() ?
                                new Tuple<>(new EclipticSeasonProvider(), new ActiveSeasonGrowthCalculator()) :
                                new Tuple<>(new NullSeasonProvider(), new NullSeasonGrowthCalculator())
                );
                seasonManager.setTropicalPredicate((world, pos) -> world.getBiome(pos).is(Tags.Biomes.IS_HOT_OVERWORLD));
                return seasonManager;
            });
        }
    }
}
