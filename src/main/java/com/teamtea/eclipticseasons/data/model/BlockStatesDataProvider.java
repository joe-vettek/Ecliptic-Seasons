package com.teamtea.eclipticseasons.data.model;

import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.common.block.CalendarBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.List;

public class BlockStatesDataProvider extends BlockStateProvider {


    private final ExistingFileHelper existingFileHelper;

    public BlockStatesDataProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EclipticSeasonsApi.MODID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(EclipticSeasonsMod.ModContents.calendar.get()).forAllStatesExcept(state -> ConfiguredModel.builder()
                .modelFile(models().getExistingFile(resourceBlock("calendar")))
                .rotationY(getRotateYByFacing(state.getValue(BlockStateProperties.HORIZONTAL_FACING)))
                .build());

        addSimple(EclipticSeasonsMod.ModContents.wind_chimes.value());
        addSimple(EclipticSeasonsMod.ModContents.paper_wind_chimes.value());
        addSimple(EclipticSeasonsMod.ModContents.bamboo_wind_chimes.value());

    }

    public void addSimple(Block block) {
        simpleBlock(block, models().getExistingFile(resourceBlock(blockName(block))));
    }

    private String blockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public static ResourceLocation resourceBlock(String path) {
        return EclipticSeasonsMod.rl("block/" + path);
    }


    public static int getRotateYByFacing(Direction state) {
        switch (state) {
            case EAST -> {
                return 90;
            }
            case SOUTH -> {
                return 180;
            }
            case WEST -> {
                return 270;
            }
            default -> {
                return 0;
            }
        }
    }

}
