package com.Cicadellidea.doom_spear.mixin;

import com.Cicadellidea.doom_spear.capability.MobStunCapabilityProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
//    @Shadow public abstract void remove(Entity.RemovalReason pReason);
    @Inject(
            method = "isEffectiveAi",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stunNoAi(CallbackInfoReturnable<Boolean> cir)
    {
        if ((Object) this instanceof Mob mob){
            Level level = mob.level();
            // 客户端直接放行，不要读取Capability！
            if (level.isClientSide())
            {
                return;
            }

            mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).ifPresent(data ->
            {
                if (data.getStunTime() > 0)
                {
                    // 眩晕：强制关闭AI
                    cir.setReturnValue(false);
                }
            });
        }

    }

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onTick(CallbackInfo ci){
        if ((Object) this instanceof Mob mob){
            Level level = mob.level();

            mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).ifPresent(data ->
            {
                data.countDown();
            });
        }

    }

}
