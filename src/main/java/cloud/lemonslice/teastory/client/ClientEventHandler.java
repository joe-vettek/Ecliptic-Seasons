package cloud.lemonslice.teastory.client;


import cloud.lemonslice.teastory.config.ServerConfig;
import cloud.lemonslice.teastory.environment.crop.CropHumidityInfo;
import cloud.lemonslice.teastory.environment.crop.CropInfoManager;
import cloud.lemonslice.teastory.environment.crop.CropSeasonInfo;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xueluoanping.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID, value = Dist.CLIENT)
public final class ClientEventHandler {
    // @SubscribeEvent
    // public static void addFogColor(EntityREnder.FogColors event)
    // {
    //     BlockState state = event.getInfo().getFluidState().getBlockState();
    //
    //     if (state.getBlock() instanceof NormalFlowingFluidBlock)
    //     {
    //         int color = state.getFluidState().getFluid().getAttributes().getColor();
    //
    //         event.setRed((float) (((color >> 16) & 255) / 255.0));
    //         event.setGreen((float) (((color >> 8) & 255) / 255.0));
    //         event.setBlue((float) ((color & 255) / 255.0));
    //     }
    // }

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event) {

        if (ServerConfig.Season.enable.get()) {
            if (event.getItemStack().getItem() instanceof BlockItem) {
                if (CropInfoManager.getHumidityCrops().contains(((BlockItem) event.getItemStack().getItem()).getBlock())) {
                    CropHumidityInfo info = CropInfoManager.getHumidityInfo(((BlockItem) event.getItemStack().getItem()).getBlock());
                    if (info != null) event.getToolTip().addAll(info.getTooltip());
                }
                if (CropInfoManager.getSeasonCrops().contains(((BlockItem) event.getItemStack().getItem()).getBlock())) {
                    CropSeasonInfo info = CropInfoManager.getSeasonInfo(((BlockItem) event.getItemStack().getItem()).getBlock());
                    if (info != null) event.getToolTip().addAll(info.getTooltip());
                }
            }
        }
    }
}
