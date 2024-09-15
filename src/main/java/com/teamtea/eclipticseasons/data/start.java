package com.teamtea.eclipticseasons.data;

import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.data.advancement.Advancements;
import com.teamtea.eclipticseasons.data.advancement.FDAdvancementGenerator;
import com.teamtea.eclipticseasons.data.lang.Lang_EN;
import com.teamtea.eclipticseasons.data.lang.Lang_ZH;
import com.teamtea.eclipticseasons.data.loot.EclipticSeasonsLootTableProvider;
import com.teamtea.eclipticseasons.data.tag.TagsDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;


import java.util.concurrent.CompletableFuture;


public class start {
    public final static String MODID = EclipticSeasonsApi.MODID;

    public static void dataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        if (event.includeServer()) {
            EclipticSeasonsMod.logger("Generate We Data!!!");

            generator.addProvider(event.includeServer(),new TagsDataProvider(packOutput,lookupProvider,MODID,helper));
            generator.addProvider(event.includeServer(),new Advancements(packOutput,lookupProvider,helper));
            generator.addProvider(event.includeServer(),new EclipticSeasonsLootTableProvider(packOutput,lookupProvider));
        }
        if (event.includeClient()) {
            generator.addProvider(event.includeClient(),new Lang_EN(packOutput,helper));
            generator.addProvider(event.includeClient(),new Lang_ZH(packOutput,helper));
        }


    }
}
