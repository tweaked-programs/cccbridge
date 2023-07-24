package cc.tweaked_programs.cccbridge.client.blockEntityRenderer;

import cc.tweaked_programs.cccbridge.client.animatronic.AnimatronicModel;
import cc.tweaked_programs.cccbridge.common.assistance.Randomness;
import cc.tweaked_programs.cccbridge.common.minecraft.block.AnimatronicBlock;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.AnimatronicBlockEntity;
import cc.tweaked_programs.cccbridge.common.modloader.CCCBridge;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static cc.tweaked_programs.cccbridge.client.animatronic.AnimatronicModel.createBodyLayer;

@Environment(EnvType.CLIENT)
public class AnimatronicBlockEntityRenderer implements BlockEntityRenderer<AnimatronicBlockEntity> {
    public static final ResourceLocation TEXTURE_BODY = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/body.png");

    public static final ResourceLocation TEXTURE_FACE_NORMAL = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_normal.png");
    public static final ResourceLocation TEXTURE_FACE_HAPPY = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_happy.png");
    public static final ResourceLocation TEXTURE_FACE_QUESTION = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_question.png");
    public static final ResourceLocation TEXTURE_FACE_SAD = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_sad.png");

    public static final ResourceLocation TEXTURE_FACE_CURSED = new ResourceLocation(CCCBridge.MOD_ID, "textures/entity/animatronic/face_creepy.png");

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
        if (blockEntity.getLevel() instanceof VirtualRenderWorld virtualRenderWorld)
            for (Map.Entry<BlockPos, BlockState> entry : virtualRenderWorld.blocksAdded.entrySet()) {
                BlockPos pos = entry.getKey();
                BlockState state = entry.getValue();

                if (state.getBlock() instanceof AnimatronicBlock && virtualRenderWorld.getBlockEntity(pos) == blockEntity)
                    this.model.hasJob(state.getValue(AnimatronicBlock.IS_DRIVER));
            }
        else
            this.model.hasJob(blockEntity.getBlockState().getValue(AnimatronicBlock.IS_DRIVER));


        // Render model
        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.model.renderType(TEXTURE_BODY));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Eyes
        vertexConsumer = bufferSource.getBuffer(this.model.renderType(Randomness.rareCreepiness() ? TEXTURE_FACE_CURSED : AnimatronicModel.getFace(blockEntity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, Randomness.lightFlickering(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Checkout
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(AnimatronicBlockEntity blockEntity) {
        return true;
    }
}
