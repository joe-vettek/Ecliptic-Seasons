package com.teamtea.ecliptic.common.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import com.teamtea.ecliptic.common.core.solar.SolarDataManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.ecliptic.Ecliptic;

import java.util.List;
import java.util.Optional;

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

    public static int setBiomeRain(CommandSourceStack sourceStack, ResourceOrTagArgument.Result<Biome> result, boolean setRain) throws CommandSyntaxException {
        var levelBiomeWeather = WeatherManager.getBiomeList(sourceStack.getLevel());
        if (levelBiomeWeather != null) {
            boolean found = false;
            int size = levelBiomeWeather.size();
            for (WeatherManager.BiomeWeather biomeWeather : levelBiomeWeather) {
                if (result.test(biomeWeather.biomeHolder)) {
                    biomeWeather.rainTime = setRain ? ServerLevel.RAIN_DURATION.sample(sourceStack.getLevel().getRandom()) / size : 0;
                    biomeWeather.clearTime = setRain ? 0 : ServerLevel.RAIN_DURATION.sample(sourceStack.getLevel().getRandom()) / size;

                    found = true;
                }
            }
            if (found) {
                WeatherManager.sendBiomePacket(levelBiomeWeather, sourceStack.getLevel().players());
            }
        }
        return 0;
    }

    private static int getDay(ServerLevel worldIn) {
        return AllListener.getSaveDataLazy(worldIn).map(SolarDataManager::getSolarTermsDay).orElse(0);
    }

    public static int setDay(CommandSourceStack source, int day) {
        for (ServerLevel ServerLevel : List.of(source.getLevel())) {
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
        for (ServerLevel ServerLevel : List.of(source.getLevel())) {
            AllListener.getSaveDataLazy(ServerLevel).ifPresent(data ->
            {
                data.setSolarTermsDay(data.getSolarTermsDay() + add);
                data.sendUpdateMessage(ServerLevel);
                source.sendSuccess(() -> Component.translatable("commands.teastory.solar.set", data.getSolarTermsDay()), true);
            });
        }
        return getDay(source.getLevel());
    }


    public static final ResourceOrTagArgument.Result<Biome> ALL_BIOME_RESULT = new ResourceOrTagArgument.Result<Biome>() {
        @Override
        public boolean test(Holder<Biome> biomeHolder) {
            return true;
        }

        @Override
        public Either<Holder.Reference<Biome>, HolderSet.Named<Biome>> unwrap() {
            return null;
        }

        @Override
        public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> p_249572_) {
            return Optional.empty();
        }

        @Override
        public String asPrintable() {
            return null;
        }
    };
}
