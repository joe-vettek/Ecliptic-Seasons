package xueluoanping.ecliptic.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xueluoanping.ecliptic.client.util.WeatherChecker;

@Mixin(Level.class)
public class MixinClientLevel {


    @Inject(at = {@At("HEAD")}, method = {"isRaining"}, cancellable = true, remap = false)
    private void mixin_isRaining(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(WeatherChecker.isRain(clientLevel));
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"getRainLevel"}, cancellable = true, remap = false)
    private void mixin_getRainLevel(float p_46723_, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof ClientLevel clientLevel) {
            cir.setReturnValue(WeatherChecker.getRainLevel(p_46723_,clientLevel));
        }
    }
}
