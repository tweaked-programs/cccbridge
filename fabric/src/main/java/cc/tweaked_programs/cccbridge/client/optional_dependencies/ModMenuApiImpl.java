package cc.tweaked_programs.cccbridge.client.optional_dependencies;

import cc.tweaked_programs.cccbridge.client.minecraft.screen.ConfigScreen;
import cc.tweaked_programs.cccbridge.client.modloader.CCCBridgeClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<ConfigScreen>) CCCBridgeClient.ONFIG::newScreen;
    }
}
