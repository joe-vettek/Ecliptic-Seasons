package com.teamtea.ecliptic.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static final ForgeConfigSpec SERVER_CONFIG = new ForgeConfigSpec.Builder().configure(ServerConfig::new).getRight();

    protected ServerConfig(ForgeConfigSpec.Builder builder) {
        Temperature.load(builder);
        Season.load(builder);
    }


    public static class Temperature {
        public static ForgeConfigSpec.BooleanValue iceMelt;

        private static void load(ForgeConfigSpec.Builder builder) {
            builder.push("Temperature");
            iceMelt = builder.comment("Ice or snow layer will melt in warm place..")
                    .define("IceAndSnowMelt", false);
            builder.pop();
        }
    }

    public static class Season {
        public static ForgeConfigSpec.BooleanValue enable;
        public static ForgeConfigSpec.IntValue lastingDaysOfEachTerm;
        public static ForgeConfigSpec.IntValue initialSolarTermIndex;

        private static void load(ForgeConfigSpec.Builder builder) {
            builder.push("Season");
            enable = builder.comment("Enable solar term season system.")
                    .define("EnableSeason", true);
            lastingDaysOfEachTerm = builder.comment("The lasting days of each term (24 in total).")
                    .defineInRange("LastingDaysOfEachTerm", 7, 1, 30);
            initialSolarTermIndex = builder.comment("The index of the initial solar term.")
                    .defineInRange("InitialSolarTermIndex", 1, 1, 24);
            builder.pop();
        }
    }

}

