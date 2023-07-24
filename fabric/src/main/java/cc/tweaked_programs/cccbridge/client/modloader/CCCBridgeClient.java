package cc.tweaked_programs.cccbridge.client.modloader;

import cc.tweaked_programs.cccbridge.client.CCConfig;
import cc.tweaked_programs.cccbridge.client.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.client.blockEntityRenderer.RedRouterBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.AnimatronicBlockEntity;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.RedRouterBlockEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Environment(EnvType.CLIENT)
public class CCCBridgeClient implements ClientModInitializer {
    public static final CCConfig ONFIG = new CCConfig(
            FabricLoader.getInstance().getConfigDir().toString()
    );

    @Override
    public void onInitializeClient() {
        // Block rendering
        BlockEntityRendererRegistry.register((BlockEntityType<AnimatronicBlockEntity>) CCCRegistries.ANIMATRONIC_BLOCK_ENTITY.get(), AnimatronicBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register((BlockEntityType<RedRouterBlockEntity>) CCCRegistries.REDROUTER_BLOCK_ENTITY.get(), RedRouterBlockEntityRenderer::new);
    }
}