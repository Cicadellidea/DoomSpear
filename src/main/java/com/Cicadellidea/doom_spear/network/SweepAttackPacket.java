package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.DoomSpear;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.lib.FunctionLib;
import com.Cicadellidea.doom_spear.lib.SpearActions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SweepAttackPacket {

    public SweepAttackPacket() {
    }

    // 编码
    public void encode(FriendlyByteBuf buf) {
        // 无任何数据，只作为信号包
    }

    // 解码
    public static SweepAttackPacket decode(FriendlyByteBuf buf) {
        return new SweepAttackPacket();
    }

    // 服务端处理
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            var player = context.getSender();
            if (player == null) return;

//            ItemStack stack = player.getMainHandItem();
            if (!FunctionLib.hasChainSpear(player)) {
                return;
            }
            var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);

            // 判断蓄力是否满
//            float strength = player.getAttackStrengthScale(0F);
//            if (strength < 0.94F) {
//                return;
//            }
            if (playerData.isCharged()){
                if (SpearActions.doSweeping(player)){
                    playerData.consumeCharge();
//                    var level = player.level();
//                    level.players().forEach(p -> {
//                        if (p instanceof ServerPlayer p1){
//                            DoomSpear.CHANNEL.sendTo(new SyncSpearActionPacket(player.getId(),SyncSpearActionPacket.SWEEP),p1.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
//                        }
//                    });
                    // 重置攻击蓄力条
    //                player.resetAttackStrengthTicker();
    //                DoomSpear.CHANNEL.sendTo(new SyncAttackCooldownPacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                    DoomSpear.CHANNEL.sendTo(new SyncPlayerChargePacket(playerData.getSpearCharge()),player.connection.connection,NetworkDirection.PLAY_TO_CLIENT);

                }
            }
        });
        context.setPacketHandled(true);
    }
}
