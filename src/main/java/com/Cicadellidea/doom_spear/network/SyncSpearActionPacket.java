package com.Cicadellidea.doom_spear.network;

import com.Cicadellidea.doom_spear.lib.SpearActions;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSpearActionPacket
{
    private final int entityId;
    //你要同步的数据，例如旋转角度、标记、计时器
    private final int actionType;
    public static final int SWEEP = 1;
    public static final int SLAM = 2;

    public SyncSpearActionPacket(int entityId, int actionType)
    {
        this.entityId = entityId;
        this.actionType = actionType;
    }

    //编码：服务端写出
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeInt(actionType);
    }

    //解码：客户端读入
    public static SyncSpearActionPacket decode(FriendlyByteBuf buf)
    {
        int id = buf.readInt();
        int time = buf.readInt();
        return new SyncSpearActionPacket(id, time);
    }

    //客户端处理数据包
    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if(mc.level == null) return;
            Entity entity = mc.level.getEntity(entityId);
            if(entity != null) {
                if(entity instanceof Player player) {
                    if(!entity.isRemoved()) {
                        if (actionType == SWEEP){
                            SpearActions.doSweeping(player);
                        }
                        if (actionType == SLAM){
                            SpearActions.doSlam(player);
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
