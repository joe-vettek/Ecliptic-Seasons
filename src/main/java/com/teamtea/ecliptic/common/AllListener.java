package com.teamtea.ecliptic.common;


import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.common.core.biome.BiomeClimateManager;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import com.teamtea.ecliptic.common.core.crop.CropGrowthHandler;
import com.teamtea.ecliptic.common.core.solar.SolarDataManager;
import com.teamtea.ecliptic.common.handler.CustomRandomTickHandler;
import com.teamtea.ecliptic.common.network.SimpleNetworkHandler;
import com.teamtea.ecliptic.common.network.SolarTermsMessage;
import com.teamtea.ecliptic.config.ServerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public class AllListener {
    // public static LazyOptional<SolarProvider> provider = LazyOptional.empty();

    public static final Map<Level, SolarDataManager> DATA_MANAGER_MAP = new HashMap<>();


    public static SolarDataManager getSaveData(Level level) {
        return DATA_MANAGER_MAP.getOrDefault(level,null);
    }

    public static LazyOptional<SolarDataManager> getSaveDataLazy(Level level) {
        return LazyOptional.of(() -> DATA_MANAGER_MAP.getOrDefault(level, new SolarDataManager(level)));
    }


    // TagsUpdatedEvent invoke before ServerAboutToStartEvent
    @SubscribeEvent
    public static void onTagsUpdatedEvent(TagsUpdatedEvent tagsUpdatedEvent) {
        BiomeClimateManager.resetBiomeTemps(tagsUpdatedEvent.getRegistryAccess());
        WeatherManager.informUpdateBiomes(tagsUpdatedEvent.getRegistryAccess());
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
        // if (event.getLevel() instanceof ServerLevel serverLevel) {
        //     // TODO: 根据季节更新概率
        //     if (!serverLevel.isRaining() && serverLevel.getRandom().nextFloat() > 0.8) {
        //         serverLevel.setWeatherParameters(0,
        //                 ServerLevel.RAIN_DURATION.sample(serverLevel.getRandom()),
        //                 true, false);
        //     }
        // }
    }




    @SubscribeEvent
    public static void onLevelEventLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            WeatherManager.createLevelBiomeWeatherList(serverLevel);
            // 这里需要恢复一下数据
            // 客户端登录时同步天气数据，此处先放入
            DATA_MANAGER_MAP.put(serverLevel, SolarDataManager.get(serverLevel));
        }
    }

    @SubscribeEvent
    public static void onLevelUnloadEvent(LevelEvent.Unload event) {
        if (event.getLevel() instanceof Level level) {
            WeatherManager.BIOME_WEATHER_LIST.remove(level);
            if (level instanceof ServerLevel serverLevel) {
                DATA_MANAGER_MAP.remove(serverLevel);
            }
        }

    }


    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END) && ServerConfig.Season.enable.get() && !event.level.isClientSide() && event.level.dimension() == Level.OVERWORLD) {
            getSaveDataLazy(event.level).ifPresent(data ->
            {
                if (!event.level.players().isEmpty()) {
                    data.updateTicks((ServerLevel) event.level);
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

        if (event.getEntity() instanceof ServerPlayer && !(event.getEntity() instanceof FakePlayer)) {
            if (ServerConfig.Season.enable.get()) {
                getSaveDataLazy(event.getEntity().level()).ifPresent(t ->
                {
                    SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SolarTermsMessage(t.getSolarTermsDay()));
                    if (t.getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                        ((ServerPlayer) event.getEntity()).sendSystemMessage(Component.translatable("info.teastory.environment.solar_term.message", SolarTerm.get(t.getSolarTermIndex()).getAlternationText()), false);
                    }
                });
                WeatherManager.sendBiomePacket(WeatherManager.getBiomeList(event.getEntity().level()), List.of((ServerPlayer) event.getEntity()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {

        if (event.getEntity() instanceof ServerPlayer && !(event.getEntity() instanceof FakePlayer)) {
            if (ServerConfig.Season.enable.get()) {
                WeatherManager.sendBiomePacket(WeatherManager.getBiomeList(event.getEntity().level()), List.of((ServerPlayer) event.getEntity()));
            }
        }
    }


    @SubscribeEvent
    public static void canCropGrowUp(BlockEvent.CropGrowEvent.Pre event) {
        CropGrowthHandler.canCropGrowUp(event);
    }
}
