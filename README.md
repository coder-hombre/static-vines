# Static Vines

A simple NeoForge mod for Minecraft 1.21.1 that disables random ticking for vines and cave vines, preventing them from growing naturally.

## Features

- **Simple Implementation**: Disables random ticking for vine blocks to prevent growth
- **Covers All Vine Types**: Prevents growth for regular vines, cave vines, and cave vine plants
- **Manual Placement Preserved**: Players can still manually place and use vines normally
- **Lightweight**: Minimal performance impact using direct random tick cancellation
- **Server Compatible**: Works seamlessly in multiplayer environments

## How It Works

The mod uses Mixin to directly override the `randomTick` method for all vine block types:
- `VineBlock` (regular vines)
- `CaveVinesBlock` (cave vine main blocks)
- `CaveVinesPlantBlock` (cave vine plant blocks)

By making these methods do nothing, vine blocks can no longer grow or spread naturally. This is the most direct and efficient way to prevent vine growth.

No configuration is needed - the mod automatically disables random ticking for all vine types.

## Installation

1. Download the latest release from the [releases page](https://github.com/foogly/static-vines/releases)
2. Place the `staticvines-1.0.0.jar` file in your `mods` folder
3. Launch Minecraft with NeoForge 1.21.1

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.206 or later
- **Java**: 21 or later

## Compatibility

This mod is designed to work alongside other mods without conflicts. It only affects the random ticking behavior of vine blocks and doesn't modify any other game mechanics.

## Support

- **Issues**: [GitHub Issues](https://github.com/foogly/static-vines/issues)
- **Homepage**: [GitHub Repository](https://github.com/foogly/static-vines)

## Contributions

If you want to contribute changes to the mod, fork it and submit a PR.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

Built with NeoForge for Minecraft 1.21.1