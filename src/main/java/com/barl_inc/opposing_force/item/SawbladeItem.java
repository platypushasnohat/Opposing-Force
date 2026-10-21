package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.Sawblade;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class SawbladeItem extends Item implements ProjectileItem {

	public SawbladeItem(Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, SinewSoundUtils.randomizePitch(player));
		player.getCooldowns().addCooldown(this, 3);
		if (!level.isClientSide()) {
			Sawblade sawblade = new Sawblade(level, player, stack.copyWithCount(1), null);
			sawblade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 0.75F);
			if (player.getAbilities().instabuild) {
				sawblade.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			}
			level.addFreshEntity(sawblade);
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
		Sawblade sawblade = new Sawblade(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
		sawblade.pickup = AbstractArrow.Pickup.ALLOWED;
		return sawblade;
	}
}