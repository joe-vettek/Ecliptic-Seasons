package cloud.lemonslice.teastory.client.color.block;


import cloud.lemonslice.teastory.capability.CapabilitySolarTermTime;
import cloud.lemonslice.teastory.environment.solar.SolarTerm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;



public class BirchLeavesColor implements BlockColor {
    @Override
    public int getColor(BlockState state, BlockAndTintGetter reader, BlockPos pos, int index) {
        if (Minecraft.getInstance().level != null) {
            return Minecraft.getInstance().level.getCapability(CapabilitySolarTermTime.WORLD_SOLAR_TIME).map(data ->
                    SolarTerm.get(data.getSolarTermIndex()).getColorInfo().getBirchColor()).orElse(FoliageColor.getBirchColor());
        }
        return FoliageColor.getBirchColor();
    }
}
