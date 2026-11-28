package com.barl_inc.opposing_force.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public class OFConfig {

    public static IConfigSpec COMMON_CONFIG;

    // common
    public static ModConfigSpec.BooleanValue INCREASE_MONSTER_SPAWN_CAP;
    public static ModConfigSpec.IntValue MONSTER_SPAWN_CAP;
    public static ModConfigSpec.BooleanValue PROTECTION_TWEAKS;
    public static ModConfigSpec.BooleanValue ZOMBIE_TWEAKS;
    public static ModConfigSpec.BooleanValue GRELL_BITE_MANIA;
    public static ModConfigSpec.BooleanValue OF_LIGHTNING;

    public static final String CATEGORY_ZOMBIE_TWEAKS = "zombie_tweaks";
    public static ModConfigSpec.DoubleValue ZOMBIE_LEADER_HEALTH;
    public static ModConfigSpec.DoubleValue ZOMBIE_LEADER_ATTACK_DAMAGE;
    public static ModConfigSpec.DoubleValue ZOMBIE_LEADER_SCALE;
    public static ModConfigSpec.DoubleValue ZOMBIE_LEADER_REINFORCEMENT_CHANCE;
    public static ModConfigSpec.DoubleValue ZOMBIE_LEADER_SPAWN_CHANCE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        INCREASE_MONSTER_SPAWN_CAP = builder.comment("Whether the spawn cap for monsters should be increased").define("increaseMonsterSpawnCap", true);
        MONSTER_SPAWN_CAP = builder.comment("The spawn cap for monsters if increaseMonsterSpawnCap is enabled").defineInRange("monsterSpawnCap", 90, 0, 500);
        PROTECTION_TWEAKS = builder.comment("Whether Protection should be half as effective against magic damageMultiplier").define("protectionTweaks", true);
        ZOMBIE_TWEAKS = builder.comment("Whether zombie tweaks should be enabled").define("zombieTweaks", true);
        GRELL_BITE_MANIA = builder.comment("Whether the Grell's bite after failing its illusion should inflict Mania", "WARNING: Mania distorts the audio and screen, which may cause nausea, headaches, or motion sickness").define("grellBiteMania", false);
        OF_LIGHTNING = builder.comment("Whether vanilla lightning should be replaced with electric lightning").define("ofLightning", true);

        builder.push(CATEGORY_ZOMBIE_TWEAKS);
        ZOMBIE_LEADER_HEALTH = builder.comment("The amount of extra health zombie leaders should have").defineInRange("zombieLeaderHealth", 20.0D, 0, 100);
        ZOMBIE_LEADER_ATTACK_DAMAGE = builder.comment("The amount of extra attack damageMultiplier zombie leaders should have").defineInRange("zombieLeaderAttackDamage", 2.0D, 0, 100);
        ZOMBIE_LEADER_SCALE = builder.comment("The amount of extra size zombie leaders should have").defineInRange("zombieLeaderScale", 0.3D, 0, 10);
        ZOMBIE_LEADER_REINFORCEMENT_CHANCE = builder.comment("The chance hurting a zombie leader summons reinforcements nearby").defineInRange("zombieLeaderReinforcementChance", 0.5D, 0, 1);
        ZOMBIE_LEADER_SPAWN_CHANCE = builder.comment("The chance for a normal zombie to spawn as a leader").defineInRange("zombieLeaderSpawnChance", 0.05D, 0, 1);
        builder.pop();

        COMMON_CONFIG = builder.build();
    }
}
