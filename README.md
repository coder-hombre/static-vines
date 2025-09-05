# Static Vines

A NeoForge mod for Minecraft 1.21.1 that prevents vines and cave vines from naturally growing and spreading while preserving manual placement functionality.

## Features

- **Vine Growth Prevention**: Stops regular vines from growing and spreading naturally
- **Cave Vine Control**: Prevents cave vines from growing downward naturally  
- **Configurable Controls**: Individual settings for each vine type with runtime configuration reloading
- **Manual Placement Preserved**: Players can still manually place and use vines normally
- **Performance Optimized**: No impact on gameplay performance or server lag
- **Server Compatible**: Works seamlessly in multiplayer environments

## Configuration

The mod automatically creates a configuration file at `config/staticvines-common.toml` with the following options:

```toml
[vine_growth_prevention]
# Prevent regular vines from growing and spreading
prevent_vine_growth = true

# Prevent cave vines from growing downward
prevent_cave_vine_growth = true
```

**Configuration Features:**
- Changes are applied immediately without requiring a restart
- Set individual vine types to `false` to allow normal vanilla growth
- Set to `true` to completely prevent growth attempts for that vine type

## Installation

1. Download the latest release from the [releases page](https://github.com/foogly/static-vines/releases)
2. Place the `staticvines-1.0.0.jar` file in your `mods` folder
3. Launch Minecraft with NeoForge 1.21.1

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.206 or later
- **Java**: 21 or later

## Compatibility

This mod is designed to work alongside other mods without conflicts. It specifically targets vine growth mechanics while preserving all other vanilla functionality.

## Support

- **Issues**: [GitHub Issues](https://github.com/foogly/static-vines/issues)
- **Homepage**: [GitHub Repository](https://github.com/foogly/static-vines)

## Contributions

If you want to contribute changes to the mod, fork it and submit a PR.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

Built with NeoForge for Minecraft 1.21.1