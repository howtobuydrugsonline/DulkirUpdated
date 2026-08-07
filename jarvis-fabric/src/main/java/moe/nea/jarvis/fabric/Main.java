package moe.nea.jarvis.fabric;

import moe.nea.jarvis.api.JarvisPlugin;
import moe.nea.jarvis.impl.JarvisContainer;
import moe.nea.jarvis.impl.JarvisUtil;
import moe.nea.jarvis.impl.LoaderSupport;
import moe.nea.jarvis.impl.test.TestPluginClass;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class Main implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("jarviscci");

    @Override
    public void onInitializeClient() {
        List<JarvisPlugin> jarvisPlugins = FabricLoader.getInstance().getEntrypoints("jarvis", JarvisPlugin.class);
        JarvisContainer container = JarvisContainer.init(new LoaderSupport() {
            @Override
            public Optional<Component> getModName(String modid) {
                return FabricLoader.getInstance().getModContainer(modid).map(it -> Component.literal(it.getMetadata().getName()));
            }
        });
        container.plugins.addAll(jarvisPlugins);

        // Other mods ship their own copy of Jarvis shaded into their jar (Firmament,
        // for one). Fabric's key-mapping API rejects a second registration of the
        // same ID by throwing, which from 26.1 onward kills the whole client during
        // startup. Losing a keybind is not worth crashing the game over: skip it and
        // carry on, since /jarvis gui opens the same editor.
        KeyMapping hudKeybind = null;

        try {
            hudKeybind = KeyMappingHelper.registerKeyMapping(container.hudKeyBinding);
        } catch (IllegalArgumentException | IllegalStateException e) {
            LOGGER.warn(
                "Could not register the Jarvis HUD editor keybind - another mod has most likely "
                    + "registered its own bundled copy of Jarvis already. The keybind is disabled; "
                    + "use /jarvis gui instead.", e);
        }

        if (hudKeybind != null) {
            final KeyMapping boundKey = hudKeybind;
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (boundKey.consumeClick()) {
                    container.hudKeyBindingPressed();
                }
            });
        }
        if (!JarvisUtil.isTest)
            container.plugins.removeIf(it -> it instanceof TestPluginClass);
        container.finishLoading();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            container.registerCommands(dispatcher);
        });
    }
}
