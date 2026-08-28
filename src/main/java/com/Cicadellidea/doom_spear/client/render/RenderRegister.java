package com.Cicadellidea.doom_spear.client.render;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.Cicadellidea.doom_spear.init.ModEntity.RAVAGER_BULLET;
import static com.Cicadellidea.doom_spear.init.ModEntity.SPEAR_HOOK;

@Mod.EventBusSubscriber
public class RenderRegister {
    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SPEAR_HOOK.get(), CustomHookRenderer::new);
        event.registerEntityRenderer(RAVAGER_BULLET.get(), RavagerBulletRenderer::new);
    }


}
