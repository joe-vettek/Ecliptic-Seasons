package com.teamtea.ecliptic.client.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.teamtea.ecliptic.Ecliptic;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.Tags;

import java.util.List;

// TODO:全局雨量控制表
public class ClientWeatherChecker {

    public static float lastBiomeRainLevel = -1;
    public static float nowBiomeRainLevel = 0;
    public static int changeTime = 0;
    public static int MAX_CHANGE_TIME = 200;

    // public static Boolean isLocalBiomeRain(Holder<Biome> biomeHolder) {
    //     return biomeHolder.is(Tags.Biomes.IS_DESERT);
    // }
    //
    // public static Boolean isRainStandard(ClientLevel clientLevel) {
    //     if (Minecraft.getInstance().cameraEntity instanceof Player player) {
    //         if (clientLevel.getBiome(player.getOnPos()).is(Tags.Biomes.IS_DESERT)) {
    //             return false;
    //         }
    //     }
    //     return true;
    // }

    public static Boolean isRain(ClientLevel clientLevel) {
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
            var pos=player.getOnPos();
            for (BlockPos blockPos : List.of(pos.east(4), pos.north(4), pos.south(4), pos.west(4))) {
                var standBiome = clientLevel.getBiome(blockPos);
                float orainLevel = getStandardRainLevel(p46723, clientLevel, standBiome);
                if (orainLevel>rainLevel){
                    rainLevel=orainLevel;
                }
            }


            if (changeTime > 0) {
                changeTime--;
                if (lastBiomeRainLevel >= 0 && !isNear(rainLevel, lastBiomeRainLevel, 0.01f)) {
                    rainLevel = rainLevel + (lastBiomeRainLevel - rainLevel) * 0.99f;
                }
                // else
                {
                    lastBiomeRainLevel = rainLevel;
                    // Ecliptic.logger(lastBiomeRainLevel,rainLevel);
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
        // if (biomeHolder != null && biomeHolder.is(Tags.Biomes.IS_DESERT)) {
        //     return 0.0f;
        // }
        // return Mth.lerp(p46723, clientLevel.oRainLevel, clientLevel.rainLevel);
        var lists = WeatherManager.getBiomeList(clientLevel);
        if (lists != null)
            for (WeatherManager.BiomeWeather biomeWeather : lists) {
                if (biomeWeather.biomeHolder == biomeHolder) {
                    return biomeWeather.rainTime > 0 ? 1.0f : 0.0f;
                }
            }
        return 0.0f;
    }


    public static Boolean isRainingAt(BlockPos p46759, ClientLevel clientLevel) {
        if (!clientLevel.canSeeSky(p46759)) {
            return false;
        } else if (clientLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, p46759).getY() > p46759.getY()) {
            return false;
        }
        return clientLevel.getBiome(p46759).get().getPrecipitationAt(p46759)== Biome.Precipitation.RAIN;
    }
}
