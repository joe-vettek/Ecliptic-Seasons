package com.teamtea.eclipticseasons.common.misc;

import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.MapColor;

public class MapColorReplacer {
    public static MapColor getTopSnowColor(BlockGetter blockGetter, BlockState state, BlockPos pos) {
        if (!(blockGetter instanceof Level level))
            return null;

        boolean isLight = false;

        int flag = 0;
        var onBlock = state.getBlock();
        if (onBlock instanceof LeavesBlock) {
            flag = MapChecker.FLAG_LEAVES;
        } else if ((state.isSolidRender(level, pos)
                // state.isSolid()
                || onBlock instanceof LeavesBlock
                || (onBlock instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP)
                || (onBlock instanceof StairBlock && state.getValue(StairBlock.HALF) == Half.TOP))) {
            flag = MapChecker.FLAG_BLOCK;
        } else if (onBlock instanceof SlabBlock) {
            flag = MapChecker.FLAG_SLAB;
        } else if (onBlock instanceof StairBlock) {
            flag = MapChecker.FLAG_STAIRS;
        } else if (MapChecker.LowerPlant.contains(onBlock)) {
            flag = MapChecker.FLAG_GRASS;
        } else if (MapChecker.LARGE_GRASS.contains(onBlock)) {
            flag = MapChecker.FLAG_GRASS_LARGE;
        } else if ((onBlock instanceof FarmBlock || onBlock instanceof DirtPathBlock)) {
            flag = MapChecker.FLAG_FARMLAND;
        }


        int offset = 0;
        if (flag == MapChecker.FLAG_GRASS || flag == MapChecker.FLAG_GRASS_LARGE) {
            if (flag == MapChecker.FLAG_GRASS) {
                offset = 1;
            }
            // 这里不忽略这个警告，因为后续会有优化
            else if (flag == MapChecker.FLAG_GRASS_LARGE) {
                if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                    offset = 1;
                } else {
                    offset = 2;
                }
            }
        }

        // isLight = ClientConfig.Renderer.useVanillaCheck.get() ?
        //         level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(pos.above()) >= 15
        //         : ModelManager.getHeightOrUpdate(pos, false) == pos.getY() - offset;
        isLight = MapChecker.getHeightOrUpdate(level, pos, false) == pos.getY() - offset;

        // SimpleUtil.testTime(()->{getHeightOrUpdate(pos, false);});

        long seed = (long) Mth.abs(pos.hashCode());

        isLight = flag != 0 && isLight
                && state.getBlock() != Blocks.SNOW_BLOCK
                && MapChecker.shouldSnowAt(level, pos.below(offset), state, level.getRandom(), seed);


        return isLight ? MapColor.SNOW : null;
    }
}
