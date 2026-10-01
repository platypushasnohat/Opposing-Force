package com.barl_inc.opposing_force.events;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.entity.Bewilder;
import com.barl_inc.opposing_force.entity.Dicer;
import com.barl_inc.opposing_force.entity.Gnat;
import com.barl_inc.opposing_force.entity.Gusher;
import com.barl_inc.opposing_force.entity.ai.goal.SpiderTargetGoal;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.entity.villager.MultipleInputsTrade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Monster;
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
    }

    @SubscribeEvent
    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(OFEntities.DICER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.BEWILDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Bewilder::checkBewilderSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GUSHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Gusher::checkGusherSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GNAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    @SubscribeEvent
    public static void wandererTradesEvent(WandererTradesEvent event) {
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
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Mob mob) {
            if (mob instanceof Spider spider) {
                spider.goalSelector.addGoal(2, new AvoidEntityGoal<>(spider, Gusher.class, 8.0F, 1.0D, 1.2D));
                spider.targetSelector.addGoal(3, new SpiderTargetGoal<>(spider, Gnat.class));
            }
        }
    }
}
