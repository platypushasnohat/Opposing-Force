package com.barl_inc.opposing_force.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public class OFConfig {

    public static IConfigSpec COMMON_CONFIG;

    // common
    public static ModConfigSpec.BooleanValue INCREASE_MONSTER_SPAWN_CAP;
    public static ModConfigSpec.IntValue MONSTER_SPAWN_CAP;

    public static ModConfigSpec.BooleanValue ZOMBIE_TWEAKS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        INCREASE_MONSTER_SPAWN_CAP = builder.comment("Whether the spawn cap for monsters should be increased").define("increaseMonsterSpawnCap", true);
        MONSTER_SPAWN_CAP = builder.comment("The spawn cap for monsters if increaseMonsterSpawnCap is enabled").defineInRange("monsterSpawnCap", 90, 0, 200);
        ZOMBIE_TWEAKS = builder.comment("Whether zombie tweaks should be enabled").define("zombieTweaks", true);
        COMMON_CONFIG = builder.build();
    }
}
