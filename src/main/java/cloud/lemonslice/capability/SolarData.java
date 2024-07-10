package cloud.lemonslice.capability;

import cloud.lemonslice.environment.solar.BiomeTemperatureManager;
import cloud.lemonslice.environment.solar.SolarTerm;
import cloud.lemonslice.network.SolarTermsMessage;
import cloud.lemonslice.config.ServerConfig;
import cloud.lemonslice.network.SimpleNetworkHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import xueluoanping.ecliptic.util.ServerWeatherChecker;

import java.util.ArrayList;

public class SolarData {
    private int solarTermsDay = (ServerConfig.Season.initialSolarTermIndex.get() - 1) * ServerConfig.Season.lastingDaysOfEachTerm.get();
    private int solarTermsTicks = 0;
    private float snowLayer = 0.0f;
    private float sendSnowLayer = 0.0f;
    private boolean updateSnow = false;

    public void updateTicks(ServerLevel world) {
        solarTermsTicks++;
        int dayTime = Math.toIntExact(world.getDayTime() % 24000);
        if (solarTermsTicks > dayTime + 100) {
            solarTermsDay++;
            solarTermsDay %= 24 * ServerConfig.Season.lastingDaysOfEachTerm.get();
            ForgeRegistries.BIOMES.forEach(biome ->
            {
                var temperature = BiomeTemperatureManager.getDefaultTemperature(biome) + SolarTerm.get(getSolarTermIndex()).getTemperatureChange();
                var oldClimateSettings = biome.climateSettings;
                biome.climateSettings = new Biome.ClimateSettings(
                        oldClimateSettings.hasPrecipitation(),
                        temperature,
                        oldClimateSettings.temperatureModifier(),
                        oldClimateSettings.downfall());
            });
            sendUpdateMessage(world);
        }
        solarTermsTicks = dayTime;
        var snow = ServerWeatherChecker.getSnowStatus(world, null, null);
        if (snow == ServerWeatherChecker.SnowRenderStatus.SNOW) {
            snowLayer = Math.min(1, snowLayer + 3.33e-5f);
        } else if (snow == ServerWeatherChecker.SnowRenderStatus.SNOW_MELT) {
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
            // Ecliptic.logger(world.getDayTime(), "滴滴滴");
            updateSnow=false;
        }

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
    }

    public void setSolarTermsTicks(int solarTermsTicks) {
        this.solarTermsTicks = solarTermsTicks;
    }

    public float getSnowLayer() {
        return snowLayer;
    }

    public void setSnowLayer(float snowLayer) {
        this.snowLayer = snowLayer;
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
                // MutableObject<ClientboundLevelChunkWithLightPacket> mutableobject = new MutableObject<>();
                // var map = world.getChunkSource().chunkMap;
                // for (ChunkHolder chunkholder : map.getChunks()) {
                //     if (chunkholder.getFullChunk() != null)
                //         map.playerLoadedChunk(player, mutableobject, chunkholder.getFullChunk());
                // }


            }
        }
        // world.getChunkSource().tick(()->false,true);


// ;          var map=world.getChunkSource().chunkMap;
//             for(ChunkHolder chunkholder : map.getChunks()) {
//                 ChunkPos chunkpos = chunkholder.getPos();
//                 MutableObject<ClientboundLevelChunkWithLightPacket> mutableobject = new MutableObject<>();
//                 map.getPlayers(chunkpos, false).forEach((p_214864_) -> {
//                     SectionPos sectionpos = p_214864_.getLastSectionPos();
//                     // boolean flag = ChunkMap.isChunkInRange(chunkpos.x, chunkpos.z, sectionpos.x(), sectionpos.z(), j);
//                     // boolean flag1 = ChunkMap.isChunkInRange(chunkpos.x, chunkpos.z, sectionpos.x(), sectionpos.z(), map.viewDistance);
//                     map.updateChunkTracking(p_214864_, chunkpos, mutableobject, true, false);
//                 });
//             }
    }
}
