package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerLRPacket {
    private final int lrState;

    public PlayerLRPacket(int lrState) {
        this.lrState = lrState;
    }

    // 编码
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(lrState);
    }

    // 解码
    public static PlayerLRPacket decode(FriendlyByteBuf buf) {
        return new PlayerLRPacket(buf.readInt());
    }

    // 服务端处理逻辑
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var sender = ctx.get().getSender();
            if(sender == null) return;
//            sender.sendSystemMessage(Component.literal(String.valueOf(lrState)));
            var playerData = sender.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            playerData.setLrState(lrState);


        });
        ctx.get().setPacketHandled(true);
    }
}