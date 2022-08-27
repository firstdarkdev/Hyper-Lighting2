package me.hypherionmc.hyperlighting.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.hypherionmc.hyperlighting.common.blockentities.AdvancedCampfireBlockEntity;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedCampfire;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class AdvancedCampfireRenderer implements BlockEntityRenderer<AdvancedCampfireBlockEntity> {

    private static final float SIZE = 0.375F;
    private final ItemRenderer itemRenderer;

    public AdvancedCampfireRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(AdvancedCampfireBlockEntity campfire, float tick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int overlay) {
        Direction direction = campfire.getBlockState().getValue(AdvancedCampfire.FACING);
        NonNullList<ItemStack> items = campfire.getItems();
        int blockPos = (int)campfire.getBlockPos().asLong();

        for(int i = 0; i < items.size(); ++i) {
            ItemStack $$10 = items.get(i);
            if ($$10 != ItemStack.EMPTY) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.44921875, 0.5);
                Direction direction1 = Direction.from2DDataValue((i + direction.get2DDataValue()) % 4);
                float rot = -direction1.toYRot();
                poseStack.mulPose(Vector3f.YP.rotationDegrees(rot));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                poseStack.translate(-0.3125, -0.3125, 0.0);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderStatic($$10, ItemTransforms.TransformType.FIXED, combinedLight, overlay, poseStack, bufferSource, blockPos + i);
                poseStack.popPose();
            }
        }
    }
}
