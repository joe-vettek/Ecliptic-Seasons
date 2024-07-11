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

import java.util.ArrayList;


public class GlobalDataManager extends SavedData {

    private int solarTermsDay = (ServerConfig.Season.initialSolarTermIndex.get() - 1) * ServerConfig.Season.lastingDaysOfEachTerm.get();
    private int solarTermsTicks = 0;
    private float snowLayer = 0.0f;
    private float sendSnowLayer = 0.0f;
    private boolean updateSnow = false;


    public GlobalDataManager() {
    }

    public GlobalDataManager(CompoundTag nbt) {
        this();
        setSolarTermsDay(nbt.getInt("SolarTermsDay"));
        setSolarTermsTicks(nbt.getInt("SolarTermsTicks"));
        setSnowLayer(nbt.getFloat("SnowDepth"));
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compound) {
        compound.putInt("SolarTermsDay", getSolarTermsDay());
        compound.putInt("SolarTermsTicks", getSolarTermsTicks());
        compound.putFloat("SnowDepth", getSnowLayer());
        return compound;
    }

    public static GlobalDataManager get(ServerLevel worldIn) {
        ServerLevel world = (ServerLevel) worldIn;
        DimensionDataStorage storage = world.getDataStorage();
        return storage.computeIfAbsent(GlobalDataManager::new, GlobalDataManager::new, Ecliptic.MODID);
    }


    public static GlobalDataManager get(ClientLevel worldIn) {
        return  new GlobalDataManager();
    }



    public void updateTicks(ServerLevel world) {
        solarTermsTicks++;
        int dayTime = Math.toIntExact(world.getDayTime() % 24000);
        if (solarTermsTicks > dayTime + 100) {
            solarTermsDay++;
            solarTermsDay %= 24 * ServerConfig.Season.lastingDaysOfEachTerm.get();

            BiomeClimateManager.updateTemperature(world,getSolarTermIndex());
            sendUpdateMessage(world);
        }
        solarTermsTicks = dayTime;
        var snow = WeatherManager.getSnowStatus(world, null, null);
        if (snow == WeatherManager.SnowRenderStatus.SNOW) {
            snowLayer = Math.min(1, snowLayer + 3.33e-5f);
        } else if (snow == WeatherManager.SnowRenderStatus.SNOW_MELT) {
            snowLayer = Math.max(0, snowLayer - 3.33e-5f);
        }
        if (snowLayer != sendSnowLayer) {
            if (snowLayer * 1000 % 10 == 0) {
                sendUpdateOnly(world);
                sendSnowLayer = snowLayer;
                updateSnow = true;
            }

        }
        if (updateSnow && world.getDayTime() % 1000 == 0) {
            // player.connection.send();
            var a = new ArrayList<ChunkAccess>();
            for (ChunkHolder chunk : (world).getChunkSource().chunkMap.getChunks()) {
                var cs = chunk.getLastAvailable();
                if (cs != null)
                    a.add(chunk.getLastAvailable());
            }
            // 强制刷新
            world.getChunkSource().chunkMap.resendBiomesForChunks(a);
            updateSnow=false;
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

    public float getSnowLayer() {
        return snowLayer;
    }

    public void setSnowLayer(float snowLayer) {
        this.snowLayer = snowLayer;
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
