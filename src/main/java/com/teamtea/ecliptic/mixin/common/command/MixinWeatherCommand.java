package com.teamtea.ecliptic.mixin.common.command;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.teamtea.ecliptic.common.command.CommandHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.WeatherCommand;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WeatherCommand.class)
public class MixinWeatherCommand {

    @Inject(method = "setClear", at = @At(value = "HEAD"), cancellable = true)
    private static void mixin$setClear(CommandSourceStack p_139173_, int p_139174_, CallbackInfoReturnable<Integer> cir) {
        try {
            CommandHandler.setBiomeRain(p_139173_,CommandHandler.ALL_BIOME_RESULT,false,false);
            cir.setReturnValue(0);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            cir.setReturnValue(1);
        }

    }
    @Inject(method = "setRain", at = @At(value = "HEAD"), cancellable = true)
    private static void mixin$setRain(CommandSourceStack p_139173_, int p_139174_, CallbackInfoReturnable<Integer> cir) {
        try {
            CommandHandler.setBiomeRain(p_139173_,CommandHandler.ALL_BIOME_RESULT,true,false);
            cir.setReturnValue(0);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            cir.setReturnValue(1);
        }
    }

    @Inject(method = "setThunder", at = @At(value = "HEAD"), cancellable = true)
    private static void mixin$setThunder(CommandSourceStack p_139173_, int p_139174_, CallbackInfoReturnable<Integer> cir) {
        try {
            CommandHandler.setBiomeRain(p_139173_,CommandHandler.ALL_BIOME_RESULT,true,true);
            cir.setReturnValue(0);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            cir.setReturnValue(1);
        }
    }
}
