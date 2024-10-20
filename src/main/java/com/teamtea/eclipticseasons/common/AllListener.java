package com.teamtea.eclipticseasons.common;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.client.core.map.ClientMapFixer;
import com.teamtea.eclipticseasons.common.core.SolarHolders;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.crop.CropGrowthHandler;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.core.solar.SolarDataManager;
import com.teamtea.eclipticseasons.common.handler.CustomRandomTickHandler;
import com.teamtea.eclipticseasons.config.ServerConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SaplingBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.*;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = EclipticSeasonsApi.MODID)
public class AllListener {
    // public static LazyOptional<SolarProvider> provider = LazyOptional.empty();


    // TagsUpdatedEvent invoke before ServerAboutToStartEvent
    // TODO：优化这个问题，理论上来说，更新数据的时候不能发送群系包，话说回来，既然是群系天气，实际上与level关系不大，不应该一个level一个
    // 但是这也说不准啊，谁知道谁无聊就搞这个呢
    @SubscribeEvent
    public static void onTagsUpdatedEvent(TagsUpdatedEvent tagsUpdatedEvent) {
        BiomeClimateManager.resetBiomeTemps(tagsUpdatedEvent.getRegistryAccess(), tagsUpdatedEvent.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD);
        WeatherManager.informUpdateBiomes(tagsUpdatedEvent.getRegistryAccess(), tagsUpdatedEvent.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD);
    }


    @SubscribeEvent
    public static void onServerAboutToStartEvent(ServerAboutToStartEvent event) {
        WeatherManager.BIOME_WEATHER_LIST.clear();
        WeatherManager.NEXT_CHECK_BIOME_MAP.clear();
    }


    // @SubscribeEvent
    // public static void onServerStartedEvent(ServerStartedEvent event) {
    //     BiomeClimateManager.resetBiomeTemps(event.getServer().registryAccess());
    //     WeatherManager.informUpdateBiomes(event.getServer().registryAccess());
    // }

    @SubscribeEvent
    public static void onSleepFinishedTimeEvent(SleepFinishedTimeEvent event) {
        if (event.getLevel() instanceof ServerLevel level) {

            long newTime = event.getNewTime(), oldDayTime = ((Level) event.getLevel()).getDayTime();
            WeatherManager.updateAfterSleep(level, newTime, oldDayTime);
            // // TODO: 根据季节更新概率
            // if (!serverLevel.isRaining() && serverLevel.getRandom().nextFloat() > 0.8) {
            //     serverLevel.setWeatherParameters(0,
            //             ServerLevel.RAIN_DURATION.sample(serverLevel.getRandom()),
            //             true, false);
            // }
        }

    }


    @SubscribeEvent
    public static void onLevelEventLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            WeatherManager.createLevelBiomeWeatherList(serverLevel);
            // 这里需要恢复一下数据
            // 客户端登录时同步天气数据，此处先放入
            SolarHolders.createSaveData(serverLevel, SolarDataManager.get(serverLevel));
        }
    }

    @SubscribeEvent
    public static void onLevelUnloadEvent(LevelEvent.Unload event) {
        if (event.getLevel() instanceof Level level) {
            WeatherManager.BIOME_WEATHER_LIST.remove(level);
            // if (level instanceof ServerLevel serverLevel)
            {
                SolarHolders.DATA_MANAGER_MAP.remove(level);
            }
        }

    }

    // 如果是客户端，即使是混合型客户端，我们也只应该清理一次，单人世界时只看一次client会更好
    @SubscribeEvent
    public static void onChunkUnloadEvent(ChunkEvent.Unload event) {
        if ((FMLLoader.getDist() == Dist.CLIENT) == event.getLevel().isClientSide()
        ) {
            MapChecker.clearChunk(event.getChunk().getPos());
        }
    }


    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        if (ServerConfig.Season.enableInform.get()
                && !event.getLevel().isClientSide()
                && MapChecker.isValidDimension(event.getLevel())) {
            SolarHolders.getSaveDataLazy(event.getLevel()).ifPresent(data ->
            {
                if (!event.getLevel().players().isEmpty()) {
                    data.updateTicks((ServerLevel) event.getLevel());
                }
            });
        }

        CustomRandomTickHandler.onWorldTick(event);

    }


    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            WeatherManager.onLoggedIn(serverPlayer, true);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            WeatherManager.onLoggedIn(serverPlayer, false);
        }
    }

    @SubscribeEvent
    public static void onCropGrowUp(CropGrowEvent.Pre event) {
        CropGrowthHandler.beforeCropGrowUp(event);
    }

    @SubscribeEvent
    public static void onCropGrowUp(BlockGrowFeatureEvent event) {
        CropGrowthHandler.beforeCropGrowUp(event);
    }


    @SubscribeEvent
    public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            WeatherManager.tickPlayerSeasonEffecct(serverPlayer);
            // WeatherManager.tickPlayerForSeasonCheck(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onChunkUpdate(ChunkWatchEvent.Sent event) {
        MapChecker.updateChunk(event.getChunk(), event.getPos(), event.getPlayer(), List.of(), List.of());
    }
}
