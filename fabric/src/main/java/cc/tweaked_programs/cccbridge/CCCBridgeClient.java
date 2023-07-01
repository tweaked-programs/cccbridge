package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.blockEntity.AnimatronicBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.blockEntityRenderer.RedRouterBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CCCBridgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register((BlockEntityType<AnimatronicBlockEntity>)CCCRegister.getBlockEntityType("animatronic_block"), AnimatronicBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register((BlockEntityType<RedRouterBlockEntity>)CCCRegister.getBlockEntityType("redrouter_block"), RedRouterBlockEntityRenderer::new);
    }
}
