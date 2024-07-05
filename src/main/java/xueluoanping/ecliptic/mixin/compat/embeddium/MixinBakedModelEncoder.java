package xueluoanping.ecliptic.mixin.compat.embeddium;


// @Mixin({BakedModelEncoder.class})
public abstract class MixinBakedModelEncoder {
    // work for item render
    // @Inject(at = {@At("HEAD")}, method = {"writeQuadVertices"}, cancellable = true, remap = false)
    // private static void mixin_writeQuadVertices(VertexBufferWriter writer, PoseStack.Pose matrices, ModelQuadView quad, int color, int light, int overlay, CallbackInfo ci) {
    //     BakedModelEncoderFixer.writeQuadVertices(writer, matrices, quad, color, light, overlay);
    //     ci.cancel();
    // }

    // @Inject(at = {@At("HEAD")}, method = {"writeQuadVertices"}, cancellable = true, remap = false)
    // private static void mixin_writeQuadVertices2(VertexBufferWriter writer, PoseStack.Pose matrices, ModelQuadView quad, float r, float g, float b, float[] brightnessTable, boolean colorize, int[] light, int overlay, CallbackInfo ci) {
    //     BakedModelEncoderFixer.writeQuadVertices2(writer, matrices, quad, r, g, b, brightnessTable, colorize, light, overlay);
    //     ci.cancel();
    // }

}
