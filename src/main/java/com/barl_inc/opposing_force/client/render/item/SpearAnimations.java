package com.barl_inc.opposing_force.client.render.item;

import com.barl_inc.opposing_force.item.base.SpearAnimatedItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class SpearAnimations {

    public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {
        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
            if (isCharging(player, arm)) {
                poseStack.translate(invert * 0.56F, -0.52F, -0.72F);
                firstPerson(poseStack, Params.of(getTimeHeld(player, partialTick), getChargeTime(player)), invert, getChargeTurn(player));
            } else {
                poseStack.translate(invert * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
                firstPersonAttack(poseStack, swingProcess, invert);
            }
            return true;
        }
    };

    public static boolean isCharging(LivingEntity entity, HumanoidArm arm) {
        InteractionHand hand = arm == entity.getMainArm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        return entity.isUsingItem() && entity.getUsedItemHand() == hand && entity.getUseItem().getItem() instanceof SpearAnimatedItem;
    }

    private static float getChargeTime(LivingEntity entity) {
        return ((SpearAnimatedItem) entity.getUseItem().getItem()).getChargeTime();
    }

    private static float getChargeTurn(LivingEntity entity) {
        return ((SpearAnimatedItem) entity.getUseItem().getItem()).getChargeTurn();
    }

    private static float getTimeHeld(LivingEntity entity, float partialTick) {
        return entity.getTicksUsingItem() + partialTick;
    }

    private static float getTimeHeld(LivingEntity entity) {
        return getTimeHeld(entity, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
    }

    private static void firstPerson(PoseStack poseStack, Params params, int invert, float turn) {
        poseStack.translate(
                invert * (params.raise() * 0.15F + params.raiseEnd() * -0.05F),
                params.raise() * -0.075F + params.raiseMiddle() * 0.075F,
                params.raiseStart() * 0.05F + params.raiseEnd() * -0.05F
        );
        poseStack.rotateAround(Axis.XP.rotationDegrees(-65.0F * inOutBack(params.raise())), 0.0F, 0.1F, 0.0F);
        poseStack.rotateAround(Axis.YN.rotationDegrees(invert * -turn * progress(params.raise(), 0.5F, 0.55F)), invert * 0.15F, 0.0F, 0.0F);
    }

    private static void firstPersonAttack(PoseStack poseStack, float attack, int invert) {
        float start = inOutSine(progress(attack, 0.0F, 0.12F));
        float middle = outBack(progress(attack, 0.12F, 0.27F));
        float end = inOutExpo(progress(attack, 0.4F, 1.0F));
        poseStack.translate(invert * 0.1F * (start - middle), -0.075F * (start - end), 0.65F * (start - middle));
        poseStack.mulPose(Axis.XP.rotationDegrees(-70.0F * (start - end)));
        poseStack.translate(0.0F, 0.0F, -0.25F * (end - middle));
    }

    public static boolean isStabbing(LivingEntity entity) {
        return entity.swingingArm != null && entity.getItemInHand(entity.swingingArm).getItem() instanceof SpearAnimatedItem;
    }

    public static <T extends LivingEntity> void thirdPersonAttackArm(HumanoidModel<T> model, T entity) {
        HumanoidArm attackArm = entity.swingingArm == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();
        ModelPart arm = attackArm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        model.rightArm.yRot -= model.body.yRot;
        model.leftArm.yRot -= model.body.yRot;
        model.leftArm.xRot -= model.body.yRot;
        float prepare = inOutSine(progress(model.attackTime, 0.0F, 0.05F));
        float attack = Mth.square(progress(model.attackTime, 0.05F, 0.2F));
        float retract = inOutExpo(progress(model.attackTime, 0.4F, 1.0F));
        arm.xRot += (90.0F * prepare - 120.0F * attack + 30.0F * retract) * Mth.DEG_TO_RAD;
    }

    public static boolean poseArm(LivingEntity entity, HumanoidModel<?> model, boolean left) {
        HumanoidArm holdingArm = left ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        boolean charging = isCharging(entity, holdingArm);
        if (charging) {
            ModelPart arm = left ? model.leftArm : model.rightArm;
            int invert = left ? -1 : 1;
            Params params = Params.of(getTimeHeld(entity), getChargeTime(entity));
            arm.yRot = -0.1F * invert + model.head.yRot;
            arm.xRot = -Mth.HALF_PI + model.head.xRot + 0.8F;
            if (entity.isFallFlying() || model.swimAmount > 0.0F) {
                arm.xRot -= 0.9599311F;
            }
            arm.yRot = Mth.DEG_TO_RAD * Mth.clamp(Mth.RAD_TO_DEG * arm.yRot, -60.0F, 60.0F);
            arm.xRot = Mth.DEG_TO_RAD * Mth.clamp(Mth.RAD_TO_DEG * arm.xRot, -120.0F, 30.0F);
            arm.xRot += Mth.DEG_TO_RAD * (-40.0F * params.raiseStart() + 30.0F * params.raiseMiddle() - 20.0F * params.raiseEnd());
        }
        return charging;
    }

    public static void thirdPersonItem(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm, PoseStack poseStack) {
        HumanoidArm attackArm = entity.swingingArm == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();
        if (model.attackTime > 0.0F && isStabbing(entity) && attackArm == arm) {
            float attack = Mth.square(progress(model.attackTime, 0.05F, 0.2F));
            float retract = inOutExpo(progress(model.attackTime, 0.4F, 1.0F));
            poseStack.rotateAround(Axis.XN.rotationDegrees(70.0F * (attack - retract)), 0.0F, -0.125F, 0.125F);
            poseStack.translate(0.0F, ((SpearAnimatedItem) entity.getItemInHand(entity.swingingArm).getItem()).getThrustDistance() * (attack - retract), 0.0F);
        }
        if (isCharging(entity, arm)) {
            Params params = Params.of(getTimeHeld(entity), getChargeTime(entity));
            int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate(0.0F, 0.0F, -((SpearAnimatedItem) entity.getUseItem().getItem()).getThrustDistance() * (1.0F - outBack(1.0F - params.raise())));
            poseStack.rotateAround(Axis.XN.rotationDegrees(70.0F * params.raise()), 0.0F, -0.03125F, 0.125F);
            poseStack.rotateAround(Axis.YP.rotationDegrees(invert * getChargeTurn(entity) * params.raise()), 0.0F, 0.0F, 0.125F);
        }
    }

    private static float progress(float time, float start, float end) {
        return Mth.clamp(Mth.inverseLerp(time, start, end), 0.0F, 1.0F);
    }

    private static float outBack(float x) {
        return 1.0F + 2.70158F * Mth.square(x - 1.0F) * (x - 1.0F) + 1.70158F * Mth.square(x - 1.0F);
    }

    private static float inOutSine(float x) {
        return -(Mth.cos(Mth.PI * x) - 1.0F) / 2.0F;
    }

    private static float inOutExpo(float x) {
        float result;
        if (x == 0.0F || x == 1.0F) {
            result = x;
        } else if (x < 0.5F) {
            result = (float) Math.pow(2.0D, 20.0D * x - 10.0D) / 2.0F;
        } else {
            result = (2.0F - (float) Math.pow(2.0D, -20.0D * x + 10.0D)) / 2.0F;
        }
        return result;
    }

    private static float inOutBack(float x) {
        float result;
        if (x < 0.5F) {
            result = 4.0F * x * x * (7.189819F * x - 2.5949094F) / 2.0F;
        } else {
            float dt = 2.0F * x - 2.0F;
            result = (dt * dt * (3.5949094F * dt + 2.5949094F) + 2.0F) / 2.0F;
        }
        return result;
    }

    private record Params(float raise, float raiseStart, float raiseMiddle, float raiseEnd) {

        private static Params of(float time, float chargeTime) {
            float raise = progress(time, 0.0F, chargeTime);
            return new Params(
                    raise,
                    progress(raise, 0.0F, 0.5F),
                    progress(raise, 0.5F, 0.8F),
                    progress(raise, 0.8F, 1.0F)
            );
        }
    }
}
