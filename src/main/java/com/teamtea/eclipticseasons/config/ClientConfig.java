package com.teamtea.eclipticseasons.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig
{

    public static final ModConfigSpec CLIENT_CONFIG = new ModConfigSpec.Builder().configure(ClientConfig::new).getRight();

    protected ClientConfig(ModConfigSpec.Builder builder)
    {
        GUI.load(builder);
        Renderer.load(builder);
        Sound.load(builder);
    }

    public static class GUI
    {
        public static ModConfigSpec.BooleanValue debugInfo;

        private static void load(ModConfigSpec.Builder builder)
        {
            builder.push("GUI");
            debugInfo = builder.comment("Info used for development.")
                    .define("DebugInfo", false);
            builder.pop();
        }
    }

    public static class Renderer
    {
        public static ModConfigSpec.BooleanValue forceChunkRenderUpdate;
        public static ModConfigSpec.BooleanValue useVanillaCheck;
        public static ModConfigSpec.BooleanValue snowyWinter;
        public static ModConfigSpec.BooleanValue deeperSnow;
        public static ModConfigSpec.BooleanValue seasonParticle;
        public static ModConfigSpec.IntValue weatherBufferDistance;

        private static void load(ModConfigSpec.Builder builder)
        {
            builder.push("Renderer");
            forceChunkRenderUpdate = builder.comment("Force to update chunk rendering.")
                    .define("ForceChunkRenderUpdate", true);
            useVanillaCheck = builder.comment("Determines whether snow is falling based on vanilla lighting checks.")
                    .define("useVanillaCheck", false);
            snowyWinter = builder.comment("If snow falls during cold weather, it will gradually cover all solid blocks and grass.")
                    .define("SnowyWinter", true);
            deeperSnow = builder.comment("Occasionally a thicker layer of snow will cover the flowers and grass, especially.")
                    .define("DeeperSnow", false);
            seasonParticle = builder.comment("See butterflies in the spring, fireflies in the summer, and fallen leaves..")
                    .define("SeasonParticle", true);
            weatherBufferDistance = builder.comment("Modify the buffer distance for local weather changes.")
                    .defineInRange("WeatherBufferDistance", 16,1,80);
            builder.pop();
        }
    }

    public static class Sound
    {
        public static ModConfigSpec.BooleanValue sound;

        private static void load(ModConfigSpec.Builder builder)
        {
            builder.push("Sound");
            sound = builder.comment("Ambient Sound.")
                    .define("Sound", true);
            builder.pop();
        }
    }
}
