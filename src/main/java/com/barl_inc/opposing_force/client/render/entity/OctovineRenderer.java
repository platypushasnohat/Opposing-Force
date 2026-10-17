package com.barl_inc.opposing_force.client.render.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.entity.FurballModel;
import com.barl_inc.opposing_force.client.model.entity.OctovineModel;
import com.barl_inc.opposing_force.client.render.entity.layer.FurballEyesLayer;
import com.barl_inc.opposing_force.client.render.entity.layer.OctovineEyesLayer;
import com.barl_inc.opposing_force.entity.Furball;
import com.barl_inc.opposing_force.entity.Octovine;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class OctovineRenderer extends MobRenderer<Octovine, OctovineModel> {

    private static final ResourceLocation TEXTURE_LOCATION = OpposingForce.location("textures/entity/octovine/octovine.png");

    public OctovineRenderer(EntityRendererProvider.Context context) {
        super(context, new OctovineModel(context.bakeLayer(OFModelLayers.OCTOVINE)), 0.5F);
        this.addLayer(new OctovineEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Octovine octovine) {
        return TEXTURE_LOCATION;
    }
}