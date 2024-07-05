package xueluoanping.ecliptic.mixin.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.util.ModelReplacer;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Mixin({net.minecraft.client.renderer.ItemBlockRenderTypes.class})
public abstract class ItemBlockRenderTypes {

    // ctx.world().world.getBlockState(ctx.pos)
    @Inject(at = {@At("HEAD")}, method = {"getRenderLayers"}, cancellable = true, remap = false)
    private static void mixin_getRenderLayers(BlockState state, CallbackInfoReturnable<ChunkRenderTypeSet> cir) {
        if (Minecraft.getInstance().level != null)
            if (state.getBlock() instanceof SlabBlock
                    || state.getBlock().isOcclusionShapeFullBlock(state, Minecraft.getInstance().level, new BlockPos(0, 0, 0))) {
                cir.setReturnValue(ChunkRenderTypeSet.of(RenderType.cutoutMipped()));
            }
    }


}
