package cloud.lemonslice.teastory.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface INormalMessage {
    void toBytes(FriendlyByteBuf var1);

    void process(Supplier<NetworkEvent.Context> var1);

}
