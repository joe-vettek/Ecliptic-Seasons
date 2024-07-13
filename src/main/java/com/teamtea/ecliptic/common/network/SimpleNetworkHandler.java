package com.teamtea.ecliptic.common.network;


import com.teamtea.ecliptic.Ecliptic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;

public final class SimpleNetworkHandler {
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(Ecliptic.rl("main"))
            .networkProtocolVersion(() -> Ecliptic.NETWORK_VERSION)
            .serverAcceptedVersions(Ecliptic.NETWORK_VERSION::equals)
            .clientAcceptedVersions(Ecliptic.NETWORK_VERSION::equals)
            .simpleChannel();

    public static void init() {
        int id = 0;
        // registerMessage(id++, SolarTermsMessage.class, SolarTermsMessage::new);
        // registerMessage(id++, BiomeWeatherMessage.class, BiomeWeatherMessage::new);
        var a = CHANNEL.messageBuilder(SolarTermsMessage.class, id++)
                .encoder(SolarTermsMessage::toBytes)
                .decoder(SolarTermsMessage::new);
        if (FMLLoader.getDist() == Dist.CLIENT)
            a.consumerNetworkThread(NetworkdUtil::processSolarTermsMessage);
        a.add();
        var c = CHANNEL.messageBuilder(BiomeWeatherMessage.class, id++)
                .encoder(BiomeWeatherMessage::toBytes)
                .decoder(BiomeWeatherMessage::new);
        if (FMLLoader.getDist() == Dist.CLIENT)
            c.consumerNetworkThread(NetworkdUtil::processBiomeWeatherMessage);
        c.add();
    }

    // private static <T extends INormalMessage> void registerMessage(int index, Class<T> messageType, Function<FriendlyByteBuf, T> decoder) {
    //     CHANNEL.registerMessage(index, messageType, INormalMessage::toBytes, decoder, (message, context) ->
    //     {
    //
    //
    //         message.process(context);
    //         context.get().setPacketHandled(true);
    //     });
    // }


    public static <MSG> void send(ServerPlayer player, MSG msg) {
        SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void send(List<ServerPlayer> player, MSG msg) {
        player.forEach(serverPlayer -> send(serverPlayer, msg));
    }
}
