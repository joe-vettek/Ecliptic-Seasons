package com.teamtea.ecliptic.common.network;


import com.teamtea.ecliptic.api.INormalMessage;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.core.biome.BiomeClimateManager;
import com.teamtea.ecliptic.common.core.solar.SolarDataManager;
import com.teamtea.ecliptic.client.color.season.BiomeColorsHandler;

import com.teamtea.ecliptic.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SolarTermsMessage implements INormalMessage {
    int solarDay;

    public SolarTermsMessage(int solarDay) {
        this.solarDay = solarDay;
    }

    public SolarTermsMessage(FriendlyByteBuf buf) {
        solarDay = buf.readInt();

    }

    public SolarTermsMessage(SolarDataManager solarData) {
        solarDay = solarData.getSolarTermsDay();
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(solarDay);
    }


    public void process(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {
            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {

                AllListener.getSaveDataLazy(Minecraft.getInstance().level).ifPresent(data ->
                        {
                            data.setSolarTermsDay(solarDay);
                            BiomeClimateManager.updateTemperature(Minecraft.getInstance().level,data.getSolarTermIndex());
                            BiomeColorsHandler.needRefresh = true;
                        }
                );
                try {
                    if (AllListener.getSaveDataLazy(Minecraft.getInstance().level).resolve().get().getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                        // 强制刷新
                        var cc = Minecraft.getInstance().levelRenderer;
                        // cc.allChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
