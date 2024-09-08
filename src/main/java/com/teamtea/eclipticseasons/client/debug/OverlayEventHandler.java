package com.teamtea.eclipticseasons.client.debug;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.common.core.SolarHolders;
import com.teamtea.eclipticseasons.config.ClientConfig;
import com.teamtea.eclipticseasons.common.core.solar.SolarAngelHelper;
import com.teamtea.eclipticseasons.api.constant.biome.Humidity;
import net.minecraft.client.Minecraft;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = EclipticSeasonsApi.MODID)
public final class OverlayEventHandler {
    public final static ResourceLocation DEFAULT = ResourceLocation.withDefaultNamespace( "textures/gui/icons.png");
    private final static DebugInfoRenderer BAR_4 = new DebugInfoRenderer(Minecraft.getInstance());

    @SubscribeEvent(receiveCanceled = true)
    public static void onEvent(RenderGuiEvent.Post event) {

        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        var level=Minecraft.getInstance().level;
        if (clientPlayer != null &&level!=null) {
            // if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
            // if(event.getOverlay().id().getPath().equals("all"))
            {
                if ((ClientConfig.GUI.debugInfo.get()
                        // || !FMLEnvironment.production
                )
                        &&!Minecraft.getInstance().options.hideGui
                )
                {
                    var solar = SolarHolders.getSaveDataLazy(clientPlayer.level()).get().getSolarTermsDay();
                    long dayTime = clientPlayer.level().getDayTime();
                    float temp = clientPlayer.level().getBiome(clientPlayer.getOnPos()).value().getTemperature(clientPlayer.getOnPos());
                    Humidity h = Humidity.getHumid(clientPlayer.level().getBiome(clientPlayer.getOnPos()).value().getModifiedClimateSettings().downfall(), temp);
                    double env = clientPlayer.level().getBiome(clientPlayer.getOnPos()).value().getTemperature(clientPlayer.getOnPos());
                    int solarTime = SolarAngelHelper.getSolarAngelTime(clientPlayer.level(), clientPlayer.level().getDayTime());

                    BAR_4.renderStatusBar(event.getGuiGraphics(), Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(),level,clientPlayer, solar+"", dayTime, env, solarTime);
                }
            }
        }
    }
}
