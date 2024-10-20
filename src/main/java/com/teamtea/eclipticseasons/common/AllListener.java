package com.teamtea.eclipticseasons.common;


import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.api.util.EclipticTagTool;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.crop.CropGrowthHandler;
import com.teamtea.eclipticseasons.common.core.solar.SolarDataManager;
import com.teamtea.eclipticseasons.common.handler.CustomRandomTickHandler;
import com.teamtea.eclipticseasons.config.ServerConfig;
import net.minecraft.entity.player.ServerPlayerEntity;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EclipticSeasons.MODID)
public class AllListener {
    // public static LazyOptional<SolarProvider> provider = LazyOptional.empty();

    public static final Map<World, SolarDataManager> DATA_MANAGER_MAP = new HashMap<>();


    public static SolarDataManager getSaveData(World level) {
        return DATA_MANAGER_MAP.getOrDefault(level, null);
    }

    public static LazyOptional<SolarDataManager> getSaveDataLazy(World level) {
        return LazyOptional.of(() -> DATA_MANAGER_MAP.getOrDefault(level, new SolarDataManager(level)));
    }

    public static void createSaveData(World level, SolarDataManager solarDataManager) {
        DATA_MANAGER_MAP.put(level, solarDataManager);
    }

    // TagsUpdatedEvent invoke before ServerAboutToStartEvent
    // TODO：优化这个问题，理论上来说，更新数据的时候不能发送群系包，话说回来，既然是群系天气，实际上与level关系不大，不应该一个level一个
    // 但是这也说不准啊，谁知道谁无聊就搞这个呢
    @SubscribeEvent
    public static void onTagsUpdatedEvent(TagsUpdatedEvent tagsUpdatedEvent) {
        BiomeClimateManager.resetBiomeTemps();
        WeatherManager.informUpdateBiomes();
        EclipticTagTool.BIOME_TAG_KEY_MAP.clear();
    }


    // @SubscribeEvent
    // public static void onServerStartedEvent(ServerStartedEvent event) {
    //     BiomeClimateManager.resetBiomeTemps(event.getServer().registryAccess());
    //     WeatherManager.informUpdateBiomes(event.getServer().registryAccess());
    // }

    @SubscribeEvent
    public static void onSleepFinishedTimeEvent(SleepFinishedTimeEvent event) {
        if (event.getWorld() instanceof ServerWorld ) {

            long newTime = event.getNewTime(), oldDayTime = ((ServerWorld)event.getWorld()).getDayTime();
            WeatherManager.updateAfterSleep( ((ServerWorld)event.getWorld()), newTime, oldDayTime);
            // // TODO: 根据季节更新概率
            // if (!serverLevel.isRaining() && serverLevel.getRandom().nextFloat() > 0.8) {
            //     serverLevel.setWeatherParameters(0,
            //             ServerLevel.RAIN_DURATION.sample(serverLevel.getRandom()),
            //             true, false);
            // }
        }

    }


    @SubscribeEvent
    public static void onLevelEventLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld ) {
            WeatherManager.createLevelBiomeWeatherList( ((ServerWorld)event.getWorld()));
            // 这里需要恢复一下数据
            // 客户端登录时同步天气数据，此处先放入
            createSaveData( ((ServerWorld)event.getWorld()), SolarDataManager.get( ((ServerWorld)event.getWorld())));
        }
    }

    @SubscribeEvent
    public static void onLevelUnloadEvent(WorldEvent.Unload event) {
        if (event.getWorld() instanceof World ) {
            WeatherManager.BIOME_WEATHER_LIST.remove((World) event.getWorld());
            // if (level instanceof ServerLevel serverLevel)
            {
                DATA_MANAGER_MAP.remove((World) event.getWorld());
            }
        }

    }


    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END) && ServerConfig.Season.enableInform.get() && !event.world.isClientSide() && event.world.dimension() == World.OVERWORLD) {
            getSaveDataLazy(event.world).ifPresent(data ->
            {
                if (!event.world.players().isEmpty()) {
                    data.updateTicks((ServerWorld) event.world);
                }
            });
        }

        CustomRandomTickHandler.onWorldTick(event);

    }

    // @SubscribeEvent
    // public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<Level> event) {
    //     if (ServerConfig.Season.enable.get() && event.getObject().dimension() == Level.OVERWORLD) {
    //         var cc = new SolarProvider();
    //         provider = LazyOptional.of(() -> cc);
    //         event.addCapability(new ResourceLocation(Ecliptic.MODID, "world_solar_terms"), cc);
    //     }
    // }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity  && !(event.getEntity() instanceof FakePlayer)) {
            WeatherManager.onLoggedIn((ServerPlayerEntity)event.getEntity(), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity ) {
            WeatherManager.onLoggedIn((ServerPlayerEntity)event.getEntity(), false);
        }
    }


    @SubscribeEvent
    public static void onCropGrowUp(BlockEvent.CropGrowEvent.Pre event) {
        CropGrowthHandler.beforeCropGrowUp(event);
    }
}
