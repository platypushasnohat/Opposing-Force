package com.barl_inc.opposing_force.client.model.entity;

import com.barl_inc.opposing_force.client.model.entity.animation.RingProjectileAnimations;
import com.barl_inc.opposing_force.entity.projectile.RingProjectile;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

import java.util.EnumSet;
import java.util.Set;

public class RingProjectileModel extends HierarchicalModel<RingProjectile> {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    private static final Set<Direction> PLANE_Y = EnumSet.of(Direction.DOWN);
    private static final Set<Direction> PLANE_Z = EnumSet.of(Direction.NORTH);

    private final ModelPart root;

    public RingProjectileModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public void setupAnim(RingProjectile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        KeyframeAnimations.animate(this, RingProjectileAnimations.PROJECTILE, (long) (ageInTicks * 50.0F), 1.0F, ANIMATION_VECTOR_CACHE);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        meshdefinition.getRoot().addOrReplaceChild("ring", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-0.5F, 1.0F, -1.0F, 1.0F, 0.0F, 2.0F, PLANE_Y)
                .texOffs(0, 2).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 0.0F, 2.0F, PLANE_Y)
                .texOffs(0, 4).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 0.0F, PLANE_Z)
                .texOffs(2, 4).addBox(-0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, PLANE_Z), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 16);
    }
}
