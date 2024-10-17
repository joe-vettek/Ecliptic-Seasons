package com.teamtea.eclipticseasons.common.core.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.Arrays;
import java.util.stream.IntStream;

public record SnowyRemover(
        int[][] blockWatcher
) {
    public static final Codec<SnowyRemover> CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(
                    snowyRemoverInstance ->
                            snowyRemoverInstance.group(Codec.INT.sizeLimitedListOf(16 * 16)
                                            .fieldOf("blocks").forGetter(snowyRemover -> Arrays.stream(snowyRemover
                                                            .blockWatcher())
                                                    .flatMapToInt(Arrays::stream)
                                                    .boxed().toList()
                                            )
                                    )
                                    .apply(snowyRemoverInstance, ss ->
                                            new SnowyRemover(
                                                    IntStream.range(0, 16)
                                                            .mapToObj(i -> ss.subList(i * 16, (i + 1) * 16).stream().mapToInt(Integer::intValue).toArray())
                                                            .toArray(int[][]::new)
                                            )
                                    )
            )
    );

    public static final int SNOWY = 0;
    public static final int NONE_SNOWY = 1;

    public static int getChunkPos(int golobalPos) {
        return golobalPos & 15;
    }

    public void setChunkPos(BlockPos blockPos, int value) {
        blockWatcher[getChunkPos(blockPos.getX())][getChunkPos(blockPos.getZ())] = value;
    }

    public boolean notSnowyAt(int x, int z) {
        return blockWatcher[x][z] == NONE_SNOWY;
    }

    public boolean notSnowyAt(BlockPos blockPos) {
        return notSnowyAt(getChunkPos(blockPos.getX()), getChunkPos(blockPos.getZ()));
    }

    public boolean allSnowAble() {
        return Arrays.stream(blockWatcher())
                .flatMapToInt(Arrays::stream)
                .noneMatch(c->c>0);
    }
}
