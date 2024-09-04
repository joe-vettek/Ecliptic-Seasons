package com.teamtea.eclipticseasons.mixin.client;


import com.teamtea.eclipticseasons.common.core.map.MapChecker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({LevelChunk.class})
public abstract class MixinClientLevelChunk {
    @Shadow
    @Final
    private Level level;

    /**
     * TODO:是否应该迁移到服务器上呢，目前还是未知的
     * **/
    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/Heightmap;update(IIILnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1),
            method = "setBlockState", locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    public void ecliptic$Client_setBlockState(BlockPos pos, BlockState state, boolean p_62867_, CallbackInfoReturnable<BlockState> cir) {
        if (level instanceof ClientLevel clientLevel)
        {
            MapChecker.getHeightOrUpdate(clientLevel, pos, true);
        }
    }
}
