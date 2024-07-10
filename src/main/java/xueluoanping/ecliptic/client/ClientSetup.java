package xueluoanping.ecliptic.client;


// import com.jaquadro.minecraft.storagedrawers.client.renderer.TileEntityDrawersRenderer;

import cloud.lemonslice.client.color.season.BiomeColorsHandler;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xueluoanping.ecliptic.Ecliptic;
import xueluoanping.ecliptic.client.core.ModelManager;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {


    // does the Glass Lantern render in the given layer (RenderType) - used as Predicate<RenderType> lambda for setRenderLayer
    public static boolean isGlassLanternValidLayer(RenderType layerToCheck) {
        return layerToCheck == RenderType.cutoutMipped() || layerToCheck == RenderType.translucent();
    }

    @SubscribeEvent
    public static void registerExtraModels(ModelEvent.RegisterAdditional event) {
        // event.register(SnowClient.OVERLAY_MODEL);
        //
        // SnowClient.snowVariantMapping.clear();
        // ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        // ModelBakery.MODEL_LISTER.listMatchingResources(resourceManager).forEach((key, resource) -> {
        //     ModelDefinition def;
        //     try {
        //         def = resource.metadata().getSection(SnowVariantMetadataSectionSerializer.SERIALIZER).orElse(null);
        //     } catch (IOException e) {
        //         return;
        //     }
        //     if (def == null || def.model == null) {
        //         return;
        //     }
        //     SnowClient.snowVariantMapping.put(ModelBakery.MODEL_LISTER.fileToId(key), def);
        //     event.register(def.model);
        // });
    }

    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event) {
        Ecliptic.logger("Register Client");
        event.enqueueWork(() -> {
            // ItemBlockRenderTypes.setRenderLayer(ModContents.fluiddrawer.get(), ClientSetup::isGlassLanternValidLayer);
            // MenuScreens.register(ModContents.containerType.get(), Screen.Slot1::new);
            //
            ItemBlockRenderTypes.setRenderLayer(Blocks.BAMBOO_BLOCK, RenderType.cutoutMipped());
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

    // public static Map<ResourceLocation, BakedModel> BakedSnowModels=new HashMap<>();

    @SubscribeEvent
    public static void onModelBaked(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
        ModelManager.models = modelRegistry;
        // snowModel.resolve();
        ModelManager.snowySlabBottom.resolve();
        ModelManager.snowOverlayLeaves.resolve();
        var test = ModelManager.snowOverlayBlock.resolve().get();
        Ecliptic.logger(test);
        // net.minecraft.client.resources.model.ModelManager.reload
        // p_251134_.listPacks().toList().get(0).getResource(PackType.CLIENT_RESOURCES, completablefuture.get().entrySet().stream().toList().get(0).getKey()).get().readAllBytes()
        // p_251134_.listPacks().toList().get(1).getResource(PackType.CLIENT_RESOURCES, Ecliptic.rl("textures/block/icon.png")).get().readAllBytes()

        // for (Map.Entry<ResourceKey<Block>, Block> resourceKeyBlockEntry : BuiltInRegistries.BLOCK.entrySet()) {
        //     Block block = resourceKeyBlockEntry.getValue();
        //     for (BlockState state : block.getStateDefinition().getPossibleStates()) {
        //         for (Direction direction : List.of(Direction.UP, Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST, Direction.DOWN)) {
        //             // 用这个取代向下的情况。因为List of不能为空
        //             if (direction == Direction.DOWN) {
        //                 direction = null;
        //             }
        //             // TODO:这里有随机权重源的问题
        //             var ran = RandomSource.create();
        //             var list = models.get(BlockModelShaper.stateToModelLocation(state)).getQuads(state, direction, ran);
        //             LazyOptional<net.minecraft.client.resources.model.BakedModel> snowModel = LazyOptional.empty();
        //             BlockState snowState;
        //             boolean flag_shape = false;
        //             try {
        //                 flag_shape = state.getShape(null, new BlockPos(0, 0, 0)).bounds()
        //                         == Block.box(0D, 0D, 0D, 16D, 16D, 16D).bounds();
        //             } catch (Exception e) {
        //                 Ecliptic.logger("Special Shape with", state);
        //             }
        //
        //             if (flag_shape
        //                     || state.getBlock() instanceof LeavesBlock
        //                     || (state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP)) {
        //                 snowState = null;
        //                 snowModel = ClientSetup.snowOverlayBlock;
        //             } else if (state.getBlock() instanceof SlabBlock) {
        //                 snowState = null;
        //                 snowModel = ClientSetup.snowySlabBottom;
        //             } else if (state.getBlock() instanceof StairBlock) {
        //                 snowState = Ecliptic.ModContents.snowyStairs.get().defaultBlockState()
        //                         .setValue(StairBlock.FACING, state.getValue(StairBlock.FACING))
        //                         .setValue(StairBlock.HALF, state.getValue(StairBlock.HALF))
        //                         .setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE));
        //                 // ClientSetup.models.get(new ModelResourceLocation(Ecliptic.ModContents.snowyStairs.getId(),"facing=north,half=bottom,shape=outer_left,waterlogged=true"))
        //                 // 楼梯的方向是无
        //                 snowModel = LazyOptional.of(() -> ClientSetup.models.get(BlockModelShaper.stateToModelLocation(snowState)));
        //             } else {
        //                 snowState = null;
        //             }
        //
        //             if (snowModel.resolve().isPresent()) {
        //                 int size = list.size();
        //                 var snowList = snowModel.resolve().get().getQuads(snowState, direction, ran);
        //                 var newList = new ArrayList<BakedQuad>(size + snowList.size());
        //                 newList.addAll(list);
        //                 newList.addAll(snowList);
        //                 if (!list.isEmpty())
        //                     ModelReplacer.quadMap.put(list, newList);
        //                 // list = newList;
        //             }
        //         }
        //     }
        // }

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
