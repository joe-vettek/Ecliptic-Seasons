package com.teamtea.eclipticseasons.common.core.solar;


import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.common.AllListener;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IDayTimeReader;
import net.minecraft.world.World;


public class SolarAngelHelper {

    public static float getSeasonCelestialAngle(IDayTimeReader world, long worldTime) {
        return getCelestialAngle(getSolarAngelTime(world, worldTime));
    }

    public static int getSolarAngelTime(IDayTimeReader world,long worldTime)
    {
        if (world instanceof World  &&AllListener.getSaveData((World) world)!=null)
        {
            return AllListener.getSaveDataLazy((World) world).map(data ->
            {
                int dayTime = SolarTerm.get(data.getSolarTermIndex()).getDayTime();
                // dayTime=23900;
                int sunrise = 24000 - dayTime / 2;
                int sunset = dayTime / 2;
                int dayLevelTime = Math.toIntExact((worldTime + 18000) % 24000); // 0 for noon; 6000 for sunset; 18000 for sunrise.
                int solarAngelTime;
                if (0 <= dayLevelTime && dayLevelTime <= sunset)
                {
                    solarAngelTime = 6000 + dayLevelTime * 6000 / sunset;
                }
                else if (dayLevelTime > sunset && dayLevelTime <= sunrise)
                {
                    solarAngelTime = 12000 + (dayLevelTime - sunset) * 12000 / (24000 - dayTime);
                }
                else
                {
                    solarAngelTime = (dayLevelTime - sunrise) * 6000 / (24000 - sunrise);
                }
                return solarAngelTime;
            }).orElse(Math.toIntExact(worldTime % 24000));
        }
        return Math.toIntExact(worldTime % 24000);
    }

    public static float getCelestialAngle(long worldTime) {
        double d0 = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }
}
