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



public class GrassBlockColor implements BlockColor
{

    public int getColor(BlockState state, BlockAndTintGetter reader, BlockPos pos, int index) {
        if (reader != null & pos != null)
        {
            // BiomeColors.GRASS_COLOR_RESOLVER
            return reader.getBlockTint(pos, BiomeColors.GRASS_COLOR_RESOLVER);
        }
        return -1;

    }
}
