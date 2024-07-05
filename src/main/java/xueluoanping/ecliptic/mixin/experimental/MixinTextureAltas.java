package xueluoanping.ecliptic.mixin.experimental;


import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.handler.WeatherHandler;

import java.util.Map;

@Mixin({TextureAtlas.class})
public abstract class MixinTextureAltas {

    // @Inject(at = {@At("HEAD")}, method = {"getSprite"}, cancellable = true)
    // public void mixin_warmEnoughToRain(ResourceLocation p_118317_, CallbackInfoReturnable<TextureAtlasSprite> cir) {
    //     if (p_118317_.toString().contains("grass"))
    //         cir.setReturnValue(this.texturesByName.get(new ResourceLocation("block/snow")));
    // }
    //
    // @Shadow private Map<ResourceLocation, TextureAtlasSprite> texturesByName;
}
