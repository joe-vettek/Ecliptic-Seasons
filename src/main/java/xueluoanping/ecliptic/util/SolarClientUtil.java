package xueluoanping.ecliptic.util;


public class SolarClientUtil {

    public static float snowLayer = 0.0f;

    public static float getSnowLayer() {
        return snowLayer;
    }

    public static boolean updateSnowLayer(float snow) {
        snowLayer = snow;
        return true;
    }

}
