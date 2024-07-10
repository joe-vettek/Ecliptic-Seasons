package xueluoanping.ecliptic.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xueluoanping.ecliptic.client.core.ModelManager;

import java.util.List;
import java.util.stream.Collectors;

public class WarpBakedModel implements BakedModel {

    private final net.minecraft.client.resources.model.BakedModel bakedModel;
    private final net.minecraft.client.resources.model.BakedModel agentModel;

    public WarpBakedModel(BakedModel bakedModel, BakedModel agentModel) {
        this.bakedModel = bakedModel;
        this.agentModel = agentModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource randomSource) {
        var txt= ModelManager.snowOverlayBlock.resolve().get().getQuads(Blocks.SNOW_BLOCK.defaultBlockState(), Direction.UP, null).get(0);
        if(direction==Direction.UP){

            return bakedModel.getQuads(state, direction, randomSource).stream().map(bakedQuad->{
              return   new WrapBakedQuad(bakedQuad.getVertices(),
                                    bakedQuad.getTintIndex(),
                                    bakedQuad.getDirection(),
                                    bakedQuad.getSprite(),
                                    bakedQuad.isShade(),
                                    bakedQuad.hasAmbientOcclusion(), txt);
            }).collect(Collectors.toList());
        }
        return bakedModel.getQuads(state, direction, randomSource);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return bakedModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return bakedModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return bakedModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return bakedModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return bakedModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return bakedModel.getOverrides();
    }


}
