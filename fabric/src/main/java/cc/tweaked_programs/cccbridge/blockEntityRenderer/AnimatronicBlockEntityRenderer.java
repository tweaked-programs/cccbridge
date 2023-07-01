package cc.tweaked_programs.cccbridge.blockEntityRenderer;

import cc.tweaked_programs.cccbridge.blockEntity.AnimatronicBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class AnimatronicBlockEntityRenderer implements BlockEntityRenderer<AnimatronicBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;
    private static final float scale = 1F/32F*(32F-4F);

    public AnimatronicBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(@NotNull AnimatronicBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.scale(scale, scale, scale);

        Entity entity = blockEntity.getAnimatronic();
        this.entityRenderer.render(entity, 0.575D, 0.15D, 0.575D, 0.0F, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}
