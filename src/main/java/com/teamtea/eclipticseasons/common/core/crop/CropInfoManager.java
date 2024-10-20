package com.teamtea.eclipticseasons.common.core.crop;


import com.teamtea.eclipticseasons.api.constant.crop.CropHumidityInfo;
import com.teamtea.eclipticseasons.api.constant.crop.CropHumidityType;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonInfo;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonType;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import com.teamtea.eclipticseasons.EclipticSeasons;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = EclipticSeasons.MODID)
public final class CropInfoManager {
    private final static Map<Block, CropHumidityInfo> CROP_HUMIDITY_INFO = new HashMap<>();
    private final static Map<Block, CropSeasonInfo> CROP_SEASON_INFO = new HashMap<>();


    @SubscribeEvent
    public static void init(TagsUpdatedEvent event) {
        CROP_HUMIDITY_INFO.clear();
        CROP_SEASON_INFO.clear();


        ITagCollection<Item> items = event.getTagManager().getItems();
        for (CropHumidityType cropHumidityType : CropHumidityType.values()) {
            ITag<Item> tagItems = items.getTag(cropHumidityType.getRes());
            if (tagItems != null)
                tagItems.getValues().forEach(action -> {
                    registerCropHumidityInfo(action, cropHumidityType);
                });
        }
        for (CropSeasonType cropSeasonType : CropSeasonType.values()) {
            ITag<Item>  tagItems = items.getTag(cropSeasonType.getRes());
            if (tagItems != null)
                tagItems.getValues().forEach(action -> {
                    registerCropSeasonInfo(action, cropSeasonType);
                });
        }

        // Registry.BLOCK.
        ITagCollection<Block> blocks = event.getTagManager().getBlocks();
        for (CropHumidityType cropHumidityType : CropHumidityType.values()) {
            ITag<Block> tagBlocks = blocks.getTag(cropHumidityType.getRes());
            if (tagBlocks != null)
                tagBlocks.getValues().forEach(action -> {
                    registerCropHumidityInfo(action, cropHumidityType, true);
                });
        }
        for (CropSeasonType cropSeasonType : CropSeasonType.values()) {
            ITag<Block>  tagBlocks = blocks.getTag(cropSeasonType.getRes());
            if (tagBlocks != null)
                tagBlocks.getValues().forEach(action -> {
                    registerCropSeasonInfo(action, cropSeasonType, true);
                });
        }
        // event.getRegistryAccess().registry(Registries.BLOCK).get().getTagNames().toList();


        ForgeRegistries.BLOCKS.forEach(block ->
        {
            registerCropHumidityInfo(block, CropHumidityType.AVERAGE_MOIST, false);
            registerCropSeasonInfo(block, CropSeasonType.SP_SU_AU, false);
        });
    }

    public static void registerCropHumidityInfo(Item item, CropHumidityType info) {
        if (item instanceof BlockItem && !CROP_HUMIDITY_INFO.containsKey(((BlockItem) item).getBlock())) {
            CROP_HUMIDITY_INFO.put(((BlockItem) item).getBlock(), info.getInfo());
        }
    }

    public static void registerCropHumidityInfo(Block block, CropHumidityType info, boolean force) {
        if (force || block instanceof CropsBlock) {
            if (!CROP_HUMIDITY_INFO.containsKey(block)) {
                CROP_HUMIDITY_INFO.put(block, info.getInfo());
            }
        }
    }

    public static void registerCropSeasonInfo(Item item, CropSeasonType info) {
        if (item instanceof BlockItem && !CROP_SEASON_INFO.containsKey(((BlockItem) item).getBlock())) {
            CROP_SEASON_INFO.put(((BlockItem) item).getBlock(), info.getInfo());
        }
    }

    public static void registerCropSeasonInfo(Block block, CropSeasonType info, boolean force) {
        if (force || block instanceof CropsBlock) {
            if (!CROP_SEASON_INFO.containsKey(block)) {
                CROP_SEASON_INFO.put(block, info.getInfo());
            }
        }
    }

    public static Collection<Block> getHumidityCrops() {
        return CROP_HUMIDITY_INFO.keySet();
    }

    public static Collection<Block> getSeasonCrops() {
        return CROP_SEASON_INFO.keySet();
    }

    @Nullable
    public static CropHumidityInfo getHumidityInfo(Block crop) {
        return CROP_HUMIDITY_INFO.get(crop);
    }

    @Nullable
    public static CropSeasonInfo getSeasonInfo(Block crop) {
        return CROP_SEASON_INFO.get(crop);
    }

}
