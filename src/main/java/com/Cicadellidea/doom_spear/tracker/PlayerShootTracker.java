package com.Cicadellidea.doom_spear.tracker;

import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.item.MachneGun;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerShootTracker {
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if(!player.isRemoved()) {
//            player.sendSystemMessage(Component.literal("LeftHolding"));
            if (!(player.getMainHandItem().getItem() instanceof MachneGun gun)) return;
            var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            if (playerData.isHoldingLeft()){
                gun.shoot(player);
//                gun.shoot(player);
            }
        }
    }

}