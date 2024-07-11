package com.teamtea.ecliptic.data;

import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.data.tag.TagsDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;


import java.util.concurrent.CompletableFuture;


public class start {
    public final static String MODID = Ecliptic.MODID;

    public static void dataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        if (event.includeServer()) {
            Ecliptic.logger("Generate We Data!!!");

            generator.addProvider(event.includeServer(),new TagsDataProvider(packOutput,lookupProvider,MODID,helper));

        }
        if (event.includeClient()) {

        }


    }
}
