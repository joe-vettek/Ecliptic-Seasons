package com.teamtea.eclipticseasons.common.handler;

import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.common.core.Holder;
import com.teamtea.eclipticseasons.common.core.solar.SolarDataManager;
import net.minecraft.world.level.Level;


public class SolarUtil {


    public static SolarDataManager getProvider(Level level) {
        return Holder.getSaveData(level);
    }

    public static Season getSeason(Level level) {
        return Holder.getSaveDataLazy(level).map(data -> data.getSolarTerm().getSeason()).orElse(Season.NONE);
    }

    public static SolarTerm getSolarTerm(Level level) {
        return Holder.getSaveDataLazy(level).map(SolarDataManager::getSolarTerm).orElse(SolarTerm.NONE);
    }
}
