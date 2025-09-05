package com.foogly.staticvines;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

/**
 * Event handler for preventing vine growth and spreading.
 * 
 * This class intercepts block growth events and cancels them based on
 * configuration settings to prevent vines from naturally growing and spreading.
 */
@EventBusSubscriber(modid = StaticVines.MODID)
public class VineGrowthHandler {

    /**
     * Handle neighbor notification events to detect vine spreading attempts.
     * 
     * This event is fired when a block notifies its neighbors of changes,
     * which includes when vines attempt to spread to adjacent blocks.
     * 
     * @param event The neighbor notification event
     */
    @SubscribeEvent
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        try {
            LevelAccessor levelAccessor = event.getLevel();
            if (!(levelAccessor instanceof Level)) {
                return; // Only handle server-side events
            }
            Level level = (Level) levelAccessor;
            BlockPos pos = event.getPos();
            BlockState blockState = event.getState();
            
            // Validate event data
            if (pos == null || blockState == null) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("Skipping neighbor notify event with null position or block state");
                }
                return;
            }
            
            // Check if the notifying block is a vine
            VineType vineType = VineTypeDetector.detectVineType(blockState);
            if (vineType == null) {
                return; // Not a vine block, ignore
            }
            
            // Check configuration to see if growth prevention is enabled for this vine type
            if (!shouldPreventGrowthForVineType(vineType)) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("Growth prevention disabled for {} at position {}", 
                        VineTypeDetector.getVineTypeDescription(vineType), pos);
                }
                return; // Growth prevention disabled for this vine type
            }
            
            // Log the prevention action for debugging
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Preventing vine spreading from {} at position {}", 
                    VineTypeDetector.getVineTypeDescription(vineType), pos);
            }
            
            // Cancel the neighbor notification to prevent spreading
            event.setCanceled(true);
            
            // Log successful prevention
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Successfully prevented vine spreading for {} at {}", 
                    VineTypeDetector.getVineTypeDescription(vineType), pos);
            }
            
        } catch (Exception e) {
            // Comprehensive error handling for event processing failures
            StaticVines.LOGGER.error("Error processing neighbor notify event for vine growth prevention", e);
            
            // Log additional context for debugging
            if (StaticVines.LOGGER.isDebugEnabled()) {
                try {
                    StaticVines.LOGGER.debug("Event details - Level: {}, Position: {}, State: {}", 
                        event.getLevel(), event.getPos(), event.getState());
                } catch (Exception debugException) {
                    StaticVines.LOGGER.debug("Could not log event details due to additional error", debugException);
                }
            }
            
            // Graceful fallback - do not cancel event if we can't process it safely
            // This ensures mod stability and prevents breaking other mod functionality
        }
    }

    /**
     * Handle block growth feature events to detect vine growth attempts.
     * 
     * This event is fired when blocks attempt to grow or spread,
     * allowing us to intercept and cancel vine growth events.
     * 
     * @param event The block growth feature event
     */
    @SubscribeEvent
    public static void onBlockGrowFeature(BlockGrowFeatureEvent event) {
        try {
            LevelAccessor levelAccessor = event.getLevel();
            if (!(levelAccessor instanceof Level)) {
                return; // Only handle server-side events
            }
            Level level = (Level) levelAccessor;
            BlockPos pos = event.getPos();
            
            // Validate event data
            if (pos == null || level == null) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("Skipping block grow feature event with null position or level");
                }
                return;
            }
            
            BlockState blockState;
            try {
                blockState = level.getBlockState(pos);
            } catch (Exception e) {
                StaticVines.LOGGER.warn("Could not get block state at position {} for growth prevention", pos, e);
                return;
            }
            
            if (blockState == null) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("Skipping block grow feature event with null block state at {}", pos);
                }
                return;
            }
            
            // Check if the growing block is a vine
            VineType vineType = VineTypeDetector.detectVineType(blockState);
            if (vineType == null) {
                return; // Not a vine block, ignore
            }
            
            // Check configuration to see if growth prevention is enabled for this vine type
            if (!shouldPreventGrowthForVineType(vineType)) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("Growth prevention disabled for {} at position {}", 
                        VineTypeDetector.getVineTypeDescription(vineType), pos);
                }
                return; // Growth prevention disabled for this vine type
            }
            
            // Log the prevention action for debugging
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Preventing vine growth for {} at position {}", 
                    VineTypeDetector.getVineTypeDescription(vineType), pos);
            }
            
            // Cancel the growth event
            event.setCanceled(true);
            
            // Log successful prevention
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Successfully prevented vine growth for {} at {}", 
                    VineTypeDetector.getVineTypeDescription(vineType), pos);
            }
            
        } catch (Exception e) {
            // Comprehensive error handling for event processing failures
            StaticVines.LOGGER.error("Error processing block grow feature event for vine growth prevention", e);
            
            // Log additional context for debugging
            if (StaticVines.LOGGER.isDebugEnabled()) {
                try {
                    StaticVines.LOGGER.debug("Event details - Level: {}, Position: {}", 
                        event.getLevel(), event.getPos());
                } catch (Exception debugException) {
                    StaticVines.LOGGER.debug("Could not log event details due to additional error", debugException);
                }
            }
            
            // Graceful fallback - do not cancel event if we can't process it safely
            // This ensures mod stability and prevents breaking other mod functionality
        }
    }

    /**
     * Handle block break events to detect and prevent vine growth through block breaking mechanics.
     * 
     * Some vine growth occurs when blocks are broken and vines attempt to spread into the space.
     * This handler prevents such growth attempts.
     * 
     * @param event The block break event
     */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        try {
            LevelAccessor levelAccessor = event.getLevel();
            if (!(levelAccessor instanceof Level)) {
                return; // Only handle server-side events
            }
            Level level = (Level) levelAccessor;
            BlockPos pos = event.getPos();
            
            // Validate event data
            if (pos == null || level == null) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("Skipping block break event with null position or level");
                }
                return;
            }
            
            // Check adjacent positions for vine blocks that might try to grow into this space
            BlockPos[] adjacentPositions = {
                pos.above(),
                pos.below(),
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west()
            };
            
            for (BlockPos adjacentPos : adjacentPositions) {
                try {
                    if (adjacentPos == null) {
                        continue; // Skip null positions
                    }
                    
                    BlockState adjacentState;
                    try {
                        adjacentState = level.getBlockState(adjacentPos);
                    } catch (Exception e) {
                        if (StaticVines.LOGGER.isDebugEnabled()) {
                            StaticVines.LOGGER.debug("Could not get block state at adjacent position {} during block break monitoring", adjacentPos, e);
                        }
                        continue;
                    }
                    
                    if (adjacentState == null) {
                        continue; // Skip null block states
                    }
                    
                    VineType vineType = VineTypeDetector.detectVineType(adjacentState);
                    
                    if (vineType != null && shouldPreventGrowthForVineType(vineType)) {
                        // Schedule a check to prevent vine growth into the broken block space
                        if (StaticVines.LOGGER.isDebugEnabled()) {
                            StaticVines.LOGGER.debug("Monitoring for potential vine growth from {} at {} into broken block space at {}", 
                                VineTypeDetector.getVineTypeDescription(vineType), adjacentPos, pos);
                        }
                    }
                } catch (Exception e) {
                    // Handle errors for individual adjacent position checks
                    if (StaticVines.LOGGER.isDebugEnabled()) {
                        StaticVines.LOGGER.debug("Error checking adjacent position {} during block break event", adjacentPos, e);
                    }
                    // Continue with other positions
                }
            }
            
        } catch (Exception e) {
            // Comprehensive error handling for event processing failures
            StaticVines.LOGGER.error("Error processing block break event for vine growth monitoring", e);
            
            // Log additional context for debugging
            if (StaticVines.LOGGER.isDebugEnabled()) {
                try {
                    StaticVines.LOGGER.debug("Event details - Level: {}, Position: {}", 
                        event.getLevel(), event.getPos());
                } catch (Exception debugException) {
                    StaticVines.LOGGER.debug("Could not log event details due to additional error", debugException);
                }
            }
            
            // Graceful fallback - continue execution without breaking functionality
        }
    }

    /**
     * Handle block place events to ensure manual vine placement is not affected.
     * 
     * This event allows us to distinguish between player placement and natural growth,
     * ensuring that manual vine placement by players is never prevented.
     * 
     * @param event The block place event
     */
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        try {
            // Always allow player placement - this event only fires for entity/player placement
            // Natural growth does not trigger EntityPlaceEvent, so we don't need to cancel anything here
            // This method exists to document that manual placement is explicitly allowed
            
            if (event.getEntity() != null) {
                LevelAccessor levelAccessor = event.getLevel();
                if (levelAccessor instanceof Level) {
                    Level level = (Level) levelAccessor;
                    BlockPos pos = event.getPos();
                    BlockState placedState = event.getPlacedBlock();
                    
                    // Validate event data
                    if (pos == null || placedState == null) {
                        if (StaticVines.LOGGER.isDebugEnabled()) {
                            StaticVines.LOGGER.debug("Skipping block place event with null position or placed state");
                        }
                        return;
                    }
                    
                    // Check if the placed block is a vine and log for debugging
                    VineType vineType = VineTypeDetector.detectVineType(placedState);
                    if (vineType != null && StaticVines.LOGGER.isDebugEnabled()) {
                        try {
                            String entityName = event.getEntity().getName().getString();
                            StaticVines.LOGGER.debug("Allowing manual placement of {} at position {} by entity {}", 
                                VineTypeDetector.getVineTypeDescription(vineType), pos, entityName);
                        } catch (Exception e) {
                            StaticVines.LOGGER.debug("Allowing manual placement of {} at position {} by entity (name unavailable)", 
                                VineTypeDetector.getVineTypeDescription(vineType), pos);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            // Comprehensive error handling for event processing failures
            StaticVines.LOGGER.error("Error processing block place event for vine placement logging", e);
            
            // Log additional context for debugging
            if (StaticVines.LOGGER.isDebugEnabled()) {
                try {
                    StaticVines.LOGGER.debug("Event details - Level: {}, Position: {}, Entity: {}", 
                        event.getLevel(), event.getPos(), event.getEntity());
                } catch (Exception debugException) {
                    StaticVines.LOGGER.debug("Could not log event details due to additional error", debugException);
                }
            }
            
            // Graceful fallback - always allow placement (this is the intended behavior)
            // No event cancellation needed as this event is for logging purposes only
        }
    }

    /**
     * Check if growth should be prevented for the given vine type based on configuration.
     * 
     * This method checks the configuration settings to determine whether growth
     * prevention is enabled for the specific vine type. It uses safe configuration
     * access methods that include error handling and fallback values.
     * 
     * @param vineType The type of vine to check
     * @return true if growth should be prevented, false otherwise
     */
    private static boolean shouldPreventGrowthForVineType(VineType vineType) {
        if (vineType == null) {
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Null vine type provided to shouldPreventGrowthForVineType, returning false");
            }
            return false; // No vine type means no prevention needed
        }
        
        try {
            boolean preventGrowth;
            
            switch (vineType) {
                case REGULAR_VINE:
                    preventGrowth = VineConfig.getVineGrowthPrevention();
                    if (StaticVines.LOGGER.isDebugEnabled()) {
                        StaticVines.LOGGER.debug("Configuration check for REGULAR_VINE: prevention = {}", preventGrowth);
                    }
                    return preventGrowth;
                    
                case CAVE_VINE:
                case CAVE_VINE_PLANT:
                    preventGrowth = VineConfig.getCaveVineGrowthPrevention();
                    if (StaticVines.LOGGER.isDebugEnabled()) {
                        StaticVines.LOGGER.debug("Configuration check for {}: prevention = {}", vineType, preventGrowth);
                    }
                    return preventGrowth;
                    
                default:
                    // Unknown vine type, log warning and default to prevention enabled for safety
                    StaticVines.LOGGER.warn("Unknown vine type encountered: {}. Defaulting to growth prevention enabled for mod stability.", vineType);
                    if (StaticVines.LOGGER.isDebugEnabled()) {
                        StaticVines.LOGGER.debug("Available vine types: {}", java.util.Arrays.toString(VineType.values()));
                    }
                    return true;
            }
        } catch (Exception e) {
            // Configuration access error, log and default to prevention enabled
            StaticVines.LOGGER.error("Error accessing configuration for vine type {}. Defaulting to growth prevention enabled for safety.", vineType, e);
            
            // Additional debugging information
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Configuration error details - vine type: {}, error class: {}", 
                    vineType, e.getClass().getSimpleName());
                
                // Check if configuration is in a valid state
                try {
                    boolean configValid = VineConfig.isConfigurationValid();
                    StaticVines.LOGGER.debug("Configuration validation state: {}", configValid);
                    if (!configValid) {
                        String lastError = VineConfig.getLastConfigError();
                        StaticVines.LOGGER.debug("Last configuration error: {}", lastError);
                    }
                } catch (Exception configCheckException) {
                    StaticVines.LOGGER.debug("Could not check configuration state", configCheckException);
                }
            }
            
            return true; // Safe fallback - prevent growth when in doubt
        }
    }

    /**
     * Check if a vine placement is likely due to natural growth rather than player action.
     * 
     * @param level The world level
     * @param pos The position where the vine is being placed
     * @param vineType The type of vine being placed
     * @return true if this appears to be natural growth
     */
    private static boolean isNaturalVineGrowth(Level level, BlockPos pos, VineType vineType) {
        // For regular vines, check if there are adjacent vine blocks (spreading behavior)
        if (vineType == VineType.REGULAR_VINE) {
            return hasAdjacentVines(level, pos);
        }
        
        // For cave vines, check if there's a cave vine above (downward growth behavior)
        if (vineType == VineType.CAVE_VINE || vineType == VineType.CAVE_VINE_PLANT) {
            return hasCaveVineAbove(level, pos);
        }
        
        return false;
    }

    /**
     * Check if there are adjacent vine blocks that could cause spreading.
     * 
     * @param level The world level
     * @param pos The position to check around
     * @return true if adjacent vines are found
     */
    private static boolean hasAdjacentVines(Level level, BlockPos pos) {
        // Check horizontal and vertical adjacent positions
        BlockPos[] adjacentPositions = {
            pos.above(),
            pos.below(),
            pos.north(),
            pos.south(),
            pos.east(),
            pos.west()
        };
        
        for (BlockPos adjacentPos : adjacentPositions) {
            if (VineTypeDetector.isRegularVine(level.getBlockState(adjacentPos).getBlock())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Check if there's a cave vine above the position (indicating downward growth).
     * 
     * @param level The world level
     * @param pos The position to check above
     * @return true if a cave vine is found above
     */
    private static boolean hasCaveVineAbove(Level level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        Block blockAbove = level.getBlockState(abovePos).getBlock();
        return VineTypeDetector.isCaveVine(blockAbove);
    }

    /**
     * Check if a block position and its neighbors contain vine blocks that might be affected.
     * This method can be used for additional validation if needed.
     * 
     * @param level The world level
     * @param pos The position to check
     * @return true if vine blocks are present in the area
     */
    private static boolean hasVineBlocksInArea(Level level, BlockPos pos) {
        try {
            if (level == null || pos == null) {
                return false;
            }
            
            // Check the block at the position
            if (VineTypeDetector.isAnyVineType(level, pos)) {
                return true;
            }
            
            // Check adjacent positions for vine blocks
            BlockPos[] adjacentPositions = {
                pos.above(),
                pos.below(),
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west()
            };
            
            for (BlockPos adjacentPos : adjacentPositions) {
                try {
                    if (adjacentPos != null && VineTypeDetector.isAnyVineType(level, adjacentPos)) {
                        return true;
                    }
                } catch (Exception e) {
                    if (StaticVines.LOGGER.isDebugEnabled()) {
                        StaticVines.LOGGER.debug("Error checking vine blocks at adjacent position {}", adjacentPos, e);
                    }
                    // Continue checking other positions
                }
            }
            
            return false;
        } catch (Exception e) {
            StaticVines.LOGGER.warn("Error checking for vine blocks in area around position {}", pos, e);
            return false; // Safe fallback
        }
    }

    /**
     * Validates event data and logs any issues for debugging purposes.
     * This method helps ensure mod stability by checking for null or invalid data.
     * 
     * @param event The event to validate
     * @param eventType A description of the event type for logging
     * @return true if the event data is valid, false otherwise
     */
    private static boolean validateEventData(Object event, String eventType) {
        try {
            if (event == null) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("Received null {} event", eventType);
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            StaticVines.LOGGER.warn("Error validating {} event data", eventType, e);
            return false;
        }
    }

    /**
     * Logs comprehensive debugging information about vine growth prevention actions.
     * This method provides detailed logging when debug mode is enabled.
     * 
     * @param action The action being performed (e.g., "preventing", "allowing")
     * @param vineType The type of vine involved
     * @param pos The position where the action occurs
     * @param context Additional context information
     */
    private static void logVineAction(String action, VineType vineType, BlockPos pos, String context) {
        if (!StaticVines.LOGGER.isDebugEnabled()) {
            return; // Skip logging if debug is not enabled
        }
        
        try {
            String vineDescription = VineTypeDetector.getVineTypeDescription(vineType);
            if (context != null && !context.isEmpty()) {
                StaticVines.LOGGER.debug("{} {} at position {} - {}", 
                    action, vineDescription, pos, context);
            } else {
                StaticVines.LOGGER.debug("{} {} at position {}", 
                    action, vineDescription, pos);
            }
        } catch (Exception e) {
            StaticVines.LOGGER.debug("Error logging vine action: {}", e.getMessage());
        }
    }

    /**
     * Provides a safe way to get configuration status with error handling.
     * This method ensures that configuration errors don't break event processing.
     * 
     * @return A string describing the current configuration status
     */
    private static String getConfigurationStatus() {
        try {
            boolean configValid = VineConfig.isConfigurationValid();
            if (configValid) {
                return String.format("Config valid - Vine prevention: %s, Cave vine prevention: %s", 
                    VineConfig.getVineGrowthPrevention(), 
                    VineConfig.getCaveVineGrowthPrevention());
            } else {
                String lastError = VineConfig.getLastConfigError();
                return String.format("Config invalid - Last error: %s", 
                    lastError != null ? lastError : "Unknown error");
            }
        } catch (Exception e) {
            return String.format("Config status unavailable - Error: %s", e.getMessage());
        }
    }
}