package com.Cicadellidea.doom_spear.entity;

import com.Cicadellidea.doom_spear.init.ModDamageTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.concurrent.ThreadLocalRandom;


import static com.Cicadellidea.doom_spear.init.ModDamageTypes.createCustomDamage;
import static com.Cicadellidea.doom_spear.init.ModEntity.RAVAGER_BULLET;


public class RavagerBullet extends AbstractArrow {
    private int life;


    public RavagerBullet(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super((EntityType<? extends AbstractArrow>) pEntityType, pLevel);
        this.life = 0;
        this.setNoGravity(true);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    public RavagerBullet(Player pPlayer, Level pLevel) {
        this(RAVAGER_BULLET.get(), pLevel);
        this.setOwner(pPlayer);
        float rh = 10;
        float rv = 5;
        float f = (float) (pPlayer.getXRot()+1-rv+2*rv*ThreadLocalRandom.current().nextDouble());
        float f1 = (float) (pPlayer.getYRot()-rh+2*rh*ThreadLocalRandom.current().nextDouble());
        float f2 = Mth.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = Mth.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -Mth.cos(-f * 0.017453292F);
        float f5 = Mth.sin(-f * 0.017453292F);
        double d0 = pPlayer.getX() - (double)f3 * 0.3;
        double d1 = pPlayer.getEyeY()-(double) f5 * 0.3;
        double d2 = pPlayer.getZ() - (double)f2 * 0.3;

        this.moveTo(d0, d1, d2, f1, f);
        Vec3 vec3 = new Vec3((double)(-f3), (double)Mth.clamp(-(f5 / f4), -5.0F, 5.0F), (double)(-f2));
        double d3 = vec3.length();
        var speed = 2;
        vec3 = vec3.multiply(speed / d3 , speed / d3 , speed / d3 );
        this.setDeltaMovement(vec3);
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 57.2957763671875));
        this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 57.2957763671875));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(2);
    }

    @Override
    public void tick(){
        super.tick();
        this.life += 1;
        if (this.life >50){
            this.discard();
        }
    }
    @Override
    protected void onHitBlock(BlockHitResult pResult){
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult){
        this.discard();
        if (pResult.getEntity() instanceof LivingEntity entity){
//            DamageSource source;
            var owner = this.getOwner();
            if ((owner instanceof LivingEntity entity1)){
                Level level = this.level();
                Vec3 tempMom = entity.getDeltaMovement();
                DamageSource source = createCustomDamage(level, ModDamageTypes.BULLET_DAMAGE, this, entity1);
                entity.hurt(source, (float) this.getBaseDamage());

                entity.setDeltaMovement(tempMom);
                entity.hurtMarked = true;
//                if (owner instanceof Player player){
////                    player.sendSystemMessage(Component.literal(String.valueOf(source.is(DamageTypeTags.BYPASSES_COOLDOWN))));
//                    player.sendSystemMessage(Component.literal(String.valueOf(source.type())));
//                }
            }
        }

    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public void remove(RemovalReason pReason){

        super.remove(pReason);

    }
}
