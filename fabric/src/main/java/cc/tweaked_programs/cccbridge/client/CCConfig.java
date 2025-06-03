package cc.tweaked_programs.cccbridge.client;

import cc.tweaked_programs.cccbridge.client.minecraft.screen.ConfigScreen;
import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CCConfig extends ReflectiveConfig {
    public static final CCConfig CONFIG = CCConfig.createToml(FabricLoader.getInstance().getConfigDir(), "cccbridge", "client", CCConfig.class);

    @Comment("Whether the face of the Animatronics should flicker in dark")
    public final TrackedValue<Boolean> flickering = value(true);

    public ConfigScreen newScreen(@Nullable Screen parent) {
        return new ConfigScreen(this, parent);
    }
}