package cloud.lemonslice.teastory.network;


import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.capability.SolarData;
import cloud.lemonslice.teastory.client.color.season.BiomeColorsHandler;

import cloud.lemonslice.teastory.config.ServerConfig;
import cloud.lemonslice.teastory.environment.solar.BiomeTemperatureManager;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

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

    public SolarTermsMessage(SolarData solarData) {
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
