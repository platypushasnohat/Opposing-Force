package com.barl_inc.opposing_force.client.render.item;

import com.barl_inc.opposing_force.item.melee.TachiItem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Quaternionf;

public class TachiAnimations {

    private static final Int2ObjectMap<State> STATES = new Int2ObjectOpenHashMap<>();

    //my 5 billion constants
    private static final float BODY_BEND = 25.0F;
    private static final float BODY_TWIST = 10.0F;
    private static final float HEAD_BEND = 6.0F;
    private static final float LEG_SPREAD = 20.0F;
    private static final float HOLDING_ARM_PITCH = -75.0F;
    private static final float HOLDING_ARM_CROSS = 50.0F;
    private static final float FREE_ARM_PITCH = 15.0F;
    private static final float ITEM_PITCH = 30.0F;
    private static final float ITEM_YAW = -45.0F;
    private static final float ITEM_ROLL = 105.0F;
    private static final float FIRST_PERSON_PITCH = 0.0F;
    private static final float FIRST_PERSON_YAW = -25.0F;
    private static final float FIRST_PERSON_ROLL = 90.0F;
    private static final int RELEASE_TICKS = 4;

    public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {
        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            float charge = getCharge(player, partialTick);
            boolean posed = charge > 0.0F && arm == player.getMainArm();
            if (posed) {
                int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
                poseStack.translate(invert * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
                poseStack.translate(invert * -0.2F * charge, 0.2F * charge, 0.0F);
                Quaternionf held = new Quaternionf().rotationXYZ(FIRST_PERSON_PITCH * Mth.DEG_TO_RAD, invert * FIRST_PERSON_YAW * Mth.DEG_TO_RAD, invert * FIRST_PERSON_ROLL * Mth.DEG_TO_RAD);
                poseStack.rotateAround(new Quaternionf().slerp(held, charge), invert * 0.0706F, -0.1348F, 0.0125F);
            }
            return posed;
        }
    };

    public static void tick() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            STATES.clear();
        } else {
            for (AbstractClientPlayer player : level.players()) {
                boolean charging = isCharging(player);
                State state = STATES.get(player.getId());
                if (charging && state == null) {
                    state = new State();
                    STATES.put(player.getId(), state);
                }
                if (state != null) {
                    state.tick(charging);
                }
            }
            STATES.int2ObjectEntrySet().removeIf(entry -> entry.getValue().isDone() || level.getEntity(entry.getIntKey()) == null);
        }
    }

    private static boolean isCharging(LivingEntity entity) {
        return entity.isUsingItem() && entity.getUsedItemHand() == InteractionHand.MAIN_HAND && entity.getUseItem().getItem() instanceof TachiItem;
    }

    public static float getCharge(LivingEntity entity, float partialTicks) {
        State state = STATES.get(entity.getId());
        float charge = 0.0F;
        if (state != null) {
            charge = inOutSine(Mth.lerp(partialTicks, state.prevCharge, state.charge));
        }
        return charge;
    }

    public static <T extends LivingEntity> void poseBody(HumanoidModel<T> model, T entity) {
        float charge = getCharge(entity, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
        if (charge > 0.0F) {
            boolean right = entity.getMainArm() == HumanoidArm.RIGHT;
            int side = right ? 1 : -1;
            ModelPart holdingArm = right ? model.rightArm : model.leftArm;
            ModelPart freeArm = right ? model.leftArm : model.rightArm;
            ModelPart frontLeg = right ? model.rightLeg : model.leftLeg;
            ModelPart backLeg = right ? model.leftLeg : model.rightLeg;

            float bend = BODY_BEND * Mth.DEG_TO_RAD * charge;
            float twist = -side * BODY_TWIST * Mth.DEG_TO_RAD * charge;
            model.body.xRot += bend;
            model.body.yRot += twist;
            model.body.y += 2.8F * charge;

            model.head.y += 3.7F * charge;
            model.head.xRot += HEAD_BEND * Mth.DEG_TO_RAD * charge;

            model.rightArm.y += 3.2F * charge;
            model.leftArm.y += 3.2F * charge;
            model.rightArm.x = Mth.lerp(charge, model.rightArm.x, -Mth.cos(twist) * 5.0F);
            model.rightArm.z = Mth.lerp(charge, model.rightArm.z, Mth.sin(twist) * 5.0F);
            model.leftArm.x = Mth.lerp(charge, model.leftArm.x, Mth.cos(twist) * 5.0F);
            model.leftArm.z = Mth.lerp(charge, model.leftArm.z, -Mth.sin(twist) * 5.0F);

            holdingArm.xRot = Mth.lerp(charge, holdingArm.xRot, HOLDING_ARM_PITCH * Mth.DEG_TO_RAD);
            holdingArm.yRot = Mth.lerp(charge, holdingArm.yRot, -side * HOLDING_ARM_CROSS * Mth.DEG_TO_RAD + twist);
            holdingArm.zRot = Mth.lerp(charge, holdingArm.zRot, 0.0F);
            freeArm.xRot = Mth.lerp(charge, freeArm.xRot, FREE_ARM_PITCH * Mth.DEG_TO_RAD);
            freeArm.yRot = Mth.lerp(charge, freeArm.yRot, twist);
            freeArm.zRot = Mth.lerp(charge, freeArm.zRot, side * 5.0F * Mth.DEG_TO_RAD);

            model.rightLeg.z += 3.9F * charge;
            model.leftLeg.z += 3.9F * charge;
            frontLeg.xRot = Mth.lerp(charge, frontLeg.xRot, -LEG_SPREAD * Mth.DEG_TO_RAD);
            backLeg.xRot = Mth.lerp(charge, backLeg.xRot, LEG_SPREAD * Mth.DEG_TO_RAD);
        }
    }

    public static void thirdPersonItem(LivingEntity entity, HumanoidArm arm, PoseStack poseStack) {
        float charge = getCharge(entity, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
        if (charge > 0.0F && arm == entity.getMainArm()) {
            int side = arm == HumanoidArm.RIGHT ? 1 : -1;
            Quaternionf held = new Quaternionf().rotationXYZ(ITEM_PITCH * Mth.DEG_TO_RAD, side * ITEM_YAW * Mth.DEG_TO_RAD, side * ITEM_ROLL * Mth.DEG_TO_RAD);
            poseStack.rotateAround(new Quaternionf().slerp(held, charge), 0.0F, -0.1486F, 0.1781F);
        }
    }

    private static float inOutSine(float x) {
        return -(Mth.cos(Mth.PI * x) - 1.0F) / 2.0F;
    }

    private static class State {

        private float charge;
        private float prevCharge;
        private boolean charging;

        private void tick(boolean charging) {
            this.charging = charging;
            this.prevCharge = this.charge;
            if (charging) {
                this.charge = Math.min(1.0F, this.charge + 1.0F / TachiItem.FULL_CHARGE_TICKS);
            } else {
                this.charge = Math.max(0.0F, this.charge - 1.0F / RELEASE_TICKS);
            }
        }

        private boolean isDone() {
            return !this.charging && this.charge <= 0.0F && this.prevCharge <= 0.0F;
        }
    }
}
