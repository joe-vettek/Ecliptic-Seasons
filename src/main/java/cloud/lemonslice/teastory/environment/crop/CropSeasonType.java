package cloud.lemonslice.teastory.environment.crop;


import net.minecraft.resources.ResourceLocation;
import xueluoanping.ecliptic.Ecliptic;

public enum CropSeasonType
{
    SPRING(new CropSeasonInfo(1), Ecliptic.rl("crops/spring")),
    SUMMER(new CropSeasonInfo(2), Ecliptic.rl("crops/summer")),
    AUTUMN(new CropSeasonInfo(4), Ecliptic.rl("crops/autumn")),
    WINTER(new CropSeasonInfo(8), Ecliptic.rl("crops/winter")),
    SP_SU(new CropSeasonInfo(3), Ecliptic.rl("crops/spring_summer")),
    SP_AU(new CropSeasonInfo(5), Ecliptic.rl("crops/spring_autumn")),
    SP_WI(new CropSeasonInfo(9), Ecliptic.rl("crops/spring_winter")),
    SU_AU(new CropSeasonInfo(6), Ecliptic.rl("crops/summer_autumn")),
    SU_WI(new CropSeasonInfo(10), Ecliptic.rl("crops/summer_winter")),
    AU_WI(new CropSeasonInfo(12), Ecliptic.rl("crops/autumn_winter")),
    SP_SU_AU(new CropSeasonInfo(7), Ecliptic.rl("crops/spring_summer_autumn")),
    SP_SU_WI(new CropSeasonInfo(11), Ecliptic.rl("crops/spring_summer_winter")),
    SP_AU_WI(new CropSeasonInfo(13), Ecliptic.rl("crops/spring_autumn_winter")),
    SU_AU_WI(new CropSeasonInfo(14), Ecliptic.rl("crops/summer_autumn_winter")),
    ALL(new CropSeasonInfo(15), Ecliptic.rl("crops/all_seasons"));

    private final CropSeasonInfo info;
    private final ResourceLocation res;

    CropSeasonType(CropSeasonInfo info, ResourceLocation res)
    {
        this.info = info;
        this.res = res;
    }

    public CropSeasonInfo getInfo()
    {
        return info;
    }

    public ResourceLocation getRes()
    {
        return res;
    }
}
