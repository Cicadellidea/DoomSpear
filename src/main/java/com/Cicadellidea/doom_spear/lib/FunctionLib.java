package com.Cicadellidea.doom_spear.lib;

import com.Cicadellidea.doom_spear.capability.MobStunCapability;
import com.Cicadellidea.doom_spear.capability.MobStunCapabilityProvider;
import com.Cicadellidea.doom_spear.init.ModDamageTypes;
import com.Cicadellidea.doom_spear.item.ChainSpearItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

import static com.Cicadellidea.doom_spear.init.ModDamageTypes.createCustomDamage;

public class FunctionLib {
    public static boolean hasChainSpear(Player player){
        ItemStack itemstack = player.getMainHandItem();
        ItemStack itemstack1 = player.getOffhandItem();
        boolean flag = itemstack.getItem() instanceof ChainSpearItem;
        boolean flag1 = itemstack1.getItem() instanceof ChainSpearItem;

        return flag||flag1;

    }
    public static boolean hasChainSpearInMain(Player player){
        ItemStack itemstack = player.getMainHandItem();
        boolean flag = itemstack.getItem() instanceof ChainSpearItem;
        return flag;

    }

    public static void stunMob(Mob mob,int time){
        if (mob.isRemoved()){return;}
        MobStunCapability mobStunData;
        try {
            mobStunData = mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).orElseThrow(RuntimeException::new);
        }
        catch (Exception e){
            return;
        }
        var presentStunTime = mobStunData.getStunTime();
        if (presentStunTime>0){
            if (presentStunTime<time){
                mobStunData.setStunTime(time);
            }
        }
        else {
            Brain<?> brain = mob.getBrain();
            brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            mobStunData.setStunTime(time);
            mob.setDeltaMovement(Vec3.ZERO);
            mob.hurtMarked = true;
            mob.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
        }
//        if (time >10){
////            DoomSpear.CHANNEL.sendTo(new EntitySyncPacket(mob.getId(),time),player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
//
//        }

    }

    public static void execution(Mob mob,Player player){
        if (mob.isRemoved()){return;}
        var mobStunData = mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).orElseThrow(RuntimeException::new);
        if(mobStunData.getStunTime()>5){
            if (mob.getHealth()*3<mob.getMaxHealth()){
                Level level = mob.level();
                DamageSource source = createCustomDamage(level, ModDamageTypes.SPEAR_DAMAGE, null, player);
//                DamageSource suource = DamageTypes.PLAYER_ATTACK;
                mobStunData.setStunTime(-1);
//                mob.setNoAi(mobStunData.isNoAi());
                mob.setLastHurtByPlayer(player);
                mob.setHealth(1e-5f);
                mob.hurt(source,20);


//                mob.kill();
            }
        };

    }

    public static boolean shouldHurt(Entity entity, Player player){
        if (entity.getType().getCategory().isFriendly()){
            return false;
        }
        if (entity instanceof Player victim){
            if (!player.canHarmPlayer(victim)){return false;}
        }
        return true;
    }
}
