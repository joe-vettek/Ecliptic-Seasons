package com.teamtea.ecliptic.client;


import com.teamtea.ecliptic.client.core.ModelManager;
import com.teamtea.ecliptic.common.core.biome.WeatherManager;
import com.teamtea.ecliptic.common.handler.CustomRandomTickHandler;
import com.teamtea.ecliptic.config.ServerConfig;
import com.teamtea.ecliptic.common.core.crop.CropInfoManager;
import com.teamtea.ecliptic.api.crop.CropSeasonInfo;
import com.teamtea.ecliptic.api.crop.CropHumidityInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teamtea.ecliptic.Ecliptic;

import java.util.List;

@Mod.EventBusSubscriber(modid = Ecliptic.MODID, value = Dist.CLIENT)
public final class ClientEventHandler {

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


    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        synchronized (ModelManager.RegionList) {
            ModelManager.RegionList.clear();
        }
    }

    @SubscribeEvent
    public static void onLevelUnloadEvent(LevelEvent.Unload event) {
        synchronized (ModelManager.RegionList) {
            ModelManager.RegionList.clear();
        }
    }


    // 强制区块渲染
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)
                && event.level.isClientSide()
                && ((ClientLevel) event.level).getGameTime() >> 8 == 0) {
            var lr = Minecraft.getInstance().levelRenderer;
            if (lr != null) {
                //
                // ((ClientChunkCache) event.level.getChunkSource()).storage.
                if (Minecraft.getInstance().cameraEntity instanceof Player player) {
                    BlockPos pos = player.getOnPos();
                    SectionPos sectionPos = SectionPos.of(pos);
                    // lr.setSectionDirtyWithNeighbors(sectionPos.x(),sectionPos.y(),sectionPos.z());
                    int x = sectionPos.x();
                    int y = sectionPos.y();
                    int z = sectionPos.z();
                    for (int i = x - 2; i <= x + 2; ++i) {
                        for (int j = z - 2; j <= z + 2; ++j) {
                            for (int k = y - 1; k <= y + 1; ++k) {
                                lr.setSectionDirty(j, k, i);
                            }
                        }
                    }
                }
            }
        }
    }
}
