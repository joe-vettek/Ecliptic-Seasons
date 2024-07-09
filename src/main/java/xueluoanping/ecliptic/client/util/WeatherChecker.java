package xueluoanping.ecliptic.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import xueluoanping.ecliptic.Ecliptic;

// TODO:全局雨量控制表
public class WeatherChecker {

    public static float lastBiomeRainLevel = -1;
    public static float nowBiomeRainLevel = 0;
    public static int changeTime = 0;
    public static int MAX_CHANGE_TIME = 200;

    public static Boolean isLocalBiomeRain(Holder<Biome> biomeHolder) {
        return biomeHolder.is(Tags.Biomes.IS_DESERT);
    }

    public static Boolean isRainStandard(ClientLevel clientLevel) {
        if (Minecraft.getInstance().cameraEntity instanceof Player player) {
            if (clientLevel.getBiome(player.getOnPos()).is(Tags.Biomes.IS_DESERT)) {
                return false;
            }
        }
        return true;
    }

    public static Boolean isRain(ClientLevel clientLevel) {
        if (!isRainStandard(clientLevel)) {
            // if (clientLevel.getBiome(player.getOnPos()).is(Tags.Biomes.IS_DESERT)) {
            //     return false;
            // }
            return false;
        }
        return (double) getRainLevel(1.0F, clientLevel) > 0.2D;
    }

    public static boolean isNear(float a, float b, float interval) {
        return Math.abs(a - b) < interval;
    }


    //   TODO：net.minecraft.client.renderer.LevelRenderer.renderSnowAndRain 可以参考平滑方式
    public static Float getRainLevel(float p46723, ClientLevel clientLevel) {
        // if (Minecraft.getInstance().cameraEntity instanceof Player player &&clientLevel.getBiome(Minecraft.getInstance().cameraEntity.getOnPos()).is(Biomes.PLAINS) )return 0.01f;
        float rainLevel = getStandardRainLevel(p46723, clientLevel, null);
        if (Minecraft.getInstance().cameraEntity instanceof Player player) {
            // Ecliptic.logger(clientLevel.getNoiseBiome((int) player.getX(), (int) player.getY(), (int) player.getZ()));
            // TODO：根据群系过渡计算雨量（也许需要维护一个群系位置）,目前设置为时间平滑
            var standBiome = clientLevel.getBiome(player.getOnPos());
            rainLevel = getStandardRainLevel(p46723, clientLevel, standBiome);
            if (changeTime > 0) {
                changeTime--;
                if (lastBiomeRainLevel >= 0 && !isNear(rainLevel, lastBiomeRainLevel, 0.01f)) {
                    rainLevel = rainLevel + (lastBiomeRainLevel - rainLevel) * 0.9999f;
                }
                // else
                {
                    lastBiomeRainLevel = rainLevel;
                }

            } else {
                if (rainLevel != lastBiomeRainLevel) {
                    // 设置了一个极限时间，可能需要看情况
                    changeTime = MAX_CHANGE_TIME;
                    rainLevel = lastBiomeRainLevel;
                }
            }
        }
        return rainLevel;
    }

    public static Float getStandardRainLevel(float p46723, ClientLevel clientLevel, Holder<Biome> biomeHolder) {
        if (biomeHolder != null && biomeHolder.is(Tags.Biomes.IS_DESERT)) {
            return 0.0f;
        }
        return Mth.lerp(p46723, clientLevel.oRainLevel, clientLevel.rainLevel);
    }

