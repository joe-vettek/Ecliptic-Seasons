package com.teamtea.ecliptic.network;


import com.teamtea.ecliptic.api.INormalMessage;
import com.teamtea.ecliptic.core.BiomeTemperatureManager;
import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import com.teamtea.ecliptic.core.SolarDataRunner;
import com.teamtea.ecliptic.client.color.season.BiomeColorsHandler;

import com.teamtea.ecliptic.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import com.teamtea.ecliptic.client.core.SolarClientManager;

import java.util.function.Supplier;

public class SolarTermsMessage implements INormalMessage {
    int solarDay;
    // int solarDay;
    float snowLayer = 0.0f;

    public SolarTermsMessage(int solarDay) {
        this.solarDay = solarDay;
    }

    public SolarTermsMessage(FriendlyByteBuf buf) {
        solarDay = buf.readInt();
        // solarDay = buf.readInt();
        snowLayer = buf.readFloat();
    }

    public SolarTermsMessage(SolarDataRunner solarData) {
        solarDay = solarData.getSolarTermsDay();
        snowLayer = solarData.getSnowLayer();
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(solarDay);
        buf.writeFloat(snowLayer);
    }


    public void process(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {
            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {

                Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(data ->
                        {
                            data.setSolarTermsDay(solarDay);
                            data.setSnowLayer(snowLayer);
                            BiomeTemperatureManager.updateTemperature(Minecraft.getInstance().level,data.getSolarTermIndex());
                            BiomeColorsHandler.needRefresh = true;
                            SolarClientManager.updateSnowLayer(data.getSnowLayer());
                        }
                );
                try {
                    if (Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).resolve().get().getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
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
