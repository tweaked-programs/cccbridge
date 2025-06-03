package cc.tweaked_programs.cccbridge.client;

import folk.sisby.kaleido.api.ReflectiveConfig;
import cc.tweaked_programs.cccbridge.client.minecraft.screen.ConfigScreen;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class CCConfig extends ReflectiveConfig {
    public static final CCConfig CONFIG = CCConfig.createToml(FMLPaths.CONFIGDIR.get(), "cccbridge", "client", CCConfig.class);

    @Comment("Whether the face of the Animatronics should flicker in dark")
    public final TrackedValue<Boolean> flickering = value(true);

    public ConfigScreen newScreen(@Nullable Screen parent) {
        return new ConfigScreen(this, parent);
    }
}