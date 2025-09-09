package com.foogly.staticvines.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Configuration for Static Vines mod
 */
public class StaticVinesConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Configuration options for different vine types
    public static final ModConfigSpec.BooleanValue PREVENT_CAVE_VINES_GROWTH;
    public static final ModConfigSpec.BooleanValue PREVENT_WEEPING_VINES_GROWTH;
    public static final ModConfigSpec.BooleanValue PREVENT_TWISTING_VINES_GROWTH;
    public static final ModConfigSpec.BooleanValue PREVENT_REGULAR_VINES_GROWTH;
    public static final ModConfigSpec.BooleanValue PREVENT_KELP_GROWTH;

    static {
        PREVENT_CAVE_VINES_GROWTH = BUILDER
            .comment("Prevent cave vines from growing naturally")
            .define("preventCaveVinesGrowth", true);

        PREVENT_WEEPING_VINES_GROWTH = BUILDER
            .comment("Prevent weeping vines from growing naturally")
            .define("preventWeepingVinesGrowth", true);

        PREVENT_TWISTING_VINES_GROWTH = BUILDER
            .comment("Prevent twisting vines from growing naturally")
            .define("preventTwistingVinesGrowth", true);

        PREVENT_REGULAR_VINES_GROWTH = BUILDER
            .comment("Prevent regular vines from growing naturally")
            .define("preventRegularVinesGrowth", true);

        PREVENT_KELP_GROWTH = BUILDER
            .comment("Prevent kelp from growing naturally")
            .define("preventKelpGrowth", true);

        SPEC = BUILDER.build();
    }

    /**
     * Check if growth should be prevented for a specific block class
     */
    public static boolean shouldPreventGrowth(Class<?> blockClass) {
        String className = blockClass.getSimpleName();

        switch (className) {
            case "CaveVinesBlock":
            case "CaveVinesPlantBlock":
                return PREVENT_CAVE_VINES_GROWTH.get();
            case "WeepingVinesBlock":
            case "WeepingVinesPlantBlock":
                return PREVENT_WEEPING_VINES_GROWTH.get();
            case "TwistingVinesBlock":
            case "TwistingVinesPlantBlock":
                return PREVENT_TWISTING_VINES_GROWTH.get();
            case "VineBlock":
                return PREVENT_REGULAR_VINES_GROWTH.get();
            case "KelpBlock":
            case "KelpPlantBlock":
                return PREVENT_KELP_GROWTH.get();
            default:
                return false; // Don't prevent growth for unknown blocks
        }
    }
}
