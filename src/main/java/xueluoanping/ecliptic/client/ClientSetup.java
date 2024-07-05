package xueluoanping.ecliptic.client;


// import com.jaquadro.minecraft.storagedrawers.client.renderer.TileEntityDrawersRenderer;

import cloud.lemonslice.teastory.client.color.season.BiomeColorsHandler;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xueluoanping.ecliptic.Ecliptic;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {


    // does the Glass Lantern render in the given layer (RenderType) - used as Predicate<RenderType> lambda for setRenderLayer
    public static boolean isGlassLanternValidLayer(RenderType layerToCheck) {
        return layerToCheck == RenderType.cutoutMipped() || layerToCheck == RenderType.translucent();
    }


    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event) {
        Ecliptic.logger("Register Client");
        event.enqueueWork(() -> {
            // ItemBlockRenderTypes.setRenderLayer(ModContents.fluiddrawer.get(), ClientSetup::isGlassLanternValidLayer);
            // MenuScreens.register(ModContents.containerType.get(), Screen.Slot1::new);
            //
            // ItemBlockRenderTypes.setRenderLayer(ModContents.ricePlant.get(),RenderType.cutout());
            // ItemBlockRenderTypes.setRenderLayer(ModContents.RiceSeedlingBlock.get(),RenderType.cutout());
            // fix json file instead
            BiomeColors.GRASS_COLOR_RESOLVER = BiomeColorsHandler.GRASS_COLOR;
            BiomeColors.FOLIAGE_COLOR_RESOLVER = BiomeColorsHandler.FOLIAGE_COLOR;

        });
    }

    //    注意static是单次，比如启动类，没有比如右击事件
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // FluidDrawersLegacyMod.logger("Register Renderer");
        // ModContents.DRBlockEntities.getEntries().forEach((reg) -> {
        //     event.registerBlockEntityRenderer((BlockEntityType<BlockEntityFluidDrawer>)reg.get(),
        //             TESRFluidDrawer::new);
        // });
    }

    public static Map<ResourceLocation, BakedModel> models;
    public static
    LazyOptional<net.minecraft.client.resources.model.BakedModel> snowModel=
            LazyOptional.of(()->models.get(new ModelResourceLocation(new ResourceLocation("minecraft:snow_block"), "")));

    public static
    LazyOptional<net.minecraft.client.resources.model.BakedModel> snowOverlayBlock =
            LazyOptional.of(()->models.get(new ModelResourceLocation(Ecliptic.ModContents.snowyBlock.getId(), "")));
    public static
    LazyOptional<net.minecraft.client.resources.model.BakedModel> snowySlabBottom =
            LazyOptional.of(()->models.get(new ModelResourceLocation(Ecliptic.ModContents.snowySlab.getId(), "type=bottom,waterlogged=false")));

    @SubscribeEvent
    public static void onModelBaked(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
        models = modelRegistry;
        snowModel.resolve();
        snowySlabBottom.resolve();
        var test= snowOverlayBlock.resolve().get();
        Ecliptic.logger(test);
        // models.entrySet().stream().filter(e->e.getKey().namespace.equals(Ecliptic.MODID)&&e.getKey().toString().contains("slab")).toList();

        // event.getModels().entrySet().stream().filter(resourceLocationBakedModelEntry -> resourceLocationBakedModelEntry.getKey().toString().contains("grass")).collect(Collectors.toList())
        // event.getModels().get(new ModelResourceLocation(new ResourceLocation("minecraft:grass_block"), "snowy=false"))
        // event.getModels().get(new ModelResourceLocation(new ResourceLocation("minecraft:snow_block"), ""))
        // event.getModels().put(new ModelResourceLocation(new ResourceLocation("minecraft:grass_block"), "snowy=false"),event.getModels().get(new ModelResourceLocation(new ResourceLocation("minecraft:snow_block"), "")))
    }

    @SubscribeEvent
    public static void onRegisterColorHandlersEvent_Block(RegisterColorHandlersEvent.Block event) {
        Ecliptic.logger(11);
        // Register programmable custom block color providers for LeavesPropertiesJson

        // BlockState birchLeaves = Blocks.BIRCH_LEAVES.defaultBlockState();
        // BlockColors blockColors = event.getBlockColors();

        // event.register(new GrassBlockColor(),Blocks.GRASS);
        // event.register(new BirchLeavesColor(), Blocks.BIRCH_LEAVES);
    }

    @SubscribeEvent
    public static void onRegisterColorHandlersEvent_Item(RegisterColorHandlersEvent.Item event) {

    }
}
