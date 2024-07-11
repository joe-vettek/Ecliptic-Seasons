package com.teamtea.ecliptic.data.tag;

import com.teamtea.ecliptic.api.EclipticBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagsDataProvider extends TagsProvider<Biome> {


    public TagsDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BIOME, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EclipticBiomeTags.SEASONAL).addTags(BiomeTags.IS_OVERWORLD);
        this.tag(EclipticBiomeTags.MONSOONAL).addTags(BiomeTags.IS_SAVANNA);
        this.tag(EclipticBiomeTags.RAINLESS).addTags(Tags.Biomes.IS_CAVE);
        this.tag(EclipticBiomeTags.ARID).addTags(BiomeTags.IS_BADLANDS);
        this.tag(EclipticBiomeTags.DROUGHTY).addTags(BiomeTags.IS_BADLANDS, Tags.Biomes.IS_PEAK, Tags.Biomes.IS_COLD_OVERWORLD);
        this.tag(EclipticBiomeTags.SOFT).addTags(BiomeTags.IS_BEACH, BiomeTags.IS_OCEAN, BiomeTags.IS_DEEP_OCEAN);
        this.tag(EclipticBiomeTags.RAINY).addTags(BiomeTags.IS_JUNGLE);
    }
}
