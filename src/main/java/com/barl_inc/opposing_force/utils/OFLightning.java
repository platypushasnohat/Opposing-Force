package com.barl_inc.opposing_force.utils;

import com.barl_inc.opposing_force.config.OFConfig;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OFLightning {

    public static boolean isEnabled() {
        return OFConfig.OF_LIGHTNING.get();
    }

    public static float getSuperChance(LightningBolt bolt) {
        float chance = 1.0F / 12.0F;
        if (bolt.getCause() != null) {
            List<ThrownTrident> tridents = bolt.level().getEntitiesOfClass(ThrownTrident.class, bolt.getBoundingBox().inflate(4.0D), trident -> trident.getOwner() == bolt.getCause());
            if (!tridents.isEmpty()) {
                Holder<Enchantment> channeling = bolt.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.CHANNELING);
                chance = EnchantmentHelper.getItemEnchantmentLevel(channeling, tridents.get(0).getWeaponItem()) >= 2 ? 0.25F : 0.0F;
            }
        }
        return chance;
    }

    public static int getChannelingLevel(ItemStack stack, Holder<Enchantment> channeling) {
        return Math.max(EnchantmentHelper.getItemEnchantmentLevel(channeling, stack), stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(channeling));
    }

    public static void impact(ServerLevel level, LightningBolt bolt, double reach, float maxDamage, int electrifiedTicks) {
        Vec3 center = bolt.position().add(0.0D, 0.5D, 0.0D);
        burst(level, center.x, bolt.getY(), center.z, (int) (reach * 2.0D), reach * 0.5D, (int) (reach * 6.0D), (float) reach * 0.6F);
        for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(reach))) {
            double distance = Math.sqrt(living.distanceToSqr(center)) / reach;
            Vec3 push = new Vec3(living.getX() - center.x, living.getEyeY() - center.y, living.getZ() - center.z);
            if (living.isAlive() && !living.isSpectator() && living != bolt.getCause() && distance <= 1.0D && push.lengthSqr() > 0.0D) {
                double strength = (1.0D - distance) * Explosion.getSeenPercent(center, living);
                if (strength > 0.0D && living.hurt(OFDamageTypes.causeElectricDamage(level.registryAccess(), bolt, bolt.getCause()), (float) (maxDamage * strength))) {
                    double knockback = strength * (1.0D - living.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
                    living.setDeltaMovement(living.getDeltaMovement().add(push.normalize().scale(knockback)));
                    living.hurtMarked = true;
                    living.addEffect(new MobEffectInstance(OFMobEffects.ELECTRIFIED, electrifiedTicks, 0), bolt);
                }
            }
        }
    }

    public static void burst(ServerLevel level, double x, double y, double z, int minBolts, double minLength, int arcs, float volume) {
        RandomSource random = level.getRandom();
        int flatBolts = minBolts + random.nextInt(4);
        for (int i = 0; i < flatBolts; i++) {
            burstBolt(level, random, x, y, z, (i + random.nextFloat() * 0.6F) / flatBolts * Mth.TWO_PI, 0.05F + random.nextFloat() * 0.1F, minLength);
        }
        int domeBolts = (int) (flatBolts * 1.5F);
        for (int i = 0; i < domeBolts; i++) {
            burstBolt(level, random, x, y, z, (i + random.nextFloat() * 0.6F) / domeBolts * Mth.TWO_PI, 0.3F + random.nextFloat() * 1.0F, minLength);
        }
        level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), x, y + 0.3D, z, arcs, minLength * 0.4D, 0.2D, minLength * 0.4D, 0.0D);
        level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), x, y + minLength * 0.4D, z, arcs, minLength * 0.4D, minLength * 0.4D, minLength * 0.4D, 0.0D);
        level.playSound(null, x, y, z, OFSoundEvents.ELECTRIC_CHARGE_DISSIPATE.get(), SoundSource.WEATHER, volume, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);
    }

    private static void burstBolt(ServerLevel level, RandomSource random, double x, double y, double z, float angle, float elevation, double minLength) {
        double length = minLength + random.nextDouble() * minLength;
        double reachX = Mth.cos(angle) * Mth.cos(elevation) * length;
        double reachY = Mth.sin(elevation) * length;
        double reachZ = Mth.sin(angle) * Mth.cos(elevation) * length;
        double startX = x + Mth.cos(angle) * 0.4D;
        double startY = y + 0.1D;
        double startZ = z + Mth.sin(angle) * 0.4D;
        level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX, startY, startZ, 0, reachX, reachY, reachZ, 1.0D);
        double split = 0.3D + random.nextDouble() * 0.4D;
        float branchAngle = angle + (random.nextBoolean() ? 0.6F : -0.6F);
        float branchElevation = Mth.clamp(elevation + (random.nextFloat() - 0.5F) * 0.6F, 0.02F, 1.4F);
        double branchLength = length * (1.0D - split) * 0.7D;
        level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX + reachX * split, startY + reachY * split, startZ + reachZ * split, 0, Mth.cos(branchAngle) * Mth.cos(branchElevation) * branchLength, Mth.sin(branchElevation) * branchLength, Mth.sin(branchAngle) * Mth.cos(branchElevation) * branchLength, 1.0D);
    }
}