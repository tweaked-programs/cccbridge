package cc.tweaked_programs.cccbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class CCCBridgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CCCBridge.REDROUTER_BLOCK, RenderLayer.getCutout());
    }
}
