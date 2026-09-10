package com.barl_inc.opposing_force.registry;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.Dicer;
import com.barl_inc.opposing_force.entity.misc.DicerLaser;
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
    public static final DeferredHolder<EntityType<?>, EntityType<DicerLaser>> DICER_LASER = registerEntity("dicer_laser", DicerLaser::new, MobCategory.MISC, builder -> builder.sized(0.1F, 0.1F).setUpdateInterval(1).clientTrackingRange(4).fireImmune());

    public static final DeferredHolder<EntityType<?>, EntityType<LaserBolt>> LASER_BOLT = registerEntity("laser_bolt", LaserBolt::new, MobCategory.MISC, builder -> builder.sized(0.3F, 0.3F).setShouldReceiveVelocityUpdates(true).fireImmune().clientTrackingRange(4));

    public static final DeferredHolder<EntityType<?>, EntityType<LaserBlade>> LASER_BLADE = registerEntity("laser_blade", LaserBlade::new, MobCategory.MISC, builder -> builder.sized(0.95F, 0.95F).fireImmune().clientTrackingRange(4));

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
