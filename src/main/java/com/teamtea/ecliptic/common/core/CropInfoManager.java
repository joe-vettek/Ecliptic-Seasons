package com.teamtea.ecliptic.common.core;


import com.teamtea.ecliptic.api.crop.CropHumidityInfo;
import com.teamtea.ecliptic.api.crop.CropHumidityType;
import com.teamtea.ecliptic.api.crop.CropSeasonInfo;
import com.teamtea.ecliptic.api.crop.CropSeasonType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import com.teamtea.ecliptic.Ecliptic;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public final class CropInfoManager
{
    private final static Map<Block, CropHumidityInfo> CROP_HUMIDITY_INFO = new HashMap<>();
    private final static Map<Block, CropSeasonInfo> CROP_SEASON_INFO = new HashMap<>();


    @SubscribeEvent
    public static void init(TagsUpdatedEvent event)
    {
        CROP_HUMIDITY_INFO.clear();
        CROP_SEASON_INFO.clear();

        var items= event.getRegistryAccess().registry(Registries.ITEM);
        if (items.isPresent()){
            for (CropHumidityType cropHumidityType : CropHumidityType.values()) {
               var tagItems= items.get().getTag(ItemTags.create(cropHumidityType.getRes()));
                tagItems.ifPresent(holders -> holders.stream().toList().forEach(action -> {
                    registerCropHumidityInfo(action.get(), cropHumidityType);
                }));
            }
            for (CropSeasonType cropSeasonType : CropSeasonType.values()) {
                var tagItems= items.get().getTag(ItemTags.create(cropSeasonType.getRes()));
                tagItems.ifPresent(holders -> holders.stream().toList().forEach(action -> {
                    registerCropSeasonInfo(action.get(), cropSeasonType);
                }));
            }
        }

        // event.getRegistryAccess().registry(Registries.BLOCK).get().getTagNames().toList();


        ForgeRegistries.BLOCKS.forEach(block ->
        {
            registerCropHumidityInfo(block, CropHumidityType.AVERAGE_MOIST, false);
            registerCropSeasonInfo(block, CropSeasonType.SP_SU_AU, false);
        });
    }

    public static void registerCropHumidityInfo(Item item, CropHumidityType info)
    {
        if (item instanceof BlockItem && !CROP_HUMIDITY_INFO.containsKey(((BlockItem) item).getBlock()))
        {
            CROP_HUMIDITY_INFO.put(((BlockItem) item).getBlock(), info.getInfo());
        }
    }

    public static void registerCropHumidityInfo(Block block, CropHumidityType info, boolean force)
    {
        if (force || block instanceof CropBlock)
        {
            if (!CROP_HUMIDITY_INFO.containsKey(block))
            {
                CROP_HUMIDITY_INFO.put(block, info.getInfo());
            }
        }
    }

    public static void registerCropSeasonInfo(Item item, CropSeasonType info)
    {
        if (item instanceof BlockItem && !CROP_SEASON_INFO.containsKey(((BlockItem) item).getBlock()))
        {
            CROP_SEASON_INFO.put(((BlockItem) item).getBlock(), info.getInfo());
        }
    }

    public static void registerCropSeasonInfo(Block block, CropSeasonType info, boolean force)
    {
        if (force || block instanceof CropBlock)
        {
            if (!CROP_SEASON_INFO.containsKey(block))
            {
                CROP_SEASON_INFO.put(block, info.getInfo());
            }
        }
    }

    public static Collection<Block> getHumidityCrops()
    {
        return CROP_HUMIDITY_INFO.keySet();
    }

    public static Collection<Block> getSeasonCrops()
    {
        return CROP_SEASON_INFO.keySet();
    }

    @Nullable
    public static CropHumidityInfo getHumidityInfo(Block crop)
    {
        return CROP_HUMIDITY_INFO.get(crop);
    }

    @Nullable
    public static CropSeasonInfo getSeasonInfo(Block crop)
    {
        return CROP_SEASON_INFO.get(crop);
    }

}
