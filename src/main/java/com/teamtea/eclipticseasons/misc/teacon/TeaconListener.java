package com.teamtea.eclipticseasons.misc.teacon;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = EclipticSeasonsApi.MODID)
public class TeaconListener {
    @SubscribeEvent
    public static void onTeaconPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            try {
                TeaconNetworkHandler.send(serverPlayer, new TeaconMessage(TeaconCheckTool.isOnTeaconServer()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
