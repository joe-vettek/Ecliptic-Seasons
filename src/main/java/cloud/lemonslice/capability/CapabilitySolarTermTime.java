package cloud.lemonslice.capability;


import net.minecraftforge.common.capabilities.*;

public class CapabilitySolarTermTime {
    public static Capability<SolarData> WORLD_SOLAR_TIME = CapabilityManager.get(new CapabilityToken<>() {
    });

    // public static void register(RegisterCapabilitiesEvent event) {
    //     // event.register(Data.class);
    // }

}
