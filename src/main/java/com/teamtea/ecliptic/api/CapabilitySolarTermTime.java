package com.teamtea.ecliptic.api;


import com.teamtea.ecliptic.common.core.solar.SolarDataManager;
import net.minecraftforge.common.capabilities.*;

public class CapabilitySolarTermTime {
    public static Capability<SolarDataManager> WORLD_SOLAR_TIME = CapabilityManager.get(new CapabilityToken<>() {
    });

    // public static void register(RegisterCapabilitiesEvent event) {
    //     // event.register(Data.class);
    // }

}
