package com.teamtea.eclipticseasons.mixin.common.block;



import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.block.SpreadableSnowyDirtBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpreadableSnowyDirtBlock.class)
public class MixinGrassBlock {

    @Inject(at = {@At("HEAD")}, method = {"randomTick"}, cancellable = true)
    public void mixin_randomTick(CallbackInfo ci) {

    }
}
