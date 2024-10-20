package com.teamtea.eclipticseasons.common.core.solar;

import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.network.SimpleNetworkHandler;
import com.teamtea.eclipticseasons.common.network.SolarTermsMessage;
import com.teamtea.eclipticseasons.config.ServerConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import oculus.org.antlr.v4.runtime.misc.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SolarDataManager extends WorldSavedData {

    protected int solarTermsDay = (ServerConfig.Season.initialSolarTermIndex.get() - 1) * ServerConfig.Season.lastingDaysOfEachTerm.get();
    protected int solarTermsTicks = 0;

    protected WeakReference<World> levelWeakReference;

    public SolarDataManager(World level) {
        super(EclipticSeasons.MODID);
        levelWeakReference = new WeakReference<>(level);
    }

    public SolarDataManager(World level, CompoundNBT nbt) {
        this(level);
        setSolarTermsDay(nbt.getInt("SolarTermsDay"));
        setSolarTermsTicks(nbt.getInt("SolarTermsTicks"));
        ListNBT listTag = nbt.getList("biomes", Constants.NBT.TAG_COMPOUND);
        if (levelWeakReference.get() != null) {
            ArrayList<WeatherManager.BiomeWeather> biomeWeathers =WeatherManager.getBiomeList(levelWeakReference.get());
            for (int i = 0; i < listTag.size(); i++) {
                String location = listTag.getCompound(i).getString("biome");
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
    public void load(CompoundNBT p_76184_1_) {

    }

    @Override
    public @NotNull CompoundNBT save(CompoundNBT compound) {
        compound.putInt("SolarTermsDay", getSolarTermsDay());
        compound.putInt("SolarTermsTicks", getSolarTermsTicks());
        ListNBT listTag = new ListNBT();
        if (levelWeakReference.get() != null) {
            ArrayList<WeatherManager.BiomeWeather> list = WeatherManager.getBiomeList(levelWeakReference.get());
            for (WeatherManager.BiomeWeather biomeWeather : list) {
                listTag.add(biomeWeather.serializeNBT());
            }
        }
        compound.put("biomes", listTag);
        return compound;
    }
    
    public static SolarDataManager get(ServerWorld serverLevel) {
        DimensionSavedDataManager storage = serverLevel.getDataStorage();
        return storage.computeIfAbsent(() -> new SolarDataManager(serverLevel), EclipticSeasons.MODID);
    }


    public void updateTicks(ServerWorld world) {
        solarTermsTicks++;
        int dayTime = Math.toIntExact(world.getDayTime() % 24000);
        if (solarTermsTicks > dayTime + 100) {
            solarTermsDay++;
            solarTermsDay %= 24 * ServerConfig.Season.lastingDaysOfEachTerm.get();

            BiomeClimateManager.updateTemperature(world, getSolarTermIndex());
            sendUpdateMessage(world);
        }
        solarTermsTicks = dayTime;

        // 强制刷新，由于服务器区块是悲观锁，所以不能强刷
        // if (world.getRandom().nextBoolean() && world.getDayTime() % 1000 == 0) {
        //     // player.connection.send();
        //     var a = new ArrayList<ChunkAccess>();
        //     for (ChunkHolder chunk : (world).getChunkSource().chunkMap.getChunks()) {
        //         var cs = chunk.getLastAvailable();
        //         if (cs != null)
        //             a.add(chunk.getLastAvailable());
        //     }
        //
        //     world.getChunkSource().chunkMap.resendBiomesForChunks(a);
        // }

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

    public void sendUpdateMessage(ServerWorld world) {
        for (ServerPlayerEntity player : world.players()) {
            SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SolarTermsMessage(this.getSolarTermsDay()));
            if (getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                player.sendMessage(new TranslationTextComponent("info.teastory.environment.solar_term.message", SolarTerm.get(getSolarTermIndex()).getAlternationText()),  Util.NIL_UUID);
            }
        }
    }



}
