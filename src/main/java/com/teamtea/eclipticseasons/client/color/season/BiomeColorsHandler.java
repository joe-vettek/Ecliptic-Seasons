package com.teamtea.eclipticseasons.client.color.season;

import com.teamtea.eclipticseasons.api.util.EclipticTagClientTool;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.constant.solar.color.SolarTermColor;
import com.teamtea.eclipticseasons.api.constant.tag.SeasonTypeBiomeTags;
import com.teamtea.eclipticseasons.api.util.SimpleUtil;
import com.teamtea.eclipticseasons.client.core.ColorHelper;
import com.teamtea.eclipticseasons.client.core.ModelManager;
import com.teamtea.eclipticseasons.common.core.Holder;
import com.teamtea.eclipticseasons.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.MapColor;

import java.util.HashMap;
import java.util.Map;

public class BiomeColorsHandler {
    // public static int[] newFoliageBuffer = new int[65536];
    // public static int[] newGrassBuffer = new int[65536];
    public static Map<TagKey<Biome>, int[]> newFoliageBufferMap = new HashMap<>();
    public static Map<TagKey<Biome>, int[]> newGrassBufferMap = new HashMap<>();

    public static boolean needRefresh = false;

    public static final ColorResolver GRASS_COLOR = (biome, posX, posZ) ->
    {

        // if (SimpleUtil.getModsUse(0).getLast().contains("map")){
        //     return MapColor.SNOW.col;
        // }

        var clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            int originColor = biome.getGrassColor(posX, posZ);
            return Holder.getSaveDataLazy(clientLevel).map(data ->
            {
                if (needRefresh) {
                    reloadColors();
                }
                // 由于基本温度被更改
                double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature()+ SimpleUtil.getNowSolarTerm(clientLevel).getTemperatureChange(), 0.0F, 1.0F);
                double humidity = Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
                humidity = humidity * temperature;
                int i = (int) ((1.0D - temperature) * 255.0D);
                int j = (int) ((1.0D - humidity) * 255.0D);
                int k = j << 8 | i;

                int[] newGrassBuffer = newGrassBufferMap.getOrDefault(EclipticTagClientTool.getTag(biome), GrassColor.pixels);
                return k > newGrassBuffer.length ? -65281 : newGrassBuffer[k];
            }).orElse(originColor);
        } else return -1;
    };

    public static final ColorResolver FOLIAGE_COLOR = (biome, posX, posZ) ->
    {
        var clientLevel = Minecraft.getInstance().level;

        if (clientLevel != null) {
            int originColor = biome.getFoliageColor();
            return Holder.getSaveDataLazy(clientLevel).map(data ->
            {
                if (needRefresh) {
                    reloadColors();
                }
                double temperature = Mth.clamp(biome.getModifiedClimateSettings().temperature()+ SimpleUtil.getNowSolarTerm(clientLevel).getTemperatureChange(), 0.0F, 1.0F);
                double humidity = Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
                humidity = humidity * temperature;
                int i = (int) ((1.0D - temperature) * 255.0D);
                int j = (int) ((1.0D - humidity) * 255.0D);
                int k = j << 8 | i;
                
                int[] newFoliageBuffer = newFoliageBufferMap.getOrDefault(EclipticTagClientTool.getTag(biome), FoliageColor.pixels);
                return k > newFoliageBuffer.length ? originColor : newFoliageBuffer[k];
            }).orElse(originColor);
        } else return biome.getFoliageColor();
    };

    public static void reloadColors() {
        {
            var clientLevel = Minecraft.getInstance().level;
            if (clientLevel != null) {
                Holder.getSaveDataLazy(clientLevel).ifPresent(data ->
                {
                    for (TagKey<Biome> biomeTagKey : SeasonTypeBiomeTags.BIOMES) {
                        int[] newFoliageBuffer = new int[65536];
                        int[] newGrassBuffer = new int[65536];
                        int[] foliageBuffer = FoliageColor.pixels;
                        int[] grassBuffer = GrassColor.pixels;

                        SolarTerm solar = SolarTerm.get(data.getSolarTermIndex());
                        SolarTermColor colorInfo = solar.getSolarTermColor(biomeTagKey);
                        for (int i = 0; i < foliageBuffer.length; i++) {
                            int originColor = foliageBuffer[i];

                            if (colorInfo.getMix() == 0.0F) {
                                newFoliageBuffer[i] = originColor;
                            } else {
                                newFoliageBuffer[i] = ColorHelper.simplyMixColor(colorInfo.getBirchColor(), colorInfo.getMix(), originColor, 1.0F - colorInfo.getMix());
                            }
                        }

                        for (int i = 0; i < grassBuffer.length; i++) {
                            int originColor = grassBuffer[i];
                            if (colorInfo.getMix() == 0.0F) {
                                newGrassBuffer[i] = originColor;
                            } else {
                                newGrassBuffer[i] = ColorHelper.simplyMixColor(colorInfo.getColor(), colorInfo.getMix(), originColor, 1.0F - colorInfo.getMix());
                            }
                        }
                        newFoliageBufferMap.put(biomeTagKey, newFoliageBuffer);
                        newGrassBufferMap.put(biomeTagKey, newGrassBuffer);
                    }

                    needRefresh = false;
                });
            }
        }
    }

    public static MapColor getTopSnowColor(BlockGetter blockGetter, BlockState state, BlockPos pos) {
        boolean isLight = false;

        int flag = 0;
        var onBlock = state.getBlock();
        if (onBlock instanceof LeavesBlock) {
            flag = ModelManager.FLAG_LEAVES;
        } else if ((state.isSolidRender(blockGetter, pos)
                // state.isSolid()
                || onBlock instanceof LeavesBlock
                || (onBlock instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.TOP)
                || (onBlock instanceof StairBlock && state.getValue(StairBlock.HALF) == Half.TOP))) {
            flag = ModelManager.FLAG_BLOCK;
        } else if (onBlock instanceof SlabBlock) {
            flag = ModelManager.FLAG_SLAB;
        } else if (onBlock instanceof StairBlock) {
            flag =ModelManager. FLAG_STAIRS;
        } else if (ModelManager. LowerPlant.contains(onBlock)) {
            flag = ModelManager.FLAG_GRASS;
        } else if (ModelManager. LARGE_GRASS.contains(onBlock)) {
            flag =ModelManager. FLAG_GRASS_LARGE;
        } else if ((onBlock instanceof FarmBlock || onBlock instanceof DirtPathBlock) ) {
            flag = ModelManager.FLAG_FARMLAND;
        }


        int offset = 0;
        if (flag ==  ModelManager.FLAG_GRASS || flag ==  ModelManager.FLAG_GRASS_LARGE) {
            if (flag ==  ModelManager.FLAG_GRASS) {
                offset = 1;
            }
            // 这里不忽略这个警告，因为后续会有优化
            else if (flag == ModelManager. FLAG_GRASS_LARGE) {
                if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                    offset = 1;
                } else {
                    offset = 2;
                }
            }
        }

        isLight = ClientConfig.Renderer.useVanillaCheck.get() && Minecraft.getInstance().level != null ?
                Minecraft.getInstance().level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(pos.above()) >= 15
                : ModelManager.getHeightOrUpdate(pos, false) == pos.getY() - offset;


        // SimpleUtil.testTime(()->{getHeightOrUpdate(pos, false);});


        isLight=flag!=0&&isLight
                &&state.getBlock() != Blocks.SNOW_BLOCK
                && ModelManager.shouldSnowAt(Minecraft.getInstance().level, pos.below(offset), state, Minecraft.getInstance().level.getRandom(), 0);


        return isLight?MapColor.SNOW:null;
    }
}
