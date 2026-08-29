package com.Cicadellidea.doom_spear.entity;

import com.Cicadellidea.doom_spear.DoomSpear;
import com.Cicadellidea.doom_spear.capability.MobStunCapability;
import com.Cicadellidea.doom_spear.capability.MobStunCapabilityProvider;
import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.client.key.KeyBindings;
import com.Cicadellidea.doom_spear.client.render.ViewSmoother;
import com.Cicadellidea.doom_spear.lib.FunctionLib;
import com.Cicadellidea.doom_spear.network.SyncPlayerHookingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkDirection;

import javax.annotation.Nullable;

import static com.Cicadellidea.doom_spear.init.ModEntity.SPEAR_HOOK;

public class SpearHook extends AbstractArrow {
    private boolean hooked;
    private int life;
    @Nullable
    private Entity hookedIn;
    public static float speed = 8;
//    private int lastForward = 0;
//    private int lastLrstate = 0;



    public SpearHook(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super((EntityType<? extends AbstractArrow>) pEntityType, pLevel);
        this.hooked = false;
        this.life = 0;
        this.setNoGravity(true);
    }

    public SpearHook(Player pPlayer, Level pLevel) {
        this(SPEAR_HOOK.get(), pLevel);
        this.setOwner(pPlayer);
        float f = pPlayer.getXRot();
        float f1 = pPlayer.getYRot();
        float f2 = Mth.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = Mth.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -Mth.cos(-f * 0.017453292F);
        float f5 = Mth.sin(-f * 0.017453292F);
        double d0 = pPlayer.getX() - (double)f3 * 0.0;
        double d1 = pPlayer.getEyeY();
        double d2 = pPlayer.getZ() - (double)f2 * 0.0;
        this.moveTo(d0, d1, d2, f1, f);
        Vec3 vec3 = new Vec3((double)(-f3), (double)Mth.clamp(-(f5 / f4), -5.0F, 5.0F), (double)(-f2));
        double d3 = vec3.length();
//        var speed = 5;
        vec3 = vec3.multiply(speed / d3 , speed / d3 , speed / d3 );
        this.setDeltaMovement(vec3);
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 57.2957763671875));
        this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 57.2957763671875));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(0);
    }



    @Override
    public void tick(){
        super.tick();
//        if (this.level().isClientSide()){
//        }

        if (!this.level().isClientSide()){
//        if(false){
            Player player = this.getPlayerOwner();
            if (player == null) {
                this.discard();
                return;
            }
            if (this.shouldStopHooking(player)){
                return;
            }
            else{
                if(true) {
                    ++this.life;
                    if(this.life >= 400) {
                        this.discard();
                        return;
                    }
                }
            }

            if (!this.isHooked()) {

            } else {
                player.fallDistance = 0;
                if (true) {

                    AttributeInstance gravity = player.getAttribute((Attribute) ForgeMod.ENTITY_GRAVITY.get());
                    AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
                    double playerSpeed = movementSpeed.getValue();
                    double d0 = gravity.getValue();
                    var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);

//                if (!player.isNoGravity()) {
//                    if(!player.getAbilities().flying){
//                        player.setDeltaMovement(player.getDeltaMovement().add(0.0, d0 , 0.0));
//                        player.hurtMarked = true;
//                    }
//                }
                    var playerPos = player.getEyePosition();
                    var hookPos = this.position();
                    var deltaPos = hookPos.subtract(playerPos);
                    int hMove = playerData.getLrState();
                    int vMove = playerData.getForward();
                    Vec3 lookVector = player.getViewVector(1F);

                    double spd = 1;
                    if(this.hookedIn != null) {
                        if(this.hookedIn instanceof Mob mob) {
                            if (!this.hookedIn.isRemoved()){
                                MobStunCapability mobStunData;
                                try {
                                    mobStunData = mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).orElseThrow(RuntimeException::new);
                                    if(mobStunData.getStunTime() > 5) {
                                        mobStunData.setStunTime(mobStunData.getStunTime() + 1);
                                    }
                                }
                                catch (Exception e){}

                            }
                        }
                    }
                    Vec3 motion = deltaPos.normalize().scale(spd);
                    if(hMove == 0 && vMove == 0) {
                        motion = deltaPos.normalize().scale(spd);
                        if(motion.y < 0) {
                            if(deltaPos.y > -2 && deltaPos.horizontalDistance() > 2) {
                                motion = new Vec3(motion.x, 0, motion.z);
                            }
                        }
                    } else {
                        Vec3 hVector = new Vec3(hMove * deltaPos.z, 0, -hMove * deltaPos.x).normalize().scale(spd);
                        Vec3 vVector = Vec3.ZERO;
                        Vec3 moveCompensate = Vec3.ZERO;
                        if(hMove != 0) {
                            moveCompensate = new Vec3(deltaPos.x, 0, deltaPos.z).scale((playerSpeed / 100)+0.02);
                        }
                        if(vMove != 0) {
                            var deltaH = deltaPos.horizontalDistance();
                            var propX = deltaPos.x / deltaH;
                            var propZ = deltaPos.z / deltaH;
                            var deltaV = deltaPos.y;


                            if(deltaH > 0.5 || deltaPos.y * vMove > 0) {
                                vVector = new Vec3(-deltaV * propX * vMove, deltaH * vMove * 0.5, -deltaV * propZ * vMove).normalize().scale(spd);

                            } else {
                                vVector = new Vec3(lookVector.x, 0, lookVector.z).normalize().scale(0.3).add(0, 0.05, 0);
                                this.retrive();
                                return;
                            }
                            if (deltaPos.y * vMove<-5){
                                vVector=vVector.multiply(1,0.1,1).normalize().scale(spd);
                            }
//                        moveCompensate = moveCompensate.add(new Vec3(lookVector.x,0,lookVector.z).normalize().scale(playerSpeed)).scale(vMove);

                        }

                        motion = hVector.add(vVector).add(moveCompensate);

                    }

                    player.setDeltaMovement(motion.add(0, Mth.abs((float) d0), 0));
                    player.hurtMarked = true;
                }
    //
                if (this.hookedIn != null) {
                    if (!this.hookedIn.isRemoved() && this.hookedIn.level().dimension() == this.level().dimension() && this.distanceToSqr(this.hookedIn) < 100) {
                        if (!this.level().isClientSide) {
                            Vec3 pos =this.getPosition(0);
                            this.xOld = pos.x;
                            this.yOld = pos.y;
                            this.zOld = pos.z;

                            this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8), this.hookedIn.getZ());
                            ////                        this.setDeltaMovement(this.getDeltaMovement().scale(0.8));
                            if (this.distanceTo(player) < 2.5+this.hookedIn.getBoundingBox().getSize()/2){
                                this.retrive();
                            }


                        }
                    } else {
                        this.hookedIn= ((Entity)null);
                        this.hooked = false;
                        this.discard();
                        return;
                    }

                }
                else {
                    if (this.distanceToSqr(player) < 5){
                        this.retrive();
//                        player.sendSystemMessage(Component.literal("近距离收回"));

                    }
                }

            }
        }
        if (this.level().isClientSide()){
            LocalPlayer player = Minecraft.getInstance().player;
            if (this.getOwner() == player){
//                if (this.isHooked()){
                if (ViewSmoother.isHooked){
                    if (KeyBindings.hookKey.isDown()){
                        Vec3 eye = player.getEyePosition();
                        Vec3 target = this.getEyePosition();

                        Vec3 delta = target.subtract(eye);
//                        player.lookAt(EntityAnchorArgument.Anchor.EYES,target);
                        float yaw = (float) (Mth.wrapDegrees(Mth.atan2(delta.z, delta.x) * Mth.RAD_TO_DEG-90));
                        float pitch = (float) (-Mth.atan2(delta.y, delta.horizontalDistance()) * Mth.RAD_TO_DEG);
                        var actualYaw = player.getYRot();
                        int lrState = 0;
                        if (player.input.left){
                            lrState += 1;
                        }
                        if (player.input.right){
                            lrState -= 1;
                        }
                        var err = Mth.wrapDegrees((yaw-actualYaw-ViewSmoother.angularVelocity*lrState*0F))*lrState;
                        var d = err - ViewSmoother.prevErr;
                        var i = ViewSmoother.integalErr;
                        ViewSmoother.integalErr += err;
                        ViewSmoother.prevErr = err;
                        var kp = 0.5;
                        var kd = -0.1;
                        var ki = 0.1;

//                        ViewSmoother.angularVelocity += (float) (err*kp+d*kd+i*ki);
                        ViewSmoother.angularVelocity = 7.1f+(float) (err*kp+d*kd+i*ki);
//                        player.sendSystemMessage(Component.literal(String.valueOf(ViewSmoother.angularVelocity)));
//                        ViewSmoother.targetXRot = pitch;
//                        var prevYRot = ViewSmoother.targetYRot;
//                        ViewSmoother.targetYRot = yaw;


                    }
                }
            }

        }


    }

    private void retrive() {

        this.discard();
        Player player = this.getPlayerOwner();
//        player.setDeltaMovement(player.getDeltaMovement().scale(0.2));


        if (this.hookedIn != null) {
            player.setDeltaMovement(Vec3.ZERO);
            if(!this.hookedIn.level().isClientSide()){
                if (this.hookedIn instanceof LivingEntity target){
                    target.setLastHurtByPlayer(player);
                    DamageSource source = player.damageSources().playerAttack(player);
                    this.hookedIn.hurt(source,6);

                    if (target instanceof Mob mob){
//                        if (!this.hookedIn.isRemoved()){
//                            var mobStunData = mob.getCapability(MobStunCapabilityProvider.MOB_STUN_DATA).orElseThrow(RuntimeException::new);
//                        }

//                        player.sendSystemMessage(Component.literal(String.valueOf(mob.getBoundingBox().getSize())));
//                        player.sendSystemMessage(Component.literal(String.valueOf(this.distanceTo(player))));

                        FunctionLib.execution(mob,player);
                        FunctionLib.stunMob(mob,5);

                    }
                }
            }
        }
        else {
            player.setDeltaMovement(player.getDeltaMovement().scale(0.3));
            if (this.position().y>player.position().y+1){
                player.setDeltaMovement(player.getDeltaMovement().add(0,0.8,0));
            }
        }
        player.hurtMarked = true;

    }

    @Nullable
    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player)entity : null;
    }


    //    @Override
