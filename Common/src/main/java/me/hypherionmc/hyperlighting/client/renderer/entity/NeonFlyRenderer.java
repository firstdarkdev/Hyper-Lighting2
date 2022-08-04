package me.hypherionmc.hyperlighting.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.client.model.NeonFlyModel;
import me.hypherionmc.hyperlighting.common.entities.NeonFlyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * @author HypherionSA
 * @date 04/08/2022
 */
public class NeonFlyRenderer extends MobRenderer<NeonFlyEntity, NeonFlyModel<NeonFlyEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/firefly.png");

    public NeonFlyRenderer(EntityRendererProvider.Context context) {
        super(context, new NeonFlyModel(context.bakeLayer(NeonFlyModel.LAYER_LOCATION)), 1f);
        this.addLayer(new FireflyTailLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(NeonFlyEntity pEntity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(NeonFlyEntity p_174146_, BlockPos p_174147_) {
        return 1;
    }

    @Override
    protected void scale(NeonFlyEntity pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        float f = 0.08f;
        this.shadowRadius = 0.1F;
        pMatrixStack.scale(f, f, f);
    }

    class FireflyTailLayer<T extends Entity, M extends NeonFlyModel<T>> extends EyesLayer<T, M> {
        private static final RenderType FIREFLY_TAIL = RenderType.eyes(new ResourceLocation(Constants.MOD_ID, "textures/entity/firefly_tail.png"));

        public FireflyTailLayer(RenderLayerParent<T, M> p_117507_) {
            super(p_117507_);
        }

        @Override
        public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            float[] color = ((NeonFlyEntity) pLivingEntity).getColor().getTextureDiffuseColors();
            VertexConsumer vertexconsumer = pBuffer.getBuffer(this.renderType());
            this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, color[0], color[1], color[2], 1.0f);
        }

        @Override
        public RenderType renderType() {
            return FIREFLY_TAIL;
        }
    }
}
