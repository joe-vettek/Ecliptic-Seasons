package com.teamtea.ecliptic.common.network;


import com.teamtea.ecliptic.api.INormalMessage;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BiomeWeatherMessage implements INormalMessage {
    private final byte[] rain;
    private final byte[] thuder;
    private final byte[] clear;
    private final byte[] snowDepth;

    public BiomeWeatherMessage(FriendlyByteBuf buf) {
        rain = buf.readByteArray();
        thuder = buf.readByteArray();
        clear = buf.readByteArray();
        snowDepth = buf.readByteArray();
    }

    public BiomeWeatherMessage(byte[] rain, byte[] thuder, byte[] clear, byte[] snowDepth) {
        this.rain = rain;
        this.thuder = thuder;
        this.clear = clear;
        this.snowDepth = snowDepth;
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByteArray(rain);
        buf.writeByteArray(thuder);
        buf.writeByteArray(clear);
        buf.writeByteArray(snowDepth);
    }


    public void process(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {
            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                var lists = WeatherManager.BIOME_WEATHER_LIST.get(Minecraft.getInstance().level);
                if (lists != null) {
                    for (WeatherManager.BiomeWeather biomeWeather : lists) {
                        biomeWeather.rainTime = rain[biomeWeather.id] * 100000;
                        biomeWeather.clearTime = clear[biomeWeather.id] * 100000;
                        biomeWeather.thunderTime = thuder[biomeWeather.id] * 100000;
                        biomeWeather.snowDepth = snowDepth[biomeWeather.id];
                    }
                }
            }
        });
    }
}