//    protected void defineSynchedData() {
//
//    }
    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    public boolean isHooked() {
        return hooked;
    }

    public void gettingHooked(Entity target) {
        this.hooked = true;
        Player player = this.getPlayerOwner();
        if (player != null) {
            var playerPos = player.getEyePosition();
            var hookPos = this.position();

            Vec3 stretching = hookPos.subtract(playerPos).normalize();

            player.setDeltaMovement(player.getDeltaMovement().add(new Vec3(stretching.x,0.2,stretching.z).scale(2)));
            player.hurtMarked = true;
            if (player instanceof ServerPlayer player1){
                DoomSpear.CHANNEL.sendTo(new SyncPlayerHookingPacket(true,target.getId()), player1.connection.connection, NetworkDirection.PLAY_TO_CLIENT);

            }
        }



    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = 64.0;
        return pDistance < 4096.0;
    }

    private boolean shouldStopHooking(Player pPlayer) {
//        ItemStack itemstack = pPlayer.getMainHandItem();
//        ItemStack itemstack1 = pPlayer.getOffhandItem();
//        boolean flag = itemstack.getItem() instanceof ChainSpearItem;
//        boolean flag1 = itemstack1.getItem() instanceof ChainSpearItem;
        var playerData = pPlayer.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);

        if (!pPlayer.isRemoved() && pPlayer.isAlive() && (FunctionLib.hasChainSpear(pPlayer)) && !(this.distanceToSqr(pPlayer) > 2400.0) && playerData.isHooking()) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
