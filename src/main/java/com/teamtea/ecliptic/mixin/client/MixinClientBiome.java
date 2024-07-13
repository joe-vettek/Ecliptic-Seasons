package com.teamtea.ecliptic.mixin.client;


import com.teamtea.ecliptic.api.EclipticBiomeTags;
import com.teamtea.ecliptic.api.EclipticBiomeTagsClientTool;
import com.teamtea.ecliptic.common.core.biome.BiomeClimateManager;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Biome.class})
public abstract class MixinClientBiome {

    // TODO：这里需要走一下判断是在客户端还是服务器
    @Inject(at = {@At("HEAD")}, method = {"getPrecipitationAt"}, cancellable = true)
    public void mixin_getPrecipitationAt(BlockPos p_198905_, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (FMLLoader.getDist() == Dist.CLIENT)
            cir.setReturnValue(WeatherManager.getPrecipitationAt(Minecraft.getInstance().level, (Biome) (Object) this, p_198905_));
    }

    @Inject(at = {@At("HEAD")}, method = {"hasPrecipitation"}, cancellable = true)
    public void mixin_hasPrecipitation(CallbackInfoReturnable<Boolean > cir) {
        if (FMLLoader.getDist()== Dist.CLIENT)
            cir.setReturnValue(! EclipticBiomeTagsClientTool.getTag((Biome)(Object)this).equals(EclipticBiomeTags.RAINLESS));
    }
}
