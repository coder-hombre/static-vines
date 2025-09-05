# Vine Type Detection Implementation

This document describes the implementation of the vine type detection utilities for the Static Vines mod.

## Overview

The vine type detection system consists of two main components:

1. **VineType Enum** - Defines the different types of vine blocks supported by the mod
2. **VineTypeDetector Utility Class** - Provides methods to identify and categorize vine blocks

## VineType Enum

The `VineType` enum defines three types of vine blocks:

- `REGULAR_VINE` - Standard wall-climbing vines (`minecraft:vine`)
- `CAVE_VINE` - Main cave vine blocks that hang from ceilings (`minecraft:cave_vines`)
- `CAVE_VINE_PLANT` - Extending parts of cave vines that grow downward (`minecraft:cave_vines_plant`)

### Key Methods

- `getBlockId()` - Returns the string identifier for the block
- `getBlock()` - Returns the Minecraft Block instance
- `matches(Block block)` - Checks if a given block matches this vine type
- `fromBlock(Block block)` - Static method to get VineType from a Block instance
- `isVineBlock(Block block)` - Static method to check if a block is any vine type

## VineTypeDetector Utility Class

The `VineTypeDetector` class provides utility methods for detecting and identifying vine blocks in various contexts.

### Detection Methods

- `detectVineType(Block block)` - Detect vine type from a Block instance
- `detectVineType(BlockState blockState)` - Detect vine type from a BlockState
- `detectVineType(Level level, BlockPos pos)` - Detect vine type at a world position

### Type-Specific Checks

- `isRegularVine(Block block)` - Check if block is a regular vine
- `isCaveVine(Block block)` - Check if block is any type of cave vine
- `isCaveVineMain(Block block)` - Check if block is the main cave vine block
- `isCaveVinePlant(Block block)` - Check if block is a cave vine plant
- `isAnyVineType(Block block)` - Check if block is any recognized vine type

### Utility Methods

- `getVineTypeDescription(VineType vineType)` - Get human-readable description of vine type

## Implementation Details

### Design Principles

1. **Null Safety** - All methods handle null inputs gracefully
2. **Performance** - Efficient block type checking using direct Block comparisons
3. **Extensibility** - Easy to add new vine types in the future
4. **Consistency** - Consistent API across all detection methods

### Error Handling

- Null inputs return `null` for detection methods and `false` for boolean checks
- Unknown block types are handled gracefully without exceptions
- The utility class constructor throws `UnsupportedOperationException` to prevent instantiation

## Usage Examples

```java
// Detect vine type from a block
VineType vineType = VineTypeDetector.detectVineType(someBlock);
if (vineType != null) {
    System.out.println("Found vine: " + VineTypeDetector.getVineTypeDescription(vineType));
}

// Check if a block is a regular vine
if (VineTypeDetector.isRegularVine(block)) {
    // Handle regular vine logic
}

// Check if a block is any type of cave vine
if (VineTypeDetector.isCaveVine(block)) {
    // Handle cave vine logic
}

// Check from world position
VineType vineType = VineTypeDetector.detectVineType(level, blockPos);
```

## Testing Notes

Unit testing for NeoForge mods requires the full Minecraft runtime environment, which is complex to set up for isolated unit tests. The implementation has been designed with testability in mind:

- Clear separation of concerns
- Null-safe methods
- Predictable behavior
- Comprehensive error handling

For testing in a development environment, the mod can be tested using the NeoForge development runs:
- `./gradlew runClient` - Test in client environment
- `./gradlew runServer` - Test in server environment

## Requirements Satisfied

This implementation satisfies the following requirements from the specification:

- **1.1, 1.2** - Supports detection of regular vine blocks for growth prevention
- **2.1, 2.2** - Supports detection of cave vine blocks (both main and plant types)
- **Extensibility** - Easy to add support for additional vine types from other mods
- **Performance** - Efficient detection suitable for event-driven growth prevention