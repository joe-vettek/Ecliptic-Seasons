package com.teamtea.eclipticseasons.client.render;

import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostPass;

public class ClientRenderer {
    public static long reMainTick = 0;

    private static float getProgress(boolean fadeIn) {
        return Math.min(fadeIn ? (1 - reMainTick / 100f) : reMainTick / 100f, 1);
    }

    public static final int NONE_BLUR = 1;
    public static final int ON_BLUR = 2;
    public static final int TO_BLUR = 3;
    public static final int CLEAR_BLUR = 3;

    public static int oldBlurStatus = NONE_BLUR;

    public static void applyEffect(GameRenderer gameRenderer, LocalPlayer player) {
        if (player == null) return;


        int blurStatus =
                ServerConfig.Temperature.heatStroke.get()&&
                player.hasEffect(EclipticSeasons.EffectRegistry.HEAT_STROKE)
                ? ON_BLUR : NONE_BLUR;
        if (blurStatus != oldBlurStatus) {
            if (blurStatus == ON_BLUR) {
                {
                    gameRenderer.loadEffect(EclipticSeasons.rl("shaders/post/fade_in_blur.json"));
                }
            }

            if (reMainTick > 0) {
                reMainTick--;
            } else reMainTick = 100;

            float progress = getProgress(blurStatus == ON_BLUR) * 0.03f;
            // if (progress != prevProgress)
            {
                // prevProgress = progress;
                updateUniform("Progress", progress);
            }
            // EclipticSeasons.logger(reMainTick, progress, blurStatus, oldBlurStatus);
            if (reMainTick == 0) {
                oldBlurStatus = blurStatus;
                if (oldBlurStatus == NONE_BLUR) {
                    gameRenderer.shutdownEffect();
                }
            }
        }


    }

    public static void updateUniform(String name, float value) {
        var postChain = Minecraft.getInstance().gameRenderer.currentEffect();
        if (postChain != null)
            for (PostPass postPass : postChain.passes) {
                var uniform = postPass.getEffect().getUniform(name);
                if (uniform != null) {
                    uniform.set(value);
                }
            }
    }


    // public static void renderFogColors(Camera camera, float partialTick, ViewportEvent.ComputeFogColor event) {
    //     if (camera.getEntity() instanceof Player player && camera.getFluidInCamera() == FogType.NONE && ClientEventHandler.prevFogDensity > 0f) {
    //         // Calculate color based on time of day
    //         final float angle = player.level.getSunAngle(partialTick);
    //         final float height = Mth.cos(angle);
    //         final float delta = Mth.clamp((height + 0.4f) / 0.8f, 0, 1);
    //
    //         final int colorDay = 0xbfbfd8;
    //         final int colorNight = 0x0c0c19;
    //         final float red = ((colorDay >> 16) & 0xFF) * delta + ((colorNight >> 16) & 0xFF) * (1 - delta);
    //         final float green = ((colorDay >> 8) & 0xFF) * delta + ((colorNight >> 8) & 0xFF) * (1 - delta);
    //         final float blue = (colorDay & 0xFF) * delta + (colorNight & 0xFF) * (1 - delta);
    //
    //         event.setRed(red / 255f);
    //         event.setBlue(blue / 255f);
    //         event.setGreen(green / 255f);
    //
    //         ClientEventHandler.r = red / 255f;
    //         ClientEventHandler.g = blue / 255f;
    //         ClientEventHandler.b = green / 255f;
    //     }
    // }
    //
    // public static void renderFogDensity(Camera camera, ViewportEvent.RenderFog event) {
    //     if (camera.getEntity() instanceof Player player) {
    //         final long thisTick = Util.getMillis();
    //         final boolean firstTick = ClientEventHandler.prevFogTick == -1;
    //         final float deltaTick = firstTick ? 1e10f : (thisTick - ClientEventHandler.prevFogTick) * 0.00015f;
    //
    //         ClientEventHandler.prevFogTick = thisTick;
    //
    //         float expectedFogDensity = 0f;
    //
    //         final Level level = player.level;
    //         final Biome biome = level.getBiome(camera.getBlockPosition()).value();
    //         if (level.isRaining() && biome.coldEnoughToSnow(camera.getBlockPosition())) {
    //             final int light = level.getBrightness(LightLayer.SKY,new BlockPos(player.getEyePosition()));
    //             expectedFogDensity = Mth.clampedMap(light, 0f, 15f, 0f, 1f);
    //         }
    //
    //         // Smoothly interpolate fog towards the expected value - increasing faster than it decreases
    //         if (expectedFogDensity > ClientEventHandler.prevFogDensity) {
    //             ClientEventHandler.prevFogDensity = Math.min(ClientEventHandler.prevFogDensity + 4f * deltaTick, expectedFogDensity);
    //         } else if (expectedFogDensity < ClientEventHandler.prevFogDensity) {
    //             ClientEventHandler.prevFogDensity = Math.max(ClientEventHandler.prevFogDensity - deltaTick, expectedFogDensity);
    //         }
    //
    //         if (camera.getFluidInCamera() != FogType.NONE) {
    //             ClientEventHandler.prevFogDensity = -1; // Immediately cancel fog if there's another fog effect going on
    //             ClientEventHandler.prevFogTick = -1;
    //         }
    //
    //         if (ClientEventHandler.prevFogDensity > 0) {
    //             final float scaledDelta = 1 - (1 - ClientEventHandler.prevFogDensity) * (1 - ClientEventHandler.prevFogDensity);
    //             final float fogDensity = 0.1f;
    //             final float farPlaneScale = Mth.lerp(scaledDelta, 1f, fogDensity);
    //             final float nearPlaneScale = Mth.lerp(scaledDelta, 1f, 0.3f * fogDensity);
    //
    //             event.scaleNearPlaneDistance(nearPlaneScale);
    //             event.scaleFarPlaneDistance(farPlaneScale);
    //             event.setCanceled(true);
    //         }
    //     }
    // }
}
