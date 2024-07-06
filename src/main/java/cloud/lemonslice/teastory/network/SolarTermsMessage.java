package cloud.lemonslice.teastory.network;


import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.client.color.season.BiomeColorsHandler;

import cloud.lemonslice.teastory.config.ServerConfig;
import cloud.lemonslice.teastory.environment.solar.BiomeTemperatureManager;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import com.google.common.collect.Queues;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Queue;
import java.util.function.Supplier;

public class SolarTermsMessage implements INormalMessage {
    int solarDay;

    public SolarTermsMessage(int solarDay) {
        this.solarDay = solarDay;
    }

    public SolarTermsMessage(FriendlyByteBuf buf) {
        solarDay = buf.readInt();
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(solarDay);
    }


    public void process(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->
        {
            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {

                Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(data ->
                        {
                            data.setSolarTermsDay(solarDay);
                            ForgeRegistries.BIOMES.forEach(biome ->
                                    {
                                        var temperature = BiomeTemperatureManager.getDefaultTemperature(biome) + SolarTerm.get(data.getSolarTermIndex()).getTemperatureChange();
                                        var oldClimateSettings = biome.climateSettings;
                                        biome.climateSettings = new Biome.ClimateSettings(
                                                oldClimateSettings.hasPrecipitation(),
                                                temperature,
                                                oldClimateSettings.temperatureModifier(),
                                                oldClimateSettings.downfall());
                                    }
                            );
                            BiomeColorsHandler.needRefresh = true;
                        }
                );
                try {
                    if (Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).resolve().get().getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {

                        var cc = Minecraft.getInstance().levelRenderer;
                        // cc.allChanged();
                        // cc.needsUpdate();
                        // cc.recentlyCompiledChunks.clear();
                        // cc.renderChunksInFrustum.clear();
                        // var p_194339_ = Minecraft.getInstance().gameRenderer.getMainCamera();
                        // Vec3 vec3 = p_194339_.getPosition();
                        // Queue<LevelRenderer.RenderChunkInfo> queue1 = Queues.newArrayDeque();
                        // cc.initializeQueueForFullUpdate(p_194339_, queue1);
                        // LevelRenderer.RenderChunkStorage levelrenderer$renderchunkstorage1 = new LevelRenderer.RenderChunkStorage(cc.viewArea.chunks.length);
                        // cc.updateRenderChunks(levelrenderer$renderchunkstorage1.renderChunks, levelrenderer$renderchunkstorage1.renderInfoMap, vec3, queue1, false);
                        // cc.renderChunkStorage.set(levelrenderer$renderchunkstorage1);
                        // cc.needsFrustumUpdate.set(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
