package com.Cicadellidea.doom_spear.tracker;

import com.Cicadellidea.doom_spear.capability.MobStunCapability;
import com.Cicadellidea.doom_spear.capability.MobStunCapabilityProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityStunHandler {
    @SubscribeEvent
    public void onMobTick(LivingEvent.LivingTickEvent event) {
//        if(event.getPhase() != TickEvent.Phase.END) return;

        Entity entity = event.getEntity();

//        if(entity.level().isClientSide()) return;

        if(entity instanceof Mob mob) {
            if (!entity.isRemoved()){
                MobStunCapability mobStunData;
                try {
                    mobStunData = mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).orElseThrow(RuntimeException::new);
                }
                catch (Exception e){
                    return;
                }
                var time = mobStunData.getStunTime();
                if (time <0){return;}
                if (time >1){
                    AttributeInstance gravity = mob.getAttribute((Attribute) ForgeMod.ENTITY_GRAVITY.get());
                    double d0 = gravity.getValue();
                    mobStunData.setStunTime(time-1);
                    var spd0 = mob.getDeltaMovement();
                    var spd1 = spd0.multiply(0.5,0.9,0.5).add(new Vec3(0,-d0,0));
                    mob.setDeltaMovement(spd1);

                    mob.move(MoverType.SELF,spd1);


                }
                else {
                    mobStunData.setStunTime(time-1);
                    mob.setNoAi(mobStunData.isNoAi());

                }
            }



        }
    }
}
