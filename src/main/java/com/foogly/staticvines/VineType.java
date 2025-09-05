package com.foogly.staticvines;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Enumeration of different vine block types supported by the Static Vines mod.
 * 
 * This enum provides a centralized way to identify and categorize different
 * types of vine blocks that can be prevented from growing.
 */
public enum VineType {
    /**
     * Regular vines that grow on walls and can spread to adjacent blocks
     */
    REGULAR_VINE("minecraft:vine", Blocks.VINE),
    
    /**
     * Cave vines (the main block that hangs from cave ceilings)
     */
    CAVE_VINE("minecraft:cave_vines", Blocks.CAVE_VINES),
    
    /**
     * Cave vine plant (the extending part of cave vines that grows downward)
     */
    CAVE_VINE_PLANT("minecraft:cave_vines_plant", Blocks.CAVE_VINES_PLANT);

    private final String blockId;
    private final Block block;

    /**
     * Constructor for VineType enum
     * 
     * @param blockId The string identifier for the block (e.g., "minecraft:vine")
     * @param block The Minecraft Block instance
     */
    VineType(String blockId, Block block) {
        this.blockId = blockId;
        this.block = block;
    }

    /**
     * Get the string identifier for this vine type
     * 
     * @return The block ID string
     */
    public String getBlockId() {
        return blockId;
    }

    /**
     * Get the Minecraft Block instance for this vine type
     * 
     * @return The Block instance
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Check if the given block matches this vine type
     * 
     * @param block The block to check
     * @return true if the block matches this vine type, false otherwise
     */
    public boolean matches(Block block) {
        if (block == null) {
            return false;
        }
        
        try {
            return this.block == block;
        } catch (Exception e) {
            // Handle errors when comparing blocks (e.g., from other mods)
            StaticVines.LOGGER.warn("Error comparing block {} with vine type {}. Treating as non-match for stability.", 
                block, this.name(), e);
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Block comparison error details - vine type: {}, block: {}, error: {}", 
                    this.name(), block, e.getMessage());
            }
            return false; // Safe fallback - treat as non-match
        }
    }

    /**
     * Get a VineType from a Block instance
     * 
     * @param block The block to identify
     * @return The corresponding VineType, or null if not a recognized vine type
     */
    public static VineType fromBlock(Block block) {
        if (block == null) {
            return null;
        }
        
        try {
            for (VineType vineType : values()) {
                if (vineType.matches(block)) {
                    return vineType;
                }
            }
            return null; // Not a recognized vine type
        } catch (Exception e) {
            // Handle errors when comparing unknown block types
            StaticVines.LOGGER.warn("Error identifying vine type for block: {}. Treating as non-vine block for stability.", block, e);
            if (StaticVines.LOGGER.isDebugEnabled()) {
                StaticVines.LOGGER.debug("Block identification error details - block: {}, error class: {}", 
                    block, e.getClass().getSimpleName());
            }
            return null; // Treat problematic blocks as non-vine blocks
        }
    }

    /**
     * Check if a block is any type of vine
     * 
     * @param block The block to check
     * @return true if the block is a recognized vine type, false otherwise
     */
    public static boolean isVineBlock(Block block) {
        return fromBlock(block) != null;
    }
}