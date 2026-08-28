package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerLeftHoldingPacket {

    private final Boolean isHoldingLeft;

    public PlayerLeftHoldingPacket(boolean isHoldingLeft) {
        this.isHoldingLeft = isHoldingLeft;
    }

    // 编码
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isHoldingLeft);
    }

    // 解码
    public static PlayerLeftHoldingPacket decode(FriendlyByteBuf buf) {
        return new PlayerLeftHoldingPacket(buf.readBoolean());
    }

    // 服务端处理逻辑
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var sender = ctx.get().getSender();
            if(sender == null) return;
            var playerData = sender.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            playerData.setHoldingLeft(isHoldingLeft);
        });
        ctx.get().setPacketHandled(true);
    }
}