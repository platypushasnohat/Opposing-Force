package com.barl_inc.opposing_force.item.utils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class SlimyShielding {

    public static void absorbHit(LivingIncomingDamageEvent event, Item shield, int cooldown) {
        if (event.getEntity() instanceof Player player
                && player.level() instanceof ServerLevel serverLevel
                && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && !player.getCooldowns().isOnCooldown(shield)
                && player.invulnerableTime <= 10
                && CuriosApi.getCuriosInventory(player).map(inventory -> inventory.isEquipped(shield)).orElse(false)) {
            event.setAmount(0.1F);
            player.getCooldowns().addCooldown(shield, cooldown);
            serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, player.getX(), player.getY(0.5D), player.getZ(), 20, 0.3D, 0.5D, 0.3D, 0.4D);
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SLIME_ATTACK, player.getSoundSource(), 1.0F, 1.0F);
        }
    }

    public static void resetOnDamage(LivingDamageEvent.Post event, Item shield, int cooldown) {
        if (event.getEntity() instanceof Player player && event.getNewDamage() > 0.0F && player.getCooldowns().isOnCooldown(shield)) {
            player.getCooldowns().addCooldown(shield, cooldown);
        }
    }
}
