package cloud.lemonslice.teastory.network;


import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.client.color.season.BiomeColorsHandler;

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
            }
        });
    }
}
