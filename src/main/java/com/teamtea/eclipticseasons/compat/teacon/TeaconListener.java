package com.teamtea.eclipticseasons.compat.teacon;


import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
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

    @SubscribeEvent
    public static void onTeaconPlayerLoggedIn(EntityJoinLevelEvent event) {
        if (FMLLoader.getDist()== Dist.CLIENT) {
            TeaconCheckTool.updateCheck();
        }
    }
}
