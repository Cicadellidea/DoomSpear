package com.Cicadellidea.doom_spear.tracker;

import com.Cicadellidea.doom_spear.DoomSpear;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.client.key.KeyBindings;
import com.Cicadellidea.doom_spear.lib.FunctionLib;
import com.Cicadellidea.doom_spear.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientInputReader {
    // 记录上一帧状态，用来做节流
    private int lastForwardState = 0;
    @SubscribeEvent
    public void clientForwardTick(TickEvent.ClientTickEvent event){
        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if(player == null) return;
        int nowfwState = 0;
        if (player.input.up){
            nowfwState = nowfwState +1;
        }
        if (player.input.down){
            nowfwState = nowfwState -1;
        }


        // 只有状态发生变化才发包，不要每tick发送！
        if(nowfwState != lastForwardState){
            lastForwardState = nowfwState;
            DoomSpear.CHANNEL.sendToServer(new PlayerForwardPacket(nowfwState));
        }
    }

    private int lrState = 0;
    @SubscribeEvent
    public void clientLRTick(TickEvent.ClientTickEvent event){
        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) return;
        int nowlrState = 0;
        if (player.input.left){
            nowlrState = nowlrState +1;
        }
        if (player.input.right){
            nowlrState = nowlrState -1;
        }

        if(nowlrState != lrState){
            lrState = nowlrState;
            DoomSpear.CHANNEL.sendToServer(new PlayerLRPacket(nowlrState));

        }
    }

    @SubscribeEvent
    public void clientSweep(TickEvent.ClientTickEvent event) {
//        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) return;
        if(FunctionLib.hasChainSpear(player)){
            while(KeyBindings.sweepKey.consumeClick()) {
                DoomSpear.CHANNEL.sendToServer(new SweepAttackPacket());
            }
        }
    }

    @SubscribeEvent
    public void clientHook(TickEvent.ClientTickEvent event) {
//        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) return;
        if(FunctionLib.hasChainSpear(player)){
            while(KeyBindings.hookKey.consumeClick()) {
                DoomSpear.CHANNEL.sendToServer(new ShootHookPacket());
            }
        }
    }

    @SubscribeEvent
    public void clientDash(TickEvent.ClientTickEvent event) {
//        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) return;
        if(FunctionLib.hasChainSpear(player)){
            while(KeyBindings.dashKey.consumeClick()) {
                DoomSpear.CHANNEL.sendToServer(new DashPacket());
            }
        }
    }

    @SubscribeEvent
    public void clientSlam(TickEvent.ClientTickEvent event) {
//        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) return;
        if(FunctionLib.hasChainSpear(player)){
            while(KeyBindings.slamKey.consumeClick()) {


                DoomSpear.CHANNEL.sendToServer(new SlamPacket());
                var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
//                player.sendSystemMessage(Component.literal(String.valueOf(playerData.getSpearCharge())));
//                player.sendSystemMessage(Component.literal(String.valueOf(player.getId())));

//                player.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(0,0,0));
            }
        }
    }

//
    private boolean lastLeftClickState = false;
    @SubscribeEvent
    public void clientLeftClick(TickEvent.ClientTickEvent event){
        if(event.phase != TickEvent.Phase.END) return;
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if(player == null) return;
        var nowState = mc.mouseHandler.isLeftPressed();

        // 只有状态发生变化才发包，不要每tick发送！
        if(nowState != lastLeftClickState){
            lastLeftClickState = nowState;
            DoomSpear.CHANNEL.sendToServer(new PlayerLeftHoldingPacket(nowState));
        }
    }
}
