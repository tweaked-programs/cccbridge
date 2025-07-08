package dev.kleinbox.cccbridge.client;

import dev.kleinbox.cccbridge.client.minecraft.screen.ConfigScreen;
import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class CCConfig extends ReflectiveConfig {
    public static final CCConfig CONFIG = CCConfig.createToml(FMLPaths.CONFIGDIR.get(), "cccbridge", "client", CCConfig.class);

    @Comment("Whether the face of the Animatronics should flicker in dark")
    public final TrackedValue<Boolean> flickering = value(true);
}