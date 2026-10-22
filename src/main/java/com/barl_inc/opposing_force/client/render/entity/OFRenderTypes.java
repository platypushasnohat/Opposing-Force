package com.barl_inc.opposing_force.client.render.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.platypushasnohat.sinew.client.render.SinewRenderTypes;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class OFRenderTypes extends SinewRenderTypes {

    public OFRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType getGlowingEffect(ResourceLocation resourceLocation) {
        TextureStateShard shard = new TextureStateShard(resourceLocation, false, false);
        CompositeState compositeState = CompositeState.builder()
                .setTextureState(shard)
                .setShaderState(RENDERTYPE_EYES_SHADER)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL).setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return create("glow_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
    }

    //old
    //public static RenderType getLaserBolt(ResourceLocation resourceLocation) {
    //     RenderType.CompositeState compositeState = CompositeState.builder()
    //             .setTextureState(new TextureStateShard(resourceLocation, false, false))
    //             .setShaderState(RenderType.RENDERTYPE_ENERGY_SWIRL_SHADER)
    //             .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
    //             .setCullState(NO_CULL)
    //             .setLightmapState(LIGHTMAP)
    //             .createCompositeState(false);
    //     return create("laser_bolt", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256,false,true, compositeState);
    //}
    //minor tweak
    private static final Function<ResourceLocation, RenderType> LASER_BOLT = Util.memoize(resourceLocation -> {
        CompositeState compositeState = CompositeState.builder()
                .setTextureState(new TextureStateShard(resourceLocation, false, false))
                .setShaderState(RenderType.RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .createCompositeState(false);
        return create("laser_bolt", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, compositeState);
    });

    public static RenderType getPhasingGlow(ResourceLocation resourceLocation) {
        CompositeState compositeState = CompositeState.builder()
                .setShaderState(RENDERTYPE_EYES_SHADER)
                .setTextureState(new TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(ADDITIVE_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return create("phasing_glow", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, compositeState);
    }

    public static RenderType getLaserBolt(ResourceLocation resourceLocation) {
        return LASER_BOLT.apply(resourceLocation);
    }
}
