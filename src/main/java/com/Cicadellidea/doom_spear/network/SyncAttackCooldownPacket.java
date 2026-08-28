package com.Cicadellidea.doom_spear.network;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncAttackCooldownPacket {

    public SyncAttackCooldownPacket() {}

    public void encode(FriendlyByteBuf buf) {}

    public static SyncAttackCooldownPacket decode(FriendlyByteBuf buf) {
        return new SyncAttackCooldownPacket();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            // 运行在客户端线程
            var player = net.minecraft.client.Minecraft.getInstance().player;
            if(player != null) {
                // 客户端本地重置蓄力计数器，UI同步
                player.resetAttackStrengthTicker();
                player.swing(InteractionHand.MAIN_HAND);
            }
        });
        context.setPacketHandled(true);
    }
}
