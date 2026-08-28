package com.Cicadellidea.doom_spear.lib;

import com.Cicadellidea.doom_spear.capability.PlayerExtraDataProvider;
import com.Cicadellidea.doom_spear.entity.SpearHook;
import com.Cicadellidea.doom_spear.init.ModDamageTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.Cicadellidea.doom_spear.init.ModDamageTypes.createCustomDamage;

public class SpearActions {
    private static float BASE_SWEEP_RANGE = 1F;
    private static float SWEEP_ANGLE = 180F;
    private static float SWEEP_BASE_DMG = 16;
    private static float BASE_SLAM_RANGE = 7F;
    private static float SLAM_BASE_DMG = 5;
    public static boolean doSweeping(Player player){
        Level level = player.level();
//        if (level.isClientSide()) return false;
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getViewVector(1F).multiply(1,0,1).normalize();
        var reach = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
//        player.sendSystemMessage(Component.literal(String.valueOf(reach)));
        var range = BASE_SWEEP_RANGE +reach;
        AABB aabb = AABB.ofSize(
                eyePos,
                range*2,
                range*2,
                range*2
        );

        double cosThreshold = Math.cos(Math.toRadians(SWEEP_ANGLE * 0.5D));
        AtomicBoolean success = new AtomicBoolean(false);

        level.getEntitiesOfClass(LivingEntity.class, aabb, e -> e != player).forEach(target -> {
            Vec3 toTarget = target.position().subtract(eyePos).multiply(1,0,1).normalize();
            double dot = lookVec.dot(toTarget);
            double dist = target.position().subtract(eyePos).horizontalDistance();
//            player.sendSystemMessage(Component.literal(String.valueOf(target)));
//            player.sendSystemMessage(Component.literal(String.valueOf(dot)));
//            player.sendSystemMessage(Component.literal(String.valueOf(cosThreshold)));
//            player.sendSystemMessage(Component.literal(String.valueOf(dist)));
            var hsize = (target.getBoundingBox().getXsize()+target.getBoundingBox().getZsize())/2;

            if (dot >= cosThreshold && dist <= range+hsize) {

                float dmg = computeVanillaMeleeDamage(player, player.getMainHandItem(), target);
//                DamageSource source = player.damageSources().playerAttack(player);
                DamageSource source = createCustomDamage(level, ModDamageTypes.SPEAR_DAMAGE, null, player);

//                if(!target.level().isClientSide()){
//                    target.setLastHurtByPlayer(player);
//                }
                target.hurt(source, dmg+SWEEP_BASE_DMG);
                if (target instanceof Mob mob) {
                    FunctionLib.stunMob(mob,5);
                    success.set(true);
                }
            }
        });
        if (success.get()){
//            player.sendSystemMessage(Component.literal("Slam"));
            level.playSound(null,player.getX(),player.getY(),player.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP,
                    SoundSource.PLAYERS,
                    2.0F,   //音量
                    1.0F);
//            player.playSound(SoundEvents.ANVIL_PLACE,1f,1f);
        }

        return success.get();
    }
    public static float computeVanillaMeleeDamage(Player player, ItemStack weaponStack, LivingEntity target) {
        //1.基础攻击伤害（包含武器属性modifier）
        float baseDmg = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        //2.攻击冷却倍率
//        float cooldown = player.getAttackStrengthScale(0.5F);
//        baseDmg *= 0.2F + cooldown * cooldown * 0.8F;
        //3.附魔伤害（锋利、亡灵杀手、节肢杀手）
        float enchDmg = EnchantmentHelper.getDamageBonus(weaponStack, ((LivingEntity)target).getMobType());
//        enchDmg *= cooldown;
        //4.力量、虚弱药水效果
        if (player.hasEffect(MobEffects.DAMAGE_BOOST)) {
            baseDmg += 3.0F * (player.getEffect(MobEffects.DAMAGE_BOOST).getAmplifier() + 1);
        }
        if (player.hasEffect(MobEffects.WEAKNESS)) {
            baseDmg -= 4.0F * (player.getEffect(MobEffects.WEAKNESS).getAmplifier() + 1);
        }
        float total = baseDmg + enchDmg;
        return Math.max(total, 0.0F);
    }

    public static boolean doSlam(Player player){
        Level level = player.level();
//        if (level.isClientSide()) return false;
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getViewVector(1F).multiply(1,0,1).normalize();

        var reach = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
//        player.sendSystemMessage(Component.literal(String.valueOf(reach)));
        var range = BASE_SLAM_RANGE +reach;
        AABB aabb = AABB.ofSize(
                eyePos,
                range*2,
                range*2,
                range*2
        );


        AtomicBoolean success = new AtomicBoolean(false);

        level.getEntitiesOfClass(LivingEntity.class, aabb, e -> e != player).forEach(target -> {

            double dist = target.position().subtract(eyePos).horizontalDistance();
            var hsize = (target.getBoundingBox().getXsize()+target.getBoundingBox().getZsize())/2;
            if (dist <= range+hsize) {

                float dmg = computeVanillaMeleeDamage(player, player.getMainHandItem(), target);
                DamageSource source = createCustomDamage(level, ModDamageTypes.SPEAR_DAMAGE, null, player);
//                if(!target.level().isClientSide()){
//                    target.setLastHurtByPlayer(player);
//                }
                target.hurt(source, dmg+SLAM_BASE_DMG);
                if (target instanceof Mob mob) {
                    FunctionLib.stunMob(mob,100);
                    success.set(true);

                }
            }
        });
        if (success.get()){
//            player.sendSystemMessage(Component.literal("Slam"));
            level.playSound(null,player.getX(),player.getY(),player.getZ(),
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.PLAYERS,
                    2.0F,   //音量
                    1.0F);
//            player.playSound(SoundEvents.ANVIL_PLACE,1f,1f);
        }

        return success.get();
    }
    public static void ShootHook(Level level, Player player){
        if(!level.isClientSide()) {
            var playerData = player.getCapability(PlayerExtraDataProvider.PLAYER_EXTRA_DATA).orElseThrow(RuntimeException::new);
            boolean hooking = playerData.isHooking();
            ItemStack itemstack = player.getMainHandItem();
//            boolean b = itemstack.getItem() instanceof ChainSpearItem;

//            player.sendSystemMessage(Component.literal(String.valueOf(player.getAttackStrengthScale(0))));

            if (!hooking) {
                playerData.setHooking(true);
                SpearHook hook = new SpearHook(player, level);
                level.addFreshEntity(hook);

            }
            else{
//                playerData.setHooking(false);
            }
        }

    }
}
