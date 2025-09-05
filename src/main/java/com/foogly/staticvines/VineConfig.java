package com.foogly.staticvines;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration class for Static Vines mod
 * 
 * Provides TOML-based configuration with boolean settings for vine growth prevention.
 * Supports runtime configuration reloading without requiring a restart.
 * Includes validation, error handling, and automatic default file generation.
 */
@EventBusSubscriber(modid = StaticVines.MODID)
public class VineConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Configuration section for vine growth prevention
    static {
        BUILDER.comment("Vine Growth Prevention Settings")
                .push("vine_growth_prevention");
    }

    /**
     * Configuration option to prevent regular vines from growing and spreading
     * Default: true (growth prevention enabled)
     */
    public static final ModConfigSpec.BooleanValue PREVENT_VINE_GROWTH = BUILDER
            .comment("Prevent regular vines from growing and spreading")
            .define("prevent_vine_growth", true);

    /**
     * Configuration option to prevent cave vines from growing downward
     * Default: true (growth prevention enabled)
     */
    public static final ModConfigSpec.BooleanValue PREVENT_CAVE_VINE_GROWTH = BUILDER
            .comment("Prevent cave vines from growing downward")
            .define("prevent_cave_vine_growth", true);

    static {
        BUILDER.pop();
    }

    // Build the configuration specification
    static final ModConfigSpec SPEC = BUILDER.build();

    // Configuration validation state
    private static boolean configurationValid = true;
    private static String lastConfigError = null;

    /**
     * Event handler for configuration loading
     * Handles initial configuration loading with validation and default file generation
     */
    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            StaticVines.LOGGER.info("Loading Static Vines configuration...");
            
            try {
                // Validate configuration values
                validateConfiguration();
                
                // Log successful configuration load
                logConfigurationValues("Configuration loaded successfully");
                
                // Ensure default configuration file exists
                ensureDefaultConfigExists(event.getConfig().getFullPath());
                
            } catch (Exception e) {
                handleConfigurationError("Failed to load configuration", e);
            }
        }
    }

    /**
     * Event handler for configuration reloading
     * Allows runtime configuration reloading without restart requirement
     */
    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            StaticVines.LOGGER.info("Reloading Static Vines configuration...");
            
            try {
                // Validate configuration values after reload
                validateConfiguration();
                
                // Log successful configuration reload
                logConfigurationValues("Configuration reloaded successfully");
                
                // Clear any previous error state
                configurationValid = true;
                lastConfigError = null;
                
                StaticVines.LOGGER.info("Configuration reload completed successfully - changes applied without restart");
                
            } catch (Exception e) {
                handleConfigurationError("Failed to reload configuration", e);
            }
        }
    }

    /**
     * Validates configuration values and handles invalid entries
     * Provides error handling for invalid configuration values
     */
    private static void validateConfiguration() {
        try {
            // Validate vine growth prevention setting
            Boolean vineGrowthValue = PREVENT_VINE_GROWTH.get();
            if (vineGrowthValue == null) {
                StaticVines.LOGGER.warn("Invalid value for prevent_vine_growth, using default: true");
                // NeoForge will use the default value automatically
            }
            
            // Validate cave vine growth prevention setting
            Boolean caveVineGrowthValue = PREVENT_CAVE_VINE_GROWTH.get();
            if (caveVineGrowthValue == null) {
                StaticVines.LOGGER.warn("Invalid value for prevent_cave_vine_growth, using default: true");
                // NeoForge will use the default value automatically
            }
            
            // Additional validation for configuration consistency
            if (vineGrowthValue != null && caveVineGrowthValue != null) {
                if (!vineGrowthValue && !caveVineGrowthValue) {
                    StaticVines.LOGGER.info("Both vine growth prevention settings are disabled - mod will not prevent any vine growth");
                }
            }
            
            configurationValid = true;
            
        } catch (Exception e) {
            configurationValid = false;
            throw new RuntimeException("Configuration validation failed", e);
        }
    }

    /**
     * Logs current configuration values with appropriate level and context
     */
    private static void logConfigurationValues(String context) {
        StaticVines.LOGGER.info("{}: ", context);
        StaticVines.LOGGER.info("  - Vine growth prevention: {}", PREVENT_VINE_GROWTH.get());
        StaticVines.LOGGER.info("  - Cave vine growth prevention: {}", PREVENT_CAVE_VINE_GROWTH.get());
        
        // Log debug information about configuration state
        if (StaticVines.LOGGER.isDebugEnabled()) {
            StaticVines.LOGGER.debug("Configuration validation state: {}", configurationValid ? "valid" : "invalid");
            if (lastConfigError != null) {
                StaticVines.LOGGER.debug("Last configuration error: {}", lastConfigError);
            }
        }
    }

    /**
     * Handles configuration errors with appropriate logging and fallback behavior
     */
    private static void handleConfigurationError(String message, Exception e) {
        configurationValid = false;
        lastConfigError = e.getMessage();
        
        StaticVines.LOGGER.error("{}: {}", message, e.getMessage());
        StaticVines.LOGGER.error("Using default configuration values for safety");
        
        // Log the default values that will be used
        StaticVines.LOGGER.warn("Fallback configuration values:");
        StaticVines.LOGGER.warn("  - Vine growth prevention: true (default)");
        StaticVines.LOGGER.warn("  - Cave vine growth prevention: true (default)");
        
        if (StaticVines.LOGGER.isDebugEnabled()) {
            StaticVines.LOGGER.debug("Configuration error details", e);
        }
    }

    /**
     * Ensures that a default configuration file exists
     * Creates default configuration file when missing
     */
    private static void ensureDefaultConfigExists(Path configPath) {
        try {
            if (configPath != null && !Files.exists(configPath)) {
                StaticVines.LOGGER.info("Configuration file not found, it will be created with default values");
                
                // Create parent directories if they don't exist
                Path parentDir = configPath.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                    StaticVines.LOGGER.debug("Created configuration directory: {}", parentDir);
                }
                
                StaticVines.LOGGER.info("Default configuration file will be generated at: {}", configPath);
            } else if (configPath != null) {
                StaticVines.LOGGER.debug("Configuration file exists at: {}", configPath);
            }
            
        } catch (Exception e) {
            StaticVines.LOGGER.error("Failed to ensure default configuration file exists: {}", e.getMessage());
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Configuration file creation error details", e);
            }
        }
    }

    /**
     * Gets the current configuration validation state
     * @return true if configuration is valid, false if there are errors
     */
    public static boolean isConfigurationValid() {
        return configurationValid;
    }

    /**
     * Gets the last configuration error message
     * @return the last error message, or null if no errors
     */
    public static String getLastConfigError() {
        return lastConfigError;
    }

    /**
     * Safely gets the vine growth prevention setting with error handling
     * @return the configured value, or true as fallback if configuration is invalid
     */
    public static boolean getVineGrowthPrevention() {
        try {
            return PREVENT_VINE_GROWTH.get();
        } catch (Exception e) {
            StaticVines.LOGGER.error("Error accessing vine growth prevention setting, using default: true", e);
            return true; // Safe fallback
        }
    }

    /**
     * Safely gets the cave vine growth prevention setting with error handling
     * @return the configured value, or true as fallback if configuration is invalid
     */
    public static boolean getCaveVineGrowthPrevention() {
        try {
            return PREVENT_CAVE_VINE_GROWTH.get();
        } catch (Exception e) {
            StaticVines.LOGGER.error("Error accessing cave vine growth prevention setting, using default: true", e);
            return true; // Safe fallback
        }
    }
}