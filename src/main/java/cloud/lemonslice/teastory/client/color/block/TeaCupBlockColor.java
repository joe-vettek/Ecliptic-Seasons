package cloud.lemonslice.teastory.client.color.block;

import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class TeaCupBlockColor implements BlockColor {
    @Override
    public int getColor(BlockState state, BlockAndTintGetter reader, BlockPos pos, int index) {
        if (pos != null) {
            if (Minecraft.getInstance().level != null) {

                var world = Minecraft.getInstance().level;
                // TileEntity te = world.getTileEntity(pos);
                // if (te instanceof TeaCupTileEntity) {
                //     int drink = 0;
                //     int t = tintindex;
                //     int color;
                //     do {
                //         color = ((TeaCupTileEntity) te).getFluid(drink).getAttributes().getColor();
                //         drink++;
                //         if (color != 0) {
                //             t--;
                //         }
                //     }
                //     while (t != 0 && drink < 3);
                //     return color;
                // }
            }
        }
        return -1;
    }
}
