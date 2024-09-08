package com.teamtea.eclipticseasons.data.lang;


import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class Lang_ZH extends LangHelper {
    public Lang_ZH(PackOutput gen, ExistingFileHelper helper) {
        super(gen, helper, EclipticSeasonsApi.MODID, "zh_cn");
    }


    @Override
    protected void addTranslations() {
        add(EclipticSeasonsMod.ModContents.calendar.get(), "日历");
        add(EclipticSeasonsMod.ModContents.wind_chimes.get(), "风铃");
        add(EclipticSeasonsMod.ModContents.paper_wind_mill.get(), "纸风车");

    }


}
