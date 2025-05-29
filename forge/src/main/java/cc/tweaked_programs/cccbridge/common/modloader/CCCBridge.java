package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.client.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.client.blockEntityRenderer.RedRouterBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CCCBridge.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCCBridge {
    public static final String MOD_ID = "cccbridge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public CCCBridge() {
        // Minecraft stuff
        CCCRegistries.register();
    }

    @SubscribeEvent
    public static void complete(FMLLoadCompleteEvent event) {
        event.enqueueWork(CCCRegistries::registerCompat);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CCCRegistries.REDROUTER_BLOCK_ENTITY.get(), RedRouterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CCCRegistries.ANIMATRONIC_BLOCK_ENTITY.get(), AnimatronicBlockEntityRenderer::new);
    }
}
