package com.barl_inc.opposing_force.events;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.armor.EmeraldArmorModel;
import com.barl_inc.opposing_force.client.model.armor.MoonShoesModel;
import com.barl_inc.opposing_force.client.model.entity.*;
import com.barl_inc.opposing_force.client.model.item.BlasterModel;
import com.barl_inc.opposing_force.client.model.item.LeitmotifModel;
import com.barl_inc.opposing_force.client.model.item.ScatterBlasterModel;
import com.barl_inc.opposing_force.client.model.item.TriBlasterModel;
import com.barl_inc.opposing_force.client.particle.*;
import com.barl_inc.opposing_force.client.render.entity.*;
import com.barl_inc.opposing_force.client.render.item.OFItemExtensions;
import com.barl_inc.opposing_force.item.BlasterItem;
import com.barl_inc.opposing_force.item.LeitmotifItem;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.platypushasnohat.sinew.events.custom.PoseHandEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.util.TriState;

@EventBusSubscriber(modid = OpposingForce.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OFEntities.DICER.get(), DicerRenderer::new);
        event.registerEntityRenderer(OFEntities.BEWILDER.get(), BewilderRenderer::new);
        event.registerEntityRenderer(OFEntities.GUSHER.get(), GusherRenderer::new);
        event.registerEntityRenderer(OFEntities.GNAT.get(), GnatRenderer::new);
        event.registerEntityRenderer(OFEntities.SCORCHER.get(), ScorcherRenderer::new);
        event.registerEntityRenderer(OFEntities.FURBALL.get(), FurballRenderer::new);
        event.registerEntityRenderer(OFEntities.TERROR.get(), TerrorRenderer::new);
        event.registerEntityRenderer(OFEntities.TARANTULA.get(), TarantulaRenderer::new);
        event.registerEntityRenderer(OFEntities.MUSHY.get(), MushyRenderer::new);
        event.registerEntityRenderer(OFEntities.MOTIF.get(), MotifRenderer::new);

        event.registerEntityRenderer(OFEntities.DICER_LASER.get(), DicerLaserRenderer::new);
        event.registerEntityRenderer(OFEntities.LASER_BOLT.get(), LaserBoltRenderer::new);
        event.registerEntityRenderer(OFEntities.LASER_BLADE.get(), LaserBladeRenderer::new);
        event.registerEntityRenderer(OFEntities.ACID_CHARGE.get(), AcidChargeRenderer::new);
        event.registerEntityRenderer(OFEntities.ACID_CLOUD.get(), NoopRenderer::new);
        event.registerEntityRenderer(OFEntities.MUDBALL.get(), MudballRenderer::new);
        event.registerEntityRenderer(OFEntities.MUD_SPLATTER.get(), MudSplatterRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OFModelLayers.DICER, DicerModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.BEWILDER, BewilderModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.GUSHER, GusherModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.GNAT, GnatModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.SCORCHER, ScorcherModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.FURBALL, FurballModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.TERROR, TerrorModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.TARANTULA, TarantulaModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.MUSHY, MushyModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.MOTIF, MotifModel::createBodyLayer);

        event.registerLayerDefinition(OFModelLayers.LASER_BOLT, LaserBoltModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.ACID_CHARGE, AcidChargeModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.MUDBALL, MudballModel::createBodyLayer);

        event.registerLayerDefinition(OFModelLayers.BLASTER, BlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.TRI_BLASTER, TriBlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.SCATTER_BLASTER, ScatterBlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.LEITMOTIF, LeitmotifModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(OFParticleTypes.LASER_DUST.get(), LaserDustParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.LASER_IMPACT.get(), LaserImpactParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.LASER_SWEEP.get(), LaserSweepParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.ACID.get(), AcidParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.FIRE_BREATH.get(), FireBreathParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.SPORE_CLOUD.get(), SporeCloudParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.TRI_BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.SCATTER_BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.LEITMOTIF.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
                return EmeraldArmorModel.INSTANCE;
            }
        }, OFItems.EMERALD_MASK, OFItems.EMERALD_CHESTPLATE, OFItems.EMERALD_LEGGINGS, OFItems.EMERALD_BOOTS);

        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
                return MoonShoesModel.INSTANCE.withAnimations(entity);
            }
        }, OFItems.MOON_SHOES);
    }

    @SubscribeEvent
    public static void onPoseHand(PoseHandEvent event) {
        LivingEntity player = (LivingEntity) event.getEntity();
        if (isAimedGun(player.getItemInHand(InteractionHand.MAIN_HAND))) {
            if (player.getMainArm() == HumanoidArm.RIGHT) {
                event.getHumanoidModel().rightArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().rightArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().rightArm.zRot = 0;
            } else {
                event.getHumanoidModel().leftArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().leftArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().leftArm.zRot = 0;
            }
            event.setResult(TriState.TRUE);
        }
        if (isAimedGun(player.getItemInHand(InteractionHand.OFF_HAND))) {
            if (player.getMainArm() == HumanoidArm.RIGHT) {
                event.getHumanoidModel().leftArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().leftArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().leftArm.zRot = 0;
            } else {
                event.getHumanoidModel().rightArm.xRot = (event.getHumanoidModel().head.xRot - (float) Math.toRadians(80F));
                event.getHumanoidModel().rightArm.yRot = event.getHumanoidModel().head.yRot;
                event.getHumanoidModel().rightArm.zRot = 0;
            }
            event.setResult(TriState.TRUE);
        }
    }

    private static boolean isAimedGun(ItemStack stack) {
        return stack.getItem() instanceof BlasterItem || stack.getItem() instanceof LeitmotifItem;
    }

    private static final ResourceLocation MYSTIC_POT_OVERLAY = OpposingForce.location("textures/misc/mystic_pot_overlay.png");

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, OpposingForce.location("mystic_pot_overlay"), (guiGraphics, deltaTracker) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) return;
            if (!minecraft.player.getItemBySlot(EquipmentSlot.HEAD).is(OFItems.MYSTIC_POT.get())) return;
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            guiGraphics.blit(MYSTIC_POT_OVERLAY, 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        });
    }
}