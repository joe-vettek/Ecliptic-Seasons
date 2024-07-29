package com.teamtea.ecliptic.mixin.client.block;


import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public class MixinLeavesBlock {


    @Inject(at = {@At("HEAD")}, method = {"animateTick"}, remap = true)
    private void mixin$onProjectileHit_isThundering(BlockState p_221374_, Level level, BlockPos pos, RandomSource randomSource, CallbackInfo ci) {
        if (randomSource.nextInt(15) == 1) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = level.getBlockState(blockpos);

            // if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(level, blockpos, Direction.UP)) {
            //     ParticleUtils.spawnParticleBelow(level, pos, randomSource, ParticleTypes.DRIPPING_WATER);
            // }
        }
    }

}
