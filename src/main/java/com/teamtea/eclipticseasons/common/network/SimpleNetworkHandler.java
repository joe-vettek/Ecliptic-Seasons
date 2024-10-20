package com.teamtea.eclipticseasons.common.network;


import com.teamtea.eclipticseasons.EclipticSeasons;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;


import java.util.List;

public final class SimpleNetworkHandler {
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(EclipticSeasons.rl("main"))
            .networkProtocolVersion(() -> EclipticSeasons.NETWORK_VERSION)
            .serverAcceptedVersions(EclipticSeasons.NETWORK_VERSION::equals)
            .clientAcceptedVersions(EclipticSeasons.NETWORK_VERSION::equals)
            .simpleChannel();

    public static void init() {
        int id = 0;
        // registerMessage(id++, SolarTermsMessage.class, SolarTermsMessage::new);
        // registerMessage(id++, BiomeWeatherMessage.class, BiomeWeatherMessage::new);
        SimpleChannel.MessageBuilder<SolarTermsMessage> a = CHANNEL.messageBuilder(SolarTermsMessage.class, id++)
                .encoder(SolarTermsMessage::toBytes)
                .decoder(SolarTermsMessage::new);
        if (FMLLoader.getDist() == Dist.CLIENT)
            a.consumer(NetworkdUtil::processSolarTermsMessage);
        a.add();

        SimpleChannel.MessageBuilder<BiomeWeatherMessage> c = CHANNEL.messageBuilder(BiomeWeatherMessage.class, id++)
                .encoder(BiomeWeatherMessage::toBytes)
                .decoder(BiomeWeatherMessage::new);
        if (FMLLoader.getDist() == Dist.CLIENT)
            c.consumer(NetworkdUtil::processBiomeWeatherMessage);
        c.add();


        SimpleChannel.MessageBuilder<EmptyMessage> d = CHANNEL.messageBuilder(EmptyMessage.class, id++)
                .encoder(EmptyMessage::toBytes)
                .decoder(EmptyMessage::new);
        if (FMLLoader.getDist() == Dist.CLIENT)
            d.consumer(NetworkdUtil::processEmptyMessage);
        d.add();
    }

    private static void registerMessage(int i, Class<BiomeWeatherMessage> biomeWeatherMessageClass, Object o) {
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


    public static <MSG> void send(ServerPlayerEntity player, MSG msg) {
        SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void send(List<ServerPlayerEntity> players, MSG msg) {
        players.forEach(player -> {
            // if (player instanceof ServerPlayer serverPlayer)
            {
                send(player, msg);
            }
        });
    }
}
