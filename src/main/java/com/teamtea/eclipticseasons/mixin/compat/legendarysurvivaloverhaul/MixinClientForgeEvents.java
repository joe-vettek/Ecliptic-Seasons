package com.teamtea.eclipticseasons.mixin.compat.legendarysurvivaloverhaul;


import com.teamtea.eclipticseasons.compat.legendarysurvivaloverhaul.ESRenderSeasonCards;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.client.events.ClientForgeEvents;
import sfiomn.legendarysurvivaloverhaul.config.Config;

@Mixin({ClientForgeEvents.class})
public abstract class MixinClientForgeEvents {

    @Inject(
            remap = false,
            method = "onClientTick",
            at = @At(value = "HEAD")
    )
    private static void mixin_lambda$static$0(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = Minecraft.getInstance().player;
            if (!Minecraft.getInstance().isPaused() && player != null) {
                if (Config.Baked.seasonCardsEnabled) {
                    ESRenderSeasonCards.updateSeasonCardFading(player);
                }
            }
        }
    }

    @Inject(
            remap = false,
            method = "onPlayerJoinWorld",
            at = @At(value = "HEAD")
    )
    private static void mixin_onPlayerJoinWorld(EntityJoinLevelEvent event, CallbackInfo ci) {
        if (event.getLevel().isClientSide && event.getEntity() instanceof Player) {
            ESRenderSeasonCards.init();
        }
    }
}
