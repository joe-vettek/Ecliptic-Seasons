package com.teamtea.eclipticseasons.common.block;

import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class CalendarBlockItem extends BlockItem {
    public CalendarBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        var sd = EclipticSeasonsApi.getInstance().getSolarTerm(pContext.level());
        pTooltipComponents.add(Component.translatable("info.eclipticseasons.environment.solar_term.hint")
                .withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(sd.getTranslation().withStyle(sd.getSeason().getColor()));
    }
}