//        super.onHitBlock(pResult);
        Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05000000074505806);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.playSound(SoundEvents.CHAIN_PLACE, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.setSoundEvent(SoundEvents.ARROW_HIT);
        if (!this.isHooked()){
            this.gettingHooked(this);
        }

    }

    protected void onHitEntity(EntityHitResult pResult) {
//        super.onHitEntity(pResult);
        this.playSound(SoundEvents.CHAIN_PLACE, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.setSoundEvent(SoundEvents.ARROW_HIT);

        if (!this.isHooked()){
            this.hookedIn = pResult.getEntity();
            this.setDeltaMovement(this.getDeltaMovement().scale(0.01));
            this.gettingHooked(this.hookedIn);
        }
    }


    protected boolean canHitEntity(Entity p_37135_) {
        return super.canHitEntity(p_37135_) ;

    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

//    @Override
//    protected void updateRotation() {
//        if (!this.hooked){
//            Vec3 vec3 = this.getDeltaMovement();
//            double d0 = vec3.horizontalDistance();
//            this.setXRot((float)(Mth.atan2(vec3.y, d0) * 57.2957763671875));
//            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 57.2957763671875));
//        }
//
//    }



    @Override
    public void remove(RemovalReason pReason){

        Player player = this.getPlayerOwner();
        if (player != null) {
            var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            playerData.setHooking(false);
//            player.sendSystemMessage(Component.literal(String.valueOf(this.life)));
//            player.sendSystemMessage(Component.literal(String.valueOf(this.getYRot())));
//            player.sendSystemMessage(Component.literal(String.valueOf(this.distanceToSqr(player))));
            if (player instanceof ServerPlayer player1){
                DoomSpear.CHANNEL.sendTo(new SyncPlayerHookingPacket(false,-1), player1.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
//                player.getCooldowns().addCooldown(ChainSpearItem,20);
            }
        }
//        if (this.level().isClientSide){
//            LocalPlayer localPlayer = Minecraft.getInstance().player;
//            if (player == localPlayer){
//                ViewSmoother.isHooked = false;
//                player.sendSystemMessage(Component.literal(String.valueOf(ViewSmoother.isHooked)));
//            }
//        }
        super.remove(pReason);
//        this.getOwner().setNoGravity(false);

    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

//    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
//    }


}


