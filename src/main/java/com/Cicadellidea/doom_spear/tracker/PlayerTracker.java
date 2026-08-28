package com.Cicadellidea.doom_spear.tracker;


import com.Cicadellidea.doom_spear.DoomSpear;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.lib.SpearActions;
import com.Cicadellidea.doom_spear.network.SyncPlayerChargePacket;
import com.Cicadellidea.doom_spear.network.SyncSpearActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;

public class PlayerTracker {
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event){
        if(event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (!player.isRemoved()){

            var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            playerData.tick();
            if (player instanceof ServerPlayer serverPlayer){
                if (playerData.isFalling()){
                    player.fallDistance = 0;
                    if (player.onGround()){
                        playerData.setFalling(false);
                        if (playerData.isCharged()){
                            if (SpearActions.doSlam(player)){
                                playerData.consumeCharge();
                                var level = player.level();
                                level.players().forEach(p -> {
                                    if (p instanceof ServerPlayer p1){
                                        DoomSpear.CHANNEL.sendTo(new SyncSpearActionPacket(player.getId(),SyncSpearActionPacket.SLAM),p1.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                                    }
                                });
//                                DoomSpear.CHANNEL.sendTo(new SyncSpearActionPacket(player.getId(),SyncSpearActionPacket.SLAM),serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                                DoomSpear.CHANNEL.sendTo(new SyncPlayerChargePacket(playerData.getSpearCharge()),serverPlayer.connection.connection,NetworkDirection.PLAY_TO_CLIENT);

                            }
                        }
                    }
                    else {
                        player.setDeltaMovement(player.getDeltaMovement().add(0,-0.1,0));
                    }
                }

            }

        }
//
    }

    @SubscribeEvent
    public void playerJoinEvent(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof Player player){
            if (!player.isRemoved()) {
                var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
                playerData.setHooking(false);
            }
        }
    }
    @SubscribeEvent
    public void playerHurtEvent(LivingAttackEvent event){
        if (event.getEntity() instanceof  ServerPlayer player){
            if (!player.level().isClientSide()){
                var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
                if (playerData.getDashImmune()>0){
//                    player.sendSystemMessage(Component.literal("doge"));
                    playerData.reCharge();
                    DoomSpear.CHANNEL.sendTo(new SyncPlayerChargePacket(playerData.getSpearCharge()),player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
//                    player.sendSystemMessage(Component.literal("sent"));
                    event.setCanceled(true);
                }
            }
        }

    }

}
