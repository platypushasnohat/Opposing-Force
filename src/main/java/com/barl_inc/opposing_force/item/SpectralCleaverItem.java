package com.barl_inc.opposing_force.item;

import com.barl_inc.opposing_force.entity.projectile.Soul;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.item.CustomSweepParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;

public class SpectralCleaverItem extends SwordItem implements CustomSweepParticle {

    public SpectralCleaverItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.level().isClientSide && !target.isAlliedTo(player) && !(target instanceof OwnableEntity ownable && ownable.getOwner() == player) && player.level().getGameTime() - player.getPersistentData().getLong("SoulCooldown") >= 15) {
            player.getPersistentData().putLong("SoulCooldown", player.level().getGameTime());
            Vec3 back = Vec3.directionFromRotation(0.0F, player.getYRot() + 180.0F);
            Vec3 side = Vec3.directionFromRotation(0.0F, player.getYRot() + 90.0F);
            Vec3 position = player.position().add(back.scale(5.0D + player.getRandom().nextDouble() * 1.25D)).add(side.scale(player.getRandom().nextDouble() * 2.0D - 1.0D)).add(0.0D, player.getRandom().nextDouble() * 3.0D - 1.0D, 0.0D);
            float damage = Math.max(0.0F, 3.0F + (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 7.5F);
            float speed = 0.25F * (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
            player.level().addFreshEntity(new Soul(player.level(), player, target, position, damage, speed));
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public ParticleOptions getSweepParticle() {
        return OFParticleTypes.SPECTRAL_SWEEP.get();
    }
}