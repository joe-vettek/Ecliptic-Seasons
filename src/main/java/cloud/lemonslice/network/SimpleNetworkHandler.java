package cloud.lemonslice.network;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import xueluoanping.ecliptic.Ecliptic;

import java.util.function.Function;

public final class SimpleNetworkHandler
{
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Ecliptic.MODID, "main"))
            .networkProtocolVersion(() -> Ecliptic.NETWORK_VERSION)
            .serverAcceptedVersions(Ecliptic.NETWORK_VERSION::equals)
            .clientAcceptedVersions(Ecliptic.NETWORK_VERSION::equals)
            .simpleChannel();

    public static void init()
    {
        int id = 0;
        registerMessage(id++, SolarTermsMessage.class, SolarTermsMessage::new);
    }

    private static <T extends INormalMessage> void registerMessage(int index, Class<T> messageType, Function<FriendlyByteBuf, T> decoder)
    {
        CHANNEL.registerMessage(index, messageType, INormalMessage::toBytes, decoder, (message, context) ->
        {
            message.process(context);
            context.get().setPacketHandled(true);
        });
    }

}
