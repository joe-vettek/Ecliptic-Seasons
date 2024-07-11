package com.teamtea.ecliptic.common.core.solar;

import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.common.core.biome.BiomeClimateManager;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import com.teamtea.ecliptic.common.network.SimpleNetworkHandler;
import com.teamtea.ecliptic.common.network.SolarTermsMessage;
import com.teamtea.ecliptic.config.ServerConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class SolarDataManager extends SavedData {

    private int solarTermsDay = (ServerConfig.Season.initialSolarTermIndex.get() - 1) * ServerConfig.Season.lastingDaysOfEachTerm.get();
    private int solarTermsTicks = 0;

    private WeakReference<Level> levelWeakReference;

    public SolarDataManager(Level level) {
        levelWeakReference = new WeakReference<>(level);
    }

    public SolarDataManager(Level level, CompoundTag nbt) {
        this(level);
        setSolarTermsDay(nbt.getInt("SolarTermsDay"));
        setSolarTermsTicks(nbt.getInt("SolarTermsTicks"));
        var listTag = nbt.getList("biomes", Tag.TAG_COMPOUND);
        if (levelWeakReference.get() != null) {
            var biomeWeathers =WeatherManager.getBiomeList(levelWeakReference.get());
            for (int i = 0; i < listTag.size(); i++) {
                var location = listTag.getCompound(i).getString("biome");
                for (WeatherManager.BiomeWeather biomeWeather : biomeWeathers) {
                    if (location.equals(biomeWeather.location.toString()))
                    {
                        biomeWeather.deserializeNBT(listTag.getCompound(i));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compound) {
        compound.putInt("SolarTermsDay", getSolarTermsDay());
        compound.putInt("SolarTermsTicks", getSolarTermsTicks());
        ListTag listTag = new ListTag();
        if (levelWeakReference.get() != null) {
            var list = WeatherManager.getBiomeList(levelWeakReference.get());
            for (WeatherManager.BiomeWeather biomeWeather : list) {
                listTag.add(biomeWeather.serializeNBT());
            }
        }
        compound.put("biomes", listTag);
        return compound;
    }

    public static SolarDataManager get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return get(serverLevel);
        }
        if (level instanceof ClientLevel clientLevel) {
            return get(clientLevel);
        }
        return null;
    }


    public static SolarDataManager get(ServerLevel serverLevel) {
        DimensionDataStorage storage = serverLevel.getDataStorage();
        return storage.computeIfAbsent((compoundTag) -> new SolarDataManager(serverLevel, compoundTag),
                () -> new SolarDataManager(serverLevel), Ecliptic.MODID);
    }


    public static SolarDataManager get(ClientLevel clientLevel) {
        return new SolarDataManager(clientLevel);
    }


    public void updateTicks(ServerLevel world) {
        solarTermsTicks++;
        int dayTime = Math.toIntExact(world.getDayTime() % 24000);
        if (solarTermsTicks > dayTime + 100) {
            solarTermsDay++;
            solarTermsDay %= 24 * ServerConfig.Season.lastingDaysOfEachTerm.get();

            BiomeClimateManager.updateTemperature(world, getSolarTermIndex());
            sendUpdateMessage(world);
        }
        solarTermsTicks = dayTime;

        if (world.getRandom().nextBoolean() && world.getDayTime() % 1000 == 0) {
            // player.connection.send();
            var a = new ArrayList<ChunkAccess>();
            for (ChunkHolder chunk : (world).getChunkSource().chunkMap.getChunks()) {
                var cs = chunk.getLastAvailable();
                if (cs != null)
                    a.add(chunk.getLastAvailable());
            }
            // 强制刷新
            world.getChunkSource().chunkMap.resendBiomesForChunks(a);
        }

        setDirty();
    }

    public int getSolarTermIndex() {
        return solarTermsDay / ServerConfig.Season.lastingDaysOfEachTerm.get();
    }

    public SolarTerm getSolarTerm() {
        return SolarTerm.get(this.getSolarTermIndex());
    }

    public int getSolarTermsDay() {
        return solarTermsDay;
    }

    public int getSolarTermsTicks() {
        return solarTermsTicks;
    }

    public void setSolarTermsDay(int solarTermsDay) {
        this.solarTermsDay = Math.max(solarTermsDay, 0) % (24 * ServerConfig.Season.lastingDaysOfEachTerm.get());
        setDirty();
    }

    public void setSolarTermsTicks(int solarTermsTicks) {
        this.solarTermsTicks = solarTermsTicks;
        setDirty();
    }


    public void sendUpdateOnly(ServerLevel world) {
        for (ServerPlayer player : world.getServer().getPlayerList().getPlayers()) {
            SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SolarTermsMessage(this));
        }
    }

    public void sendUpdateMessage(ServerLevel world) {
        for (ServerPlayer player : world.getServer().getPlayerList().getPlayers()) {
            SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SolarTermsMessage(this));
            if (getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                player.sendSystemMessage(Component.translatable("info.teastory.environment.solar_term.message", SolarTerm.get(getSolarTermIndex()).getAlternationText()), false);
            }
        }
    }

}
