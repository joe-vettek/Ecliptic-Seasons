package com.teamtea.ecliptic.common.core;

import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class SolarProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public final SolarDataRunner worldSolarTime = new SolarDataRunner();
    // private final Capability.IStorage<Data> storage = WORLD_SOLAR_TIME.getStorage();


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("SolarTermsDay", worldSolarTime.getSolarTermsDay());
        compound.putInt("SolarTermsTicks", worldSolarTime.getSolarTermsTicks());
        compound.putFloat("SnowDepth", worldSolarTime.getSnowLayer());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        worldSolarTime.setSolarTermsDay(nbt.getInt("SolarTermsDay"));
        worldSolarTime.setSolarTermsTicks(nbt.getInt("SolarTermsTicks"));
        worldSolarTime.setSnowLayer(nbt.getFloat("SnowDepth"));
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
        if (cap.equals(CapabilitySolarTermTime.WORLD_SOLAR_TIME))
            return LazyOptional.of(() -> worldSolarTime).cast();
        else
            return LazyOptional.empty();
    }
}
