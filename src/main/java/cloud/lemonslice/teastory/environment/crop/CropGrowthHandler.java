package cloud.lemonslice.teastory.environment.crop;


import cloud.lemonslice.silveroak.environment.Humidity;
import cloud.lemonslice.teastory.config.ServerConfig;
import cloud.lemonslice.teastory.helper.SeasonHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xueluoanping.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public final class CropGrowthHandler
{
    @SubscribeEvent
    public static void canCropGrowUp(BlockEvent.CropGrowEvent.Pre event)
    {
        Block block = event.getState().getBlock();
        var world = event.getLevel();
        BlockPos pos = event.getPos();
        CropSeasonInfo seasonInfo = CropInfoManager.getSeasonInfo(block);
        if (seasonInfo != null && ServerConfig.Season.enable.get())
        {
            if (seasonInfo.isSuitable(SeasonHelper.getSeason((Level) world)))
            {
                Humidity env = Humidity.getHumid(world.getBiome(pos).get().getModifiedClimateSettings().downfall(), world.getBiome(pos).get().getTemperature(pos));
                CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
                checkHumidity(event, world, humidityInfo, env);
            }
            else if (world.getRandom().nextInt(100) < 25)
            {
                Humidity env = Humidity.getHumid(world.getBiome(pos).get().getModifiedClimateSettings().downfall(), world.getBiome(pos).get().getTemperature(pos));
                CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
                checkHumidity(event, world, humidityInfo, env);
            }
            else
            {
                event.setResult(Event.Result.DENY);
            }
        }
        else
        {
            Humidity env = Humidity.getHumid(world.getBiome(pos).get().getModifiedClimateSettings().downfall(), world.getBiome(pos).get().getTemperature(pos));
            CropHumidityInfo humidityInfo = CropInfoManager.getHumidityInfo(block);
            checkHumidity(event, world, humidityInfo, env);
        }
    }

    public static void checkHumidity(BlockEvent.CropGrowEvent.Pre event, LevelAccessor world, CropHumidityInfo humidityInfo, Humidity env)
    {
        if (humidityInfo != null)
        {
            float f = humidityInfo.getGrowChance(env);
            if (f == 0)
            {
                event.setResult(Event.Result.DENY);
            }
            else if (f > 1.0F)
            {
                event.setResult(Event.Result.ALLOW);
            }
            else
            {
                if (world.getRandom().nextInt(1000) < 1000 * f)
                {
                    event.setResult(Event.Result.DEFAULT);
                }
                else
                {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
