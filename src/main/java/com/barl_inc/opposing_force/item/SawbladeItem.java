package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.Sawblade;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SawbladeItem extends Item {

	public SawbladeItem(Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, SinewSoundUtils.randomizePitch(player));
		player.getCooldowns().addCooldown(this, 10);
		if (!level.isClientSide) {
			Sawblade sawblade = OFEntities.SAWBLADE.get().create(level);
			if (sawblade != null) {
				sawblade.setPos(player.getX(), player.getEyeY() - 0.1F, player.getZ());
				sawblade.setDeltaMovement(player.getLookAngle().scale(1.25D));
				sawblade.setYRot(180.0F + player.getYHeadRot());
				sawblade.setOwner(player);
				sawblade.setLifespan(80);
				level.addFreshEntity(sawblade);
			}
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        OFTooltips.addStats(tooltipComponents, "item.opposing_force.tooltip.when_thrown", OFTooltips.rangedDamage(Sawblade.DAMAGE, 1));
    }
}
