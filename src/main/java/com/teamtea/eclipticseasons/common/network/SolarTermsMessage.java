package com.teamtea.eclipticseasons.common.network;


import net.minecraft.network.PacketBuffer ;

public class SolarTermsMessage {
    public int solarDay;

    public SolarTermsMessage(int solarDay) {
        this.solarDay = solarDay;
    }

    public SolarTermsMessage(PacketBuffer  buf) {
        solarDay = buf.readInt();
    }


    public void toBytes(PacketBuffer  buf) {
        buf.writeInt(solarDay);
    }


}
