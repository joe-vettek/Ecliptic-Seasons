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
        public static ModConfigSpec.IntValue playerTemperatureX;
        public static ModConfigSpec.IntValue playerTemperatureY;
        public static ModConfigSpec.BooleanValue debugInfo;

        private static void load(ModConfigSpec.Builder builder)
        {
            builder.push("GUI");
            playerTemperatureX = builder.comment("The position X of Player Temperature UI")
                    .defineInRange("PlayerTemperatureX", 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
            playerTemperatureY = builder.comment("The position Y of Player Temperature UI")
                    .defineInRange("PlayerTemperatureY", 40, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
        public static ModConfigSpec.BooleanValue underSnow;
        public static ModConfigSpec.BooleanValue particle;
        public static ModConfigSpec.IntValue offset;

        private static void load(ModConfigSpec.Builder builder)
        {
            builder.push("Renderer");
            forceChunkRenderUpdate = builder.comment("Force to update chunk rendering.")
                    .define("ForceChunkRenderUpdate", true);
            useVanillaCheck = builder.comment("Force to update chunk rendering.")
                    .define("useVanillaCheck", false);
            snowyWinter = builder.comment("If snow falls during cold weather, it will gradually cover all solid blocks and grass.")
                    .define("SnowyWinter", true);
            deeperSnow = builder.comment("Occasionally a thicker layer of snow will cover the flowers and grass, especially.")
                    .define("DeeperSnow", false);
            underSnow = builder.comment("Blocks below fences and bamboo will also accumulate snow.")
                    .define("UnderSnow", false);
            particle = builder.comment("Particle.")
                    .define("Particle", true);
            offset = builder.comment("Offset.")
                    .defineInRange("Offset", 16,1,80);
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
