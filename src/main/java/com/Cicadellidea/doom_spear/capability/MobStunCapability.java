package com.Cicadellidea.doom_spear.capability;

import net.minecraft.nbt.CompoundTag;

public class MobStunCapability {
    private int stunTime = 0;
    private boolean noAi = false;

    public int getStunTime() {
        return stunTime;
    }
    public void setStunTime(int stunTime){
        this.stunTime = stunTime;
    }
    public CompoundTag saveNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("stun_time",stunTime);
        tag.putBoolean("no_ai",noAi);
        return tag;
    }
    public void loadNBT(CompoundTag tag) {
        stunTime = tag.getInt("stun_time");
        noAi = tag.getBoolean("no_ai");
    }

    public boolean isNoAi() {
        return noAi;
    }

    public void  setNoAi(boolean noAi){
        this.noAi = noAi;
    }

    public void countDown(){
        if (this.stunTime>0){
            this.stunTime -= 1;
        }
    }
}
