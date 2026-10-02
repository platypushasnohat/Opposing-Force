package com.barl_inc.opposing_force.mixins;

import com.barl_inc.opposing_force.config.OFConfig;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {

    @Shadow
    private static @Final ResourceLocation LEADER_ZOMBIE_BONUS_ID;

    @SuppressWarnings("WrongEntityDataParameterClass")
    @Unique
    private static final EntityDataAccessor<Boolean> DATA_IS_LEADER = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);

    @Shadow
    protected abstract boolean supportsBreakDoorGoal();

    @Shadow
    public abstract void setCanBreakDoors(boolean canBreakDoors);

    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    private void opposingForce$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_IS_LEADER, false);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void opposingForce$addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean("Leader", this.opposingForce$isLeader());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void opposingForce$readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.opposingForce$setLeader(compoundTag.getBoolean("Leader"));
    }

    @Unique
    public boolean opposingForce$isLeader() {
        return this.entityData.get(DATA_IS_LEADER);
    }

    @Unique
    public void opposingForce$setLeader(boolean leader) {
        this.entityData.set(DATA_IS_LEADER, leader);
        if (OFConfig.ZOMBIE_TWEAKS.get() && leader) {
            this.opposingForce$setupLeaderAttributes();
        }
    }

    @Unique
    public void opposingForce$spawnReinforcementParticles(ServerLevel serverLevel, Zombie reinforcement, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            double xSpeed = reinforcement.getRandom().nextGaussian() * 0.02D;
            double ySpeed = reinforcement.getRandom().nextGaussian() * 0.05D;
            double zSpeed = reinforcement.getRandom().nextGaussian() * 0.02D;
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, reinforcement.getRandomX(1.0D), reinforcement.getRandomY(), reinforcement.getRandomZ(1.0D), 1, xSpeed, ySpeed, zSpeed, 0.03D);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Unique
    private void opposingForce$setupLeaderAttributes() {
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, 0.5F, AttributeModifier.Operation.ADD_VALUE));
        this.getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, 20.0F, AttributeModifier.Operation.ADD_VALUE));
        this.getAttribute(Attributes.SCALE).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, 0.3F, AttributeModifier.Operation.ADD_VALUE));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, 2.0F, AttributeModifier.Operation.ADD_VALUE));
        this.setCanBreakDoors(this.supportsBreakDoorGoal());
    }

    @Inject(at = @At("HEAD"), method = "handleAttributes", cancellable = true)
    private void opposingForce$handleAttributes(float difficulty, CallbackInfo ci) {
        if (OFConfig.ZOMBIE_TWEAKS.get()) {
            ci.cancel();
            if (this.getRandom().nextFloat() < 0.07F) {
                this.opposingForce$setLeader(true);
                this.heal(this.getMaxHealth());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    public void opposingForce$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (OFConfig.ZOMBIE_TWEAKS.get()) {
            cir.cancel();
            if (!super.hurt(source, amount)) {
                cir.setReturnValue(false);
            } else if (!(this.level() instanceof ServerLevel serverLevel)) {
                cir.setReturnValue(false);
            } else {
                LivingEntity target = this.getTarget();
                if (target == null && source.getEntity() instanceof LivingEntity) {
                    target = (LivingEntity) source.getEntity();
                }
                if (target != null && this.opposingForce$isLeader() && (double) this.getRandom().nextFloat() < this.getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE) && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    int xFloor = Mth.floor(this.getX());
                    int yFloor = Mth.floor(this.getY());
                    int zFloor = Mth.floor(this.getZ());
                    EntityType<? extends Zombie> entityType = (EntityType<? extends Zombie>) this.getType();
                    Zombie reinforcement = entityType.create(this.level());
                    if (reinforcement != null) {
                        for (int l = 0; l < 50; l++) {
                            int xPos = xFloor + Mth.nextInt(this.getRandom(), 4, 8) * Mth.nextInt(this.getRandom(), -1, 1);
                            int yPos = yFloor + Mth.nextInt(this.getRandom(), 4, 8) * Mth.nextInt(this.getRandom(), -1, 1);
                            int zPos = zFloor + Mth.nextInt(this.getRandom(), 4, 8) * Mth.nextInt(this.getRandom(), -1, 1);
                            BlockPos blockpos = new BlockPos(xPos, yPos, zPos);
                            if (SpawnPlacements.isSpawnPositionOk(entityType, this.level(), blockpos) && SpawnPlacements.checkSpawnRules(entityType, serverLevel, MobSpawnType.REINFORCEMENT, blockpos, this.level().getRandom())) {
                                reinforcement.setPos(xPos, yPos, zPos);
                                if (!this.level().hasNearbyAlivePlayer(xPos, yPos, zPos, 7.0D) && this.level().isUnobstructed(reinforcement) && this.level().noCollision(reinforcement) && !this.level().containsAnyLiquid(reinforcement.getBoundingBox())) {
                                    if (!(target instanceof Player player && player.isCreative())) {
                                        reinforcement.setTarget(target);
                                    }
                                    reinforcement.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(reinforcement.blockPosition()), MobSpawnType.REINFORCEMENT, null);
                                    serverLevel.addFreshEntityWithPassengers(reinforcement);
                                    this.level().playSound(null, this.blockPosition(), OFSoundEvents.ZOMBIE_REINFORCEMENT.get(), this.getSoundSource(), 1.0F, SinewSoundUtils.randomizePitch(this));
                                    this.opposingForce$spawnReinforcementParticles(serverLevel, reinforcement, 20);
                                    break;
                                }
                            }
                        }
                    }
                }
                cir.setReturnValue(true);
            }
        }
    }
}
