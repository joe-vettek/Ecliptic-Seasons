package cloud.lemonslice.environment.crop;


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
import xueluoanping.ecliptic.Ecliptic;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID)
public final class CropInfoManager
{
    private final static Map<Block, CropHumidityInfo> CROP_HUMIDITY_INFO = new HashMap<>();
    private final static Map<Block, CropSeasonInfo> CROP_SEASON_INFO = new HashMap<>();

    // private final static Map<VinePair, TrellisWithVineBlock> TRELLIS_VINES_INFO = new HashMap<>();
    // private final static Map<TrellisWithVineBlock, TrellisBlock> TRELLIS_INFO = new HashMap<>();
    //
    // public static void registerVineTypeConnections(VineType typeIn, TrellisBlock blockIn, TrellisWithVineBlock vineOut)
    // {
    //     TRELLIS_VINES_INFO.put(new VinePair(typeIn, blockIn), vineOut);
    //     TRELLIS_INFO.put(vineOut, blockIn);
    // }
    //
    // public static void initTrellisBlocks()
    // {
    //     registerVineTypeConnections(VineType.GRAPE, BlockRegistry.OAK_TRELLIS, BlockRegistry.OAK_TRELLIS_GRAPE);
    //     registerVineTypeConnections(VineType.GRAPE, BlockRegistry.BIRCH_TRELLIS, BlockRegistry.BIRCH_TRELLIS_GRAPE);
    //     registerVineTypeConnections(VineType.GRAPE, BlockRegistry.JUNGLE_TRELLIS, BlockRegistry.JUNGLE_TRELLIS_GRAPE);
    //     registerVineTypeConnections(VineType.GRAPE, BlockRegistry.SPRUCE_TRELLIS, BlockRegistry.SPRUCE_TRELLIS_GRAPE);
    //     registerVineTypeConnections(VineType.GRAPE, BlockRegistry.DARK_OAK_TRELLIS, BlockRegistry.DARK_OAK_TRELLIS_GRAPE);
    //     registerVineTypeConnections(VineType.GRAPE, BlockRegistry.ACACIA_TRELLIS, BlockRegistry.ACACIA_TRELLIS_GRAPE);
    //     registerVineTypeConnections(VineType.CUCUMBER, BlockRegistry.OAK_TRELLIS, BlockRegistry.OAK_TRELLIS_CUCUMBER);
    //     registerVineTypeConnections(VineType.CUCUMBER, BlockRegistry.BIRCH_TRELLIS, BlockRegistry.BIRCH_TRELLIS_CUCUMBER);
    //     registerVineTypeConnections(VineType.CUCUMBER, BlockRegistry.JUNGLE_TRELLIS, BlockRegistry.JUNGLE_TRELLIS_CUCUMBER);
    //     registerVineTypeConnections(VineType.CUCUMBER, BlockRegistry.SPRUCE_TRELLIS, BlockRegistry.SPRUCE_TRELLIS_CUCUMBER);
    //     registerVineTypeConnections(VineType.CUCUMBER, BlockRegistry.DARK_OAK_TRELLIS, BlockRegistry.DARK_OAK_TRELLIS_CUCUMBER);
    //     registerVineTypeConnections(VineType.CUCUMBER, BlockRegistry.ACACIA_TRELLIS, BlockRegistry.ACACIA_TRELLIS_CUCUMBER);
    //     registerVineTypeConnections(VineType.BITTER_GOURD, BlockRegistry.OAK_TRELLIS, BlockRegistry.OAK_TRELLIS_BITTER_GOURD);
    //     registerVineTypeConnections(VineType.BITTER_GOURD, BlockRegistry.BIRCH_TRELLIS, BlockRegistry.BIRCH_TRELLIS_BITTER_GOURD);
    //     registerVineTypeConnections(VineType.BITTER_GOURD, BlockRegistry.JUNGLE_TRELLIS, BlockRegistry.JUNGLE_TRELLIS_BITTER_GOURD);
    //     registerVineTypeConnections(VineType.BITTER_GOURD, BlockRegistry.SPRUCE_TRELLIS, BlockRegistry.SPRUCE_TRELLIS_BITTER_GOURD);
    //     registerVineTypeConnections(VineType.BITTER_GOURD, BlockRegistry.DARK_OAK_TRELLIS, BlockRegistry.DARK_OAK_TRELLIS_BITTER_GOURD);
    //     registerVineTypeConnections(VineType.BITTER_GOURD, BlockRegistry.ACACIA_TRELLIS, BlockRegistry.ACACIA_TRELLIS_BITTER_GOURD);
    // }
    //
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


        // Arrays.asList(CropHumidityType.values()).forEach(type ->
        //         ItemTags.getCollection().getTagByID(type.getRes()).getAllElements().forEach(crop -> registerCropHumidityInfo(crop, type)));
        // Arrays.asList(CropSeasonType.values()).forEach(type ->
        //         ItemTags.getCollection().getTagByID(type.getRes()).getAllElements().forEach(crop -> registerCropSeasonInfo(crop, type)));

        // Register the crops in Pam's that are nonstandard.
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:bellpepperseeditem")), CropHumidityType.AVERAGE);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:brusselsproutseeditem")), CropHumidityType.AVERAGE_MOIST);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:cactusfruitseeditem")), CropHumidityType.ARID_DRY);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:candleberryseeditem")), CropHumidityType.DRY_MOIST);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:chilipepperseeditem")), CropHumidityType.AVERAGE);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:coffeebeanseeditem")), CropHumidityType.AVERAGE_HUMID);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:greengrapeseeditem")), CropHumidityType.MOIST_HUMID);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:juniperberryseeditem")), CropHumidityType.DRY_AVERAGE);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:mustardseedsseeditem")), CropHumidityType.DRY_MOIST);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:sesameseedsseeditem")), CropHumidityType.AVERAGE_MOIST);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:oatsseeditem")), CropHumidityType.DRY);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:peasseeditem")), CropHumidityType.AVERAGE);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:sweetpotatoseeditem")), CropHumidityType.DRY_MOIST);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:waterchestnutseeditem")), CropHumidityType.MOIST_HUMID);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:whitemushroomseeditem")), CropHumidityType.MOIST_HUMID);
        registerCropHumidityInfo(ForgeRegistries.ITEMS.getValue(new ResourceLocation("pamhc2crops:wintersquashseeditem")), CropHumidityType.DRY_MOIST);



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

    // public static TrellisWithVineBlock getVineTrellis(VineType typeIn, TrellisBlock blockIn)
    // {
    //     return TRELLIS_VINES_INFO.get(new VinePair(typeIn, blockIn));
    // }
    //
    // public static TrellisBlock getEmptyTrellis(TrellisWithVineBlock blockIn)
    // {
    //     return TRELLIS_INFO.get(blockIn);
    // }
}
