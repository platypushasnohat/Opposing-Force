package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.registry.OFParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class KnockbackSporesItem extends Item {

    public KnockbackSporesItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PUFFER_FISH_BLOW_OUT, SoundSource.PLAYERS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = player.position().add(0.0D, 0.8D, 0.0D);
            for (int i = 0; i < 48; i++) {
                float angle = i * Mth.TWO_PI / 48.0F;
                double speed = 0.18D + level.getRandom().nextDouble() * 0.08D;
                serverLevel.sendParticles(OFParticleTypes.SPORE_CLOUD.get(), center.x, center.y + (level.getRandom().nextDouble() - 0.5D) * 0.8D, center.z, 0, Mth.cos(angle) * speed, 0.02D, Mth.sin(angle) * speed, 1.0D);
            }
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4.0D), entity -> entity != player && entity.isAlive() && !entity.isAlliedTo(player) && !(entity instanceof OwnableEntity ownable && ownable.getOwner() == player))) {
                Vec3 away = entity.position().subtract(player.position()).multiply(1.0D, 0.0D, 1.0D);
                double distance = away.length();
                if (distance <= 4.0D) {
                    if (distance < 1.0E-4D) {
                        away = player.getLookAngle().multiply(1.0D, 0.0D, 1.0D);
                    }
                    double strength = (1.6D - distance * 0.25D) * (1.0D - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    entity.push(away.normalize().scale(strength).add(0.0D, 0.35D * strength, 0.0D));
                    entity.hurtMarked = true;
                }
            }
        }
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
