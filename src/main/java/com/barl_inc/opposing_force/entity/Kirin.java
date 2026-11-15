package com.barl_inc.opposing_force.entity;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.ai.goal.KirinAttackGoal;
import com.barl_inc.opposing_force.entity.projectile.ElectricCharge;
import com.barl_inc.opposing_force.registry.OFItems;
import com.barl_inc.opposing_force.item.armor.StormLordArmorItem;
import com.barl_inc.opposing_force.registry.OFDamageTypes;
import com.barl_inc.opposing_force.registry.OFMobEffects;
import com.barl_inc.opposing_force.registry.OFParticleTypes;
import com.barl_inc.opposing_force.registry.OFSoundEvents;
import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.base.SummonableMount;
import com.platypushasnohat.sinew.entity.utils.BodyChain;
import com.platypushasnohat.sinew.entity.utils.BodyChainMob;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.Item;
import java.util.UUID;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class Kirin extends SummonableMount implements BodyChainMob, PlayerRideableJumping {

    public static final int IDLE_TWITCH_ONE_ANIMATION = 1;
    public static final int IDLE_TWITCH_TWO_ANIMATION = 2;
    public static final int IDLE_TWITCH_THREE_ANIMATION = 3;
    public static final int IDLE_SHAKE_ONE_ANIMATION = 4;
    public static final int IDLE_SHAKE_TWO_ANIMATION = 5;
    public static final int DEATH_ANIMATION = 6;
    public static final int TELEPORT_ANIMATION = 7;
    public static final int LIGHTNING_CALL_ANIMATION = 8;
    public static final int DASH_ANIMATION = 9;
    public static final int JUMP_START_ANIMATION = 10;
    public static final int JUMP_FALL_ANIMATION = 11;
    public static final int AIR_WALK_ANIMATION = 12;
    public static final int LIGHTNING_CALL_AIR_ANIMATION = 13;
    public static final int LIGHTNING_BALL_ONE_ANIMATION = 14;
    public static final int LIGHTNING_BALL_TWO_ANIMATION = 15;
    public static final int JUMP_MIDAIR_ANIMATION = 16;
    public static final int JUMP_MIDAIR_LEFT_ANIMATION = 17;
    public static final int JUMP_MIDAIR_RIGHT_ANIMATION = 18;
    public static final int SLAM_START_ANIMATION = 19;
    public static final int SLAM_ANIMATION = 20;
    public static final int SLAM_END_ANIMATION = 21;
    public static final int BLESSING_BECKON_ANIMATION = 22;
    public static final int BLESSING_RECEIVE_ANIMATION = 23;
    public static final int RUN_ANIMATION = 24;
    public static final int SIT_START_ANIMATION = 25;
    public static final int SIT_ANIMATION = 26;
    public static final int SIT_END_ANIMATION = 27;
    public static final byte DASH_SKIP_EVENT = 80;
    public static final byte SLAM_STEP_EVENT = 81;

    public static final float DEATH_GLOW_START = 1.3F;
    public static final float DEATH_LENGTH = 2.0F;

    private static final int[] IDLE_BLEND_DURATIONS = {20, 20, 25, 20, 20};
    private static final int IDLE_BLEND_COOLDOWN = 200;

    private static final float[] STEP_TIMES = {0.0F, 0.5F, 1.0F, 1.5F};
    private static final float[] RUN_STEP_TIMES = {0.0F, 0.6875F, 0.125F, 0.5625F};
    private static final float[] AIR_STEP_TIMES = {0.0F, 0.25F, 0.5F, 0.75F};
    private static final float[][] FOOT_POSITIONS = {{2.0F, -19.5F}, {2.0F, -0.5F}, {-2.0F, -19.5F}, {-2.0F, -0.5F}};
    private static final float[][] IDLE_FOOT_POSITIONS = {{2.5F, -4.4F}, {2.5F, 2.7F}, {-2.5F, -2.4F}, {-2.5F, 6.6F}};

    public final SmoothAnimationState idleTwitchOneAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleTwitchTwoAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleTwitchThreeAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleShakeOneAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState idleShakeTwoAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState deathAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState teleportAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState lightningCallAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState dashAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState jumpStartAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState jumpFallAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState airWalkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState lightningCallAirAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState lightningBallOneAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState lightningBallTwoAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState jumpMidairAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState jumpMidairLeftAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState jumpMidairRightAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slamStartAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slamAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState slamEndAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState blessingBeckonAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState blessingReceiveAnimationState = new SmoothAnimationState(1.0F);
    public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitStartAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sitEndAnimationState = new SmoothAnimationState();

    private final KirinRider rider = new KirinRider(this);
    private final BodyChain bodyChain = new BodyChain(0.5F, 0.0F, 0.0F, 0.0F, new float[]{0.5F, 0.25F, 0.3F, 0.35F}, new float[]{0.0F, 0.0F, 0.0F, 0.0F});

    private int idleBlendCooldown;
    private int deathTicks;
    @Nullable
    private Player closePlayer;
    private int closeTicks;
    @Nullable
    private DamageSource deathSource;
    private int offeringStage;
    private int tameStep;
    private boolean naturalSpawn;
    private int sitTicks;
    @Nullable
    private UUID tameTrialPlayer;
    private int offeringTicks;
    private Player offeringPlayer;
    private ItemStack offering = ItemStack.EMPTY;
    private ItemStack offeringReward = ItemStack.EMPTY;
    private float prevWalkTime;
    private float prevAirWalkTime;
    private int animationTicks;
    private int prevAnimationState;

    public Kirin(EntityType<? extends Kirin> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 30;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 275.0D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(SinewAttributes.PROJECTILE_RESISTANCE, 0.2D)
                .add(SinewAttributes.MAGIC_RESISTANCE, 0.2D)
                .add(SinewAttributes.ARMOR_PIERCING, 0.3D);
    }

    public static boolean checkKirinSpawnRules(EntityType<Kirin> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return (MobSpawnType.isSpawner(spawnType) || (level.getLevel().isThundering() && level.canSeeSky(pos))) && AnimatedMonster.checkSurfaceMonsterSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new KirinOfferingGoal(this));
        this.goalSelector.addGoal(0, new KirinSitGoal(this));
        this.goalSelector.addGoal(1, new KirinAttackGoal(this));
        this.goalSelector.addGoal(1, new KirinIdleBlendGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.2D, 10.0F, 4.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && Kirin.this.getCommand() == COMMAND_FOLLOW;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && Kirin.this.getCommand() == COMMAND_FOLLOW;
            }
        });
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(0, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new KirinProximityTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Husk.class, true));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.naturalSpawn = spawnType == MobSpawnType.NATURAL;
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("IdleBlendCooldown", this.idleBlendCooldown);
        compound.putBoolean("NaturalSpawn", this.naturalSpawn);
        if (this.tameTrialPlayer != null) {
            compound.putUUID("TameTrialPlayer", this.tameTrialPlayer);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.idleBlendCooldown = compound.getInt("IdleBlendCooldown");
        this.naturalSpawn = compound.getBoolean("NaturalSpawn");
        this.setNoGravity(false);
        if (compound.hasUUID("TameTrialPlayer")) {
            this.tameTrialPlayer = compound.getUUID("TameTrialPlayer");
        }
        if (this.getAnimationState() >= TELEPORT_ANIMATION) {
            this.setAnimationState(0);
        }
        if (this.isTame() && this.getCommand() == COMMAND_SIT) {
            this.setAnimationState(SIT_ANIMATION);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isAlive()) {
            this.tickSitting();
            if (this.isTame() && !this.isSummoned() && this.getHealth() < this.getMaxHealth() && this.tickCount % 100 == 0) {
                this.heal(2.0F);
            }
        }
        if (this.level().isClientSide) {
            if (this.getAnimationState() == this.prevAnimationState) {
                this.animationTicks++;
            } else {
                this.animationTicks = 0;
                this.prevAnimationState = this.getAnimationState();
            }
        }
        if (this.level().isClientSide && !this.isDeadOrDying()) {
            this.bodyChain.tick(this.yBodyRot, 0.0F, 0.0F);
            this.tickFootsteps();
            this.tickAirFootsteps();
            if (this.getAnimationState() == DASH_ANIMATION && this.animationTicks >= 15 && this.animationTicks <= 32) {
                OpposingForce.PROXY.spawnAfterImage(this, 0.0F, 10, 0x99D9EA, 0.25F);
                OpposingForce.PROXY.spawnAfterImage(this, 0.5F, 10, 0x99D9EA, 0.25F);
            }
            if (this.onGround() && this.walkAnimation.speed() < 0.15F && this.getAnimationState() != TELEPORT_ANIMATION && this.getAnimationState() != DASH_ANIMATION) {
                this.spawnHoverSmoke();
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(OFDamageTypes.ELECTRIFIED) || (source.getEntity() != null && this.hasPassenger(source.getEntity())) || super.isInvulnerableTo(source);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result;
        ItemStack food = player.getItemInHand(hand);
        float healing = this.getFoodHealing(food);
        if (this.isTame() && !this.isSummoned() && healing > 0.0F && this.getHealth() < this.getMaxHealth()) {
            if (!this.level().isClientSide) {
                this.heal(healing);
                food.consume(1, player);
                this.playSound(SoundEvents.HORSE_EAT, 1.0F, 0.8F + this.random.nextFloat() * 0.2F);
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.HEART, this.getX(), this.getY(0.8D), this.getZ(), 3, 0.5D, 0.3D, 0.5D, 0.0D);
            }
            result = InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.isTame() && !this.isSummoned() && this.getOwner() == player) {
            if (player.isShiftKeyDown()) {
                if (!this.level().isClientSide) {
                    this.setCommand(this.getCommand() >= COMMAND_WANDER ? COMMAND_SIT : this.getCommand() + 1);
                    player.displayClientMessage(Component.translatable("entity.sinew.all.command_" + this.getCommand(), this.getName()), true);
                }
            } else if (!this.level().isClientSide) {
                player.startRiding(this);
            }
            result = InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (hand == InteractionHand.MAIN_HAND && !this.isSummoned() && !this.isTame() && this.tameTrialPlayer == null && this.offeringStage == 0 && this.getTarget() == null && this.getAnimationState() < DEATH_ANIMATION && StormLordArmorItem.hasFullSet(player)) {
            if (!this.level().isClientSide) {
                this.offeringPlayer = player;
                this.offeringStage = 1;
            }
            result = InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            result = super.mobInteract(player, hand);
        }
        return result;
    }

    private float getFoodHealing(ItemStack stack) {
        float healing = 0.0F;
        if (stack.is(Items.APPLE)) {
            healing = 20.0F;
        } else if (stack.is(Items.CARROT)) {
            healing = 15.0F;
        } else if (stack.is(Items.WHEAT)) {
            healing = 10.0F;
        } else if (stack.is(Items.FERN) || stack.is(Items.LARGE_FERN) || stack.is(Items.SHORT_GRASS) || stack.is(Items.TALL_GRASS)) {
            healing = 5.0F;
        }
        return healing;
    }

    private void completeOffering() {
        List<Item> tameOfferings = List.of(Items.RECOVERY_COMPASS, OFItems.TESLA_INGOT.get(), Items.LIGHTNING_ROD, OFItems.IRIDESCENT_ESSENCE.get()); //my cryptic order that will be a future puzzle
        if (this.tameStep < tameOfferings.size() && this.offering.is(tameOfferings.get(this.tameStep))) {
            this.offering = ItemStack.EMPTY;
            this.tameStep++;
            if (this.tameStep >= tameOfferings.size()) {
                this.startTameTrial();
            } else {
                this.offeringStage = 1;
                this.offeringTicks = 0;
                this.setAnimationState(BLESSING_BECKON_ANIMATION);
            }
        } else {
            this.tameStep = 0;
            this.offeringReward = this.offering;
            this.offering = ItemStack.EMPTY;
            this.offeringStage = 0;
            this.setHealth(0.0F);
            this.die(this.damageSources().genericKill());
        }
    }

    private void startTameTrial() { //fyi it gets 45% armor pierce, strength 2, better attack tracking and super reduced cooldowns during this
        Player player = this.offeringPlayer;
        this.tameStep = 0;
        this.offeringStage = 0;
        this.setAnimationState(0);
        this.setHealth(this.getMaxHealth());
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 12000, 1), this); //good luck dude
        AttributeInstance piercing = this.getAttribute(SinewAttributes.ARMOR_PIERCING);
        if (piercing != null && !piercing.hasModifier(OpposingForce.location("tame_trial_armor_piercing"))) {
            piercing.addPermanentModifier(new AttributeModifier(OpposingForce.location("tame_trial_armor_piercing"), 0.45D, AttributeModifier.Operation.ADD_VALUE)); //GOOD LUCK DUDE
        }
        if (player != null) {
            this.tameTrialPlayer = player.getUUID();
            this.setTarget(player);
        }
    }

    private void finishTameTrial() {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
        if (bolt != null) {
            bolt.moveTo(this.getX(), this.getY(), this.getZ());
            bolt.setVisualOnly(true);
            this.level().addFreshEntity(bolt);
        }
        this.setTame(true, true);
        this.setOwnerUUID(this.tameTrialPlayer);
        this.tameTrialPlayer = null;
        this.setHealth(this.getMaxHealth());
        this.removeEffect(MobEffects.DAMAGE_BOOST);
        AttributeInstance piercing = this.getAttribute(SinewAttributes.ARMOR_PIERCING);
        if (piercing != null) {
            piercing.removeModifier(OpposingForce.location("tame_trial_armor_piercing"));
        }
        this.setTarget(null);
        this.setLastHurtByMob(null);
        this.getNavigation().stop();
        this.setCommand(COMMAND_SIT);
    }

    public boolean isInTameTrial() {
        return this.tameTrialPlayer != null;
    }

    public boolean isSitState() {
        return this.getAnimationState() >= SIT_START_ANIMATION && this.getAnimationState() <= SIT_END_ANIMATION;
    }

    private void tickSitting() {
        int state = this.getAnimationState();
        boolean wantsSit = this.isTame() && !this.isSummoned() && this.getCommand() == COMMAND_SIT && !this.isVehicle();
        this.sitTicks++;
        if (state == SIT_START_ANIMATION && this.sitTicks >= 20) {
            this.setAnimationState(SIT_ANIMATION);
        } else if (state == SIT_END_ANIMATION && this.sitTicks >= 20) {
            this.setAnimationState(0);
        } else if (state == SIT_ANIMATION && !wantsSit) {
            this.setAnimationState(SIT_END_ANIMATION);
            this.sitTicks = 0;
        } else if (state == 0 && wantsSit && this.onGround()) {
            this.setAnimationState(SIT_START_ANIMATION);
            this.sitTicks = 0;
        }
    }

    private void giveOfferingReward(ItemStack offering) { //my awesome secret trades
        List<Holder<MobEffect>> effects = List.of(MobEffects.DAMAGE_BOOST, MobEffects.MOVEMENT_SPEED, MobEffects.DAMAGE_RESISTANCE);
        if (offering.is(OFItems.TACHI)) {
            this.dropReward(new ItemStack(Items.PORKCHOP)); //temporary
        } else if (offering.is(OFItems.FUR)) {
            this.dropReward(new ItemStack(OFItems.ENERGIZED_TUFT.get(), 6 + this.random.nextInt(5)));
        } else if (offering.is(Items.SADDLE)) {
            this.dropReward(new ItemStack(OFItems.CLOUD_WHISTLE.get()));
        } else if (offering.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            if (this.offeringPlayer != null) {
                for (Holder<MobEffect> effect : effects) {
                    this.offeringPlayer.addEffect(new MobEffectInstance(effect, 72000, 0), this);
                }
            }
        } else if (this.random.nextBoolean()) { //everything else is either charges or random buff
            this.dropReward(new ItemStack(OFItems.ELECTRIC_CHARGE.get(), 8 + this.random.nextInt(13)));
        } else if (this.offeringPlayer != null) {
            this.offeringPlayer.addEffect(new MobEffectInstance(effects.get(this.random.nextInt(effects.size())), 72000, this.random.nextInt(2)), this);
        }
    }

    private void dropReward(ItemStack reward) {
        while (!reward.isEmpty()) {
            this.spawnAtLocation(reward.split(reward.getMaxStackSize()), 0.5F);
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        if (this.tameTrialPlayer != null && !this.level().isClientSide) {
            this.finishTameTrial();
        } else {
            super.die(damageSource);
            this.setAnimationState(DEATH_ANIMATION);
        }
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel level, DamageSource damageSource) {
        this.deathSource = damageSource;
    }

    @Override
    protected void tickDeath() {
        this.deathTicks++;
        this.hurtTime = 0;
        if (this.level().isClientSide && this.deathTicks == Mth.ceil(DEATH_GLOW_START * 20.0F)) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(OFParticleTypes.KIRIN_RING.get(), this.getX(), this.getY(), this.getZ(), this.yBodyRot, 1.4D, i);
            }
        }
        if (!this.level().isClientSide && this.deathTicks >= DEATH_LENGTH * 20.0F && !this.isRemoved()) {
            if (!this.offeringReward.isEmpty()) {
                this.giveOfferingReward(this.offeringReward);
            } else if (this.deathSource != null) {
                super.dropAllDeathLoot((ServerLevel) this.level(), this.deathSource);
            }
            if (!this.isSummoned()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, this.getSoundSource(), 190.0F, 0.8F + this.random.nextFloat() * 0.2F);
            }
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, this.getSoundSource(), 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onSummoned(Player player) {
        super.onSummoned(player);
        this.teleportBurst();
    }

    @Override
    public void expire() {
        if (this.isAlive()) {
            this.setHealth(0.0F);
            this.setNoGravity(false);
            this.setAnimationState(DEATH_ANIMATION);
            this.ejectPassengers();
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide && this.isTame() && this.isAlive()) {
            this.setAnimationState(0);
            this.setNoGravity(false);
        }
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return new Vec3(0.0D, 2.125D, 0.0D);
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        if (passenger instanceof LivingEntity living) {
            living.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        if (this.rider.isDashing()) {
            float yaw = this.rider.getDashYaw();
            this.setRot(yaw, 0.0F);
            this.yRotO = yaw;
            this.yBodyRot = yaw;
            this.yHeadRot = yaw;
        } else {
            super.tickRidden(player, travelVector);
        }
        this.rider.tick(player);
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        return !this.rider.blocksInput() && !this.isSitState() ? super.getRiddenInput(player, travelVector) : Vec3.ZERO;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        LivingEntity controller = super.getControllingPassenger();
        if (this.isTame() && this.getFirstPassenger() instanceof Player player) {
            controller = player;
        }
        return controller;
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return super.getRiddenSpeed(player) * this.rider.getSpeedFactor();
    }

    @Override
    protected float getFlyingSpeed() {
        return this.getControllingPassenger() instanceof Player ? this.getSpeed() * 0.2F : super.getFlyingSpeed();
    }

    @Override
    public void onPlayerJump(int power) {
        this.rider.onPlayerJump(power);
    }

    @Override
    public boolean canJump() {
        return this.isVehicle() && this.onGround() && this.rider.isIdle() && !this.isSitState();
    }

    @Override
    public void handleStartJump(int power) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public boolean acceptsRiderKey(int key) {
        return key > 0;
    }

    @Override
    public void onRiderKey(Player player, int key) {
        this.rider.onRiderKey(key);
    }

    public boolean isTeleportHidden(float partialTicks) {
        float seconds = (this.animationTicks + partialTicks) / 20.0F;
        return this.getAnimationState() == TELEPORT_ANIMATION && seconds >= 0.09F && seconds < 0.34F;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        if ((this.getAnimationState() == TELEPORT_ANIMATION || this.getAnimationState() == DASH_ANIMATION) && this.distanceToSqr(x, y, z) > 4.0D) {
            this.lerpSteps = 0;
            this.setPos(x, y, z);
            this.setYRot(yRot);
            this.setXRot(xRot);
            this.yBodyRot = yRot;
            this.yBodyRotO = yRot;
            this.yHeadRot = yRot;
            this.yHeadRotO = yRot;
            this.setOldPosAndRot();
        } else {
            super.lerpTo(x, y, z, yRot, xRot, steps);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == DASH_SKIP_EVENT) {
            int skipTicks = Math.max(0, Mth.floor((1500L - this.dashAnimationState.getAccumulatedTime()) / 50.0F));
            this.dashAnimationState.fastForward(skipTicks, 1.0F);
            this.animationTicks = 30;
        } else if (id == SLAM_STEP_EVENT) {
            for (float[] foot : FOOT_POSITIONS) {
                this.footstep(foot, 0.0D, 2.5F);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    public void lightningBall(LivingEntity target, int index) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, this.getYRot());
        Vec3 side = new Vec3(-forward.z, 0.0D, forward.x);
        Vec3 head = this.position().add(forward.scale(1.3D)).add(0.0D, 2.2D, 0.0D);
        Vec3 direction = target.getBoundingBox().getCenter().subtract(head).normalize();
        Vec3 offset = side.scale((index - 1) * 0.9D + (this.random.nextDouble() - 0.5D) * 0.3D).add(0.0D, (this.random.nextDouble() - 0.5D) * 1.0D, 0.0D);
        Vec3 spawn = head.add(offset);
        ElectricCharge charge = new ElectricCharge(this.level(), this);
        charge.setPos(spawn.x, spawn.y, spawn.z);
        charge.shoot(direction.x, direction.y, direction.z, 0.75F, 0.0F);
        if (this.isInTameTrial()) {
            charge.setHomingTarget(target);
        }
        this.level().addFreshEntity(charge);
        this.level().playSound(null, spawn.x, spawn.y, spawn.z, OFSoundEvents.ELECTRIC_CHARGE_ZAP.get(), SoundSource.HOSTILE, 1.5F, 0.8F + this.random.nextFloat() * 0.3F);
    }

    public void slamImpact() {
        this.slamImpact(this::isValidVictim, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0F);
    }

    public void slamImpact(Predicate<LivingEntity> victims, float damage) {
        ServerLevel level = (ServerLevel) this.level();
        this.electricBurst(this.getX(), this.getY(), this.getZ(), 20, 4.0D, 60, 6.0F);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.HOSTILE, 3.0F, 0.6F + this.random.nextFloat() * 0.2F);
        AABB hitBox = this.getBoundingBox().inflate(3.5D, 1.0D, 3.5D);
        for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, hitBox, victims)) {
            double distance = living.position().subtract(this.position()).horizontalDistance();
            if (distance <= 4.0D) {
                if (living instanceof Player player && player.isBlocking()) {
                    player.disableShield();
                }
                if (living.hurt(OFDamageTypes.causeElectricDamage(this.level().registryAccess(), this, this), damage)) {
                    Vec3 push = living.position().subtract(this.position()).multiply(1.0D, 0.0D, 1.0D);
                    if (push.lengthSqr() < 1.0E-4D) {
                        push = new Vec3(1.0D, 0.0D, 0.0D);
                    }
                    double strength = 1.5D * (1.0D - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    push = push.normalize().scale(strength);
                    living.setDeltaMovement(push.x, 0.5D, push.z);
                    living.hurtMarked = true;
                    living.addEffect(new MobEffectInstance(OFMobEffects.ELECTRIFIED, 900, 0), this);
                }
            }
        }
    }

    public void dashChargeParticles() { //when its chargng up, doesnt play during dash
        ((ServerLevel) this.level()).sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), this.getX(), this.getY(0.5D), this.getZ(), 8, this.getBbWidth() * 0.5D, this.getBbHeight() * 0.35D, this.getBbWidth() * 0.5D, 0.0D);
    }

    public void dashBlinkSound(Vec3 from) {
        this.level().playSound(null, from.x, from.y, from.z, OFSoundEvents.ELECTRIC_CHARGE_ZAP.get(), SoundSource.HOSTILE, 1.5F, 1.2F + this.random.nextFloat() * 0.3F);
    }

    public void dashSkidParticles(int skidTicks) {//fits to dash end animation
        float seconds = skidTicks / 20.0F;
        double behind = 0.0D;
        if (seconds < 0.125F) {
            behind = Mth.lerp(seconds / 0.125F, 2.0D, 0.5D);
        } else if (seconds < 0.5F) {
            behind = Mth.lerp((seconds - 0.125F) / 0.375F, 0.5D, 0.0D);
        }
        Vec3 feet = this.position().subtract(Vec3.directionFromRotation(0.0F, this.getYRot()).scale(behind));
        ServerLevel level = (ServerLevel) this.level();
        level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), feet.x, feet.y + 0.15D, feet.z, 4, 0.7D, 0.05D, 0.7D, 0.0D);
        if (skidTicks % 2 == 0) {
            float angle = this.random.nextFloat() * Mth.TWO_PI;
            double length = 0.8D + this.random.nextDouble() * 0.7D;
            level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), feet.x, feet.y + 0.1D, feet.z, 0, Mth.cos(angle) * length, 0.05D, Mth.sin(angle) * length, 1.0D);
        }
    }

    public void dashHit(LivingEntity player, Vec3 direction) {
        boolean blocking = player.isBlocking();
        if (player.hurt(OFDamageTypes.causeElectricDamage(this.level().registryAccess(), this, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
            double strength = 2.5D * (1.0D - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            player.setDeltaMovement(direction.x * strength, 0.35D, direction.z * strength);
            player.hurtMarked = true;
            player.addEffect(new MobEffectInstance(OFMobEffects.ELECTRIFIED, 600, 0), this);
        }
        if (blocking && player instanceof Player blocker) {
            blocker.disableShield();
        }
    }

    public void teleportBurst() {
        this.electricBurst(this.getX(), this.getY(), this.getZ(), 8, 1.5D, 12, 3.0F);
    }

    public void electricBurst(double x, double y, double z, int minBolts, double minLength, int arcs, float volume) { //the floor burst thing
        ServerLevel level = (ServerLevel) this.level();
        int bolts = minBolts + this.random.nextInt(4);
        for (int i = 0; i < bolts; i++) {
            float angle = (i + this.random.nextFloat() * 0.6F) / bolts * Mth.TWO_PI;
            double length = minLength + this.random.nextDouble() * minLength;
            double reachX = Mth.cos(angle) * length;
            double reachY = 0.1D + this.random.nextDouble() * 0.3D;
            double reachZ = Mth.sin(angle) * length;
            double startX = x + Mth.cos(angle) * 0.4D;
            double startY = y + 0.1D;
            double startZ = z + Mth.sin(angle) * 0.4D;
            level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX, startY, startZ, 0, reachX, reachY, reachZ, 1.0D);
            double split = 0.3D + this.random.nextDouble() * 0.4D;
            float branchAngle = angle + (this.random.nextBoolean() ? 0.6F : -0.6F);
            double branchLength = length * (1.0D - split) * 0.7D;
            level.sendParticles(OFParticleTypes.ELECTRIC_BOLT.get(), startX + reachX * split, startY + reachY * split, startZ + reachZ * split, 0, Mth.cos(branchAngle) * branchLength, 0.05D, Mth.sin(branchAngle) * branchLength, 1.0D);
        }
        level.sendParticles(OFParticleTypes.ELECTRIC_ARC.get(), x, y + 0.3D, z, arcs, minLength * 0.4D, 0.2D, minLength * 0.4D, 0.0D);
        this.level().playSound(null, x, y, z, OFSoundEvents.ELECTRIC_CHARGE_DISSIPATE.get(), SoundSource.HOSTILE, volume, (1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F) * 0.7F);
    }

    public void lightningCall(Vec3 strike) {
        ((ServerLevel) this.level()).sendParticles(OFParticleTypes.LIGHTNING_CALL.get(), strike.x, strike.y, strike.z, 0, 0.0D, 40.0D, 0.0D, 1.0D);
    }

    public void lightningCallImpact(Vec3 strike) {
        this.electricBurst(strike.x, strike.y, strike.z, 16, 3.0D, 30, 5.0F);
        this.level().playSound(null, strike.x, strike.y, strike.z, OFSoundEvents.ELECTRIC_CHARGE_ZAP.get(), SoundSource.HOSTILE, 3.0F, 0.6F + this.random.nextFloat() * 0.2F);
        AABB hitBox = new AABB(strike.x - 0.8D, strike.y - 0.5D, strike.z - 0.8D, strike.x + 0.8D, strike.y + 2.5D, strike.z + 0.8D);
        for (LivingEntity player : this.level().getEntitiesOfClass(LivingEntity.class, hitBox, this::isValidVictim)) {
            if (player.hurt(OFDamageTypes.causeLightningCallDamage(this.level().registryAccess(), this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                player.addEffect(new MobEffectInstance(OFMobEffects.ELECTRIFIED, 900, 0), this);
            }
        }
    }

    public List<LivingEntity> getLightningCallTargets() {
        return this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16.0D), living -> this.isValidVictim(living) && living.hasEffect(OFMobEffects.ELECTRIFIED));
    }

    private boolean canHit(LivingEntity living) {
        return living != this && living.isAlive() && !living.isSpectator() && !this.isAlliedTo(living) && !this.hasPassenger(living);
    }

    public boolean isValidVictim(LivingEntity living) {
        boolean creative = living instanceof Player player && player.isCreative();
        return this.canHit(living) && !creative && (living instanceof Player || living == this.getTarget());
    }

    public boolean isValidRideVictim(LivingEntity living) {
        return this.canHit(living) && living != this.getOwner();
    }

    public static double slamRise(double air) {
        return 1.583D * air - 0.066D * air * air;
    }

    public float getDeathSeconds(float partialTicks) {
        return (this.deathTicks + partialTicks) / 20.0F;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        Player nearest = this.level().getNearestPlayer(TargetingConditions.forCombat().range(6.0D).selector(target -> !this.isIgnoredPlayer((Player) target)), this);
        if (nearest != null && nearest == this.closePlayer) {
            this.closeTicks++;
        } else {
            this.closePlayer = nearest;
            this.closeTicks = 0;
        }
        if (this.idleBlendCooldown > 0) {
            this.idleBlendCooldown--;
        }
        if (!this.level().isThundering() && !this.isTame() && !this.isSummoned() && this.getTarget() == null && this.offeringStage == 0 && this.tameTrialPlayer == null) {
            this.expire();
        }
    }

    private void tickFootsteps() {
        boolean running = this.getAnimationState() == RUN_ANIMATION;
        float[] stepTimes = running ? RUN_STEP_TIMES : STEP_TIMES;
        float length = running ? 0.75F : 2.0F;
        float walkTime = this.walkAnimation.position() * 0.05F * (running ? 2.0F : 5.0F);
        if (this.onGround() && this.walkAnimation.speed() > 0.15F && this.getAnimationState() != DASH_ANIMATION) {
            for (int i = 0; i < stepTimes.length; i++) {
                if (Mth.floor((walkTime - stepTimes[i]) / length) > Mth.floor((this.prevWalkTime - stepTimes[i]) / length)) {
                    this.footstep(FOOT_POSITIONS[i], 0.0D, 1.0F);
                }
            }
        }
        this.prevWalkTime = walkTime;
    }

    private void spawnHoverSmoke() {
        float[] foot = IDLE_FOOT_POSITIONS[this.random.nextInt(IDLE_FOOT_POSITIONS.length)];
        double x = this.footX(foot) + (this.random.nextDouble() - 0.5D) * 0.4D;
        double z = this.footZ(foot) + (this.random.nextDouble() - 0.5D) * 0.4D;
        this.level().addParticle(OFParticleTypes.KIRIN_SMOKE.get(), x, this.getY() + 0.05D, z, (this.random.nextDouble() - 0.5D) * 0.02D, 0.01D, (this.random.nextDouble() - 0.5D) * 0.02D);
    }

    private double footX(float[] foot) {
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        return this.getX() + foot[0] / 16.0D * Mth.cos(yaw) + foot[1] / 16.0D * Mth.sin(yaw);
    }

    private double footZ(float[] foot) {
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        return this.getZ() + foot[0] / 16.0D * Mth.sin(yaw) - foot[1] / 16.0D * Mth.cos(yaw);
    }

    private void tickAirFootsteps() {
        float airWalkTime = this.airWalkAnimationState.getAccumulatedTime() / 1000.0F;
        if (this.isAirWalking()) {
            for (int i = 0; i < AIR_STEP_TIMES.length; i++) {
                if (Mth.floor(airWalkTime - AIR_STEP_TIMES[i]) > Mth.floor(this.prevAirWalkTime - AIR_STEP_TIMES[i])) {
                    float forward = -FOOT_POSITIONS[i][1] / 16.0F;
                    this.footstep(new float[]{FOOT_POSITIONS[i][0], -forward * 0.866F * 16.0F}, -forward * 0.5F, 1.0F);
                }
            }
            this.prevAirWalkTime = airWalkTime;
        } else {
            this.prevAirWalkTime = 0.0F;
        }
    }

    public boolean isAirWalking() {
        return this.getAnimationState() >= AIR_WALK_ANIMATION && this.getAnimationState() <= LIGHTNING_BALL_TWO_ANIMATION;
    }

    public boolean isAirborneState() {
        return this.getAnimationState() >= JUMP_START_ANIMATION && this.getAnimationState() <= SLAM_END_ANIMATION;
    }

    private void footstep(float[] foot, double yOffset, float scale) {
        double x = this.footX(foot);
        double y = this.getY() + yOffset;
        double z = this.footZ(foot);
        this.level().addParticle(OFParticleTypes.KIRIN_STEP.get(), x, y + 0.02D, z, this.yBodyRot, scale, 0.0D);
        this.level().playLocalSound(x, y, z, SoundEvents.AMETHYST_BLOCK_STEP, this.getSoundSource(), 0.5F * scale, (0.85F + this.random.nextFloat() * 0.3F) / Mth.sqrt(scale), false);
    }

    @Override
    public BodyChain getBodyChain() {
        return this.bodyChain;
    }

    @Override
    public float getRenderYaw(float partialTicks) {
        return this.bodyChain.getRenderYaw(partialTicks);
    }

    @Override
    public float getSegmentYawOffset(int index, float partialTicks) {
        return this.bodyChain.getSegmentYawOffset(index, partialTicks);
    }

    @Override
    public float getSegmentPitchOffset(int index, float partialTicks) {
        return this.bodyChain.getSegmentPitchOffset(index, partialTicks, 0.0F);
    }

    public float getDarkness() {
        float daylight = Mth.clamp(0.5F + 2.0F * Mth.cos(this.level().getTimeOfDay(1.0F) * Mth.TWO_PI), 0.0F, 1.0F);
        float weather = (1.0F - this.level().getRainLevel(1.0F) * 5.0F / 16.0F) * (1.0F - this.level().getThunderLevel(1.0F) * 5.0F / 16.0F);
        return 1.0F - daylight * weather;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        //empty because the step stuff is custom
    }

    @Override
    public void setupAnimationStates() { //holy bloat
        this.idleAnimationState.animateWhen(this.getAnimationState() != DEATH_ANIMATION && !this.isAirborneState() && !this.isSitState(), this.tickCount);
        this.walkAnimationState.animateWhen(this.getAnimationState() != DEATH_ANIMATION && this.getAnimationState() != RUN_ANIMATION && !this.isAirborneState() && !this.isSitState(), this.tickCount);
        this.sitStartAnimationState.animateWhen(this.getAnimationState() == SIT_START_ANIMATION, this.tickCount);
        this.sitAnimationState.animateWhen(this.getAnimationState() == SIT_ANIMATION, this.tickCount);
        this.sitEndAnimationState.animateWhen(this.getAnimationState() == SIT_END_ANIMATION, this.tickCount);
        this.runAnimationState.animateWhen(this.getAnimationState() == RUN_ANIMATION, this.tickCount);
        this.idleTwitchOneAnimationState.animateWhen(this.getAnimationState() == IDLE_TWITCH_ONE_ANIMATION, this.tickCount);
        this.idleTwitchTwoAnimationState.animateWhen(this.getAnimationState() == IDLE_TWITCH_TWO_ANIMATION, this.tickCount);
        this.idleTwitchThreeAnimationState.animateWhen(this.getAnimationState() == IDLE_TWITCH_THREE_ANIMATION, this.tickCount);
        this.idleShakeOneAnimationState.animateWhen(this.getAnimationState() == IDLE_SHAKE_ONE_ANIMATION, this.tickCount);
        this.idleShakeTwoAnimationState.animateWhen(this.getAnimationState() == IDLE_SHAKE_TWO_ANIMATION, this.tickCount);
        this.deathAnimationState.animateWhen(this.getAnimationState() == DEATH_ANIMATION, this.tickCount);
        this.teleportAnimationState.animateWhen(this.getAnimationState() == TELEPORT_ANIMATION, this.tickCount);
        this.lightningCallAnimationState.animateWhen(this.getAnimationState() == LIGHTNING_CALL_ANIMATION, this.tickCount);
        this.dashAnimationState.animateWhen(this.getAnimationState() == DASH_ANIMATION, this.tickCount);
        this.jumpStartAnimationState.animateWhen(this.getAnimationState() == JUMP_START_ANIMATION, this.tickCount);
        this.jumpFallAnimationState.animateWhen(this.getAnimationState() == JUMP_FALL_ANIMATION, this.tickCount);
        this.airWalkAnimationState.animateWhen(this.isAirWalking(), this.tickCount);
        this.lightningCallAirAnimationState.animateWhen(this.getAnimationState() == LIGHTNING_CALL_AIR_ANIMATION, this.tickCount);
        this.lightningBallOneAnimationState.animateWhen(this.getAnimationState() == LIGHTNING_BALL_ONE_ANIMATION, this.tickCount);
        this.lightningBallTwoAnimationState.animateWhen(this.getAnimationState() == LIGHTNING_BALL_TWO_ANIMATION, this.tickCount);
        this.jumpMidairAnimationState.animateWhen(this.getAnimationState() == JUMP_MIDAIR_ANIMATION, this.tickCount);
        this.jumpMidairLeftAnimationState.animateWhen(this.getAnimationState() == JUMP_MIDAIR_LEFT_ANIMATION, this.tickCount);
        this.jumpMidairRightAnimationState.animateWhen(this.getAnimationState() == JUMP_MIDAIR_RIGHT_ANIMATION, this.tickCount);
        this.slamStartAnimationState.animateWhen(this.getAnimationState() == SLAM_START_ANIMATION, this.tickCount);
        this.slamAnimationState.animateWhen(this.getAnimationState() == SLAM_ANIMATION, this.tickCount);
        this.slamEndAnimationState.animateWhen(this.getAnimationState() == SLAM_END_ANIMATION, this.tickCount);
        this.blessingBeckonAnimationState.animateWhen(this.getAnimationState() == BLESSING_BECKON_ANIMATION, this.tickCount);
        this.blessingReceiveAnimationState.animateWhen(this.getAnimationState() == BLESSING_RECEIVE_ANIMATION, this.tickCount);
    }

    private boolean isIgnoredPlayer(Player player) {
        return StormLordArmorItem.hasFullSet(player);
    }

    private static class KirinProximityTargetGoal extends TargetGoal {

        private final Kirin kirin;

        public KirinProximityTargetGoal(Kirin kirin) {
            super(kirin, false);
            this.kirin = kirin;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            return !this.kirin.isSummoned() && !this.kirin.isTame() && this.kirin.getTarget() == null && this.kirin.closePlayer != null && this.kirin.closeTicks >= 300;
        }

        @Override
        public void start() {
            this.kirin.setTarget(this.kirin.closePlayer);
            this.kirin.closeTicks = 0;
            super.start();
        }
    }

    private static class KirinIdleBlendGoal extends Goal {

        private final Kirin kirin;
        private int duration;
        private int timer;

        public KirinIdleBlendGoal(Kirin kirin) {
            this.kirin = kirin;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.kirin.isVehicle() && this.kirin.getAnimationState() == 0 && this.kirin.idleBlendCooldown <= 0 && this.kirin.getTarget() == null && this.kirin.getNavigation().isDone() && this.kirin.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4D && this.kirin.getRandom().nextInt(reducedTickDelay(100)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < this.duration && this.kirin.getTarget() == null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            int idlePick = this.kirin.getRandom().nextInt(IDLE_BLEND_DURATIONS.length);
            this.timer = 0;
            this.duration = IDLE_BLEND_DURATIONS[idlePick];
            this.kirin.getNavigation().stop();
            this.kirin.setAnimationState(IDLE_TWITCH_ONE_ANIMATION + idlePick);
        }

        @Override
        public void stop() {
            this.kirin.setAnimationState(0);
            this.kirin.idleBlendCooldown = IDLE_BLEND_COOLDOWN;
        }

        @Override
        public void tick() {
            this.timer++;
        }
    }

    private static class KirinSitGoal extends Goal {

        private final Kirin kirin;

        public KirinSitGoal(Kirin kirin) {
            this.kirin = kirin;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return this.kirin.isSitState() && !this.kirin.isVehicle();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.kirin.getNavigation().stop();
            this.kirin.setDeltaMovement(0.0D, this.kirin.getDeltaMovement().y, 0.0D);
        }
    }

    private static class KirinOfferingGoal extends Goal {

        private final Kirin kirin;

        public KirinOfferingGoal(Kirin kirin) {
            this.kirin = kirin;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.kirin.offeringStage > 0;
        }

        @Override
        public boolean canContinueToUse() {
            Player player = this.kirin.offeringPlayer;
            return this.kirin.offeringStage > 0 && this.kirin.getTarget() == null && player != null && player.isAlive() && player.distanceToSqr(this.kirin) < 256.0D;
        }

        @Override
        public void start() {
            this.kirin.getNavigation().stop();
            this.kirin.offeringTicks = 0;
            this.kirin.setAnimationState(BLESSING_BECKON_ANIMATION);
        }

        @Override
        public void stop() {
            if (this.kirin.offeringStage > 0) {
                if (!this.kirin.offering.isEmpty()) {
                    this.kirin.spawnAtLocation(this.kirin.offering, 0.5F);
                    this.kirin.offering = ItemStack.EMPTY;
                }
                this.kirin.offeringStage = 0;
                this.kirin.tameStep = 0;
                this.kirin.offeringPlayer = null;
                this.kirin.setAnimationState(0);
            }
        }

        @Override
        public void tick() {
            Player player = this.kirin.offeringPlayer;
            this.kirin.getNavigation().stop();
            this.kirin.getLookControl().setLookAt(player, 30.0F, 30.0F);
            this.kirin.offeringTicks++;
            if (this.kirin.offeringStage == 1 && this.kirin.offeringTicks >= 40) {
                this.kirin.setAnimationState(0);
                this.kirin.offeringStage = 2;
                this.kirin.offeringTicks = 0;
            } else if (this.kirin.offeringStage == 2) {
                List<ItemEntity> items = this.kirin.level().getEntitiesOfClass(ItemEntity.class, this.kirin.getBoundingBox().inflate(4.0D), item -> item.isAlive() && item.getOwner() == player);
                if (!items.isEmpty()) {
                    ItemEntity item = items.get(0);
                    ItemStack stack = item.getItem().copy();
                    this.kirin.offering = stack.split(1);
                    item.setItem(stack);
                    if (stack.isEmpty()) {
                        item.discard();
                    }
                    this.kirin.setAnimationState(BLESSING_RECEIVE_ANIMATION);
                    this.kirin.offeringStage = 3;
                    this.kirin.offeringTicks = 0;
                } else if (this.kirin.offeringTicks >= 600) {
                    this.stop();
                }
            } else if (this.kirin.offeringStage == 3 && this.kirin.offeringTicks >= 80) {
                this.kirin.completeOffering();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}