    public static void renderSnowAndRain(LevelRenderer levelRenderer, int ticks, float[] rainSizeX, float[] rainSizeZ, ResourceLocation RAIN_LOCATION, ResourceLocation SNOW_LOCATION, LightTexture p_109704_, float p_109705_, double p_109706_, double p_109707_, double p_109708_) {
        float f = Minecraft.getInstance().level.getRainLevel(p_109705_);
        float f_all = 1.0f;
        // if (!(f <= 0.0F))
        {
            p_109704_.turnOnLightLayer();
            Level level = Minecraft.getInstance().level;
            int i = Mth.floor(p_109706_);
            int j = Mth.floor(p_109707_);
            int k = Mth.floor(p_109708_);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            int l = 5;
            if (Minecraft.useFancyGraphics()) {
                l = 10;
            }

            RenderSystem.depthMask(Minecraft.useShaderTransparency());
            int i1 = -1;
            float f1 = (float) ticks + p_109705_;
            RenderSystem.setShader(GameRenderer::getParticleShader);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int j1 = k - l; j1 <= k + l; ++j1) {
                for (int k1 = i - l; k1 <= i + l; ++k1) {
                    int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                    double d0 = (double) rainSizeX[l1] * 0.5D;
                    double d1 = (double) rainSizeZ[l1] * 0.5D;
                    blockpos$mutableblockpos.set((double) k1, p_109707_, (double) j1);
                    var biome = level.getBiome(blockpos$mutableblockpos);
                    if (biome.get().hasPrecipitation()) {
                        int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
                        int j2 = j - l;
                        int k2 = j + l;
                        if (j2 < i2) {
                            j2 = i2;
                        }

                        if (k2 < i2) {
                            k2 = i2;
                        }

                        int l2 = i2;
                        if (i2 < j) {
                            l2 = j;
                        }

                        if (j2 != k2) {
                            RandomSource randomsource = RandomSource.create((long) (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
                            blockpos$mutableblockpos.set(k1, j2, j1);
                            Biome.Precipitation biome$precipitation = biome.get().getPrecipitationAt(blockpos$mutableblockpos);
                            boolean small=false;

                            try {
                                small=biome.is(Biomes.DESERT);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (biome$precipitation == Biome.Precipitation.RAIN) {
                                if (i1 != 0) {
                                    if (i1 >= 0) {
                                        tesselator.end();
                                    }

                                    i1 = 0;
                                    RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }

                                int i3 = ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                                float f2 = -((float) i3 + p_109705_) / 32.0F * (3.0F + randomsource.nextFloat());
                                double d2 = (double) k1 + 0.5D - p_109706_;
                                double d4 = (double) j1 + 0.5D - p_109708_;
                                float f3 = (float) Math.sqrt(d2 * d2 + d4 * d4) / (float) l;

                                float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * (small?f:f_all);
                                blockpos$mutableblockpos.set(k1, l2, j1);
                                int j3 = levelRenderer.getLightColor(level, blockpos$mutableblockpos);
                                bufferbuilder.vertex((double) k1 - p_109706_ - d0 + 0.5D, (double) k2 - p_109707_, (double) j1 - p_109708_ - d1 + 0.5D).uv(0.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex((double) k1 - p_109706_ + d0 + 0.5D, (double) k2 - p_109707_, (double) j1 - p_109708_ + d1 + 0.5D).uv(1.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex((double) k1 - p_109706_ + d0 + 0.5D, (double) j2 - p_109707_, (double) j1 - p_109708_ + d1 + 0.5D).uv(1.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex((double) k1 - p_109706_ - d0 + 0.5D, (double) j2 - p_109707_, (double) j1 - p_109708_ - d1 + 0.5D).uv(0.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                            } else if (biome$precipitation == Biome.Precipitation.SNOW) {
                                if (i1 != 1) {
                                    if (i1 >= 0) {
                                        tesselator.end();
                                    }

                                    i1 = 1;
                                    RenderSystem.setShaderTexture(0, SNOW_LOCATION);
                                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }

                                float f5 = -((float) (ticks & 511) + p_109705_) / 512.0F;
                                float f6 = (float) (randomsource.nextDouble() + (double) f1 * 0.01D * (double) ((float) randomsource.nextGaussian()));
                                float f7 = (float) (randomsource.nextDouble() + (double) (f1 * (float) randomsource.nextGaussian()) * 0.001D);
                                double d3 = (double) k1 + 0.5D - p_109706_;
                                double d5 = (double) j1 + 0.5D - p_109708_;
                                float f8 = (float) Math.sqrt(d3 * d3 + d5 * d5) / (float) l;
                                float f9 = ((1.0F - f8 * f8) * 0.3F + 0.5F) * (small?f:f_all);
                                blockpos$mutableblockpos.set(k1, l2, j1);
                                int k3 = levelRenderer.getLightColor(level, blockpos$mutableblockpos);
                                int l3 = k3 >> 16 & '\uffff';
                                int i4 = k3 & '\uffff';
                                int j4 = (l3 * 3 + 240) / 4;
                                int k4 = (i4 * 3 + 240) / 4;
                                bufferbuilder.vertex((double) k1 - p_109706_ - d0 + 0.5D, (double) k2 - p_109707_, (double) j1 - p_109708_ - d1 + 0.5D).uv(0.0F + f6, (float) j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double) k1 - p_109706_ + d0 + 0.5D, (double) k2 - p_109707_, (double) j1 - p_109708_ + d1 + 0.5D).uv(1.0F + f6, (float) j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double) k1 - p_109706_ + d0 + 0.5D, (double) j2 - p_109707_, (double) j1 - p_109708_ + d1 + 0.5D).uv(1.0F + f6, (float) k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double) k1 - p_109706_ - d0 + 0.5D, (double) j2 - p_109707_, (double) j1 - p_109708_ - d1 + 0.5D).uv(0.0F + f6, (float) k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            }
                        }
                    }
                }
            }

            if (i1 >= 0) {
                tesselator.end();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            p_109704_.turnOffLightLayer();
        }
    }
}
