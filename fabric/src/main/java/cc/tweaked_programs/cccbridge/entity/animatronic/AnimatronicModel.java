package cc.tweaked_programs.cccbridge.entity.animatronic;

import cc.tweaked_programs.cccbridge.CCCRegister;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;

public class AnimatronicModel<T extends AnimatronicEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CCCRegister.getEntityTypeId("animatronic"), "main");
    private static final float awesomeFactor = ((float)Math.PI / 180F);
    private final ModelPart root;

    public AnimatronicModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(8, 32).addBox(-1.0F, -6.0F, -3.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-3.0F, -4.0F, -4.5F, 6.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(26, 20).addBox(-6.0F, -2.5F, -3.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -2.5F));

        PartDefinition rightarm = root.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -2.0F, -2.5F));

        PartDefinition leftarm = root.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(20, 20).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -2.0F, -2.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch) {
        if (entity.isMoving())
            entity.updateCurrentPoses(partialTicks);

        this.root.getChild("head").xRot = entity.getHeadPose().getX() * awesomeFactor;
        this.root.getChild("head").yRot = entity.getHeadPose().getY() * awesomeFactor;
        this.root.getChild("head").zRot = entity.getHeadPose().getZ() * awesomeFactor;

        this.root.yRot = entity.getBodyPose().getY() * awesomeFactor;
        this.root.xRot = entity.getBodyPose().getX() * awesomeFactor;
        this.root.zRot = entity.getBodyPose().getZ() * awesomeFactor;

        this.root.getChild("leftarm").xRot = entity.getLeftArmPose().getX() * awesomeFactor;
        this.root.getChild("leftarm").yRot = entity.getLeftArmPose().getY() * awesomeFactor;
        this.root.getChild("leftarm").zRot = entity.getLeftArmPose().getZ() * awesomeFactor;

        this.root.getChild("rightarm").xRot = entity.getRightArmPose().getX() * awesomeFactor;
        this.root.getChild("rightarm").yRot = entity.getRightArmPose().getY() * awesomeFactor;
        this.root.getChild("rightarm").zRot = entity.getRightArmPose().getZ() * awesomeFactor;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}