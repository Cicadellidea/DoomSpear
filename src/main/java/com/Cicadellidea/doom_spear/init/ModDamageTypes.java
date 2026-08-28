package com.Cicadellidea.doom_spear.init;

import com.Cicadellidea.doom_spear.DoomSpear;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ModDamageTypes
{
    //把your_modid换成你的mod id
    public static final ResourceKey<DamageType> BULLET_DAMAGE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation(DoomSpear.MODID, "bullet_damage")
    );

    public static final ResourceKey<DamageType> SPEAR_DAMAGE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation(DoomSpear.MODID, "spear_damage")
    );

    public static DamageSource createCustomDamage(
            Level level,
            ResourceKey<DamageType> damageKey,
            @Nullable Entity directEntity,
            @Nullable LivingEntity attacker
    )
    {
        Holder<DamageType> holder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(damageKey);
        return new DamageSource(holder, directEntity, attacker);
    }
}
