package com.barl_inc.opposing_force.debug;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.item.utils.SlimyShielding;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = OpposingForce.MOD_ID)
public class DebugEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        Item upgrade = OFDebugItems.getUpgrade(event.getItemStack().getItem());
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
        SlimyShielding.absorbHit(event, OFDebugItems.SLIMEWARD.get(), 20);
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        SlimyShielding.resetOnDamage(event, OFDebugItems.SLIMEWARD.get(), 20);
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof ItemEntity item) {
            item.setItem(OFDebugItems.revert(item.getItem()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof Player player) {
            OFDebugItems.revertContainer(player.getInventory());
            OFDebugItems.revertCurios(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide && !OFDebugItems.isDebugPlayer(player)) {
            OFDebugItems.revertContainer(player.getInventory());
            OFDebugItems.revertCurios(player);
        }
    }

    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        Player player = event.getEntity();
        if (OFDebugItems.isDebugPlayer(player)) {
            for (Slot slot : event.getContainer().slots) {
                if (slot.container != player.getInventory() && slot.container != player.getEnderChestInventory()) {
                    ItemStack stack = slot.getItem();
                    ItemStack reverted = OFDebugItems.revert(stack);
                    if (reverted != stack) {
                        slot.set(reverted);
                    }
                }
            }
        }
    }
}
