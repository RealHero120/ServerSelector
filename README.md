# ServerSelector

Lightweight server selector plugin for the **Wild Rose MC** network (Velocity proxy + Paper lobby).

## Modules

| Module | Target | Purpose |
|--------|--------|---------|
| `paper-plugin` | Paper lobby server | Gives players a compass that opens a server-selector GUI |
| `velocity-plugin` | Velocity proxy | Receives transfer requests and moves players between servers |

### How it works

1. A player joins the lobby → receives a **compass** in hotbar slot 0.
2. Right-clicking the compass opens a 9-slot **"Server Selector"** GUI.
3. Clicking an item in the GUI sends a plugin-message (`serverselector:connect`) to the Velocity proxy.
4. The Velocity plugin reads the message and transfers the player to the requested server.

Servers are hardcoded as `lobby`, `smp`, and `minigames` (matching the server names in `velocity.toml`).

## Requirements

- Java 21
- Paper 1.21.x
- Velocity 3.x

## Building

```bash
# Unix / macOS
./gradlew build

# Windows
gradlew.bat build
```

Output jars:

```
paper-plugin/build/libs/ServerSelector-Paper.jar
velocity-plugin/build/libs/ServerSelector-Velocity.jar
```

## Installation

1. Copy `ServerSelector-Paper.jar` to your lobby server's `plugins/` folder  
   *(e.g. `C:\Minecraft\lobby\plugins\` on the Wild Rose MC VPS)*
2. Copy `ServerSelector-Velocity.jar` to your Velocity proxy's `plugins/` folder  
   *(e.g. `C:\Minecraft\proxy\plugins\` on the Wild Rose MC VPS)*
3. **Restart** the Velocity proxy first, then restart the lobby server.

That's it — no configuration files are needed.
