# Jarvis CCI — Minecraft 26.1 Fabric port

> **This is a modified version of Jarvis.**
>
> Modified on 2026-08-06, based on [lineargraph/jarvis](https://github.com/lineargraph/jarvis)
> at commit `3dedb72` (the last upstream release, targeting Minecraft 1.21.10).
> Upstream has no Minecraft 26.x release.
>
> Changes made in this fork:
> - Ported to Minecraft 26.1.2 (Fabric).
> - Restructured from a multi-module Architectury project into a single Fabric
>   module. **The Forge platform was dropped** — this fork is Fabric-only.
> - Migrated to the non-remapping `net.fabricmc.fabric-loom` toolchain
>   (Gradle 9.5.1, Java 25) required from Minecraft 26.1 onward.
> - Updated for 26.1 API changes: `ResourceLocation`→`Identifier`,
>   `GuiGraphics`→`GuiGraphicsExtractor`, `Screen.render`→`extractRenderState`,
>   `drawCenteredString`→`centeredText`, `submitOutline`→`outline`,
>   `displayClientMessage`→`sendSystemMessage`, and Fabric's
>   `keybinding.v1.KeyBindingHelper`→`keymapping.v1.KeyMappingHelper`.
>
> Jarvis is licensed LGPL-3.0-or-later; this modified version remains under the
> same license. See `COPYING` and `COPYING.LESSER`.
>
> Original author: Linnea Gräf. Upstream: https://nea.moe/projects/jarvis/

> Common Config Index - A search engine for your minecraft mod configs.

This README currently is more *sollen* instead of *sein*.

## For Users

Jarvis is a search engine for all your minecraft mods (that support Jarvis). That means you can search for config
options in one place and get a direct link that opens that mods config menu at the correct position. Furthermore, Jarvis
has a HUD editor that allows you to edit the HUDs of all mods (that support Jarvis).

### Installation

Just grab the latest forge or fabric version and put it in your mod folder. Jarvis will automatically detect mods that
support it.

To open Jarvis in game, type `/jarvis` or press the "Jarvis" button inside the pause menu.


## For Developers

To support Jarvis just depend on `moe.nea.jarvis:jarvis-api:<version>`.

Everything starts with a `JarvisPlugin` that you need to implement and register.

(Example for groovy, note that version might be different!)
```
repositories {
	maven { url "https://repo.nea.moe/releases/" }
}

dependencies {
    modImplementation("moe.nea.jarvis:jarvis-api:2.0.0")
	include("moe.nea.jarvis:jarvis-fabric:2.0.0")
	modLocalRuntime("moe.nea.jarvis:jarvis-fabric:2.0.0")
}
```

```
dependencies {
    modImplementation("moe.nea.jarvis:jarvis-api:2.0.0")
	include("moe.nea.jarvis:jarvis-forge:2.0.0")
	modLocalRuntime("moe.nea.jarvis:jarvis-forge:2.0.0")
}
```

### Fabric

For fabric you just need to add a `jarvis` entrypoint in your `fabric.mod.json`, pointing to a **class** implementing
`moe.nea.jarvis.api.JarvisPlugin`.

```json
{
  "entrypoints": {
    "client": [
      "my.mod.ClientInit"
    ],
    "jarvis": [
      "my.mod.MyJarvisPlugin"
    ]
  }
}
```

### Forge

For Forge you register an IMC enqueue listener like so:

```java
@Mod("mymod")
public class MyMod {
    public MyMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEnqueue);
    }

    public void onEnqueue(InterModEnqueueEvent event) {
        InterModComms.sendTo(JarvisConstants.MODID, JarvisConstants.IMC_REGISTER_PLUGIN, () -> MyJarvisPlugin.class);
    }
}
```

