package com.barl_inc.opposing_force.item.armor;

import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.network.ReconDashEffectPayload;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.item.ArmorDefinition;
import com.platypushasnohat.sinew.item.SkinLayerHidingArmorItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class ReconKnightArmorItem extends SkinLayerHidingArmorItem {

    public static final int DASH_FORWARD = 0;
    public static final int DASH_BACK = 1;
    public static final int DASH_LEFT = 2;
    public static final int DASH_RIGHT = 3;

    public static final int DASH_COOLDOWN = 80;
    private static final double DASH_DISTANCE = 6.5D;
    private static final double DASH_STEP = 0.25D;

    private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public ReconKnightArmorItem(Type type, Properties properties, ArmorDefinition definition) {
        super(type, properties, definition);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(OFTooltips.description("item.opposing_force.recon_knight.set_bonus"));
    }

    public static boolean hasFullSet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(OFItems.RECON_KNIGHT_HELMET)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(OFItems.RECON_KNIGHT_CHESTPLATE)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(OFItems.RECON_KNIGHT_LEGGINGS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(OFItems.RECON_KNIGHT_BOOTS);
    }

    public static boolean canDash(Player player) {
        return player.isAlive() && !player.isSpectator() && hasFullSet(player) && !player.getCooldowns().isOnCooldown(OFItems.RECON_KNIGHT_CHESTPLATE.get());
    }

    public static void dash(ServerPlayer player, int direction) {
        if (canDash(player)) {
            performDash(player, direction, DASH_DISTANCE);
        } else if (StormLordArmorItem.canDash(player)) {
            performDash(player, direction, 10.0D);
        }
    }

    public static void performDash(ServerPlayer player, int direction, double dashDistance) {
        if (direction >= DASH_FORWARD && direction <= DASH_RIGHT) {
            ServerLevel level = player.serverLevel();
            Vec3 heading = getHeading(player, direction);
            Vec3 start = player.position();

            double distance = 0.0D;
            boolean clear = true;
            while (clear && distance + DASH_STEP <= dashDistance) {
                AABB box = player.getBoundingBox().move(heading.scale(distance + DASH_STEP));
                clear = level.noCollision(player, box);
                if (clear) {
                    distance += DASH_STEP;
                }
            }

            Vec3 end = start.add(heading.scale(distance));
            spawnDashEffects(level, player, start, end);

            player.connection.teleport(end.x, end.y, end.z, player.getYRot(), player.getXRot());
            player.resetFallDistance();
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ReconDashEffectPayload(player.getId(), start, end));

            for (EquipmentSlot slot : SLOTS) {
                ItemStack stack = player.getItemBySlot(slot);
                player.getCooldowns().addCooldown(stack.getItem(), DASH_COOLDOWN);
                stack.hurtAndBreak(1, player, slot);
            }
        }
    }

    private static Vec3 getHeading(Player player, int direction) {
        float yaw = player.getYRot() * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
        Vec3 left = new Vec3(Mth.cos(yaw), 0.0D, Mth.sin(yaw));
        Vec3 heading;
        if (direction == DASH_BACK) {
            heading = forward.reverse();
        } else if (direction == DASH_LEFT) {
            heading = left;
        } else if (direction == DASH_RIGHT) {
            heading = left.reverse();
        } else {
            heading = forward;
        }
        return heading;
    }

    private static void spawnDashEffects(ServerLevel level, Player player, Vec3 start, Vec3 end) {
        Vec3 back = start.subtract(end);
        double length = back.length();
        if (length > 0.5D) {
            Vec3 direction = back.normalize();
            int bolts = 5 + level.random.nextInt(3);
            for (int i = 0; i < bolts; i++) {
                double originX = end.x + (level.random.nextDouble() - 0.5D) * player.getBbWidth();
                double originY = end.y + 0.2D + level.random.nextDouble() * (player.getBbHeight() - 0.4D);
                double originZ = end.z + (level.random.nextDouble() - 0.5D) * player.getBbWidth();
                double boltLength = length * (0.6D + level.random.nextDouble() * 0.4D);
                Vec3 spread = new Vec3(level.random.nextGaussian(), level.random.nextGaussian() * 0.5D, level.random.nextGaussian()).scale(0.12D);
                Vec3 reach = direction.add(spread).normalize().scale(boltLength);
                level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), originX, originY, originZ, 0, reach.x, reach.y, reach.z, 1.0D);
            }
        }

        level.playSound(null, start.x, start.y, start.z, OFSoundEvents.ELECTRIC_CHARGE_ZAP.get(), SoundSource.PLAYERS, 1.0F, 1.2F + level.random.nextFloat() * 0.2F);
        level.playSound(null, end.x, end.y, end.z, OFSoundEvents.ELECTRIC_CHARGE_DISSIPATE.get(), SoundSource.PLAYERS, 0.6F, 1.4F + level.random.nextFloat() * 0.2F);
    }
}
