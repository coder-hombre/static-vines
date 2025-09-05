# Design Document

## Overview

The Static Vines mod for NeoForge 1.21.1 will prevent vine and cave vine growth through event interception and configuration-based control. The mod will use NeoForge's event system to intercept block growth events and cancel them based on configuration settings. A simple configuration file will allow users to enable/disable growth prevention for different vine types.

## Architecture

The mod follows a standard NeoForge architecture with the following key components:

- **Main Mod Class**: Entry point and mod registration
- **Configuration System**: TOML-based configuration with automatic reloading
- **Event Handler**: Intercepts and processes block growth events
- **Vine Type Registry**: Manages different vine block types and their growth behaviors

The mod operates by:
1. Loading configuration on mod initialization
2. Registering event listeners for block growth events
3. Intercepting growth attempts and checking configuration
4. Canceling events when growth is disabled for specific vine types

## Components and Interfaces

### 1. Main Mod Class (`StaticVinesMod.java`)
- Annotated with `@Mod("staticvines")`
- Handles mod initialization
- Registers configuration and event handlers
- Manages mod lifecycle

### 2. Configuration Manager (`VineConfig.java`)
- Uses NeoForge's configuration system with TOML format
- Provides boolean settings for each vine type:
  - `preventVineGrowth` (default: true)
  - `preventCaveVineGrowth` (default: true)
- Supports runtime configuration reloading
- Validates configuration values

### 3. Growth Prevention Handler (`VineGrowthHandler.java`)
- Subscribes to `BlockEvent.NeighborNotifyEvent` and `BlockEvent.CropGrowEvent.Pre`
- Identifies vine-related growth attempts
- Checks configuration settings
- Cancels growth events when prevention is enabled
- Handles both regular vines and cave vines

### 4. Vine Type Detector (`VineTypeDetector.java`)
- Utility class to identify vine block types
- Handles detection of:
  - Regular vines (`minecraft:vine`)
  - Cave vines (`minecraft:cave_vines`, `minecraft:cave_vines_plant`)
- Extensible for future vine types or mod compatibility

## Data Models

### Configuration Structure
```toml
[vine_growth_prevention]
# Prevent regular vines from growing and spreading
prevent_vine_growth = true

# Prevent cave vines from growing downward
prevent_cave_vine_growth = true
```

### Vine Type Enumeration
```java
public enum VineType {
    REGULAR_VINE("minecraft:vine"),
    CAVE_VINE("minecraft:cave_vines"),
    CAVE_VINE_PLANT("minecraft:cave_vines_plant");
    
    private final String blockId;
}
```

## Error Handling

### Configuration Errors
- Invalid configuration values default to `true` (growth prevention enabled)
- Missing configuration file is automatically created with defaults
- Configuration parsing errors are logged with clear error messages
- Malformed TOML files trigger fallback to default values

### Event Handling Errors
- Event listener exceptions are caught and logged without crashing
- Failed growth prevention attempts are logged for debugging
- Unknown block types are ignored rather than causing errors

### Compatibility Issues
- Graceful handling of missing block types from other mods
- Safe event cancellation that doesn't interfere with other mod events
- Fallback behavior when NeoForge APIs change

## Testing Strategy

### Unit Testing
- Configuration loading and validation tests
- Vine type detection accuracy tests
- Event cancellation logic verification
- Configuration reload functionality tests

### Integration Testing
- In-game vine growth prevention verification
- Configuration file modification during runtime
- Compatibility testing with common mods
- Performance impact measurement

### Manual Testing Scenarios
1. **Basic Functionality**
   - Place vines and verify they don't grow when prevention is enabled
   - Verify manual vine placement still works
   - Test cave vine growth prevention in lush caves

2. **Configuration Testing**
   - Modify config file and verify changes apply without restart
   - Test with invalid configuration values
   - Verify default configuration creation

3. **Compatibility Testing**
   - Test with world generation mods
   - Verify behavior with other growth-related mods
   - Test in multiplayer environments

### Performance Considerations
- Event handlers use efficient block type checking
- Configuration access is cached to minimize file I/O
- Event cancellation occurs early to prevent unnecessary processing
- Minimal memory footprint with static utility methods

### Extensibility Design
- Vine type detection is modular for easy extension
- Configuration system can accommodate new vine types
- Event handling supports additional growth event types
- Plugin-friendly architecture for mod compatibility