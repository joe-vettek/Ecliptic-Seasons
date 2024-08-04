package com.teamtea.eclipticseasons.client;


import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.api.util.SimpleUtil;
import com.teamtea.eclipticseasons.client.core.ModelManager;
import com.teamtea.eclipticseasons.common.AllListener;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.common.core.solar.ClientSolarDataManager;
import com.teamtea.eclipticseasons.config.ClientConfig;
import com.teamtea.eclipticseasons.config.ServerConfig;
import com.teamtea.eclipticseasons.common.core.crop.CropInfoManager;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonInfo;
import com.teamtea.eclipticseasons.api.constant.crop.CropHumidityInfo;
import me.jellysquid.mods.sodium.mixin.features.render.model.block.BlockModelRendererMixin;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PandaModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.eclipticseasons.EclipticSeasons;

import java.util.Random;

@Mod.EventBusSubscriber(modid = EclipticSeasons.MODID, value = Dist.CLIENT)
public final class ClientEventHandler {

    public static float prevFogDensity = -1f;
    public static long prevFogTick = -1L;

    public static float r = 0.0f;
    public static float g = 0.0f;
    public static float b = 0.0f;

    @SubscribeEvent
    public static void onFogEvent(ViewportEvent.ComputeFogColor event) {
        renderFogColors(event.getCamera(), (float) event.getPartialTick(), event);
    }

    @SubscribeEvent
    public static void onFogEvent(ViewportEvent.RenderFog event) {
        renderFogDensity(event.getCamera(), event);
    }

    public static void renderFogColors(Camera camera, float partialTick, ViewportEvent.ComputeFogColor event) {
        if (camera.getEntity() instanceof Player player && camera.getFluidInCamera() == FogType.NONE && prevFogDensity > 0f) {
            // Calculate color based on time of day
            final float angle = player.level().getSunAngle(partialTick);
            final float height = Mth.cos(angle);
            final float delta = Mth.clamp((height + 0.4f) / 0.8f, 0, 1);

            final int colorDay = 0xbfbfd8;
            final int colorNight = 0x0c0c19;
            final float red = ((colorDay >> 16) & 0xFF) * delta + ((colorNight >> 16) & 0xFF) * (1 - delta);
            final float green = ((colorDay >> 8) & 0xFF) * delta + ((colorNight >> 8) & 0xFF) * (1 - delta);
            final float blue = (colorDay & 0xFF) * delta + (colorNight & 0xFF) * (1 - delta);

            event.setRed(red / 255f);
            event.setBlue(blue / 255f);
            event.setGreen(green / 255f);

            r = red / 255f;
            g = blue / 255f;
            b = green / 255f;
        }
    }

