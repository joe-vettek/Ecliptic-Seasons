package com.teamtea.eclipticseasons.mixin.compat.distanthorizons;


import com.seibel.distanthorizons.core.api.internal.SharedApi;
import com.seibel.distanthorizons.core.level.IDhClientLevel;
import com.seibel.distanthorizons.core.world.IDhClientWorld;
import com.teamtea.eclipticseasons.common.network.EmptyMessage;
import com.teamtea.eclipticseasons.common.network.NetworkdUtil;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkdUtil.class})
public abstract class MixinNetworkdUtil {

    @Inject(at = {@At("HEAD")}, method = {"processEmptyMessage2"})
    private static void ecliptic$processEmptyMessage2(EmptyMessage emptyMessage, IPayloadContext context, CallbackInfo ci) {
        context.enqueueWork(() -> {
            IDhClientWorld clientWorld = SharedApi.getIDhClientWorld();
            if (Minecraft.getInstance().level != null
                    && ClientLevelWrapper.getWrapper(Minecraft.getInstance().level) instanceof ClientLevelWrapper clientLevelWrapper
                    && clientWorld.getLevel(clientLevelWrapper) instanceof IDhClientLevel clientLevel) {
                // var clientRenderStateAtomicReference = clientLevel.clientside.ClientRenderStateRef;
                // if (clientRenderStateAtomicReference != null) {
                //     // 也许未来需要定向刷新，但是目前来看只需要全部刷新即可
                //     // DhSectionPos.encode((byte) 0,100,100);
                //     // long rootPos = DhSectionPos.encode( clientRenderStateAtomicReference.get().quadtree.treeMinDetailLevel, pos2D.getX(), pos2D.getY());
                //     // clientRenderStateAtomicReference.get().quadtree.reloadPos();
                // }
                // 这里检测一下是否发生了变化，无变化不重置了
                clientLevel.clearRenderCache();
            }
        });
    }
}
