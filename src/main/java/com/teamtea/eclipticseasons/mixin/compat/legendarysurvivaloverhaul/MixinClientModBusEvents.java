package com.teamtea.eclipticseasons.mixin.compat.legendarysurvivaloverhaul;


import com.teamtea.eclipticseasons.compat.legendarysurvivaloverhaul.ESRenderSeasonCards;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.client.events.ClientModBusEvents;
import sfiomn.legendarysurvivaloverhaul.config.Config;

@Mixin({ClientModBusEvents.class})
public abstract class MixinClientModBusEvents {

    @Inject(
            remap = false,
            method = "registerGuiOverlays",
            at = @At(value = "HEAD")
    )
    private static void mixin_registerGuiOverlays(RegisterGuiOverlaysEvent event, CallbackInfo ci) {
        event.registerAbove(VanillaGuiOverlay.ITEM_NAME.id(), "season_card_patch", ESRenderSeasonCards.SEASON_CARD_GUI);
    }
}
