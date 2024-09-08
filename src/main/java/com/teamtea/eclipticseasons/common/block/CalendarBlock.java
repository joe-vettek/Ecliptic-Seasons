package com.teamtea.eclipticseasons.common.block;

import com.mojang.serialization.MapCodec;
import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.common.block.base.SimpleHorizontalEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CalendarBlock extends SimpleHorizontalEntityBlock {
    public CalendarBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CalendarBlock::new);
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        var facing = pState.getValue(FACING);
        var facePos = pPos.relative(pState.getValue(FACING).getOpposite());
        return pLevel.getBlockState(facePos).isFaceSturdy(pLevel, facePos, facing);
    }

    @Override
    protected BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pDirection == pState.getValue(FACING).getOpposite() && pNeighborState.isAir())
            return Blocks.AIR.defaultBlockState();
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return EclipticSeasonsMod.ModContents.calendar_entity_type.get().create(pPos, pState);
    }
}
