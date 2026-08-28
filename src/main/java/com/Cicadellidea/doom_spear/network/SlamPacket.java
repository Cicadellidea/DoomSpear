package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.lib.FunctionLib;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SlamPacket {

    public SlamPacket() {
    }

    // 编码
    public void encode(FriendlyByteBuf buf) {
        // 无任何数据，只作为信号包
    }

    // 解码
    public static SlamPacket decode(FriendlyByteBuf buf) {
        return new SlamPacket();
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
                if (playerData.isCharged()){
                    if (playerData.isFalling()){
                        playerData.setFalling(false);
                    }
                    else {
                        playerData.setFalling(true);
                    }
                    player.fallDistance = 0;
                }
            }
        });
        context.setPacketHandled(true);
    }
}
