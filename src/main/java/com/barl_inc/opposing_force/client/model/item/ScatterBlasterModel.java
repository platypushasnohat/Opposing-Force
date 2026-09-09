package com.barl_inc.opposing_force.client.model.item;

import com.barl_inc.opposing_force.client.model.item.animation.BlasterAnimations;
import com.barl_inc.opposing_force.item.ScatterBlasterItem;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ScatterBlasterModel extends BlasterModel<ScatterBlasterItem> {

    public ScatterBlasterModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(Entity entity, ScatterBlasterItem item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, item.blastAnimationState, BlasterAnimations.SCATTER_BLASTER_SHOOT, ageInTicks);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(22, 14).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(1.0F, -2.75F, -8.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(39, 22).addBox(4.0F, -2.75F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 22).addBox(5.0F, -2.75F, -1.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 22).addBox(-3.0F, -2.75F, -0.025F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(19, 46).addBox(2.0F, -0.75F, -0.025F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(19, 43).addBox(-2.0F, -0.75F, -0.025F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(26, 46).addBox(-2.0F, -0.75F, -0.025F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 0).addBox(-5.0F, -2.75F, -1.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(37, 0).addBox(-5.0F, -2.75F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -2.75F, -8.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(12, 35).addBox(-0.5F, 1.0F, -0.025F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(-1, 45).addBox(-0.5F, -1.0F, -0.025F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 5).addBox(0.0F, 1.0F, -3.025F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-0.5F, -1.0F, 2.975F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(14, 26).addBox(-0.5F, 0.0F, 4.975F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(14, 26).addBox(-0.5F, 0.0F, 8.975F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 25).addBox(-0.5F, 3.0F, 4.975F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(10, 25).addBox(-0.5F, 0.0F, 4.975F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 29).addBox(3.0F, -4.75F, -8.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(0.0F, -7.75F, -2.0F, 0.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(18, 29).addBox(-3.0F, -4.75F, -8.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(1.5F, -2.25F, -13.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 10).addBox(2.5F, -1.0F, -10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 38).addBox(1.5F, -2.0F, -9.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(18, 38).mirror().addBox(-4.5F, -2.0F, -9.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 10).mirror().addBox(-3.5F, -1.0F, -10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 35).mirror().addBox(-4.5F, -2.25F, -13.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 20.0F, 2.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}