package com.betterpathfinding.betterpathfinding.config;

import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BetterPathfindingConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.BooleanValue DEBUG_RENDER;
    public static final ModConfigSpec.BooleanValue PERFORMANCE_STATS;
    public static final ModConfigSpec.BooleanValue FIX_SIGHT_ON_TRANSPARENT;
    public static final ModConfigSpec.BooleanValue FIX_DOOR_PATHFINDING;
    public static final ModConfigSpec.BooleanValue FIX_FALL_DAMAGE_PATH;
    public static final ModConfigSpec.BooleanValue FIX_STUCK_DETECTION;
    public static final ModConfigSpec.BooleanValue FIX_STRAFING_RESET;
    public static final ModConfigSpec.BooleanValue FIX_POST_FEED_PATH;
    public static final ModConfigSpec.BooleanValue FIX_SPECIAL_BLOCKS;
    public static final ModConfigSpec.BooleanValue PATH_CACHE_ENABLED;
    public static final ModConfigSpec.BooleanValue STEERING_ENABLED;
    public static final ModConfigSpec.BooleanValue FREQUENCY_CONTROL_ENABLED;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SIGHT_TRANSPARENT_BLOCKS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Better Pathfinding general settings").push("general");
        ENABLED = builder.comment("Master switch for all fixes").define("enabled", true);
        DEBUG_RENDER = builder.comment("Path visualization overlay").define("debug_render", false);
        PERFORMANCE_STATS = builder.comment("Pathfinding performance statistics").define("performance_stats", false);
        builder.pop();
        builder.comment("Individual fix toggles").push("fixes");
        FIX_SIGHT_ON_TRANSPARENT = builder.comment("Mobs can see through glass, glass panes, iron bars, etc.").define("fix_sight_on_transparent", true);
        FIX_DOOR_PATHFINDING = builder.comment("Mobs can path through wooden doors and trapdoors").define("fix_door_pathfinding", true);
        FIX_FALL_DAMAGE_PATH = builder.comment("Mobs prefer safe paths over dangerous drops").define("fix_fall_damage_path", true);
        FIX_STUCK_DETECTION = builder.comment("Faster stuck detection and path recalculation").define("fix_stuck_detection", true);
        FIX_STRAFING_RESET = builder.comment("Fix skeleton sprint-spin after losing target (MC-165128)").define("fix_strafing_reset", true);
        FIX_POST_FEED_PATH = builder.comment("Fed animals stop following when not breedable").define("fix_post_feed_path", true);
        FIX_SPECIAL_BLOCKS = builder.comment("Improve pathfinding on honey blocks, ladders, vines").define("fix_special_blocks", false);
        builder.pop();
        builder.comment("Performance options").push("performance");
        PATH_CACHE_ENABLED = builder.comment("Skip duplicate moveTo calls within a short window").define("path_cache_enabled", true);
        STEERING_ENABLED = builder.comment("Same-type mobs gently push apart when too close").define("steering_enabled", true);
        FREQUENCY_CONTROL_ENABLED = builder.comment("Pause pathfinding after consecutive failures").define("frequency_control_enabled", true);
        builder.pop();
        builder.comment("Blocks that don't block mob line-of-sight").push("sight_fix");
        SIGHT_TRANSPARENT_BLOCKS = builder.comment("List of block IDs that mobs can see through").defineListAllowEmpty("transparent_blocks", List.of("minecraft:glass", "minecraft:white_stained_glass", "minecraft:orange_stained_glass", "minecraft:magenta_stained_glass", "minecraft:light_blue_stained_glass", "minecraft:yellow_stained_glass", "minecraft:lime_stained_glass", "minecraft:pink_stained_glass", "minecraft:gray_stained_glass", "minecraft:light_gray_stained_glass", "minecraft:cyan_stained_glass", "minecraft:purple_stained_glass", "minecraft:blue_stained_glass", "minecraft:brown_stained_glass", "minecraft:green_stained_glass", "minecraft:red_stained_glass", "minecraft:black_stained_glass", "minecraft:tinted_glass", "minecraft:glass_pane", "minecraft:white_stained_glass_pane", "minecraft:orange_stained_glass_pane", "minecraft:magenta_stained_glass_pane", "minecraft:light_blue_stained_glass_pane", "minecraft:yellow_stained_glass_pane", "minecraft:lime_stained_glass_pane", "minecraft:pink_stained_glass_pane", "minecraft:gray_stained_glass_pane", "minecraft:light_gray_stained_glass_pane", "minecraft:cyan_stained_glass_pane", "minecraft:purple_stained_glass_pane", "minecraft:blue_stained_glass_pane", "minecraft:brown_stained_glass_pane", "minecraft:green_stained_glass_pane", "minecraft:red_stained_glass_pane", "minecraft:black_stained_glass_pane", "minecraft:iron_bars"), obj -> {
            String s;
            return obj instanceof String && (s = (String)obj).startsWith("minecraft:");
        });
        builder.pop();
        SPEC = builder.build();
    }
}
