package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.lib.FunctionLib;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DashPacket {

    public DashPacket() {
    }

    // 编码
    public void encode(FriendlyByteBuf buf) {
        // 无任何数据，只作为信号包
    }

    // 解码
    public static DashPacket decode(FriendlyByteBuf buf) {
        return new DashPacket();
    }

    // 服务端处理
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            var player = context.getSender();
            if (player == null) return;

            if (!FunctionLib.hasChainSpear(player)) {
                return;
            }

            var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            if (playerData.isHooking()){
                playerData.setHooking(false);
            }
            if (playerData.isFalling()){
                playerData.setFalling(false);
            }

            if (playerData.dash()){
                Vec3 direction = player.getViewVector(1F);
                double tempx;
                double tempz;
                int fw;
                if (playerData.getLrState() == 0 && playerData.getForward() == 0){
                    fw = 1;
                }
                else {
                    fw = playerData.getForward();
                }
                tempx = playerData.getLrState()*direction.z+fw*direction.x;
                tempz = -playerData.getLrState()*direction.x+fw*direction.z;
                Vec3 dV;
                if (player.onGround()){
                    dV = new Vec3(tempx, 0, tempz).normalize().scale(1.4)
                            .add(new Vec3(0,0.2,0));
                }
                else {
                    dV = new Vec3(tempx, 0, tempz).normalize().scale(0.7)
                            .add(new Vec3(0,0.1,0));
                }
                player.setDeltaMovement(player.getDeltaMovement().add(dV));

                player.hurtMarked = true;
                player.fallDistance = 0;
            }
//            player.sendSystemMessage(Component.literal(String.valueOf(playerData.getDashCharge())));
//            player.sendSystemMessage(Component.literal(String.valueOf(playerData.getDashImmune())));

        });
        context.setPacketHandled(true);
    }
}
