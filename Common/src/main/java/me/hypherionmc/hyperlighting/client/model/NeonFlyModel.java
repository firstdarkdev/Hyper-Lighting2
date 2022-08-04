package me.hypherionmc.hyperlighting.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.hypherionmc.hyperlighting.Constants;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * @author HypherionSA
 * @date 04/08/2022
 */
public class NeonFlyModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "firefly"), "main");
    private final ModelPart head;
    private final ModelPart tail_light;
    private final ModelPart body;
    private final ModelPart right_wing_front;
    private final ModelPart right_wing_back;
    private final ModelPart left_front_wing;
    private final ModelPart left_back_wing;
    private final ModelPart left_front_leg;
    private final ModelPart left_middle_leg;
    private final ModelPart left_rear_leg;
    private final ModelPart right_front_leg;
    private final ModelPart right_middle_leg;
    private final ModelPart right_rear_leg;

    public NeonFlyModel(ModelPart root) {
        this.head = root.getChild("head");
        this.tail_light = root.getChild("tail_light");
        this.body = root.getChild("body");
        this.right_wing_front = root.getChild("right_wing_front");
        this.right_wing_back = root.getChild("right_wing_back");
        this.left_front_wing = root.getChild("left_front_wing");
        this.left_back_wing = root.getChild("left_back_wing");
        this.left_front_leg = root.getChild("left_front_leg");
        this.left_middle_leg = root.getChild("left_middle_leg");
        this.left_rear_leg = root.getChild("left_rear_leg");
        this.right_front_leg = root.getChild("right_front_leg");
        this.right_middle_leg = root.getChild("right_middle_leg");
        this.right_rear_leg = root.getChild("right_rear_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(21, 25).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(23, 6).addBox(-2.0F, 1.0F, -7.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 31).addBox(3.0F, -1.0F, -5.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -1.0F, -5.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, -2.0F));

        PartDefinition left_antenna = head.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(0, 38).addBox(-0.5F, -1.0174F, -0.5325F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -3.9826F, -2.4675F));
        PartDefinition cube_r1 = left_antenna.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(6, 0).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.6109F, 0.0F, 0.0F));
        PartDefinition right_antenna = head.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(34, 6).addBox(-0.5F, -1.0174F, -0.5325F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -3.9826F, -2.4675F));
        PartDefinition cube_r2 = right_antenna.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition tail_light = partdefinition.addOrReplaceChild("tail_light", CubeListBuilder.create().texOffs(0, 17).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 8.0F));
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 3.0F));

        /** Wings **/
        PartDefinition right_wing_front = partdefinition.addOrReplaceChild("right_wing_front", CubeListBuilder.create().texOffs(0, 38).addBox(-8.0F, 0.0F, -2.5F, 8.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 16.0F, 0.5F));
        PartDefinition right_wing_back = partdefinition.addOrReplaceChild("right_wing_back", CubeListBuilder.create().texOffs(33, 6).addBox(-8.0F, 0.0F, -2.5F, 8.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 16.0F, 5.5F));
        PartDefinition left_front_wing = partdefinition.addOrReplaceChild("left_front_wing", CubeListBuilder.create().texOffs(23, 0).addBox(0.0F, 0.0F, -2.5F, 8.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 16.0F, 0.5F));
        PartDefinition left_back_wing = partdefinition.addOrReplaceChild("left_back_wing", CubeListBuilder.create().texOffs(20, 17).addBox(0.0F, 0.0F, -2.5F, 8.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 16.0F, 5.5F));

        /** Legs **/
        PartDefinition left_front_leg = partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(33, 12).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 22.0F, -0.5F));
        PartDefinition left_middle_leg = partdefinition.addOrReplaceChild("left_middle_leg", CubeListBuilder.create().texOffs(14, 31).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 22.0F, 3.0F));
        PartDefinition left_rear_leg = partdefinition.addOrReplaceChild("left_rear_leg", CubeListBuilder.create().texOffs(9, 31).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 22.0F, 6.5F));
        PartDefinition right_front_leg = partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(20, 17).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 22.0F, -0.5F));
        PartDefinition right_middle_leg = partdefinition.addOrReplaceChild("right_middle_leg", CubeListBuilder.create().texOffs(0, 17).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 22.0F, 3.0F));
        PartDefinition right_rear_leg = partdefinition.addOrReplaceChild("right_rear_leg", CubeListBuilder.create().texOffs(23, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 22.0F, 6.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);

        this.right_wing_front.zRot = 0.0f;
        this.right_wing_back.zRot = 0.0f;
        this.left_front_wing.zRot = 0.0f;
        this.left_back_wing.zRot = 0.0f;

        boolean flag = entity.isOnGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7D;

        if (flag) {
            this.right_wing_front.zRot = -0.1f;
            this.left_front_wing.zRot = 0.1f;
            this.right_wing_back.zRot = 0.1f;
            this.left_back_wing.zRot = -0.1f;

            this.tail_light.xRot = 0.0f;
            this.tail_light.yRot = 0.0f;
            this.tail_light.zRot = 0.0f;
        } else {
            float f = ageInTicks * 120.32113F * ((float)Math.PI / 180F);
            this.right_wing_front.zRot = Mth.cos(f) * (float)Math.PI * 0.15F;
            this.left_front_wing.zRot = this.right_wing_front.zRot;

            this.right_wing_back.zRot = -Mth.cos(f) * (float)Math.PI * 0.15F;
            this.left_back_wing.zRot = this.right_wing_back.zRot;

            this.tail_light.xRot = -Mth.cos(f) * (float)Math.PI * 0.001F;
            this.tail_light.yRot = Mth.cos(f) * (float)Math.PI * 0.001F;
            this.tail_light.zRot = 0f;
        }

        if (entity.isOnGround()) {
            this.right_front_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.right_middle_leg.xRot = -Mth.cos(limbSwing * 0.6662F) * 1.3F * limbSwingAmount;
            this.right_rear_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;

            this.left_front_leg.xRot = -this.right_front_leg.xRot;
            this.left_middle_leg.xRot = this.right_middle_leg.xRot;
            this.left_rear_leg.xRot = -this.right_front_leg.xRot;
        } else {
            this.right_front_leg.xRot = 0.2f;
            this.right_middle_leg.xRot = 0.2f;
            this.right_rear_leg.xRot = 0.2f;
            this.left_front_leg.xRot = 0.2f;
            this.left_middle_leg.xRot = 0.2f;
            this.left_rear_leg.xRot = 0.2f;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay);
        tail_light.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, buffer, packedLight, packedOverlay);
        right_wing_front.render(poseStack, buffer, packedLight, packedOverlay);
        right_wing_back.render(poseStack, buffer, packedLight, packedOverlay);
        left_front_wing.render(poseStack, buffer, packedLight, packedOverlay);
        left_back_wing.render(poseStack, buffer, packedLight, packedOverlay);
        left_front_leg.render(poseStack, buffer, packedLight, packedOverlay);
        left_middle_leg.render(poseStack, buffer, packedLight, packedOverlay);
        left_rear_leg.render(poseStack, buffer, packedLight, packedOverlay);
        right_front_leg.render(poseStack, buffer, packedLight, packedOverlay);
        right_middle_leg.render(poseStack, buffer, packedLight, packedOverlay);
        right_rear_leg.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
