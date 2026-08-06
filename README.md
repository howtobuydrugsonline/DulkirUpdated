# DulkirMod for Minecraft 26.1.2

A port of [DulkirMod-Fabric](https://github.com/inglettronald/DulkirMod-Fabric)
(a quality-of-life mod for Hypixel SkyBlock) to **Minecraft 26.1.2**.

Upstream targets Minecraft 1.21.11, which Hypixel dropped support for in 2026 —
so upstream builds no longer work on SkyBlock. This repository contains that
port, plus a port of the Jarvis library it depends on.

## What's in here

| Folder | What it is | License |
| --- | --- | --- |
| [`DulkirMod-Fabric/`](DulkirMod-Fabric) | The mod itself, ported to 26.1.2. Fork of [inglettronald/DulkirMod-Fabric](https://github.com/inglettronald/DulkirMod-Fabric). | MPL-2.0 |
| [`jarvis-fabric/`](jarvis-fabric) | HUD editor / config search library, ported to 26.1.2. Modified from [lineargraph/jarvis](https://github.com/lineargraph/jarvis). | LGPL-3.0-or-later |

Jarvis needed porting too: upstream has no Minecraft 26.x release, and the
non-remapping Loom toolchain used from 26.1 onward cannot consume its old jar.

## Building it yourself

**Requirements:** JDK 25 or newer. Minecraft 26.1 requires Java 25; this was
built and tested with JDK 26.

```bash
git clone https://github.com/<your-username>/<this-repo>.git
cd <this-repo>/DulkirMod-Fabric
./gradlew build
```

On Windows use `gradlew.bat` instead of `./gradlew`.

The finished mod lands in `DulkirMod-Fabric/build/libs/`, named something like
`DulkirMod-Fabric-1.1.4-26.1.2.jar`. Ignore the `-sources.jar` — that one is for
developers, not for playing.

Jarvis is built automatically as part of that command (via a Gradle composite
build) and **bundled into the output jar**, so the single jar above is the only
file you install.

## Installing it

1. Install [Fabric Loader](https://fabricmc.net/use/installer) 0.19.3+ for
   Minecraft 26.1.2.
2. Drop these into your `.minecraft/mods` folder:
   - the `DulkirMod-Fabric-*.jar` you built (or one from Releases)
   - [Fabric API](https://modrinth.com/mod/fabric-api) `0.155.2+26.1.2`
   - [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) `1.13.13+`

Cloth Config, Orbit and Jarvis are already bundled inside the mod jar — you do
not need to download those separately.

**Make sure Minecraft is set to use Java 25 or newer**, or the game will refuse
to load the mod. This is the most common reason it doesn't show up.

## Running it from source

```bash
cd DulkirMod-Fabric
./gradlew runClient
```

This launches without authentication, which is enough to reach the main menu but
not to join a server. To sign in, configure `~/.devauth/config.toml` and run
`./gradlew runClient -Pdevauth=true`.

## Credits and licensing

DulkirMod is by **inglettronald** and contributors (MPL-2.0). Jarvis is by
**Linnea Gräf** (LGPL-3.0-or-later). This repository is a modified redistribution
of both; each folder keeps its original license and a notice describing exactly
what was changed. See `DulkirMod-Fabric/LICENSE`, `jarvis-fabric/COPYING`, and
`jarvis-fabric/COPYING.LESSER`.

## Status

Verified: builds clean, the mod and Jarvis both load, all mixins apply, and
Minecraft reaches the title screen without errors.

Not verified: anything Hypixel-specific. Dungeon solvers, chat parsing, slayer
timers and ESP rendering can only be exercised on a live server with a real
account. Expect rough edges and please open an issue if you hit one.
