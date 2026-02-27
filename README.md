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

## Security

### Velocity Modern Forwarding (required)

Because Velocity runs in **online mode** while the backend servers run in **offline mode**, you
**must** enable Velocity Modern Forwarding so backends can verify that every connecting player was
authenticated by Mojang through the proxy. Without this, a player could bypass the proxy and
connect directly to a backend server using any username/UUID.

**Step 1 – `velocity.toml` (proxy)**

```toml
player-info-forwarding-mode = "MODERN"
```

**Step 2 – `config/paper-global.yml` (each backend server)**

```yaml
proxies:
  velocity:
    enabled: true
    online-mode: true
    secret: "YOUR_FORWARDING_SECRET"
```

Generate a strong random secret (e.g. `openssl rand -hex 32`) and use the **same value** in
every backend server's config and in `forwarding.secret` inside the Velocity proxy directory.

### Firewall / network binding

Backend servers must **not** be reachable directly from the internet.  Bind each backend to
`127.0.0.1` (or an internal LAN address) in its `server.properties`:

```properties
server-ip=127.0.0.1
```

Only the Velocity proxy port (default `25565`) should be exposed publicly.

### What this plugin does on top

* **Ban check (Paper side)** – the compass GUI cannot be opened and no transfer message is sent
  if the player is currently banned on the lobby server.
* **Online-mode guard (Velocity side)** – the proxy rejects any transfer request that arrives
  from a connection that was not authenticated in online mode (i.e. not verified by Mojang),
  and logs a warning with the player's UUID for auditing.
* **Transfer audit log (Velocity side)** – every successful server transfer is logged with the
  player's username **and UUID** so you have a clear, tamper-evident record.

