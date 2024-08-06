package com.teamtea.eclipticseasons.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class CPostPass extends PostPass {
    public CPostPass(ResourceManager pResourceManager, String pName, RenderTarget pInTarget, RenderTarget pOutTarget) throws IOException {
        super(pResourceManager, pName, pInTarget, pOutTarget);
    }


}
