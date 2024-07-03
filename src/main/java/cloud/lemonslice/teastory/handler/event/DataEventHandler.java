package cloud.lemonslice.teastory.handler.event;


import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.config.ServerConfig;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import cloud.lemonslice.teastory.network.SimpleNetworkHandler;
import cloud.lemonslice.teastory.network.SolarTermsMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import xueluoanping.ecliptic.Ecliptic;


@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public final class DataEventHandler {
    public static LazyOptional<CapabilitySolarTermTime.Provider> provider = LazyOptional.empty();

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {

    }

    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<Level> event) {
        if (ServerConfig.Season.enable.get() && event.getObject().dimension() == Level.OVERWORLD) {
            var cc = new CapabilitySolarTermTime.Provider();
            provider = LazyOptional.of(() -> cc);
            event.addCapability(new ResourceLocation(Ecliptic.MODID, "world_solar_terms"), cc);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.getEntity() instanceof ServerPlayer && !(event.getEntity() instanceof FakePlayer)) {
            if (ServerConfig.Season.enable.get()) {
                event.getEntity().level().getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).ifPresent(t ->
                {
                    SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SolarTermsMessage(t.getSolarTermsDay()));
                    if (t.getSolarTermsDay() % ServerConfig.Season.lastingDaysOfEachTerm.get() == 0) {
                        ((ServerPlayer) event.getEntity()).sendSystemMessage(Component.translatable("info.teastory.environment.solar_term.message", SolarTerm.get(t.getSolarTermIndex()).getAlternationText()), false);
                    }
                });
            }
        }
    }
}
