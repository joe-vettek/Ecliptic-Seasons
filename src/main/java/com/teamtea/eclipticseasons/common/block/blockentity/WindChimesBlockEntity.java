package com.teamtea.eclipticseasons.common.block.blockentity;

import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.common.block.blockentity.base.SyncBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WindChimesBlockEntity extends SyncBlockEntity {
    public WindChimesBlockEntity( BlockPos pos, BlockState state) {
        super(EclipticSeasonsMod.ModContents.wind_chimes_entity_type.get(), pos, state);
    }
}
