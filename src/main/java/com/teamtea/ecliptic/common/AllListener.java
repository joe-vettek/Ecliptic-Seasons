package com.teamtea.ecliptic.common;


import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.common.core.biome.BiomeClimateManager;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import com.teamtea.ecliptic.common.core.crop.CropGrowthHandler;
import com.teamtea.ecliptic.common.handler.SolarProvider;
import com.teamtea.ecliptic.common.handler.CustomRandomTickHandler;
import com.teamtea.ecliptic.common.network.SimpleNetworkHandler;
import com.teamtea.ecliptic.common.network.SolarTermsMessage;
import com.teamtea.ecliptic.config.ServerConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public class AllListener {
    public static LazyOptional<SolarProvider> provider = LazyOptional.empty();

    @SubscribeEvent
    public static void onTagsUpdatedEvent(TagsUpdatedEvent tagsUpdatedEvent) {
        BiomeClimateManager.init(tagsUpdatedEvent.getRegistryAccess());
    }


    @SubscribeEvent
    public static void onSleepFinishedTimeEvent(SleepFinishedTimeEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            // TODO: 根据季节更新概率
            if (!serverLevel.isRaining() && serverLevel.getRandom().nextFloat() > 0.8) {
                serverLevel.setWeatherParameters(0,
                        ServerLevel.RAIN_DURATION.sample(serverLevel.getRandom()),
                        true, false);
            }
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStartEvent(ServerAboutToStartEvent event) {
        WeatherManager.BIOME_WEATHER_LIST.clear();
        WeatherManager.NEXT_CHECK_BIOME_MAP.clear();
        BiomeClimateManager.BIOME_DEFAULT_TEMPERATURE_MAP.clear();
    }


    @SubscribeEvent
    public static void onLevelEventLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof Level level) {
            var list = new ArrayList<WeatherManager.BiomeWeather>();
            WeatherManager.BIOME_WEATHER_LIST.put(level, list);
            if (level instanceof ClientLevel clientLevel) {
                var biomes = clientLevel.registryAccess().registry(Registries.BIOME);
                if (biomes.isPresent()) {
                    for (Biome biome : biomes.get()) {
                        var loc = biomes.get().getKey(biome);
                        var id = biomes.get().getId(biome);
                        var biomeHolder = biomes.get().getHolder(ResourceKey.create(Registries.BIOME, biomes.get().getKey(biome)));
                        if (biomeHolder.isPresent()) {
                            var biomeWeather = new WeatherManager.BiomeWeather(biomeHolder.get());
                            biomes.get().getId(biome);
                            biomeWeather.location = loc;
                            biomeWeather.id = id;
                            list.add(biomeWeather);
                        }
                    }
                }
                int aa=0;
            }
        }
    }

    @SubscribeEvent
    public static void onLevelEventUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof Level level)
            WeatherManager.BIOME_WEATHER_LIST.remove(level);
    }


    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END) && ServerConfig.Season.enable.get() && !event.level.isClientSide() && event.level.dimension() == Level.OVERWORLD) {
            event.level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(data ->
            {
                if (!event.level.players().isEmpty()) {
                    data.updateTicks((ServerLevel) event.level);
                }
            });
        }

        CustomRandomTickHandler.onWorldTick(event);
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<Level> event) {
        if (ServerConfig.Season.enable.get() && event.getObject().dimension() == Level.OVERWORLD) {
            var cc = new SolarProvider();
            provider = LazyOptional.of(() -> cc);
            event.addCapability(new ResourceLocation(Ecliptic.MODID, "world_solar_terms"), cc);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.getEntity() instanceof ServerPlayer && !(event.getEntity() instanceof FakePlayer)) {
            if (ServerConfig.Season.enable.get()) {
                event.getEntity().level().getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(t ->
                {
                    SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SolarTermsMessage(t));
                    if (t.getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                        ((ServerPlayer) event.getEntity()).sendSystemMessage(Component.translatable("info.teastory.environment.solar_term.message", SolarTerm.get(t.getSolarTermIndex()).getAlternationText()), false);
                    }
                });
            }
        }
    }


    @SubscribeEvent
    public static void canCropGrowUp(BlockEvent.CropGrowEvent.Pre event) {
        CropGrowthHandler.canCropGrowUp(event);
    }
}
