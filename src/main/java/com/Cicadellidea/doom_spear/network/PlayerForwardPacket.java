package com.Cicadellidea.doom_spear.network;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerForwardPacket {
    private final int isPressingForward;

    public PlayerForwardPacket(int isPressingForward) {
        this.isPressingForward = isPressingForward;
    }

    // 编码
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(isPressingForward);
    }

    // 解码
    public static PlayerForwardPacket decode(FriendlyByteBuf buf) {
        return new PlayerForwardPacket(buf.readInt());
    }

    // 服务端处理逻辑
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var sender = ctx.get().getSender();
            if(sender == null) return;

//            if(isPressingForward){
//                // 按住前进，添加发光效果，1tick，立即刷新
//                sender.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false));
//
//            }else{
//                // 松开，移除发光
//                sender.removeEffect(MobEffects.GLOWING);
//            }
            var playerData = sender.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            playerData.setForward(isPressingForward);
        });
        ctx.get().setPacketHandled(true);
    }
}