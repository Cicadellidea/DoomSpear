package com.Cicadellidea.doom_spear.tracker;

import com.Cicadellidea.doom_spear.capability.MobStunCapabilityProvider;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.Cicadellidea.doom_spear.DoomSpear.MODID;

@Mod.EventBusSubscriber
public class CapabilityTracker {
    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event)
    {
        event.register(PlayerExtraDataProvider.class);
        event.register(MobStunCapabilityProvider.class);
    }
    @SubscribeEvent
    public void onAttachPlayer(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(entity instanceof net.minecraft.world.entity.player.Player player) {
            if (!player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).isPresent()) {
                event.addCapability(
                        new ResourceLocation(MODID, "player_extra_data"),
                        new PlayerExtraDataProvider()
                );
            }
        }
    }
    @SubscribeEvent
    public void onAttachMob(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(entity instanceof net.minecraft.world.entity.Mob mob) {
            if (!mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).isPresent()) {
                event.addCapability(
                        new ResourceLocation(MODID, "mob_stun_data"),
                        new MobStunCapabilityProvider()
                );
            }
        }
    }
//    @SubscribeEvent
//    public void cloneCapability(PlayerEvent.Clone event) {
//        Player origin = event.getOriginal();
//
//        if(!origin.level().isClientSide) {
//            origin.reviveCaps();
//            Player newPlayer = event.getEntity();
//            var original = origin.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
//            var newInstance = newPlayer.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
//            newInstance.clone(original);
//        }
//    }
}
