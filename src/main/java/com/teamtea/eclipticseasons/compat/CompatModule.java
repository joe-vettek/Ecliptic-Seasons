package com.teamtea.eclipticseasons.compat;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.teamtea.eclipticseasons.compat.dynamictrees.DynamicTreeMod;
import net.minecraftforge.fml.ModList;

public class CompatModule {

    public static void register() {

        if (ModList.get().isLoaded(DynamicTrees.MOD_ID)) {
            DynamicTreeMod.init();
        }
    }
}
