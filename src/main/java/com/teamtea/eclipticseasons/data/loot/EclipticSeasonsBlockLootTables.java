package com.teamtea.eclipticseasons.data.loot;

import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EclipticSeasonsBlockLootTables extends BlockLootSubProvider {

    public EclipticSeasonsBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return map.entrySet()
                .stream()
                .map(e -> BuiltInRegistries.BLOCK.stream()
                        .filter(block -> block.getLootTable().equals(e.getKey()))
                        .findFirst()
                        .get())
                .toList();
    }


    protected void dropSelfWithContents(Set<Block> blocks) {
        for (Block block : blocks) {
            // if (skipBlocks.contains(block)) {
            //     continue;
            // }
            add(block, createSingleItemTable(block));
        }
    }

    @Override
    protected void generate() {
        Set<Block> blocks = BuiltInRegistries.BLOCK.stream()
                .filter(block -> EclipticSeasonsApi.MODID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace()))
                .filter(block -> ! block.getLootTable().equals( BuiltInLootTables.EMPTY))
                .filter(block -> block.asItem()!= Items.AIR)
                .collect(Collectors.toSet())
                ;

        dropSelfWithContents(blocks);

    }




}
