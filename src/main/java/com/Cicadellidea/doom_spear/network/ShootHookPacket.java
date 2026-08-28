package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.lib.FunctionLib;
import com.Cicadellidea.doom_spear.lib.SpearActions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShootHookPacket {

    public ShootHookPacket() {
    }

    // 编码
    public void encode(FriendlyByteBuf buf) {
        // 无任何数据，只作为信号包
    }

    // 解码
    public static ShootHookPacket decode(FriendlyByteBuf buf) {
        return new ShootHookPacket();
    }

    // 服务端处理
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            var player = context.getSender();
            if (player == null) return;
            if (player.isRemoved()) return;

            if (!FunctionLib.hasChainSpear(player)) {
                return;
            }

            SpearActions.ShootHook(player.level(),player);
        });
        context.setPacketHandled(true);
    }
}
