package xueluoanping.ecliptic.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
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

}
