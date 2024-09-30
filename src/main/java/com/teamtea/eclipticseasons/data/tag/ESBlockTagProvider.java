package com.teamtea.eclipticseasons.data.tag;


import com.teamtea.eclipticseasons.api.constant.tag.SeasonalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;


public final class ESBlockTagProvider extends BlockTagsProvider {
    public ESBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(SeasonalBlockTags.NONE_FALLEN_LEAVES).add(Blocks.CHERRY_LEAVES);
        tag(SeasonalBlockTags.HABITAT_BUTTERFLY).addTag(BlockTags.FLOWERS);
        tag(SeasonalBlockTags.HABITAT_FIREFLY).addTag(BlockTags.FLOWERS).add(Blocks.SHORT_GRASS, Blocks.TALL_GRASS);
    }
}
