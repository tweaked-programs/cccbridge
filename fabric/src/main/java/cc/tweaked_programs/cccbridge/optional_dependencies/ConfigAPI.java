package cc.tweaked_programs.cccbridge.optional_dependencies;

import cc.tweaked_programs.cccbridge.modloader.CCCBridge;
import cc.tweaked_programs.cccbridge.assistance.Config;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;

import static cc.tweaked_programs.cccbridge.modloader.CCCBridgeClient.FABRIC_API;

public class ConfigAPI {
    private static final String CONFIG_MOD =  "cloth-config";

    @Nullable
    public static Screen getScreen(Screen parent) {
        if (!(FabricLoader.getInstance().isModLoaded(CONFIG_MOD))) {
            CCCBridge.LOGGER.warn("Can't create config screen because the '"+CONFIG_MOD+"' API is missing.");
            return null;
        }

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("cccbridge.screen.config_screen"))
                .setSavingRunnable(() -> {
                    // Serialise the config into the config file. This will be called last after all variables are updated.
                });

        ConfigCategory visuals = builder.getOrCreateCategory(Component.translatable("category.cccbridge.visuals"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        visuals.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.cccbridge.flickering"), Config.FLICKERING)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.cccbridge.flickering.tooltip"))
                .setSaveConsumer(newValue -> Config.FLICKERING = newValue)
                .build());

        return builder.build();
    }
}
