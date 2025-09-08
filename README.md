# Static Vines

A simple NeoForge mod for Minecraft 1.21.1 that disables random ticking for cave and weeping vines, preventing them from growing naturally.

## Features

- **Simple Implementation**: Disables random ticking for cave and weeping vine blocks to prevent growth
- **Covers Cave Vine Types**: Prevents growth for cave and weeping vines and cave vine plants
- **Manual Placement Preserved**: Players can still manually place and use cave and weeping vines normally
- **Lightweight**: Should actually save on performance by injectiong and telling minecraft that cave and weeping vines aren't randomly ticking and canceling them instead of trigering growth checks and block updates
- **Server Compatible**: Works seamlessly in multiplayer environments

## How It Works

The mod uses Mixin to directly override the `randomTick` method for cave and weeping vine block types by targeting `GrowingPlantHeadBlock`:
- Cave vines (glow berries)
- Cave vine plants
- Weeping vines

By canceling the random tick behavior, cave vine blocks can no longer grow or spread naturally. This is the most direct and efficient way to prevent cave vine growth.

No configuration is needed - the mod automatically disables random ticking for cave vine types.

## Installation

1. Download the latest release from the [releases page](https://github.com/foogly/static-vines/releases)
2. Place the `staticvines-1.0.0.jar` file in your `mods` folder
3. Launch Minecraft with NeoForge 1.21.1

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.206 or later
- **Java**: 21 or later

## Compatibility

This mod is designed to work alongside other mods without conflicts. It only affects the random ticking behavior of cave vine blocks and doesn't modify any other game mechanics.

## Note

This mod currently only prevents cave and weeping vine growth. Regular vines (the climbable ones that grow on walls) are not affected by this mod and will continue to grow naturally unless the game rule for vine spread is set to false.

## Support

- **Issues**: [GitHub Issues](https://github.com/foogly/static-vines/issues)
- **Homepage**: [GitHub Repository](https://github.com/foogly/static-vines)

## Contributions

If you want to contribute changes to the mod, fork it and submit a PR.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

Built with NeoForge for Minecraft 1.21.1