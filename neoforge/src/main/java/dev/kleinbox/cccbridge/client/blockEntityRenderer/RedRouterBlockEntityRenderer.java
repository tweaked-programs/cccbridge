package dev.kleinbox.cccbridge.client.blockEntityRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.kleinbox.cccbridge.common.assistance.Randomness;
import dev.kleinbox.cccbridge.common.minecraft.block.RedRouterBlock;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.RedRouterBlockEntity;
import dev.kleinbox.cccbridge.common.modloader.CCCBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RedRouterBlockEntityRenderer implements BlockEntityRenderer<RedRouterBlockEntity> {
    private final Map<Integer, ResourceLocation> FACE_TEXTURE;
    public  RedRouterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        Map<Integer, ResourceLocation> faces = new HashMap<>();
        for (int i=0; i<=16; i++) {
            faces.put(i, ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "block/redrouter/face/"+i));
        }
        FACE_TEXTURE = faces;
    }

    @Override
    public void render(RedRouterBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState block = blockEntity.getBlockState();
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutout());
        int flickering = Randomness.lightFlickering();

        poseStack.pushPose();

        // Rotate
        poseStack.translate(0.5D, 0.5D, 0.5D);
        if (block.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            poseStack.mulPose(Axis.YP.rotationDegrees(getDir(block.getValue(BlockStateProperties.HORIZONTAL_FACING))));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        int id = 0;
        if (block.hasProperty(RedRouterBlock.FACE))
            id = block.getValue(RedRouterBlock.FACE);

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(getFace(id));

        poseStack.translate(0D, 0D, 2D/16D-0.01);

        buffer.addVertex(poseStack.last().pose(), 0 ,0 ,0)
                .setColor(0.7F, 0.7F, 0.7F, 1.0F)
                .setUv(sprite.getU1(), sprite.getV1())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(flickering)
                .setNormal(poseStack.last(), 0, 0, 0);

        buffer.addVertex(poseStack.last().pose(), 0 ,1 ,0)
                .setColor(0.7F, 0.7F, 0.7F, 1.0F)
                .setUv(sprite.getU1(), sprite.getV0())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(flickering)
                .setNormal(poseStack.last(), 0, 0, 0);

        buffer.addVertex(poseStack.last().pose(), 1 ,1 ,0)
                .setColor(0.7F, 0.7F, 0.7F, 1.0F)
                .setUv(sprite.getU0(), sprite.getV0())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(flickering)
                .setNormal(poseStack.last(), 0, 0, 0);

        buffer.addVertex(poseStack.last().pose(), 1 ,0 ,0)
                .setColor(0.7F, 0.7F, 0.7F, 1.0F)
                .setUv(sprite.getU0(), sprite.getV1())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(flickering)
                .setNormal(poseStack.last(), 0, 0, 0);

        poseStack.popPose();
    }

    public ResourceLocation getFace(int id) {
        return (id >= 1 && id <= 16) ? FACE_TEXTURE.get(id) : FACE_TEXTURE.get(0);
    }

    public int getDir(Direction dir) {
        return switch(dir) {
            case NORTH -> 0;
            case EAST -> 270;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        };
    }
}
