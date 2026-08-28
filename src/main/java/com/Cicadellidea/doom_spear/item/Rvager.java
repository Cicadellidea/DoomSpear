package com.Cicadellidea.doom_spear.item;

import com.Cicadellidea.doom_spear.entity.RavagerBullet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public class Rvager extends MachneGun{
    public Rvager(Properties pProperties) {
        super(pProperties);
    }


    public void shoot(Player player){
        Level level = player.level();
        RavagerBullet bullet = new RavagerBullet(player, level);
        level.addFreshEntity(bullet);
        level.playSound(null,player.getX(),player.getY(),player.getZ(),
                SoundEvents.ARMOR_EQUIP_CHAIN,
                SoundSource.PLAYERS,
                1.0F,   //音量
                1.5F);
    }
}
