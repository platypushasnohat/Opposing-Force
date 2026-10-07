package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.*;
import com.barl_inc.opposing_force.entity.misc.AcidCloud;
import com.barl_inc.opposing_force.entity.misc.DicerLaser;
import com.barl_inc.opposing_force.entity.projectile.AcidCharge;
import com.barl_inc.opposing_force.entity.projectile.LaserBlade;
import com.barl_inc.opposing_force.entity.projectile.LaserBolt;
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

    public static final DeferredHolder<EntityType<?>, EntityType<Dicer>> DICER = registerEntity("dicer", Dicer::new, MobCategory.MONSTER, builder -> builder.sized(0.9F, 2.8F).eyeHeight(2.4F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Bewilder>> BEWILDER = registerEntity("bewilder", Bewilder::new, MobCategory.MONSTER, builder -> builder.sized(1.5F, 1.9F).eyeHeight(1.25F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Gusher>> GUSHER = registerEntity("gusher", Gusher::new, MobCategory.MONSTER, builder -> builder.sized(1.8F, 2.55F).eyeHeight(2.15F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Gnat>> GNAT = registerEntity("gnat", Gnat::new, MobCategory.MONSTER, builder -> builder.sized(0.65F, 0.65F).eyeHeight(0.45F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Scorcher>> SCORCHER = registerEntity("scorcher", Scorcher::new, MobCategory.MONSTER, builder -> builder.sized(0.95F, 0.95F).eyeHeight(0.7F).fireImmune().clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Furball>> FURBALL = registerEntity("furball", Furball::new, MobCategory.MONSTER, builder -> builder.sized(0.85F, 0.925F).eyeHeight(0.6F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Terror>> TERROR = registerEntity("terror", Terror::new, MobCategory.MONSTER, builder -> builder.sized(1.25F, 1.25F).eyeHeight(0.75F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Tarantula>> TARANTULA = registerEntity("tarantula", Tarantula::new, MobCategory.MONSTER, builder -> builder.sized(3.4F, 1.9F).eyeHeight(1.5F).clientTrackingRange(8));
    public static final DeferredHolder<EntityType<?>, EntityType<Mushy>> MUSHY = registerEntity("mushy", Mushy::new, MobCategory.MONSTER, builder -> builder.sized(0.69F, 1.05F).eyeHeight(0.31F).clientTrackingRange(8).setUpdateInterval(1));

    public static final DeferredHolder<EntityType<?>, EntityType<DicerLaser>> DICER_LASER = registerEntity("dicer_laser", DicerLaser::new, MobCategory.MISC, builder -> builder.sized(0.1F, 0.1F).setUpdateInterval(1).clientTrackingRange(4).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<AcidCharge>> ACID_CHARGE = registerEntity("acid_charge", AcidCharge::new, MobCategory.MISC, builder -> builder.sized(0.4F, 0.4F).setUpdateInterval(10).clientTrackingRange(4));
    public static final DeferredHolder<EntityType<?>, EntityType<AcidCloud>> ACID_CLOUD = registerEntity("acid_cloud", AcidCloud::new, MobCategory.MISC, builder -> builder.sized(4.0F, 0.5F).fireImmune().clientTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE));
    public static final DeferredHolder<EntityType<?>, EntityType<LaserBolt>> LASER_BOLT = registerEntity("laser_bolt", LaserBolt::new, MobCategory.MISC, builder -> builder.sized(0.3F, 0.3F).setShouldReceiveVelocityUpdates(true).fireImmune().clientTrackingRange(4));
    public static final DeferredHolder<EntityType<?>, EntityType<LaserBlade>> LASER_BLADE = registerEntity("laser_blade", LaserBlade::new, MobCategory.MISC, builder -> builder.sized(2.25F, 0.95F).fireImmune().clientTrackingRange(4));

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
