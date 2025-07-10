package dev.kleinbox.cccbridge.client.blockEntityRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kleinbox.cccbridge.client.animatronic.AnimatronicModel;
import dev.kleinbox.cccbridge.common.assistance.Randomness;
import dev.kleinbox.cccbridge.common.minecraft.block.AnimatronicBlock;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.AnimatronicBlockEntity;
import dev.kleinbox.cccbridge.common.modloader.CCCBridge;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static dev.kleinbox.cccbridge.client.animatronic.AnimatronicModel.createBodyLayer;

@OnlyIn(Dist.CLIENT)
public class AnimatronicBlockEntityRenderer implements BlockEntityRenderer<AnimatronicBlockEntity> {
    public static final ResourceLocation TEXTURE_BODY = ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "textures/entity/animatronic/body.png");

    public static final ResourceLocation TEXTURE_FACE_NORMAL = ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "textures/entity/animatronic/face_normal.png");
    public static final ResourceLocation TEXTURE_FACE_HAPPY = ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "textures/entity/animatronic/face_happy.png");
    public static final ResourceLocation TEXTURE_FACE_QUESTION = ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "textures/entity/animatronic/face_question.png");
    public static final ResourceLocation TEXTURE_FACE_SAD = ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "textures/entity/animatronic/face_sad.png");

    public static final ResourceLocation TEXTURE_FACE_CURSED = ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "textures/entity/animatronic/face_creepy.png");

    private static final float scale = 1F/32F*(32F-4F);
    private final AnimatronicModel<AnimatronicBlockEntity> model = new AnimatronicModel<>(createBodyLayer().bakeRoot());

    public AnimatronicBlockEntityRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(@NotNull AnimatronicBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.scale(-scale, -scale, scale);
        poseStack.translate(-0.5666F,  -1.5F, 0.5666F);

        this.model.setupAnim(blockEntity, 0.0F, 0.0F, partialTick, 0.0F, 0.0F);

        // Train hat
        this.model.hasJob(blockEntity.getBlockState().getValue(AnimatronicBlock.IS_DRIVER));

        // Render model
        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.model.renderType(TEXTURE_BODY));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

        // Eyes
        vertexConsumer = bufferSource.getBuffer(this.model.renderType(Randomness.rareCreepiness() ? TEXTURE_FACE_CURSED : AnimatronicModel.getFace(blockEntity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, Randomness.lightFlickering(), OverlayTexture.NO_OVERLAY);

        // Checkout
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(AnimatronicBlockEntity blockEntity) {
        return true;
    }
}
