package com.Cicadellidea.doom_spear.client.render;

import com.Cicadellidea.doom_spear.client.key.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class ViewSmoother {
    public static float targetXRot = 0;
    public static float targetYRot = 0;
    public static boolean isHooked = false;
    public static Entity target;
    public static float angularVelocity = 7.1f;
    public static float integalErr = 0;
    public static float prevErr = 0;
    public static float fov = 0;

//    public static float prevTick = 0;



    public static float lerpAngle(float from, float to, float factor) {
        float delta = ((to - from+180f) % 360f)-180f;
        return from + delta * factor;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) return;

        if (isHooked){
            if(KeyBindings.hookKey.isDown()){
                float partialTicks = mc.getFrameTime();

                float oldYawHead = player.yHeadRot;
                float oldYawOffset = player.yBodyRot;
                float oldPitch = player.getXRot();
                float oldYaw = player.getYRot();

                float prevYawHead = player.yHeadRotO;
                float prevYawOffset = player.yBodyRotO;
                float prevYaw = player.yRotO;
                float prevPitch = player.xRotO;
                int lrState = 0;
                if (player.input.left){
                    lrState += 1;
                }
                if (player.input.right){
                    lrState -= 1;
                }
                float yrot =prevYaw+lrState*angularVelocity*event.renderTickTime;

                player.setYRot(yrot);
                player.setYHeadRot(player.getYRot());
                player.setYBodyRot(player.getYRot());

                player.yBodyRot = oldYawOffset;
                player.yBodyRotO = prevYawOffset;
                player.yHeadRotO = prevYawHead;
                player.yRotO = prevYaw;


            }
        }

//        player.setXRot(player.getViewXRot(event.renderTickTime));
//        player.setYRot(player.getViewYRot(event.renderTickTime));


    }
}
