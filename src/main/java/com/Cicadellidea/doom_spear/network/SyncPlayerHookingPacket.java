package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.client.render.ViewSmoother;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPlayerHookingPacket {
    private final boolean isHooked;
    private final int id;

    public SyncPlayerHookingPacket(boolean isHooked,int id) {
        this.isHooked = isHooked;
        this.id = id;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isHooked);
        buf.writeInt(id);
    }

    public static SyncPlayerHookingPacket decode(FriendlyByteBuf buf) {
        return new SyncPlayerHookingPacket(buf.readBoolean(),buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            var player = net.minecraft.client.Minecraft.getInstance().player;

            // 运行在客户端线程
            ViewSmoother.isHooked = this.isHooked;
//            ViewSmoother.target = player.level().getEntity(id);
            ViewSmoother.angularVelocity = 7.1f;
            ViewSmoother.integalErr = 0;
            ViewSmoother.prevErr = 0;

        });
        context.setPacketHandled(true);
    }
}
