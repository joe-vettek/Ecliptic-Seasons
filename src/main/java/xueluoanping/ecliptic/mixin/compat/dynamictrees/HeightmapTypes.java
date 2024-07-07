package xueluoanping.ecliptic.mixin.compat.dynamictrees;

import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.util.ModelManager;

import java.util.List;
import java.util.function.Predicate;


@Mixin({Heightmap.Types.class})
public class HeightmapTypes {

    // public static void init() {
    //     if (FMLLoader.getLoadingModList().getModFileById("dynamictrees") != null) {
    //         Heightmap.Types.MOTION_BLOCKING.isOpaque = (p_284915_) -> p_284915_.blocksMotion() || !p_284915_.getFluidState().isEmpty()
    //                 || p_284915_.getBlock() instanceof DynamicLeavesBlock;
    //     }
    // }

    // ctx.world().world.getBlockState(ctx.pos)
    @Inject(at = {@At("HEAD")}, method = {"isOpaque"}, cancellable = true)
    private void mixin_renderQuadList(CallbackInfoReturnable<Predicate<BlockState>> cir) {
      if ((Heightmap.Types)(Object)this == Heightmap.Types.MOTION_BLOCKING){
          cir.setReturnValue( (state) -> state.blocksMotion() || !state.getFluidState().isEmpty()
                                  || state.getBlock() instanceof DynamicLeavesBlock);
      }
    }

}
