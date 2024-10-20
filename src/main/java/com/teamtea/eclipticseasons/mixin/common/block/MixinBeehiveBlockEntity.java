package com.teamtea.eclipticseasons.mixin.common.block;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.util.WeatherUtil;

import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeehiveTileEntity.class)
public abstract class MixinBeehiveBlockEntity extends TileEntity {

    public MixinBeehiveBlockEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @WrapOperation(
            method = "releaseOccupant",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isRaining()Z")
    )
    private boolean mixin$releaseOccupantCheckRain(World level, Operation<Boolean> original) {
        return WeatherUtil.isBlockInRain(level, getBlockPos());
    }


}
