package com.teamtea.eclipticseasons.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public static final ModConfigSpec SERVER_CONFIG = new ModConfigSpec.Builder().configure(ServerConfig::new).getRight();

    protected ServerConfig(ModConfigSpec.Builder builder) {
        Temperature.load(builder);
        Season.load(builder);
        Debug.load(builder);
    }

    public static class Debug {
        public static ModConfigSpec.BooleanValue debugMode;
        public static ModConfigSpec.BooleanValue useSolarWeather;

        private static void load(ModConfigSpec.Builder builder) {
            builder.push("Debug");
            debugMode = builder.comment("Enable debug option to detect illegal use of functions.")
                    .define("Debug", false);
            useSolarWeather = builder.comment("Enable solar term weather system.")
                    .define("UseSolarWeather", true);
            builder.pop();
        }
    }

    public static class Temperature {
        public static ModConfigSpec.BooleanValue iceMelt;
        public static ModConfigSpec.BooleanValue heatStroke;

        private static void load(ModConfigSpec.Builder builder) {
            builder.push("Temperature");
            iceMelt = builder.comment("Ice or snow layer will melt in warm place..")
                    .define("IceAndSnowMelt", false);
            heatStroke = builder.comment("Add heat stroke effect in summer noon while in hot biome.")
                    .define("HeatStroke", true);
            builder.pop();
        }
    }

    public static class Season {
        public static ModConfigSpec.BooleanValue enableCrop;
        public static ModConfigSpec.BooleanValue enableInform;
        public static ModConfigSpec.IntValue lastingDaysOfEachTerm;
        public static ModConfigSpec.IntValue initialSolarTermIndex;
        public static ModConfigSpec.IntValue rainChanceMultiplier;

        private static void load(ModConfigSpec.Builder builder) {
            builder.push("Season");
            lastingDaysOfEachTerm = builder.comment("The lasting days of each term (24 in total).")
                    .defineInRange("LastingDaysOfEachTerm", 7, 1, 1000);
            initialSolarTermIndex = builder.comment("The index of the initial solar term.")
                    .defineInRange("InitialSolarTermIndex", 1, 1, 24);

            enableCrop = builder.comment("Enable seasonal crop.")
                    .define("EnableSeasonalCrop", true);

            enableInform = builder.comment("Enable solar term change inform.")
                    .define("EnableInform", true);

            rainChanceMultiplier = builder.comment("Set the percentage multiplier of the probability of rain, the range should be between 0 and 1000.")
                    .defineInRange("RainChancePercentMultiplier", 60, 0, 1000);


            builder.pop();
        }
    }

}

