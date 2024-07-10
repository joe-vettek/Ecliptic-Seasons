package xueluoanping.ecliptic.mixin.compat.dynamictrees;

import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;


@Mixin({Heightmap.Types.class})
public class HeightmapTypes {

    // public static void init() {
    //     if (FMLLoader.getLoadingModList().getModFileById("dynamictrees") != null) {
    //         Heightmap.Types.MOTION_BLOCKING.isOpaque = (p_284915_) -> p_284915_.blocksMotion() || !p_284915_.getFluidState().isEmpty()
    //                 || p_284915_.getBlock() instanceof DynamicLeavesBlock;
    //     }
    // }

    // ctx.world().world.getBlockState(ctx.pos)
    // @Inject(at = {@At("HEAD")}, method = {"isOpaque"}, cancellable = true)
    // private void mixin_isOpaque(CallbackInfoReturnable<Predicate<BlockState>> cir) {
    //   if ((Heightmap.Types)(Object)this == Heightmap.Types.MOTION_BLOCKING){
    //       cir.setReturnValue( (state) -> state.blocksMotion() || !state.getFluidState().isEmpty()
    //                               || state.getBlock() instanceof DynamicLeavesBlock);
    //   }
    // }

}
