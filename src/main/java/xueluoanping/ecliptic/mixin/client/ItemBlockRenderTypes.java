package xueluoanping.ecliptic.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({net.minecraft.client.renderer.ItemBlockRenderTypes.class})
public abstract class ItemBlockRenderTypes {

    // ctx.world().world.getBlockState(ctx.pos)
    @Inject(at = {@At("HEAD")}, method = {"getRenderLayers"}, cancellable = true)
    private static void mixin_getRenderLayers(BlockState state, CallbackInfoReturnable<ChunkRenderTypeSet> cir) {
        if (Minecraft.getInstance().level != null)
            if (state.getBlock() instanceof SlabBlock||state.getBlock() instanceof StairBlock
                    || state.getBlock().isOcclusionShapeFullBlock(state, Minecraft.getInstance().level, new BlockPos(0, 0, 0))) {
                cir.setReturnValue(ChunkRenderTypeSet.of(RenderType.cutoutMipped()));
            }
    }


}
