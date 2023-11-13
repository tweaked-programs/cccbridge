package cc.tweaked_programs.cccbridge.client.blockEntityRenderer;

import cc.tweaked_programs.cccbridge.common.assistance.Randomness;
import cc.tweaked_programs.cccbridge.common.minecraft.block.RedRouterBlock;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.common.modloader.CCCBridge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RedRouterBlockEntityRenderer implements BlockEntityRenderer<RedRouterBlockEntity> {
    private final Map<Integer, ResourceLocation> FACE_TEXTURE;
    public  RedRouterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        Map<Integer, ResourceLocation> faces = new HashMap<>();
        for (int i=0; i<=16; i++) {
            faces.put(i, new ResourceLocation(CCCBridge.MOD_ID, "block/redrouter/face/"+i));
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
        int sprite_size = sprite.contents().width();

        poseStack.translate(0D, 0D, 2D/16D-0.01);

        buffer.vertex(poseStack.last().pose(), 0 ,0 ,0)
                .color(0.7F, 0.7F, 0.7F, 1.0F)
                .uv(sprite.getU(sprite_size), sprite.getV(sprite_size))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(flickering)
                .normal(poseStack.last().normal(), 0, 0, 0).endVertex();

        buffer.vertex(poseStack.last().pose(), 0 ,1 ,0)
                .color(0.7F, 0.7F, 0.7F, 1.0F)
                .uv(sprite.getU(sprite_size), sprite.getV(0))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(flickering)
                .normal(poseStack.last().normal(), 0, 0, 0).endVertex();

        buffer.vertex(poseStack.last().pose(), 1 ,1 ,0)
                .color(0.7F, 0.7F, 0.7F, 1.0F)
                .uv(sprite.getU(0), sprite.getV(0))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(flickering)
                .normal(poseStack.last().normal(), 0, 0, 0).endVertex();

        buffer.vertex(poseStack.last().pose(), 1 ,0 ,0)
                .color(0.7F, 0.7F, 0.7F, 1.0F)
                .uv(sprite.getU(0), sprite.getV(sprite_size))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(flickering)
                .normal(poseStack.last().normal(), 0, 0, 0).endVertex();

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
