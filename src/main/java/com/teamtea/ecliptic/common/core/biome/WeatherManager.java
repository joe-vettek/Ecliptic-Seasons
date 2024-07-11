package com.teamtea.ecliptic.common.core.biome;

import com.teamtea.ecliptic.api.solar.SolarTerm;
import com.teamtea.ecliptic.common.handler.SolarUtil;
import com.teamtea.ecliptic.common.network.BiomeWeatherMessage;
import com.teamtea.ecliptic.common.network.SimpleNetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class WeatherManager {

    public static Map<Level, ArrayList<BiomeWeather>> BIOME_WEATHER_LIST = new LinkedHashMap<>();
    public static Map<Level, Integer> NEXT_CHECK_BIOME_MAP = new HashMap<>();

    public static ArrayList<BiomeWeather> getBiomeList(Level level) {
        if (level == null) return BIOME_WEATHER_LIST.entrySet().stream().findFirst().get().getValue();
        return BIOME_WEATHER_LIST.get(level);
    }


    public static Float getRainLevel(float p46723, ServerLevel serverLevel) {
        if (getBiomeList(serverLevel) != null)
            for (BiomeWeather biomeWeather : getBiomeList(serverLevel)) {
                if (biomeWeather.shouldRain()) {
                    return 1.0f;
                }
            }
        return 0.0f;
    }

    public static Boolean isRaining(ServerLevel serverLevel) {
        if (getBiomeList(serverLevel) != null)
            for (BiomeWeather biomeWeather : getBiomeList(serverLevel)) {
                if (biomeWeather.shouldRain()) {
                    return true;
                }
            }
        return false;
    }

    public static Boolean isRainingAt(BlockPos pos, ServerLevel serverLevel) {
        if (!serverLevel.canSeeSky(pos)) {
            return false;
        } else if (serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return false;
        }
        var biome = serverLevel.getBiome(pos);
        // for (BiomeWeather biomeWeather : BIOME_WEATHER_LIST) {
        //     Ecliptic.logger(biomeWeather.biomeHolder,biomeWeather.biomeHolder.get().getModifiedClimateSettings().temperature());
        // }
        if (getBiomeList(serverLevel) != null)
            for (BiomeWeather biomeWeather : getBiomeList(serverLevel)) {
                if (biome == biomeWeather.biomeHolder) {
                    return biomeWeather.shouldRain();
                }
            }
        return false;
    }

    public static Biome.Precipitation getPrecipitationAt(Biome biome, BlockPos p198905) {
        for (BiomeWeather biomeWeather : getBiomeList(null)) {
            if (biomeWeather.shouldRain()) {
                return Biome.Precipitation.RAIN;
            }
        }
        return Biome.Precipitation.NONE;
    }

    public static void createLevelBiomeWeatherList(Level level) {
        var list = new ArrayList<WeatherManager.BiomeWeather>();
        WeatherManager.BIOME_WEATHER_LIST.put(level, list);
        {
            var biomes = level.registryAccess().registry(Registries.BIOME);
            if (biomes.isPresent()) {
                for (Biome biome : biomes.get()) {
                    var loc = biomes.get().getKey(biome);
                    var id = biomes.get().getId(biome);
                    var biomeHolder = biomes.get().getHolder(ResourceKey.create(Registries.BIOME, biomes.get().getKey(biome)));
                    if (biomeHolder.isPresent()) {
                        var biomeWeather = new WeatherManager.BiomeWeather(biomeHolder.get());
                        biomes.get().getId(biome);
                        biomeWeather.location = loc;
                        biomeWeather.id = id;
                        list.add(biomeWeather);
                    }
                }
            }
        }
    }

    public static void informUpdateBiomes(RegistryAccess registryAccess) {
        var biomes = registryAccess.registry(Registries.BIOME);
        if (biomes.isPresent()) {
            biomes.get().forEach(biome ->
            {
                var loc = biomes.get().getKey(biome);
                var id = biomes.get().getId(biome);
                biomes.get().getHolder(ResourceKey.create(Registries.BIOME, biomes.get().getKey(biome))).ifPresent(biomeHolder -> {

                    WeatherManager.BIOME_WEATHER_LIST.entrySet().stream().forEach(levelArrayListEntry ->
                    {
                        var biomeWeathers = levelArrayListEntry.getValue();

                        boolean inList = false;

                        for (BiomeWeather biomeWeather : biomeWeathers) {
                            // 这里需要根据holder确定一下
                            if (biomeWeather.location.equals(loc)) {
                                biomeWeather.id = id;
                                biomeWeather.biomeHolder = biomeHolder;
                                inList = true;
                                break;
                            }
                        }
                        if (!inList) {
                            var biomeWeather = new WeatherManager.BiomeWeather(biomeHolder);
                            biomes.get().getId(biome);
                            biomeWeather.location = loc;
                            biomeWeather.id = id;
                            biomeWeathers.add(biomeWeather);
                        }
                    });
                });

            });
        }
    }


    public static class BiomeWeather implements INBTSerializable<CompoundTag> {
        public Holder<Biome> biomeHolder;
        public int id;
        public ResourceLocation location;
        public int rainTime = 0;
        public int thunderTime = 0;
        public int clearTime = 0;
        public byte snowDepth = 0;

        public BiomeWeather(Holder<Biome> biomeHolder) {
            this.biomeHolder = biomeHolder;
        }

        // 雨天也可能是晴天
        public boolean shouldRain() {
            return rainTime > 0;
        }

        public boolean shouldThunder() {
            return thunderTime > 0;
        }

        public boolean shouldClear() {
            return clearTime > 0;
        }


        @Override
        public String toString() {
            return serializeNBT().toString();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("biome", location.toString());
            tag.putInt("rainTime", rainTime);
            tag.putInt("thunderTime", thunderTime);
            tag.putInt("clearTime", clearTime);
            tag.putByte("snowDepth", snowDepth);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            location = new ResourceLocation(nbt.getString("biome"));
            rainTime = nbt.getInt("rainTime");
            thunderTime = nbt.getInt("thunderTime");
            clearTime = nbt.getInt("clearTime");
            snowDepth = nbt.getByte("snowDepth");
        }
    }

    public static Boolean onCheckWarmEnoughToRain(BlockPos p198905) {
        // return SolarTerm.get(AllListener.provider.resolve().get().worldSolarTime.getSolarTermIndex()).getSeason() != Season.WINTER;
        return true;
    }

    public static boolean onShouldSnow(ServerLevel level, Biome biome, BlockPos pos) {
        // return SolarTerm.get(AllListener.provider.resolve().get().worldSolarTime.getSolarTermIndex()).getSeason() == Season.WINTER;
        return true;
    }

    public static boolean agentAdvanceWeatherCycle(ServerLevel level, ServerLevelData serverLevelData, WritableLevelData levelData, RandomSource random) {

        int pos = NEXT_CHECK_BIOME_MAP.getOrDefault(level, -1);

        var levelBiomeWeather = getBiomeList(level);

        if (pos >= 0 && levelBiomeWeather != null && pos < levelBiomeWeather.size()) {
            var w = getBiomeList(level).get(pos);

            if (w.shouldClear()) {
                w.clearTime--;
            } else {
                if (w.shouldRain()) {
                    w.rainTime--;
                } else {
                    w.rainTime = ServerLevel.RAIN_DURATION.sample(random);
                }
            }
            // if (w.biomeHolder.is(Biomes.PLAINS)){
            //     Ecliptic.logger(w);
            // }
            if (!w.shouldRain() && !w.shouldClear()) {
                w.clearTime = ServerLevel.RAIN_DELAY.sample(random);
            }

            pos++;

        } else {
            pos = 0;
        }

        // Ecliptic.logger(level.getGameTime(),level.getGameTime() & 100);
        if (levelBiomeWeather != null && (level.getGameTime() % 100) == 0 && !level.players().isEmpty()) {
            // Ecliptic.logger(level.getGameTime());
            sendBiomePacket(levelBiomeWeather, level.players());
        }

        NEXT_CHECK_BIOME_MAP.put(level, pos);
        return true;
    }

    public static void sendBiomePacket(ArrayList<BiomeWeather> levelBiomeWeather, List<ServerPlayer> players) {
        byte[] rains = new byte[levelBiomeWeather.size()];
        byte[] thunders = new byte[levelBiomeWeather.size()];
        byte[] clears = new byte[levelBiomeWeather.size()];
        byte[] snows = new byte[levelBiomeWeather.size()];
        for (BiomeWeather biomeWeather : levelBiomeWeather) {
            int index = biomeWeather.id;
            rains[index] = (byte) (biomeWeather.shouldRain() ? 1 : 0);
            thunders[index] = (byte) (biomeWeather.shouldThunder() ? 1 : 0);
            clears[index] = (byte) (biomeWeather.shouldClear() ? 1 : 0);
            snows[index] = biomeWeather.snowDepth;
        }
        var msg = new BiomeWeatherMessage(rains, thunders, clears, snows);
        SimpleNetworkHandler.send(players, msg);
    }

    public enum SnowRenderStatus {
        SNOW,
        SNOW_MELT,
        // RAIN,
        // CLOUD,
        NONE
    }

    public static boolean isInTerms(SolarTerm start, SolarTerm end, SolarTerm check) {
        if (start.ordinal() <= end.ordinal()) {
            return start.ordinal() <= check.ordinal() && check.ordinal() <= end.ordinal();
        } else
            return start.ordinal() <= check.ordinal() || check.ordinal() <= end.ordinal();
    }

    public static SnowRenderStatus getSnowStatus(ServerLevel level, Biome biome, BlockPos pos) {
        var provider = SolarUtil.getProvider(level);
        var status = SnowRenderStatus.NONE;
        if (provider != null) {
            if (level.isRaining()) {
                if (isInTerms(SolarTerm.LIGHT_SNOW, SolarTerm.GREATER_COLD, provider.getSolarTerm())) {
                    status = SnowRenderStatus.SNOW;
                }
            }
            if (isInTerms(SolarTerm.RAIN_WATER, SolarTerm.AUTUMNAL_EQUINOX, provider.getSolarTerm())) {
                status = SnowRenderStatus.SNOW_MELT;
            }
        }
        return status;
    }
}
