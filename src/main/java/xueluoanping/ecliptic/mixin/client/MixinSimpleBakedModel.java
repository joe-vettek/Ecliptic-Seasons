package xueluoanping.ecliptic.mixin.client;


import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.core.ModelManager;

import java.util.List;
import java.util.Map;

@Mixin({SimpleBakedModel.class})
public class MixinSimpleBakedModel {

    @Shadow @Final protected Map<Direction, List<BakedQuad>> culledFaces;

    @Inject(at = {@At("HEAD")}, method = {"getQuads"}, cancellable = true)
    private void mixin_renderQuadList(BlockState p_235054_, Direction p_235055_, RandomSource p_235056_, CallbackInfoReturnable<List<BakedQuad>> cir) {

        if( ModelManager.ShouldReplaceQuads(p_235054_,p_235055_,p_235056_)){
            cir.setReturnValue( ModelManager.getReplacedQuads(culledFaces,p_235054_,p_235055_,p_235056_));
        }

    }
}
