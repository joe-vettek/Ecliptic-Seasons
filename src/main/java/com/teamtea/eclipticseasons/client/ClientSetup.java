package com.teamtea.eclipticseasons.client;

import com.teamtea.eclipticseasons.client.color.season.BiomeColorsHandler;
import com.teamtea.eclipticseasons.client.particle.FireflyParticle;
import com.teamtea.eclipticseasons.client.particle.WildGooseParticle;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.RenderType;

import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;


import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.client.core.ModelManager;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    // does the Glass Lantern render in the given layer (RenderType) - used as Predicate<RenderType> lambda for setRenderLayer
    public static boolean isGlassLanternValidLayer(RenderType layerToCheck) {
        return layerToCheck == RenderType.cutoutMipped() || layerToCheck == RenderType.translucent();
    }

    @SubscribeEvent
    public static void blockRegister(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(EclipticSeasons.ParticleRegistry.FIREFLY, (p_277215_) ->
                (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) ->
                        new FireflyParticle(level, x, y, z, p_277215_));
        Minecraft.getInstance().particleEngine.register(EclipticSeasons.ParticleRegistry.WILD_GOOSE, (p_277215_) ->
                (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) ->
                        new WildGooseParticle(level, x, y, z,0.01,0.01,0.01, p_277215_));
    }

    @SubscribeEvent
    public static void registerExtraModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(ModelManager.snowy_fern);
        ModelLoader.addSpecialModel(ModelManager.snowy_grass);
        ModelLoader.addSpecialModel(ModelManager.snowy_tall_grass_top);
        ModelLoader.addSpecialModel(ModelManager.snowy_tall_grass_bottom);
        ModelLoader.addSpecialModel(ModelManager.snowy_large_fern_top);
        // 注意这里使用地址和model地址效果不同，后者需要写blockstate
        ModelLoader.addSpecialModel(ModelManager.snowy_large_fern_bottom);
        ModelLoader.addSpecialModel(ModelManager.snowy_dandelion);
        ModelLoader.addSpecialModel(ModelManager.dandelion_top);
        ModelLoader.addSpecialModel(ModelManager.overlay_2);
        ModelLoader.addSpecialModel(ModelManager.snow_height2);
        ModelLoader.addSpecialModel(ModelManager.snow_height2_top);
        ModelLoader.addSpecialModel(ModelManager.grass_flower);
        ModelLoader.addSpecialModel(ModelManager.butterfly1);
        ModelLoader.addSpecialModel(ModelManager.butterfly2);
        ModelLoader.addSpecialModel(ModelManager.butterfly3);

    }

    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event) {
        EclipticSeasons.logger("Register Client");
        event.enqueueWork(() -> {
            // ItemBlockRenderTypes.setRenderLayer(ModContents.fluiddrawer.get(), ClientSetup::isGlassLanternValidLayer);
            // MenuScreens.register(ModContents.containerType.get(), Screen.Slot1::new);
            //
            // ItemBlockRenderTypes.setRenderLayer(ModContents.RiceSeedlingBlock.get(),RenderType.cutout());
            // fix json file instead
            BiomeColors.GRASS_COLOR_RESOLVER = BiomeColorsHandler.GRASS_COLOR;
            BiomeColors.FOLIAGE_COLOR_RESOLVER = BiomeColorsHandler.FOLIAGE_COLOR;
            Minecraft.getInstance().getBlockColors().register((state, blockAndTintGetter, pos, i) -> {
                if (i == 1) {

                    return blockAndTintGetter != null && pos != null ? BiomeColors.getAverageGrassColor(blockAndTintGetter, pos) : GrassColors.get(0.5D, 1.0D);
                } else {
                    return -1;
                }
            }, Blocks.DANDELION);

            // Map<ResourceLocation, BakedModel> modelRegistry =Minecraft.getInstance().getModelManager().;
            // ModelManager.models = modelRegistry;
            // snowModel.resolve();
            // ModelManager.snowySlabBottom.resolve();
            // ModelManager.snowOverlayLeaves.resolve();
            // var test = ModelManager.snowOverlayBlock.resolve().get();
            // EclipticSeasons.logger(test);

            ModelManager.quadMap.clear();
            ModelManager.quadMap_1.clear();
        });
    }



    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent event) {
        ModelManager.models=event.getModelRegistry();

        // net.minecraft.client.resources.model.ModelManager.reload
        // p_251134_.listPacks().toList().get(0).getResource(PackType.CLIENT_RESOURCES, completablefuture.get().entrySet().stream().toList().get(0).getKey()).get().readAllBytes()
        // p_251134_.listPacks().toList().get(1).getResource(PackType.CLIENT_RESOURCES, Ecliptic.rl("textures/block/icon.png")).get().readAllBytes()

        // for (Map.Entry<ResourceKey<Block>, Block> resourceKeyBlockEntry : BuiltInRegistries.BLOCK.entrySet()) {
        //     Block block = resourceKeyBlockEntry.getValue();
        //     for (BlockState state : block.getStateDefinition().getPossibleStates()) {
        //         for (Direction direction : List.of(Direction.UP, Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST, Direction.DOWN)) {
        //     }
        // }

        // models.entrySet().stream().filter(e->e.getKey().namespace.equals(Ecliptic.MODID)&&e.getKey().toString().contains("slab")).toList();

    }

}
