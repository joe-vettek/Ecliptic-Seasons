package cloud.lemonslice.teastory.client.color.item;

import net.minecraft.client.color.item.ItemColor;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;

@OnlyIn(Dist.CLIENT)
public class BottleItemColors implements ItemColor
{

    @Override
    public int getColor(ItemStack itemStack, int tintIndex)
    {
        if (tintIndex == 0)
        {
            int color = FluidUtil.getFluidHandler(itemStack).map(h ->
                            net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions.of(h.getFluidInTank(0).getFluid())
                                    .getTintColor(h.getFluidInTank(0)))
                    .orElse(-1);
            if (color == 0)
            {
                return -1;
            }
            return color;
        }
        else return -1;
    }
}
