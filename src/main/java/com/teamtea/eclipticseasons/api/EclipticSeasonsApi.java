package com.teamtea.eclipticseasons.api;

import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;


public interface EclipticSeasonsApi {

    String MODID = "eclipticseasons";
    String SMODID = "ecliptic";

    static EclipticSeasonsApi getInstance() {
        return EclipticUtil.INSTANCE;
    }

    /**
     * Get the solar term.
     * Or use it to get the season{@link SolarTerm#getSeason()},
     * or get the climate classification of the biome{@link SolarTerm#getBiomeRain(Holder)} ()},
     * and which solar terms of the biome snow{@link SolarTerm#getSnowTerm(Biome, boolean)} ()}.
     *
     * <p>Only dimensions marked as {@linkplain DimensionType#natural()  natural} have solar term changes.</p>
     *
     */
    SolarTerm getSolarTerm(Level level);

    boolean isDay(Level level);

    boolean isNight(Level level);

    /**
     * The nighttime is used to process the time command.
     * It is also used as a time to distinguish between day and night.
     * After this time, the player can fall asleep quickly.
     */
    int getNightTime(Level level);

    /**
     * Determine if it is noon, a few hours around tick 6000.
     */
    boolean isNoon(Level level);

    /**
     * Judging whether it is evening now, it will not last until deep into midnight.
     */
    boolean isEvening(Level level);

}
