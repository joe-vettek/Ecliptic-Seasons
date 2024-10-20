package com.teamtea.eclipticseasons.mixin.client;


import com.teamtea.eclipticseasons.api.util.EclipticTagClientTool;
import com.teamtea.eclipticseasons.api.constant.tag.SeasonTypeBiomeTags;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Biome.class})
public abstract class MixinClientBiome {

    // TODO：这里需要走一下判断是在客户端还是服务器
    @Inject(at = {@At("HEAD")}, method = {"getPrecipitation"}, cancellable = true)
    public void ecliptic$getPrecipitationAt(CallbackInfoReturnable<Biome.RainType> cir) {
        if (FMLLoader.getDist() == Dist.CLIENT)
            cir.setReturnValue(WeatherManager.getPrecipitationAt(Minecraft.getInstance().level, (Biome) (Object) this, BlockPos.ZERO));
    }

}
