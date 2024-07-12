package com.teamtea.ecliptic;


import com.teamtea.ecliptic.config.ClientConfig;
import com.teamtea.ecliptic.common.network.SimpleNetworkHandler;
import com.teamtea.ecliptic.config.ServerConfig;
import com.teamtea.ecliptic.data.start;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
// import xueluoanping.fluiddrawerslegacy.handler.ControllerFluidCapabilityHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Ecliptic.MODID)
public class Ecliptic {
    public static final String MODID = "ecliptic";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(Ecliptic.MODID);
    public static final String NETWORK_VERSION = "1.0";

    public static void logger(String x) {
        // 通过它可以判断是否在哪个服务器
        // ServerLifecycleHooks.getCurrentServer()
        // if (!FMLEnvironment.production||General.bool.get())
        {
//            LOGGER.debug(x);
            LOGGER.info(x);
        }
    }

    public static void logger(Object... x) {

        // if (!FMLEnvironment.production||General.bool.get())
        {
            StringBuilder output = new StringBuilder();

            for (Object i : x) {
                if (i == null) output.append(", ").append("null");
                else if (i.getClass().isArray()) {
                    output.append(", [");
                    for (Object c : (int[]) i) {
                        output.append(c).append(",");
                    }
                    output.append("]");
                } else if (i instanceof List) {
                    output.append(", [");
                    for (Object c : (List) i) {
                        output.append(c);
                    }
                    output.append("]");
                } else
                    output.append(", ").append(i);
            }
            LOGGER.info(output.substring(1));
        }

    }


    public Ecliptic() {

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModContents.ModBlocks.register(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::FMLCommonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ServerConfig.SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);

    }


    public static ResourceLocation rl(String id) {
        return new ResourceLocation(MODID, id);
    }

    public void FMLCommonSetup(final FMLCommonSetupEvent event) {
        SimpleNetworkHandler.init();
    }

    public void gatherData(final GatherDataEvent event) {
        start.dataGen(event);
    }


    public static class ModContents {
        public static final DeferredRegister<Block> ModBlocks = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        public static RegistryObject<Block> snowySlab = ModBlocks.register("snowy_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB).dynamicShape().noOcclusion()));
        public static RegistryObject<Block> snowyStairs = ModBlocks.register("snowy_stairs", () -> new StairBlock(Blocks.OAK_PLANKS::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS).dynamicShape().noOcclusion()));
        public static RegistryObject<Block> snowyBlock = ModBlocks.register("snowy_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK).dynamicShape().noOcclusion()));
        public static RegistryObject<Block> snowyLeaves = ModBlocks.register("snowy_leaves", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK).dynamicShape().noOcclusion()));
    }
}
