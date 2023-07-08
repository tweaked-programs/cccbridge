package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.blockEntity.AnimatronicBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.blockEntityRenderer.RedRouterBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicEntity;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicModel;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CCCBridgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register((BlockEntityType<AnimatronicBlockEntity>)CCCRegister.getBlockEntityType("animatronic_block"), AnimatronicBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register((BlockEntityType<RedRouterBlockEntity>)CCCRegister.getBlockEntityType("redrouter_block"), RedRouterBlockEntityRenderer::new);

        // Register Animatronic Entity rendering stuff
        EntityType<AnimatronicEntity> entity = (EntityType<AnimatronicEntity>)CCCRegister.getEntityType("animatronic");
        EntityRendererRegistry.register(entity, AnimatronicRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AnimatronicModel.LAYER_LOCATION, AnimatronicModel::createBodyLayer);
    }
}
