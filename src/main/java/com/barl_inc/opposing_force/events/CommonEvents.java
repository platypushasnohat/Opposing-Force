package com.barl_inc.opposing_force.events;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.*;
import com.barl_inc.opposing_force.entity.ai.goal.LightDependentTargetGoal;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.villager.MultipleInputsTrade;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.List;

@EventBusSubscriber(modid = OpposingForce.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(OFEntities.DICER.get(), Dicer.registerAttributes().build());
        event.put(OFEntities.BEWILDER.get(), Bewilder.registerAttributes().build());
        event.put(OFEntities.GUSHER.get(), Gusher.registerAttributes().build());
        event.put(OFEntities.GNAT.get(), Gnat.registerAttributes().build());
        event.put(OFEntities.SCORCHER.get(), Scorcher.registerAttributes().build());
        event.put(OFEntities.FURBALL.get(), Furball.registerAttributes().build());
        event.put(OFEntities.TERROR.get(), Terror.registerAttributes().build());
        event.put(OFEntities.TARANTULA.get(), Tarantula.registerAttributes().build());
        event.put(OFEntities.OCTOVINE.get(), Octovine.registerAttributes().build());
    }

    @SubscribeEvent
    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(OFEntities.DICER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.BEWILDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkSurfaceMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GUSHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Gusher::checkGusherSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GNAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkSurfaceMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.SCORCHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.FURBALL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Furball::checkFurballSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.TERROR.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.TARANTULA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkSurfaceMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        SpawnPlacementType groundOrWater = (level, pos, type) ->
                SpawnPlacementTypes.ON_GROUND.isSpawnPositionOk(level, pos, type)
                        || SpawnPlacementTypes.IN_WATER.isSpawnPositionOk(level, pos, type);
        event.register(OFEntities.OCTOVINE.get(), groundOrWater, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Octovine::checkOctovineSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerWandererTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_SWORD, 1, 32, OFItems.EMERALD_SWORD.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_SHOVEL, 1, 32, OFItems.EMERALD_SHOVEL.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_PICKAXE, 1, 32, OFItems.EMERALD_PICKAXE.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_AXE, 1, 32, OFItems.EMERALD_AXE.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_HOE, 1, 32, OFItems.EMERALD_HOE.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_HELMET, 1, 32, OFItems.EMERALD_MASK.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_CHESTPLATE, 1, 32, OFItems.EMERALD_CHESTPLATE.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_LEGGINGS, 1, 32, OFItems.EMERALD_LEGGINGS.get(), 1, 1, 20));
        rareTrades.add(new MultipleInputsTrade(Items.DIAMOND_BOOTS, 1, 32, OFItems.EMERALD_BOOTS.get(), 1, 1, 20));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Mob mob) {
            if (mob instanceof Spider spider) {
                spider.goalSelector.addGoal(2, new AvoidEntityGoal<>(spider, Gusher.class, 8.0F, 1.0D, 1.2D));
                spider.targetSelector.addGoal(3, new LightDependentTargetGoal<>(spider, Gnat.class, true));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (entity instanceof Gnat && damageSource.getEntity() instanceof Spider) {
            event.setNewDamage(event.getOriginalDamage() * 4);
        }
    }
}
