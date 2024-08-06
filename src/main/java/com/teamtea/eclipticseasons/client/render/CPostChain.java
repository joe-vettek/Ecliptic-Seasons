package com.teamtea.eclipticseasons.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class CPostChain extends PostChain {
    private final ResourceManager resourceManager;
    private final List<PostPass> passes = Lists.newArrayList();
    private final RenderTarget screenTarget;
    private final Map<String, RenderTarget> customRenderTargets = Maps.newHashMap();
    private int screenWidth;
    private int screenHeight;

    public CPostChain(TextureManager pTextureManager, ResourceManager pResourceManager, RenderTarget pScreenTarget, ResourceLocation pName) throws IOException, JsonSyntaxException {
        super(pTextureManager, pResourceManager, pScreenTarget, pName);
        this.screenWidth = pScreenTarget.viewWidth;
        this.screenHeight = pScreenTarget.viewHeight;
        this.resourceManager = pResourceManager;
        this.screenTarget = pScreenTarget;
    }
}
