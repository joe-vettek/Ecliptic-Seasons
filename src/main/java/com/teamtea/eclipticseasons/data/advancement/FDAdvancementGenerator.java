package com.teamtea.eclipticseasons.data.advancement;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.common.advancement.SolarTermsCriterion;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Optional;
import java.util.function.Consumer;

public class FDAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        AdvancementHolder seasons = Advancement.Builder.advancement()
                .display(Items.BOOK,
                        Component.translatable( "eclipticseasons.advancement.root"),
                        Component.translatable( "eclipticseasons.advancement.root.desc"),
                        ResourceLocation.parse("minecraft:textures/block/bricks.png"),
                        AdvancementType.TASK, true, true, false)
                .addCriterion("solar_terms", SolarTermsCriterion.TriggerInstance.simple())
                .requirements(AdvancementRequirements.Strategy.AND)
                .save(consumer, getNameId("main/root"));

    }


    private String getNameId(String id) {
        return EclipticSeasonsApi.MODID + ":" + id;
    }
}
