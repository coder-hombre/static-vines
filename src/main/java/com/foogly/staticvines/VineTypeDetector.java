package com.foogly.staticvines;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Utility class for detecting and identifying vine block types.
 * 
 * This class provides methods to identify different types of vine blocks
 * and determine their growth characteristics for prevention purposes.
 */
public class VineTypeDetector {

    /**
     * Private constructor to prevent instantiation of utility class
     */
    private VineTypeDetector() {
        throw new UnsupportedOperationException("VineTypeDetector is a utility class and cannot be instantiated");
    }

    /**
     * Detect the vine type from a Block instance
     * 
     * @param block The block to analyze
     * @return The VineType if recognized, null otherwise
     */
    public static VineType detectVineType(Block block) {
        if (block == null) {
            return null;
        }
        
        try {
            return VineType.fromBlock(block);
        } catch (Exception e) {
            // Handle errors when encountering unknown block types
            StaticVines.LOGGER.warn("Error detecting vine type for block: {}. Treating as non-vine block.", block, e);
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Block detection error details - block class: {}, error: {}", 
                    block.getClass().getSimpleName(), e.getMessage());
            }
            return null; // Treat unknown/problematic blocks as non-vine blocks
        }
    }

    /**
     * Detect the vine type from a BlockState
     * 
     * @param blockState The block state to analyze
     * @return The VineType if recognized, null otherwise
     */
    public static VineType detectVineType(BlockState blockState) {
        if (blockState == null) {
            return null;
        }
        
        try {
            Block block = blockState.getBlock();
            if (block == null) {
                if (StaticVines.LOGGER.isDebugEnabled()) {
                    StaticVines.LOGGER.debug("BlockState contains null block, treating as non-vine");
                }
                return null;
            }
            return detectVineType(block);
        } catch (Exception e) {
            // Handle errors when encountering unknown block states
            StaticVines.LOGGER.warn("Error detecting vine type from block state. Treating as non-vine block.", e);
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("BlockState detection error details - state class: {}, error: {}", 
                    blockState.getClass().getSimpleName(), e.getMessage());
            }
            return null; // Treat unknown/problematic block states as non-vine blocks
        }
    }

    /**
     * Detect the vine type at a specific position in the world
     * 
     * @param level The world/level
     * @param pos The position to check
     * @return The VineType if recognized, null otherwise
     */
    public static VineType detectVineType(Level level, BlockPos pos) {
        if (level == null || pos == null) {
            return null;
        }
        
        try {
            BlockState blockState = level.getBlockState(pos);
            return detectVineType(blockState);
        } catch (Exception e) {
            // Handle errors when accessing world blocks
            StaticVines.LOGGER.warn("Error detecting vine type at position {} in level. Treating as non-vine block.", pos, e);
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("World access error details - level: {}, position: {}, error: {}", 
                    level.getClass().getSimpleName(), pos, e.getMessage());
            }
            return null; // Treat inaccessible blocks as non-vine blocks
        }
    }

    /**
     * Check if a block is a regular vine (wall-climbing vine)
     * 
     * @param block The block to check
     * @return true if it's a regular vine, false otherwise
     */
    public static boolean isRegularVine(Block block) {
        VineType vineType = detectVineType(block);
        return vineType == VineType.REGULAR_VINE;
    }

    /**
     * Check if a block is a cave vine (either main block or plant)
     * 
     * @param block The block to check
     * @return true if it's any type of cave vine, false otherwise
     */
    public static boolean isCaveVine(Block block) {
        VineType vineType = detectVineType(block);
        return vineType == VineType.CAVE_VINE || vineType == VineType.CAVE_VINE_PLANT;
    }

    /**
     * Check if a block is the main cave vine block (hangs from ceiling)
     * 
     * @param block The block to check
     * @return true if it's the main cave vine block, false otherwise
     */
    public static boolean isCaveVineMain(Block block) {
        VineType vineType = detectVineType(block);
        return vineType == VineType.CAVE_VINE;
    }

    /**
     * Check if a block is a cave vine plant (extending downward part)
     * 
     * @param block The block to check
     * @return true if it's a cave vine plant, false otherwise
     */
    public static boolean isCaveVinePlant(Block block) {
        VineType vineType = detectVineType(block);
        return vineType == VineType.CAVE_VINE_PLANT;
    }

    /**
     * Check if any block is a recognized vine type
     * 
     * @param block The block to check
     * @return true if it's any recognized vine type, false otherwise
     */
    public static boolean isAnyVineType(Block block) {
        return detectVineType(block) != null;
    }

    /**
     * Check if a BlockState represents any vine type
     * 
     * @param blockState The block state to check
     * @return true if it's any recognized vine type, false otherwise
     */
    public static boolean isAnyVineType(BlockState blockState) {
        return detectVineType(blockState) != null;
    }

    /**
     * Check if the block at a specific position is any vine type
     * 
     * @param level The world/level
     * @param pos The position to check
     * @return true if it's any recognized vine type, false otherwise
     */
    public static boolean isAnyVineType(Level level, BlockPos pos) {
        return detectVineType(level, pos) != null;
    }

    /**
     * Get a human-readable description of the vine type
     * 
     * @param vineType The vine type to describe
     * @return A descriptive string, or "Unknown" if null
     */
    public static String getVineTypeDescription(VineType vineType) {
        if (vineType == null) {
            return "Unknown";
        }
        
        switch (vineType) {
            case REGULAR_VINE:
                return "Regular Vine";
            case CAVE_VINE:
                return "Cave Vine (Main)";
            case CAVE_VINE_PLANT:
                return "Cave Vine (Plant)";
            default:
                return "Unknown Vine Type";
        }
    }
}