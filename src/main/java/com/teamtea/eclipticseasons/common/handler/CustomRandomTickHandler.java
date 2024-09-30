package com.teamtea.eclipticseasons.common.handler;


import com.teamtea.eclipticseasons.api.misc.CustomRandomTick;
import com.teamtea.eclipticseasons.common.core.biome.WeatherManager;
import com.teamtea.eclipticseasons.config.ServerConfig;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.tick.LevelTickEvent;


import java.util.Collections;
import java.util.List;


public final class CustomRandomTickHandler {
    private static final CustomRandomTick SNOW_MELT = (state, serverLevel, pos) ->
    {
        BlockPos blockpos = new BlockPos(pos.getX(), serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ()), pos.getZ());
        if (serverLevel.isAreaLoaded(blockpos, 1) && serverLevel.getBiome(blockpos).value().getTemperature(pos) >= 0.15F && !WeatherManager.onCheckWarmEnoughToRain(pos)) {
            BlockState topState = serverLevel.getBlockState(blockpos);
            if (topState.getBlock().equals(Blocks.SNOW)) {
                serverLevel.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
            } else {
                BlockState belowState = serverLevel.getBlockState(blockpos.below());
                if (belowState.getBlock().equals(Blocks.ICE)) {
                    serverLevel.setBlockAndUpdate(blockpos.below(), Blocks.WATER.defaultBlockState());
                }
            }
        }
    };

    public static void onWorldTick(LevelTickEvent.Post event) {
        if ( ServerConfig.Temperature.iceMelt.get()
                && !event.getLevel().isClientSide()) {
            ServerLevel level = (ServerLevel) event.getLevel();
            int randomTickSpeed = level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
            if (randomTickSpeed > 0) {
                List<ChunkHolder> list = Lists.newArrayList(((ServerLevel) level).getChunkSource().chunkMap.getChunks());
                Collections.shuffle(list);
                level.getChunkSource().chunkMap.getChunks().forEach(chunkHolder ->
                {

                    LevelChunk optional = chunkHolder.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).orElse(null);
                    if (optional!=null) {
                        for (var chunksection : optional.getSections()) {
                            if (chunksection.isRandomlyTicking()) {
                                int i = optional.getPos().getMinBlockX();
                                int j = optional.getPos().getMinBlockZ();
                                BlockPos blockpos1 = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, level.getBlockRandomPos(i, 0, j, 15));
                                // BlockPos blockpos2 = blockpos1.below();
                                // Biome biome = level.getBiome(blockpos1).value();
                                if (level.isAreaLoaded(blockpos1, 1)) // Forge: check area to avoid loading neighbors in unloaded chunks
                                {
                                    for (int l = 0; l < randomTickSpeed; ++l) {
                                        if (level.getRandom().nextInt(32) == 0) {
                                            int x = blockpos1.getX();
                                            int y = blockpos1.getY();
                                            int z = blockpos1.getZ();
                                            doCustomRandomTick(level, x, y, z);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private static void doCustomRandomTick(ServerLevel serverLevel, int x, int y, int z) {
        if (ServerConfig.Temperature.iceMelt.get()) {
            SNOW_MELT.tick(null, serverLevel, new BlockPos(x, y, z));
        }
    }
}
