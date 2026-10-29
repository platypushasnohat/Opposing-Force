package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.*;
import com.barl_inc.opposing_force.entity.misc.*;
import com.barl_inc.opposing_force.entity.projectile.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OFEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, OpposingForce.MOD_ID);

    public static List<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> ENTITY_TRANSLATIONS = new ArrayList<>();

    //mobs
    public static final DeferredHolder<EntityType<?>, EntityType<Dicer>> DICER = registerEntity("dicer", Dicer::new, MobCategory.MONSTER, builder -> builder.sized(0.9F, 2.8F).eyeHeight(2.4F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Bewilder>> BEWILDER = registerEntity("bewilder", Bewilder::new, MobCategory.MONSTER, builder -> builder.sized(1.5F, 1.9F).eyeHeight(1.25F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Gusher>> GUSHER = registerEntity("gusher", Gusher::new, MobCategory.MONSTER, builder -> builder.sized(1.8F, 2.55F).eyeHeight(2.15F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Gnat>> GNAT = registerEntity("gnat", Gnat::new, MobCategory.MONSTER, builder -> builder.sized(0.65F, 0.65F).eyeHeight(0.45F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Scorcher>> SCORCHER = registerEntity("scorcher", Scorcher::new, MobCategory.MONSTER, builder -> builder.sized(0.95F, 0.95F).eyeHeight(0.7F).fireImmune().clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Furball>> FURBALL = registerEntity("furball", Furball::new, MobCategory.MONSTER, builder -> builder.sized(0.85F, 0.925F).eyeHeight(0.6F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Terror>> TERROR = registerEntity("terror", Terror::new, OFMobCategories.AQUIFER_MONSTER, builder -> builder.sized(1.25F, 1.25F).eyeHeight(0.75F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Tarantula>> TARANTULA = registerEntity("tarantula", Tarantula::new, MobCategory.MONSTER, builder -> builder.sized(3.4F, 1.9F).eyeHeight(1.5F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Mushy>> MUSHY = registerEntity("mushy", Mushy::new, MobCategory.MONSTER, builder -> builder.sized(0.69F, 1.05F).eyeHeight(0.31F).clientTrackingRange(8).setUpdateInterval(1));
    public static final DeferredHolder<EntityType<?>, EntityType<Motif>> MOTIF = registerEntity("motif", Motif::new, MobCategory.MONSTER, builder -> builder.sized(1.5F, 2.8F).eyeHeight(2.5F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Fillet>> FILLET = registerEntity("fillet", Fillet::new, OFMobCategories.AQUIFER_MONSTER, builder -> builder.sized(0.5F, 0.5F).eyeHeight(0.25F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Ghost>> GHOST = registerEntity("ghost", Ghost::new, MobCategory.MONSTER, builder -> builder.sized(0.75F, 1.85F).eyeHeight(1.55F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Roamer>> ROAMER = registerEntity("roamer", Roamer::new, MobCategory.MONSTER, builder -> builder.sized(0.7F, 1.75F).eyeHeight(1.45F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Haunt>> HAUNT = registerEntity("haunt", Haunt::new, MobCategory.MONSTER, builder -> builder.sized(0.8F, 1.8F).eyeHeight(1.4F).clientTrackingRange(8).updateInterval(1));
    public static final DeferredHolder<EntityType<?>, EntityType<Grasper>> GRASPER = registerEntity("grasper", Grasper::new, MobCategory.MONSTER, builder -> builder.sized(0.9F, 0.6F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Parasoul>> PARASOUL = registerEntity("parasoul", Parasoul::new, MobCategory.MONSTER, builder -> builder.sized(0.75F, 3.1F).eyeHeight(2.4F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Grell>> GRELL = registerEntity("grell", Grell::new, MobCategory.MONSTER, builder -> builder.sized(1.5F, 2.0F).eyeHeight(1.4F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<Pipsqueak>> PIPSQUEAK = registerEntity("pipsqueak", Pipsqueak::new, MobCategory.MONSTER, builder -> builder.sized(0.9F, 1.0F).eyeHeight(0.4F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Brick>> BRICK = registerEntity("brick", Brick::new, MobCategory.MONSTER, builder -> builder.sized(2.3F, 2.5F).clientTrackingRange(8));

    //other
    public static final DeferredHolder<EntityType<?>, EntityType<DicerLaser>> DICER_LASER = registerEntity("dicer_laser", DicerLaser::new, MobCategory.MISC, builder -> builder.sized(0.1F, 0.1F).setUpdateInterval(1).clientTrackingRange(4).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<AcidCharge>> ACID_CHARGE = registerEntity("acid_charge", AcidCharge::new, MobCategory.MISC, builder -> builder.sized(0.4F, 0.4F).setUpdateInterval(10).clientTrackingRange(4));
    public static final DeferredHolder<EntityType<?>, EntityType<AcidCloud>> ACID_CLOUD = registerEntity("acid_cloud", AcidCloud::new, MobCategory.MISC, builder -> builder.sized(4.0F, 0.5F).fireImmune().clientTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE));
    public static final DeferredHolder<EntityType<?>, EntityType<LaserBolt>> LASER_BOLT = registerEntity("laser_bolt", LaserBolt::new, MobCategory.MISC, builder -> builder.sized(0.3F, 0.3F).setUpdateInterval(20).fireImmune().clientTrackingRange(4));
    public static final DeferredHolder<EntityType<?>, EntityType<LaserBlade>> LASER_BLADE = registerEntity("laser_blade", LaserBlade::new, MobCategory.MISC, builder -> builder.sized(2.25F, 0.95F).fireImmune().clientTrackingRange(4));
    public static final DeferredHolder<EntityType<?>, EntityType<Mudball>> MUDBALL = registerEntity("mudball", Mudball::new, MobCategory.MISC, builder -> builder.sized(0.4F, 0.4F).setUpdateInterval(1).clientTrackingRange(4).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<MudSplatter>> MUD_SPLATTER = registerEntity("mud_splatter", MudSplatter::new, MobCategory.MISC, builder -> builder.sized(1.3F, 0.15F).fireImmune().clientTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE));
    public static final DeferredHolder<EntityType<?>, EntityType<Bullet>> BULLET = registerEntity("bullet", Bullet::new, MobCategory.MISC, builder -> builder.sized(0.25F, 0.25F).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(4));
    public static final DeferredHolder<EntityType<?>, EntityType<Sawblade>> SAWBLADE = registerEntity("sawblade", Sawblade::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).setUpdateInterval(20).clientTrackingRange(4));
    public static final DeferredHolder<EntityType<?>, EntityType<RingProjectile>> RING_PROJECTILE = registerEntity("ring_projectile", RingProjectile::new, MobCategory.MISC, builder -> builder.sized(0.25F, 0.25F).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(4).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<Soul>> SOUL = registerEntity("soul", Soul::new, MobCategory.MISC, builder -> builder.sized(0.3F, 0.3F).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(4).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<Shockwave>> SHOCKWAVE = registerEntity("shockwave", Shockwave::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).fireImmune().clientTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE));
    public static final DeferredHolder<EntityType<?>, EntityType<GrellClone>> GRELL_CLONE = registerEntityNoLang("grell_clone", GrellClone::new, MobCategory.MONSTER, builder -> builder.sized(1.5F, 2.0F).eyeHeight(1.4F).clientTrackingRange(10).noSave());
    public static final DeferredHolder<EntityType<?>, EntityType<Soundwave>> SOUNDWAVE = registerEntity("soundwave", Soundwave::new, MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(4).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<ManiaBrain>> MANIA_BRAIN = registerEntity("mania_brain", ManiaBrain::new, MobCategory.MISC, builder -> builder.sized(0.9F, 0.9F).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(4).fireImmune());

    public static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerEntity(String name, EntityType.EntityFactory<E> factory, MobCategory entityClassification, Consumer<EntityType.Builder<E>> builderConsumer) {
        DeferredHolder<EntityType<?>, EntityType<E>> entity = registerEntityNoLang(name, factory, entityClassification, builderConsumer);
        ENTITY_TRANSLATIONS.add(entity);
        return entity;
    }

    public static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerEntityNoLang(String name, EntityType.EntityFactory<E> factory, MobCategory entityClassification, Consumer<EntityType.Builder<E>> builderConsumer) {
        return ENTITY_TYPE.register(name, () -> {
            var builder = EntityType.Builder.of(factory, entityClassification);
            builderConsumer.accept(builder);
            return builder.build(OpposingForce.MOD_ID + ":" + name);
        });
    }
}
