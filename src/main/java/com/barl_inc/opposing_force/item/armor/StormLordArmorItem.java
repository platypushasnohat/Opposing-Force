package com.barl_inc.opposing_force.item.armor;

import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.network.MidairJumpEffectPayload;
import com.barl_inc.opposing_force.registry.OFAttributes;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.item.ArmorDefinition;
import com.platypushasnohat.sinew.item.SkinLayerHidingArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class StormLordArmorItem extends SkinLayerHidingArmorItem {

    public StormLordArmorItem(Type type, Properties properties, ArmorDefinition definition) {
        super(type, properties, definition);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(OFTooltips.description("item.opposing_force.storm_lord.set_bonus"));
        tooltip.add(OFTooltips.description("item.opposing_force.storm_lord.set_bonus.electrified"));
        tooltip.add(OFTooltips.description("item.opposing_force.storm_lord.set_bonus.dash"));
        tooltip.add(OFTooltips.description("item.opposing_force.storm_lord.set_bonus.kirin").copy().withStyle(ChatFormatting.ITALIC));
    }

    public static boolean hasFullSet(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(OFItems.STORM_LORD_HELMET)
                && entity.getItemBySlot(EquipmentSlot.CHEST).is(OFItems.STORM_LORD_CHESTPLATE)
                && entity.getItemBySlot(EquipmentSlot.LEGS).is(OFItems.STORM_LORD_LEGGINGS)
                && entity.getItemBySlot(EquipmentSlot.FEET).is(OFItems.STORM_LORD_BOOTS);
    }

    public static boolean preventsFall(LivingEntity entity) {
        return entity.getPersistentData().contains("opposing_force:storm_lord_fall");
    }

    public static void tickFall(Player player) {
        boolean settled = player.level().getGameTime() - player.getPersistentData().getLong("opposing_force:storm_lord_fall") > 5L;
        if ((player.onGround() && player.fallDistance <= 0.0F || player.isInWater()) && settled) {
            player.getPersistentData().remove("opposing_force:storm_lord_fall");
        }
    }

    public static boolean canDash(Player player) {
        return player.isAlive() && !player.isSpectator() && hasFullSet(player) && !player.getCooldowns().isOnCooldown(OFItems.STORM_LORD_CHESTPLATE.get());
    }

    public static void airJump(ServerPlayer player) {
        if (player.getAttributeValue(OFAttributes.KIRIN_JUMPS) >= 1.0D) {
            player.resetFallDistance();
            player.getPersistentData().putLong("opposing_force:storm_lord_fall", player.level().getGameTime());
            PacketDistributor.sendToPlayersTrackingEntity(player, new MidairJumpEffectPayload(player.getId()));
        }
    }
}
