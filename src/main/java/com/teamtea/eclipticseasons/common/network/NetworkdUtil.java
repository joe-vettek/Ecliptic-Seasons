package com.teamtea.eclipticseasons.common.network;

import com.teamtea.eclipticseasons.client.color.season.BiomeColorsHandler;
import com.teamtea.eclipticseasons.common.AllListener;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;


import java.util.ArrayList;
import java.util.function.Supplier;

public class NetworkdUtil {

    public static ClientWorld getClient() {
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
                ArrayList<WeatherManager.BiomeWeather> lists = WeatherManager.getBiomeList(NetworkdUtil.getClient());
                if (lists != null) {
                    for (WeatherManager.BiomeWeather biomeWeather : lists) {
                        biomeWeather.rainTime = biomeWeatherMessage.rain[biomeWeather.id] * 10000;
                        biomeWeather.clearTime = biomeWeatherMessage.clear[biomeWeather.id] * 10000;
                        biomeWeather.thunderTime = biomeWeatherMessage.thuder[biomeWeather.id] * 10000;
                        biomeWeather.snowDepth = biomeWeatherMessage.snowDepth[biomeWeather.id];
                    }
                }
            }
        });
        return true;
    }

    public static boolean processEmptyMessage(EmptyMessage emptyMessage, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {
            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
               Minecraft.getInstance().levelRenderer.allChanged();
            }
        });
        return true;
    }
}
