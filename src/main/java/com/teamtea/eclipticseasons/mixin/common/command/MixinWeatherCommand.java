package com.teamtea.eclipticseasons.mixin.common.command;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.teamtea.eclipticseasons.common.command.CommandHandler;

import net.minecraft.command.CommandSource;
import net.minecraft.command.impl.WeatherCommand;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherCommand.class)
public class MixinWeatherCommand {

    @Inject(method = "setClear", at = @At(value = "HEAD"), cancellable = true)
    private static void mixin$setClear(CommandSource source, int p_198869_1_, CallbackInfoReturnable<Integer> cir) {
        source.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).iterator()
                .forEachRemaining(biome -> {
                    try {
                        CommandHandler.setBiomeRain(source, source.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome), false, false);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
        cir.setReturnValue(0);
    }

    @Inject(method = "setRain", at = @At(value = "HEAD"), cancellable = true)
    private static void mixin$setRain(CommandSource source, int p_139174_, CallbackInfoReturnable<Integer> cir) {
        source.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).iterator()
                .forEachRemaining(biome -> {
                    try {
                        CommandHandler.setBiomeRain(source, source.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome), true, false);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
        cir.setReturnValue(0);
    }

    @Inject(method = "setThunder", at = @At(value = "HEAD"), cancellable = true)
    private static void mixin$setThunder(CommandSource source, int p_139174_, CallbackInfoReturnable<Integer> cir) {
        source.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).iterator()
                .forEachRemaining(biome -> {
                    try {
                        CommandHandler.setBiomeRain(source, source.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome), true, true);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
        cir.setReturnValue(0);
    }
}
