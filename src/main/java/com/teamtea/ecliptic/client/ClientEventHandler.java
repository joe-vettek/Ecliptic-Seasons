package com.teamtea.ecliptic.client;


import com.teamtea.ecliptic.client.core.ModelManager;
import com.teamtea.ecliptic.common.AllListener;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import com.teamtea.ecliptic.common.core.solar.ClientSolarDataManager;
import com.teamtea.ecliptic.config.ClientConfig;
import com.teamtea.ecliptic.config.ServerConfig;
import com.teamtea.ecliptic.common.core.crop.CropInfoManager;
import com.teamtea.ecliptic.api.constant.crop.CropSeasonInfo;
import com.teamtea.ecliptic.api.constant.crop.CropHumidityInfo;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID, value = Dist.CLIENT)
public final class ClientEventHandler {

    public static float prevFogDensity = -1f;
    public static long prevFogTick = -1L;

    public static float r = 0.0f;
    public static float g = 0.0f;
    public static float b = 0.0f;

    @SubscribeEvent
    public static void onFogEvent(ViewportEvent.ComputeFogColor event) {
        renderFogColors(event.getCamera(), (float) event.getPartialTick(), event);
        prevFogDensity=0.0f;
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

            r = red/ 255f ;
            g = blue/ 255f ;
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
}
