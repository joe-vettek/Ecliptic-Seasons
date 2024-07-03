package cloud.lemonslice.teastory.client.color.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GrassColor;


public class GrassBlockItemColors implements ItemColor
{
    @Override
    public int getColor(ItemStack itemStack, int tintIndex)
    {
        return GrassColor.get(0.5D, 1.0D);
    }
}
