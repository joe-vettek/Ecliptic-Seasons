package cloud.lemonslice.teastory.capability;


import cloud.lemonslice.teastory.config.ServerConfig;
import cloud.lemonslice.teastory.environment.solar.BiomeTemperatureManager;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import cloud.lemonslice.teastory.network.SimpleNetworkHandler;
import cloud.lemonslice.teastory.network.SolarTermsMessage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilitySolarTermTime {
    public static Capability<Data> WORLD_SOLAR_TIME = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(Data.class);
    }

    // public static class Storage implements Capability.IStorage<Data> {
    //     @Nullable
    //     @Override
    //     public INBT writeNBT(Capability<Data> capability, Data instance, Direction side) {
    //         CompoundNBT compound = new CompoundNBT();
    //         compound.putInt("SolarTermsDay", instance.getSolarTermsDay());
    //         compound.putInt("SolarTermsTicks", instance.getSolarTermsTicks());
    //         return compound;
    //     }
    //
    //     @Override
    //     public void readNBT(Capability<Data> capability, Data instance, Direction side, INBT nbt) {
    //         instance.setSolarTermsDay(((CompoundNBT) nbt).getInt("SolarTermsDay"));
    //         instance.setSolarTermsTicks(((CompoundNBT) nbt).getInt("SolarTermsTicks"));
    //     }
    // }

    public static class Data {
        private int solarTermsDay = (ServerConfig.Season.initialSolarTermIndex.get() - 1) * ServerConfig.Season.lastingDaysOfEachTerm.get();
        private int solarTermsTicks = 0;

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

        public void sendUpdateMessage(ServerLevel world) {
            for (ServerPlayer player : world.getServer().getPlayerList().getPlayers()) {
                SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SolarTermsMessage(solarTermsDay));
                if (getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                    player.sendSystemMessage(Component.translatable("info.teastory.environment.solar_term.message", SolarTerm.get(getSolarTermIndex()).getAlternationText()), false);
                }
            }
        }
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public final Data worldSolarTime = new Data();
        // private final Capability.IStorage<Data> storage = WORLD_SOLAR_TIME.getStorage();


        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.putInt("SolarTermsDay", worldSolarTime.getSolarTermsDay());
            compound.putInt("SolarTermsTicks", worldSolarTime.getSolarTermsTicks());
            return compound;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            worldSolarTime.setSolarTermsDay(((CompoundTag) nbt).getInt("SolarTermsDay"));
            worldSolarTime.setSolarTermsTicks(((CompoundTag) nbt).getInt("SolarTermsTicks"));
        }



        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
            if (cap.equals(WORLD_SOLAR_TIME))
                return LazyOptional.of(() -> worldSolarTime).cast();
            else
                return LazyOptional.empty();
        }
    }
}
