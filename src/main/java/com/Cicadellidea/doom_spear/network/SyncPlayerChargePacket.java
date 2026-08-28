package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPlayerChargePacket {

    private final int charge;

    public SyncPlayerChargePacket(int charge) {

        this.charge = charge;
    }

    public void encode(FriendlyByteBuf buf) {

        buf.writeInt(charge);
    }

    public static SyncPlayerChargePacket decode(FriendlyByteBuf buf) {
        return new SyncPlayerChargePacket(buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            var player = net.minecraft.client.Minecraft.getInstance().player;
            var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            playerData.setSpearCharge(charge);

        });
        context.setPacketHandled(true);
    }
}
