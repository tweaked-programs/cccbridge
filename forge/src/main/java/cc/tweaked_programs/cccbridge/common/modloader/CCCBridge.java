package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.client.CCConfig;
import cc.tweaked_programs.cccbridge.client.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.client.blockEntityRenderer.RedRouterBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.AnimatronicBlockEntity;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.RedRouterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CCCBridge.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCCBridge {
    public static final String MOD_ID = "cccbridge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @OnlyIn(Dist.CLIENT)
    public static final CCConfig CONFIG = new CCConfig(
            FMLPaths.CONFIGDIR.get().toString()
    );

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
        event.registerBlockEntityRenderer((BlockEntityType<RedRouterBlockEntity>) CCCRegistries.REDROUTER_BLOCK_ENTITY.get(), RedRouterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType<AnimatronicBlockEntity>) CCCRegistries.ANIMATRONIC_BLOCK_ENTITY.get(), AnimatronicBlockEntityRenderer::new);
    }
}
