package com.barl_inc.opposing_force.events;

import com.barl_inc.opposing_force.OpposingForce;
import com.barl_inc.opposing_force.config.OFConfig;
import com.barl_inc.opposing_force.entity.*;
import com.barl_inc.opposing_force.entity.ai.goal.LightDependentTargetGoal;
import com.barl_inc.opposing_force.entity.misc.MudSplatter;
import com.barl_inc.opposing_force.item.SpectralShieldItem;
import com.barl_inc.opposing_force.item.curios.MomentumGauntletItem;
import com.barl_inc.opposing_force.item.curios.SpectralGogglesItem;
import com.barl_inc.opposing_force.registry.OFEntities;
import com.barl_inc.opposing_force.registry.OFItems;
import com.platypushasnohat.sinew.entity.base.AnimatedMonster;
import com.platypushasnohat.sinew.entity.villager.MultipleInputsTrade;
import com.platypushasnohat.sinew.registry.SinewDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import top.theillusivec4.curios.api.CuriosApi;

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
        event.put(OFEntities.MUSHY.get(), Mushy.registerAttributes().build());
        event.put(OFEntities.MOTIF.get(), Motif.registerAttributes().build());
        event.put(OFEntities.FILLET.get(), Fillet.registerAttributes().build());
        event.put(OFEntities.GHOST.get(), Ghost.registerAttributes().build());
        event.put(OFEntities.ROAMER.get(), Roamer.registerAttributes().build());
        event.put(OFEntities.HAUNT.get(), Haunt.registerAttributes().build());
        event.put(OFEntities.GRASPER.get(), Grasper.registerAttributes().build());
    }

    @SubscribeEvent
    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(OFEntities.DICER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.BEWILDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkSurfaceMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GUSHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Gusher::checkGusherSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GNAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkSurfaceMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.SCORCHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.FURBALL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Furball::checkFurballSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.TERROR.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Terror::checkTerrorSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.TARANTULA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AnimatedMonster::checkSurfaceMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.MUSHY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mushy::checkMushySpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.MOTIF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Motif::checkSurfaceMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.FILLET.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Fillet::checkFilletSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GHOST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Ghost::checkGhostSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.ROAMER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Roamer::checkRoamerSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.HAUNT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Haunt::checkHauntSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(OFEntities.GRASPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Grasper::checkGrasperSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
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
        rareTrades.add(new MultipleInputsTrade(Items.DECORATED_POT, 1, 64, OFItems.POT_OF_GREEN.get(), 1, 1, 20));
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

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker
                && event.getEntity().getHealth() >= event.getEntity().getMaxHealth()
                && CuriosApi.getCuriosInventory(attacker).map(inventory -> inventory.isEquipped(OFItems.STALKER_GLOVES.get())).orElse(false)) {
            event.setAmount(event.getAmount() * 1.3F);
        }
        if (event.getEntity() instanceof Player player
                && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && CuriosApi.getCuriosInventory(player).map(inventory -> inventory.isEquipped(OFItems.SLIMY_SHIELD.get())).orElse(false)) {
            if (!player.getCooldowns().isOnCooldown(OFItems.SLIMY_SHIELD.get()) && player.invulnerableTime <= 10 && player.level() instanceof ServerLevel serverLevel) {
                event.setAmount(0.1F);
                player.getCooldowns().addCooldown(OFItems.SLIMY_SHIELD.get(), 300);
                serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, player.getX(), player.getY(0.5D), player.getZ(), 20, 0.3D, 0.5D, 0.3D, 0.4D);
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SLIME_ATTACK, player.getSoundSource(), 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof Player player && event.getNewDamage() > 0.0F && player.getCooldowns().isOnCooldown(OFItems.SLIMY_SHIELD.get())) {
            player.getCooldowns().addCooldown(OFItems.SLIMY_SHIELD.get(), 240);
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getDamageSource();
        boolean magic = source.is(Tags.DamageTypes.IS_MAGIC) || source.is(SinewDamageTypes.MAGIC_DAMAGE);
        if (entity.isBlocking()
                && entity.getUseItem().getItem() instanceof SpectralShieldItem
                && source.getSourcePosition() != null
                && (magic || !source.is(DamageTypeTags.BYPASSES_SHIELD))) {
            event.setBlocked(true);
        }
    }

    @SubscribeEvent
    public static void onEnderTeleport(EntityTeleportEvent.EnderEntity event) {
        if (event.getEntityLiving() instanceof EnderMan enderman && !enderman.level().getEntitiesOfClass(MudSplatter.class, enderman.getBoundingBox()).isEmpty()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide) {
            SpectralGogglesItem.updateSight(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        MomentumGauntletItem.addMomentum(event.getPlayer());
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            event.addPackFinders(OpposingForce.location("datapacks/enchantment_tweaks"), PackType.SERVER_DATA, Component.translatable("pack.opposing_force.protection_tweaks"), PackSource.BUILT_IN, OFConfig.PROTECTION_TWEAKS.get(), Pack.Position.TOP);
        }
    }
}
