package com.teamtea.eclipticseasons.api.constant.solar;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;


public enum Season
{
    SPRING(ChatFormatting.DARK_GREEN),
    SUMMER(ChatFormatting.RED),
    AUTUMN(ChatFormatting.GOLD),
    WINTER(ChatFormatting.BLUE),
    NONE(ChatFormatting.DARK_AQUA);

    private final ChatFormatting color;

    Season(ChatFormatting color)
    {
        this.color = color;
    }

    public String getName()
    {
        return this.toString().toLowerCase();
    }

    public Component getTranslation()
    {
        return new TranslatableComponent("info.teastory.environment.season." + getName()).withStyle(color);
    }

    public ChatFormatting getColor()
    {
        return color;
    }
}
