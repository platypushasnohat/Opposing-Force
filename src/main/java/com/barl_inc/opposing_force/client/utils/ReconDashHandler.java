package com.barl_inc.opposing_force.client.utils;

import com.barl_inc.opposing_force.item.armor.ReconKnightArmorItem;
import com.barl_inc.opposing_force.network.ReconDashPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class ReconDashHandler {

    private static final int DOUBLE_TAP_WINDOW = 7;

    private static int lastTapTick = -100;
    private static boolean wasDown;

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        boolean down = isSprintKeyDown(minecraft);
        boolean pressed = down && !wasDown;
        wasDown = down;

        if (player != null && minecraft.screen == null && pressed) {
            boolean doubleTap = player.tickCount - lastTapTick <= DOUBLE_TAP_WINDOW;
            if (doubleTap && ReconKnightArmorItem.canDash(player)) {
                PacketDistributor.sendToServer(new ReconDashPayload(getDirection(minecraft.options, player.getRandom())));
                lastTapTick = -100;
            } else {
                lastTapTick = player.tickCount;
            }
        }
    }

    private static boolean isSprintKeyDown(Minecraft minecraft) {
        InputConstants.Key key = minecraft.options.keySprint.getKey();
        long window = minecraft.getWindow().getWindow();
        boolean down;
        if (key.getType() == InputConstants.Type.MOUSE) {
            down = GLFW.glfwGetMouseButton(window, key.getValue()) == GLFW.GLFW_PRESS;
        } else if (key.getType() == InputConstants.Type.KEYSYM && key.getValue() != InputConstants.UNKNOWN.getValue()) {
            down = InputConstants.isKeyDown(window, key.getValue());
        } else {
            down = false;
        }
        return down;
    }

    //horrible slop, yandere dev level code. this code is so fucking bad but im tired and im not going to change it. oh my god this fucking sucks
    private static int getDirection(Options options, RandomSource random) {
        boolean forward = options.keyUp.isDown();
        boolean back = options.keyDown.isDown();
        boolean left = options.keyLeft.isDown();
        boolean right = options.keyRight.isDown();
        int direction;
        if (forward && back) {
            direction = random.nextBoolean() ? ReconKnightArmorItem.DASH_FORWARD : ReconKnightArmorItem.DASH_BACK;
        } else if (forward) {
            direction = ReconKnightArmorItem.DASH_FORWARD;
        } else if (back) {
            direction = ReconKnightArmorItem.DASH_BACK;
        } else if (left && right) {
            direction = random.nextBoolean() ? ReconKnightArmorItem.DASH_LEFT : ReconKnightArmorItem.DASH_RIGHT;
        } else if (left) {
            direction = ReconKnightArmorItem.DASH_LEFT;
        } else if (right) {
            direction = ReconKnightArmorItem.DASH_RIGHT;
        } else {
            direction = ReconKnightArmorItem.DASH_FORWARD;
        }
        return direction;
    }
}