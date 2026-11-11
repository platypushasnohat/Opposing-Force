package com.barl_inc.opposing_force.item.melee;

import com.barl_inc.opposing_force.entity.projectile.HeroSwordBeam;
import com.barl_inc.opposing_force.item.utils.OFTooltips;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.platypushasnohat.sinew.item.CustomSweepParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class HeroSwordItem extends SwordItem implements CustomSweepParticle {

    public static final float DEFAULT_ATTACK_DAMAGE = 7.0F;
    public static final float FULL_CHARGE = 0.9F;

    public HeroSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public static void shootBeam(ServerPlayer player) {
        if (player.getMainHandItem().getItem() instanceof HeroSwordItem && !player.isSpectator() && player.getAttackStrengthScale(0.5F) >= FULL_CHARGE) {
            float attackDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float damage = Math.max(0.0F, DEFAULT_ATTACK_DAMAGE / 3.0F + (attackDamage - DEFAULT_ATTACK_DAMAGE) / 2.0F);
            HeroSwordBeam beam = new HeroSwordBeam(player.level(), player, damage);
            beam.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.0F);
            player.level().addFreshEntity(beam);
            player.getMainHandItem().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.8F, 1.6F);
        }
    }

    @Override
    public ParticleOptions getSweepParticle() {
        return OFParticleTypes.HERO_SWEEP.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(OFTooltips.description("item.opposing_force.hero_sword.description"));
    }
}
