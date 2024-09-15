package com.teamtea.eclipticseasons.mixin.compat.distanthorizons;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.seibel.distanthorizons.core.pos.blockPos.DhBlockPos;
import com.seibel.distanthorizons.core.pos.blockPos.DhBlockPosMutable;
import com.seibel.distanthorizons.core.wrapperInterfaces.block.IBlockStateWrapper;
import com.teamtea.eclipticseasons.client.core.ModelManager;
import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import com.teamtea.eclipticseasons.common.misc.MapColorReplacer;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.McObjectConverter;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.block.BiomeWrapper;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.block.BlockStateWrapper;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.block.ClientBlockStateColorCache;
import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;

// 这个类缺乏必要的查询手段
@Mixin({ClientLevelWrapper.class})
public abstract class MixinNeoForgeClientLevelWrapper {
}
