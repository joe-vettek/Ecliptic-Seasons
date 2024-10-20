package com.teamtea.eclipticseasons.common.network;

import com.teamtea.eclipticseasons.client.color.season.BiomeColorsHandler;
import com.teamtea.eclipticseasons.common.AllListener;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;


import java.util.ArrayList;
import java.util.Optional;
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
                            BiomeClimateManager.updateTemperature(NetworkdUtil.getClient(), data.getSolarTermIndex());
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
                    for (int i = 0; i < biomeWeatherMessage.ids.length; i++) {
                        int biomeId = biomeWeatherMessage.ids[i];
                        Optional<WeatherManager.BiomeWeather> biomeWeather =
                                lists.stream()
                                        .filter(biomeWeather1 -> biomeWeather1.id == biomeId)
                                        .findFirst();
                        int finalI = i;
                        biomeWeather.ifPresent(biomeWeather1 ->
                                {
                                    biomeWeather1.rainTime = biomeWeatherMessage.rain[finalI] * 10000;
                                    biomeWeather1.clearTime = biomeWeatherMessage.clear[finalI] * 10000;
                                    biomeWeather1.thunderTime = biomeWeatherMessage.thuder[finalI] * 10000;
                                    biomeWeather1.snowDepth = biomeWeatherMessage.snowDepth[finalI];
                                }
                        );
                    }

                    WorldRenderer lr = Minecraft.getInstance().levelRenderer;
                    if (lr != null) {
                        //
                        // ((ClientChunkCache) event.level.getChunkSource()).storage.
                        if (Minecraft.getInstance().player != null) {

                            BlockPos pos = Minecraft.getInstance().player.blockPosition().below();
                            SectionPos sectionPos = SectionPos.of(pos);
                            lr.setSectionDirtyWithNeighbors(sectionPos.x(),sectionPos.y(),sectionPos.z());
                            // int x = sectionPos.x();
                            // int y = sectionPos.y();
                            // int z = sectionPos.z();
                            // for (int i = x - 2; i <= x + 2; ++i) {
                            //     for (int j = z - 2; j <= z + 2; ++j) {
                            //         for (int k = y - 1; k <= y + 1; ++k) {
                            //             lr.setSectionDirty(j, k, i);
                            //         }
                            //     }
                            // }
                        }
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
