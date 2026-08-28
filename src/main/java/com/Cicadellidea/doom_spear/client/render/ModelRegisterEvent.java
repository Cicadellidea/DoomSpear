package com.Cicadellidea.doom_spear.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelRegisterEvent {
//    public static ModelPart CUBE_ROOT;


    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TestCube.LAYER_LOCATION, TestCube::createBodyLayer);
        event.registerLayerDefinition(Dizzy.LAYER_LOCATION,Dizzy::createBodyLayer);

    }

    @SubscribeEvent
    public static void bakeRoot(EntityRenderersEvent.AddLayers event) {
        // 一次性烘焙，存入全局缓存
        ClientModelCache.CUBE_ROOT = event.getEntityModels().bakeLayer(TestCube.LAYER_LOCATION);
        ClientModelCache.DIZZY_ROOT = event.getEntityModels().bakeLayer(Dizzy.LAYER_LOCATION);

    }
}