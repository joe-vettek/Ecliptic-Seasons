package com.teamtea.eclipticseasons.mixin.compat.optifine;


import com.teamtea.eclipticseasons.client.core.ModelManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.optifine.model.BlockModelCustomizer;
import net.optifine.render.RenderEnv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(BlockModelCustomizer.class)
public abstract class MixinBlockModelCustomizer {


    private static Random randomSource = new Random();

    // 这里不知道要不要ordinal=1
    // 但是opt这里要处理的是那个jar文件得移动移动一下，不能直接用
    // opt 似乎无法使用
    // IdentityHashMap似乎不适合Opt
    @Inject(at = {@At(value = "RETURN"),}, remap = false, method = {"getRenderQuads(Ljava/util/List;Lnet/minecraft/world/IBlockDisplayReader;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;Lnet/minecraft/client/renderer/RenderType;JLnet/optifine/render/RenderEnv;)Ljava/util/List;"}, cancellable = true)
    private static void ecliptic$getRenderQuads(List<BakedQuad> quads, IBlockDisplayReader worldIn, BlockState stateIn, BlockPos posIn, Direction enumfacing, RenderType layer, long rand, RenderEnv renderEnv, CallbackInfoReturnable<List<BakedQuad>> cir) {
        List<BakedQuad> bakedQuadList = cir.getReturnValue();
        if (!bakedQuadList.isEmpty() && Minecraft.getInstance().level != null) {
            randomSource.setSeed(rand);
            cir.setReturnValue(ModelManager.appendOverlay(worldIn, stateIn, posIn, enumfacing, randomSource, rand, bakedQuadList));
        }
    }


}
