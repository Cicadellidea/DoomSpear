package com.Cicadellidea.doom_spear.client.render;

import com.Cicadellidea.doom_spear.DoomSpear;
import com.Cicadellidea.doom_spear.entity.RavagerBullet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RavagerBulletRenderer extends EntityRenderer<RavagerBullet> {
//    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft","textures/entity/fishing_hook.png");
//    private static final RenderType RENDER_TYPE;
    private static final ResourceLocation RAVAGER_BULLET_TEXTURE = new ResourceLocation(DoomSpear.MODID, "textures/entity/projectiles/ravager_bullet.png");

    private static final double VIEW_BOBBING_SCALE = 960.0;

    public RavagerBulletRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(RavagerBullet pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(0.01F, 0.01F, 0.01F);
        pMatrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = pMatrixStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutout(RAVAGER_BULLET_TEXTURE));
        vertex(vertexconsumer, matrix4f, matrix3f, 0xF000F0, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, 0xF000F0, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, 0xF000F0, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, matrix4f, matrix3f, 0xF000F0, 0.0F, 1, 0, 0);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);


    }

    @Override
    public ResourceLocation getTextureLocation(RavagerBullet ravagerBullet) {
        return RAVAGER_BULLET_TEXTURE;
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f p_254085_, Matrix3f p_253962_, int pLightmapUV, float pX, int pY, int pU, int pV) {
        pConsumer.vertex(p_254085_, pX - 0.5F, (float)pY - 0.5F, 0.0F).color(0, 255, 255, 0).uv((float)pU, (float)pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pLightmapUV).normal(p_253962_, 0.0F, 1.0F, 0.0F).endVertex();
    }


}