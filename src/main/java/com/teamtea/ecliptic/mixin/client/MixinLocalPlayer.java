package com.teamtea.ecliptic.mixin.client;


import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.ecliptic.client.sound.SeasonalBiomeAmbientSoundsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({LocalPlayer.class})
public abstract class MixinLocalPlayer {

    @Shadow @Final private List<AmbientSoundHandler> ambientSoundHandlers;

    @Inject(at = {@At("RETURN")}, method = {"<init>"})
    private void mixin_init(CallbackInfo ci, @Local Minecraft minecraft, @Local ClientLevel clientLevel) {
        if(clientLevel.dimensionType().natural())
       ambientSoundHandlers.add(new SeasonalBiomeAmbientSoundsHandler((LocalPlayer) (Object)this, minecraft.getSoundManager(), clientLevel.getBiomeManager()));
    }
}
