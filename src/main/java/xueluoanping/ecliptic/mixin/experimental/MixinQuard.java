package xueluoanping.ecliptic.mixin.experimental;


import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.BakedModelEncoderFixer;

import java.util.Map;

@Mixin({BakedQuad.class})
public abstract class MixinQuard {

    // @Inject(at = {@At("HEAD")}, method = {"getSprite"}, cancellable = true)
    // public void mixin_warmEnoughToRain(CallbackInfoReturnable<TextureAtlasSprite> cir) {
    //     if (this.sprite.atlasLocation().toString().contains("grass"))
    //         cir.setReturnValue(BakedModelEncoderFixer.getSnow());
    // }
    //
    // @Shadow @Final protected TextureAtlasSprite sprite;
}
