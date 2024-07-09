package xueluoanping.ecliptic.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.data.SpriteSourceProvider;

public class WrapBakedQuad extends BakedQuad {

    protected final BakedQuad wrapper;

    // public WrapBakedQuad(int[] p_111298_, int p_111299_, Direction p_111300_, TextureAtlasSprite p_111301_, boolean p_111302_) {
    //     this(p_111298_, p_111299_, p_111300_, p_111301_, p_111302_);
    // }

    public WrapBakedQuad(int[] p_111298_, int p_111299_, Direction p_111300_, TextureAtlasSprite p_111301_, boolean p_111302_, boolean hasAmbientOcclusion, BakedQuad wrapper) {
        super(p_111298_, p_111299_, p_111300_, p_111301_, p_111302_, hasAmbientOcclusion);
        this.wrapper=wrapper;

    }

    @Override
    public TextureAtlasSprite getSprite() {
        return wrapper==null?super.getSprite():wrapper.getSprite();
    }
}
