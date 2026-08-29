package com.Cicadellidea.doom_spear.capability;

import net.minecraft.nbt.CompoundTag;
import java.util.UUID;

public class PlayerExtraDataImpl {
    private static final int MAX_DASH_CHARGE = 80;
    private static final int SINGLE_DASH_CHARGE = 40;
    private static final int SINGLE_SPEAR_CHARGE = 600;
    private static final int MAX_SPEAR_CHARGE = 1800;

    private static final int DASH_IMMUNE_TIME = 15;

    private int snowBallCount = 10;
    private UUID hookUuid;
    private boolean hooking;
    private int forward = 0;
    private int lrState = 0;
    private int dashImmune = 0;
    private int actionImmune = 0;
    private boolean falling = false;
    private int spearCharge = 0;
    private int dashCharge = MAX_DASH_CHARGE;
    private boolean holdingLeft = false;



    public int getSnowBallCount() {
        return snowBallCount;
    }
    public void setSnowBallCount(int val) {
        this.snowBallCount = val;
    }

    public UUID getHookUuid() {
        return hookUuid;
    }
    public void setHookUuid(UUID uuid) {
        this.hookUuid = uuid;
    }

    public boolean isHooking(){
        return hooking;
    }
    public void setHooking(boolean hooked) {
        this.hooking = hooked;
    }

    public CompoundTag saveNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("snow_ball_count", snowBallCount);
        if(hookUuid != null){
            tag.putUUID("hook_uuid", hookUuid);
        }
        tag.putBoolean("hooked", hooking);
        tag.putInt("forward",forward);
        tag.putInt("lrState",lrState);
        return tag;
    }

    public void loadNBT(CompoundTag tag) {
        snowBallCount = tag.getInt("snow_ball_count");
        if(tag.hasUUID("hook_uuid")){
            hookUuid = tag.getUUID("hook_uuid");
        }else{
            hookUuid = null;
        }
        hooking = tag.getBoolean("hooked");
        forward = tag.getInt("forward");
        lrState = tag.getInt("lrState");
    }
    public void clone(PlayerExtraDataImpl original){
        this.hookUuid = original.getHookUuid();
        this.snowBallCount = original.getSnowBallCount();
    };

    public int getForward() {
        return forward;
    }
    public void setForward(int forward){
        this.forward = forward;
    }

    public int getLrState() {
        return lrState;
    }
    public void setLrState(int lrState){
        this.lrState = lrState;
    }

    public int getDashImmune() {
        return dashImmune;
    }
    private void setDashImmune(int dashImmune){
        this.dashImmune = dashImmune;
    }
    public boolean isFalling() {
        return falling;
    }
    public void setFalling(boolean falling){
        this.falling = falling;
    }
    public int getActionImmune() {
        return actionImmune;
    }
    public void setActionImmune(int actionImmune){
        this.actionImmune = actionImmune;
    }

    public int getSpearCharge() {
        return spearCharge;
    }
    public void addSpearCharge(){
        this.spearCharge += 1;
    }
    public int getDashCharge() {
        return dashCharge;
    }
    public void setDashCharge(int dashCharge){
        this.dashCharge = dashCharge;
    }
    public boolean dash(){
        if (dashCharge>= SINGLE_DASH_CHARGE){
            dashCharge -= SINGLE_DASH_CHARGE;
            dashImmune += DASH_IMMUNE_TIME;
            return true;
        }
        else {
            return false;
        }
    }
    public void tick(){
        if (dashImmune>0){
            dashImmune -= 1;
        }
        if (actionImmune>0){
            actionImmune -= 1;
        }
        if (dashCharge<MAX_DASH_CHARGE){
            dashCharge += 1;
        }
        if (spearCharge < MAX_SPEAR_CHARGE){
            spearCharge += 1;
        }
    }
    public boolean isCharged(){
        return this.spearCharge>=SINGLE_SPEAR_CHARGE;
    }
    public void consumeCharge(){
        this.spearCharge -= SINGLE_SPEAR_CHARGE;
    }
    public void reCharge(){
        spearCharge += SINGLE_SPEAR_CHARGE;
        if (spearCharge>MAX_SPEAR_CHARGE){
            spearCharge = MAX_SPEAR_CHARGE;
        }

    }
    public void setSpearCharge(int charge){
        this.spearCharge = charge;
    }

    public int getSpearChargeCount(){
        return spearCharge/SINGLE_SPEAR_CHARGE;
    }

    public boolean isHoldingLeft() {
        return holdingLeft;
    }
    public void setHoldingLeft(boolean holdingLeft){
        this.holdingLeft = holdingLeft;
    }
}