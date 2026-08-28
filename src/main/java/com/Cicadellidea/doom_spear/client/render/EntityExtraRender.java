package com.Cicadellidea.doom_spear.client.render;

import com.Cicadellidea.doom_spear.DoomSpear;
import com.Cicadellidea.doom_spear.capability.MobStunCapability;
import com.Cicadellidea.doom_spear.capability.MobStunCapabilityProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EntityExtraRender {
    private static final ResourceLocation CUBE_TEXTURE = new ResourceLocation(DoomSpear.MODID, "textures/util/test_cube.png");
    private static final ResourceLocation DIZZY_TEXTURE = new ResourceLocation(DoomSpear.MODID, "textures/util/dizzy.png");
//    private final TestCube cubeModel;



    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, ?> event) {
        Minecraft mc = Minecraft.getInstance();
        long gameTime;
        LivingEntity entity = event.getEntity();
        try (var level = entity.level()) {
            gameTime = level.getGameTime();

        }
        catch (Exception e){
            return;

        }
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        int packedLight = event.getPackedLight();
        final int FULL_BRIGHT = 0xF000F0;

        if (entity instanceof Player) {
            return;
        }

        // 缓存还没烘焙完成就直接退出，防止空指针
        if (ClientModelCache.DIZZY_ROOT == null) {
            return;
        }

        if (entity instanceof Mob mob){
            if (!entity.isRemoved()){
                MobStunCapability mobStunData;
                try {
                    mobStunData = mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).orElseThrow(RuntimeException::new);
                }
                catch (Exception e){
                    return;
                }
                var time = mobStunData.getStunTime();
                if (time>10){
                    Dizzy dizzyModel = new Dizzy(ClientModelCache.DIZZY_ROOT);

                    poseStack.pushPose();
                    {
                        // Y偏移移动到生物头顶
                        poseStack.translate(0.0D, entity.getBbHeight()-1, 0.0D);
                        poseStack.mulPose(Axis.YP.rotationDegrees((gameTime*4)%360));

                        var vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(DIZZY_TEXTURE));
                        dizzyModel.renderToBuffer(poseStack, vertexConsumer, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                    }
                    poseStack.popPose();
                }
            }
        }
    }

}
