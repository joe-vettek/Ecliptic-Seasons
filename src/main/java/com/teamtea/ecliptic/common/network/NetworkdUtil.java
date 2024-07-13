package com.teamtea.ecliptic.common.network;

import com.teamtea.ecliptic.client.color.season.BiomeColorsHandler;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.core.biome.BiomeClimateManager;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NetworkdUtil {

    public static ClientLevel getClient() {
        return Minecraft.getInstance().level;
    }

    public static boolean processSolarTermsMessage(SolarTermsMessage solarTermsMessage, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {
            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {

                AllListener.getSaveDataLazy(NetworkdUtil.getClient()).ifPresent(data ->
                        {
                            data.setSolarTermsDay(solarTermsMessage.solarDay);
                            BiomeClimateManager.updateTemperature(NetworkdUtil.getClient(),data.getSolarTermIndex());
                            BiomeColorsHandler.needRefresh = true;
                        }
                );
                // try {
                //     // if (AllListener.getSaveDataLazy(NetworkdUtil.getClient()).resolve().get().getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                //     //     // 强制刷新
                //     //     // var cc = Minecraft.getInstance().levelRenderer;
                //     //     // cc.allChanged();
                //     // }
                // } catch (Exception e) {
                //     e.printStackTrace();
                // }
            }
        });
        return true;
    }

    public static boolean processBiomeWeatherMessage(BiomeWeatherMessage biomeWeatherMessage, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {

            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                var lists = WeatherManager.getBiomeList(NetworkdUtil.getClient());
                if (lists != null) {
                    for (WeatherManager.BiomeWeather biomeWeather : lists) {
                        biomeWeather.rainTime = biomeWeatherMessage.rain[biomeWeather.id] * 100000;
                        biomeWeather.clearTime = biomeWeatherMessage.clear[biomeWeather.id] * 100000;
                        biomeWeather.thunderTime = biomeWeatherMessage.thuder[biomeWeather.id] * 100000;
                        biomeWeather.snowDepth = biomeWeatherMessage.snowDepth[biomeWeather.id];
                    }
                }
            }
        });
        return true;
    }
}
