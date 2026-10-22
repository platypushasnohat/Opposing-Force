package com.barl_inc.opposing_force.events;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.client.model.armor.EmeraldArmorModel;
import com.barl_inc.opposing_force.client.model.armor.MoonShoesModel;
import com.barl_inc.opposing_force.client.model.entity.*;
import com.barl_inc.opposing_force.client.model.item.*;
import com.barl_inc.opposing_force.client.particle.*;
import com.barl_inc.opposing_force.client.render.entity.*;
import com.barl_inc.opposing_force.client.render.entity.layer.SpectralShieldLayer;
import com.barl_inc.opposing_force.client.render.item.OFItemExtensions;
import com.barl_inc.opposing_force.client.render.shader.SpectralLensOverlay;
import com.barl_inc.opposing_force.item.base.GunItem;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFModelLayers;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.platypushasnohat.sinew.events.custom.PoseHandEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.util.TriState;

import java.io.IOException;

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
        event.registerEntityRenderer(OFEntities.FILLET.get(), FilletRenderer::new);
        event.registerEntityRenderer(OFEntities.GHOST.get(), GhostRenderer::new);
        event.registerEntityRenderer(OFEntities.ROAMER.get(), RoamerRenderer::new);
        event.registerEntityRenderer(OFEntities.HAUNT.get(), context -> new PhasingMonsterRenderer<>(context, new HauntModel(context.bakeLayer(OFModelLayers.HAUNT)), "haunt", 1.1F));
        event.registerEntityRenderer(OFEntities.GRASPER.get(), GrasperRenderer::new);

        event.registerEntityRenderer(OFEntities.DICER_LASER.get(), DicerLaserRenderer::new);
        event.registerEntityRenderer(OFEntities.LASER_BOLT.get(), LaserBoltRenderer::new);
        event.registerEntityRenderer(OFEntities.LASER_BLADE.get(), LaserBladeRenderer::new);
        event.registerEntityRenderer(OFEntities.ACID_CHARGE.get(), AcidChargeRenderer::new);
        event.registerEntityRenderer(OFEntities.ACID_CLOUD.get(), NoopRenderer::new);
        event.registerEntityRenderer(OFEntities.MUDBALL.get(), MudballRenderer::new);
        event.registerEntityRenderer(OFEntities.MUD_SPLATTER.get(), MudSplatterRenderer::new);
        event.registerEntityRenderer(OFEntities.BULLET.get(), BulletRenderer::new);
        event.registerEntityRenderer(OFEntities.SAWBLADE.get(), SawbladeRenderer::new);
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
        event.registerLayerDefinition(OFModelLayers.FILLET, FilletModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.GHOST, GhostModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.ROAMER, RoamerModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.HAUNT, HauntModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.GRASPER, GrasperModel::createBodyLayer);

        event.registerLayerDefinition(OFModelLayers.LASER_BOLT, LaserBoltModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.ACID_CHARGE, AcidChargeModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.MUDBALL, MudballModel::createBodyLayer);

        event.registerLayerDefinition(OFModelLayers.BLASTER, BlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.TRI_BLASTER, TriBlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.SCATTER_BLASTER, ScatterBlasterModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.LEITMOTIF, LeitmotifModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.CRACKSHOT, CrackshotModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.JAEGER, JaegerModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.BOOMSTICK, BoomstickModel::createBodyLayer);
        event.registerLayerDefinition(OFModelLayers.GORLOFF, GorloffModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new SpectralShieldLayer<>(renderer));
            }
        }
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(OFParticleTypes.LASER_DUST.get(), LaserDustParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.LASER_IMPACT.get(), ImpactParticle.LaserFactory::new);
        event.registerSpriteSet(OFParticleTypes.LASER_SWEEP.get(), LaserSweepParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.ACID.get(), AcidParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.FIRE_BREATH.get(), FireBreathParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.SPORE_CLOUD.get(), SporeCloudParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.BULLET_TRAIL.get(), BulletTrailParticle.Factory::new);
        event.registerSpriteSet(OFParticleTypes.BULLET_IMPACT.get(), ImpactParticle.BulletFactory::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.TRI_BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.SCATTER_BLASTER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.LEITMOTIF.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.CRACKSHOT.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.JAEGER.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.BOOMSTICK.get());
        event.registerItem(OFItemExtensions.itemExtensions, OFItems.GORLOFF.get());

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
        HumanoidModel<?> humanoidModel = event.getHumanoidModel();
        boolean left = event.isLeftHanded();
        InteractionHand hand = left == (player.getMainArm() == HumanoidArm.LEFT) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if (poseGunArm(humanoidModel, left, player.getItemInHand(hand), player.getItemInHand(otherHand))) {
            event.setResult(TriState.TRUE);
        }
        if (player.isUsingItem() && !poseGunArm(humanoidModel, !left, player.getItemInHand(otherHand), player.getItemInHand(hand)) && player.getUseItem().getItem() instanceof GunItem && !player.getItemInHand(otherHand).isEmpty()) {
            ModelPart arm = left ? humanoidModel.rightArm : humanoidModel.leftArm;
            arm.xRot = arm.xRot * 0.5F - (float) Math.PI / 10.0F;
            arm.yRot = 0.0F;
        }
    }

    private static boolean poseGunArm(HumanoidModel<?> humanoidModel, boolean left, ItemStack stack, ItemStack other) {
        ModelPart arm = left ? humanoidModel.leftArm : humanoidModel.rightArm;
        ModelPart head = humanoidModel.head;
        boolean posed = true;
        if (stack.is(OFItems.GORLOFF)) {
            arm.xRot = head.xRot - (float) Math.toRadians(70.0F);
            arm.yRot = head.yRot;
        } else if (other.is(OFItems.GORLOFF)) {
            arm.xRot = head.xRot - (float) Math.toRadians(40.0F);
            arm.yRot = head.yRot + (float) Math.toRadians(left ? 40.0F : -40.0F);
        } else if (isAimedGun(stack)) {
            arm.xRot = head.xRot - (float) Math.toRadians(80.0F);
            arm.yRot = head.yRot;
        } else {
            posed = false;
        }
        if (posed) {
            arm.zRot = 0.0F;
        }
        return posed;
    }

    private static boolean isAimedGun(ItemStack stack) {
        return stack.getItem() instanceof GunItem && !stack.is(OFItems.GORLOFF);
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
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, OpposingForce.location("spectral_lens"), SpectralLensOverlay::render);
    }
    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), OpposingForce.location("spectral_lens"), DefaultVertexFormat.POSITION), shader -> SpectralLensOverlay.shader = shader);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        SpectralLensOverlay.tick();
    }
}