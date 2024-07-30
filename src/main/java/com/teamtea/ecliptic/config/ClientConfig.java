package com.teamtea.ecliptic.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig
{

    public static final ForgeConfigSpec CLIENT_CONFIG = new ForgeConfigSpec.Builder().configure(ClientConfig::new).getRight();

    protected ClientConfig(ForgeConfigSpec.Builder builder)
    {
        GUI.load(builder);
        Renderer.load(builder);
        Sound.load(builder);
    }

    public static class GUI
    {
        public static ForgeConfigSpec.IntValue playerTemperatureX;
        public static ForgeConfigSpec.IntValue playerTemperatureY;
        public static ForgeConfigSpec.BooleanValue debugInfo;

        private static void load(ForgeConfigSpec.Builder builder)
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
        public static ForgeConfigSpec.BooleanValue forceChunkRenderUpdate;
        public static ForgeConfigSpec.BooleanValue snowyWinter;
        public static ForgeConfigSpec.BooleanValue deeperSnow;

        private static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("Renderer");
            forceChunkRenderUpdate = builder.comment("Force to update chunk rendering.")
                    .define("ForceChunkRenderUpdate", true);
            snowyWinter = builder.comment("If snow falls during cold weather, it will gradually cover all solid blocks and grass.")
                    .define("snowyWinter", true);
            deeperSnow = builder.comment("Occasionally a thicker layer of snow will cover the flowers and grass, especially.")
                    .define("deeperSnow", false);
            builder.pop();
        }
    }

    public static class Sound
    {
        public static ForgeConfigSpec.BooleanValue sound;

        private static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("Sound");
            sound = builder.comment("Ambient Sound.")
                    .define("Sound", true);
            builder.pop();
        }
    }
}
