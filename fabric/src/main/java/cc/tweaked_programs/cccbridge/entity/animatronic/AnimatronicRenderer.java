package cc.tweaked_programs.cccbridge.entity.animatronic;

import cc.tweaked_programs.cccbridge.CCCBridge;
import cc.tweaked_programs.cccbridge.Misc;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AnimatronicRenderer extends EntityRenderer<AnimatronicEntity> {
    public static final ResourceLocation TEXTURE_BODY = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/body.png");

    public static final ResourceLocation TEXTURE_FACE_NORMAL = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_normal.png");
    public static final ResourceLocation TEXTURE_FACE_HAPPY = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_happy.png");
    public static final ResourceLocation TEXTURE_FACE_QUESTION = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_question.png");
    public static final ResourceLocation TEXTURE_FACE_SAD = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_sad.png");

    public static final ResourceLocation TEXTURE_FACE_CURSED = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_creepy.png");

    private final AnimatronicModel<AnimatronicEntity> model;

    public AnimatronicRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new AnimatronicModel<>(context.bakeLayer(AnimatronicModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AnimatronicEntity entity) {
        return TEXTURE_BODY;
    }

    @Override
    public void render(@NotNull AnimatronicEntity entity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        // Setup
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F,  -1.5F, 0.0F);

        this.model.setupAnim(entity, 0.0F, 0.0F, tickDelta, 0.0F, 0.0F);

        // Render model
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(TEXTURE_BODY));
        this.model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Eyes
        vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(Misc.rareCreepiness() ? TEXTURE_FACE_CURSED : entity.getFace()));
        this.model.renderToBuffer(poseStack, vertexConsumer, Misc.flickering(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Checkout
        poseStack.popPose();
        super.render(entity, yaw, tickDelta, poseStack, multiBufferSource, light);
    }
}