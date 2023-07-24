package cc.tweaked_programs.cccbridge.client.animatronic;

import cc.tweaked_programs.cccbridge.client.blockEntityRenderer.AnimatronicBlockEntityRenderer;
import cc.tweaked_programs.cccbridge.common.minecraft.block.AnimatronicBlock;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.AnimatronicBlockEntity;
import cc.tweaked_programs.cccbridge.common.modloader.CCCBridge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class AnimatronicModel<T extends AnimatronicBlockEntity> extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CCCBridge.MOD_ID, "animatronic"), "main");
    private static final float awesomeFactor = ((float)Math.PI / 180F);
    private final ModelPart root;
    private final ModelPart hat;
    private final ModelPart support;

    public AnimatronicModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root.getChild("root");
        this.support = root.getChild("support");
        this.hat = this.root.getChild("head").getChild("hat");
        hat.visible = false;
    }

    public void hasJob(boolean value) {
        hat.visible = value;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(8, 32).addBox(-1.0F, -7.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(26, 20).addBox(-6.0F, -3.5F, -1.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -1.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(52, 7).addBox(-2.05F, -0.85F, -2.6F, 4.15F, 1.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition nose_r1 = hat.addOrReplaceChild("nose_r1", CubeListBuilder.create().texOffs(40, 7).addBox(-1.975F, 0.0F, -4.4F, 3.95F, 0.5F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition face = head.addOrReplaceChild("face", CubeListBuilder.create().texOffs(40, 10).addBox(-5.0F, -9.0F, -5.025F, 10.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightarm = root.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -3.0F, -1.0F));

        PartDefinition leftarm = root.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(20, 20).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -3.0F, -1.0F));

        PartDefinition support = partdefinition.addOrReplaceChild("support", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

        PartDefinition upper_arm_r1 = support.addOrReplaceChild("upper_arm_r1", CubeListBuilder.create().texOffs(0, 47).addBox(7.5F, 2.775F, -6.725F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -7.0F, 7.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition lower_arm_r1 = support.addOrReplaceChild("lower_arm_r1", CubeListBuilder.create().texOffs(0, 46).addBox(7.5F, -1.0F, -5.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -0.25F, 7.5F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch) {
        if (entity.isMoving())
            entity.updateCurrentPoses(partialTicks);

        float facing = ((float) entity.getBlockState().getValue(AnimatronicBlock.PRECISE_FACING) + entity.getBodyPose().getY()) * awesomeFactor;

        this.root.getChild("head").xRot = entity.getHeadPose().getX() * awesomeFactor;
        this.root.getChild("head").yRot = entity.getHeadPose().getY() * awesomeFactor;
        this.root.getChild("head").zRot = entity.getHeadPose().getZ() * awesomeFactor;

        this.root.xRot = entity.getBodyPose().getX() * awesomeFactor;
        this.root.yRot = facing;
        this.root.zRot = entity.getBodyPose().getZ() * awesomeFactor;

        this.support.xRot = 0;
        this.support.yRot = facing;
        this.support.zRot = 0;

        this.root.getChild("leftarm").xRot = entity.getLeftArmPose().getX() * awesomeFactor;
        this.root.getChild("leftarm").yRot = entity.getLeftArmPose().getY() * awesomeFactor;
        this.root.getChild("leftarm").zRot = entity.getLeftArmPose().getZ() * awesomeFactor;

        this.root.getChild("rightarm").xRot = entity.getRightArmPose().getX() * awesomeFactor;
        this.root.getChild("rightarm").yRot = entity.getRightArmPose().getY() * awesomeFactor;
        this.root.getChild("rightarm").zRot = entity.getRightArmPose().getZ() * awesomeFactor;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.support.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static ResourceLocation getFace(AnimatronicBlockEntity be) {
        String face = be.getFace();

        if (face == null)
            return AnimatronicBlockEntityRenderer.TEXTURE_FACE_NORMAL;

        return switch (face) {
            case "normal" -> AnimatronicBlockEntityRenderer.TEXTURE_FACE_NORMAL;
            case "happy" -> AnimatronicBlockEntityRenderer.TEXTURE_FACE_HAPPY;
            case "question" -> AnimatronicBlockEntityRenderer.TEXTURE_FACE_QUESTION;
            case "sad" -> AnimatronicBlockEntityRenderer.TEXTURE_FACE_SAD;

            default -> AnimatronicBlockEntityRenderer.TEXTURE_FACE_NORMAL;
        };
    }
}