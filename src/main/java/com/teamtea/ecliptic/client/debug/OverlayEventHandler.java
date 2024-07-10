package com.teamtea.ecliptic.client.debug;


import com.teamtea.ecliptic.api.CapabilitySolarTermTime;
import com.teamtea.ecliptic.core.SolarDataRunner;
import com.teamtea.ecliptic.config.ClientConfig;
import com.teamtea.ecliptic.core.AsmHandler;
import com.teamtea.ecliptic.api.biome.Humidity;
import net.minecraft.client.Minecraft;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Ecliptic.MODID)
public final class OverlayEventHandler {
    public final static ResourceLocation DEFAULT = new ResourceLocation("minecraft", "textures/gui/icons.png");
    private final static DebugInfoRenderer BAR_4 = new DebugInfoRenderer(Minecraft.getInstance());

    @SubscribeEvent(receiveCanceled = true)
    public static void onEvent(RenderGuiOverlayEvent.Pre event) {


        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null) {
            // if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
            // if(event.getOverlay().id().getPath().equals("all"))
            {
                if (ClientConfig.GUI.debugInfo.get()) {
                    int solar = clientPlayer.level().getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).orElse(new SolarDataRunner()).getSolarTermsDay();
                    long dayTime = clientPlayer.level().getDayTime();
                    float temp = clientPlayer.level().getBiome(clientPlayer.getOnPos()).get().getTemperature(clientPlayer.getOnPos());
                    Humidity h = Humidity.getHumid(clientPlayer.level().getBiome(clientPlayer.getOnPos()).get().getModifiedClimateSettings().downfall(), temp);
                    double env = clientPlayer.level().getBiome(clientPlayer.getOnPos()).get().getTemperature(clientPlayer.getOnPos());
                    int solarTime = AsmHandler.getSolarAngelTime(clientPlayer.level().getDayTime(), clientPlayer.level());

                    BAR_4.renderStatusBar(event.getGuiGraphics(), event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), solar, dayTime, env, solarTime);
                }
            }
        }
    }
}
