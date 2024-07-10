package cloud.lemonslice.environment.solar;


import cloud.lemonslice.capability.CapabilitySolarTermTime;
import cloud.lemonslice.config.ServerConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xueluoanping.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public class WorldSolarTermManager {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
// Ecliptic.logger(event.level);
        if (event.phase.equals(TickEvent.Phase.END) && ServerConfig.Season.enable.get() && !event.level.isClientSide() && event.level.dimension() == Level.OVERWORLD) {
            event.level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(data ->
            {
                if (!event.level.players().isEmpty()) {
                    data.updateTicks((ServerLevel) event.level);
                }
            });
        }
    }
}
