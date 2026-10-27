package com.barl_inc.opposing_force.item.melee;

import com.barl_inc.opposing_force.entity.projectile.Soul;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.item.CustomSweepParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpectralCleaverItem extends SwordItem implements CustomSweepParticle {

    public SpectralCleaverItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.level().isClientSide && !target.isAlliedTo(player) && !(target instanceof OwnableEntity ownable && ownable.getOwner() == player) && player.level().getGameTime() - player.getPersistentData().getLong("SoulCooldown") >= this.getSoulCooldown()) {
            player.getPersistentData().putLong("SoulCooldown", player.level().getGameTime());
            for (int i = 0; i < this.getSoulCount(); i++) {
                this.summonSoul(player, target);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    private void summonSoul(Player player, LivingEntity target) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, player.getYRot());
        Vec3 side = Vec3.directionFromRotation(0.0F, player.getYRot() + (player.getRandom().nextBoolean() ? 90.0F : -90.0F));
        Vec3 position = player.getRandom().nextInt(4) == 0 ? player.position().add(player.getRandom().nextDouble() * 4.0D - 2.0D, 8.0D + player.getRandom().nextDouble() * 2.0D, player.getRandom().nextDouble() * 4.0D - 2.0D) : player.position().add(side.scale(8.0D + player.getRandom().nextDouble() * 3.0D)).add(forward.scale(1.5D + player.getRandom().nextDouble() * 1.5D)).add(0.0D, player.getRandom().nextDouble() * 3.0D - 1.0D, 0.0D);
        float damage = Math.max(0.0F, 2.5F + ((float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 7.5F) / 2.0F);
        float speed = 0.25F * (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
        player.level().addFreshEntity(new Soul(player.level(), player, target, position, damage, speed));
        player.level().playSound(null, position.x, position.y, position.z, SoundEvents.PHANTOM_AMBIENT, SoundSource.PLAYERS, 1.0F, 0.5F);
    }

    public int getSoulCount() {
        return 1;
    }

    public int getSoulCooldown() {
        return 8;
    }

    @Override
    public ParticleOptions getSweepParticle() {
        return OFParticleTypes.SPECTRAL_SWEEP.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(OFTooltips.description("item.opposing_force.spectral_cleaver.description"));
    }
}
