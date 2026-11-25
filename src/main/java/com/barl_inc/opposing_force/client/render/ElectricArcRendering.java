package com.barl_inc.opposing_force.client.render;

import com.barl_inc.opposing_force.client.render.entity.OFRenderTypes;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4fStack;

import java.util.LinkedHashMap;

public class ElectricArcRendering {

    private static final MultiBufferSource.BufferSource BUFFER_SOURCE = MultiBufferSource.immediateWithBuffers(Util.make(new LinkedHashMap<RenderType, ByteBufferBuilder>(), buffers -> {
        buffers.put(OFRenderTypes.getElectricArcOverlay(), new ByteBufferBuilder(262144));
        buffers.put(OFRenderTypes.getElectricArc(), new ByteBufferBuilder(262144));
        buffers.put(OFRenderTypes.getElectricArcDepth(), new ByteBufferBuilder(262144));
    }), new ByteBufferBuilder(256));
    private static RenderTarget depthCopy;
    private static boolean depthSaved;

    public static VertexConsumer getBuffer() {
        return Minecraft.useShaderTransparency() ? VertexMultiConsumer.create(BUFFER_SOURCE.getBuffer(OFRenderTypes.getElectricArc()), BUFFER_SOURCE.getBuffer(OFRenderTypes.getElectricArcDepth())) : BUFFER_SOURCE.getBuffer(OFRenderTypes.getElectricArcOverlay());
    }

    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            saveDepth();
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER && Minecraft.useShaderTransparency()) {
            flush(event);
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL && !Minecraft.useShaderTransparency()) {
            draw(event);
        }
    }

    private static void saveDepth() {
        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        depthSaved = !Minecraft.useShaderTransparency();
        if (depthSaved) {
            if (depthCopy == null) {
                depthCopy = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
            } else if (depthCopy.width != main.width || depthCopy.height != main.height) {
                depthCopy.resize(main.width, main.height, Minecraft.ON_OSX);
            }
            depthCopy.copyDepthFrom(main);
            main.bindWrite(false);
        }
    }

    private static void draw(RenderLevelStageEvent event) {
        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        if (depthSaved && depthCopy != null) {
            main.copyDepthFrom(depthCopy);
        }
        main.bindWrite(false);
        depthSaved = false;
        flush(event);
    }

    private static void flush(RenderLevelStageEvent event) {
        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.set(event.getModelViewMatrix());
        RenderSystem.applyModelViewMatrix();
        BUFFER_SOURCE.endBatch();
        modelView.popMatrix();
        RenderSystem.applyModelViewMatrix();
    }
}