    public static void renderFogDensity(Camera camera, ViewportEvent.RenderFog event) {
        if (camera.getEntity() instanceof Player player) {
            final long thisTick = Util.getMillis();
            final boolean firstTick = prevFogTick == -1;
            final float deltaTick = firstTick ? 1e10f : (thisTick - prevFogTick) * 0.00015f;

            prevFogTick = thisTick;

            float expectedFogDensity = 0f;

            final Level level = player.level();
            final Biome biome = level.getBiome(camera.getBlockPosition()).value();
            if (level.isRaining() && biome.coldEnoughToSnow(camera.getBlockPosition())) {
                final int light = level.getBrightness(LightLayer.SKY, BlockPos.containing(player.getEyePosition()));
                expectedFogDensity = Mth.clampedMap(light, 0f, 15f, 0f, 1f);
            }

            // Smoothly interpolate fog towards the expected value - increasing faster than it decreases
            if (expectedFogDensity > prevFogDensity) {
                prevFogDensity = Math.min(prevFogDensity + 4f * deltaTick, expectedFogDensity);
            } else if (expectedFogDensity < prevFogDensity) {
                prevFogDensity = Math.max(prevFogDensity - deltaTick, expectedFogDensity);
            }

            if (camera.getFluidInCamera() != FogType.NONE) {
                prevFogDensity = -1; // Immediately cancel fog if there's another fog effect going on
                prevFogTick = -1;
            }

            if (prevFogDensity > 0) {
                final float scaledDelta = 1 - (1 - prevFogDensity) * (1 - prevFogDensity);
                final float fogDensity = 0.1f;
                final float farPlaneScale = Mth.lerp(scaledDelta, 1f, fogDensity);
                final float nearPlaneScale = Mth.lerp(scaledDelta, 1f, 0.3f * fogDensity);

                event.scaleNearPlaneDistance(nearPlaneScale);
                event.scaleFarPlaneDistance(farPlaneScale);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event) {

        if (ServerConfig.Season.enableCrop.get()) {
            if (event.getItemStack().getItem() instanceof BlockItem) {
                if (CropInfoManager.getHumidityCrops().contains(((BlockItem) event.getItemStack().getItem()).getBlock())) {
                    CropHumidityInfo info = CropInfoManager.getHumidityInfo(((BlockItem) event.getItemStack().getItem()).getBlock());
                    if (info != null) event.getToolTip().addAll(info.getTooltip());
                }
                if (CropInfoManager.getSeasonCrops().contains(((BlockItem) event.getItemStack().getItem()).getBlock())) {
                    CropSeasonInfo info = CropInfoManager.getSeasonInfo(((BlockItem) event.getItemStack().getItem()).getBlock());
                    if (info != null) event.getToolTip().addAll(info.getTooltip());
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        synchronized (ModelManager.RegionList) {
            ModelManager.RegionList.clear();
        }
    }

    @SubscribeEvent
    public static void onLevelUnloadEvent(LevelEvent.Unload event) {
        synchronized (ModelManager.RegionList) {
            ModelManager.RegionList.clear();
        }
    }

    @SubscribeEvent
    public static void onLevelEventLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ClientLevel clientLevel) {
            // ModelManager.quadMap.clear();
            // ModelManager.quadMap_1.clear();

            WeatherManager.createLevelBiomeWeatherList(clientLevel);
            // 这里需要恢复一下数据
            // 客户端登录时同步天气数据，此处先放入
            AllListener.DATA_MANAGER_MAP.put(clientLevel, ClientSolarDataManager.get(clientLevel));
        }
    }

    // 强制区块渲染
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (ClientConfig.Renderer.forceChunkRenderUpdate.get()) {
            if (event.phase.equals(TickEvent.Phase.END)
                    && event.level.isClientSide()
                    && ((ClientLevel) event.level).getGameTime() >> 8 == 0) {
                var lr = Minecraft.getInstance().levelRenderer;
                if (lr != null) {
                    //
                    // ((ClientChunkCache) event.level.getChunkSource()).storage.
                    if (Minecraft.getInstance().cameraEntity instanceof Player player) {
                        BlockPos pos = player.getOnPos();
                        SectionPos sectionPos = SectionPos.of(pos);
                        // lr.setSectionDirtyWithNeighbors(sectionPos.x(),sectionPos.y(),sectionPos.z());
                        int x = sectionPos.x();
                        int y = sectionPos.y();
                        int z = sectionPos.z();
                        for (int i = x - 2; i <= x + 2; ++i) {
                            for (int j = z - 2; j <= z + 2; ++j) {
                                for (int k = y - 1; k <= y + 1; ++k) {
                                    lr.setSectionDirty(j, k, i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onRenderLevelStageEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS
                && SimpleUtil.getNowSolarTerm(Minecraft.getInstance().level).getSeason() == Season.SPRING) {
            var multiBufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            // var itr = Minecraft.getInstance().getItemRenderer();
            // var mds = itr.getItemModelShaper();
            // var stack = Items.ACACIA_BOAT.getDefaultInstance();

            var cameraEntity = Minecraft.getInstance().cameraEntity;
            // var blockpos4 = Minecraft.getInstance().cameraEntity.blockPosition();
            var blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            var random = event.getLevelRenderer().level.getRandom();
            var level = event.getLevelRenderer().level;
            int b = 32;
            random = RandomSource.create();
            for (int i = 0; i < 20; ++i)
                for (int j = 0; j < 20; ++j)
                    for (int k = 0; k < 20; ++k)
                    // for (int z = 0; z < 667; ++z)
                    {
                        blockpos$mutableblockpos.set(
                                cameraEntity.xo - 10 + i,
                                cameraEntity.yo - 15 + j,
                                cameraEntity.zo - 10 + k);
                        random.setSeed(blockpos$mutableblockpos.asLong());
                        if (random.nextInt(63) == 0) {
                            BlockState blockstate = event.getLevelRenderer().level.getBlockState(blockpos$mutableblockpos);

                            if (blockstate.is(BlockTags.FLOWERS)) {


                                var blockpos4 = blockpos$mutableblockpos;
                                Vec3 vec3c = blockpos4.getCenter().add(-0.5f, -0.5f + 0.25f, -0.5f);
                                vec3c.add(blockstate.getOffset(Minecraft.getInstance().level, blockpos$mutableblockpos));

                                var state = Blocks.CAMPFIRE.defaultBlockState();
                                Vec3 vec3 = event.getCamera().getPosition();
                                double d0 = vec3.x();
                                double d1 = vec3.y();
                                double d2 = vec3.z();

                                var poseStack = event.getPoseStack();
                                poseStack.pushPose();
                                // poseStack.scale(0.25f, 0.25f, 0.25f);
                                poseStack.translate((double) vec3c.x() - d0, (double) vec3c.y() - d1, (double) vec3c.z() - d2);
                                // poseStack.scale(0.25f, 0.25f, 0.25f);
                                // poseStack.translate(2f, (double) vec3c.y() - d1, 2f);
                                // ((ModelPart)Minecraft.getInstance().getEntityModels().roots.entrySet().toArray()[9].value.bakeRoot()).render();
                                var rs = ModelManager.butterfly1;
                                if (random.nextBoolean()) {
                                    rs = ModelManager.butterfly2;
                                } else if (random.nextBoolean()) {
                                    rs = ModelManager.butterfly3;
                                }

                                int ii;
                                if (level != null) {
                                    ii = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
                                } else {
                                    ii = 15728880;
                                }
                                Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                                        event.getPoseStack().last(),
                                        multiBufferSource.getBuffer(RenderType.cutoutMipped()), (BlockState) null,
                                        Minecraft.getInstance().getModelManager().getModel(rs),
                                        1f, 1f, 1f, ii, OverlayTexture.NO_OVERLAY
                                );


                                // var panda = EntityType.PANDA.create(event.getLevelRenderer().level);
                                // panda.moveTo(new BlockPos(-365,-60,-435),0f,0f);
                                // var livingEntityRenderer = ((LivingEntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(panda));
                                // PandaModel<Panda> livingEntityRendererModel = (PandaModel<Panda>) livingEntityRenderer.getModel();
                                // // livingEntityRendererModel.setupAnim();
                                // var renderType = livingEntityRendererModel.renderType(livingEntityRenderer.getTextureLocation(panda));
                                // livingEntityRendererModel.setupAnim(panda, event.getPartialTick(), 0.0F, -0.1F, 0.0F, 0.0F);
                                //
                                // livingEntityRendererModel.renderToBuffer(event.getPoseStack(), multiBufferSource.getBuffer(renderType), 15728880, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
                                // // look LivingEntityRenderer

                                poseStack.popPose();
                            }
                        }
                    }
            // var blockpos4=new BlockPos(-365,-60,-435);
            // // Vec3 vec3c = new Vec3(cameraEntity.xo - 0.5f, cameraEntity.yOld , cameraEntity.zo - 0.5f);
            //
            // Vec3   vec3c=blockpos4.getCenter().add(0.5f,-0.5f,0.5f);
            //
            // var state = Blocks.CAMPFIRE.defaultBlockState();
            // var model=Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
            // Vec3 vec3 = event.getCamera().getPosition();
            // double d0 = vec3.x();
            // double d1 = vec3.y();
            // double d2 = vec3.z();
            //
            // var poseStack = event.getPoseStack();
            // poseStack.pushPose();
            // // poseStack.scale(0.25f, 0.25f, 0.25f);
            // poseStack.translate((double) vec3c.x() - d0, (double) vec3c.y() - d1, (double) vec3c.z() - d2);
            // // poseStack.scale(0.25f, 0.25f, 0.25f);
            // // poseStack.translate(2f, (double) vec3c.y() - d1, 2f);
            //
            // Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
            //         event.getPoseStack().last(),
            //         multiBufferSource.getBuffer(RenderType.solid()), null,
            //         Minecraft.getInstance().getModelManager().getModel(ModelManager.butterfly),
            //         1f, 1f, 1f, event.getRenderTick(), OverlayTexture.NO_OVERLAY
            // );
            //
            //
            // poseStack.popPose();
        }
    }
}
