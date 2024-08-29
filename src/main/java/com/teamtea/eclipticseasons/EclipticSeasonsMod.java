package com.teamtea.eclipticseasons;


import com.teamtea.eclipticseasons.api.misc.BasicWeather;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.common.misc.HeatStrokeEffect;
import com.teamtea.eclipticseasons.compat.CompatModule;
import com.teamtea.eclipticseasons.config.ClientConfig;
import com.teamtea.eclipticseasons.config.ServerConfig;
import com.teamtea.eclipticseasons.data.start;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
// import xueluoanping.fluiddrawerslegacy.handler.ControllerFluidCapabilityHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EclipticSeasonsApi.MODID)
public class EclipticSeasonsMod {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(EclipticSeasonsApi.MODID);
    public static final String NETWORK_VERSION = "1.0";

    public EclipticSeasonsMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::FMLCommonSetup);
        modEventBus.addListener(this::gatherData);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModContents.ModBlocks.register(modEventBus);
        OtherContents.weathers.register(modEventBus);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        // NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        // modContainer.addListener(this::gatherData);
        // modContainer.addListener(this::FMLCommonSetup);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, ServerConfig.SERVER_CONFIG);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        if (FMLLoader.getDist() == Dist.CLIENT)
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }


    public static ResourceLocation rl(String id) {
        return ResourceLocation.fromNamespaceAndPath(EclipticSeasonsApi.MODID, id);
    }


    public void FMLCommonSetup(final FMLCommonSetupEvent event) {
        // SimpleNetworkHandler.init();
        CompatModule.register();
    }


    public void gatherData(final GatherDataEvent event) {
        start.dataGen(event);
    }


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
                    ;
                    output.append(", [");
                    for (Object c : Arrays.stream((Object[]) i).toList()) {
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

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class SoundEventsRegistry {
        public final static SoundEvent spring_forest = SoundEvent.createVariableRangeEvent(rl("ambient.spring_forest"));
        public final static SoundEvent garden_wind = SoundEvent.createVariableRangeEvent(rl("ambient.garden_wind"));
        public final static SoundEvent night_river = SoundEvent.createVariableRangeEvent(rl("ambient.night_river"));
        public final static SoundEvent windy_leave = SoundEvent.createVariableRangeEvent(rl("ambient.windy_leave"));
        public final static SoundEvent winter_forest = SoundEvent.createVariableRangeEvent(rl("ambient.winter_forest"));
        public final static SoundEvent winter_cold = SoundEvent.createVariableRangeEvent(rl("ambient.winter_cold"));

        @SubscribeEvent
        public static void blockRegister(RegisterEvent event) {
            // MultiPackResourceManager
            event.register(Registries.SOUND_EVENT, soundEventRegisterHelper -> {
                soundEventRegisterHelper.register(spring_forest.getLocation(), spring_forest);
                soundEventRegisterHelper.register(garden_wind.getLocation(), garden_wind);
                soundEventRegisterHelper.register(night_river.getLocation(), night_river);
                soundEventRegisterHelper.register(windy_leave.getLocation(), windy_leave);
                soundEventRegisterHelper.register(winter_forest.getLocation(), winter_forest);
                soundEventRegisterHelper.register(winter_cold.getLocation(), winter_cold);
            });
        }
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static final class ParticleRegistry {
        public static final SimpleParticleType FIREFLY = new SimpleParticleType(false);
        public static final SimpleParticleType WILD_GOOSE = new SimpleParticleType(false);

        @SubscribeEvent
        public static void blockRegister(RegisterEvent event) {
            event.register(Registries.PARTICLE_TYPE, particleTypeRegisterHelper -> {
                particleTypeRegisterHelper.register(rl("firefly"), FIREFLY);
                particleTypeRegisterHelper.register(rl("wild_goose"), WILD_GOOSE);
            });
        }


    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static final class EffectRegistry {
        public static final MobEffect HEAT_STROKE = new HeatStrokeEffect(MobEffectCategory.NEUTRAL, 0xf9d27d);

        @SubscribeEvent
        public static void blockRegister(RegisterEvent event) {
            event.register(Registries.MOB_EFFECT, soundEventRegisterHelper -> {
                soundEventRegisterHelper.register(rl("heat_stroke"), HEAT_STROKE);
            });


        }


    }

    public static class ModContents {
        public static final DeferredRegister<Block> ModBlocks = DeferredRegister.create(Registries.BLOCK, EclipticSeasonsApi.MODID);
        public static DeferredHolder<Block, Block> snowySlab = ModBlocks.register("snowy_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).dynamicShape().noOcclusion()));
        public static DeferredHolder<Block, Block> snowyStairs = ModBlocks.register("snowy_stairs", () -> new StairBlock(Blocks.OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).dynamicShape().noOcclusion()));
        public static DeferredHolder<Block, Block> snowyBlock = ModBlocks.register("snowy_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK).dynamicShape().noOcclusion()));
        public static DeferredHolder<Block, Block> snowyLeaves = ModBlocks.register("snowy_leaves", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK).dynamicShape().noOcclusion()));
    }


    private static <T> ResourceKey<Registry<T>> createRegistryKey(String pName) {
        return ResourceKey.createRegistryKey(rl(pName));
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class OtherContents {
        public static final ResourceKey<Registry<BasicWeather>> WEATHER = createRegistryKey("weather");
        public static final Registry<BasicWeather> BASIC_WEATHERS = new RegistryBuilder<>(WEATHER).sync(true).create();
        public static final DeferredRegister<BasicWeather> weathers = DeferredRegister.create(WEATHER, EclipticSeasonsApi.MODID);

        static {
            // ByteBufCodecs.registry(Registries.ENTITY_TYPE).encode(p_320192_, this.type);
            if (FMLLoader.getDist() == Dist.CLIENT) {
                weathers.register("test", () -> new BasicWeather() {
                    @Override
                    protected Object clone() {
                        return this;
                    }
                });
                weathers.register("ss", () -> new BasicWeather() {
                    @Override
                    protected Object clone() {
                        return this;
                    }
                });
            } else {
                weathers.register("ss", () -> new BasicWeather() {
                    @Override
                    protected Object clone() {
                        return this;
                    }
                });
                weathers.register("test", () -> new BasicWeather() {
                    @Override
                    protected Object clone() {
                        return this;
                    }
                });
            }


        }

        @SubscribeEvent
        public static void newRegistryEvent(NewRegistryEvent event) {
            event.register(BASIC_WEATHERS);
        }

    }
}
