package cc.tweaked_programs.cccbridge.optional_dependencies;

import cc.tweaked_programs.cccbridge.modloader.CCCBridge;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.network.chat.Component;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigAPI::getScreen;
    }

    public static Component createModsButtonText() {
        CCCBridge.LOGGER.info(ModMenu.createModsButtonText().getString());
        return ModMenu.createModsButtonText();
    }
}
