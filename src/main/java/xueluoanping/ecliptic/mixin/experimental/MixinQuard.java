package xueluoanping.ecliptic.mixin.experimental;


import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;

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
