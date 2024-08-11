package com.teamtea.eclipticseasons.common.network;

import com.teamtea.eclipticseasons.client.color.season.BiomeColorsHandler;
import com.teamtea.eclipticseasons.common.core.Holder;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NetworkdUtil {

    public static ClientLevel getClient() {
        return Minecraft.getInstance().level;
    }


    public static void processSolarTermsMessage2(SolarTermsMessage solarTermsMessage, IPayloadContext context) {
        context.enqueueWork(() -> {
                    Holder.getSaveDataLazy(context.player().level()).ifPresent(data ->
                            {
                                data.setSolarTermsDay(solarTermsMessage.solarDay);
                                BiomeClimateManager.updateTemperature(context.player().level(), data.getSolarTerm());
                                BiomeColorsHandler.needRefresh = true;
                            }
                    );
                })
                .exceptionally(e -> {
                    // Handle exception
                    context.disconnect(Component.translatable("ecliptic.networking.failed", e.getMessage()));
                    return null;
                });
    }

    public static void processEmptyMessage2(EmptyMessage emptyMessage, IPayloadContext context) {
        context.enqueueWork(() -> {
                    Minecraft.getInstance().levelRenderer.allChanged();
                })
                .exceptionally(e -> {
                    // Handle exception
                    context.disconnect(Component.translatable("ecliptic.networking.failed", e.getMessage()));
                    return null;
                });
    }

    public static void processBiomeWeatherMessage2(BiomeWeatherMessage biomeWeatherMessage, IPayloadContext context) {
        context.enqueueWork(() -> {
                    var lists = WeatherManager.getBiomeList(context.player().level());
                    if (lists != null) {
                        for (WeatherManager.BiomeWeather biomeWeather : lists) {
                            biomeWeather.rainTime = biomeWeatherMessage.rain[biomeWeather.id] * 10000;
                            biomeWeather.clearTime = biomeWeatherMessage.clear[biomeWeather.id] * 10000;
                            biomeWeather.thunderTime = biomeWeatherMessage.thuder[biomeWeather.id] * 10000;
                            biomeWeather.snowDepth = biomeWeatherMessage.snowDepth[biomeWeather.id];
                        }
                    }
                })
                .exceptionally(e -> {
                    // Handle exception
                    context.disconnect(Component.translatable("ecliptic.networking.failed", e.getMessage()));
                    return null;
                });
    }
}
