package dev.kleinbox.cccbridge.common.modloader;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import dev.kleinbox.cccbridge.client.CCConfig;
import dev.kleinbox.cccbridge.client.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import dev.kleinbox.cccbridge.client.blockEntityRenderer.RedRouterBlockEntityRenderer;
import dev.kleinbox.cccbridge.client.minecraft.screen.ConfigScreen;
import dev.kleinbox.cccbridge.common.CCCRegistries;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.PeripheralBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.kleinbox.cccbridge.common.CCCRegistries.BLOCK_ENTITIES;

@Mod(CCCBridge.MOD_ID)
@EventBusSubscriber(modid = CCCBridge.MOD_ID)
public class CCCBridge {
    public static final String MOD_ID = "cccbridge";
    //public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public CCCBridge(IEventBus eventBus) {
        var ignored = CCConfig.CONFIG; // Just needs to be touched soon-ish for mcqoy to work
        CCCRegistries.register(eventBus);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientInit(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("mcqoy"))
            return;

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> new IConfigScreenFactory() {
            @Override
            public @NotNull Screen createScreen(@NotNull ModContainer modContainer, @NotNull Screen parent) {
                return new ConfigScreen(CCConfig.CONFIG, parent);
            }
        });
    }

    @SubscribeEvent
    public static void complete(FMLLoadCompleteEvent event) {
        event.enqueueWork(CCCRegistries::registerCompat);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        BLOCK_ENTITIES.getEntries().forEach((entry) -> {
            event.registerBlockEntity(
                    PeripheralCapability.get(),
                    entry.get(),
                    (be, side) -> {
                        if (be instanceof PeripheralBlockEntity provider)
                            return provider.getPeripheral(side);
                        return null;
                    }
            );
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CCCRegistries.REDROUTER_BLOCK_ENTITY.get(), RedRouterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CCCRegistries.ANIMATRONIC_BLOCK_ENTITY.get(), AnimatronicBlockEntityRenderer::new);
    }
}
