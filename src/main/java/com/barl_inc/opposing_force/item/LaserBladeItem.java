package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.LaserBlade;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LaserBladeItem extends SwordItem {

    private int swingSoundCooldown = 0;

    public LaserBladeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        if (!level.isClientSide) {
            Vec3 position = player.position().add(0, player.getBbHeight() * 0.5F, 0);
            LaserBlade laserBlade = new LaserBlade(level, position.x, position.y, position.z);
            laserBlade.setOwner(player);
            laserBlade.setItem(stack);
            laserBlade.setReturnTime(20);
            laserBlade.setDamage(12.0F);
            laserBlade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 0.0F);
            level.addFreshEntity(laserBlade);
//            player.getInventory().removeItem(stack);
            level.playSound(null, laserBlade.blockPosition(), OFSoundEvents.LASER_BLADE_SWING.get(), SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(level));
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            player.swing(hand, true);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (this.swingSoundCooldown > 0) {
            this.swingSoundCooldown--;
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (!entity.level().isClientSide && this.swingSoundCooldown == 0) {
            this.swingSoundCooldown = 10;
            if (entity instanceof ServerPlayer) {
                entity.level().playSound(null, entity.blockPosition(), OFSoundEvents.LASER_BLADE_SWING.get(), SoundSource.PLAYERS, 1.0F, SinewSoundUtils.randomizePitch(entity));
            }
        }
        return super.onEntitySwing(stack, entity, hand);
    }
}
