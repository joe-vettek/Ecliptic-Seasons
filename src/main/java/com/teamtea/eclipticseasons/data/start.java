package com.teamtea.eclipticseasons.data;

import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.data.tag.TagsDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;


public class start {
    public final static String MODID = EclipticSeasons.MODID;

    public static void dataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeServer()) {
            EclipticSeasons.logger("Generate We Data!!!");

            generator.addProvider(new TagsDataProvider(generator,MODID,helper));

        }
        if (event.includeClient()) {

        }


    }
}
