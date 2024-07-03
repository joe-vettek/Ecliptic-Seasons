package cloud.lemonslice.teastory.client.color.item;


import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;

public class BucketItemColors implements ItemColor
{
    @Override
    public int getColor(ItemStack itemStack, int tintIndex)
    {
        if (itemStack.getItem() instanceof BucketItem bucketItem && tintIndex == 1)
        {
            return net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions.of(bucketItem.getFluid()).getTintColor();
        }
        return -1;
    }
}
