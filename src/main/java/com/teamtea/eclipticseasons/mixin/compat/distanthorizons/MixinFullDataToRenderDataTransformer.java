package com.teamtea.eclipticseasons.mixin.compat.distanthorizons;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.seibel.distanthorizons.core.config.Config;
import com.seibel.distanthorizons.core.dataObjects.fullData.FullDataPointIdMap;
import com.seibel.distanthorizons.core.dataObjects.transformers.FullDataToRenderDataTransformer;
import com.seibel.distanthorizons.core.level.IDhClientLevel;
import com.seibel.distanthorizons.core.pos.blockPos.DhBlockPos;
import com.seibel.distanthorizons.core.pos.blockPos.DhBlockPosMutable;
import com.seibel.distanthorizons.core.util.FullDataPointUtil;
import com.seibel.distanthorizons.core.wrapperInterfaces.IWrapperFactory;
import com.seibel.distanthorizons.core.wrapperInterfaces.block.IBlockStateWrapper;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IBiomeWrapper;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.McObjectConverter;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.block.BiomeWrapper;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.block.BlockStateWrapper;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.block.ClientBlockStateColorCache;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;
import java.util.HashSet;

@Mixin({FullDataToRenderDataTransformer.class})
public abstract class MixinFullDataToRenderDataTransformer {


    @Shadow
    @Final
    private static IWrapperFactory WRAPPER_FACTORY;

    @WrapOperation(
            remap = false,
            method = "setRenderColumnView",
            at = @At(value = "INVOKE", target = "Lcom/seibel/distanthorizons/core/level/IDhClientLevel;computeBaseColor(Lcom/seibel/distanthorizons/core/pos/blockPos/DhBlockPos;Lcom/seibel/distanthorizons/core/wrapperInterfaces/world/IBiomeWrapper;Lcom/seibel/distanthorizons/core/wrapperInterfaces/block/IBlockStateWrapper;)I")
    )
    private static int ecliptic$setRenderColumnView_computeBaseColor(IDhClientLevel instance,
                                                                     DhBlockPos dhBlockPos,
                                                                     IBiomeWrapper iBiomeWrapper,
                                                                     IBlockStateWrapper iBlockStateWrapper,
                                                                     Operation<Integer> original,
                                                                     @Local(argsOnly = true) FullDataPointIdMap fullDataMapping,
                                                                     @Local(argsOnly = true) LongArrayList fullColumnData) {
        if (!dhBlockPos.equals(DhBlockPos.ZERO) && iBlockStateWrapper instanceof BlockStateWrapper blockStateWrapper
                && !blockStateWrapper.isAir()) {
            var mcPos = McObjectConverter.Convert(dhBlockPos);
            var level = Minecraft.getInstance().level;
            var blockState = blockStateWrapper.blockState;
            // 当给的pos未加载时，读取的是虚空，这并不好。
            if (instance.getClientLevelWrapper() instanceof ClientLevelWrapper clientLevelWrapper) {
                var holderKey = ResourceKey.create(Registries.BIOME, ResourceLocation.parse(iBiomeWrapper.getSerialString()));
                if ((clientLevelWrapper.getLevel().registryAccess().holder(holderKey).orElse(null)
                        instanceof Holder.Reference<Biome> holder)) {
                    if (MapChecker.shouldSnowAtBiome(level, holder.value(), blockState, level.getRandom(), blockState.getSeed(mcPos)))
                    //     return mapColor.col;
                    {
                        HashSet<IBlockStateWrapper> blockStatesToIgnore = WRAPPER_FACTORY.getRendererIgnoredBlocks(instance.getLevelWrapper());
                        for (int i = 0; i < fullColumnData.size(); i++) {
                            long fullData = fullColumnData.getLong(i);
                            int id = FullDataPointUtil.getId(fullData);
                            IBlockStateWrapper iBlockStateWrapper_NowQuery;
                            try {
                                iBlockStateWrapper_NowQuery = fullDataMapping.getBlockStateWrapper(id);
                            } catch (IndexOutOfBoundsException e) {
                                continue;
                            }
                            int bottomY = FullDataPointUtil.getBottomY(fullData);
                            int blockHeight = FullDataPointUtil.getHeight(fullData);
                            int topY = bottomY + blockHeight;
                            if (iBlockStateWrapper_NowQuery instanceof BlockStateWrapper blockStateWrapper_NowQuery
                                    && !iBlockStateWrapper_NowQuery.isAir()
                                    && !blockStatesToIgnore.contains(iBlockStateWrapper_NowQuery)
                            ) {
                                // TODO:后续需要研究哪些方块是被DH跳过渲染的，实际上草是不渲染的，但是樱花树林的粉色小花却渲染
                                // 此为错误结论，原因是上面被跳过的方块会给下面的染色，比如上面是花，那么下面就可以被染色粉色
                                // 以及直接跳过流体下方的渲染
                                // 那么还是要研究谁会被跳过渲染
                                // 目前猜测是无生物碰撞的
                                // 以及双层的植物需要继续向下，看是否需要处理
                                // boolean colorBelowWithAvoidedBlocks = Config.Client.Advanced.Graphics.Quality.tintWithAvoidedBlocks.get();

                                if (bottomY + instance.getMinY() == dhBlockPos.getY() &&
                                        (MapChecker.getBlockType(blockStateWrapper_NowQuery.blockState, level, mcPos) != 0
                                                // || (blockStateWrapper1.blockState.is(BlockTags.FLOWERS))
                                                || (!blockStateWrapper_NowQuery.isSolid() && !blockStateWrapper_NowQuery.isLiquid())
                                        )) {
                                    return Color.WHITE.getRGB();
                                } else {
                                    if (!blockStateWrapper_NowQuery.isLiquid()
                                            && !blockStateWrapper_NowQuery.blockState.blocksMotion()) {
                                        // 如果colorBelowWithAvoidedBlocks时，这时会查看下面的方块，我们也进行一个染色
                                        // 暂时不处理多层需要跳过的方块，实际上也许保留一点颜色会更好看
                                        if (i + 1 < fullColumnData.size()) {
                                            int belowBottomY = FullDataPointUtil.getBottomY(fullColumnData.getLong(i + 1));
                                            if (belowBottomY + instance.getMinY() == dhBlockPos.getY())
                                                return Color.WHITE.getRGB();
                                        }
                                    }
                                    break;
                                }
                            }


                        }
                    }
                }
            }


        }
        return original.call(instance, dhBlockPos, iBiomeWrapper, iBlockStateWrapper);
    }


}
