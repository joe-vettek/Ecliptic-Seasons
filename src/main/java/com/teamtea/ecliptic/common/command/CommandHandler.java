package com.teamtea.ecliptic.common.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import com.teamtea.ecliptic.common.core.solar.GlobalDataManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public class CommandHandler {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal(Ecliptic.MODID).
                then(Commands.literal("solar")
                        .requires((source) -> source.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.argument("day", IntegerArgumentType.integer())
                                        .executes(commandContext -> setDay(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "day")))))
                        .then(Commands.literal("add")
                                .then(Commands.argument("day", IntegerArgumentType.integer()).executes(commandContext -> addDay(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "day"))))))
                .then(Commands.literal("biomeweather")
                        .requires((source) -> source.hasPermission(2))
                        .then(Commands.argument("biome", ResourceOrTagArgument.resourceOrTag(event.getBuildContext(), Registries.BIOME))
                                .then(Commands.literal("rain")
                                        .executes((commandContext) -> setBiomeRain(commandContext.getSource(), ResourceOrTagArgument.getResourceOrTag(commandContext, "biome", Registries.BIOME), true)))
                                .then(Commands.literal("clear")
                                        .executes((commandContext) -> setBiomeRain(commandContext.getSource(), ResourceOrTagArgument.getResourceOrTag(commandContext, "biome", Registries.BIOME), false)))
                        )
                )
        );
    }

    private static int setBiomeRain(CommandSourceStack sourceStack, ResourceOrTagArgument.Result<Biome> result, boolean setRain) throws CommandSyntaxException {
        var list = WeatherManager.getBiomeList(sourceStack.getLevel());
        if (list != null) {
            boolean found=false;
            for (WeatherManager.BiomeWeather biomeWeather : list) {
                if (result.test(biomeWeather.biomeHolder)) {
                    biomeWeather.rainTime = setRain ? ServerLevel.RAIN_DURATION.sample(sourceStack.getLevel().getRandom()) : 0;
                    biomeWeather.clearTime = setRain ? 0 : ServerLevel.RAIN_DELAY.sample(sourceStack.getLevel().getRandom());

                    found=true;
                }
            }
            if(found){
                WeatherManager.sendBiomePacket(list, sourceStack.getLevel().players());
            }
        }
        return 0;
    }

    private static int getDay(ServerLevel worldIn) {
        return AllListener.getSaveDataLazy(worldIn).map(GlobalDataManager::getSolarTermsDay).orElse(0);
    }

    public static int setDay(CommandSourceStack source, int day) {
        for (ServerLevel ServerLevel : source.getServer().getAllLevels()) {
            AllListener.getSaveDataLazy(ServerLevel).ifPresent(data ->
            {
                data.setSolarTermsDay(day);
                data.sendUpdateMessage(ServerLevel);
            });
        }

        source.sendSuccess(() -> Component.translatable("commands.teastory.solar.set", day), true);
        return getDay(source.getLevel());
    }

    public static int addDay(CommandSourceStack source, int add) {
        for (ServerLevel ServerLevel : source.getServer().getAllLevels()) {
            AllListener.getSaveDataLazy(ServerLevel).ifPresent(data ->
            {
                data.setSolarTermsDay(data.getSolarTermsDay() + add);
                data.sendUpdateMessage(ServerLevel);
                source.sendSuccess(() -> Component.translatable("commands.teastory.solar.set", data.getSolarTermsDay()), true);
            });
        }
        return getDay(source.getLevel());
    }
}
