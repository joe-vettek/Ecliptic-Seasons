package com.teamtea.eclipticseasons.common.core.solar;

import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;

import java.lang.ref.WeakReference;


public class ClientSolarDataManager extends SolarDataManager {

    public ClientSolarDataManager(Level level) {
        super(level);
        this.levelWeakReference = new WeakReference<>(level);
    }

    public ClientSolarDataManager(Level level, CompoundTag nbt) {
        this(level);
        setSolarTermsDay(nbt.getInt("SolarTermsDay"));
        setSolarTermsTicks(nbt.getInt("SolarTermsTicks"));
        var listTag = nbt.getList("biomes", Tag.TAG_COMPOUND);
        if (this.levelWeakReference.get() != null) {
            var biomeWeathers = WeatherManager.getBiomeList(this.levelWeakReference.get());
            for (int i = 0; i < listTag.size(); i++) {
                var location = listTag.getCompound(i).getString("biome");
                for (WeatherManager.BiomeWeather biomeWeather : biomeWeathers) {
                    if (location.equals(biomeWeather.location.toString())) {
                        biomeWeather.deserializeNBT(listTag.getCompound(i));
                        break;
                    }
                }
            }
        }
    }


    public static ClientSolarDataManager get(Level level) {
        if (level instanceof ClientLevel clientLevel) {
            return get(clientLevel);
        }
        return null;
    }


    public static ClientSolarDataManager get(ClientLevel clientLevel) {
        return new ClientSolarDataManager(clientLevel);
    }


}
