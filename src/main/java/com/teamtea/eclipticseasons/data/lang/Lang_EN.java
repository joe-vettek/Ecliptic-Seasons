package com.teamtea.eclipticseasons.data.lang;

import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class Lang_EN extends LangHelper {
    public Lang_EN(PackOutput gen, ExistingFileHelper helper) {
        super(gen, helper, EclipticSeasonsApi.MODID, "en_us");
    }


    @Override
    protected void addTranslations() {
        add(EclipticSeasonsMod.ModContents.calendar.get(), "calendar");
        add(EclipticSeasonsMod.ModContents.wind_chimes.get(), "Wind Chimes");
        add(EclipticSeasonsMod.ModContents.paper_wind_mill.get(), "paper Wind Mill");
    }
}
