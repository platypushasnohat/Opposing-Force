package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.registry.OFItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = OpposingForce.MOD_ID)
public class DebugEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        Item upgrade = getUpgrade(event.getItemStack());
        if (upgrade != null && OFDebugItems.isDebugPlayer(player) && event.getLevel().getBlockState(pos).is(Blocks.CRYING_OBSIDIAN)) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                player.setItemInHand(event.getHand(), new ItemStack(upgrade));
                serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 40, 0.4D, 0.2D, 0.4D, 0.1D);
                serverLevel.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1.0F, 0.8F);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player
                && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && CuriosApi.getCuriosInventory(player).map(inventory -> inventory.isEquipped(OFDebugItems.SLIMEWARD.get())).orElse(false)) {
            if (!player.getCooldowns().isOnCooldown(OFDebugItems.SLIMEWARD.get()) && player.invulnerableTime <= 10 && player.level() instanceof ServerLevel serverLevel) {
                event.setAmount(0.1F);
                player.getCooldowns().addCooldown(OFDebugItems.SLIMEWARD.get(), 10);
                serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, player.getX(), player.getY(0.5D), player.getZ(), 20, 0.3D, 0.5D, 0.3D, 0.4D);
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SLIME_ATTACK, player.getSoundSource(), 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof Player player && event.getNewDamage() > 0.0F && player.getCooldowns().isOnCooldown(OFDebugItems.SLIMEWARD.get())) {
            player.getCooldowns().addCooldown(OFDebugItems.SLIMEWARD.get(), 20);
        }
    }

    @Nullable
    private static Item getUpgrade(ItemStack stack) {
        Item upgrade = null;
        if (stack.is(OFItems.CURSED_RINGLINE.get())) {
            upgrade = OFDebugItems.RINGMASTER.get();
        } else if (stack.is(OFItems.SPECTRAL_CLEAVER.get())) {
            upgrade = OFDebugItems.SOULCLEAVE.get();
        } else if (stack.is(OFItems.SPECTRAL_SHIELD.get())) {
            upgrade = OFDebugItems.KNIGHTSOUL.get();
        } else if (stack.is(OFItems.SLIMY_SHIELD.get())) {
            upgrade = OFDebugItems.SLIMEWARD.get();
        }
        return upgrade;
    }
}
