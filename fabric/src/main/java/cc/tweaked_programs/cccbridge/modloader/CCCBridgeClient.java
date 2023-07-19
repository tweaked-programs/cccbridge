package cc.tweaked_programs.cccbridge.modloader;

import cc.tweaked_programs.cccbridge.Registries;
import cc.tweaked_programs.cccbridge.minecraft.blockEntity.AnimatronicBlockEntity;
import cc.tweaked_programs.cccbridge.minecraft.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.minecraft.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.minecraft.blockEntityRenderer.RedRouterBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.minecraft.entity.animatronic.AnimatronicEntity;
import cc.tweaked_programs.cccbridge.minecraft.entity.animatronic.AnimatronicModel;
import cc.tweaked_programs.cccbridge.minecraft.entity.animatronic.AnimatronicRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Environment(EnvType.CLIENT)
public class CCCBridgeClient implements ClientModInitializer {
    public static final String FABRIC_API = "fabric-api";
    @Override
    public void onInitializeClient() {
        // Block rendering
        BlockEntityRendererRegistry.register((BlockEntityType<AnimatronicBlockEntity>) Registries.ANIMATRONIC_BLOCK_ENTITY.get(), AnimatronicBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register((BlockEntityType<RedRouterBlockEntity>) Registries.REDROUTER_BLOCK_ENTITY.get(), RedRouterBlockEntityRenderer::new);

        // Register Animatronic Entity rendering stuff
        EntityType<AnimatronicEntity> entity = (EntityType<AnimatronicEntity>) Registries.ANIMATRONIC_ENTITY.get();
        EntityRendererRegistry.register(entity, AnimatronicRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(AnimatronicModel.LAYER_LOCATION, AnimatronicModel::createBodyLayer);
    }
}