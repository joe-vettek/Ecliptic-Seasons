package com.teamtea.eclipticseasons.mixin.compat.distanthorizons;


import loaderCommon.neoforge.com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import org.spongepowered.asm.mixin.Mixin;

// 这个类缺乏必要的查询手段
@Mixin({ClientLevelWrapper.class})
public abstract class MixinNeoForgeClientLevelWrapper {
}
