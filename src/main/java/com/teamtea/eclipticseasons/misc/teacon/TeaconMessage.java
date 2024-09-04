package com.teamtea.eclipticseasons.misc.teacon;


import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class TeaconMessage implements CustomPacketPayload {
    public boolean teacon;

    public TeaconMessage(boolean teacon) {
        this.teacon = teacon;
    }


    public static final Type<TeaconMessage> TYPE = new Type<>(EclipticSeasonsMod.rl("teacon"));

    public static final StreamCodec<ByteBuf, TeaconMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            solarTermsMessage -> solarTermsMessage.teacon,
            TeaconMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
