package com.teamtea.eclipticseasons.common.core.crop;


import com.teamtea.eclipticseasons.api.constant.crop.CropHumidityInfo;
import com.teamtea.eclipticseasons.api.constant.crop.CropHumidityType;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonInfo;
import com.teamtea.eclipticseasons.api.constant.crop.CropSeasonType;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import com.teamtea.eclipticseasons.EclipticSeasons;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = EclipticSeasons.MODID)
public final class CropInfoManager
{
    private final static Map<Block, CropHumidityInfo> CROP_HUMIDITY_INFO = new HashMap<>();
    private final static Map<Block, CropSeasonInfo> CROP_SEASON_INFO = new HashMap<>();


    @SubscribeEvent
    public static void init(TagsUpdatedEvent event)
    {
        CROP_HUMIDITY_INFO.clear();
        CROP_SEASON_INFO.clear();

        var items= event.getTagManager().registry(ForgeRegistries.ITEMS.getRegistryKey());
        if (items.isPresent()){
            for (CropHumidityType cropHumidityType : CropHumidityType.values()) {
               var tagItems= items.get().getTag(ItemTags.create(cropHumidityType.getRes()));
                tagItems.ifPresent(holders -> holders.stream().toList().forEach(action -> {
                    registerCropHumidityInfo(action.value(), cropHumidityType);
                }));
            }
            for (CropSeasonType cropSeasonType : CropSeasonType.values()) {
                var tagItems= items.get().getTag(ItemTags.create(cropSeasonType.getRes()));
                tagItems.ifPresent(holders -> holders.stream().toList().forEach(action -> {
                    registerCropSeasonInfo(action.value(), cropSeasonType);
                }));
            }
        }

        // Registry.BLOCK.
        var blocks = event.getTagManager().registry(ForgeRegistries.BLOCKS.getRegistryKey());

        if (blocks.isPresent()) {
            for (CropHumidityType cropHumidityType : CropHumidityType.values()) {
                var tagBlocks = blocks.get().getTag(BlockTags.create(cropHumidityType.getRes()));
                tagBlocks.ifPresent(holders -> holders.stream().toList().forEach(action -> {
                    registerCropHumidityInfo(action.value(), cropHumidityType, true);
                }));
            }
            for (CropSeasonType cropSeasonType : CropSeasonType.values()) {
                var tagBlocks = blocks.get().getTag(BlockTags.create(cropSeasonType.getRes()));
                tagBlocks.ifPresent(holders -> holders.stream().toList().forEach(action -> {
                    registerCropSeasonInfo(action.value(), cropSeasonType, true);
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
